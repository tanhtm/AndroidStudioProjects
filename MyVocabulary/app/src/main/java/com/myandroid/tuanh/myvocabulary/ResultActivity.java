package com.myandroid.tuanh.myvocabulary;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myandroid.tuanh.myvocabulary.applib.HandlingWeb;
import com.myandroid.tuanh.myvocabulary.applib.MyFolder;
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro;
import com.myandroid.tuanh.myvocabulary.applib.VecterTypeWord;
import com.myandroid.tuanh.myvocabulary.database.FolderDB;
import com.myandroid.tuanh.myvocabulary.database.HistoryDB;
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController;
import com.myandroid.tuanh.myvocabulary.database.WordProDB;

import java.io.IOException;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    int id;
    HistoryDB historyDB;
    MyWordPro myWordPro;
    TextView textViewWord;
    WebView webView;
    WordProDB wordDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        createDB();
        Intent intent = getIntent();
        intent.getExtras();
        id= intent.getIntExtra("id",0);
        wordDB = new WordProDB(ResultActivity.this);
        historyDB = new HistoryDB(ResultActivity.this);
        myWordPro= historyDB.getHistory(id);
        String note = myWordPro.getNote();
        myWordPro= new HandlingWeb(myWordPro.getWord(),myWordPro.getWebViewFull()).getMyWord();
        webView= findViewById(R.id.webViewAll);
        textViewWord= findViewById(R.id.textViewWord);
        textViewWord.setText(myWordPro.getWord() + " ["+ myWordPro.getPronunciation()+"]");
        String dateWeb = myWordPro.showWeb();
        webView.loadDataWithBaseURL(null,dateWeb, "text/html", "UTF-8", null);
        if(note.equals("A-V")) {
            textViewWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wordDB.isExisted(myWordPro))
                        Toast.makeText(ResultActivity.this, "Không thêm được vì từ " + myWordPro.getWord() + " đã tồn tại trong thư viện !", Toast.LENGTH_LONG).show();
                    else
                        dialogAddLib();
                }
            });
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
    void dialogAddLib() {
        wordDB = new WordProDB(ResultActivity.this);
        final Dialog dialog = new Dialog(ResultActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_editword);
        dialog.show();
        final EditText edtWord = dialog.findViewById(R.id.editTextWord);
        edtWord.setText(myWordPro.getWord());
        final EditText edtPron = dialog.findViewById(R.id.editTextPron);
        String strPron = myWordPro.getPronunciation();
        edtPron.setText(strPron);
        final EditText edtHint = dialog.findViewById(R.id.editTextHint);
        Button btnSave = dialog.findViewById(R.id.buttonSave);
        Button btnDel = dialog.findViewById(R.id.buttonDel);
        btnDel.setVisibility(View.GONE);
        Button btnQuit = dialog.findViewById(R.id.buttonQuit);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //// spinner chọn nghĩa chính
        final ArrayList<VecterTypeWord> arrayList = myWordPro.getListType();
        Spinner spinnerM= dialog.findViewById(R.id.spinnerMean);
        ArrayAdapter arrayAdapterM = new ArrayAdapter(ResultActivity.this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapterM.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerM.setAdapter(arrayAdapterM);
        spinnerM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myWordPro.setVectorTypeWord(arrayList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                myWordPro.setVectorTypeWord(arrayList.get(0));
            }
        });
        //// sprinner chọn thư mục
        final Spinner spinerF = dialog.findViewById(R.id.spinnerFolder);
        FolderDB folder = new FolderDB(ResultActivity.this);
        final ArrayList<MyFolder> listFolder = folder.getList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(ResultActivity.this, android.R.layout.simple_spinner_item, listFolder);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinerF.setAdapter(arrayAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWordPro.setId(wordDB.newId());
                myWordPro.setIdFolder(listFolder.get(spinerF.getSelectedItemPosition()).getId());
                myWordPro.setNote(edtHint.getText().toString());
                myWordPro.setWord(edtWord.getText().toString());
                if (myWordPro.getWord().equals("") || myWordPro.getListType().size() == 0)
                    Toast.makeText(ResultActivity.this, "Không được bỏ trống phần từ và nghĩa !", Toast.LENGTH_SHORT).show();
                else {
                    if (!wordDB.isExisted(myWordPro)) {
                        if (wordDB.insertWord(myWordPro)) {
                            dialog.dismiss();
                            Toast.makeText(ResultActivity.this, "Thêm từ " + myWordPro.getWord() + " thành công !", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ResultActivity.this, "Lỗi database !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResultActivity.this, "Từ " + myWordPro.getWord() + " đã tồn tại !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
