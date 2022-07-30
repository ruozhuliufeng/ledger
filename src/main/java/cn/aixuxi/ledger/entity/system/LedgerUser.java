package cn.aixuxi.ledger.entity.system;

import cn.aixuxi.ledger.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 用户主表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ledger_user")
public class LedgerUser extends BaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 账号
     */
    private String account;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 性别
     */
    private String sex;
    /**
     * 是否删除
     */
    private Integer isDeleted;
    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 用户角色
     */
    @TableField(exist = false)
    private List<LedgerRole> roles;
}
