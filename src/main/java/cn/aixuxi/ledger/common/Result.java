package cn.aixuxi.ledger.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回参数
 */
@Data
@AllArgsConstructor
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> succeed(){
        return of(200, "操作成功", null);
    }

    public static <T> Result<T> succeed(String message){
        return of(200,message,null);
    }

    public static <T> Result<T> succeed(int code,String message){
        return of(code,message,null);
    }

    public static <T> Result<T> succeed(String message, T data){
        return of(200,message,data);
    }

    public static <T> Result<T> succeed(T data){
        return of(200, "操作成功", data);
    }
    public static <T> Result<T> failed(){
        return of(500, "操作失败", null);
    }

    public static <T> Result<T> failed(String message){
        return of(500,message,null);
    }

    public static <T> Result<T> failed(int code,String message){
        return of(code,message,null);
    }

    public static <T> Result<T> failed(String message, T data){
        return of(500,message,data);
    }

    public static <T> Result<T> failed(T data){
        return of(500, "操作失败", data);
    }



    public static <T> Result<T> of(int code,String message,T data){
        return new Result<>(code,message,data);
    }
}
