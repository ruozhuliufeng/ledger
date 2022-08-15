package cn.aixuxi.ledger.controller.tissue;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.tissue.LedgerTissue;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueQuery;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueUser;
import cn.aixuxi.ledger.enums.TissueTypeEnum;
import cn.aixuxi.ledger.service.tissue.LedgerTissueService;
import cn.aixuxi.ledger.service.tissue.LedgerTissueUserService;
import cn.aixuxi.ledger.vo.LedgerTissueUserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织-家庭记账控制器
 *
 * @author ruozhuliufeng
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/tissue/family")
public class LedgerTissueFamilyController {
    private final LedgerTissueService tissueService;
    private final LedgerTissueUserService tissueUserService;

    @GetMapping("/query")
    public Result<LedgerTissue> queryFamily(){
        List<LedgerTissue> tissueList = tissueService.queryFamily(TissueTypeEnum.FAMILY.getCode());
        LedgerTissue tissue = tissueList.stream().findAny().orElse(new LedgerTissue());
        return Result.succeed(tissue);
    }

    /**
     * 新增家庭信息
     *
     * @param tissue 家庭信息
     * @return 家庭信息
     */
    @PostMapping("/save")
    public Result<LedgerTissue> save(@RequestBody LedgerTissue tissue) {
        tissue.setTissueType(TissueTypeEnum.FAMILY.getCode());
        return tissueService.saveTissue(tissue);
    }

    /**
     * 查询家庭成员列表
     * @return 家庭成员列表
     */
    @GetMapping("/query/user/{tissueId}")
    public Result<List<LedgerTissueUserVO>> queryUser(@PathVariable("tissueId") Long tissueId) {
        return tissueUserService.queryUserList(tissueId);
    }

    /**
     * 更新家庭名称
     *
     * @param tissue 家庭名称
     */
    @PostMapping("/update")
    public Result update(@RequestBody LedgerTissue tissue) {
        tissueService.updateById(tissue);
        return Result.succeed();
    }

    /**
     * 解散家庭
     * @param id 家庭ID
     */
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return tissueService.delete(id);
    }

    /**
     * 查询待申请的家庭列表
     * @param name 家庭名称
     * @param code 唯一编码
     * @return 家庭列表
     */
    @PostMapping("/query/list")
    public Result queryList(@RequestParam("name") String name,@RequestParam("code") String code){

        return Result.succeed();
    }

    /**
     * 批量删除家庭成员
     * @param query ID列表
     */
    @PostMapping("/delete/user/list")
    public Result deleteUser(@RequestBody LedgerTissueQuery query){
        if (CollectionUtils.isEmpty(query.getTissueUserIds())){
            return Result.failed("请选择人员");
        }
        tissueService.deleteUser(query.getTissueUserIds());
        return Result.succeed();
    }

    /**
     * 更新家庭成员昵称
     * @param tissueUser 家庭成员昵称
     */
    @PostMapping("/update/user")
    public Result updateUser(@RequestBody LedgerTissueUser tissueUser){
        tissueUserService.updateById(tissueUser);
        return Result.succeed();
    }

}
