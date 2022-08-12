package cn.aixuxi.ledger.utils;

import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 安全工具类
 */
@RequiredArgsConstructor
@Component
public class SecureUtil {
    private final LedgerUserService userService;

    public Long getUserId(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LedgerUser user = userService.getByUsername(username);
        if (ObjectUtils.isEmpty(user)){
            return null;
        }
        return user.getId();
    }
}
