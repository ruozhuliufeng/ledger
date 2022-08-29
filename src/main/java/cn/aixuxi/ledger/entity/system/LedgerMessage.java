package cn.aixuxi.ledger.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息中心表
 *
 * @author ruozhuliufeng
 */
@Data
@TableName("ledger_message")
public class LedgerMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 发送人id
     */
    private Long sendUserId;
    /**
     * 发送用户名称
     */
    @TableField(exist = false)
    private String sendUserName;
    /**
     * 消息内容
     */
    private String messageContent;
    /**
     * 接收人id
     */
    private Long receiveUserId;
    /**
     * 消息状态 0：未处理 1：已确认
     */
    private Integer messageStatus;
    /**
     * 消息类型 notice：通知 tissue：组织 。。。。。
     */
    private String messageType;
    /**
     * 消息标题
     */
    private String messageTitle;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 关联业务ID
     */
    private Long businessId;
}
