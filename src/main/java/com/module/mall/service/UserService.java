package com.module.mall.service;

import com.module.mall.exception.MallException;
import com.module.mall.model.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 描述：     UserService
 */
public interface UserService {


    void register(String userName, String password, String emailAddress) throws MallException;

    User login(String userName, String password) throws MallException;

    void updateUserSignature(User user) throws MallException;

    Boolean checkAdminRole(User user) throws MallException;

    User getUserById(String Id);


    // 没用dao层
    List<Map<String, Object>> getUserByNameJdbc(String Id);

    boolean checkEmailRegistered(String emailAddress);

}
