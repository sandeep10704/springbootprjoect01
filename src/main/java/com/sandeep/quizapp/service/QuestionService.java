package com.sandeep.quizapp.service;

import com.sandeep.quizapp.model.Question;
import com.sandeep.quizapp.dao.QuestionDao;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    private static final String OPENAI_API_KEY = "api key"; 
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("NOT SUCCESS", HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<String> deleteQuestionById(Integer id) {
        try {
            if (questionDao.existsById(id)) {
                questionDao.deleteById(id);
                return new ResponseEntity<>("SUCCESS", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("NO CONTENT", HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("NOT MODIFIED", HttpStatus.NOT_MODIFIED);
    }
}




