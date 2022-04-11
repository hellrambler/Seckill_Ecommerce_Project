package com.seckillproject.service;

import com.seckillproject.error.BusinessException;
import com.seckillproject.service.model.UserModel;

public interface UserService {
    // id -> user object
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;

    /**
     * @param mobile: mobile number user used to register
     * @param password: encrypted password
     */
    UserModel validateLogin(String mobile, String password) throws BusinessException;

}
