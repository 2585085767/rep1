package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid){
        return ResponseEntity.ok(specificationService.queryGroupByCid(cid));
    }

    //增加商品规格组
    @PostMapping("group")
    public ResponseEntity<Void> addGroup(SpecGroup specGroup){
        specificationService.addGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //删除商品规格组
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> delGroup(@PathVariable("id") Long id){
        specificationService.delGroup(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("group")
    public ResponseEntity<Void> putGroup(SpecGroup specGroup){
        specificationService.putGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamByList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching
                                                           ){
        return ResponseEntity.ok(specificationService.queryParamByList(gid,cid,searching));
    }

    @PostMapping("param")
    public ResponseEntity<Void> addParam(SpecParam specParam){
        specificationService.addParam(specParam);
        return ResponseEntity.ok().build();
    }

    @PutMapping("param")
    public ResponseEntity<Void> putParam(SpecParam specParam){
        specificationService.putParam(specParam);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> delParam(@PathVariable("id") Long id){
        specificationService.delParam(id);
        return ResponseEntity.ok().build();
    }
}
