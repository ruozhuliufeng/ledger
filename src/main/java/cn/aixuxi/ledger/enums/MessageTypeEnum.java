package cn.aixuxi.ledger.enums;

public enum MessageTypeEnum {
    NOTICE("notice", "通知"),
    WARNING("warning", "警告"),
    ERROR("error", "错误"),
    TISSUE("tissue", "组织"),
    ;
    private String code;


    private String name;

    MessageTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
