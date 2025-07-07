package org.example.teacherservice.controller;


import org.example.teachercommon.entity.JwtUserInfo;
import org.example.teacherservice.util.UserContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("service/assignment")
public class AssignmentController {

    @GetMapping("a")
    public Boolean isAssigned() {
        System.out.println("进入测试");
        // 从上下文中获取用户信息
        JwtUserInfo currentUser = UserContext.get();
        System.out.println(currentUser.getUserId());
        System.out.println(currentUser.getUsername());
        System.out.println(currentUser.getUserType());
        return true;
    }

}
