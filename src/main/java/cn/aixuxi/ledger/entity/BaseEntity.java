package cn.aixuxi.ledger.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    /**
     * 创建人
     */
    private Integer createUser;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private Integer updateUser;
    /**
     * 更新时间
     */
    private Date updateTime;

}

