package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface ReportMapper {


    @Select("select SUM(amount) from orders where status = #{status} and pay_status = #{payStatus} and order_time >= #{localDateTimeStart} and order_time <= #{localDateTimeEnd}")
    Integer getTrunoverByDate(Integer status, Integer payStatus, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);

    @Select("select count(*) from user where create_time >= #{localDateTimeStart} and create_time <= #{localDateTimeEnd}")
    Integer getUserAddByDate(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);

    @Select("select count(*) from orders where order_time >= #{localDateTimeStart} and order_time <= #{localDateTimeEnd}")
    Integer getOrderAddByDate(LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);

    @Select("select count(*) from orders where  status = #{status} and pay_status = #{payStatus} and order_time >= #{localDateTimeStart} and order_time <= #{localDateTimeEnd}")
    Integer getValidOrder(Integer status, Integer payStatus, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);


}
