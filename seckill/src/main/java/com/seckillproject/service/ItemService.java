package com.seckillproject.service;

import com.seckillproject.error.BusinessException;
import com.seckillproject.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    //create item
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //browse item list
    List<ItemModel> listItem();
    //item description
    ItemModel getItemById(Integer id);

    //deduct inventory
    boolean decreaseInventory(Integer integer, Integer amount) throws BusinessException;

    //increase sales
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
