package com.myandroid.tuanh.myvocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    Button buttonPLib,buttonGame,buttonGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        buttonPLib= findViewById(R.id.buttonLib);
        buttonPLib.setAnimation(AnimationUtils.loadAnimation(MainMenuActivity.this,R.anim.sacle));
        buttonGame= findViewById(R.id.buttonGame);
        buttonGame.setAnimation(AnimationUtils.loadAnimation(MainMenuActivity.this,R.anim.sacle));
        buttonGoogle= findViewById(R.id.buttonGoogle);
        buttonGoogle.setAnimation(AnimationUtils.loadAnimation(MainMenuActivity.this,R.anim.sacle));
        buttonGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this,GameActivity.class);
                startActivity(intent);
            }
        });
        buttonPLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainMenuActivity.this,FolderActivity.class);
                startActivity(intent);
            }
        });
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainMenuActivity.this,TranslateActivity.class);
                startActivity(intent);
            }
        });
    }
}
