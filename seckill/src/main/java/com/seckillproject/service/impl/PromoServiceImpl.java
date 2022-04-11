package com.seckillproject.service.impl;

import com.seckillproject.DAO.PromoDAOMapper;
import com.seckillproject.dataObject.PromoDAO;
import com.seckillproject.service.PromoService;
import com.seckillproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDAOMapper promoDAOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        // get the promo info of a specific item
        PromoDAO promoDAO = promoDAOMapper.selectByItemId(itemId);

        // DAO -> model
        PromoModel promoModel = convertFromDataObject(promoDAO);
        if (promoModel == null) {
            return null;
        }

        //filter the promo by date -> current / in the future
        DateTime now = new DateTime();
        if (promoModel.getStartDate().isAfterNow()) {
            promoModel.setStatus(1);
        } else if (promoModel.getEndDate().isBeforeNow()) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        return promoModel;

    }

    private PromoModel convertFromDataObject(PromoDAO promoDAO){
        if (promoDAO == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDAO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDAO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDAO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDAO.getEndDate()));

        return promoModel;
    }
}
