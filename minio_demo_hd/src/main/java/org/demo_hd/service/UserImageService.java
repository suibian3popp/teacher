package org.demo_hd.service;

import org.demo_hd.entity.UserImage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author tang_
* @description 针对表【t_user_image】的数据库操作Service
* @createDate 2025-06-28 19:43:57
*/
public interface UserImageService extends IService<UserImage> {

    boolean saveOrUpdateUserImage(Integer uid,String bucket,String object);
}
