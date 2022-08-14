package cn.aixuxi.ledger.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

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
    private Long id;
    /**
     * 发送人id
     */
    private Long sendUserId;
    /**
     * 消息内容
     */
    private String messageContent;
    /**
     * 接收人id
     */
    private Long receiveUserId;
    /**
     * 消息状态 0：未处理 1：已确认 2：已拒绝
     */
    private Integer messageStatus;
    /**
     * 消息类型 notice：通知 tissue：组织 。。。。。
     */
    private String messageType;
}
