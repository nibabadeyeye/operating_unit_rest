package com.gpdi.operatingunit.config;

import com.gpdi.operatingunit.controller.system.support.SysConstant;
import com.gpdi.operatingunit.entity.system.SysPermission;
import com.gpdi.operatingunit.entity.system.SysUser;
import com.gpdi.operatingunit.service.system.SysPermissionService;
import com.gpdi.operatingunit.service.system.SysUserService;
import com.gpdi.operatingunit.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Lxq
 * @Date: 2019/9/6 12:40
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("————权限认证————");
        SysUser sysUser = (SysUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 从接口中获取用户的权限数据
        List<SysPermission> userPermission = sysPermissionService.queryPermissionByUserIdAndLevel(
                sysUser.getId(), SysConstant.PERMS_LEVEL_BUTTON);
        Set<String> permission = userPermission.stream().map(SysPermission::getPerms).collect(Collectors.toSet());
        authorizationInfo.addStringPermissions(permission);
        return authorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
       // System.out.println("————身份认证方法————");
        String token = (String) auth.getCredentials();
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token 无效");
        }

        SysUser sysUser = sysUserService.queryUserByUserName(username);
        if (sysUser == null) {
            throw new AuthenticationException("用户不存在!");
        }

        if (!JwtUtil.verify(token, username, sysUser.getPassword())) {
            throw new AuthenticationException("token 失效");
        }

        return new SimpleAuthenticationInfo(sysUser, token, getName());

    }
}
