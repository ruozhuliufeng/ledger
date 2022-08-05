package cn.aixuxi.ledger.controller;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.service.LedgerRecordService;
import cn.aixuxi.ledger.service.system.LedgerUserRoleService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统管理-角色管理
 *
 * @author ruozhuliufeng
 */
@RestController
@RequestMapping("/record")
@AllArgsConstructor
public class LedgerRecordController {

    private final LedgerRecordService recordService;
    private final LedgerUserService userService;
    private final HttpServletRequest request;
    private final LedgerUserRoleService userRoleService;

    /**
     * 角色信息
     *
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/info/{id}")
    public Result<LedgerRecord> info(@PathVariable("id") Long id) {
        LedgerRecord record = recordService.getById(id);
        return Result.succeed(record);
    }

    /**
     * 角色列表
     *
     * @param name 角色名称
     * @return 角色列表
     */
    @GetMapping("list")
    public Result<Page<LedgerRecord>> list(String name) {
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int size = ServletRequestUtils.getIntParameter(request, "size", 10);
        Page<LedgerRecord> pageData = recordService.page(new Page<>(current, size),
                new QueryWrapper<LedgerRecord>()
                        .like(StrUtil.isNotBlank(name), "name", name));
        return Result.succeed(pageData);
    }

    /**
     * 新建角色
     *
     * @param record 角色信息
     */
    @PostMapping("/save")
    public Result<LedgerRecord> save(@Validated @RequestBody LedgerRecord record) {
        recordService.save(record);
        return Result.succeed(record);
    }

    /**
     * 更新角色
     *
     * @param record 角色信息
     */
    @PostMapping("/update")
    public Result<LedgerRecord> update(@Validated @RequestBody LedgerRecord record) {
        recordService.updateById(record);
        // 更新缓存
        userService.clearUserAuthorityInfoByRoleId(record.getId());
        return Result.succeed(record);
    }

    /**
     * 批量删除角色信息
     *
     * @param ids 角色ID列表
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody List<Long> ids) {
        recordService.removeByIds(ids);
        return Result.succeed();
    }


    /**
     * 微信/支付宝账单导入
     * @param files 导入ZIP文件
     * @param password 解压密码
     */
    @PostMapping("/import/third/recod")
    public Result importRecordByThird(@RequestBody MultipartFile[] files,String password){
        recordService.importRecordByThird(files,password);
        return Result.succeed();
    }
}
