package com.phh.controller.mem;


import com.phh.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author phh
 * @since 2019-05-17
 */
@Controller
@RequestMapping("/mem/user")
public class UserController extends BaseController {

    @RequestMapping("/info")
    public String info() {

        return "/mem/user/info";
    }

    @RequiresPermissions({"mem:user:del"})
    @RequestMapping("/del")
    public String del() {

        return "/mem/user/del";
    }

}
