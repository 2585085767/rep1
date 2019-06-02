package com.leyou.search.service;

import com.leyou.common.enums.ExceptionHandleEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Goods buildGoods(Spu spu){
        //查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())
        );
        if (CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionHandleEnum.CATEGORY_NOT_FOND);
        }
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand ==null){
            throw new LyException(ExceptionHandleEnum.BRAND_NOT_FOND);
        }
        //搜索字段
        String all = spu.getTitle()+ StringUtils.join(names," ")+brand.getName();

        //sku查询
        List<Sku> skuList = goodsClient.querySkuListBySpuId(spu.getId());
        if (CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionHandleEnum.GOODS_SKU_NOT_FOND);
        }
        //对sku进行处理
        List<Map<String,Object>> skus = new ArrayList<>();
        //处理价格
        Set<Long> prices = new HashSet<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("images",StringUtils.substringBefore(sku.getImages(),","));
            map.put("price",sku.getPrice());
            skus.add(map);
            //价格
            prices.add(sku.getPrice());
        }

        //查询specs

        //构建goods对象
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setId(spu.getId());
        goods.setAll(all);// 搜索字段包含标题，分类，品牌，规格等；
        goods.setPrice(prices);// 所有的sku价格集合
        goods.setSkus(JsonUtils.serialize(skus));// 所有的sku集合的json格式
        goods.setSpecs(null);//TODO 所有的可搜索的规格参数
        return goods;
    }
}
