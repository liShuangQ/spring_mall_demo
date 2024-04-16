package com.module.mall.controller;

import com.github.pagehelper.PageInfo;
import com.module.mall.common.ApiRestResponse;
import com.module.mall.exception.MallException;
import com.module.mall.model.vo.OrderStatisticsVO;
import com.module.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 描述：     订单后台管理Controller
 */
@RestController
public class OrderAdminController {

    @Autowired
    OrderService orderService;

    @PostMapping("admin/order/list")
    @ApiOperation("管理员订单列表")
    public ApiRestResponse listForAdmin(@RequestParam(name = "pageNum", required = true) Integer pageNum, @RequestParam(name = "pageSize", required = true) Integer pageSize) throws MallException {
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 发货。订单状态流程：0-用户已取消，10-未付款，20-已付款，30-已发货，40-交易完成
     */
    @PostMapping("admin/order/delivered")
    @ApiOperation("管理员发货")
    public ApiRestResponse delivered(@RequestParam String orderNo) throws MallException {
        orderService.deliver(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 完结订单。订单状态流程 0-用户已取消，10-未付款，20-已付款，30-已发货，40-交易完成。管理员和用户都可以调用
     */
    @PostMapping("order/finish")
    @ApiOperation("完结订单")
    public ApiRestResponse finish(@RequestParam String orderNo) throws MallException {
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("admin/order/statistics")
    @ApiOperation("每日订单量统计")
    public ApiRestResponse statistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<OrderStatisticsVO> statistics = orderService.statistics(startDate, endDate);
        return ApiRestResponse.success(statistics);
    }
}
