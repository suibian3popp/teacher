package org.demo_hd.service;

import org.demo_hd.entity.UserContract;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author tang_
* @description 针对表【t_user_contract】的数据库操作Service
* @createDate 2025-06-28 19:44:11
*/
public interface UserContractService extends IService<UserContract> {

    boolean saveOrUpdateUserContract(Integer uid,String bucket,String object);


}
