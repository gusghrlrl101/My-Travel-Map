package com.example.mytravelmap;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(SampleSlide.newInstance(R.layout.app_intro1));
        addSlide(SampleSlide.newInstance(R.layout.app_intro2));
        addSlide(SampleSlide.newInstance(R.layout.app_intro3));
        addSlide(SampleSlide.newInstance(R.layout.app_intro4));
        addSlide(SampleSlide.newInstance(R.layout.app_intro5));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#FFFFFF"));
        setSeparatorColor(Color.parseColor("#FFFFFF"));
        setIndicatorColor(Color.parseColor("#000000"), Color.parseColor("#0000FF"));
        setColorDoneText(Color.parseColor("#FF0000"));
        setNextArrowColor(Color.parseColor("#0000FF"));
        setFadeAnimation();

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        finish();
        intent.putExtra("done", true);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
