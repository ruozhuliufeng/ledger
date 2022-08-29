package cn.aixuxi.ledger.controller.tissue;

import cn.aixuxi.ledger.common.Result;
import cn.aixuxi.ledger.entity.LedgerRecord;
import cn.aixuxi.ledger.entity.tissue.LedgerTissue;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueQuery;
import cn.aixuxi.ledger.entity.tissue.LedgerTissueUser;
import cn.aixuxi.ledger.enums.TissueTypeEnum;
import cn.aixuxi.ledger.service.tissue.LedgerTissueService;
import cn.aixuxi.ledger.service.tissue.LedgerTissueUserService;
import cn.aixuxi.ledger.utils.SecureUtil;
import cn.aixuxi.ledger.vo.LedgerTissueUserVO;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
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
    private final SecureUtil secureUtil;

    /**
     * 查询当前登录用户的家庭信息
     * @return 家庭信息
     */
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
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        return tissueService.delete(id);
    }

    /**
     * 查询待申请的家庭列表
     * @param query 查询条件
     * @return 家庭列表
     */
    @PostMapping("/query/list")
    public Result<Page<LedgerTissue>> queryList(@RequestBody LedgerTissueQuery query){
        Page<LedgerTissue> pageData = tissueService.page(new Page<>(query.getCurrent(), query.getSize()),
                new QueryWrapper<LedgerTissue>()
                        .like(StrUtil.isNotBlank(query.getName()), "tissue_name", query.getName())
                        .like(StrUtil.isNotBlank(query.getCode()), "tissue_code", query.getCode())
                        .eq("tissue_code",TissueTypeEnum.FAMILY.getCode())
        );
        return Result.succeed(pageData);
    }

    /**
     * 删除家庭成员
     * @param id ID列表
     */
    @PutMapping("/delete/user/{id}")
    public Result deleteUser(@PathVariable("id") Long id){
        if (ObjectUtils.isEmpty(id)){
            return Result.failed("请选择人员");
        }
        tissueService.deleteUser(id);
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

    /**
     * 家庭记录查询
     * @return 收支记录
     */
    @PostMapping("/query/record")
    public Result<IPage<LedgerRecord>> queryFamilyRecord(@RequestBody LedgerTissueQuery query){
        if (ObjectUtils.isEmpty(query.getTissueId())){
            return Result.failed("未加入家庭，无收支记录");
        }
        return tissueService.queryRecordList(query);
    }


    /**
     * 申请加入家庭
     * @param tissueId 家庭ID
     */
    @GetMapping("/apply/join/{tissueId}")
    public Result applyJoinFamily(@PathVariable("tissueId") Long tissueId){
        tissueService.applyJoinFamily(tissueId);
        return Result.succeed("您的申请已提交,请等待负责人员审核。");
    }

    /**
     * 邀请加入家庭
     * @param userId 邀请用户ID
     * @return
     */
    @GetMapping("/invite/join/{userId}")
    public Result inviteJoinFamily(@PathVariable("userId") Long userId){
        LedgerTissue tissue = tissueService.getOne(new QueryWrapper<LedgerTissue>()
                .eq("tissue_leader",secureUtil.getUserId())
                .eq("tissue_type",TissueTypeEnum.FAMILY.getCode()));
        if (ObjectUtils.isEmpty(tissue)){
            return Result.failed("您不是家庭的负责人,请联系负责人处理.");
        }
        tissueService.inviteJoinFamily(tissue,userId);
        return Result.succeed();
    }
}
