package com.myandroid.tuanh.myvocabulary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.myandroid.tuanh.myvocabulary.adapter.AdapterHistory;
import com.myandroid.tuanh.myvocabulary.applib.HandlingWeb;
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.myandroid.tuanh.myvocabulary.database.HistoryDB;
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController;
import com.myandroid.tuanh.myvocabulary.database.WordProDB;

public class TranslateActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton buttonSwap;
    TextView buttonLang1, buttonLang2;
    TextView textViewAdd;
    String web = "http://tratu.coviet.vn/hoc-tieng-anh/tu-dien/lac-viet/";
    String av = "A-V";
    MyWordPro myWordPro = new MyWordPro("");
    WordProDB wordDB;
    ProgressBar progressBar;
    String result = "";
    ListView listViewHistory;
    ArrayList<MyWordPro> list;
    HistoryDB historyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        createDB();
        setToolbar();
        createHistory();
        setListViewHistory();
        textViewAdd = findViewById(R.id.textViewAdd);
        swapLang();
        translate();

    }
    void setToolbar()
    {
        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Từ điển online (tratu.coviet.vn)");
        toolbar.inflateMenu(R.menu.menu_translate);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.quit:
                        finish();
                        return true;
                    case R.id.library:
                        startActivity(new Intent(TranslateActivity.this,FolderActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void swapLang() {
        buttonLang1 = findViewById(R.id.buttonLang1);
        buttonLang2 = findViewById(R.id.buttonLang2);
        buttonSwap = findViewById(R.id.buttonSwap);
        buttonLang1.setText("Tiếng Anh");
        buttonLang2.setText("Tiếng Việt");
        buttonSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = buttonLang1.getText().toString();
                buttonLang1.setText(buttonLang2.getText());
                buttonLang2.setText(lang);
                if (!lang.equals("Tiếng Anh"))
                    av = "A-V";
                else
                    av = "V-A";
            }
        });
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

    void translate() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                    progressBar.setVisibility(View.VISIBLE);
                    String url = s;
                    try {
                        url = URLEncoder.encode(url, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    url = web + av +"/" + url + ".html";
                    myWordPro.setWord(s);
                    myWordPro.setNote(av);
                    new ReadWeb().execute(url);
                    return  false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    list= historyDB.getListSearch(s);
                    setListViewHistory();
                return false;
            }
        });
        progressBar = findViewById(R.id.progressBarLoad);
        progressBar.setVisibility(View.GONE);
    }
/*
    private class ReadWeb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                result = new String();
                while ((result = bufferedReader.readLine()) != null) {
                    if (result.contains("class=\"kq\"")) {
                        break;
                    }
                    if (result.contains("<div class=\"m\"><span> Xem </span>")) {
                        result = result.substring(result.indexOf(".html\">") + 7, result.indexOf("</a> </div></div>"));
                        return "Xem " + "\"" + result + "\"";
                    }
                }
                if (result == null||result.contains("class=\"i p10\">Dữ liệu đang được cập nhật")) {
                    return null;
                } else {
                    if (av.equals("A-V/")) {
                        Log.d("tran", stringBuilder.toString());
                        myWordPro = myWordPro.getMyWord(editTextWord.getText().toString(), result);
                    }
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (s == null) {
                Toast.makeText(TranslateActivity.this, "Không tìm được", Toast.LENGTH_SHORT).show();
                webView.setVisibility(View.GONE);
                textViewAdd.setVisibility(View.GONE);
            }
            else {
                if (s.contains("Xem")) {
                    webView.setVisibility(View.VISIBLE);
                    webView.loadDataWithBaseURL(null, "<div style=\"color: blue;\"><big><b>" + s + "</div>", "text/html", "UTF-8", null);
                    textViewAdd.setVisibility(View.GONE);
                }else {
                        myWordPro.setWebViewFull(s);
                        HandlingWeb handlingWeb = new HandlingWeb();
                        s = handlingWeb.run(s);
                        Log.d("WEB", s);
                        webView.setVisibility(View.VISIBLE);
                        webView.loadDataWithBaseURL(null, s, "text/html", "UTF-8", null);
                        if (av.equals("A-V/")) {
                            textViewAdd.setVisibility(View.VISIBLE);
                            textViewAdd.setText( myWordPro.getWord() + " ["+myWordPro.getPronunciation()+"]");
                        } else
                            textViewAdd.setVisibility(View.GONE);
                }
            }
        }
    }*/
@SuppressLint("StaticFieldLeak")
private class ReadWeb extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(strings[0]);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            result = new String();
            while ((result = bufferedReader.readLine()) != null) {
                if (result.contains("class=\"kq\"")) {
                    break;
                }
            }
            if (result.contains("<div class=\"m\"><span> Xem </span>")) {
                result = result.substring(result.indexOf(".html\">") + 7, result.indexOf("</a> </div></div>"));
                myWordPro.setWord(result);
                return "Xem " + "\"" + result + "\"";
            }
            if (result == null||result.contains("class=\"i p10\">Dữ liệu đang được cập nhật")) {
                return null;
            } else {
                if (av.equals("A-V/")) {
                    Log.d("tran", stringBuilder.toString());
                }
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.GONE);
        if (s == null) {
            Toast.makeText(TranslateActivity.this, "Không tìm được", Toast.LENGTH_SHORT).show();
        }
        else {
            if (s.contains("Xem")) {
                textViewAdd.setText(s);
                textViewAdd.setVisibility(View.VISIBLE);
            }else {
                String note= myWordPro.getNote();
                myWordPro= new HandlingWeb(myWordPro.getWord(),s).getMyWord();
                myWordPro.setNote(note);
                createDB();
                HistoryDB historyDB = new HistoryDB(TranslateActivity.this);
                myWordPro.setId(historyDB.newId());
                if(!historyDB.isExisted(myWordPro)) {
                    if (historyDB.insertHistory(myWordPro)){
                        Toast.makeText(TranslateActivity.this, "Đã lưu lịch sử", Toast.LENGTH_SHORT).show();
                        list.add(0,myWordPro);
                        Intent intent = new Intent(TranslateActivity.this,ResultActivity.class);
                        intent.putExtra("id",myWordPro.getId());
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(TranslateActivity.this, "Lỗi db History", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
    void createHistory(){
        listViewHistory = findViewById(R.id.listViewHistory);
        createDB();
        historyDB = new HistoryDB(TranslateActivity.this);
        list= historyDB.getList();
    }
    void setListViewHistory(){
        AdapterHistory adapterHistory = new AdapterHistory(TranslateActivity.this,list);
        listViewHistory.setAdapter(adapterHistory);
    }
}
