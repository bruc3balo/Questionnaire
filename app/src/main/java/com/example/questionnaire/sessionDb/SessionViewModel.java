package com.example.questionnaire.sessionDb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.questionnaire.model.Models;
import com.example.questionnaire.utils.MyLinkedMap;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.questionnaire.adapter.QuestionAdapter.checkIfNull;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_ANSWER_NO;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_ANSWER_YES;
import static com.example.questionnaire.model.Models.QuestionClass.CREATED_AT;
import static com.example.questionnaire.model.Models.QuestionClass.LAST_MODIFIED;
import static com.example.questionnaire.model.Models.QuestionClass.PRIMARY_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_ID;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_TYPE;
import static com.example.questionnaire.model.Models.QuestionClass.SECONDARY_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.WORK_FORCE_TYPE;
import static com.example.questionnaire.model.Models.QuestionSession.ANSWER_LIST_MAP;
import static com.example.questionnaire.model.Models.QuestionSession.LAST_MODIFIED_SESSION;
import static com.example.questionnaire.model.Models.QuestionSession.QUESTION_SESSION_DB;
import static com.example.questionnaire.model.Models.QuestionSession.SESSION_ID;
import static com.example.questionnaire.model.Models.QuestionSession.WORK_FORCE_AGENT_Age;
import static com.example.questionnaire.model.Models.QuestionSession.WORK_FORCE_AGENT_NAME;
import static com.example.questionnaire.model.Models.WORK_CUSTOMER;
import static com.example.questionnaire.model.Models.WORK_MAID;
import static com.example.questionnaire.model.Models.getMapFromString;

public class SessionViewModel extends ViewModel {

