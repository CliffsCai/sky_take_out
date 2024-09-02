package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    PageResult pageQuery(int page, int pageSize, Integer status);

    void cancelById(Long id);

    OrderVO details(Long id);

    void repetitionOrderById(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    void adminCancelById(Orders orders);

    OrderStatisticsVO showStatistics();

    void deliveryOrder(Long id);

    void confirmOrder(OrdersDTO ordersDTO);

    void rejectionOrder(Orders orders);

    void completeOrder(Long id);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);

    void paySuccess(String outTradeNo);

    void reminder(Long id);
}
