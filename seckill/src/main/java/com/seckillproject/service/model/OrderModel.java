package com.seckillproject.service.model;


import java.math.BigDecimal;

// consumer order
public class OrderModel {
    private String id;

    //id of consumer
    private Integer userId;

    //id of the item/product
    private Integer itemId;

    //whether bought in promo
    private Integer promoId;

    //item price as of transaction -> promo price if promoId not null
    private BigDecimal itemPrice;

    //transaction item amount
    private Integer amount;

    //transaction dollar amount -> promo price if promoId not null
    private BigDecimal dollarAmount;

    public String getId() {
        return id;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(BigDecimal dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
