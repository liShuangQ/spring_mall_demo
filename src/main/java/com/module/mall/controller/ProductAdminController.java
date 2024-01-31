package com.module.mall.controller;

import com.github.pagehelper.PageInfo;
import com.module.mall.common.ApiRestResponse;
import com.module.mall.common.Constant;
import com.module.mall.common.ValidList;
import com.module.mall.exception.MallException;
import com.module.mall.model.dao.ProductMapper;
import com.module.mall.model.pojo.Product;
import com.module.mall.model.request.AddProductReq;
import com.module.mall.model.request.UpdateProductReq;
import com.module.mall.service.ProductService;
import com.module.mall.service.UploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequestMapping("/admin")
@RestController // 相当于下面每个都加上了ResponseBody
@Validated
public class ProductAdminController {
    @Autowired
    ProductService productService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    ProductMapper productMapper;

    @ApiOperation("后台添加商品")
    @PostMapping("/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) throws MallException {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    private URI getHost(URI uri) throws URISyntaxException {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                    null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }


    @ApiOperation("后台上传商品图片")
    @PostMapping("/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file) throws IOException, MallException {
        String result = uploadService.uploadFile(file);
        return ApiRestResponse.success(result);

//        String fileName = file.getOriginalFilename();
//        String suffixName = fileName.substring(fileName.lastIndexOf("."));
//        //生成文件名称UUID
//        UUID uuid = UUID.randomUUID();
//        String newFileName = uuid.toString() + suffixName;
//        //创建文件
//        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
//        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
//        if (!fileDirectory.exists()) {
//            if (!fileDirectory.mkdir()) {
//                throw new MallException(MallExceptionEnum.MKDIR_FAILED);
//            }
//        }
//        try {
//            // 从请求中写到新的文件
//            file.transferTo(destFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            return ApiRestResponse
//                    .success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/"
//                            + newFileName);
//        } catch (URISyntaxException e) {
//            return ApiRestResponse.error(MallExceptionEnum.UPLOAD_FAILED);
//        }
    }

    @ApiOperation("后台上传商品图片（图片处理）")
    @PostMapping("/upload/image")
    public ApiRestResponse uploadImage(HttpServletRequest httpServletRequest,
                                       @RequestParam("file") MultipartFile file) throws IOException, MallException {
        String result = uploadService.uploadImage(file);
        return ApiRestResponse.success(result);
    }

    @ApiOperation("后台更新商品")
    @PostMapping("/product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) throws MallException {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台删除商品")
    @PostMapping("/product/delete")
    public ApiRestResponse deleteProduct(@RequestParam Integer id) throws MallException {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量上下架接口")
    @PostMapping("/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,
                                                 @RequestParam Integer sellStatus) {
        if (ids.length == 0) {
            return ApiRestResponse.error("请检查参数");
        }
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品列表接口")
    @PostMapping("/product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("后台批量上传商品接口")
    @PostMapping("product/upload")
    public ApiRestResponse uploadProduct(@RequestParam("file") MultipartFile multipartFile) throws IOException, MallException {
        String newFileName = uploadService.getNewFileName(multipartFile);
        // 创建文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        uploadService.createFile(multipartFile, fileDirectory, destFile);
        productService.addProductByExcel(destFile);
        // 用完之后删除了
        boolean delete = destFile.delete();
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量更新商品")
    @PostMapping("/product/batchUpdate")
    public ApiRestResponse batchUpdateProduct(@Valid @RequestBody List<UpdateProductReq> updateProductReqList) throws MallException {
        if (updateProductReqList.isEmpty()) {
            return ApiRestResponse.error("请检查参数");
        }
        List<Product> list = new ArrayList<>();
        for (UpdateProductReq updateProductReq : updateProductReqList) {
            //方法一，手动校验
            if (Objects.isNull(updateProductReq.getId())) {
                throw new MallException("id不能为空");
            }
            if (updateProductReq.getPrice() < 1) {
                throw new MallException("价格过低");
            }
            if (updateProductReq.getStock() > 10000) {
                throw new MallException("库存不能大于10000");
            }
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq, product);
            list.add(product);
//            productService.update(product);
        }
        productService.batchUpdateProduct(list);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量更新商品，ValidList验证")
    @PostMapping("/product/batchUpdate2")
    public ApiRestResponse batchUpdateProduct2(@Valid @RequestBody ValidList<UpdateProductReq> updateProductReqList) throws MallException {
        if (updateProductReqList.isEmpty()) {
            return ApiRestResponse.error("请检查参数");
        }
        List<Product> list = new ArrayList<>();
        for (UpdateProductReq updateProductReq : updateProductReqList) {
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq, product);
            list.add(product);
        }
        productService.batchUpdateProduct(list);
        return ApiRestResponse.success();
    }


    @ApiOperation("后台批量更新商品，@Validated验证") //类上注解
    @PostMapping("/product/batchUpdate3")
    public ApiRestResponse batchUpdateProduct3(@Valid @RequestBody List<UpdateProductReq> updateProductReqList) throws MallException {
        if (updateProductReqList.isEmpty()) {
            return ApiRestResponse.error("请检查参数");
        }
        List<Product> list = new ArrayList<>();
        for (UpdateProductReq updateProductReq : updateProductReqList) {
            Product product = new Product();
            BeanUtils.copyProperties(updateProductReq, product);
            list.add(product);
        }
        productService.batchUpdateProduct(list);
        return ApiRestResponse.success();
    }

}
