package com.module.mall.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.module.mall.common.Constant;
import com.module.mall.exception.MallException;
import com.module.mall.exception.MallExceptionEnum;
import com.module.mall.model.dao.ProductMapper;
import com.module.mall.model.pojo.Product;
import com.module.mall.model.query.ProductListQuery;
import com.module.mall.model.request.AddProductReq;
import com.module.mall.model.request.ProductListReq;
import com.module.mall.model.vo.CategoryVO;
import com.module.mall.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.module.mall.utils.Tools.toBatchList;

@Service
public class ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;


    public void add(AddProductReq addProductReq) throws MallException {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
    }


    public void update(Product updateProduct) throws MallException {
        Product productOld = productMapper.selectByName(updateProduct.getName());
        //同名且不同id，不能继续修改
        if (productOld != null && !productOld.getId().equals(updateProduct.getId())) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    public void delete(Integer id) throws MallException {
        Product productOld = productMapper.selectByPrimaryKey(id);
        //查不到该记录，无法删除
        if (productOld == null) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        List<List<Integer>> batchList = toBatchList(Arrays.asList(ids));
        for (List<Integer> item : batchList) {
            productMapper.batchUpdateSellStatus(item.toArray(new Integer[0]), sellStatus);
        }
    }


    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }


    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }


    public PageInfo list(ProductListReq productListReq) {
        //构建Query对象
        ProductListQuery productListQuery = new ProductListQuery();

        //搜索处理
        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword())
                    .append("%").toString();
            productListQuery.setKeyword(keyword);
        }

        //目录处理：如果查某个目录下的商品，不仅是需要查出该目录下的，还要把所有子目录的所有商品都查出来，所以要拿到一个目录id的List
        if (productListReq.getCategoryId() != null) {
            List<CategoryVO> categoryVOList = categoryService
                    .listCategoryForCustomer(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }
        //排序处理
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ORDER_ENUM.contains(orderBy)) {
            PageHelper
                    .startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            PageHelper
                    .startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }
        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds) {
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addProductByExcel(File destFile) throws IOException, MallException {
        List<Product> products = readProductsFromExcel(destFile);
//        for (int i = 0; i < products.size(); i++) {
//            Product product = products.get(i);
//            Product productOld = productMapper.selectByName(product.getName());
//            if (productOld != null) {
//                throw new MallException(MallExceptionEnum.NAME_EXISTED);
//            }
//            int count = productMapper.insertSelective(product);
//            if (count == 0) {
//                throw new MallException(MallExceptionEnum.CREATE_FAILED);
//            }
//        }
        List<List<Product>> batchList = toBatchList(products, 200);
        for (List<Product> item : batchList) {
            int count = productMapper.insertSelectiveBatch(item);
            if (count == 0) {
                throw new MallException(MallExceptionEnum.CREATE_FAILED);
            }
        }
    }

    private List<Product> readProductsFromExcel(File excelFile) throws IOException {
        ArrayList<Product> listProducts = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(excelFile);

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        // 第0行开始
        XSSFSheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        // 每一行
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            // 每一列
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Product aProduct = new Product();

            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int columnIndex = nextCell.getColumnIndex();
                // 更具每一列的定义好的属性加入map中，最后将map加入list，处理完成
                switch (columnIndex) {
                    case 0:
                        aProduct.setName((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 1:
                        aProduct.setImage((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 2:
                        aProduct.setDetail((String) ExcelUtil.getCellValue(nextCell));
                        break;
                    case 3:
                        Double cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        aProduct.setCategoryId(cellValue.intValue());
                        break;
                    case 4:
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        aProduct.setPrice(cellValue.intValue());
                        break;
                    case 5:
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        aProduct.setStock(cellValue.intValue());
                        break;
                    case 6:
                        cellValue = (Double) ExcelUtil.getCellValue(nextCell);
                        aProduct.setStatus(cellValue.intValue());
                        break;
                    default:
                        break;
                }
            }
            listProducts.add(aProduct);
        }
        workbook.close();
        inputStream.close();
        return listProducts;
    }


    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateProduct(List<Product> list) throws MallException {
        List<List<Product>> batchList = toBatchList(list, 50);
        for (List<Product> item : batchList) {
            int count = productMapper.batchUpdateProduct(item);
            if (count == 0) {
                throw new MallException("插入错误");
            }
        }
    }
}
