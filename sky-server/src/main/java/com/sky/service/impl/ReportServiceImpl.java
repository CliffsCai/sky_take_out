package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.*;
import com.sky.service.ReportService;
import com.sky.service.UserService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Autowired
    private UserMapper userMapper;


    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        StringBuilder dateString = new StringBuilder();
        StringBuilder moneyString = new StringBuilder();
        LocalDate currDate = begin;
        while (currDate.isBefore(end) || currDate.isEqual(end)){
            //计算日期
            dateString.append(currDate);
            dateString.append(",");
            //计算每日营业额
            LocalDateTime localDateTimeStart = LocalDateTime.of(currDate, LocalTime.MIN);
            LocalDateTime localDateTimeEnd = LocalDateTime.of(currDate, LocalTime.MAX);
            Integer turnover = reportMapper.getTrunoverByDate(Orders.COMPLETED, Orders.PAID, localDateTimeStart,localDateTimeEnd);
            if(turnover == null){
                turnover = 0;
            }
            moneyString.append(turnover);
            moneyString.append(",");

            //日期加一
            LocalDate localDate = currDate.plusDays(1);
            currDate = localDate;

        }
        dateString.deleteCharAt(dateString.length()-1);
        moneyString.deleteCharAt(moneyString.length()-1);
        return TurnoverReportVO.builder().dateList(dateString.toString()).turnoverList(moneyString.toString()).build();



    }

    @Override
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        StringBuilder dateString = new StringBuilder();
        StringBuilder userString = new StringBuilder();
        StringBuilder userAllString = new StringBuilder();
        LocalDate currDate = begin;
        while (currDate.isBefore(end) || currDate.isEqual(end)){
            //计算日期
            dateString.append(currDate);
            dateString.append(",");
            //计算每日新增用户
            LocalDateTime localDateTimeStart = LocalDateTime.of(currDate, LocalTime.MIN);
            LocalDateTime localDateTimeEnd = LocalDateTime.of(currDate, LocalTime.MAX);
            Integer userAdd = reportMapper.getUserAddByDate(localDateTimeStart,localDateTimeEnd);
            if(userAdd == null){
                userAdd = 0;
            }
            userString.append(userAdd);
            userString.append(",");

            //计算到目前为止总用户
            Integer userAllAdd = userMapper.getUserNumber(localDateTimeEnd);
            userAllString.append(userAllAdd);
            userAllString.append(",");

            //日期加一
            LocalDate localDate = currDate.plusDays(1);
            currDate = localDate;

        }
        dateString.deleteCharAt(dateString.length()-1);
        userString.deleteCharAt(userString.length()-1);
        userAllString.deleteCharAt(userAllString.length()-1);
        return UserReportVO.builder().dateList(dateString.toString()).newUserList(userString.toString()).
                totalUserList(userAllString.toString()).build();



    }

    @Override
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {
        StringBuilder dateString = new StringBuilder();
        StringBuilder orderString = new StringBuilder();
        StringBuilder validOrderString = new StringBuilder();
        LocalDate currDate = begin;
        while (currDate.isBefore(end) || currDate.isEqual(end)){
            //计算日期
            dateString.append(currDate);
            dateString.append(",");
            //计算每日订单数
            LocalDateTime localDateTimeStart = LocalDateTime.of(currDate, LocalTime.MIN);
            LocalDateTime localDateTimeEnd = LocalDateTime.of(currDate, LocalTime.MAX);
            Integer orderAdd = reportMapper.getOrderAddByDate(localDateTimeStart,localDateTimeEnd);
            if(orderAdd == null){
                orderAdd = 0;
            }
            orderString.append(orderAdd);
            orderString.append(",");

            //计算每日有效订单数
            Integer validOrder = reportMapper.getValidOrder(Orders.COMPLETED, Orders.PAID,localDateTimeStart,localDateTimeEnd);
            validOrderString.append(validOrder);
            validOrderString.append(",");

            //日期加一
            LocalDate localDate = currDate.plusDays(1);
            currDate = localDate;

        }

        dateString.deleteCharAt(dateString.length()-1);
        orderString.deleteCharAt(orderString.length()-1);
        validOrderString.deleteCharAt(validOrderString.length()-1);


        Integer orderCount = orderMapper.getTotalNumber();
        Integer validOrderCount = orderMapper.getValidTotalNumber(Orders.COMPLETED, Orders.PAID);


        return OrderReportVO.builder().dateList(dateString.toString()).
                orderCountList(orderString.toString()).
                validOrderCountList(validOrderString.toString()).
                totalOrderCount(orderCount).validOrderCount(validOrderCount).
                orderCompletionRate(((double)validOrderCount)/((double)orderCount)).build();


    }

    @Override
    public SalesTop10ReportVO salesTop10Report(LocalDate begin, LocalDate end) {

        StringBuilder sbKey = new StringBuilder();
        StringBuilder sbValue = new StringBuilder();


        List<GoodsSalesDTO> countByDishnameList = orderDetailMapper.getCountByDishname();
        for (GoodsSalesDTO stringIntegerMap : countByDishnameList) {
            sbKey.append(stringIntegerMap.getName()).append(",");
            sbValue.append(stringIntegerMap.getNumber()).append(",");
        }
        sbKey.deleteCharAt(sbKey.length()-1);
        sbValue.deleteCharAt(sbValue.length()-1);

        return   SalesTop10ReportVO.builder().nameList(sbKey.toString()).
                numberList(sbValue.toString()).build();
    }
}
