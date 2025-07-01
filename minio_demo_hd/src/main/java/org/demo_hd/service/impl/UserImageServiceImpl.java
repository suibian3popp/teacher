package org.demo_hd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import kotlin.jvm.internal.Lambda;
import org.demo_hd.entity.UserImage;
import org.demo_hd.service.UserImageService;
import org.demo_hd.mapper.UserImageMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Wrapper;

/**
* @author tang_
* @description 针对表【t_user_image】的数据库操作Service实现
* @createDate 2025-06-28 19:43:57
*/
@Service
public class UserImageServiceImpl extends ServiceImpl<UserImageMapper, UserImage>
    implements UserImageService{

    @Resource
    private UserImageMapper userImageMapper;

    @Override
    public boolean saveOrUpdateUserImage(Integer uid,String bucket,String object) {
        LambdaQueryWrapper<UserImage> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(UserImage::getUid,uid);
        boolean flag=false;
        long count=userImageMapper.selectCount(wrapper);

        UserImage userImage=new UserImage();
        userImage.setUid(uid);
        userImage.setBucket(bucket);
        userImage.setObject(object);

        if(count<=0){
            userImage.setCreateTime(new Date(System.currentTimeMillis()));
            flag=userImageMapper.insert(userImage)>=1;
        }else{
            userImage.setUpdateTime(new Date(System.currentTimeMillis()));
            flag=userImageMapper.update(userImage,wrapper)>=1;
        }

        return false;
    }


}




