package com.chatwithstranger.demo.controller;

import com.chatwithstranger.demo.service.UserService;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void signupViewTest() throws Exception {
        // Arrange
        String url = "/signup";

        // Act
        ResultActions actual = this.mockMvc.perform(get(url)).andDo(print());
        ModelAndView actualMAV = actual.andReturn().getModelAndView();

        // Assert
        actual.andExpect(status().isOk());
        assertEquals("signup", actualMAV.getViewName());
    }

    @Test
    public void afterLeaveViewTest() throws Exception {
        // Arrange
        String url = "/logout";

        // Act
        ResultActions actual = this.mockMvc.perform(get(url)).andDo(print());
        ModelAndView actualMAV = actual.andReturn().getModelAndView();

        // Assert
        actual.andExpect(status().isOk());
        assertEquals("index", actualMAV.getViewName());
    }
}
