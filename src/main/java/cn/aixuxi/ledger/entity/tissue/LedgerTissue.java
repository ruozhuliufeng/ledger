package cn.aixuxi.ledger.entity.tissue;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("ledger_tissue")
public class LedgerTissue implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 组织名称
     */
    private String tissueName;
    /**
     * 组织编码
     */
    private String tissueCode;
    /**
     * 组织类型 0:家庭 1:团队
     */
    private Integer tissueType;
    /**
     * 组织描述
     */
    private String tissueDescription;
    /**
     * 负责人id
     */
    private Long tissueLeader;
    /**
     * 初始金额
     */
    private BigDecimal initialAmount;
}
