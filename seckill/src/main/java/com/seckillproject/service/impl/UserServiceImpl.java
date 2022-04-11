package com.seckillproject.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.seckillproject.DAO.UserDAOMapper;
import com.seckillproject.DAO.UserPasswordDAOMapper;
import com.seckillproject.dataObject.UserDAO;
import com.seckillproject.dataObject.UserPasswordDAO;
import com.seckillproject.error.BusinessException;
import com.seckillproject.error.EnumBusinessError;
import com.seckillproject.service.UserService;
import com.seckillproject.service.model.UserModel;
import com.seckillproject.validator.ValidationRes;
import com.seckillproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAOMapper userDAOMapper;

    @Autowired
    private UserPasswordDAOMapper userPasswordDAOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDAO userDAO = userDAOMapper.selectByPrimaryKey(id);
        if (userDAO == null) {
            return null;
        }
        // id -> user id from user_info
        // user id -> password from user password (encrypted)
        UserPasswordDAO userPasswordDAO = userPasswordDAOMapper.selectByUserId(userDAO.getId());
        return convertFromDataObject(userDAO, userPasswordDAO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if (StringUtils.isEmpty(userModel.getName()) || userModel.getGender() == null
//        || userModel.getAge() == null || StringUtils.isEmpty(userModel.getMobile())){
//            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
//        }

        ValidationRes validationRes = validator.validate(userModel);
        if (validationRes.isHasErr()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, validationRes.getErrMsg());
        }

        //model -> DAO
        UserDAO userDAO = convertFromModel(userModel);
        try{
            userDAOMapper.insertSelective(userDAO);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "This mobile number has been registered");
        }

        userModel.setId(userDAO.getId());

        UserPasswordDAO userPasswordDAO = convertPasswordFromModel(userModel);
        userPasswordDAOMapper.insertSelective(userPasswordDAO);

        return;
    }

    @Override
    public UserModel validateLogin(String mobile, String encryptPassword) throws BusinessException {
        //mobile -> user info
        UserDAO userDAO = userDAOMapper.selectByMobile(mobile);
        if (userDAO == null) {
            throw new BusinessException(EnumBusinessError.USER_LOGIN_FAIL);
        }

        UserPasswordDAO userPasswordDAO = userPasswordDAOMapper.selectByUserId(userDAO.getId());
        UserModel userModel = convertFromDataObject(userDAO, userPasswordDAO);

        //user info matches input password?
        if (!org.apache.commons.lang3.StringUtils.equals(encryptPassword, userModel.getEncryptPassword())){
            throw new BusinessException(EnumBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDAO convertPasswordFromModel(UserModel userModel){
        if (userModel == null) return null;

        UserPasswordDAO userPasswordDAO = new UserPasswordDAO();
        userPasswordDAO.setEncrptPassword(userModel.getEncryptPassword());
        userPasswordDAO.setUserId(userModel.getId());

        return userPasswordDAO;
    }

    private UserDAO convertFromModel(UserModel userModel) {
        if (userModel == null) return null;

        UserDAO userDAO = new UserDAO();
        BeanUtils.copyProperties(userModel, userDAO);

        return userDAO;
    }

    private UserModel convertFromDataObject(UserDAO userDAO, UserPasswordDAO userPasswordDAO){
        if (userDAO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDAO, userModel);

        if (userPasswordDAO != null){
            userModel.setEncryptPassword(userPasswordDAO.getEncrptPassword());
        }
        return userModel;
    }
}
