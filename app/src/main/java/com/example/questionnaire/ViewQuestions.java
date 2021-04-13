package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questionnaire.adapter.QuestionListRv;
import com.example.questionnaire.model.Models;
import com.example.questionnaire.questionDb.QuestionRepository;

import java.util.ArrayList;

import static com.example.questionnaire.QuestionsActivity.CUSTOMER;
import static com.example.questionnaire.QuestionsActivity.MAID;

public class ViewQuestions extends AppCompatActivity {

    private QuestionListRv questionListRv;
    private final ArrayList<Models.QuestionClass> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);
        Toolbar viewQtb = findViewById(R.id.viewQtb);
        setSupportActionBar(viewQtb);
        viewQtb.setNavigationOnClickListener(v -> finish());

        RadioGroup typeGroup = findViewById(R.id.typeGroup);
        typeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.maidRadio) {
                setMaidMode(viewQtb);
            } else if (checkedId == R.id.customerRadio) {
                setCustomerMode(viewQtb);
            }
        });

        RecyclerView viewQRv = findViewById(R.id.viewQRv);
        viewQRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        questionListRv = new QuestionListRv(this, questionList);
        viewQRv.setAdapter(questionListRv);
    }

    private void loadOfflineCustomerQuestions() {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        questionRepository.getAllQuestionsTypeLiveData(CUSTOMER).observe(this, questionClasses -> {
            questionList.clear();
            questionList.addAll(questionClasses);
            questionListRv.notifyDataSetChanged();
        });
    }

    private void loadOfflineMaidQuestions() {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        questionRepository.getAllQuestionsTypeLiveData(MAID).observe(this, questionClasses -> {
            questionList.clear();
            questionList.addAll(questionClasses);
            questionListRv.notifyDataSetChanged();
        });
    }

    private void loadOnlineCustomerQuestions() {

    }

    private void loadOnlineMaidQuestions() {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setMaidMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list));
        setAnimatedBg(toolbar);
        toolbar.setSubtitle(R.string.maid);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setCustomerMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list_cust));
        setAnimatedBg(toolbar);
        toolbar.setSubtitle(R.string.customer);
    }

    private void setAnimatedBg(Toolbar toolbar) {
        AnimationDrawable animationDrawable = (AnimationDrawable) toolbar.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }

}