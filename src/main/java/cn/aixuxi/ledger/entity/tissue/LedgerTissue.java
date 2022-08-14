package cn.aixuxi.ledger.entity.tissue;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ledger_tissue")
public class LedgerTissue implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
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
    private String tissueType;
    /**
     * 组织描述
     */
    private String tissueDescription;
    /**
     * 负责人id
     */
    private Long tissueLeader;
}
