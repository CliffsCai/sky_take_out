package com.sky.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    private final WeChatProperties weChatProperties;

    public UserServiceImpl(WeChatProperties weChatProperties) {
        this.weChatProperties = weChatProperties;
    }

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        String openid = getOpenId(userLoginDTO.getCode());

        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断openid是否为新，若为是自动注册，保存到sql的user表

        User user = userMapper.getByOpenId(openid);
        if(user == null){
            user = User.builder().
                    openid(openid).
                    createTime(LocalDateTime.now()).build();
            userMapper.insertUser(user);
        }


        return user;
    }

    private String getOpenId(String code){
        //调用微信接口，且判断openid是否为空
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String s = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(s);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
