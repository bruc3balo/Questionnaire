package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questionnaire.adapter.QuestionListRv;
import com.example.questionnaire.model.Models;
import com.example.questionnaire.questionDb.QuestionRepository;
import com.example.questionnaire.questionDb.QuestionsViewModel;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator2;

import static com.example.questionnaire.QuestionsActivity.CUSTOMER;
import static com.example.questionnaire.QuestionsActivity.MAID;
import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_CLOSED;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_OPEN;
import static com.example.questionnaire.model.Models.QuestionClass.OPEN_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.QUESTION_DB;
import static com.example.questionnaire.model.Models.QuestionClass.RATING_QUESTION;

public class ViewQuestions extends AppCompatActivity {

    private QuestionListRv questionListRv;
    private final ArrayList<Models.QuestionClass> questionList = new ArrayList<>();
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);
        Toolbar viewQtb = findViewById(R.id.viewQtb);
        setSupportActionBar(viewQtb);
        viewQtb.setNavigationOnClickListener(v -> finish());

        progressBar = findViewById(R.id.viewPb);
        progressBar.setVisibility(View.GONE);

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
        questionListRv.setClickListener((view, position) -> editQuestion(questionList.get(position)));

        setCircleIndicator(viewQRv, questionListRv);



        // getAllQuestions();
        //getAllOnlineQuestions();

        // addDummyQuestions();

    }

    private void editQuestion(Models.QuestionClass question) {
        startActivity(new Intent(ViewQuestions.this,EditQuestion.class).putExtra(QUESTION_DB,question));
    }

    private void setCircleIndicator(RecyclerView recyclerView, QuestionListRv adapter) {
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        CircleIndicator2 indicator = findViewById(R.id.rvQuestionIndicator);
        indicator.attachToRecyclerView(recyclerView, pagerSnapHelper);

        // optional
        adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());
    }

    private void addDummyQuestions() {

        String q = "Is this a question?";
        String q1 = "Is this a secondary question";

        Models.QuestionClass questionClass = new Models.QuestionClass("1");
        questionClass.setQuestionType(OPEN_QUESTION);
        questionClass.setPrimaryQuestion(q);

        questionList.add(questionClass);

        Models.QuestionClass questionClass1 = new Models.QuestionClass("12");
        questionClass1.setQuestionType(CLOSED_QUESTION);
        questionList.add(questionClass1);
        questionClass1.setPrimaryQuestion(q);
        questionClass1.setClosedAnswerNo("Not at all");
        questionClass1.setClosedAnswerYes("Absolutely");

        Models.QuestionClass questionClass2 = new Models.QuestionClass("123");
        questionClass2.setQuestionType(RATING_QUESTION);
        questionList.add(questionClass2);
        questionClass2.setPrimaryQuestion(q);

        Models.QuestionClass questionClass3 = new Models.QuestionClass("1234");
        questionClass3.setQuestionType(CONDITION_QUESTION_OPEN);
        questionList.add(questionClass3);
        questionClass3.setPrimaryQuestion(q);
        questionClass3.setClosedAnswerNo("Not at all");
        questionClass3.setSecondaryQuestion(q1);
        questionClass3.setClosedAnswerYes("Absolutely");

        Models.QuestionClass questionClass4 = new Models.QuestionClass("12345");
        questionClass4.setQuestionType(CONDITION_QUESTION_CLOSED);
        questionList.add(questionClass4);
        questionClass4.setPrimaryQuestion(q);
        questionClass4.setClosedAnswerNo("Not at all");
        questionClass4.setSecondaryQuestion(q1);
        questionClass4.setClosedAnswerYes("Absolutely");


        Toast.makeText(this, "" + questionList.size(), Toast.LENGTH_SHORT).show();

    }

    private void loadOfflineCustomerQuestions() {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        questionRepository.getAllQuestionsTypeLiveData(CUSTOMER).observe(this, questionClasses -> {
            System.out.println(questionClasses.size() + "customer size");
            //questionList.clear();
            questionList.addAll(questionClasses);
            questionListRv.notifyDataSetChanged();
        });
    }

    private void loadOfflineMaidQuestions() {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        questionRepository.getAllQuestionsTypeLiveData(MAID).observe(this, questionClasses -> {
            System.out.println(questionClasses.size() + "maid size");
            // questionList.clear();
            questionList.addAll(questionClasses);
            questionListRv.notifyDataSetChanged();
        });
    }

    private void loadOnlineCustomerQuestions() {
        Models.isLoadingData(progressBar);
        getAllCustomerQuestions();
    }

    private void loadOnlineMaidQuestions() {
        Models.isLoadingData(progressBar);
        getAllMaidQuestions();
    }

    private void getAllQuestions() {
        QuestionRepository questionRepository = new QuestionRepository(getApplication());
        if (questionRepository.getAllQuestionListLiveData() != null) {
            questionRepository.getAllQuestionListLiveData().observe(this, questionClasses -> {
                if (questionClasses.size() == 0) {
                    Toast.makeText(this, "Offline questions are empty", Toast.LENGTH_SHORT).show();
                    getAllOnlineQuestions();
                } else {

                    questionList.addAll(questionClasses);
                    questionListRv.notifyDataSetChanged();
                }
            });
        } else {
            Toast.makeText(this, "Offline questions are empty", Toast.LENGTH_SHORT).show();
            getAllQuestions();
        }
    }

    private void getAllMaidQuestions() {
        QuestionsViewModel questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionsViewModel.getMaidQuestionsLiveData().observe(this, questionClasses -> {
            if (questionClasses.size() == 0) {
                Toast.makeText(ViewQuestions.this, "Online maid questions are empty", Toast.LENGTH_SHORT).show();
            } else {
                questionList.clear();
                questionList.addAll(questionClasses);
                questionListRv.notifyDataSetChanged();
            }

            Models.stopLoadingData(progressBar);
        });
    }

    private void getAllCustomerQuestions() {
        QuestionsViewModel questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionsViewModel.getCustomerQuestionsLiveData().observe(this, questionClasses -> {
            if (questionClasses.size() == 0) {
                Toast.makeText(ViewQuestions.this, "Online customer questions are empty", Toast.LENGTH_SHORT).show();
            } else {
                questionList.clear();
                questionList.addAll(questionClasses);
                questionListRv.notifyDataSetChanged();
            }

            Models.stopLoadingData(progressBar);

        });
    }


    private void getAllOnlineQuestions() {
        QuestionsViewModel questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionsViewModel.getQuestionsLiveData().observe(this, questionClasses -> {
            if (questionClasses.size() == 0) {
                Toast.makeText(this, "Online questions are empty", Toast.LENGTH_SHORT).show();
            } else {

                questionList.addAll(questionClasses);
                questionListRv.notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setMaidMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list));
        setAnimatedBg(toolbar);
        toolbar.setSubtitle(R.string.maid);
        loadOfflineMaidQuestions();
        loadOnlineMaidQuestions();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setCustomerMode(Toolbar toolbar) {
        toolbar.setBackground(getResources().getDrawable(R.drawable.gradient_list_cust));
        setAnimatedBg(toolbar);
        toolbar.setSubtitle(R.string.customer);
        loadOfflineCustomerQuestions();
        loadOnlineCustomerQuestions();
    }

    private void setAnimatedBg(Toolbar toolbar) {
        AnimationDrawable animationDrawable = (AnimationDrawable) toolbar.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }

}