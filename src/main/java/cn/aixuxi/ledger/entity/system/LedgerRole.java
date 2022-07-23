package cn.aixuxi.ledger.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色信息
 */
@Data
@TableName("ledger_role")
public class LedgerRole implements Serializable{
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 是否删除
     */
    private Integer isDeleted;
    /**
     * 上级ID
     */
    private Integer parentId;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 排序
     */
    private Integer sort;
}
