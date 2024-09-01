package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface OrderDetailMapper {


    void submitDetail(List<OrderDetail> list);


    List<OrderDetail> getByOrderId(Long orderId);

    @Select("select * from order_detail where order_id = #{id}")
    Orders getOrderDetailById(Long id);
}
