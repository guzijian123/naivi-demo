package com.gzj.demo.controller;

import com.gzj.demo.common.Result;
import com.gzj.demo.pojo.LoginDto;
import com.gzj.demo.service.DUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private DUserService userService;

    @GetMapping("/sendCode")
    public Result<?> sendCode (@RequestParam("email") String email) {
        userService.sendCode(email);
        return Result.OK();
    }

    @PostMapping("/login")
    public Result<String> login (@RequestBody LoginDto dto) {
        return Result.OK(userService.login(dto));
    }
}
