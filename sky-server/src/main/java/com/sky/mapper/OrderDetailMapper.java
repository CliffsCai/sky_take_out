package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {


    void submitDetail(List<OrderDetail> list);


    List<OrderDetail> getByOrderId(Long orderId);

    @Select("select * from order_detail where order_id = #{id}")
    Orders getOrderDetailById(Long id);

    @Select("SELECT name, COUNT(*) AS number FROM order_detail od, orders o " +
            "where od.order_id = o.id and o.status = 5 GROUP BY name ORDER BY number DESC LIMIT 10")
    List<GoodsSalesDTO> getCountByDishname();
}
