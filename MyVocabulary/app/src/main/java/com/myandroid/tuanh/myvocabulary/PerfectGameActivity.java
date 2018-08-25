package com.myandroid.tuanh.myvocabulary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.myandroid.tuanh.myvocabulary.applib.MyFolder;
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro;
import com.myandroid.tuanh.myvocabulary.database.FolderDB;
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController;
import com.myandroid.tuanh.myvocabulary.database.WordProDB;
import com.myandroid.tuanh.myvocabulary.gameofvoca.GroupBotton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class PerfectGameActivity extends AppCompatActivity {

    private ArrayList<MyWordPro> arrayList= new ArrayList<>();
    private Button buttonStart;
    private ProgressBar progressBarLoad;
    private ConstraintLayout constraintLayoutLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_game);

        buttonStart= findViewById(R.id.buttonStart);
        new LoadData().execute();
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayoutLoad= findViewById(R.id.layoutLoad);
                constraintLayoutLoad.setVisibility(View.GONE);
                //play();
            }
        });
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.id.button);
        arrayList.add(R.id.button1);
        arrayList.add(R.id.button2);
        arrayList.add(R.id.button3);
        GroupBotton groupBotton = new GroupBotton(PerfectGameActivity.this,arrayList) {
            @Override
            public void setOnClick(@NotNull Button button) {
                button.setText("OOOO");
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
            FolderDB folderDB = new FolderDB(PerfectGameActivity.this);
            MyFolder myFolder = folderDB.getFolder(idF);
            //toolbarGame.setTitle("Trắc nhiệm nhanh ( "+ myFolder.getName()+" )");
            WordProDB wordDB= new WordProDB(PerfectGameActivity.this);
            arrayList= wordDB.getListFromFolder(myFolder);
            publishProgress("Load... done !");
            return "Done !";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(PerfectGameActivity.this,values[0],Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBarLoad=  findViewById(R.id.progressBar);
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
