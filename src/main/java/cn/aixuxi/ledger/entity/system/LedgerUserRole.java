package cn.aixuxi.ledger.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("ledger_user_role")
public class LedgerUserRole implements Serializable{
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 角色ID
     */
    private Integer roleId;
}
