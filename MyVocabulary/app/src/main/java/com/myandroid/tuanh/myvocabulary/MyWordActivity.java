package com.myandroid.tuanh.myvocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.IOException;

import com.myandroid.tuanh.myvocabulary.applib.MyWordPro;
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController;
import com.myandroid.tuanh.myvocabulary.database.WordProDB;

public class MyWordActivity extends AppCompatActivity {

    TextView textViewWord;
    WebView webViewAll;
    MyWordPro myWordPro;
    WordProDB wordProDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word);
        textViewWord= findViewById(R.id.textViewWord);
        webViewAll= findViewById(R.id.webViewAll);
        webViewAll.setWebViewClient(new WebViewClient());
        createDB();
        Intent intent = getIntent();
        intent.getExtras();
        int id= intent.getIntExtra("id",0);
        wordProDB= new WordProDB(MyWordActivity.this);
        myWordPro= wordProDB.getWord(id);
        textViewWord.setText(myWordPro.getWord() +" [" +myWordPro.getPronunciation()+"]");
        String web="";
        if(!myWordPro.getWebViewFull().equals("")){
            web= myWordPro.showWeb();
            Log.d("WEB",web);
            webViewAll.loadDataWithBaseURL(null,web, "text/html", "UTF-8", null);
        }
        if(web.equals(""))
            finish();
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
