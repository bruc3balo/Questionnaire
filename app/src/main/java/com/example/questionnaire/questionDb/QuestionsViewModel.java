package com.example.questionnaire.questionDb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.questionnaire.model.Models;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.questionnaire.QuestionsActivity.CUSTOMER;
import static com.example.questionnaire.QuestionsActivity.MAID;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_ANSWER_NO;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_ANSWER_YES;
import static com.example.questionnaire.model.Models.QuestionClass.CREATED_AT;
import static com.example.questionnaire.model.Models.QuestionClass.LAST_MODIFIED;
import static com.example.questionnaire.model.Models.QuestionClass.PRIMARY_ANSWER;
import static com.example.questionnaire.model.Models.QuestionClass.PRIMARY_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_ID;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_TYPE;
import static com.example.questionnaire.model.Models.QuestionClass.SECONDARY_ANSWER;
import static com.example.questionnaire.model.Models.QuestionClass.SECONDARY_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.WORK_FORCE_TYPE;

public class QuestionsViewModel extends ViewModel {

    public QuestionsViewModel() {

    }

    private MutableLiveData<Models.QuestionClass> getASpecificQuestions(String questionId) {
        MutableLiveData<Models.QuestionClass> questionMutable = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Questions Maid fetched");
                Models.QuestionClass question;
                for (DocumentSnapshot qs : Objects.requireNonNull(task.getResult()).getDocuments()) {

                    if (questionId.equals(Objects.requireNonNull(qs.get(QUESTION_ID)).toString())) {
                        question = new Models.QuestionClass(Objects.requireNonNull(qs.get(QUESTION_ID)).toString());
                        question.setSecondaryQuestion(Objects.requireNonNull(qs.get(SECONDARY_QUESTION)).toString());
                        question.setPrimaryQuestion(Objects.requireNonNull(qs.get(PRIMARY_QUESTION)).toString());

                        question.setClosedAnswerNo(Objects.requireNonNull(qs.get(CLOSED_ANSWER_NO)).toString());
                        question.setClosedAnswerYes(Objects.requireNonNull(qs.get(CLOSED_ANSWER_YES)).toString());
                        question.setQuestionType(Objects.requireNonNull(qs.get(QUESTION_TYPE)).toString());

                        question.setWorkForceType(Integer.parseInt(String.valueOf(qs.get(WORK_FORCE_TYPE))));
                        question.setLastModifiedAt(Objects.requireNonNull(qs.get(LAST_MODIFIED)).toString());
                        question.setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());



                        questionMutable.setValue(question);
                    }
                }

            } else {
                System.out.println("Failed to get questions");
            }
        });
        return questionMutable;
    }

    private Models.QuestionClass getASpecificQuestionModel (String questionId) {
        final Models.QuestionClass[] question = new Models.QuestionClass[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).document(questionId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Questions Maid fetched");
                DocumentSnapshot qs = task.getResult();

                if (questionId.equals(Objects.requireNonNull(qs.get(QUESTION_ID)).toString())) {
                    question[0] = new Models.QuestionClass(Objects.requireNonNull(qs.get(QUESTION_ID)).toString());
                    question[0].setSecondaryQuestion(Objects.requireNonNull(qs.get(SECONDARY_QUESTION)).toString());
                    question[0].setPrimaryQuestion(Objects.requireNonNull(qs.get(PRIMARY_QUESTION)).toString());

                    question[0].setClosedAnswerNo(Objects.requireNonNull(qs.get(CLOSED_ANSWER_NO)).toString());
                    question[0].setClosedAnswerYes(Objects.requireNonNull(qs.get(CLOSED_ANSWER_YES)).toString());
                    question[0].setQuestionType(Objects.requireNonNull(qs.get(QUESTION_TYPE)).toString());

                    question[0].setWorkForceType(Integer.parseInt(String.valueOf(qs.get(WORK_FORCE_TYPE))));
                    question[0].setLastModifiedAt(Objects.requireNonNull(qs.get(LAST_MODIFIED)).toString());
                    question[0].setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());
                    question[0].setPrimaryAnswer(qs.get(PRIMARY_ANSWER).toString());
                    question[0].setSecondaryAnswer(qs.get(SECONDARY_ANSWER).toString());

                }

            } else {
                System.out.println("Failed to get questions");
            }
        });
        return question[0];
    }

    private MutableLiveData<List<Models.QuestionClass>> getAllQuestions() {
        MutableLiveData<List<Models.QuestionClass>> questionMutableList = new MutableLiveData<>();
        List<Models.QuestionClass> questionList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Questions fetched");
                Models.QuestionClass question;
                for (DocumentSnapshot qs : Objects.requireNonNull(task.getResult()).getDocuments()) {

                    question = new Models.QuestionClass(Objects.requireNonNull(qs.get(QUESTION_ID)).toString());
                    question.setSecondaryQuestion(Objects.requireNonNull(qs.get(SECONDARY_QUESTION)).toString());
                    question.setPrimaryQuestion(Objects.requireNonNull(qs.get(PRIMARY_QUESTION)).toString());

                    question.setClosedAnswerNo(Objects.requireNonNull(qs.get(CLOSED_ANSWER_NO)).toString());
                    question.setClosedAnswerYes(Objects.requireNonNull(qs.get(CLOSED_ANSWER_YES)).toString());
                    question.setQuestionType(Objects.requireNonNull(qs.get(QUESTION_TYPE)).toString());

                    question.setWorkForceType(Integer.parseInt(String.valueOf(qs.get(WORK_FORCE_TYPE))));
                    question.setLastModifiedAt(Objects.requireNonNull(qs.get(LAST_MODIFIED)).toString());
                    question.setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());



                    questionList.add(question);
                }
                questionMutableList.setValue(questionList);
            } else {
                System.out.println("Failed to get questions");
            }
        });

        return questionMutableList;
    }

    private MutableLiveData<List<Models.QuestionClass>> getAllMaidQuestions() {
        MutableLiveData<List<Models.QuestionClass>> maidMutable = new MutableLiveData<>();
        List<Models.QuestionClass> questionList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Questions Maid fetched");
                Models.QuestionClass question;
                for (DocumentSnapshot qs : Objects.requireNonNull(task.getResult()).getDocuments()) {
                    question = new Models.QuestionClass(Objects.requireNonNull(qs.get(QUESTION_ID)).toString());
                    if (String.valueOf(MAID).equals(qs.get(WORK_FORCE_TYPE).toString())) {
                        question.setSecondaryQuestion(Objects.requireNonNull(qs.get(SECONDARY_QUESTION)).toString());
                        question.setPrimaryQuestion(Objects.requireNonNull(qs.get(PRIMARY_QUESTION)).toString());

                        question.setClosedAnswerNo(Objects.requireNonNull(qs.get(CLOSED_ANSWER_NO)).toString());
                        question.setClosedAnswerYes(Objects.requireNonNull(qs.get(CLOSED_ANSWER_YES)).toString());
                        question.setQuestionType(Objects.requireNonNull(qs.get(QUESTION_TYPE)).toString());

                        question.setWorkForceType(Integer.parseInt(String.valueOf(qs.get(WORK_FORCE_TYPE))));
                        question.setLastModifiedAt(Objects.requireNonNull(qs.get(LAST_MODIFIED)).toString());
                        question.setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());



                        questionList.add(question);
                    }
                }
                maidMutable.setValue(questionList);
            } else {
                System.out.println("Failed to get questions");
            }
        });
        return maidMutable;
    }

    private MutableLiveData<List<Models.QuestionClass>> getAllCustomerQuestions() {
        MutableLiveData<List<Models.QuestionClass>> customerMutable = new MutableLiveData<>();
        List<Models.QuestionClass> questionList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Questions Customer fetched");
                Models.QuestionClass question;
                for (DocumentSnapshot qs : Objects.requireNonNull(task.getResult()).getDocuments()) {
                    question = new Models.QuestionClass(Objects.requireNonNull(qs.get(QUESTION_ID)).toString());
                    if (String.valueOf(CUSTOMER).equals(qs.get(WORK_FORCE_TYPE).toString())) {
                        question.setSecondaryQuestion(Objects.requireNonNull(qs.get(SECONDARY_QUESTION)).toString());
                        question.setPrimaryQuestion(Objects.requireNonNull(qs.get(PRIMARY_QUESTION)).toString());

                        question.setClosedAnswerNo(Objects.requireNonNull(qs.get(CLOSED_ANSWER_NO)).toString());
                        question.setClosedAnswerYes(Objects.requireNonNull(qs.get(CLOSED_ANSWER_YES)).toString());
                        question.setQuestionType(Objects.requireNonNull(qs.get(QUESTION_TYPE)).toString());

                        question.setWorkForceType(Integer.parseInt(String.valueOf(qs.get(WORK_FORCE_TYPE))));
                        question.setLastModifiedAt(Objects.requireNonNull(qs.get(LAST_MODIFIED)).toString());
                        question.setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());



                        questionList.add(question);
                    }
                }
                customerMutable.setValue(questionList);
            } else {
                System.out.println("Failed to get questions");
            }
        });
        return customerMutable;
    }

    public LiveData<List<Models.QuestionClass>> getQuestionsLiveData() {
        return getAllQuestions();
    }

    public LiveData<List<Models.QuestionClass>> getMaidQuestionsLiveData() {
        return getAllMaidQuestions();
    }

    public LiveData<List<Models.QuestionClass>> getCustomerQuestionsLiveData() {
        return getAllCustomerQuestions();
    }

    public LiveData<Models.QuestionClass> getSpecificQuestion(String questionId) {
        return getASpecificQuestions(questionId);
    }

    public Models.QuestionClass getQuestion(String questionId) {
        return getASpecificQuestionModel(questionId);
    }

}
