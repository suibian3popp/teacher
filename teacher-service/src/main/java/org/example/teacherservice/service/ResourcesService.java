package org.example.teacherservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.jdi.InternalException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.XmlParserException;
import org.example.teacherservice.dto.ResourcesQueryDTO;
import org.example.teacherservice.entity.Resources;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ResourcesService extends IService<Resources> {

    boolean delResourceById(Integer resourceId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


    void saveResources(Integer ownerId,
                       String bucket,
                       String object,
                       MultipartFile file,
                       String name,
                       String type,
                       String difficulty,
                       String permission);

    boolean checkResourceNameExists(Integer ownerId, String name);

    //带筛选和分页的查询方法
    Page<Resources> viewList(Integer ownerId, ResourcesQueryDTO queryDTO);}
