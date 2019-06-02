package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionHandleEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.entity.Example;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

//    @Transactional
    private void updateSkuAndStocker(Spu spu){
        //定义Stocker集合
        List<Stock> stockList = new ArrayList<>();
        int count ;
        //sku
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setId(null);
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            count = skuMapper.insert(sku);
            if (count !=1 ){
                throw new LyException(ExceptionHandleEnum.GOODS_SAVE_ERROR);
            }
            //stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);

        }
        //stock批量增加
        count = stockMapper.insertList(stockList);
        if (count != stockList.size()){
            throw new LyException(ExceptionHandleEnum.GOODS_SAVE_ERROR);
        }
    }

    public PageResult<Spu> querySpuByPage(String key, Integer page, Integer rows, Boolean saleable) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //搜索字段过滤
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //上下架过滤
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        //排序
        example.setOrderByClause("last_update_time DESC");
        //查询
        List<Spu> spus = spuMapper.selectByExample(example);
        //判断结果
        if (CollectionUtils.isEmpty(spus)){
            throw new LyException(ExceptionHandleEnum.GOODS_NOT_FOND);
        }

        //解析分类和品牌的名称
        loadCategoryNameAndBrandName(spus);
        //解析结果
        PageInfo<Spu> info = new PageInfo<>(spus);

        return new PageResult<>(info.getTotal(),spus);
    }

    private void loadCategoryNameAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            //处理分类名称
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names,"/"));

            //处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        }
    }

    @Transactional
    public void saveGoods(Spu spu) {

//        //定义Stocker集合
//        List<Stock> stockList = new ArrayList<>();
        //spu
        spu.setId(null);
        spu.setSaleable(true);
        spu.setValid(false);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        int count = spuMapper.insert(spu);
        if (count !=1 ){
            throw new LyException(ExceptionHandleEnum.GOODS_SAVE_ERROR);
        }
        //spuDtail
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        spuDetailMapper.insert(detail);
        //sku
        /*List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setId(null);
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            count = skuMapper.insert(sku);
            if (count !=1 ){
                throw new LyException(ExceptionHandleEnum.GOODS_SAVE_ERROR);
            }
            //stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);

        }
        //stock批量增加
        count = stockMapper.insertList(stockList);
        if (count != stockList.size()){
            throw new LyException(ExceptionHandleEnum.GOODS_SAVE_ERROR);
        }*/
        updateSkuAndStocker(spu);
    }

    public SpuDetail queryDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null){
            throw new LyException(ExceptionHandleEnum.GOODS_DETAIL_NOT_FOND);
        }
        return spuDetail;
    }

    public List<Sku> querySkuListBySpuId(Long spuId) {
        //查询sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> list = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionHandleEnum.GOODS_SKU_NOT_FOND);
        }
        //查询库存
        /*for (Sku s : list) {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if (stock == null){
                throw new LyException(ExceptionHandleEnum.GOODS_STOCKER_NOT_FOND);
            }
            s.setStock(stock.getStock());
        }*/
        List<Long> ids = list.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        Map<Long, Integer> stockerMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        list.forEach(s -> s.setStock(stockerMap.get(s.getId())));
        return list;
    }

    @Transactional
    public void updateGoods(Spu spu) {
        //查询sku
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skus = skuMapper.select(sku);
        if (!CollectionUtils.isEmpty(skus)){
            //删除sku
            skuMapper.delete(sku);
            //删除stocker
            List<Long> ids = skus.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }
        //修改spu
        spu.setSaleable(true);
        spu.setValid(false);
        spu.setCreateTime(spu.getCreateTime());
        spu.setLastUpdateTime(new Date());
        int count = spuMapper.updateByPrimaryKey(spu);
        if (count !=1 ){
            throw new LyException(ExceptionHandleEnum.GOODS_UPDATE_ERROR);
        }
        //spuDtail
        count = spuDetailMapper.updateByPrimaryKey(spu.getSpuDetail());
        if (count !=1 ){
            throw new LyException(ExceptionHandleEnum.GOODS_UPDATE_ERROR);
        }

        //新增sku和stocker
        updateSkuAndStocker(spu);
    }

    @Transactional
    public void updateStatus(Spu spu) {
        //获取状态
        if (spu.getSaleable()){
            spu.setSaleable(false);
        }else {
            spu.setSaleable(true);
        }
        int count = spuMapper.updateByPrimaryKey(spu);
        if (count != 1){
            throw new LyException(ExceptionHandleEnum.GOODS_UPDATE_STATUS_ERROR);
        }
    }
}
