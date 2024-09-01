package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    List<Long> getSetmealByDishIds(List<Long> dishIds);

    List<SetmealDish> getSetmealDishesBySetmealid(Long setmealId);

    void insertSetmealDishes(List<SetmealDish> setmealDishes);

    void updateSetmealDish(SetmealDish setmealDish);

    @Delete("delete from setmeal_dish where setmeal_id = #{setmeal_id}")
    void deleteBySetmealId(Long setmeal_id);

    void deleteBySetmealIds(List<Long> setmealIds);

    @Insert("INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies) " +
            "VALUES (#{setmealId},#{dishId},#{name},#{price},#{copies})")
    void insert(SetmealDish setmealDish);


}
