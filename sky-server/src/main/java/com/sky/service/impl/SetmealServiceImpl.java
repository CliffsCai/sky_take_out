package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SetmealDishMapper  setmealDishMapper;

    @Override
    @Transactional
    public void saveSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishMapper.insertSetmealDishes(setmealDishes);
    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetmeal(setmeal);

        Long id = setmealDTO.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        //同时更新setmealdish表

        setmealDishMapper.deleteBySetmealId(id);
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
            setmealDishMapper.insert(setmealDish);
        }


    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        List<SetmealVO> result = page.getResult();

        //通过category id获取category name
        for (SetmealVO setmealVO : result) {
            Long categoryId = setmealVO.getCategoryId();
            String categoryName = categoryMapper.getCategoryNameById(categoryId);
            setmealVO.setCategoryName(categoryName);

            //通过setmeal id 获取 setmeal dish name
            Long setmealId = setmealVO.getId();
            List<SetmealDish> setmealDishesName = setmealDishMapper.getSetmealDishesBySetmealid(setmealId);
            setmealVO.setSetmealDishes(setmealDishesName);
        }

        return new PageResult(page.getTotal(), result);

    }

    @Override
    public SetmealVO getById(Long id) {
        // etmealMapper.getById获取的值没有SetmealDish，所以额外要调用setmealDishMapper中的方法，去另一张表拿值
        SetmealVO setmealVO = setmealMapper.getById(id);
        List<SetmealDish> setmealDishesByid = setmealDishMapper.getSetmealDishesBySetmealid(id);
        setmealVO.setSetmealDishes(setmealDishesByid);
        return setmealVO;
    }

    @Override
    @Transactional
    public void deleteSetmealByIds(List<Long> setmealIds) {
        setmealMapper.deleteByIds(setmealIds);
        //同时把setmeal表中含有该套餐的菜品删掉
        setmealDishMapper.deleteBySetmealIds(setmealIds);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.updateSetmeal(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

}
