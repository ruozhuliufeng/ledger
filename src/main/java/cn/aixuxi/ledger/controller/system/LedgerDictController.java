package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.system.LedgerDict;
import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.service.system.LedgerDictService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 数据字典 Controller
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/dict")
public class LedgerDictController {

    private final LedgerDictService dictService;
    private final HttpServletRequest request;

    /**
     * 数据字典列表分页查询
     *
     * @param parentId 父主键
     * @param dictCode 字典编码
     * @param dictName 字典名称
     * @return 分页数据字典数据
     */
    @GetMapping("/list/{parentId}")
    public Result<Page<LedgerDict>> list(@PathVariable Long parentId, String dictCode, String dictName) {
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int size = ServletRequestUtils.getIntParameter(request, "size", 10);
        Page<LedgerDict> pageData = dictService.page(new Page<>(current, size), new QueryWrapper<LedgerDict>()
                .eq("parent_id", parentId)
                .like(StrUtil.isNotBlank(dictCode), "code", dictCode)
                .like(StrUtil.isNotBlank(dictName), "dict_value", dictName));
        return Result.succeed(pageData);
    }


    /**
     * 根据字典ID获取字典信息
     *
     * @param id 字典ID
     * @return 字典信息
     */
    @GetMapping("/query/{id}")
    public Result<LedgerDict> queryDict(@PathVariable("id") Long id) {
        LedgerDict dict = dictService.getById(id);
        return Result.succeed(dict);
    }

    /**
     * 新增数据字典
     *
     * @param dict 数据字典
     * @return 新增结果
     */
    @PostMapping("/save")
    public Result save(@RequestBody LedgerDict dict) {
        dictService.save(dict);
        return Result.succeed();
    }

    /**
     * 更新数据字典详情
     *
     * @param dict 数据字典详情
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody LedgerDict dict) {
        dictService.updateById(dict);
        return Result.succeed();
    }

    /**
     * 删除数据字典
     *
     * @param id 数据字典主键
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        dictService.removeById(id);
        return Result.succeed();
    }

}

