package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    //查询spu
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(name = "key",required = false) String key,
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "rows",defaultValue = "5") Integer rows,
            @RequestParam(name = "saleable",required = false) Boolean saleable
    ){
        return ResponseEntity.ok(goodsService.querySpuByPage(key,page,rows,saleable));
    }
    //增加商品
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.ok().build();
    }

    //更新商品
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //查询商品详情
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable("id") Long spuId){
        return ResponseEntity.ok(goodsService.queryDetailById(spuId));
    }

    //根据spuid查询sku属性
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuListBySpuId(@RequestParam("id") Long spuId){
        return ResponseEntity.ok(goodsService.querySkuListBySpuId(spuId));
    }

    //修改商品状态
    @PutMapping("spu/status")
    public ResponseEntity<Void> updateStatus(@RequestBody Spu spu){
        goodsService.updateStatus(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
