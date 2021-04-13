package com.example.questionnaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Toast;

import com.example.questionnaire.model.Models;
import com.example.questionnaire.questionDb.QuestionRepository;

import java.util.List;

public class EditQuestion extends AppCompatActivity {

    private Models.QuestionClass thisQuestion = new Models.QuestionClass("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        Toolbar editQtb = findViewById(R.id.editQtb);
        setSupportActionBar(editQtb);
        editQtb.setNavigationOnClickListener(v -> finish());
    }

    private void getQuestionDetailsOfflineWithId (String questionId) {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        thisQuestion = questionRepository.getSpecificQuestionWithId(questionId);
    }

    private void deleteQuestion (String questionId) {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        if (checkIfAvailable(thisQuestion)) {
            questionRepository.deleteSpecificQuestionWithId(questionId);
        } else {
            try {
                questionRepository.delete(thisQuestion);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No question found", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updateQuestion (Models.QuestionClass question) {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        if (checkIfAvailable(question)) {
            questionRepository.update(question);
        } else {
            questionRepository.insert(question);
        }
    }

    private boolean checkIfAvailable(Models.QuestionClass question) {
        boolean available = false;
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        List<Models.QuestionClass> questionList = questionRepository.getAllQuestionList();

        if (!questionList.isEmpty()) {
            for (int i = 0; i <= questionList.size() - 1; i++) {
                if (questionList.get(i).getQuestionId().equals(question.getQuestionId())) {
                    available = true;
                    return true;
                }
            }
        } else {
            available = false;
            return false;
        }

        return available;
    }
}