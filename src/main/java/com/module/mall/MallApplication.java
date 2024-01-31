package com.module.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
// 如果不配置 在service层的时候会找不到mapper
@MapperScan(basePackages = {"com.module.mall.model.dao"})
@EnableSwagger2
// 打开缓存
@EnableCaching
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
        System.out.println("启动成功");
    }


}


