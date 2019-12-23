package com.gpdi.operatingunit.controller.system;

import com.gpdi.operatingunit.config.JwtToken;
import com.gpdi.operatingunit.config.R;
import com.gpdi.operatingunit.dao.system.SysUserMapper;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.system.SysUserService;
import com.gpdi.operatingunit.utils.JwtUtil;
import com.gpdi.operatingunit.utils.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Lxq
 * @Date: 2019/9/6 14:21
 */
@RestController
@RequestMapping("/user")
@Api(description = "用户登录管理控制器")
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysUserMapper sysUserMapper;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public R userLogin(String username, String password) {
        try {
            SysUser sysUser = sysUserService.queryUserByUserName(username);
            if (sysUser == null || !sysUser.getPassword().equals(
                    new Sha256Hash(password, sysUser.getSalt(), 3).toString()
            )) {
                return R.error("账号不存在或密码不正确");
            }
            //账号密码正确,创建token
            String token = JwtUtil.sign(username, new Sha256Hash(password, sysUser.getSalt(), 3).toString());
            // 将token 放到redis 中
            redisTemplate.opsForValue().set(token, token, 2, TimeUnit.HOURS);
            // 登录一下保存用户在securityUtils
            JwtToken jwtToken = new JwtToken(token);
            // 提交给realm进行登入
            Subject subject = SecurityUtils.getSubject();
            subject.login(jwtToken);
            Map<String, Object> map = new HashMap<>();
            map.put("status", 200);
            map.put("token", token);
            map.put("name", sysUser.getName());
            return R.ok("login success", map);
        } catch (Exception ex) {
            ex.printStackTrace();
            return R.error("login error");
        }
    }

    @GetMapping("/logout")
    @ApiOperation("用户退出")
    public R userLogout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        Map<String, Object> map = new HashMap<>();
        map.put("status", 200);
        return R.ok("logout success", map);
    }



    @PostMapping("checkPass")
    public R checkPass(String pass) {
        SysUser user = ShiroUtils.getUser();
        Boolean isPass = user.getPassword().equals(
                new Sha256Hash(pass, user.getSalt(), 3).toString());
        return R.ok(isPass);
    }

    @PostMapping("/updatePass")
    public R updateUserInfo(String pass){
        SysUser user = ShiroUtils.getUser();
        user.setSalt(getSalt());
        user.setPassword(new Sha256Hash(pass, user.getSalt(), 3).toString());
        user.setUpdateTime(new Date());
        sysUserMapper.updateById(user);
        return R.ok();
    }

    /**
     * 随机生成密码盐
     *
     * @return
     */
    private String getSalt() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
