package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    ReportService reportService;


    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){

        TurnoverReportVO turnoverStatistics = reportService.turnoverStatistics(begin,end);
        return Result.success(turnoverStatistics);

    }

    @GetMapping("/userStatistics")
    public Result<UserReportVO> userReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){

        UserReportVO userReportVO = reportService.userReport(begin,end);
        return Result.success(userReportVO);

    }

    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){

        OrderReportVO orderReportVO = reportService.orderReport(begin,end);
        return Result.success(orderReportVO);

    }

    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){

        SalesTop10ReportVO salesTop10ReportVO = reportService.salesTop10Report(begin,end);
        return Result.success(salesTop10ReportVO);

    }

    @GetMapping("/export")
    public Result export(){
        return Result.success("OK");
    }


}
