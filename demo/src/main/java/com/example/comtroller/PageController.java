package com.example.comtroller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wxh
 * @Package com.example.demo
 * @Description: TODO
 * @date 2018/6/4 0004 上午 10:39
 */
@Controller
@Api(value = "页面接口", tags = {"测试 页面接口接口"})
public class PageController {

    @GetMapping("/hello")
    public ModelAndView hello(){
        ModelAndView modelAndView = new ModelAndView("hello");
        String a = "aaaaaaaaa ";
        String v = "ss";


        return modelAndView;
    }
}
