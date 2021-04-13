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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questionnaire.utils.OnSwipeTouchListener;

import static com.example.questionnaire.QuestionsActivity.CUSTOMER;
import static com.example.questionnaire.QuestionsActivity.MAID;
import static com.example.questionnaire.QuestionsActivity.WORK_FORCE;
import static com.example.questionnaire.SplashScreen.boldItalicString;
import static com.example.questionnaire.SplashScreen.underlineString;

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