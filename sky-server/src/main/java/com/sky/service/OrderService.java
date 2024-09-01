package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    OrderSubmitVO order(OrdersSubmitDTO ordersSubmitDTO);


    PageResult pageQuery(int page, int pageSize, Integer status);

    void cancelById(Long id);

    OrderVO details(Long id);

    void repetitionOrderById(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);
}
