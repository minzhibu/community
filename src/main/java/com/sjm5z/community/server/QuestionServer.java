package com.sjm5z.community.server;

import com.sjm5z.community.model.Question;

import javax.servlet.http.HttpSession;

public interface QuestionServer {

    void insert(Question question, HttpSession session);
}
