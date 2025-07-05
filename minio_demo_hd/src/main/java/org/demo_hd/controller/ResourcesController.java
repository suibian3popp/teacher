package org.demo_hd.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.demo_hd.config.MinIOInfo;
import org.demo_hd.dto.ResourcesQueryDTO;
import org.demo_hd.entity.Resources;
import org.demo_hd.result.R;
import org.demo_hd.service.ResourcesService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api")
public class ResourcesController {
    @Resource
    private ResourcesService resourcesService;

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinIOInfo minIOInfo;

    //带筛选和分页的资源列表接口 以及搜索
    @GetMapping("/resource/list/{ownerId}")
    public R resourcesList(@PathVariable(value="ownerId") Integer ownerId,
            @ModelAttribute ResourcesQueryDTO queryDTO )  {
        //初始化分页参数（默认第一页 一页10条）
        if (queryDTO.getPage()==null) queryDTO.setPage(1);
        if (queryDTO.getPageSize()==null) queryDTO.setPageSize(10);

        //调用service查询
        Page<Resources> pageResult=resourcesService.viewList(ownerId,queryDTO);

        //封装返回结果
        //构建返回数据的Map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", pageResult.getTotal());
        resultMap.put("records", pageResult.getRecords());
        return R.OK(resultMap);
    }

    //上传资源
    @PostMapping("/resource/upload")
    public R uploadResources(@RequestParam("ownerId") Integer ownerId,
                             @RequestParam("file") MultipartFile file,
//                             @RequestParam("name") String name,
                             @RequestParam("type") String type,
                             @RequestParam("difficulty") String difficulty,
                             @RequestParam("permission") String permission) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        if (file == null || file.isEmpty()) {
            return R.FAIL(); //提前校验
        }

        //使用用户ID作为存储桶名称
        String bucketName = String.valueOf(ownerId);

        //获取文件原始名称和后缀
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
//        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String originalFileNameReal = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        //检查存储桶是否存在，不存在则创建
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            System.out.println("创建存储桶: " + bucketName);
        }

        //查询存储桶中已有的、基础名匹配的文件数量
        int existingCount = 0;
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(originalFilename.replaceAll("\\..*$", ""))//按基础名前缀查询
                .build());
        for (Result<Item> result : results) {
//            Item item = result.get();
//            String itemName = item.objectName();
//            //简单正则匹配，判断是否是基础名 + (数字) + 后缀的形式 或者 基础名 + 后缀的形式
//            Pattern pattern = Pattern.compile(originalFileNameReal + "(\\(\\d+\\))?" + Pattern.quote(suffix));
//            Matcher matcher = pattern.matcher(itemName);
//            if (matcher.matches()) {
//                existingCount++;
//            }
            existingCount++;
        }

        //生成目标文件名
        String targetFileName;
        if (existingCount == 0) {
            targetFileName = originalFilename;
        } else {
            targetFileName = originalFilename.replaceAll("\\..*$", "") + "(" + existingCount + ")" + getFileSuffix(originalFilename);
        }

        //上传文件到MinIO
        ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(targetFileName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());
        System.out.println("MinIO上传成功: " + response);

        //保存资源信息到数据库
        resourcesService.saveResources(
                ownerId,
                bucketName,
                targetFileName,
                file,
                targetFileName,
                type,
                difficulty,
                permission);

        return R.OK();
    }
//    //检验上传的文件名
//    @GetMapping("/resource/checkName")
//    public R checkResourceName(@RequestParam("ownerId") Integer ownerId,
//                               @RequestParam("name") String name) {
//        boolean exists = resourcesService.checkResourceNameExists(ownerId, name);
//        return R.OK(exists);
//    }

    //提取文件后缀（仅用于命名，不关联资源类型）
    private String getFileSuffix(String filename) {
        int lastIndex = filename.lastIndexOf(".");
        return lastIndex == -1 ? "" : filename.substring(lastIndex);
    }

    //下载资源
    @GetMapping(value="resource/download/{resourceId}")
    public void downloadResource(@PathVariable(value="resourceId") Integer resourceId, HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Resources resource=resourcesService.getById(resourceId);
        String bucket = resource.getBucket();
        String object = resource.getObjectKey();

        //要想让浏览器弹出下载框，后端要设置一下响应头信息
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(object, StandardCharsets.UTF_8));

        GetObjectResponse getObjectResponse = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build());

        getObjectResponse.transferTo(response.getOutputStream());

    }

    //资源预览
    @GetMapping("/resource/preview/{resourceId}")
    public R previewResource(@PathVariable("resourceId") Integer resourceId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //获取资源信息
        Resources resource = resourcesService.getById(resourceId);
        if(resource==null) return R.FAIL();

        String bucket = resource.getBucket();
        String object = resource.getObjectKey();
        String fileName=resource.getName();

        //获取文件后缀名
        String suffix = getFileSuffix(fileName);

        //构建预览链接
        String previewUrl;
        //根据后缀判断文件类型 生成不同的文件预览方式
        if(isImageType(suffix)){
            //图片类型直接生成预览链接
            previewUrl=minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                    .builder()
                    .bucket(bucket)
                    .object(object)
                    .method(Method.GET)
                    .expiry(60*60)//链接有效期
                    .build());
        }else if(isTextType(suffix)){
            previewUrl=minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                    .builder()
                    .bucket(bucket)
                    .object(object)
                    .method(Method.GET)
                    .build());
        }else if(isPdfType(suffix)){
            previewUrl=minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                    .builder()
                    .bucket(bucket)
                    .object(object)
                    .method(Method.GET)
                    .build());
        }else{
            //其他类型不支持预览 返回下载链接
            previewUrl=minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(object)
                    .method(Method.GET)
                    .expiry(60*60)
                    .build());
        }
        //封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("previewUrl", previewUrl);
        result.put("fileName", fileName);
        result.put("fileType", suffix);

        return R.OK(result);
    }

    //删除
    @DeleteMapping("/resource/delete/{resourceId}")
    public R delResource(@PathVariable(value = "resourceId") Integer resourceId) {
        try{
            boolean del = resourcesService.delResourceById(resourceId);
            return del?R.OK():R.FAIL();
        }catch (Exception e){
            e.printStackTrace();
            return R.FAIL();
        }

    }

    //判断是否为图片类型
    private boolean isImageType(String suffix) {
        suffix=suffix.replace(".","").toLowerCase();
        return "jpg,jpeg,png,gif,bmp,webp".contains(suffix);
    }

    //判断是否为文本类型
    private boolean isTextType(String suffix) {
        suffix=suffix.replace(".","").toLowerCase();
        return "txt,md,json,xml,html,css,js".contains(suffix);
    }

    //判断是否为PDF类型
    private boolean isPdfType(String suffix) {
        suffix=suffix.replace(".","").toLowerCase();
        return "pdf".equals(suffix);
    }




}










