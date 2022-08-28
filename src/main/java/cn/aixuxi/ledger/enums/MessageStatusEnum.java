package cn.aixuxi.ledger.enums;

public enum MessageStatusEnum {
    WAIT(0, "待处理"),
    HANDLE(1, "已处理"),
    ;
    private Integer code;
    private String message;
    MessageStatusEnum(Integer code, String message){
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
