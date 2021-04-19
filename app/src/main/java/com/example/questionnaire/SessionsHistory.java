package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questionnaire.adapter.SessionListRv;
import com.example.questionnaire.model.Models;
import com.example.questionnaire.sessionDb.SessionViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator2;

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
import static com.example.questionnaire.SessionAnswers.SESSION;

public class SessionsHistory extends AppCompatActivity {

    private SessionListRv sessionListRv;
    private final ArrayList<Models.QuestionSession> questionList = new ArrayList<>();
    private static ProgressBar sessionPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_history);

        Toolbar viewQtb = findViewById(R.id.sessionQtb);
        setSupportActionBar(viewQtb);
        viewQtb.setNavigationOnClickListener(v -> finish());

        sessionPb = findViewById(R.id.sessionPb);
        sessionPb.setVisibility(View.GONE);

        RadioGroup typeGroup = findViewById(R.id.typeGroup);
        typeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.maidRadio) {
                setMaidMode(viewQtb);
            } else if (checkedId == R.id.customerRadio) {
                setCustomerMode(viewQtb);
            }
        });

        RecyclerView viewQRv = findViewById(R.id.sessionRv);
        viewQRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        sessionListRv = new SessionListRv(this, questionList);
        viewQRv.setAdapter(sessionListRv);
        sessionListRv.setClickListener((view, position) -> goToSessionAnswers(questionList.get(position)));

        setCircleIndicator(viewQRv, sessionListRv);
       // getAllSessionsList();
        //testResolver("Fri Apr 16 00:34-MD");
    }

    private void goToSessionAnswers(Models.QuestionSession session) {
        startActivity(new Intent(SessionsHistory.this, SessionAnswers.class).putExtra(SESSION,session));
    }

    private void getAllCustomerList() {
        SessionViewModel sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        sessionViewModel.getCustomerSessionLiveData().observe(this, sessionList -> {
            questionList.clear();
            questionList.addAll(sessionList);
            sessionListRv.notifyDataSetChanged();
            Models.stopLoadingData(sessionPb);

        });
    }


    private void getAllMaidList() {
        SessionViewModel sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        sessionViewModel.getMaidSessionLiveData().observe(this, sessionList -> {
            questionList.clear();
            questionList.addAll(sessionList);
            sessionListRv.notifyDataSetChanged();
            Models.stopLoadingData(sessionPb);
        });
    }


    private void getAllSessionsList() {
        SessionViewModel sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        sessionViewModel.getAllSessionsLiveData().observe(this, sessionList -> {
            questionList.clear();
            questionList.addAll(sessionList);
            sessionListRv.notifyDataSetChanged();
        });
    }

    private void setCircleIndicator(RecyclerView recyclerView, SessionListRv adapter) {
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        CircleIndicator2 indicator = findViewById(R.id.rvQuestionIndicator);
        indicator.attachToRecyclerView(recyclerView, pagerSnapHelper);

        // optional
        adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setMaidMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list));
        setAnimatedBg(toolbar);
        toolbar.setSubtitle(R.string.maid);
        loadOfflineMaidSessions();
        loadOnlineMaidSessions();
    }

    private void loadOnlineMaidSessions() {
        Models.isLoadingData(sessionPb);
        getAllMaidList();
    }

    private void loadOfflineMaidSessions() {

    }

    private void loadOnlineCustomerSessions() {
        Models.isLoadingData(sessionPb);
        getAllCustomerList();
    }

    private void loadOfflineCustomerSessions() {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setCustomerMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list_cust));
        setAnimatedBg(toolbar);
        toolbar.setSubtitle(R.string.customer);
        loadOfflineCustomerSessions();
        loadOnlineCustomerSessions();
    }


    private void setAnimatedBg(Toolbar toolbar) {
        AnimationDrawable animationDrawable = (AnimationDrawable) toolbar.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }

    private Models.QuestionClass testResolver(String questionId) {
        Models.QuestionClass[] question = new Models.QuestionClass[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(QUESTION_DB).document(questionId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Question resolved");
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
                    System.out.println(question[0].getQuestionId()+ " id of question is "+ question[0].getPrimaryQuestion()+" of type "+ question[0].getQuestionType());
                }
            } else {
                System.out.println("Question not resolved");
            }
        });
        return question[0];
    }



}