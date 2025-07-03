package org.demo_hd.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.demo_hd.config.MinIOInfo;
import org.demo_hd.dto.ResourcesQueryDTO;
import org.demo_hd.entity.Resources;
import org.demo_hd.entity.UserInfo;
import org.demo_hd.result.R;
import org.demo_hd.service.ResourcesService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class ResourcesController {
    @Resource
    private ResourcesService resourcesService;

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinIOInfo minIOInfo;

    //带筛选和分页的资源列表接口
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
            System.out.println("创建存储桶: "+bucketName);
        }

        //查询存储桶中已有的、基础名匹配的文件数量
        int existingCount = 0;
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(originalFilename.replaceAll("\\..*$",""))//按基础名前缀查询
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
        System.out.println("MinIO上传成功: "+response);

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

}










