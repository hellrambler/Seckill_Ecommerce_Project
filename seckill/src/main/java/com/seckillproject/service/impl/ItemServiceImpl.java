package com.seckillproject.service.impl;

import com.seckillproject.DAO.ItemDAOMapper;
import com.seckillproject.DAO.ItemInventoryDAOMapper;
import com.seckillproject.dataObject.ItemDAO;
import com.seckillproject.dataObject.ItemInventoryDAO;
import com.seckillproject.error.BusinessException;
import com.seckillproject.error.EnumBusinessError;
import com.seckillproject.service.ItemService;
import com.seckillproject.service.PromoService;
import com.seckillproject.service.model.ItemModel;
import com.seckillproject.service.model.PromoModel;
import com.seckillproject.validator.ValidationRes;
import com.seckillproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDAOMapper itemDAOMapper;

    @Autowired
    private ItemInventoryDAOMapper itemInventoryDAOMapper;

    @Autowired
    private PromoService promoService;

    private ItemDAO convertItemDAOFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemDAO itemDAO = new ItemDAO();
        BeanUtils.copyProperties(itemModel, itemDAO);
        itemDAO.setPrice(itemModel.getPrice().doubleValue());

        return itemDAO;
    }

    private ItemInventoryDAO convertItemInventoryDAOFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemInventoryDAO itemInventoryDAO = new ItemInventoryDAO();
        itemInventoryDAO.setItemId(itemModel.getId());
        itemInventoryDAO.setInventory(itemModel.getInventory());

        return itemInventoryDAO;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //validate input
        ValidationRes validationRes = validator.validate(itemModel);
        if (validationRes.isHasErr()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, validationRes.getErrMsg());
        }

        //item model -> item DAO
        ItemDAO itemDAO = convertItemDAOFromItemModel(itemModel);

        //write in database
        itemDAOMapper.insertSelective(itemDAO);
        itemModel.setId(itemDAO.getId());

        ItemInventoryDAO itemInventoryDAO = convertItemInventoryDAOFromItemModel(itemModel);
        itemInventoryDAOMapper.insertSelective(itemInventoryDAO);
        //return Object

        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {

        List<ItemDAO> itemDAOList = itemDAOMapper.listItem();

        List<ItemModel> itemModelList = itemDAOList.stream().map(itemDAO -> {
            ItemInventoryDAO itemInventoryDAO = itemInventoryDAOMapper.selectByItemId(itemDAO.getId());
            ItemModel itemModel = convertModelFromDataObject(itemDAO, itemInventoryDAO);
            return itemModel;
        }).collect(Collectors.toList());

        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDAO itemDAO = itemDAOMapper.selectByPrimaryKey(id);
        if (itemDAO == null){
            return null;
        }
        // get inventory object
        ItemInventoryDAO itemInventoryDAO = itemInventoryDAOMapper.selectByItemId(itemDAO.getId());

        //data object -> model
        ItemModel itemModel = convertModelFromDataObject(itemDAO, itemInventoryDAO);

        // get promo info
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if (promoModel != null && promoModel.getStatus().intValue() != 3){
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseInventory(Integer itemId, Integer amount) throws BusinessException {
        int affectedRowNum = itemInventoryDAOMapper.decreaseInventory(itemId, amount);
        // whether successful
        return (affectedRowNum > 0);
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDAOMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject(ItemDAO itemDAO, ItemInventoryDAO itemInventoryDAO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDAO, itemModel);
        itemModel.setPrice(new BigDecimal(itemDAO.getPrice()));
        itemModel.setInventory(itemInventoryDAO.getInventory());

        return itemModel;
    }
}
