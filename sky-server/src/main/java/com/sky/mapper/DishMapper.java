package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface DishMapper {

    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> listByCategoryId(Dish dish);

    @Select("select count(id) from dish where id = #{id}")
    Integer countByCategoryId(Long id);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);


    void deleteDishesByIds(List<Long> ids);

    @Select("select id from dish where id = #{id} and status = 0")
    Long getDishStatusByDishIds(Long id);

    @Select("select * from dish where id = #{id}")
    DishDTO getDishById(Long id);

    @Select("select * from dish where category_id = #{category_id}")
    List<DishVO> getDishByCategoryId(Long categoryId);

    @Select("SELECT name FROM dish ")
    List<String> listDishName();


}
