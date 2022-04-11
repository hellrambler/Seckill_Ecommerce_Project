package com.seckillproject.service.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemModel {
    private Integer id;

    //combine incomplete corresponding promo (if any) with item, else null
    private PromoModel promoModel;

    @NotBlank(message = "The name of the product cannot be empty")
    private String title; //product name

    @NotNull(message = "Product needs a price")
    @Min(value = 0, message = "Price of product must be above $0")
    private BigDecimal price; //product price

    @NotNull(message = "Inventory is required. 0 is accepted")
    private Integer inventory;

    @NotBlank(message = "The description of the product cannot be empty")
    private String description;

    private Integer sales;

    @NotBlank(message = "The descriptive image of the product cannot be empty")
    private String imgUrl; //product image

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }
}
