package com.myandroid.tuanh.mywords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    Button buttonPLib,buttonGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        buttonPLib= (Button)findViewById(R.id.buttonLib);
        buttonGame=(Button)findViewById(R.id.buttonGame);
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
                Intent intent= new Intent(MainMenuActivity.this,LibActivity.class);
                startActivity(intent);
            }
        });
    }
}
