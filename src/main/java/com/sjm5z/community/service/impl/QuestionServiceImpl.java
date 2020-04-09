package com.sjm5z.community.service.impl;

import com.sjm5z.community.dto.PaginationDTO;
import com.sjm5z.community.dto.QuestionDTO;
import com.sjm5z.community.dto.QuestionQueryDTO;
import com.sjm5z.community.exception.CustomizeErrorCode;
import com.sjm5z.community.exception.CustomizeException;
import com.sjm5z.community.mapper.QuestionExtMapper;
import com.sjm5z.community.mapper.QuestionMapper;
import com.sjm5z.community.mapper.UserMapper;
import com.sjm5z.community.model.Question;
import com.sjm5z.community.model.QuestionExample;
import com.sjm5z.community.model.User;
import com.sjm5z.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insert(Question question, HttpSession session) {
        User user = (User) session.getAttribute("user");
        question.setUserId(Long.valueOf(user.getId()));
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setViewCount(0);
        question.setLikeCount(0);
        question.setCommentCount(0);
        questionMapper.insert(question);
    }

    @Override
    public PaginationDTO findAll(String search,Integer page, Integer size) {
        return list(search,null, page, size);
    }

    @Override
    public PaginationDTO list(String search,Long id, Integer page, Integer size) {
        if(size < 2 || size > 20){
            size = 7;
        }
        if(StringUtils.isNotBlank(search)){
            String[] searchs = StringUtils.split(search ," ");
            search = String.join("|", searchs);
        }
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setUserId(id);
        questionQueryDTO.setSize(size);
        int totalCount =  questionExtMapper.countBySearch(questionQueryDTO);
        if(totalCount != 0){
            //初始分页对象的属性
            paginationDTO.setPagination(totalCount, page, size);
            //获取总记录数
            questionQueryDTO.setPage(size * (paginationDTO.getPage() - 1));
            List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
            List<QuestionDTO> questionList = new ArrayList<>();
            for(Question question: questions){
                User user = userMapper.selectByPrimaryKey(question.getUserId());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser(user);
                questionList.add(questionDTO);
            }
            paginationDTO.setQuestions(questionList);
            return paginationDTO;
        }
        return null;
    }

    @Override
    public QuestionDTO getById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getUserId());
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    @Override
    public void insertOrUpdate(Question question, HttpSession session) {
        System.out.println(question);
        if(question.getId() == null) {
            insert(question, session);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question, example);
        }
    }

    @Override
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    @Override
    public List<QuestionDTO> selectRelate(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String tags = StringUtils.replace(questionDTO.getTag(), ",", "|");
        tags = StringUtils.replace(tags,"+","\\\\+");
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(tags);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return questionDTOS;
    }

}
