package cn.aixuxi.ledger.entity;

import lombok.Data;

/**
 * 通用查询条件
 *
 * @author ruozhuliufeng
 */
@Data
public class BasePageQuery {
    /**
     * 当前页
     */
    private Integer current = 1;
    /**
     * 每页的数量
     */
    private Integer size = 10;
    /**
     * 正序字段
     */
    private String ascs;
    /**
     * 倒序字段
     */
    private String descs;
}
