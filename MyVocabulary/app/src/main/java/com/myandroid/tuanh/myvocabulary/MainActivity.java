package com.myandroid.tuanh.myvocabulary;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    Button buttonStart,buttonQuit,buttonName;
    Dialog dialogAuthor;
    FloatingActionButton floatingActionButtonInfo;
    LinearLayout linearLayoutInfo;
    private void create(){
        buttonStart= findViewById(R.id.buttonStart);
        floatingActionButtonInfo= findViewById(R.id.floatingActionButtonInfo);
        buttonQuit= findViewById(R.id.buttonQuit);
        buttonName= findViewById(R.id.buttonAuthor);
        linearLayoutInfo= findViewById(R.id.layoutinfo);
        linearLayoutInfo.setVisibility(View.GONE);
    }

    private void onClick(){
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutInfo.setVisibility(View.GONE);
                buttonStart.setVisibility(View.VISIBLE);
                floatingActionButtonInfo.setVisibility(View.VISIBLE);
            }
        });
        buttonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAuthor.show();
            }
        });
        floatingActionButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setVisibility(View.GONE);
                floatingActionButtonInfo.setVisibility(View.GONE);
                linearLayoutInfo.setVisibility(View.VISIBLE);
            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
    private void createDialog(){
        dialogAuthor= new Dialog(this);
        dialogAuthor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAuthor.setContentView(R.layout.layout_author);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create();

        createDialog();

        onClick();
    }
}
