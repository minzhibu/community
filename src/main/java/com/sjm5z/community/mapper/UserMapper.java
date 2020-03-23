package com.sjm5z.community.mapper;

import com.sjm5z.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {



    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) " +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);


    @Select("select * from user where account_id = #{accountId}")
    User selectUserExist(long accountId);


    @Select("select token from user where account_id = #{accountId}")
    String selectToken(long accountId);


    @Select("select * from user where token = #{token}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "account_id",property = "accountId"),
            @Result(column = "name",property = "name"),
            @Result(column = "gmt_create",property = "gmtCreate"),
            @Result(column = "gmt_modified",property = "gmtModified"),
            @Result(column = "avatar_url",property = "avatarUrl"),
    })
    User selectUserOfToken(String token);

    @Select("select * from user where id = #{id}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "account_id",property = "accountId"),
            @Result(column = "name",property = "name"),
            @Result(column = "gmt_create",property = "gmtCreate"),
            @Result(column = "gmt_modified",property = "gmtModified"),
            @Result(column = "avatar_url",property = "avatarUrl"),
    })
    User selectUserByID(String id);

//    @Update("update user set token = #{token} where id = #{userID}")
//    void updateToken(Long userID,String token);
}
