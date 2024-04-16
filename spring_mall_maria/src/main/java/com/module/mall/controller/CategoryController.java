package com.module.mall.controller;

import com.github.pagehelper.PageInfo;
import com.module.mall.common.ApiRestResponse;
import com.module.mall.exception.MallException;
import com.module.mall.model.pojo.Category;
import com.module.mall.model.request.AddCategoryReq;
import com.module.mall.model.request.UpdateCategoryReq;
import com.module.mall.model.vo.CategoryVO;
import com.module.mall.service.CategoryService;
import com.module.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * 描述：     目录Controller
 */
@Controller
public class CategoryController {
    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    /**
     * 后台添加目录
     */
    @ApiOperation("后台添加目录")
    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody //接收的是json
    AddCategoryReq addCategoryReq) throws MallException {
        //（通过配置用户过滤器可省略）
//        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
//        if (currentUser == null) {
//            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
//        }
        //校验是否是管理员
//        boolean adminRole = false;
//        adminRole = userService.checkAdminRole(currentUser);
//        if (adminRole) {
//            //是管理员，执行操作
//            categoryService.add(addCategoryReq);
//            return ApiRestResponse.success();
//        } else {
//            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
//        }
        categoryService.add(addCategoryReq);
        return ApiRestResponse.success("目录添加成功");
    }


    @ApiOperation("后台更新目录")
    @PostMapping("/admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq,
                                          HttpSession session) throws MallException {
        Category category = new Category();
        //两个类里面字段和字段类型一样拷贝过去（从哪来，到哪里）
        BeanUtils.copyProperties(updateCategoryReq, category);
        categoryService.update(category);
        return ApiRestResponse.success("目录更新成功");
    }

    @ApiOperation("后台删除目录")
    @PostMapping("/admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id) throws MallException {
        categoryService.delete(id);
        return ApiRestResponse.success("目录删除成功");
    }

    @ApiOperation("后台目录列表")
    @PostMapping("/admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台目录列表")
    @PostMapping("/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer() {
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }

}
