package com.sjm5z.community.model;

import lombok.Data;

@Data
public class Question {;
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer userID;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
}
