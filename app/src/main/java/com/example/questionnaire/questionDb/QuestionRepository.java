package com.example.questionnaire.questionDb;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.questionnaire.model.Models.QuestionClass;

import java.util.ArrayList;
import java.util.List;


public class QuestionRepository {
    private final QuestionDao questionDao;
    private LiveData<List<QuestionClass>> allQuestions;

    public QuestionRepository(Application application) {
        QuestionDB database = QuestionDB.getInstance(application);
        questionDao = database.questionDao();
        new Handler(Looper.myLooper()).post(() -> allQuestions = questionDao.getAllQuestions());
    }

    //Abstraction layer for encapsulation

    //Create
    private void insertQuestionsRepo(QuestionClass question) {
        new Handler(Looper.myLooper()).post(() -> {
            questionDao.insert(question);
            System.out.println(question.getQuestionId() + " inserted");
        });
    }

    public void insert(QuestionClass question) {
        insertQuestionsRepo(question);
    }

    //Read
    public LiveData<List<QuestionClass>> getAllQuestionListLiveData() {
        return allQuestions;
    }

    public LiveData<QuestionClass> getObservableQuestion(String QuestionId) {
        return getSpecificQuestionFromIdRepo(QuestionId);
    }

    public LiveData<List<QuestionClass>> getAllQuestionsTypeLiveData(int type) {
        return questionDao.getAllTypeQuestionsLiveData(type);
    }

    public List<QuestionClass> getAllQuestionList () {
        return getAllQuestionListRepo();
    }

    public List<QuestionClass> getAllQuestionListType (String type) {
        return getTypeQuestionsFromRepo(type);
    }

    public QuestionClass getSpecificQuestionWithId (String QuestionId) {
        return getSpecificQuestionWithIdRepo(QuestionId);
    }

    private QuestionClass getSpecificQuestionWithIdRepo(String questionId){
        final QuestionClass[] Questions = {new QuestionClass("")};
        new Handler(Looper.myLooper()).post(() -> Questions[0] = questionDao.getSpecificQuestion(questionId));
        return Questions[0];
    }

    private MutableLiveData<QuestionClass> getSpecificQuestionFromIdRepo (String questionId) {
        MutableLiveData<QuestionClass> questionClassMutableLiveData = new MutableLiveData<>();
        new Handler(Looper.getMainLooper()).post(() -> questionClassMutableLiveData.setValue(questionDao.getSpecificQuestion(questionId)));
        return questionClassMutableLiveData;
    }

    private List<QuestionClass> getTypeQuestionsFromRepo(String type) {
        ArrayList<QuestionClass> questionList = new ArrayList<>();
        new Handler(Looper.getMainLooper()).post(() -> questionList.addAll(questionDao.getAllTypeQuestionList(type)));
        return questionList;
    }

    private List<QuestionClass> getAllQuestionListRepo () {
        ArrayList<QuestionClass> QuestionList = new ArrayList<>();
        new Handler(Looper.getMainLooper()).post(() -> QuestionList.addAll(questionDao.getAllQuestionList()));
        return QuestionList;
    }


    //Update
    private void updateQuestionsRepo(QuestionClass question) {
        new Handler(Looper.myLooper()).post(() -> {
            questionDao.update(question);
            System.out.println(question.getQuestionId() + " updated");
        });
    }

    public void update(QuestionClass question) {
        updateQuestionsRepo(question);
    }


    //Delete
    public void delete(QuestionClass question) {
        deleteQuestionsRepo(question);
    }

    public void deleteAllQuestions() {
        deleteAllTheQuestionsRepo();
    }

    public void deleteSpecificQuestionWithId (String questionId) {
        deleteSpecificQuestionWithIdRepo(questionId);
    }

    private void deleteSpecificQuestionWithIdRepo (String questionId) {
        questionDao.deleteSpecificQuestions(questionId);
        System.out.println(questionId + " deleted");
    }

    private void deleteQuestionsRepo(QuestionClass question) {
        new Handler(Looper.myLooper()).post(() -> {
            questionDao.delete(question);
            System.out.println(question.getQuestionId() + "deleted");
        });
    }

    private void deleteAllTheQuestionsRepo() {
        new Handler(Looper.myLooper()).post(() -> {
            questionDao.deleteAllQuestions();
            System.out.println("All Questions deleted");
        });
    }
}
