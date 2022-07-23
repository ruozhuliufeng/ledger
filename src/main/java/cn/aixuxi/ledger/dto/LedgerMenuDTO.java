package cn.aixuxi.ledger.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {
 * 					name: 'SysUser',
 * 					title: '用户管理',
 * 					icon: 'el-icon-s-custom',
 * 					path: '/sys/users',
 * 					component: 'sys/User',
 * 					children: []
 *                                },
 */
@Data
public class LedgerMenuDTO implements Serializable{
    private Integer id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<LedgerMenuDTO> children = new ArrayList<>();

}
