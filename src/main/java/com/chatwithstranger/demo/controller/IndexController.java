package com.chatwithstranger.demo.controller;

import com.chatwithstranger.demo.controller.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping("/")
    public ModelAndView displayIndexPage() {
        return new ModelAndView("index");
    }

    @GetMapping("/signup")
    public ModelAndView displaySignupPage(@ModelAttribute("user") UserVO userVO) {
        if (userVO == null)
            userVO = new UserVO();
        return createNewModelAndView("signup", userVO, "user");
    }

    @PostMapping("/signup")
    public ModelAndView signupAndDisplayIndexView(@ModelAttribute("user") UserVO userVO) {
        // TODO : signup - post method
        return createNewModelAndView("index", userVO, "user");
    }

    private ModelAndView createNewModelAndView(String viewName, Object attributeValue, String attributeName) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(viewName);
        if (attributeValue != null)
            mav.addObject(attributeName, attributeValue);
        return mav;
    }

}
