package com.sardine.user.app.controller;

import com.sardine.common.entity.domain.UserDto;
import com.sardine.common.entity.http.CommonResult;
import com.sardine.common.exception.SardineRuntimeException;
import com.sardine.common.util.JwtUtils;
import com.sardine.user.app.entity.vo.UserVo;
import com.sardine.user.app.properties.JwtProperties;
import com.sardine.user.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 用户控制层
 *
 * @author Keith
 */
@Slf4j
@RestController
@Api(tags = "用户管理相关接口")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    JwtProperties jwtProperties;

    @PostMapping("login")
    @ApiOperation("登录")
    public CommonResult<String> login(@Valid @RequestBody UserVo userVo, BindingResult result) {
        if (result.hasFieldErrors())
            throw new SardineRuntimeException(result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("|")));
        String token = userService.identify(userVo.getUsername(), userVo.getPassword());
        if (StringUtils.isBlank(token))
            return CommonResult.failed("身份未认证", "");
        return CommonResult.success("登录成功", token);
    }

    @PostMapping("signup")
    @ApiOperation("注册")
    public CommonResult<Boolean> signup(@Valid @RequestBody UserVo userVo, BindingResult result){
        if (result.hasFieldErrors())
            throw new SardineRuntimeException(result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("|")));
        userService.insertOne(userVo);
        return CommonResult.success("用户注册成功");
    }

    @PostMapping("code")
    @ApiOperation("发送手机验证码")
    public CommonResult<Void> code(String phone){
        userService.sendCode(phone);
        return CommonResult.success();
    }

    @PostMapping("identify")
    @ApiOperation("解析Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token令牌")
    })
    public CommonResult<UserDto> identify(@RequestParam String token) {
        UserDto userInfo = null;
        try {
            userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            return CommonResult.failed(null);
        }
        return CommonResult.success(userInfo);
    }

    @GetMapping("hello")
    public String hello(){
        return "Hello World";
    }

    public static void main(String[] args) {
        ExecutorService pool = new ThreadPoolExecutor(
                2, 5, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        try {
            for (int i = 0; i < 10; i++) {
                final int fi = i;
                pool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t" + fi);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    static class MyThreadFactory implements ThreadFactory{

        @Override
        public Thread newThread(Runnable r) {
            return new Thread();
        }
    }
}
