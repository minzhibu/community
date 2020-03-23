package com.sjm5z.community.mapper;

import com.sjm5z.community.dto.QuestionDTO;
import com.sjm5z.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,user_id,tag)" +
            " values(#{title},#{description},#{gmtCreate},#{gmtModified},#{userID},#{tag})")
    void insert(Question question);

    //查询全部
    @Select("select * from question")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "title", property = "title"),
            @Result(column = "description", property = "description"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified"),
            @Result(column = "user_id", property = "userID"),
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
    @Results(id = "questionDto",value ={
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "title", property = "title"),
            @Result(column = "description", property = "description"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified"),
            @Result(column = "user_id", property = "userID"),
            @Result(column = "comment_count", property = "commentCount"),
            @Result(column = "view_count", property = "viewCount"),
            @Result(column = "like_count", property = "likeCount"),
            @Result(column = "tag", property = "tag"),
            @Result(property = "user", column = "user_id", one = @One(select = "com.sjm5z.community.mapper.UserMapper.selectUserByID", fetchType = FetchType.LAZY))
    })
    List<QuestionDTO> findAllAndUser(Integer offset, Integer size);


    //查找一共有多少条记录
    @Select("select count(*) from question")
    Integer count();

    /**
     * 更加UserAccountId来查询
     * @param userAccountId
     * @return
     */
    @Select("select * from question where creator = #{userAccountId}")
    List<Question> findByAccountId(Long userAccountId);

    //查询符合条件的一共有多少条记录
    @Select("select count(*) from question where user_id = #{ID}")
    Integer countByAccountId(Integer userID);

    //查询符合条件的一共有多少个问题
    @Select("select * from question where user_id = #{userID} limit #{offset},#{size}")
    @ResultMap(value = "questionDto")
    List<QuestionDTO> findUserToQuestion(Integer userID, Integer offset, Integer size);

    //更具ID查询提问
    @Select("select * from question where id = #{id}")
    @ResultMap(value = "questionDto")
    QuestionDTO findById(Integer id);
}
