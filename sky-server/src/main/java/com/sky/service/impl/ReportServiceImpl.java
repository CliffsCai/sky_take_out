package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.*;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private WorkspaceService workspaceService;


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

    @Override
    public void export(HttpServletResponse response) {

        //查询营业数据最近30天
        LocalDate now = LocalDate.now();
        LocalDate start = now.minusDays(30);
        LocalDate end = now.minusDays(1);

        BusinessDataVO businessData = workspaceService.getBusinessData(
                LocalDateTime.of(start, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));

        //通过POI写入excel
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {


            XSSFWorkbook excel = new XSSFWorkbook(resourceAsStream);

            //获取表格sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");
            // 填充时间
            sheet.getRow(1).getCell(1).setCellValue("时间："+start + "至" + end);

            //获取第四行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            //获得第五行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            //填充30天数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = start.plusDays(i);
                BusinessDataVO tempBusinessDataVO = workspaceService.getBusinessData(
                        LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                XSSFRow tempRow = sheet.getRow(7+i);
                tempRow.getCell(1).setCellValue(date.toString());
                tempRow.getCell(2).setCellValue(tempBusinessDataVO.getTurnover());
                tempRow.getCell(3).setCellValue(tempBusinessDataVO.getValidOrderCount());
                tempRow.getCell(4).setCellValue(tempBusinessDataVO.getOrderCompletionRate());
                tempRow.getCell(5).setCellValue(tempBusinessDataVO.getUnitPrice());
                tempRow.getCell(6).setCellValue(tempBusinessDataVO.getNewUsers());

            }

            //通过输出流将Excel文件下载到浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);
            outputStream.close();
            excel.close();
        }catch (Exception e){

        }


    }
}
