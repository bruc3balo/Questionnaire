package com.example.questionnaire;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler(Looper.myLooper()).postDelayed(this::goToChooseWorkForce, 2000);
        hideNavBar();
        setTitle();
    }

    private void hideNavBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void goToChooseWorkForce() {
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        finish();
    }

    private void setTitle() {
        SpannableString q = new SpannableString(getResources().getString(R.string.questionnaire));
        TextView splashTitleTv = findViewById(R.id.splashTitleTv);
        splashTitleTv.setText(underlineString(boldItalicString(q)));
    }

    static SpannableString boldItalicString(SpannableString s) {
        SpannableString boldnessMamamia = new SpannableString(s);
        boldnessMamamia.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, s.length(), 0);
        return boldnessMamamia;
    }


    static SpannableString underlineString(SpannableString s) {
        SpannableString underlined = new SpannableString(s);
        underlined.setSpan(new UnderlineSpan(), 0, s.length(), 0);
        return underlined;
    }

    static SpannableString boldItalicUnderline(SpannableString s) {
        SpannableString underlinedBoldItalic = new SpannableString(s);
        underlineString(boldItalicString(underlinedBoldItalic));
        return underlinedBoldItalic;
    }



}