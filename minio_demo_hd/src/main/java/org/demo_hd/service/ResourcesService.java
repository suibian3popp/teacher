package org.demo_hd.service;

import io.minio.errors.*;
import org.demo_hd.entity.Resources;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.demo_hd.dto.ResourcesQueryDTO;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
