package com.seckillproject.dataObject;

public class ItemInventoryDAO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item_inventory.id
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item_inventory.Inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    private Integer inventory;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item_inventory.item_id
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    private Integer itemId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item_inventory.id
     *
     * @return the value of item_inventory.id
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item_inventory.id
     *
     * @param id the value for item_inventory.id
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item_inventory.Inventory
     *
     * @return the value of item_inventory.Inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    public Integer getInventory() {
        return inventory;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item_inventory.Inventory
     *
     * @param inventory the value for item_inventory.Inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item_inventory.item_id
     *
     * @return the value of item_inventory.item_id
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item_inventory.item_id
     *
     * @param itemId the value for item_inventory.item_id
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}