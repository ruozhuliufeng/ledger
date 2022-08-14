package cn.aixuxi.ledger.controller.tissue;

import cn.aixuxi.ledger.service.tissue.LedgerTissueService;
import cn.aixuxi.ledger.service.tissue.LedgerTissueUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
