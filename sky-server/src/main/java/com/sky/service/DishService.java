package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;


public interface DishService {

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void saveWithFlavour(DishDTO dishDTO);

    void updateDish(DishDTO dishDTO);

    void deleteDishes(List<Long> ids);

    DishVO getDishByIdWithFlavor(Long id);


    List<DishVO> listByCategoryId(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    void updateStatus(Integer status, Long id);
}
