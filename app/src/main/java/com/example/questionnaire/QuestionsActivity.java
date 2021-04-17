package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.questionnaire.adapter.QuestionAdapter;
import com.example.questionnaire.model.Models;
import com.example.questionnaire.questionDb.QuestionsViewModel;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

import static com.example.questionnaire.model.Models.QuestionClass.CLOSED_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_CLOSED;
import static com.example.questionnaire.model.Models.QuestionClass.CONDITION_QUESTION_OPEN;
import static com.example.questionnaire.model.Models.QuestionClass.OPEN_QUESTION;
import static com.example.questionnaire.model.Models.QuestionClass.RATING_QUESTION;


public class QuestionsActivity extends AppCompatActivity {

    public static final int MAID = 0;
    public static final int CUSTOMER = 1;
    public static final String WORK_FORCE = "Work Force";
    private int type = 0;
    private boolean backPressed;
    private int current_position;
    private QuestionAdapter questionAdapter;
    private final ArrayList<Models.QuestionClass> questionList = new ArrayList<>();
    private Models.QuestionSession questionSession;
    private String nameOfCandidate = "";
    private String age = "";
    private Toolbar showQtb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        showQtb = findViewById(R.id.showQtb);
        setSupportActionBar(showQtb);
        showQtb.setNavigationOnClickListener(v -> cancelQuestionAsking());

        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt(WORK_FORCE);
        }
        showWelcomeDialog();


        //getAllOnlineQuestions();

    }

    private void showWelcomeDialog() {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.info_layout);
        TextView infoTv = d.findViewById(R.id.infoTv);
        Button dismissButton = d.findViewById(R.id.dismissButton);
        d.show();
        infoTv.setText("Welcome to this ".concat(getResources().getString(R.string.questionnaire)).concat("."));
        dismissButton.setOnClickListener(v -> d.dismiss());
        d.setOnDismissListener(dialog -> enterNameDialog());
    }

    private void enterNameDialog() {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.enter_single_value_layout);
        TextView descriptionDialog = d.findViewById(R.id.descriptionDialog);
        TextView valueField = d.findViewById(R.id.valueField);
        Button submitButton = d.findViewById(R.id.submitButton);

        d.show();

        descriptionDialog.setText("Enter name of candidate ...");
        descriptionDialog.setInputType(InputType.TYPE_CLASS_TEXT);
        submitButton.setOnClickListener(v -> {
            if (valueField.getText().toString().isEmpty()) {
                valueField.setError("Name cannot be empty");
                valueField.requestFocus();
            } else {
                nameOfCandidate = valueField.getText().toString();
                d.dismiss();

            }
        });
        d.setOnDismissListener(dialog -> {
            if (nameOfCandidate.equals("")) {
                Toast.makeText(QuestionsActivity.this, "Enter name to continue", Toast.LENGTH_SHORT).show();
                enterNameDialog();
            } else {
                enterAgeDialog(nameOfCandidate);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void enterAgeDialog(String nameOfCandidate) {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.enter_single_value_layout);
        TextView descriptionDialog = d.findViewById(R.id.descriptionDialog);
        TextView valueField = d.findViewById(R.id.valueField);
        Button submitButton = d.findViewById(R.id.submitButton);

        d.show();

        descriptionDialog.setText("Enter age of candidate " + nameOfCandidate);
        descriptionDialog.setRawInputType(Configuration.KEYBOARD_12KEY);
        submitButton.setOnClickListener(v -> {
            if (valueField.getText().toString().isEmpty()) {
                valueField.setError("Age cannot be empty");
                valueField.requestFocus();
            } else {
                age = valueField.getText().toString();
                d.dismiss();
            }
        });

        d.setOnDismissListener(dialog -> {
            if (age.equals("")) {
                Toast.makeText(QuestionsActivity.this, "Must enter age to continue", Toast.LENGTH_SHORT).show();
                enterAgeDialog(nameOfCandidate);
            } else {
                decideQuestionnaireType(type, showQtb);
            }
        });
    }

    private void getAllOnlineQuestions() {
        QuestionsViewModel questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionsViewModel.getQuestionsLiveData().observe(this, questionClasses -> {
            if (questionClasses.size() == 0) {
                Toast.makeText(this, "Online questions are empty", Toast.LENGTH_SHORT).show();
            } else {
                questionList.addAll(questionClasses);
                questionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getAllMaidQuestions() {
        QuestionsViewModel questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionsViewModel.getMaidQuestionsLiveData().observe(this, questionClasses -> {
            if (questionClasses.size() == 0) {
                String empty = "Online maid questions are empty";
                Toast.makeText(this, empty, Toast.LENGTH_SHORT).show();
                showEmptyListDialog(empty);
            } else {
                questionList.clear();
                questionList.addAll(questionClasses);
                questionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getAllCustomerQuestions() {
        QuestionsViewModel questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        questionsViewModel.getCustomerQuestionsLiveData().observe(this, questionClasses -> {
            if (questionClasses.size() == 0) {
                String empty = "Online customer questions are empty";
                Toast.makeText(this, empty, Toast.LENGTH_SHORT).show();
                showEmptyListDialog(empty);
            } else {
                questionList.clear();
                questionList.addAll(questionClasses);
                questionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showEmptyListDialog(String s) {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.info_layout);
        TextView infoTv = d.findViewById(R.id.infoTv);
        infoTv.setText(s);
        d.show();
        Button dismiss = d.findViewById(R.id.dismissButton);
        dismiss.setOnClickListener(v -> d.dismiss());
        d.setOnDismissListener(dialog -> finish());
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

    private void decideQuestionnaireType(int n, Toolbar toolbar) {
        if (n == MAID) {
            startMaidQuestions(toolbar);
            getAllMaidQuestions();
        } else if (n == CUSTOMER) {
            startCustomerQuestions(toolbar);
            getAllCustomerQuestions();
        } else {
            failedToGetType();
        }

        setUpViewPager(showQtb);
    }

    private void failedToGetType() {
        Toast.makeText(getApplicationContext(), "Failed to get type, Try Again !", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void cancelQuestionAsking() {
        finish();
    }

    private void startMaidQuestions(Toolbar toolbar) {
        toolbar.setTitle(getResources().getString(R.string.questionnaire).concat("'s for maids"));
    }

    private void startCustomerQuestions(Toolbar toolbar) {
        toolbar.setTitle(getResources().getString(R.string.questionnaire).concat("'s for customers"));
    }

    private void setUpViewPager(Toolbar toolbar) {
        //addDummyQuestions();
        ViewPager2 questionViewPager = findViewById(R.id.questionViewPager);
        questionViewPager.setUserInputEnabled(false);

        questionViewPager.setPadding(20, 50, 20, 50);
        questionViewPager.setClipToPadding(false);
        questionViewPager.setClipChildren(false);
        questionViewPager.setOffscreenPageLimit(3);

        questionAdapter = new QuestionAdapter(this, questionList, questionViewPager, nameOfCandidate, age, type, QuestionsActivity.this);
        questionViewPager.setAdapter(questionAdapter);
        questionAdapter.setClickListener((view, position) -> {
        });


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleX(0.85f + r * 0.15f);
        });

        //Indicator
        CircleIndicator3 viewPagerIndicator = findViewById(R.id.questionIndicator);
        viewPagerIndicator.setViewPager(questionViewPager);
        // optional
        questionAdapter.registerAdapterDataObserver(viewPagerIndicator.getAdapterDataObserver());

        questionViewPager.setPageTransformer(compositePageTransformer);
        ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                current_position = position;
                questionViewPager.setCurrentItem(position);
                questionAdapter.setCurrent_position(position);
                toolbar.setSubtitle(questionList.get(position).getQuestionType());


                //   toolbar.setSubtitle(questionList.get(position));
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        };
        questionViewPager.registerOnPageChangeCallback(onPageChangeCallback);


    }

    @Override
    public void finish() {
        super.finish();
        if (type == MAID) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (type == CUSTOMER) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        backPressed = false;
    }

    @Override
    public void onBackPressed() {
        if (!backPressed) {
            Toast.makeText(this, "Press back to exit", Toast.LENGTH_SHORT).show();
            backPressed = true;
        } else {
            finish();
            super.onBackPressed();
        }
    }
}