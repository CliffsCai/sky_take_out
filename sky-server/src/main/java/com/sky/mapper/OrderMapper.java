package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface OrderMapper {




    void submit(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    void cancelById(Orders orders);

    @Select("select count(*) from orders where status = #{status}")
    Integer getStatusCount(Integer status);

    void updateOrderStatus(OrdersDTO ordersDTO);

    void rejectOrder(Orders orders);

    @Update("update orders set status = 6 , cancel_reason = '订单超时，自动取消' where pay_status = 0 and status = 1 and order_time < #{localDateTime}")
    void processTimeoutOrder(LocalDateTime localDateTime);

    @Select("SELECT COUNT(*) from orders")
    Integer getTotalNumber();

    @Select("SELECT COUNT(*) from orders where status = #{status} and pay_status = #{payStatus}")
    Integer getValidTotalNumber(Integer status, Integer payStatus);

    @Select("SELECT SUM(amount) from orders where status = #{status}")
    Double getTotalCount(Integer status);
    
    @Select("SELECT SUM(amount) from orders where status=#{status} and order_time>= #{begin} and order_time <= #{end}")
    Double getTotalCountByTime(Integer status, LocalDateTime begin, LocalDateTime end);


    @Select("SELECT count(*) from orders where status = #{status} and order_time>= #{begin} and order_time <= #{end}")
    Integer getValidTotalNumberByTime(Integer status, Integer paid, LocalDateTime begin, LocalDateTime end);
}
