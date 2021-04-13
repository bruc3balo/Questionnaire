package com.example.questionnaire.questionDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.questionnaire.model.Models.QuestionClass;

import java.util.List;

import static com.example.questionnaire.model.Models.QuestionClass.CREATED_AT;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_ID;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_TYPE;


@Dao
public interface QuestionDao {

    String DELETE_QUESTIONS = "DELETE FROM " + QUESTION_DB;
    String GET_ALL_QUESTIONS= "SELECT * FROM " + QUESTION_DB + " ORDER BY " + CREATED_AT + " ASC";
    String GET_SPECIFIC_QUESTION = "SELECT * FROM " + QUESTION_DB +" WHERE "+ QUESTION_ID + " LIKE " + ":questionId "+ " ORDER BY " + CREATED_AT + " ASC";
    String GET_TYPE_QUESTIONS = "SELECT * FROM " + QUESTION_DB +" WHERE "+ QUESTION_TYPE + " LIKE " + ":questionType "+ " ORDER BY " + CREATED_AT + " ASC";
    String DELETE_SPECIFIC_QUESTION = "DELETE FROM " + QUESTION_DB +" WHERE "+ QUESTION_ID + " LIKE " + ":questionId ";


    @Insert
    void insert(QuestionClass questionClass);

    @Update
    void update(QuestionClass questionClass);

    @Delete
    void delete(QuestionClass questionClass);

    @Query(DELETE_QUESTIONS)
    void deleteAllQuestions();

    @Query(DELETE_SPECIFIC_QUESTION)
    void deleteSpecificQuestions(String questionId);

    @Query(GET_ALL_QUESTIONS)
    LiveData<List<QuestionClass>> getAllQuestions();

    @Query(GET_TYPE_QUESTIONS)
    LiveData<List<QuestionClass>> getAllTypeQuestionsLiveData(int questionType);

    @Query(GET_ALL_QUESTIONS)
    List<QuestionClass> getAllQuestionList();

    @Query(GET_TYPE_QUESTIONS)
    List<QuestionClass> getAllTypeQuestionList(String questionType);

    @Query(GET_SPECIFIC_QUESTION)
    QuestionClass getSpecificQuestion(String questionId);

}
