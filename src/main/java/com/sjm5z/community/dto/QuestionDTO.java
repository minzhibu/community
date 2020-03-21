package com.sjm5z.community.dto;

import com.sjm5z.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO{
   private Integer id;
   private String title;
   private String description;
   private Long gmtCreate;
   private Long gmtModified;
   private Long creator;
   private Integer commentCount;
   private Integer viewCount;
   private Integer likeCount;
   private String tag;
   private User user;
}
