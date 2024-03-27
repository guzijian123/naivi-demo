package com.gzj.demo.service;

import com.gzj.demo.domain.DUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gzj.demo.pojo.LoginDto;

/**
* @author 20412
* @description 针对表【d_user】的数据库操作Service
* @createDate 2024-03-21 16:47:02
*/
public interface DUserService extends IService<DUser> {

    void sendCode(String email);

    String login(LoginDto dto);
}
