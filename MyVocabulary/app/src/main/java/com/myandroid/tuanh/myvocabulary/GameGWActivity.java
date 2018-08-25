package com.myandroid.tuanh.myvocabulary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.myandroid.tuanh.myvocabulary.applib.MyFolder;
import com.myandroid.tuanh.myvocabulary.applib.MyWord;
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import com.myandroid.tuanh.myvocabulary.database.FolderDB;
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController;
import com.myandroid.tuanh.myvocabulary.database.WordProDB;
import com.myandroid.tuanh.myvocabulary.gameofvoca.GroupBotton;
import com.myandroid.tuanh.myvocabulary.gameofvoca.Guess;

public class GameGWActivity extends AppCompatActivity {

    private ArrayList<MyWord> arrayList= new ArrayList<>();
    private ProgressBar progressBarLoad,progressBarTime;
    private Button buttonStart, buttonQues, buttonSc;
    private ConstraintLayout constraintLayoutLoad;
    private Button buttonA, buttonB,buttonC,buttonD,buttonFolder,buttonPause;
    private Guess guess;
    private CountDownTimer countDownTimer;
    private int end= 0,tru= 0;
    private Toolbar toolbarGame;
    private GroupBotton groupBotton;
    private Button buttonAns;
    private FloatingActionButton floatingActionButtonSel;
    private boolean f= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_gw);
        floatingActionButtonSel = findViewById(R.id.floatingActionButtonSelect);
        create();
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(f) {
                    groupBotton.clearOnClick();
                    countDownTimer.cancel();
                    buttonPause.setBackgroundResource(R.drawable.play);
                    f= false;
                }
                else {
                    play();
                    buttonPause.setBackgroundResource(R.drawable.pause);
                    f= true;
                }
            }
        });
        final AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setTitle(" Xác nhận thoát trò chơi");
        alBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        alBuilder.setMessage("Bạn muốn thoát khỏi trò chơi mà không có điểm ?");
        alBuilder.setPositiveButton("Đúng rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(GameGWActivity.this, FolderActivity.class);
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
        /*
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
        */
        new LoadData().execute();
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayoutLoad.setVisibility(View.GONE);
                play();
            }
        });

    }
    @SuppressLint("SetTextI18n")
    private void play()
    {
        setRadioGroup();
        Animation anim = AnimationUtils.loadAnimation(GameGWActivity.this,R.anim.sacle);
        buttonSc.setText(""+tru+"/"+end);
        guess = new Guess(arrayList);
        Vector<String> vector= guess.run();
        buttonQues.setText(vector.get(0));
        buttonQues.startAnimation(anim);
        buttonA.setText(vector.get(1));
        buttonB.setText(vector.get(2));
        buttonC.setText(vector.get(3));
        buttonD.setText(vector.get(4));
        int ss= 10000;
        int sss=100;
        if(guess.getType()==2) {
            ss = 20000;
            sss= 200;
        }
        progressBarTime.setProgress(1);
        countDownTimer= new CountDownTimer(ss,sss) {
            @Override
            public void onTick(long l) {
                int i= progressBarTime.getProgress();
                progressBarTime.setProgress(i+1);
            }
            @Override
            public void onFinish() {
                next();
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        groupBotton.clearOnClick();
        countDownTimer.cancel();
        buttonPause.setBackgroundResource(R.drawable.play);
        f= false;
        final AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setTitle(" Xác nhận thoát trò chơi");
        alBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        alBuilder.setMessage("Bạn muốn thoát khỏi trò chơi ?");
        alBuilder.setPositiveButton("Đúng rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                play();
                buttonPause.setBackgroundResource(R.drawable.pause);
                f= true;
            }
        });
        alBuilder.show();
    }

    private void next(){
        groupBotton.clearSelect();
        groupBotton.clearOnClick();
        end++;
        switch (guess.getAns()) {
            case 0: {
                buttonAns = findViewById(R.id.buttonA);
                break;
            }
            case 1: {
                buttonAns = findViewById(R.id.buttonB);
                break;
            }
            case 2: {
                buttonAns = findViewById(R.id.buttonC);
                break;
            }
            case 3: {
                buttonAns = findViewById(R.id.buttonD);
                break;
            }
        }
        progressBarTime.setProgress(0);
        countDownTimer.cancel();
        countDownTimer= new CountDownTimer(2000,20) {
            @Override
            public void onTick(long l) {
                int i= progressBarTime.getProgress();
                progressBarTime.setProgress(i+1);
                    if(i%5==0)
                        buttonAns.setBackgroundResource(R.drawable.background_button);
                    else
                        buttonAns.setBackgroundResource(R.drawable.background_new);
                    if(i==5)
                        Toast.makeText(GameGWActivity.this, "Bỏ qua !", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFinish() {
                buttonAns.setBackgroundResource(R.drawable.background_new);
                play();
            }
        }.start();
    }
    private void checkAns()
    {
        countDownTimer.cancel();
        switch (guess.getAns()) {
            case 0: {
                buttonAns = findViewById(R.id.buttonA);
                break;
            }
            case 1: {
                buttonAns = findViewById(R.id.buttonB);
                break;
            }
            case 2: {
                buttonAns = findViewById(R.id.buttonC);
                break;
            }
            case 3: {
                buttonAns = findViewById(R.id.buttonD);
                break;
            }
        }
        progressBarTime.setProgress(0);
        countDownTimer= new CountDownTimer(4000,40) {
            @Override
            public void onTick(long l) {
                int i= progressBarTime.getProgress();
                progressBarTime.setProgress(i+1);
                if(i>40){
                    if(i%5==0)
                        buttonAns.setBackgroundResource(R.drawable.background_button);
                    else
                        buttonAns.setBackgroundResource(R.drawable.background_new);
                }
                if(i== 40) {
                    end++;
                    if (guess.check()) {
                        tru++;
                        Toast.makeText(GameGWActivity.this, "Đúng !", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(GameGWActivity.this, "Sai !", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFinish() {
                buttonAns.setBackgroundResource(R.drawable.background_new);
                groupBotton.clearSelect();
                countDownTimer.cancel();
                play();
            }
        }.start();
    }
    private void create()
    {
        progressBarLoad=  findViewById(R.id.progressBar);
        progressBarTime=  findViewById(R.id.progressBarTime);
        buttonStart= findViewById(R.id.buttonStart);
        constraintLayoutLoad= findViewById(R.id.layoutLoad);
        buttonQues= findViewById(R.id.buttonQues);
        buttonA=findViewById(R.id.buttonA);
        buttonB=findViewById(R.id.buttonB);
        buttonC=findViewById(R.id.buttonC);
        buttonD=findViewById(R.id.buttonD);
        buttonSc= findViewById(R.id.buttonScore);
        buttonFolder= findViewById(R.id.buttonFolder);
        buttonPause = findViewById(R.id.buttonPause);
        //toolbarGame=findViewById(R.id.toolbarGame);

        progressBarTime.setMax(99);
        progressBarTime.setProgress(1);
    }
    private void setRadioGroup(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.id.buttonA);
        arrayList.add(R.id.buttonB);
        arrayList.add(R.id.buttonC);
        arrayList.add(R.id.buttonD);
        groupBotton = new GroupBotton(GameGWActivity.this,arrayList) {
            @Override
            public void setOnClick(@NotNull Button button) {
                countDownTimer.cancel();
                int i = button.getId();
                switch (i){
                    case R.id.buttonA: {
                        guess.setAns(0);
                        break;
                    }
                    case R.id.buttonB: {
                        guess.setAns(1);
                        break;
                    }
                    case R.id.buttonC: {
                        guess.setAns(2);
                        break;
                    }
                    case R.id.buttonD: {
                        guess.setAns(3);
                        break;
                    }
                }
                checkAns();
                clearOnClick();
            }
        };

    }
    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<Void,String,String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            createDB();
            Intent intent = getIntent();
            int idF= intent.getIntExtra("idfolder",0);
            FolderDB folderDB = new FolderDB(GameGWActivity.this);
            MyFolder myFolder = folderDB.getFolder(idF);
            //toolbarGame.setTitle("Trắc nhiệm nhanh ( "+ myFolder.getName()+" )");
            buttonFolder.setText("\tTrắc nghiệm hoàn hảo :"+ myFolder.getName());
            WordProDB wordDB= new WordProDB(GameGWActivity.this);
            arrayList= new MyWordPro("").getListMyWordFromListPro(wordDB.getListFromFolder(myFolder));
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
            buttonStart.startAnimation(AnimationUtils.loadAnimation(GameGWActivity.this,R.anim.ssss));
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
