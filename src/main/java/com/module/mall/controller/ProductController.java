package com.module.mall.controller;

import com.github.pagehelper.PageInfo;
import com.module.mall.common.ApiRestResponse;
import com.module.mall.model.pojo.Product;
import com.module.mall.model.request.ProductListReq;
import com.module.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：     前台商品Controller
 */
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @ApiOperation("前台商品详情")
    @PostMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("前台商品列表")
    @PostMapping("/product/list")
    public ApiRestResponse list(@RequestBody ProductListReq productListReq) {
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
