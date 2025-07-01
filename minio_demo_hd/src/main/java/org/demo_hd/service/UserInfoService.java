package org.demo_hd.service;

import io.minio.errors.*;
import org.demo_hd.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
* @author tang_
* @description 针对表【t_user_info】的数据库操作Service
* @createDate 2025-06-28 19:38:31
*/
public interface UserInfoService extends IService<UserInfo> {
    List<UserInfo> getUseList();

    UserInfo getUserById(Integer id);

    boolean delUserById(Integer id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

}
