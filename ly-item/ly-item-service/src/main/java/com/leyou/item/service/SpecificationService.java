package com.leyou.item.service;

import com.leyou.common.enums.ExceptionHandleEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        //查询条件
        SpecGroup g = new SpecGroup();
        g.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(g);
        //判断结果
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionHandleEnum.SPEC_GROUP_NOT_FOND);
        }
        return list;
    }

    @Transactional
    public void addGroup(SpecGroup specGroup) {
        int count = specGroupMapper.insert(specGroup);
        if (count != 1){
            throw new LyException(ExceptionHandleEnum.SPEC_GROUP_SAVE_ERROR);
        }
    }

    //删除规格组
    @Transactional
    public void delGroup(Long id) {
        int i = specGroupMapper.deleteByPrimaryKey(id);
        if (i != 1){
            throw new LyException(ExceptionHandleEnum.SPEC_GROUP_DELETE_ERROR);
        }
    }

    //更新规格组
    @Transactional
    public void putGroup(SpecGroup specGroup) {
        int i = specGroupMapper.updateByPrimaryKey(specGroup);
        if (i != 1){
            throw new LyException(ExceptionHandleEnum.SPEC_GROUP_UPDATE_ERROR);
        }
    }

    //查询规格参数
    public List<SpecParam> queryParamByList(Long gid,Long cid,Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(param);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionHandleEnum.SPEC_PARAM_NOT_FOND);
        }
        return list;
    }

    //新增规则参数
    @Transactional
    public void addParam(SpecParam specParam) {
        int i = specParamMapper.insert(specParam);
        if (i != 1){
            throw new LyException(ExceptionHandleEnum.SPEC_PARAM_SAVE_ERROR);
        }
    }

    //更新规格参数
    @Transactional
    public void putParam(SpecParam specParam) {
        int i = specParamMapper.updateByPrimaryKey(specParam);
        if (i != 1){
            throw new LyException(ExceptionHandleEnum.SPEC_PARAM_UPDATE_ERROR);
        }
    }

    public void delParam(Long id) {
        int i = specParamMapper.deleteByPrimaryKey(id);
        if (i != 1){
            throw new LyException(ExceptionHandleEnum.SPEC_PARAM_DELETE_ERROR);
        }
    }
}
