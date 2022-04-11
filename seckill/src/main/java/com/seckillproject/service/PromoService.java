package com.seckillproject.service;

import com.seckillproject.service.model.PromoModel;

public interface PromoService {
    // itemId -> current/upcoming promos
    PromoModel getPromoByItemId(Integer itemId);

}
