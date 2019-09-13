package com.chatwithstranger.demo.controller;

import com.chatwithstranger.demo.controller.vo.UserVO;
import com.chatwithstranger.demo.service.UserService;
import com.chatwithstranger.demo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public ModelAndView displaySignupPage(@ModelAttribute("user") UserVO userVO) {
        if (userVO == null)
            userVO = new UserVO();
        return createNewModelAndView("signup", userVO, "user");
    }

    @PostMapping("/signup")
    public ModelAndView signupAndDisplayIndexView(@ModelAttribute("user") UserVO userVO, HttpServletResponse response) throws IOException {
        Optional<User> checkUser = userService.findUserByUsername(userVO.getUsername());
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (checkUser.isPresent()) {
            out.println("<script>alert('username already exist.');</script>");
            out.flush();
            return createNewModelAndView("signup", null, null);
        } else {
            User user = User.createWithAllArgs(userVO.getUsername(), userVO.getPassword(), sdf.format(new Date()));
            userService.signupUser(user);
            out.println("<script>alert('welcome!!');</script>");
            out.flush();
            return createNewModelAndView("index", userVO, "user");
        }
    }

    @PostMapping("/signin")
    public ModelAndView signinAndDisplayChatView(@ModelAttribute("user") UserVO userVO
            , HttpServletRequest servletRequest, HttpServletResponse response) throws IOException {
        Optional<User> checkUser = userService.findUserByUsernameAndPassword(userVO.getUsername(), userVO.getPassword());
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (checkUser.isPresent()) {
            servletRequest.getSession().setAttribute("loginInfo", checkUser.get().getUsername());

            out.println("<script>alert('Last Login : " + checkUser.get().getLastLogin() + "');</script>");
            out.flush();

            checkUser.get().setLastLogin(sdf.format(new Date()));
            userService.updateUser(checkUser.get());

            return createNewModelAndView("chat", userVO, "user");
        } else {
            out.println("<script>alert('incorrect username or password. try again.');</script>");
            out.flush();
            return createNewModelAndView("index", userVO, "user");
        }
    }

    @GetMapping("/logout")
    public ModelAndView logoutAndDisplayIndexView(HttpServletRequest servletRequest) {
        servletRequest.getSession().invalidate();
        servletRequest.getSession().removeAttribute("loginInfo");

        return createNewModelAndView("index", User.createInitialUser(), "user");
    }

    private ModelAndView createNewModelAndView(String viewName, Object attributeValue, String attributeName) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(viewName);
        if (attributeValue != null)
            mav.addObject(attributeName, attributeValue);
        return mav;
    }
}
