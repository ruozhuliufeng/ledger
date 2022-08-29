package cn.aixuxi.ledger.utils;

import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 安全工具类
 *
 * @author ruozhuliufeng
 */
@RequiredArgsConstructor
@Component
public class SecureUtil {
    private final LedgerUserService userService;

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Long getUserId(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LedgerUser user = userService.getByUsername(username);
        if (ObjectUtils.isEmpty(user)){
            return null;
        }
        return user.getId();
    }

    /**
     * 获取用户
     * @return 用户信息
     */
    public LedgerUser getUser(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LedgerUser user = userService.getByUsername(username);
        if (ObjectUtils.isEmpty(user)){
            return null;
        }
        return user;
    }
}
