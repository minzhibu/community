package com.sjm5z.community.server;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * 授权登录
 */
public interface AuthorizationLoginServer {
    /**
     * 授权登录
     * @return
     */
    Cookie authorizationLogin(String code, String state, HttpSession session);

    /**
     * 判断用户是否已经登录过
     * @param cookies
     */
    Cookie getTokenToUser(Cookie[] cookies,HttpSession session);
}
