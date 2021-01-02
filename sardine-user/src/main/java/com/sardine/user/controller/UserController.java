package com.sardine.user.controller;

import com.sardine.common.entity.dto.UserDto;
import com.sardine.common.entity.http.CommonResult;
import com.sardine.common.exception.SardineRuntimeException;
import com.sardine.common.util.JwtUtils;
import com.sardine.user.entity.vo.UserVo;
import com.sardine.user.properties.JwtProperties;
import com.sardine.user.service.UserService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    public CommonResult<String> login(@Valid @RequestBody UserVo userVo, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
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
    public CommonResult<Boolean> signup(@Valid @RequestBody UserVo userVo, BindingResult result,
                                        @RequestParam("code") String code){
        if (result.hasFieldErrors())
            throw new SardineRuntimeException(result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("|")));
        userService.insertOne(userVo, code);
        return CommonResult.success("创建用户成功");
    }

    @PostMapping("identify")
    @ApiOperation("解析Token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token令牌")
    })
    public CommonResult<UserDto> identify(@RequestParam(value = "token", required = false) String token) {
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
}
