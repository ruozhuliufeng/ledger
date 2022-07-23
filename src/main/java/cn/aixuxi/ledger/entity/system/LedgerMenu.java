package cn.aixuxi.ledger.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("ledger_menu")
public class LedgerMenu implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 父级菜单
     */
    private Integer parentId;
    /**
     * 操作类型
     */
    private Integer action;
    /**
     * 菜单别名
     */
    private String alias;
    /**
     * 菜单类型
     */
    private Integer category;
    /**
     * 菜单编号(如：user:list,user:create)
     */
    private String code;
    /**
     * 组件地址
     */
    private String component;

    /**
     * 是否删除
     */
    private Integer isDeleted;
    /**
     * 是否打开新页面
     */
    private Integer isOpen;
    /**
     * 菜单名称
     */
    private String name;

    /**
     * 请求地址
     */
    private String path;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 菜单资源
     */
    private String source;

    @TableField(exist = false)
    private List<LedgerMenu> childen = new ArrayList<>();
}
