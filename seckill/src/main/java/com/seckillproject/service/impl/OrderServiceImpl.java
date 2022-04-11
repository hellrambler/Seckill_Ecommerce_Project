package com.seckillproject.service.impl;

import com.seckillproject.DAO.OrderDAOMapper;
import com.seckillproject.DAO.SequenceDAOMapper;
import com.seckillproject.dataObject.OrderDAO;
import com.seckillproject.dataObject.SequenceDAO;
import com.seckillproject.error.BusinessException;
import com.seckillproject.error.EnumBusinessError;
import com.seckillproject.service.ItemService;
import com.seckillproject.service.OrderService;
import com.seckillproject.service.UserService;
import com.seckillproject.service.model.ItemModel;
import com.seckillproject.service.model.OrderModel;
import com.seckillproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDAOMapper orderDAOMapper;

    @Autowired
    private SequenceDAOMapper sequenceDAOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount, Integer promoId) throws BusinessException {
        // validate order info - item exists, user exists, amount valid
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "product does not exist");
        }

        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "user does not exist");
        }

        if (amount <= 0 || amount > 99){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "invalid amount");
        }

        // validate promo info
        if (promoId != null){
            // promo is the same as what front end sent back
            if (promoId.intValue() != itemModel.getPromoModel().getId()){
                throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "invalid promo");
            // promo is ongoing or not
            } else if (itemModel.getPromoModel().getStatus().intValue() != 2) {
                throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "promo has not started");
            }
        }

        // inventory - amount (at transaction or at payment)
        boolean success = itemService.decreaseInventory(itemId, amount);

        if (!success) {
            throw new BusinessException(EnumBusinessError.INSUFFICIENT_INVENTORY);
        }

        // order documented
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null) {
            orderModel.setPromoId(promoId);
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setDollarAmount(orderModel.getItemPrice().multiply(new BigDecimal(amount)));

        //generate transaction number
        orderModel.setId(generateOrderNo());
        OrderDAO orderDAO = convertOrderDAOFROMOrderModel(orderModel);
        orderDAOMapper.insertSelective(orderDAO);

        //increase sales
        itemService.increaseSales(itemId, amount);

        // info -> front end
        return orderModel;
    }

    private OrderDAO convertOrderDAOFROMOrderModel(OrderModel orderModel){
        if (orderModel == null) {
            return null;
        }

        OrderDAO orderDAO = new OrderDAO();
        BeanUtils.copyProperties(orderModel, orderDAO);
        orderDAO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDAO.setDollarAmount(orderModel.getDollarAmount().doubleValue());

        return orderDAO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNo(){
        /**
         * 16 digits in total
         * 8-digit transaction YYYYMMDD
         * 6-digit auto-increased - not duplicated within 1 day
         * 2-digit customer split into database/data table 00-99
         */

        StringBuilder stringBuilder = new StringBuilder();

        // date
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        // auto increased series
        //  get sequence
        SequenceDAO sequenceDAO = sequenceDAOMapper.selectSequenceByName("order_info");

        int sequence = sequenceDAO.getCurrentValue();
        sequenceDAO.setCurrentValue(sequenceDAO.getCurrentValue() + sequenceDAO.getStep());
        sequenceDAOMapper.updateByPrimaryKeySelective(sequenceDAO);

        String sequenceStr = String.format("%06d", sequence);
        stringBuilder.append(sequenceStr);

        // database/table
        Random random = new Random();
        int splitNum = random.nextInt(100) - 1;

        String splitStr = String.format("%02d", splitNum);
        stringBuilder.append(splitStr);

        return stringBuilder.toString();
    }
}
