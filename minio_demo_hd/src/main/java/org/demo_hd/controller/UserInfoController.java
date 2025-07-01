package org.demo_hd.controller;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.demo_hd.config.MinIOInfo;
import org.demo_hd.entity.UserInfo;
import org.demo_hd.result.R;
import org.demo_hd.service.UserContractService;
import org.demo_hd.service.UserImageService;
import org.demo_hd.service.UserInfoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinIOInfo minIOInfo;

    @Resource
    private UserImageService userImageService;

    @Resource
    private UserContractService userContractService;

    //获取用户列表
    @GetMapping(value="/users")
    public R users(){
        List<UserInfo> userInfoList = userInfoService.getUseList();
        return R.OK(userInfoList);
    }

    //上传头像
    @PostMapping(value="/user/image")
    public R images(MultipartFile file,@RequestParam("id") Integer id) throws Exception {
        if (file == null || file.isEmpty()) {
            return R.FAIL(); // 提前校验
        }

        //文件后缀
        String subfix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String object=id+subfix;
        //上传文件
        ObjectWriteResponse objectWriteResponse=minioClient.putObject(PutObjectArgs.builder()
                .bucket(minIOInfo.getBucket())
                .object(object)
                .stream(file.getInputStream(),file.getSize(),-1)
                .build()
        );

        System.out.println("MinIO上传相应："+objectWriteResponse);

        userImageService.saveOrUpdateUserImage(id,minIOInfo.getBucket(),object);

        return R.OK();
    }

    //上传文件
    @PostMapping(value="/user/contract")
    public R contracts(MultipartFile file,@RequestParam("id") Integer id) throws Exception {
        //文件后缀
        String subfix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String object=id+subfix;

        //上传文件
        ObjectWriteResponse objectWriteResponse=
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(minIOInfo.getBucket())
                        .object(object)
                        .stream(file.getInputStream(),file.getSize(),-1)
                        .build()
                );

        System.out.println(objectWriteResponse);
        userContractService.saveOrUpdateUserContract(id,minIOInfo.getBucket(),object);

        return R.OK();
    }

    //获取用户信息
    @GetMapping(value="/user/{id}")
    public R user(@PathVariable(value = "id") Integer id){
        UserInfo userInfo = userInfoService.getUserById(id);
        return R.OK(userInfo);
    }

    //接受用户信息
    @PutMapping(value="/user")
    public R updateUser(UserInfo userInfo) throws Exception {
        boolean update = userInfoService.updateById(userInfo);
        return update ? R.OK() : R.FAIL();
    }

    //下载
    @GetMapping(value="/download/{id}")
    public void download(@PathVariable(value = "id") Integer id, HttpServletResponse response) throws Exception {
        UserInfo userInfo = userInfoService.getUserById(id);
        String bucket = userInfo.getUserContractDO().getBucket();
        String object = userInfo.getUserContractDO().getObject();

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

    //删除
    @DeleteMapping("/user/{id}")
    public R delUser(@PathVariable(value = "id") Integer id) {
        try{
            boolean del = userInfoService.delUserById(id);
            return del?R.OK():R.FAIL();
        }catch (Exception e){
            e.printStackTrace();
            return R.FAIL();
        }

    }

}