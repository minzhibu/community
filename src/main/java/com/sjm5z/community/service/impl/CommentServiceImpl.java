package com.sjm5z.community.service.impl;

import com.sjm5z.community.dto.*;
import com.sjm5z.community.enums.CommentTypeEnum;
import com.sjm5z.community.enums.NotificationStatusEnum;
import com.sjm5z.community.enums.NotificationTypeEnum;
import com.sjm5z.community.exception.CustomizeErrorCode;
import com.sjm5z.community.exception.CustomizeException;
import com.sjm5z.community.mapper.*;
import com.sjm5z.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public Object insert(CommentCreateDTO commentCreateDTO, User user) {
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //添加评论数
            Comment comments = commentMapper.selectByPrimaryKey(comment.getParentId());
            Long parentId = comments.getParentId();
            Question question = new Question();
            question.setCommentCount(1);
            question.setId(parentId);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotify(comment, dbComment.getCommentator(), user.getName(), comments.getContent(), NotificationTypeEnum.REPLY_COMMENT, parentId);
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotify(comment, question.getUserId(),user.getName(),question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
        return ResultDTO.okOf();
    }
    //创建通知
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if(receiver.equals(comment.getCommentator())){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNRAED.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public PaginationDTO<CommentDTO> listByQuestionId(Long id,Integer type, Integer size,Integer page) {
        if(size < 2 || size > 20){
            size = 7;
        }
        PaginationDTO<CommentDTO> paginationDTO = new PaginationDTO<>();
        int totalCount;
        CommentExample commentExample = new CommentExample();
        if (id != null) {
            commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type);
            commentExample.setOrderByClause("gmt_create desc");
            totalCount = Math.toIntExact(commentMapper.countByExample(commentExample));
        } else {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(totalCount != 0) {
            //初始分页对象的属性
            paginationDTO.setPagination(totalCount, page, size);
            //获取总记录数
            Integer page1 = paginationDTO.getPage();
            int offset = size * (page1 - 1);
            List<Comment> comments;
            comments = commentMapper.selectByExampleWithRowbounds(commentExample, new RowBounds(offset, size));
            if (comments.size() == 0) {
                return null;
            }
            //获取去重的评论人
            Set<Long> commentators = comments.stream().map(Comment::getCommentator).collect(Collectors.toSet());
            //获取评论人并转换为Map
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdIn(new ArrayList<>(commentators));
            List<User> users = userMapper.selectByExample(userExample);
            Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

            //转换Comment为
            List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                if(type.equals(CommentTypeEnum.QUESTION.getType())){
                    CommentExample commentExample1 = new CommentExample();
                    commentExample1.createCriteria().andParentIdEqualTo(comment.getId()).andTypeEqualTo(CommentTypeEnum.COMMENT.getType());
                    commentDTO.setCollapseCommentCount(commentMapper.selectByExample(commentExample1).size());
                }
                BeanUtils.copyProperties(comment, commentDTO);
                commentDTO.setUser(userMap.get(comment.getCommentator()));
                return commentDTO;
            }).collect(Collectors.toList());
            paginationDTO.setQuestions(commentDTOS);
        }
        return paginationDTO;
    }
}
