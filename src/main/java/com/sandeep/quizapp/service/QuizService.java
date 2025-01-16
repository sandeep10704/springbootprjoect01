package com.sandeep.quizapp.service;

import com.sandeep.quizapp.dao.QuestionDao;
import com.sandeep.quizapp.dao.QuizDao;
import com.sandeep.quizapp.model.Question;
import com.sandeep.quizapp.model.QuestionWrapper;
import com.sandeep.quizapp.model.Quiz;
import com.sandeep.quizapp.model.response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizDao quizDao;

    @Autowired
    private QuestionDao questionDao;



    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);

        if (quiz.isPresent()) {
            List<Question> questionsFromDB = quiz.get().getQuestions();
            List<QuestionWrapper> questionForUser = new ArrayList<>();

            for (Question q : questionsFromDB) {
                QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
                questionForUser.add(qw);
            }

            return new ResponseEntity<>(questionForUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        try {
            // Validate numQ
            if (numQ < 1) {
                return new ResponseEntity<>("Number of questions must be at least 1", HttpStatus.BAD_REQUEST);
            }

            // Fetch random questions by category
            List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);

            if (questions == null || questions.isEmpty()) {
                return new ResponseEntity<>("No questions found for the given category", HttpStatus.NOT_FOUND);
            }

            // Create and save the quiz
            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);
            quizDao.save(quiz);

            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception and return an error response
            e.printStackTrace();
            return new ResponseEntity<>("NOT SUCCESS: " + e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public ResponseEntity<Integer> calulateResult(Integer id, List<response> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();
        int right = 0;
        int i =0;

        for(response r:responses){
            if(r.getResponse().equals(questions.get(i).getRightAnswer())) {
                right++;
            }
            i++;


        }

      return new ResponseEntity<>(i,HttpStatus.OK);
    }
}
