package org.demo_hd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.demo_hd.dto.ResourcesQueryDTO;
import org.demo_hd.entity.Resources;
import org.demo_hd.service.ResourcesService;
import org.demo_hd.mapper.ResourcesMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources>
    implements ResourcesService{

    @Resource
    private MinioClient minioClient;

    @Resource
    private ResourcesMapper resourcesMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delResourceById(Integer resourceId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Resources resource = resourcesMapper.selectById(resourceId);

        int delResource = resourcesMapper.deleteById(resourceId);

        if (null != resource) {
            //删除MinIO服务器上的文件 (删除头像)
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(resource.getBucket())
                    .object(resource.getObjectKey())
                    .build());
        }
        return (delResource >= 1);
    }

    @Override
    public void saveResources(Integer ownerId,
                              String bucket,
                              String object,
                              MultipartFile file,
                              String name,
                              String type,
                              String difficulty,
                              String permission) {
        //构建Resources实体对象
        Resources resources = new Resources();
        resources.setOwnerId(ownerId);
        resources.setBucket(bucket);
        resources.setObjectKey(object);
        resources.setName(name);
        resources.setType(type);
        resources.setPermission(permission);
        resources.setFileSize(file.getSize());
        resources.setDifficulty(difficulty);

        baseMapper.insert(resources);
    }

    @Override
    public boolean checkResourceNameExists(Integer ownerId, String name) {
        QueryWrapper<Resources> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id", ownerId)
                .eq("name", name);
        return baseMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public Page<Resources> viewList(Integer ownerId, ResourcesQueryDTO queryDTO) {

        //构建查询条件
        LambdaQueryWrapper<Resources> queryWrapper = new LambdaQueryWrapper<>();
        //必选条件 ownerId
        queryWrapper.eq(Resources::getOwnerId, ownerId);
        //可选条件：资源类型 访问权限 资源难度
        if(queryDTO.getType() != null&&!queryDTO.getType().isEmpty()){
            queryWrapper.eq(Resources::getType, queryDTO.getType());
        }
        if(queryDTO.getPermission() != null&&!queryDTO.getPermission().isEmpty()){
            queryWrapper.eq(Resources::getPermission, queryDTO.getPermission());
        }
        if(queryDTO.getDifficulty() != null&&!queryDTO.getDifficulty().isEmpty()){
            queryWrapper.eq(Resources::getDifficulty, queryDTO.getDifficulty());
        }

        //排序逻辑
        if(queryDTO.getSortBy() != null&&!queryDTO.getSortBy().isEmpty()){
            String sortBy=queryDTO.getSortBy();
            String sortDir=queryDTO.getSortDir();

            //根据sortBy动态指定排序字段
            switch (sortBy){
                case"uploadTime":
                    if("asc".equals(sortDir)){
                        queryWrapper.orderByAsc(Resources::getUploadTime);
                    }else{
                        queryWrapper.orderByDesc(Resources::getUploadTime);
                    }
                    break;
                case "name":
                    if("asc".equals(sortDir)){
                        queryWrapper.orderByAsc(Resources::getName);
                    }else {
                        queryWrapper.orderByDesc(Resources::getName);
                    }
                    break;
                case "fileSize":
                    if("asc".equals(sortDir)){
                        queryWrapper.orderByAsc(Resources::getFileSize);
                    }else {
                        queryWrapper.orderByDesc(Resources::getFileSize);
                    }
                    break;
                default:
                    //默认按上传时间排序
                    queryWrapper.orderByDesc(Resources::getUploadTime);
            }
        }else{
            queryWrapper.orderByDesc(Resources::getUploadTime);
        }

//        // 打印查询条件
//        System.out.println("Query Wrapper: " + queryWrapper.getSqlSegment());
//        System.out.println("Parameters: " + queryWrapper.getParamNameValuePairs());

        //执行分页查询
        return resourcesMapper.selectPage(
                new Page<>(queryDTO.getPage(), queryDTO.getPageSize()), queryWrapper
        );
    }
}




