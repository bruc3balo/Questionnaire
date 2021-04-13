package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questionnaire.model.Models;
import com.example.questionnaire.utils.MyLinkedMap;

import java.util.ArrayList;
import java.util.Map;

import static com.example.questionnaire.QuestionsActivity.CUSTOMER;
import static com.example.questionnaire.QuestionsActivity.MAID;
import static com.example.questionnaire.QuestionsActivity.WORK_FORCE;
import static com.example.questionnaire.SplashScreen.boldItalicString;
import static com.example.questionnaire.SplashScreen.underlineString;
import static com.example.questionnaire.model.Models.keyValueSep;
import static com.example.questionnaire.model.Models.primary_secondary_sep;
import static com.example.questionnaire.model.Models.sep1;
import static com.example.questionnaire.model.Models.sep2;

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

        MyLinkedMap<String,String> myMap = mapFromListWithDifferentKeys(keyList, contentList);
        String s = getStringListFromMap(myMap);

        System.out.println("map is " + myMap);
        System.out.println("String map is "+ s);
        System.out.println("mapString is "+ getMapFromString(s));
    }

    private MyLinkedMap<String, String> mapFromListWithDifferentKeys(String[] keyList, String[] list) {
        MyLinkedMap<String, String> map = new MyLinkedMap<>();
        if (list.length == 0 || keyList.length == 0) {
            System.out.println("map is empty");
            return map;
        } else {
            for (int i = 0; i <= list.length - 1; i++) {
                map.put(keyList[i], list[i]);
                System.out.println("key is " + keyList[i] + "map is " + list[i]);
            }

        }

        return map;
    }
    private String getStringListFromMap(MyLinkedMap<String, String> map) {
        String mapString = "";
        if (map.isEmpty()) {
            return mapString;

        } else {
            for (Map.Entry<String, String> pair : map.entrySet()) {
                mapString = mapString.concat(sep1.concat(pair.getKey()).concat(keyValueSep).concat(pair.getValue()).concat(sep2));
                System.out.println("entry "+mapString);
            }
        }

        return mapString;
    }
    private MyLinkedMap<String, String> getMapFromString(String s) {
        MyLinkedMap<String, String> map = new MyLinkedMap<>();
        if (s == null) {
            return map;
        } else if (s.equals("")) {
            return map;
        } else {
            String key = "";
            String value = "";
            String entry = "";

            if (s.equals("")) {
                return map;
            } else { //if sep1 > start, if : > 1st word is key, if } > 2nd word is value ... end word
                boolean keyFound = false;
                for (int i = 0; i <= s.length() - 1; i++) {
                    if (String.valueOf(s.charAt(i)).equals(sep1)) {
                        System.out.println("Start of map");
                    } else if (String.valueOf(s.charAt(i)).equals(sep2)) {
                        value = entry;
                        keyFound = false;
                        map.put(key, value);
                        entry = "";
                        System.out.println("End : entry is " + key + keyValueSep + value);
                    } else {

                        if (String.valueOf(s.charAt(i)).equals(keyValueSep)) { //(key:value) //reached key

                            if (!keyFound) {
                                key = entry;
                                entry = "";
                                keyFound = true;
                                System.out.println("key is " + key);
                            }


                        } else {
                            entry = entry.concat(String.valueOf(s.charAt(i))); //add word
                        }
                    }
                }
            }
        }
        return map;
    }

    private MyLinkedMap<String,String> getAnswer1Answer2 (String s) {
        MyLinkedMap<String,String> map = new MyLinkedMap<>();



        return answers;
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