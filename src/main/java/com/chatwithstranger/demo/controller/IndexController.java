package com.chatwithstranger.demo.controller;

import com.chatwithstranger.demo.controller.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping("/")
    public ModelAndView displayIndexPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("user", new UserVO(null, null));
        return mav;
    }
}
