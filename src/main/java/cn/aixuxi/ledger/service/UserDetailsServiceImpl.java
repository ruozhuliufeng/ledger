package cn.aixuxi.ledger.service;

import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.security.AccountUser;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Security User Details Service
 *
 * @author ruozhuliufeng
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final LedgerUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LedgerUser user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new AccountUser(user.getId(), user.getAccount(), user.getPassword(), getUserAuthority(user.getId()));
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     *
     * @param id 用户id
     * @return 用户权限信息
     */
    private List<GrantedAuthority> getUserAuthority(Integer id) {
        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
        String authority = userService.getUserAuthorityInfo(id);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
