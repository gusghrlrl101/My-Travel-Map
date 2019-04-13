package com.example.mytravelmap;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 도움말 목록
        addSlide(SampleSlide.newInstance(R.layout.app_intro1));
        addSlide(SampleSlide.newInstance(R.layout.app_intro2));
        addSlide(SampleSlide.newInstance(R.layout.app_intro3));
        addSlide(SampleSlide.newInstance(R.layout.app_intro4));
        addSlide(SampleSlide.newInstance(R.layout.app_intro5));
        // 색깔 설정
        setBarColor(Color.parseColor("#FFFFFF"));
        setSeparatorColor(Color.parseColor("#FFFFFF"));
        setIndicatorColor(Color.parseColor("#000000"), Color.parseColor("#0000FF"));
        setColorDoneText(Color.parseColor("#FF0000"));
        setNextArrowColor(Color.parseColor("#0000FF"));
        // 애니메이션 설정
        setFadeAnimation();
        // 스킵 버튼 숨기기
        showSkipButton(false);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Main 액티비티로 이동
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        intent.putExtra("done", true);
        startActivity(intent);
    }
}
