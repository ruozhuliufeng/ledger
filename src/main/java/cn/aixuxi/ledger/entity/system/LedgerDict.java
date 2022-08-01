package cn.aixuxi.ledger.entity.system;

import cn.aixuxi.ledger.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典表
 */
@Data
@TableName("ledger_dict")
public class LedgerDict implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 父主键
     */
    private Long parentId;
    /**
     * 字典码
     */
    private String code;
    /**
     * 字典值
     */
    private String dictKey;
    /**
     * 字典名称
     */
    private String dictValue;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 字典备注
     */
    private String remark;
    /**
     * 是否已封存
     */
    private Integer isSealed;
    /**
     * 是否已删除
     */
    private Integer isDeleted;
}
