package cn.aixuxi.ledger.controller.system;

import cn.aixuxi.ledger.entity.system.LedgerUser;
import cn.aixuxi.ledger.service.system.LedgerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class LedgerUserController {

    @Autowired
    private LedgerUserService userService;

    @PostMapping("/query/list")
    public List<LedgerUser> queryList(){
        return userService.list();
    }
}
