package cn.aixuxi.ledger.service.system.impl;

import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.entity.system.LedgerMenu;
import cn.aixuxi.ledger.entity.system.LedgerRole;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.mapper.LedgerMenuMapper;
import cn.aixuxi.ledger.mapper.LedgerUserMapper;
import cn.aixuxi.ledger.properties.LedgerSmmsProperties;
import cn.aixuxi.ledger.service.system.LedgerRoleService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.aixuxi.ledger.utils.RedisUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 服务实现类
 *
 * @author ruozhuliufeng
 */
@Service
@AllArgsConstructor
public class LedgerUserServiceImpl extends ServiceImpl<LedgerUserMapper, LedgerUser>
        implements LedgerUserService {

    private final LedgerRoleService roleService;
    private final LedgerMenuMapper menuMapper;
    private final RedisUtil redisUtil;
    private final static String GRANTED_AUTHORITY_KEY = "GrantedAuthority:";
    private final LedgerSmmsProperties smmsProperties;

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public LedgerUser getByUsername(String username) {
        return getOne(new QueryWrapper<LedgerUser>().eq("account", username));
    }

    /**
     * 获取用户权限信息
     *
     * @param userId 用户id
     * @return 用户权限信息
     */
    @Override
    public String getUserAuthorityInfo(Long userId) {
        LedgerUser user = this.getById(userId);
        // ROLE_admin,sys:user:list,....
        String authority = "";
        if (redisUtil.hasKey(GRANTED_AUTHORITY_KEY + user.getAccount())) {
            authority = (String) redisUtil.get(GRANTED_AUTHORITY_KEY + user.getAccount());
        } else {
            // 获取角色编码
            List<LedgerRole> roleList = roleService.list(
                    new QueryWrapper<LedgerRole>()
                            .inSql("id", "select role_id from ledger_user_role where user_id = " + userId));
            if (roleList.size() > 0) {
                String roleCodes = roleList.stream().map(role -> "ROLE_" + role.getRoleCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            // 获取菜单编码
            List<Integer> menuIds = this.baseMapper.getNavMenuIds(userId);
            if (menuIds.size() > 0) {
                List<LedgerMenu> menuList = menuMapper.selectBatchIds(menuIds);
                String menuPerms = menuList.stream().map(LedgerMenu::getCode).collect(Collectors.joining(","));
                authority = authority.concat(menuPerms);
            }
            redisUtil.set(GRANTED_AUTHORITY_KEY + user.getAccount(), authority);
        }
        return authority;
    }

    /**
     * 清除用户权限信息
     *
     * @param username 用户名
     */
    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del(GRANTED_AUTHORITY_KEY + username);
    }

    /**
     * 根据角色id清除用户权限信息
     *
     * @param roleId 角色id
     */
    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<LedgerUser> userList = this.list(
                new QueryWrapper<LedgerUser>()
                        .inSql("id", "select user_id from ledger_user_role where role_id = " + roleId)
        );
        userList.forEach(user -> {
            this.clearUserAuthorityInfo(user.getAccount());
        });
    }

    /**
     * 根据菜单id清除用户权限信息
     *
     * @param menuId 菜单id
     */
    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<LedgerUser> userList = this.baseMapper.listByMenuId(menuId);
        userList.forEach(user -> {
            this.clearUserAuthorityInfo(user.getAccount());
        });
    }

    /**
     * 头像上传
     *
     * @param avatarFile 头像文件
     * @return 头像路径
     */
    @Override
    public String uploadAvatar(MultipartFile avatarFile) {
        String token = getSmmsToken();
        Map<String,Object> params = new HashMap<>();
        params.put("smfile", avatarFile);
        String resultBody = HttpRequest.post(LedgerConstant.SM_MS_UPLOAD_IMAGE)
                .header("Authorization", token)
                .form(params)
                .timeout(30000)
                .execute().body();
        JSONObject result = JSONUtil.parseObj(resultBody);
        JSONObject data = result.getJSONObject("data");
        return data.getStr("url");
    }

    private String getSmmsToken() {
        String url = LedgerConstant.SMMS_GET_API_TOKEN + "?username=" + smmsProperties.getUsername() + "&password=" + smmsProperties.getPassword();
        String resultBody = HttpRequest.post(url)
                .keepAlive(true)
                .timeout(30000)
                .execute().body();
        JSONObject result = JSONUtil.parseObj(resultBody);
        JSONObject data = result.getJSONObject("data");
        return data.getStr("token");
    }
}
