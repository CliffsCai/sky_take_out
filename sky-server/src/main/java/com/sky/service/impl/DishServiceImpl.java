package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavourMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavourMapper dishFlavourMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;



    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void saveWithFlavour(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //xml文件写了sql语句可以返回主键值，因此dish可以获得id
        dishMapper.insert(dish);
        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        //有值再加
        if(flavors!=null&&flavors.size()>0){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(id);
            }
            dishFlavourMapper.insert(flavors);
        }


    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setId(dishDTO.getId());

        dishMapper.update(dish);

        //删除原来菜品绑定的口味，并换成新的
        List<Long> list = new ArrayList<>();
        list.add(dishDTO.getId());
        dishFlavourMapper.deleteByDishIds(list);

        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDTO.getId());
        }
        dishFlavourMapper.insert(flavors);
    }

    @Override
    @Transactional
    public void deleteDishes(List<Long> ids) {
        List<Long> setmealByDishIds = setmealDishMapper.getSetmealByDishIds(ids);

        if(setmealByDishIds!=null&&setmealByDishIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        for (Long id : ids) {
            if(dishMapper.getDishStatusByDishIds(id)==null){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            };
        }

        dishMapper.deleteDishesByIds(ids);
        dishFlavourMapper.deleteByDishIds(ids);
    }

    @Override
    /**
     * 根据id查找菜品和口味并返回前端，菜品表和口味表是两个
     */
    public DishVO getDishByIdWithFlavor(Long id) {
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dishMapper.getDishById(id), dishVO);

        List<DishFlavor> dishFlavorList = dishFlavourMapper.getByDishId(id);
        dishVO.setFlavors(dishFlavorList);

        return dishVO;
    }

    @Override
    public List<DishVO> listByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.listByCategoryId(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavourMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = Dish.builder().status(status).id(id).build();
        dishMapper.update(dish);
    }
}
