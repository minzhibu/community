package com.sjm5z.community.mapper;

import com.sjm5z.community.dto.QuestionDTO;
import com.sjm5z.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag)" +
            " values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void insert(Question question);

    //查询全部
    @Select("select * from question")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "title", property = "title"),
            @Result(column = "description", property = "description"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified"),
            @Result(column = "creator", property = "creator"),
            @Result(column = "comment_count", property = "commentCount"),
            @Result(column = "view_count", property = "viewCount"),
            @Result(column = "like_count", property = "likeCount"),
            @Result(column = "tag", property = "tag"),
    })
    List<Question> findAll();

    /**
     * 找到所有发布的问题和发布问题的人
     *
     * @param offset
     * @param size
     * @return
     */
    @Select("select * from question limit #{offset},#{size}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "title", property = "title"),
            @Result(column = "description", property = "description"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified"),
            @Result(column = "creator", property = "creator"),
            @Result(column = "comment_count", property = "commentCount"),
            @Result(column = "view_count", property = "viewCount"),
            @Result(column = "like_count", property = "likeCount"),
            @Result(column = "tag", property = "tag"),
            @Result(property = "user", column = "creator", one = @One(select = "com.sjm5z.community.mapper.UserMapper.selectUserOfAccountID", fetchType = FetchType.LAZY))
    })
    List<QuestionDTO> findAllAndUser(Integer offset, Integer size);


    @Select("select count(*) from question")
    Integer count();
}
