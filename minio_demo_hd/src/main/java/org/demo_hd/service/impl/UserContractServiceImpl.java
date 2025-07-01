package org.demo_hd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.demo_hd.entity.UserContract;
import org.demo_hd.entity.UserImage;
import org.demo_hd.mapper.UserImageMapper;
import org.demo_hd.service.UserContractService;
import org.demo_hd.mapper.UserContractMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
* @author tang_
* @description 针对表【t_user_contract】的数据库操作Service实现
* @createDate 2025-06-28 19:44:11
*/
@Service
public class UserContractServiceImpl extends ServiceImpl<UserContractMapper, UserContract>
    implements UserContractService{

    @Resource
    private UserContractMapper userContractMapper;

    @Override
    public boolean saveOrUpdateUserContract(Integer uid,String bucket,String object) {
        LambdaQueryWrapper<UserContract> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(UserContract::getUid,uid);
        boolean flag=false;

        long count=userContractMapper.selectCount(wrapper);

        UserContract userContract=new UserContract();
        userContract.setUid(uid);
        userContract.setBucket(bucket);
        userContract.setObject(object);

        if(count<=0){
            userContract.setCreateTime(new Date(System.currentTimeMillis()));
            flag=userContractMapper.insert(userContract)>=1;
        }else{
            userContract.setUpdateTime(new Date(System.currentTimeMillis()));
            flag= userContractMapper.update(userContract,wrapper)>=1;
        }

        return flag;
    }

}




