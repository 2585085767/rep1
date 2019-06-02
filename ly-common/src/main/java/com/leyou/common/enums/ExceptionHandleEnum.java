package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionHandleEnum {

    CATEGORY_NOT_FOND(404,"商品分类没有查到"),
    BRAND_NOT_FOND(404,"品牌没有查到"),
    SPEC_GROUP_NOT_FOND(404,"商品规格组不存在"),
    SPEC_PARAM_NOT_FOND(404,"商品规格参数不存在"),
    GOODS_NOT_FOND(404,"商品不存在"),
    GOODS_DETAIL_NOT_FOND(404,"商品详情不存在"),
    GOODS_SKU_NOT_FOND(404,"商品详情不存在"),
    GOODS_STOCKER_NOT_FOND(404,"商品库存不存在"),
    BRAND_SAVE_ERROR(500,"品牌新增失败"),
    SPEC_GROUP_SAVE_ERROR(500,"商品规格组新增失败"),
    SPEC_PARAM_SAVE_ERROR(500,"商品规格参数新增失败"),
    GOODS_SAVE_ERROR(500,"商品新增失败"),
    SPEC_PARAM_UPDATE_ERROR(500,"商品规格参数更新失败"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    SPEC_GROUP_DELETE_ERROR(500,"商品规格组删除失败"),
    SPEC_PARAM_DELETE_ERROR(500,"商品规格参数删除失败"),
    SPEC_GROUP_UPDATE_ERROR(500,"商品规格组修改失败"),
    GOODS_UPDATE_ERROR(500,"商品修改失败"),
    GOODS_UPDATE_STATUS_ERROR(500,"商品上下架修改失败"),
    INVALID_FILE_TYPE(400,"无效的文件类型")
    ;
    private int code;
    private String message;
}
