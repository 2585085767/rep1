package com.leyou.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {
    //查询商品详情
    @GetMapping("spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id") Long spuId);

    //根据spuid查询sku属性
    @GetMapping("sku/list")
   List<Sku> querySkuListBySpuId(@RequestParam("id") Long spuId);


    //查询spu
    @GetMapping("spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(name = "key",required = false) String key,
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "rows",defaultValue = "5") Integer rows,
            @RequestParam(name = "saleable",required = false) Boolean saleable
    );
}
