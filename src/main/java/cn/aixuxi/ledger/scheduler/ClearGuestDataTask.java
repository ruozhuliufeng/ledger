package cn.aixuxi.ledger.scheduler;

import cn.aixuxi.ledger.constant.LedgerConstant;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.system.LedgerRole;
import cn.aixuxi.ledger.entity.system.LedgerUserRole;
import cn.aixuxi.ledger.service.LedgerRecordService;
import cn.aixuxi.ledger.service.system.LedgerRoleService;
import cn.aixuxi.ledger.service.system.LedgerUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务-每天上午2点清理访客角色的收支记录
 */
@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
public class ClearGuestDataTask {

    private final LedgerRoleService roleService;
    private final LedgerUserRoleService userRoleService;
    private final LedgerRecordService recordService;

    /**
     * 每天上午2点清理访客角色下的收支记录
     */
    @Scheduled(cron = "* * 2 * * ?")
    private void clearGuestData(){
        LedgerRole role = roleService.getOne(new QueryWrapper<LedgerRole>().eq("role_code", LedgerConstant.GUEST_ROLE));
        if (ObjectUtils.isEmpty(role)){
            log.warn("未建立访客角色,无法处理该操作");
            return;
        }
        List<LedgerUserRole> userRoles = userRoleService.list(new QueryWrapper<LedgerUserRole>().eq("role_id",role.getId()));
        if (CollectionUtils.isEmpty(userRoles)){
            log.warn("该角色未分配给用户,无法处理该操作");
            return;
        }
        List<Long> userIds = userRoles.stream().map(LedgerUserRole::getUserId).collect(Collectors.toList());
        // 清除记录
        recordService.remove(new QueryWrapper<LedgerRecord>().in("user_id",userIds));
    }
}
