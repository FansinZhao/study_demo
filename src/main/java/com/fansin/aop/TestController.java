package com.fansin.aop;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhaofeng on 17-4-1.
 */
@RestController
public class TestController {

    @RequestMapping("/")
    public void index() {
        System.out.println("Hello World!");
    }

    @RequestMapping("/login/{username}")
    public void login(@PathVariable String username, HttpServletRequest request) {
        System.out.println(String.format("%s用户登录", username));
    }

    @RequestMapping("/a")
    public void toA() {
        System.out.println("A");
    }

    @RequestMapping("/b")
    public void toB() {
        System.out.println("B");
    }

}
