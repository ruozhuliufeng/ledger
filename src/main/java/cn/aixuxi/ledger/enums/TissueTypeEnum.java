package cn.aixuxi.ledger.enums;

/**
 * 组织类型
 *
 * @author ruozhuliufeng
 */
public enum TissueTypeEnum {
    FAMILY(0, "家庭"),
    TEAM(1, "团队"),
    ;
    private int code;


    private String name;

    TissueTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
