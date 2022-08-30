package cn.aixuxi.ledger.enums;

/**
 * 消息标题及内容模板
 */
public enum MessageTemplateEnum {
    INVITE_FAMILY("家庭邀请通知", "【%s】(%s)邀请您加入家庭【%s】(唯一编码:%s),请及时处理."),
    DISSOLVE_FAMILY("家庭解散通知", "家庭【%s】已被【%s】解散,如有疑问,请及时联系【%s】(%s)。"),
    REMOVE_FAMILY("家庭移出通知", "您已被负责人从家庭中移出,如有疑问,请及时联系负责人【%s】(%s)。"),
    APPLY_JOIN_FAMILY("家庭申请通知", "【%s】(%s)申请加入您的家庭,请及时处理。"),
    ;
    private String title;


    private String content;

    MessageTemplateEnum(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
