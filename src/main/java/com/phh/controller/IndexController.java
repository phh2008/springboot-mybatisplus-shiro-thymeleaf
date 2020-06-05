package com.phh.controller;

import com.phh.entity.mem.User;
import com.phh.event.StringEvent;
import com.phh.service.mem.ISeqIdService;
import com.phh.service.mem.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 主页
 *
 * @author phh
 * @version V1.0
 * @date 2019/5/16
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ISeqIdService seqIdService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = {"", "/"})
    public String index(Model model) {
        model.addAttribute("date", new Date());
        model.addAttribute("newDate", LocalDateTime.now());
        return "/index";
    }

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login")
    public String login() {
        User user = getUserVO();
        if (user != null) {
            return "redirect:/";
        } else {
            return "/login";
        }
    }

    /**
     * 未授权
     *
     * @return
     */
    @RequestMapping(value = {"/unauthorized"})
    public String unauthorized() {
        return "/unauthorized";
    }


    /**
     * 404
     *
     * @return
     */
    @RequestMapping(value = "/404")
    public String notFound() {
        //do something
        return "/404";
    }

    @RequestMapping("/test")
    public String test(Model model) {
        User user = userService.query().one();
        model.addAttribute("user", user);
        return "/test";
    }

    @ResponseBody
    @RequestMapping("/test2")
    public User test2(String username) {

        publisher.publishEvent(new StringEvent(this, username));

        return userService.getByUsername(username);
    }

    @ResponseBody
    @RequestMapping("/test3")
    public String test3() {
        return seqIdService.getOdrNo();
    }

}
