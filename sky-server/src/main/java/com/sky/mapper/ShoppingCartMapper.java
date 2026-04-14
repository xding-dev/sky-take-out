package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface ShoppingCartMapper {

    /**
     * 动态查询购物车
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);


    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart  shoppingCart);


    /**
     * 插入购物车数据
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUE (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);


    /**
     * 清空购物车
     */
    @Delete("delete from shopping_cart where user_id = #{userID}")
    void clean(Long userID);

    /**
     * 根据Id删除购物车数据
     * @param Id
     */
    @Delete("delete from shopping_cart where id = #{Id}")
    void deleteById(Long Id);

    /**
     * 批量插入购物车数据
     *
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
