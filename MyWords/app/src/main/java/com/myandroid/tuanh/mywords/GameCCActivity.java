package com.myandroid.tuanh.mywords;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import applib.MyWord;
import gameofvoca.CorrectCharacters;
import database.SQLiteDataController;
import database.WordDB;

public class GameCCActivity extends AppCompatActivity {

    private ArrayList<MyWord> arrayList= new ArrayList<>();
    private ArrayList<Integer> arrayListId= new ArrayList<>();
    private ArrayList<Integer> arrayListIdChoose= new ArrayList<>();
    private ProgressBar progressBarLoad;
    private Button buttonStart;
    private ConstraintLayout constraintLayoutLoad;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_cc);
        create();
        toolbar.inflateMenu(R.menu.menu_game);
        final AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setTitle(" Xác nhận thoát trò chơi");
        alBuilder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        alBuilder.setMessage("Bạn muốn thoát khỏi trò chơi mà không có điểm ?");
        alBuilder.setPositiveButton("Đúng rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(GameCCActivity.this, LibActivity.class);
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.quit:
                        finish();
                        return true;
                    case R.id.library: {
                        alBuilder.show();
                        return true;
                    }
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
        CorrectCharacters correctCharacters = new CorrectCharacters(GameCCActivity.this,arrayListId,arrayListIdChoose,arrayList);
        correctCharacters.createButton();
        int point= correctCharacters.createButtonChoose();
    }
    private void create()
    {
        progressBarLoad= (ProgressBar) findViewById(R.id.progressBar);
        buttonStart= (Button)findViewById(R.id.buttonStart);
        constraintLayoutLoad= (ConstraintLayout)findViewById(R.id.layoutLoad);
        toolbar= (Toolbar)findViewById(R.id.toolbar);
        arrayListId.add(R.id.buttonW1);
        arrayListId.add(R.id.buttonW2);
        arrayListId.add(R.id.buttonW3);
        arrayListId.add(R.id.buttonW4);
        arrayListId.add(R.id.buttonW5);
        arrayListId.add(R.id.buttonW6);
        arrayListId.add(R.id.buttonW7);
        arrayListId.add(R.id.buttonW8);
        arrayListId.add(R.id.buttonW9);
        arrayListId.add(R.id.buttonW10);
        arrayListId.add(R.id.buttonW11);
        arrayListId.add(R.id.buttonW12);
        arrayListId.add(R.id.buttonW13);
        arrayListId.add(R.id.buttonW14);
        arrayListId.add(R.id.buttonW15);
        arrayListId.add(R.id.buttonW16);
        arrayListId.add(R.id.buttonW17);
        arrayListId.add(R.id.buttonW18);
        arrayListId.add(R.id.buttonW19);
        arrayListId.add(R.id.buttonW20);
        arrayListIdChoose.add(R.id.buttonC1);
        arrayListIdChoose.add(R.id.buttonC2);
        arrayListIdChoose.add(R.id.buttonC3);
        arrayListIdChoose.add(R.id.buttonC4);
        arrayListIdChoose.add(R.id.buttonC5);
        arrayListIdChoose.add(R.id.buttonC6);
        arrayListIdChoose.add(R.id.buttonC7);
        arrayListIdChoose.add(R.id.buttonC8);
        arrayListIdChoose.add(R.id.buttonC9);
        arrayListIdChoose.add(R.id.buttonC10);
        arrayListIdChoose.add(R.id.buttonC11);
        arrayListIdChoose.add(R.id.buttonC12);
        arrayListIdChoose.add(R.id.buttonC13);
        arrayListIdChoose.add(R.id.buttonC14);
        arrayListIdChoose.add(R.id.buttonC15);
        arrayListIdChoose.add(R.id.buttonC16);
        arrayListIdChoose.add(R.id.buttonC17);
        arrayListIdChoose.add(R.id.buttonC18);
        arrayListIdChoose.add(R.id.buttonC19);
        arrayListIdChoose.add(R.id.buttonC20);
        arrayListIdChoose.add(R.id.buttonC21);
        arrayListIdChoose.add(R.id.buttonC22);
        arrayListIdChoose.add(R.id.buttonC23);
        arrayListIdChoose.add(R.id.buttonC24);
        arrayListIdChoose.add(R.id.buttonC25);
        arrayListIdChoose.add(R.id.buttonC26);
    }
    private class LoadData extends AsyncTask<Void,String,String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            createDB();
            WordDB wordDB= new WordDB(GameCCActivity.this);
            arrayList= wordDB.getList();
            publishProgress("Load... done !");
            return "Done !";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(GameCCActivity.this,values[0],Toast.LENGTH_SHORT).show();
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
