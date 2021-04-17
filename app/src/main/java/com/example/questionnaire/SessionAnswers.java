package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questionnaire.R;
import com.example.questionnaire.adapter.AnswerListRv;
import com.example.questionnaire.adapter.QuestionListRv;
import com.example.questionnaire.adapter.SessionListRv;
import com.example.questionnaire.model.Models;
import com.example.questionnaire.questionDb.QuestionsViewModel;
import com.example.questionnaire.utils.MyLinkedMap;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator2;

import static com.example.questionnaire.model.Models.WORK_CUSTOMER;
import static com.example.questionnaire.model.Models.WORK_MAID;
import static com.example.questionnaire.model.Models.getAnswer1Answer2;
import static com.example.questionnaire.model.Models.getMapFromString;

public class SessionAnswers extends AppCompatActivity {

    private ProgressBar progressBar;
    private Models.QuestionSession session;
    public static final String SESSION = "session";

    private Toolbar answersQtb;
    private final ArrayList<Models.QuestionClass> answerSessions = new ArrayList<>();
    private final ArrayList<Models.QuestionClass> allQuestions = new ArrayList<>();

    private AnswerListRv answerAdapter;
    private MyLinkedMap<String,String> answerMapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_answers);

        answersQtb = findViewById(R.id.answersQtb);
        setSupportActionBar(answersQtb);
        answersQtb.setNavigationOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            session = (Models.QuestionSession) extras.get(SESSION);
            answersQtb.setSubtitle(session.getWorkForceAgentName().concat(" (").concat(session.getWorkForceAgentAge()+" yrs").concat(") ").concat(session.getWorkForceType()));
            answerMapList = getMapFromString(session.getAnswerListMap());
            System.out.println("map is "+answerMapList);
        }



        progressBar = findViewById(R.id.answers_progress_bar);
        setUpPage(session.getWorkForceType());

        RecyclerView answersQrv = findViewById(R.id.answersQrv);
        answersQrv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        answerAdapter = new AnswerListRv(this, answerSessions);
        answersQrv.setAdapter(answerAdapter);
        answerAdapter.setClickListener(new AnswerListRv.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(SessionAnswers.this, "answer is at "+ answerSessions.get(position).getClosedAnswerYes().concat(" : ").concat(answerSessions.get(position).getClosedAnswerNo()), Toast.LENGTH_SHORT).show();
            }
        });

        setCircleIndicator(answersQrv, answerAdapter);



    }

    private void answerResolver () {
        inProgress();
        for (int i = 0; i <= answerMapList.size() - 1; i++) {
            System.out.println("Question loop will run : "+answerMapList.size() + "times");
           for (int b = 0; b <= allQuestions.size() - 1;b++) {
               System.out.println("Questions too be checked are "+allQuestions.size());
               Models.QuestionClass questionSession = allQuestions.get(b);
               if (answerMapList.getKey(i).equals(questionSession.getQuestionId())) {
                   MyLinkedMap<String,String> answerMap = getAnswer1Answer2(answerMapList.getValue(i));
                   questionSession.setPrimaryAnswer(answerMap.getKey(0)); //primary answer
                   questionSession.setSecondaryAnswer(answerMap.getValue(0));
                   System.out.println("Question '"+questionSession.getPrimaryQuestion()+"' of id '"+questionSession.getQuestionId()+"' has yes answer : "+ questionSession.getClosedAnswerYes()+" and no answer : "+questionSession.getClosedAnswerNo());
                   answerSessions.add(questionSession);
                   answerAdapter.notifyDataSetChanged();
                   outProgress();
               }
           }
        }
    }

    private void getAllMaidQuestions() {
        QuestionsViewModel questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionsViewModel.getMaidQuestionsLiveData().observe(this, questionClasses -> {
            if (questionClasses.size() == 0) {
                Toast.makeText(SessionAnswers.this, "Online maid questions are empty", Toast.LENGTH_SHORT).show();
            } else {
                allQuestions.clear();
                allQuestions.addAll(questionClasses);
                answerResolver();
            }
        });
    }

    private void getAllCustomerQuestions() {
        QuestionsViewModel questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionsViewModel.getCustomerQuestionsLiveData().observe(this, questionClasses -> {
            if (questionClasses.size() == 0) {
                Toast.makeText(SessionAnswers.this, "Online customer questions are empty", Toast.LENGTH_SHORT).show();
            } else {
                allQuestions.clear();
                allQuestions.addAll(questionClasses);
                answerResolver();
            }
        });
    }

    private void setUpPage(String type) {
        if (type.equals(WORK_MAID)) {
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.RED));
            setMaidMode(answersQtb);
            getAllMaidQuestions();
        } else if (type.equals(WORK_CUSTOMER)) {
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.GREEN));
            setCustomerMode(answersQtb);
            getAllCustomerQuestions();
        } else {
            progressBar.setIndeterminateTintList(null);
        }
        inProgress();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setMaidMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list));
        setAnimatedBg(toolbar);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setCustomerMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list_cust));
        setAnimatedBg(toolbar);

    }

    private void setAnimatedBg(Toolbar toolbar) {
        AnimationDrawable animationDrawable = (AnimationDrawable) toolbar.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }

    private void setCircleIndicator(RecyclerView recyclerView, AnswerListRv adapter) {
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        CircleIndicator2 indicator = findViewById(R.id.rvQuestionIndicator);
        indicator.attachToRecyclerView(recyclerView, pagerSnapHelper);

        // optional
        adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());
    }

    private void populateList() {

    }

    private void inProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void outProgress() {
        progressBar.setVisibility(View.GONE);
    }
}