    private MutableLiveData<List<Models.QuestionSession>> getCustomerSessions() {
        MutableLiveData<List<Models.QuestionSession>> questionSessionsMutable = new MutableLiveData<>();
        List<Models.QuestionSession> sessionList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(QUESTION_SESSION_DB).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Sessions got");
                Models.QuestionSession session;
                for (DocumentSnapshot qs : Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getDocuments())) {

                    if (WORK_CUSTOMER.equals(qs.get(WORK_FORCE_TYPE))) {
                        session = new Models.QuestionSession(Objects.requireNonNull(qs.get(SESSION_ID)).toString());
                        session.setWorkForceAgentAge(Objects.requireNonNull(qs.get(WORK_FORCE_AGENT_Age)).toString());
                        session.setWorkForceAgentName(Objects.requireNonNull(qs.get(WORK_FORCE_AGENT_NAME)).toString());

                        session.setLastModified(Objects.requireNonNull(qs.get(LAST_MODIFIED_SESSION)).toString());
                        session.setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());
                        session.setAnswerListMap(Objects.requireNonNull(qs.get(ANSWER_LIST_MAP)).toString());

                        session.setWorkForceType(Objects.requireNonNull(qs.get(WORK_FORCE_TYPE)).toString());

                        sessionList.add(session);
                    }


                }

                questionSessionsMutable.setValue(sessionList);
            } else {
                System.out.println("Could not get sessions");
            }
        });
        return questionSessionsMutable;
    }

    private MutableLiveData<List<Models.QuestionSession>> getMaidSessions() {
        MutableLiveData<List<Models.QuestionSession>> questionSessionsMutable = new MutableLiveData<>();
        List<Models.QuestionSession> sessionList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(QUESTION_SESSION_DB).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Sessions got");
                Models.QuestionSession session;
                for (DocumentSnapshot qs : Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getDocuments())) {

                    if (WORK_MAID.equals(qs.get(WORK_FORCE_TYPE))) {
                        session = new Models.QuestionSession(Objects.requireNonNull(qs.get(SESSION_ID)).toString());
                        session.setWorkForceAgentAge(Objects.requireNonNull(qs.get(WORK_FORCE_AGENT_Age)).toString());
                        session.setWorkForceAgentName(Objects.requireNonNull(qs.get(WORK_FORCE_AGENT_NAME)).toString());

                        session.setLastModified(Objects.requireNonNull(qs.get(LAST_MODIFIED_SESSION)).toString());
                        session.setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());
                        session.setAnswerListMap(Objects.requireNonNull(qs.get(ANSWER_LIST_MAP)).toString());

                        session.setWorkForceType(Objects.requireNonNull(qs.get(WORK_FORCE_TYPE)).toString());

                        sessionList.add(session);
                    }


                }

                questionSessionsMutable.setValue(sessionList);
            } else {
                System.out.println("Could not get sessions");
            }
        });
        return questionSessionsMutable;
    }

    private MutableLiveData<List<Models.QuestionSession>> getAllSessions() {
        MutableLiveData<List<Models.QuestionSession>> questionSessionsMutable = new MutableLiveData<>();
        List<Models.QuestionSession> sessionList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(QUESTION_SESSION_DB).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Sessions got");
                Models.QuestionSession session;
                for (DocumentSnapshot qs : Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getDocuments())) {

                    session = new Models.QuestionSession(Objects.requireNonNull(qs.get(SESSION_ID)).toString());
                    session.setWorkForceAgentAge(Objects.requireNonNull(qs.get(WORK_FORCE_AGENT_Age)).toString());
                    session.setWorkForceAgentName(Objects.requireNonNull(qs.get(WORK_FORCE_AGENT_NAME)).toString());

                    session.setLastModified(Objects.requireNonNull(qs.get(LAST_MODIFIED_SESSION)).toString());
                    session.setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());
                    session.setAnswerListMap(Objects.requireNonNull(qs.get(ANSWER_LIST_MAP)).toString());

                    session.setWorkForceType(Objects.requireNonNull(qs.get(WORK_FORCE_TYPE)).toString());

                    sessionList.add(session);
                }

                questionSessionsMutable.setValue(sessionList);
            } else {
                System.out.println("Could not get sessions");
            }
        });
        return questionSessionsMutable;
    }

    private MutableLiveData<Models.QuestionSession> getASessions(String sessionId) {
        MutableLiveData<Models.QuestionSession> questionSessionsMutable = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(QUESTION_SESSION_DB).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Sessions got");
                Models.QuestionSession session;
                for (DocumentSnapshot qs : Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getDocuments())) {

                    if (sessionId.equals(Objects.requireNonNull(qs.get(SESSION_ID)).toString())) {
                        session = new Models.QuestionSession(Objects.requireNonNull(qs.get(SESSION_ID)).toString());
                        session.setWorkForceAgentAge(Objects.requireNonNull(qs.get(WORK_FORCE_AGENT_Age)).toString());
                        session.setWorkForceAgentName(Objects.requireNonNull(qs.get(WORK_FORCE_AGENT_NAME)).toString());

                        session.setLastModified(Objects.requireNonNull(qs.get(LAST_MODIFIED)).toString());
                        session.setCreatedAt(Objects.requireNonNull(qs.get(CREATED_AT)).toString());
                        session.setAnswerListMap(Objects.requireNonNull(qs.get(ANSWER_LIST_MAP)).toString());

                        session.setWorkForceType(Objects.requireNonNull(qs.get(WORK_FORCE_TYPE)).toString());

                        questionSessionsMutable.setValue(session);
                    }
                }
            } else {
                System.out.println("Could not get sessions");
            }
        });
        return questionSessionsMutable;
    }


    public LiveData<List<Models.QuestionSession>> getAllSessionsLiveData() {
        return getAllSessions();
    }

    public LiveData<Models.QuestionSession> getASessionLiveData(String sessionId) {
        return getASessions(sessionId);
    }

    public LiveData<List<Models.QuestionSession>> getCustomerSessionLiveData () {
        return getCustomerSessions();
    }


    public LiveData<List<Models.QuestionSession>> getMaidSessionLiveData () {
        return getMaidSessions();
    }


}
