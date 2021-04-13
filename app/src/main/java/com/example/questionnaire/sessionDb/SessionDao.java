package com.example.questionnaire.sessionDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.questionnaire.model.Models.QuestionSession;

import java.util.List;

import static com.example.questionnaire.model.Models.QuestionClass.CREATED_AT;
import static com.example.questionnaire.model.Models.QuestionClass.WORK_FORCE_TYPE;
import static com.example.questionnaire.model.Models.QuestionSession.QUESTION_SESSION_DB;
import static com.example.questionnaire.model.Models.QuestionSession.SESSION_ID;


@Dao
public interface SessionDao {

    String DELETE_QUESTIONS = "DELETE FROM " + QUESTION_SESSION_DB;
    String GET_ALL_QUESTIONS= "SELECT * FROM " + QUESTION_SESSION_DB + " ORDER BY " + CREATED_AT + " ASC";
    String GET_SPECIFIC_QUESTION = "SELECT * FROM " + QUESTION_SESSION_DB +" WHERE "+ SESSION_ID + " LIKE " + ":sessionId "+ " ORDER BY " + CREATED_AT + " ASC";
    String GET_TYPE_QUESTIONS = "SELECT * FROM " + QUESTION_SESSION_DB +" WHERE "+ WORK_FORCE_TYPE + " LIKE " + ":workForceType "+ " ORDER BY " + CREATED_AT + " ASC";
    String DELETE_SPECIFIC_QUESTION = "DELETE FROM " + QUESTION_SESSION_DB +" WHERE "+ SESSION_ID + " LIKE " + ":sessionId ";


    @Insert
    void insert(QuestionSession questionSession);

    @Update
    void update(QuestionSession questionSession);

    @Delete
    void delete(QuestionSession questionSession);

    @Query(DELETE_QUESTIONS)
    void deleteAllQuestions();

    @Query(DELETE_SPECIFIC_QUESTION)
    void deleteSpecificQuestions(String sessionId);

    @Query(GET_ALL_QUESTIONS)
    LiveData<List<QuestionSession>> getAllQuestions();

    @Query(GET_TYPE_QUESTIONS)
    LiveData<List<QuestionSession>> getAllTypeQuestionsLiveData(int type);

    @Query(GET_ALL_QUESTIONS)
    List<QuestionSession> getAllQuestionList();

    @Query(GET_TYPE_QUESTIONS)
    List<QuestionSession> getAllTypeQuestionList(int type);

    @Query(GET_SPECIFIC_QUESTION)
    QuestionSession getSpecificQuestion(String sessionId);

}
