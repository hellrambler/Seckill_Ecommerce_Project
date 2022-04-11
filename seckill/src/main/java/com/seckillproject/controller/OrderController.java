package com.seckillproject.controller;

import com.seckillproject.error.BusinessException;
import com.seckillproject.error.EnumBusinessError;
import com.seckillproject.response.CommonReturnType;
import com.seckillproject.service.OrderService;
import com.seckillproject.service.model.OrderModel;
import com.seckillproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //order request
    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId", required = false) Integer promoId) throws BusinessException {
        //check login + user log in info
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EnumBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        // create order model
        int userId = userModel.getId();
        OrderModel orderModel = orderService.createOrder(userId, itemId, amount, promoId);

        return CommonReturnType.create(null);
    }

}
