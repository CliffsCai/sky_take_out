package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {


    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openid);

    void insertUser(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    @Select("SELECT COUNT(*) from user where create_time <= #{localDateTimeEnd}")
    Integer getUserNumber(LocalDateTime localDateTimeEnd);

}
