package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;


@Mapper
public interface ShoppingCartMapper {

    void add(ShoppingCart shoppingCart);

    //返回值为list，sql语句需要声明返回值
    List<ShoppingCart> listShoppingCart(ShoppingCart shoppingCart);


    void update(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart shoppingCart);

    void deleteDishOrSetmealById(ShoppingCartDTO shoppingCartDTO);

    @Delete("DELETE from shopping_cart where user_id = #{userId}")
    void cleanByUserId(Long userId);

    void insertBatch(List<ShoppingCart> shoppingCartList);
}



