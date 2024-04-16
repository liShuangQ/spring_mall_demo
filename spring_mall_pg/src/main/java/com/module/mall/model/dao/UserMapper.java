package com.module.mall.model.dao;

import com.module.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User searchAllByName(String UserName);

    // 两个参数以上要 @Param
    // **@Param** 用于dao层，是mybatis中的注解
    // 使得mapper.xml中的参数与后台的参数对应上，也增强了可读性
    User searchLogin(@Param("userName") String userName, @Param("password") String password);


    User selectOneByEmailAddress(String emailAddress);
}