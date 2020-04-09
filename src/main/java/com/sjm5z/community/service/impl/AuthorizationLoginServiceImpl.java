package com.sjm5z.community.service.impl;

import com.sjm5z.community.dto.AccessTokenDTO;
import com.sjm5z.community.dto.GitHubUser;
import com.sjm5z.community.mapper.UserMapper;
import com.sjm5z.community.model.User;
import com.sjm5z.community.model.UserExample;
import com.sjm5z.community.provider.GitHubProvider;
import com.sjm5z.community.service.AuthorizationLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

/**
 * 登录授权的类
 */
@Service
public class AuthorizationLoginServiceImpl implements AuthorizationLoginService {
    @Autowired
    private GitHubProvider gitHubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.uri}")
    private String clientURI;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Cookie authorizationLogin(String code, String state, HttpSession session) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setRedirect_uri(clientURI);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
        Cookie cookie = null;
        User user;
        if (gitHubUser != null) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(String.valueOf(gitHubUser.getId()));
            List<User> users = userMapper.selectByExample(userExample);
            //如果当前该用户是第一授权则在数据库创建他的信息，如果不是则跳过这步
            if (users.size() == 0) {
                user = new User();
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setAccountId(String.valueOf(gitHubUser.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                user.setName(gitHubUser.getLogin());
                user.setAvatarUrl(gitHubUser.getAvatarUrl());
                userMapper.insert(user);
                cookie = new Cookie("token", token);
                session.setAttribute("user", user);
            } else {
                String token = users.get(0).getToken();
                cookie = new Cookie("token", token);
                //3天
                cookie.setMaxAge(60 * 60 * 60 * 24 * 3);
                session.setAttribute("user",users.get(0));
            }
        }
        return cookie;
    }

    //利用token来判定浏览器是否已经登录过
    @Override
    public Cookie getTokenToUser(Cookie[] cookies, HttpSession session) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                UserExample example = new UserExample();
                example.createCriteria().
                        andTokenEqualTo(cookie.getValue());
                List<User> users = userMapper.selectByExample(example);
                if (users.size() != 0) {
                    Cookie cookie1 = new Cookie("token", cookie.getValue());
                    //3天不登录cookie到期
                    cookie1.setMaxAge(60 * 60 * 60 * 24 * 3);
                    session.setAttribute("user", users.get(0));
                    return cookie1;
                }
            }
        }
        return null;
    }

    @Override
    public Cookie logout(HttpSession session) {
        User userS = (User) session.getAttribute("user");
        session.removeAttribute("user");
        User user = new User();
        user.setToken(UUID.randomUUID().toString());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(userS.getId());
        userMapper.updateByExampleSelective(user, userExample);
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        return cookie;
    }
}
