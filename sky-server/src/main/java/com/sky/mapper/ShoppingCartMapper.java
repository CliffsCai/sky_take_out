package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface ShoppingCartMapper {

    void add(ShoppingCart shoppingCart);

    //返回值为list，sql语句需要声明返回值
    List<ShoppingCart> listShoppingCart(ShoppingCart shoppingCart);

    void update(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart shoppingCart);

}



