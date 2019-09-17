package com.chatwithstranger.demo.controller;

import com.chatwithstranger.demo.controller.vo.UserVO;
import com.chatwithstranger.demo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

@Controller
public class ChatController {
    @GetMapping("/chat")
    public ModelAndView displayChatView(@ModelAttribute("user") UserVO userVO
            , HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();

        if (session == null || session.getAttribute("loginInfo") == null) {
            out.println("<script>alert('Invalid access has been detected.');</script>");
            out.flush();

            return createNewModelAndView("redirect:/index", null, null);
        } else {

            ModelAndView mav = createNewModelAndView("chat", null, null);
            mav.addObject("webSocketUrl", "ws://" + InetAddress.getLocalHost().getHostAddress()
                    + ":" + request.getServerPort() + request.getContextPath() + "/open/" + session.getAttribute("loginInfo"));

            return mav;
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
