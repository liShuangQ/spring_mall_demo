package com.module.mall.service;

import com.module.mall.exception.MallException;
import com.module.mall.exception.MallExceptionEnum;
import com.module.mall.model.dao.UserMapper;
import com.module.mall.model.pojo.User;
import com.module.mall.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * 描述：     UserService实现类
 */
@Service
public class UserServiceImpl implements UserService {

    //1:在使用@Autowired注解的时候，默认required=true,表示注入的时候bean必须存在，否则注入失败。
    //2:出现问题的原因是因为mapper是在主启动程序配置的@MapperScan(basePackages = "com.module.mall.model.dao")是给mybatis识别用的
    //但是idea不知道,所以要在每个mapper上加 @Repository 表示是一个资源即可
    //ps: idea在寻找类的时候要在类上加上注解,要不它也不知道哪些类可以被引入
    //ps: @Repository只能标注在DAO类上，因为该注解的作用不只是将类识别为Bean，同时它还能将所标注的类中抛出的数据访问异常封装为 Spring 的数据访问异常类型。
    @Autowired
    UserMapper userMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void register(String userName, String password, String emailAddress) throws MallException {
        // 查询是否存在
        User res = userMapper.searchAllByName(userName);
        if (res != null) {
            // 此时返回的是错误对象,不是定义对象 要修改（通过统一处理异常 拦截异常）
            // server 的 return 是无法打断 controller 的，所以要抛出错误
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
//            throw new MallException("已有用户，失败");
        }
        //写到数据库
        User user = new User();
        user.setUsername(userName);
        user.setEmailAddress(emailAddress);
        try {
            user.setPassword(MD5Util.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String userName, String password) throws MallException {
        String md5Password;
        try {
            md5Password = MD5Util.getMd5(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        User user = userMapper.searchLogin(userName, md5Password);
        if (user == null) {
            throw new MallException(MallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateUserSignature(User user) throws MallException {
        int conut = userMapper.updateByPrimaryKeySelective(user);
        if (conut > 1) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public Boolean checkAdminRole(User user) throws MallException {
        //1-普通 2-管理员
        return user.getRole().equals(2);
    }

    @Override
    public User getUserById(String Id) {
        return userMapper.searchAllByKey(Id);
    }

    // 没用dao层
    @Override
    public List<Map<String, Object>> getUserByNameJdbc(String name) {
        return jdbcTemplate.queryForList("SELECT * FROM mall_user WHERE username = ?", name);
    }


    @Override
    public boolean checkEmailRegistered(String emailAddress) {
        User user = userMapper.selectOneByEmailAddress(emailAddress);
        if (user != null) {
            return false;
        }
        return true;
    }


}
