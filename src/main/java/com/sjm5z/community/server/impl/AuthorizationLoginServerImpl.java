package com.sjm5z.community.server.impl;

import com.sjm5z.community.dto.AccessTokenDto;
import com.sjm5z.community.dto.GitHubUser;
import com.sjm5z.community.mapper.UserMapper;
import com.sjm5z.community.model.User;
import com.sjm5z.community.provider.GitHubProvider;
import com.sjm5z.community.server.AuthorizationLoginServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Component
public class AuthorizationLoginServerImpl implements AuthorizationLoginServer {
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
        AccessTokenDto accessTokenDTO = new AccessTokenDto();
        accessTokenDTO.setRedirect_uri(clientURI);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
        Cookie cookie = null;
        if (gitHubUser != null) {
            Long accountIDExist = userMapper.selectAccountIDExist(gitHubUser.getId());
            //如果当前该用户是第一授权则在数据库创建他的信息，如果不是则跳过这步
            if(accountIDExist == null) {
                User user = new User();
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setAccountId(String.valueOf(gitHubUser.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                user.setName(gitHubUser.getLogin());
                user.setAvatarUrl(gitHubUser.getAvatar_url());
                System.out.println(user);
                userMapper.insert(user);
                cookie = new Cookie("token",token);
            }else{
                String token = userMapper.selectToken(accountIDExist);
                cookie = new Cookie("token",token);
            }
            session.setAttribute("user", gitHubUser);
        }
        return cookie;
    }
    @Override
    public Cookie getTokenToUser(Cookie[] cookies,HttpSession session) {
        if(cookies == null) {
            return null;
        }
        for(Cookie cookie: cookies){
            if("token".equals(cookie.getName())){
                User user = userMapper.selectUserOfToken(cookie.getValue());
                if(user != null) {
                    Cookie cookie1 = new Cookie("token",cookie.getValue());
                    //3天不登录cookie到期
                    cookie1.setMaxAge(60*60*60*24*3);
                    GitHubUser gitHubUser = new GitHubUser();
                    gitHubUser.setLogin(user.getName());
                    gitHubUser.setId(Long.valueOf(user.getAccountId()));
                    session.setAttribute("user",gitHubUser);
                    return cookie1;
                }
            }
        }
        return null;
    }



}
