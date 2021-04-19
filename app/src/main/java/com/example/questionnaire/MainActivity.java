package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questionnaire.model.Models;
import com.example.questionnaire.utils.MyLinkedMap;

import static com.example.questionnaire.QuestionsActivity.CUSTOMER;
import static com.example.questionnaire.QuestionsActivity.MAID;
import static com.example.questionnaire.QuestionsActivity.WORK_FORCE;
import static com.example.questionnaire.SplashScreen.boldItalicString;
import static com.example.questionnaire.SplashScreen.underlineString;
import static com.example.questionnaire.model.Models.getAnswer1Answer2;
import static com.example.questionnaire.model.Models.getMapFromString;
import static com.example.questionnaire.model.Models.getStringListFromMap;
import static com.example.questionnaire.model.Models.mapFromListWithDifferentKeys;
import static com.example.questionnaire.model.Models.primary_secondary_sep;

public class MainActivity extends AppCompatActivity {

    private boolean backPressed;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Create
        ImageButton addNewQB = findViewById(R.id.addNewQB);
        addNewQB.setOnClickListener(v -> addNewQuestion());

        //Read//Update//Delete
        ImageButton viewQB = findViewById(R.id.viewQB);
        viewQB.setOnClickListener(v -> viewQuestions());

        ImageButton sessionQb = findViewById(R.id.sessionQb);
        sessionQb.setOnClickListener(v->viewSessions());


        //Maid
        ImageButton maidButton = findViewById(R.id.maidButton);
        maidButton.setOnClickListener(v -> {
            maidButton.setImageTintList(ColorStateList.valueOf(Color.RED));
            new Handler(Looper.myLooper()).postDelayed(() -> maidButton.setImageTintList(null), 300);
            new Handler(Looper.myLooper()).postDelayed(this::selectCleaner, 400);

        });

        //Customer
        ImageButton customerButton = findViewById(R.id.customerButton);
        customerButton.setOnClickListener(v -> {
            customerButton.setImageTintList(ColorStateList.valueOf(Color.RED));
            new Handler(Looper.myLooper()).postDelayed(() -> customerButton.setImageTintList(null), 300);
            new Handler(Looper.myLooper()).postDelayed(this::selectCustomer, 400);
        });

        animateCustomer(customerButton);
        animateMaid(maidButton);
        styleTitle();

        addDummyAnswer();
    }

    private void addDummyAnswer() {
        Models.AnswerSessions answerSessions = new Models.AnswerSessions("123");
        answerSessions.setPrimaryAnswer("answer 1");
        answerSessions.setSecondaryAnswer("answer 2");

        String[] keyList = new String[]{answerSessions.getQuestionId()};

        String[] contentList = new String[]{answerSessions.getPrimaryAnswer().concat(primary_secondary_sep).concat(answerSessions.getSecondaryAnswer())};

        MyLinkedMap<String, String> myMap = mapFromListWithDifferentKeys(keyList, contentList);
        String s = getStringListFromMap(myMap);
        MyLinkedMap<String,String> answerMap = getAnswer1Answer2(getMapFromString(s).getValue(0));

        System.out.println("map is " + myMap);
        System.out.println("String map is " + s);
        System.out.println("mapString is " + getMapFromString(s).getValue(0));
        System.out.println("answer 1 is "+answerMap.getKey(0) +"  answer 2 is " +answerMap.getValue(0));
    }



    private void selectCleaner() {
        startActivity(new Intent(MainActivity.this, QuestionsActivity.class).putExtra(WORK_FORCE, MAID));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void selectCustomer() {
        startActivity(new Intent(MainActivity.this, QuestionsActivity.class).putExtra(WORK_FORCE, CUSTOMER));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void viewQuestions() {
        startActivity(new Intent(MainActivity.this, ViewQuestions.class));
    }

    private void viewSessions() {
        startActivity(new Intent(MainActivity.this,SessionsHistory.class));
    }

    private void addNewQuestion() {
        startActivity(new Intent(MainActivity.this, AddNewQuestion.class));
    }


    private void animateMaid(ImageButton maidB) {
        setAnimatedBg(maidB);
    }

    private void animateCustomer(ImageButton custB) {
        setAnimatedBg(custB);
    }

    private void setAnimatedBg(ImageButton imageButton) {
        AnimationDrawable animationDrawable = (AnimationDrawable) imageButton.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
    }

    private void styleTitle() {
        TextView mainTitleTv = findViewById(R.id.mainTitleTv);
        SpannableString q = new SpannableString(getResources().getString(R.string.choose_your_work_force));
        mainTitleTv.setText(underlineString(boldItalicString(q)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        backPressed = false;
    }

    private void discardDialog() {
        Dialog d = new Dialog(MainActivity.this);
        d.setContentView(R.layout.skip_question_dialog);
        TextView info = d.findViewById(R.id.deleteInfoTv);
        Button yes = d.findViewById(R.id.yesButton);
        Button no = d.findViewById(R.id.noButton);
        d.show();
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        info.setText("Exit Application?");
        yes.setOnClickListener(v -> {
            d.dismiss();
            super.onBackPressed();
            finish();
        });
        no.setOnClickListener(v -> d.dismiss());
    }


    @Override
    public void onBackPressed() {
        if (!backPressed) {
            Toast.makeText(this, "Press back to exit", Toast.LENGTH_SHORT).show();
            backPressed = true;
        } else {
            discardDialog();
        }
    }
}