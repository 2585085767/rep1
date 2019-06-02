package com.leyou.item.service;

import com.leyou.common.enums.ExceptionHandleEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        //查询条件，mapper会把对象中的非空字段作为查询条件进行查询
        Category t = new Category();
        t.setParentId(pid);
        List<Category> list = categoryMapper.select(t);
        //判断结果
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionHandleEnum.CATEGORY_NOT_FOND);
        }
        return list;
    }

    public List<Category> queryByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        //判断结果
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionHandleEnum.CATEGORY_NOT_FOND);
        }
        return list;
    }
}
