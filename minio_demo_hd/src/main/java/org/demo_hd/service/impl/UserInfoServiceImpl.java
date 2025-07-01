package org.demo_hd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.demo_hd.entity.UserContract;
import org.demo_hd.entity.UserImage;
import org.demo_hd.entity.UserInfo;
import org.demo_hd.mapper.UserContractMapper;
import org.demo_hd.mapper.UserImageMapper;
import org.demo_hd.service.UserImageService;
import org.demo_hd.service.UserInfoService;
import org.demo_hd.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
* @author tang_
* @description 针对表【t_user_info】的数据库操作Service实现
* @createDate 2025-06-28 19:38:31
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserImageService userImageService;
    @Resource
    private UserContractMapper userContractMapper;
    @Resource
    private MinioClient minioClient;

    @Autowired
    private UserImageMapper userImageMapper;

    @Override
    public UserInfo getUserById(Integer id){
        return userInfoMapper.selectUserById(id);
    }

    @Override
    public List<UserInfo> getUseList(){
        return userInfoMapper.selectUserList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delUserById(Integer id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        UserInfo userInfo = userInfoMapper.selectUserById(id);

        //删除用户
        int delUser = userInfoMapper.deleteById(id);

        //删除头像
        LambdaQueryWrapper<UserImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserImage::getUid, id);
        int delImage = userImageMapper.delete(wrapper);

        //删除合同
        LambdaQueryWrapper<UserContract> contractWrapper = new LambdaQueryWrapper<>();
        contractWrapper.eq(UserContract::getUid, id);
        int delContract = userContractMapper.delete(contractWrapper);

        if (null != userInfo.getUserImageDO()) {
            //删除MinIO服务器上的文件 (删除头像)
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(userInfo.getUserImageDO().getBucket())
                    .object(userInfo.getUserImageDO().getObject())
                    .build());
        }

        if (null != userInfo.getUserContractDO()) {
            //删除MinIO服务器上的文件 (删除合同)
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(userInfo.getUserContractDO().getBucket())
                    .object(userInfo.getUserContractDO().getObject())
                    .build());
        }

        return delUser >= 1;
    }

}





