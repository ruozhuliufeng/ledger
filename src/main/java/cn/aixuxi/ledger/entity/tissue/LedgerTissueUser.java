package cn.aixuxi.ledger.entity.tissue;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织-用户关系表
 * @author ruozhuliufeng
 */
@Data
@TableName("ledger_tissue_user")
public class LedgerTissueUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 组织id
     */
    private Long tissueId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 加入时间
     */
    private Date joinTime;
}
