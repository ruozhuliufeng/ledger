package cn.aixuxi.ledger.vo;

import lombok.Data;

import java.util.Date;

/**
 * 组织人员展示VO
 *
 * @author ruozhuliufeng
 */
@Data
public class LedgerTissueUserVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号·
     */
    private String phone;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 加入时间
     */
    private Date joinTime;
}
