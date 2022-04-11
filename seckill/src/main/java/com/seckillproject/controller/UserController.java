package com.seckillproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.seckillproject.controller.viewObject.UserVO;
import com.seckillproject.error.BusinessException;
import com.seckillproject.error.EnumBusinessError;
import com.seckillproject.response.CommonReturnType;
import com.seckillproject.service.UserService;
import com.seckillproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    //log in
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="mobile") String mobile,
                                  @RequestParam(name="password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //validate input
        if (org.apache.commons.lang3.StringUtils.isEmpty(mobile) ||
                org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //check (mobile, password) -> log in
        UserModel userModel = userService.validateLogin(mobile, EncodeByMd5(password));

        //token added to successful login session
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", httpServletRequest.getSession().getId())
                .httpOnly(true)
                .secure(true)
                .domain("localhost")
                .path("/")
                .maxAge(1800)
                .sameSite("None")
                .build();

        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return CommonReturnType.create(null);
    }

    //user registration
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="mobile") String mobile,
                                     @RequestParam(name="otpCode") String otpCode,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name="gender") Integer gender,
                                     @RequestParam(name="age") Integer age,
                                     @RequestParam(name="password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //verify mobile & otp code
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(mobile);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "Invalid Code");
        }
        //register
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender)));
        userModel.setAge(age);
        userModel.setMobile(mobile);
        userModel.setRegisterMode("byphone");

        userModel.setEncryptPassword(EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //calculate
        MessageDigest messageDigestMd5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        String newstr = base64Encoder.encode(messageDigestMd5.digest(str.getBytes("utf-8")));

        return newstr;
    }

    //user get OTP SMS
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(HttpServletRequest request, HttpServletResponse response, @RequestParam(name="mobile") String mobile){
        //create OTP
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //OTP & mobile associated (redis is preferred in real practice; HTTP session for now)
        httpServletRequest.getSession().setAttribute(mobile,otpCode);

        //OTP -> SMS -> user(omit for now because no service is available)
        System.out.println("mobile: " + mobile + "&otpCode = " + otpCode);

        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", httpServletRequest.getSession().getId())
                .httpOnly(true)
                .secure(true)
                .domain("localhost")
                .path("/")
                .maxAge(1800)
                .sameSite("None")
                .build();

        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return CommonReturnType.create(null);
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //call service to retrieve user object with id == id
        //send to front end
        UserModel userModel = userService.getUserById(id);

        // user not exist
        if (userModel == null) {
            //userModel.setEncryptPassword("231");
            throw new BusinessException(EnumBusinessError.USER_NOT_EXIST);
        }


        // user model -> user VO for UI
        UserVO userVO = convertFromModel(userModel);

        // common response
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        } else {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userModel,userVO);
            return userVO;
        }
    }


}
