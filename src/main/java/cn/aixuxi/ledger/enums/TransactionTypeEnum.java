package cn.aixuxi.ledger.enums;

import lombok.Data;

public enum TransactionTypeEnum {
    INCOME(0, "收入"),
    EXPEND(1, "支出"),
    OTHER(2, "其他");
    private Integer code;
    private String message;
    TransactionTypeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode(){
        return this.code;
    }
    public void setCode(Integer code){
        this.code = code;
    }
    public String getMessage(){
        return this.message;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
