package com.nguyentudien.mycalculate;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText ed1,ed2;
    Button bAdd, bSub, bMul, bDiv, bEqual, bResult;
    Double n1,n2;
    char c;
    MyCal myCal;
    void setID(){
        ed1 = (EditText) findViewById(R.id.editTextNum1);
        ed2 = (EditText) findViewById(R.id.editTextNum2);
        bAdd = (Button) findViewById(R.id.buttonAdd);
        bSub = (Button) findViewById(R.id.buttonSub);
        bMul = (Button) findViewById(R.id.buttonMul);
        bDiv = (Button) findViewById(R.id.buttonDiv);
        bEqual = (Button) findViewById(R.id.buttonEqual);
        bResult = (Button) findViewById(R.id.buttonResult);
    }
    void setOnClick(){
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c == '-') bSub.setBackgroundColor(Color.parseColor("#FF9800"));
                if(c == '*') bMul.setBackgroundColor(Color.parseColor("#FF9800"));
                if(c == '/') bDiv.setBackgroundColor(Color.parseColor("#FF9800"));
                bAdd.setBackgroundColor(Color.parseColor("#E91E63"));
                c = '+';
            }
        });
        bSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c == '+') bAdd.setBackgroundColor(Color.parseColor("#FF9800"));
                if(c == '*') bMul.setBackgroundColor(Color.parseColor("#FF9800"));
                if(c == '/') bDiv.setBackgroundColor(Color.parseColor("#FF9800"));
                bSub.setBackgroundColor(Color.parseColor("#E91E63"));
                c = '-';
            }
        });
        bMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c == '-') bSub.setBackgroundColor(Color.parseColor("#FF9800"));
                if(c == '+') bAdd.setBackgroundColor(Color.parseColor("#FF9800"));
                if(c == '/') bDiv.setBackgroundColor(Color.parseColor("#FF9800"));
                bMul.setBackgroundColor(Color.parseColor("#E91E63"));
                c = '*';
            }
        });
        bDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c == '-') bSub.setBackgroundColor(Color.parseColor("#FF9800"));
                if(c == '*') bMul.setBackgroundColor(Color.parseColor("#FF9800"));
                if(c == '+') bAdd.setBackgroundColor(Color.parseColor("#FF9800"));
                bDiv.setBackgroundColor(Color.parseColor("#E91E63"));
                c = '/';
            }
        });
        bEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n1 = Double.parseDouble(ed1.getText().toString());
                n2 = Double.parseDouble(ed2.getText().toString());
                myCal = new MyCal(n1,n2);
                myCal.set(c);
                bResult.setText(myCal.getResult().toString());
            }
        });
        bResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText(bResult.getText());
            }
        });
    }
    void setBackroudColor(){
        bAdd.setBackgroundColor(Color.parseColor("#FF9800"));
        bSub.setBackgroundColor(Color.parseColor("#FF9800"));
        bMul.setBackgroundColor(Color.parseColor("#FF9800"));
        bDiv.setBackgroundColor(Color.parseColor("#FF9800"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setID();
        setBackroudColor();
        myCal = new MyCal();
        setOnClick();
    }
}
