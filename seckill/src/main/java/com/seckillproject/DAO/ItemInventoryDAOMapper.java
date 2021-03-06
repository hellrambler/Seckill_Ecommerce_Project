package com.seckillproject.DAO;

import com.seckillproject.dataObject.ItemInventoryDAO;
import org.apache.ibatis.annotations.Param;

public interface ItemInventoryDAOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    int insert(ItemInventoryDAO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    int insertSelective(ItemInventoryDAO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    ItemInventoryDAO selectByPrimaryKey(Integer id);

    ItemInventoryDAO selectByItemId(Integer itemId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    int updateByPrimaryKeySelective(ItemInventoryDAO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_inventory
     *
     * @mbg.generated Sat Mar 26 21:03:35 EDT 2022
     */
    int updateByPrimaryKey(ItemInventoryDAO record);

    int decreaseInventory(@Param("itemId") Integer itemId, @Param("amount") Integer amount);

}