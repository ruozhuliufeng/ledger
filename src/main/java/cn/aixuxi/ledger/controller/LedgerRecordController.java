package cn.aixuxi.ledger.controller;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.service.LedgerRecordService;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统管理-交易管理
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

    /**
     * 交易信息
     *
     * @param id 交易ID
     * @return 交易信息
     */
    @GetMapping("/info/{id}")
    public Result<LedgerRecord> info(@PathVariable("id") Long id) {
        LedgerRecord record = recordService.getById(id);
        return Result.succeed(record);
    }

    /**
     * 交易列表
     *
     * @param name 交易名称
     * @return 交易列表
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
     * 新建交易
     *
     * @param record 交易信息
     */
    @PostMapping("/save")
    public Result<LedgerRecord> save(@Validated @RequestBody LedgerRecord record) {
        recordService.saveRecord(record);
        return Result.succeed(record);
    }

    /**
     * 更新交易
     *
     * @param record 交易信息
     */
    @PostMapping("/update")
    public Result<LedgerRecord> update(@Validated @RequestBody LedgerRecord record) {
        recordService.updateById(record);
        // 更新缓存
        userService.clearUserAuthorityInfoByRoleId(record.getId());
        return Result.succeed(record);
    }

    /**
     * 批量删除交易信息
     *
     * @param ids 交易ID列表
     */
    @DeleteMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") Long[] ids) {
        recordService.removeByIds(Arrays.asList(ids));
        return Result.succeed();
    }


    /**
     * 微信/支付宝账单导入
     *
     * @param file     导入ZIP文件
     * @param password 解压密码
     */
    @PostMapping("/import/third/record")
    public Result importRecordByThird(@RequestParam(value = "file",required = true) MultipartFile file,@RequestParam(value = "password",required = false) String password) {
        if (ObjectUtils.isEmpty(file)) {
            return Result.failed("文件信息不能为空");
        }
        recordService.importRecordByThird(file, password);
        return Result.succeed();
    }

    /**
     * 查询交易分类列表
     *
     * @return 交易分类列表
     */
    @GetMapping("/query/category/list")
    public Result<List<String>> queryCategoryList() {
        List<LedgerRecord> recordList = recordService.list();
        List<String> categoryNames = recordList.stream().map(LedgerRecord::getTransactionCategory).collect(Collectors.toList());
        return Result.succeed(categoryNames);
    }
    // TODO 提供导入模板，导入数据
}
