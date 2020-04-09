package com.sjm5z.community.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * 授权登录
 */
public interface AuthorizationLoginService {
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

    //退出登录 ,并且跟新token
    Cookie logout(HttpSession session);
}
