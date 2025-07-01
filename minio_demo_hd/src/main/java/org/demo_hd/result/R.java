package org.demo_hd.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * controller的返回结果统一用该类进行包装
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class R {
    private int code;//返回的状态码
    private String message;//返回的结果信息
    private Object data;//返回的数据

    public static R OK(){
        return new R(200,"成功",null);
    }

    public static R OK(Object data){
        return new R(200,"成功",data);
    }

    public static R FAIL(){
        return new R(500,"失败",null);
    }

}
