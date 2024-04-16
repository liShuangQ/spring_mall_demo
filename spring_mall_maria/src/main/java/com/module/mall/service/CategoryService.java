package com.module.mall.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.module.mall.exception.MallException;
import com.module.mall.exception.MallExceptionEnum;
import com.module.mall.model.dao.CategoryMapper;
import com.module.mall.model.pojo.Category;
import com.module.mall.model.request.AddCategoryReq;
import com.module.mall.model.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：     目录分类Service实现类
 */
@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;


    public void add(AddCategoryReq addCategoryReq) throws MallException {
        Category category = new Category();
        //两个类里面字段和字段类型一样拷贝过去（从哪来，到哪里） 合并
        BeanUtils.copyProperties(addCategoryReq, category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if (categoryOld != null) {
            // 错误不能在try catch中，否则无法通过异常拦截捕捉
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }


    public void update(Category updateCategory) throws MallException {
        Category categoryOld = categoryMapper.selectByPrimaryKey(updateCategory.getId());
        if (categoryOld == null) {
            throw new MallException(MallExceptionEnum.SEARCH_FAILED);
        }
//        if (updateCategory.getName() != null) {
//            categoryOld = categoryMapper.selectByName(updateCategory.getName());
//            if (categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())) {
//                throw new MallException(MallExceptionEnum.NAME_EXISTED);
//            }
//        }
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }


    public void delete(Integer id) throws MallException {
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        if (categoryOld == null) {
            throw new MallException(MallExceptionEnum.SEARCH_FAILED);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }


    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        // orderBy 按照这两个字段排序
        PageHelper.startPage(pageNum, pageSize, "type, order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }


    // 缓存的字段
    @Cacheable(value = "listCategoryForCustomer")
    public List<CategoryVO> listCategoryForCustomer(Integer parentId) {
        ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
        recursivelyFindCategories(categoryVOList, parentId);
        return categoryVOList;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Integer parentId) {
        //递归获取所有子类别，并组合成为一个“目录树”
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        // 集合进行空判断
        if (!CollectionUtils.isEmpty(categoryList)) {
            for (int i = 0; i < categoryList.size(); i++) {
                Category category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVOList.add(categoryVO);
                recursivelyFindCategories(categoryVO.getChildCategory(), categoryVO.getId());
            }
        }
    }
}
