package com.gzj.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzj.demo.common.BizException;
import com.gzj.demo.common.ResultCode;
import com.gzj.demo.domain.DUser;
import com.gzj.demo.pojo.LoginDto;
import com.gzj.demo.service.DUserService;
import com.gzj.demo.mapper.DUserMapper;
import com.gzj.demo.utils.EmailUtils;
import com.gzj.demo.utils.JwtUtils;
import com.gzj.demo.utils.ValidateCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 20412
 * @description 针对表【d_user】的数据库操作Service实现
 * @createDate 2024-03-21 16:47:02
 */
@Service
public class DUserServiceImpl extends ServiceImpl<DUserMapper, DUser>
        implements DUserService {

    @Resource
    private EmailUtils emailUtils;

    @Override
    public void sendCode(String email) {
        //随机生成6位数字验证码
        String validateCode = ValidateCodeUtil.generatorCode(4);
        //给用户发送验证码
//        SMSUtils.sendMessage("", tel, validateCode.toString(), "");
        // 保存redis
        System.out.println("code:" + validateCode);
//        redisCache.setCacheObject(email, validateCode, 60, TimeUnit.SECONDS);
        String title = "登录验证码";
        String message = validateCode;
//        emailUtils.sendEmail();
    }

    @Override
    public String login(LoginDto dto) {
        LambdaQueryWrapper<DUser> qw = new LambdaQueryWrapper<>();
        qw.eq(DUser::getEmail, dto.getEmail())
                .eq(DUser::getPassword, dto.getPassword());
        DUser user = getOne(qw);
        if (user != null) {
            return JwtUtils.createToken(dto.getEmail(), dto.getPassword());
        }
        throw new BizException(ResultCode.CODE_400.getCode(), "账号密码错误");
    }
}




