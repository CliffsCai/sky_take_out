package com.sky.controller.admin;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/details/{id}")
    public Result<OrderVO> adminListOrder(@PathVariable Long id){
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO orderStatisticsVO = orderService.showStatistics();

        return Result.success(orderStatisticsVO);
    }

    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody Orders orders){
        orderService.adminCancelById(orders);
        return Result.success();
    }

    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersDTO ordersDTO){
        orderService.confirmOrder(ordersDTO);
        return Result.success();

    }


    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable Long id){
        orderService.deliveryOrder(id);
        return Result.success();

    }

    @PutMapping("/rejection")
    public Result rejectionOrder(@RequestBody Orders orders){
        orderService.rejectionOrder(orders);
        return Result.success();

    }

    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable Long id){
        orderService.completeOrder(id);
        return Result.success();

    }


}