package com.myandroid.tuanh.mywords;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import applib.MyWord;
import database.SQLiteDataController;
import database.WordDB;
import gameofvoca.Guess;

public class GameGWActivity extends AppCompatActivity {

    private ArrayList<MyWord> arrayList= new ArrayList<>();
    private ProgressBar progressBarLoad;
    private Button buttonStart, buttonQues, buttonSc;
    private ConstraintLayout constraintLayoutLoad;
    private RadioGroup radioGroup;
    private RadioButton radioButtonA, radioButtonB,radioButtonC,radioButtonD;
    private Guess guess;
    private CountDownTimer countDownTimer;
    private int end= 0,tru= 0;
    private Toolbar toolbarGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_gw);
        create();
        final AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setTitle(" Xác nhận thoát trò chơi");
        alBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        alBuilder.setMessage("Bạn muốn thoát khỏi trò chơi mà không có điểm ?");
        alBuilder.setPositiveButton("Đúng rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(GameGWActivity.this, LibActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        alBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        toolbarGame.inflateMenu(R.menu.menu_game);
        toolbarGame.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.quit:
                        finish();
                        return true;
                    case R.id.library:
                        alBuilder.show();
                        return true;
                }
                return false;
            }
        });
        new LoadData().execute();
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayoutLoad.setVisibility(View.GONE);
                play();
            }
        });

    }
    private void play()
    {
        buttonSc.setText(""+tru+"/"+end);
        radioGroup.clearCheck();
        guess = new Guess(arrayList);
        Vector<String> vector= guess.run();
        buttonQues.setText(vector.get(0));
        radioButtonA.setText(vector.get(1));
        radioButtonB.setText(vector.get(2));
        radioButtonC.setText(vector.get(3));
        radioButtonD.setText(vector.get(4));
        setRadioGroup();
    }
    private void checkAns()
    {
        countDownTimer= new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                end++;
                if(guess.check()) {
                    tru++;
                    Toast.makeText(GameGWActivity.this, "Đúng !", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(GameGWActivity.this,"Sai !",Toast.LENGTH_LONG).show();
                play();
                countDownTimer.cancel();
            }
        }.start();
    }
    private void create()
    {
        progressBarLoad= (ProgressBar) findViewById(R.id.progressBar);
        buttonStart= (Button)findViewById(R.id.buttonStart);
        constraintLayoutLoad= (ConstraintLayout)findViewById(R.id.layoutLoad);
        buttonQues= (Button)findViewById(R.id.buttonQues);
        radioGroup= (RadioGroup)findViewById(R.id.radioGroupAns);
        radioButtonA=(RadioButton)findViewById(R.id.radioButtonA);
        radioButtonB=(RadioButton)findViewById(R.id.radioButtonB);
        radioButtonC=(RadioButton)findViewById(R.id.radioButtonC);
        radioButtonD=(RadioButton)findViewById(R.id.radioButtonD);
        buttonSc= (Button)findViewById(R.id.buttonScore);
        toolbarGame= (Toolbar)findViewById(R.id.toolbarGame);
    }
    private void setRadioGroup(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.radioButtonA: {
                        guess.setAns(0);
                        checkAns();
                        break;
                    }
                    case R.id.radioButtonB: {
                        guess.setAns(1);
                        checkAns();
                        break;
                    }
                    case R.id.radioButtonC: {
                        guess.setAns(2);
                        checkAns();
                        break;
                    }
                    case R.id.radioButtonD: {
                        guess.setAns(3);
                        checkAns();
                        break;
                    }
                }
            }
        });
    }
    private class LoadData extends AsyncTask<Void,String,String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            createDB();
            WordDB wordDB= new WordDB(GameGWActivity.this);
            arrayList= wordDB.getList();
            publishProgress("Load... done !");
            return "Done !";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(GameGWActivity.this,values[0],Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBarLoad.setVisibility(View.GONE);
            buttonStart.setVisibility(View.VISIBLE);
        }
    }
    private void createDB() {
// khởi tạo database
        SQLiteDataController sql = new SQLiteDataController(this);
        try {
            sql.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
