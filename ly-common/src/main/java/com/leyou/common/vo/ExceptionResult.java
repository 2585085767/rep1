package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionHandleEnum;
import com.leyou.common.exception.LyException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestep;
    public ExceptionResult(ExceptionHandleEnum e){
        this.status = e.getCode();
        this.message = e.getMessage();
        this.timestep = System.currentTimeMillis();
    }
}
