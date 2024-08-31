package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping
    @Cacheable(cacheNames = "dishCache", key = "#dishDTO.categoryId") //新增缓存
    public Result saveWithFlavour(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavour(dishDTO);

        return Result.success();
    }

    @PutMapping
    @CacheEvict(cacheNames = "dishCache", allEntries = true) //清理缓存
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "dishCache", allEntries = true) //清理缓存
    public Result updateStatus(@PathVariable Integer status, Long id){
        dishService.updateStatus(status, id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "dishCache", allEntries = true) //清理缓存
    //list集合需要加param注解
    public Result deleteDishes(@RequestParam List<Long> ids){
        dishService.deleteDishes(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        DishVO dishvo = dishService.getDishByIdWithFlavor(id);
        return Result.success(dishvo);
    }

    @GetMapping("/list")
    public Result<List<DishVO>> listById(Long categoryId){
        return Result.success( dishService.listByCategoryId(categoryId));
    }

}
