package com.myandroid.tuanh.myvocabulary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myandroid.tuanh.myvocabulary.adapter.AdapterNote;
import com.myandroid.tuanh.myvocabulary.adapter.AdapterWord;
import com.myandroid.tuanh.myvocabulary.applib.MyFolder;
import com.myandroid.tuanh.myvocabulary.applib.MyNote;
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro;
import com.myandroid.tuanh.myvocabulary.database.FolderDB;
import com.myandroid.tuanh.myvocabulary.database.NoteDB;
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController;
import com.myandroid.tuanh.myvocabulary.database.WordProDB;

import java.io.IOException;
import java.util.ArrayList;

public class LibActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton floatingActionButtonAdd;
    boolean showVoca= true; // showVoca : kiem tra cua sao nao dang mo
    ListView listViewLib;
    ArrayList<MyWordPro> myWordArrayList= new ArrayList<>();
    ArrayList<MyNote> myNoteArrayList= new ArrayList<>();
    MyFolder myFolder;
    WordProDB wordDB;
    NoteDB noteDB;
    ProgressBar progressBarLoad;
    MyWordPro myWord;
    TextView textViewFolder;
    int idFolder;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_vocabulary:
                    toolbar.setTitle("Từ vựng cá nhân");
                    showVoca= true;
                    setListViewVoca();
                    return true;
                case R.id.navigation_note:
                    toolbar.setTitle("Sổ tay ghi chú");
                    showVoca= false;
                    setListViewNote();
                    return true;
            }
            return false;
        }

    };
    private void create()
    {
        toolbar= findViewById(R.id.toolbar);
        floatingActionButtonAdd=findViewById(R.id.floatingActionButtonAdd);
        listViewLib=findViewById(R.id.listViewLib);
        progressBarLoad= findViewById(R.id.progressBarLoad);
        textViewFolder= findViewById(R.id.textViewFolder);
        //////////////////////////////////////////////////
    }
    private void setListViewVoca(){
        if(myWordArrayList.size()!=0) {
            AdapterWord adapterWord = new AdapterWord(LibActivity.this, myWordArrayList);
            listViewLib.setAdapter(adapterWord);
        }
        else {
            Toast.makeText(LibActivity.this,"Không có từ vựng nào cả !",Toast.LENGTH_SHORT).show();
        }
    }
    private void setListViewNote(){
        if(myNoteArrayList.size()!=0) {
            AdapterNote adapterWord = new AdapterNote(LibActivity.this, myNoteArrayList);
            listViewLib.setAdapter(adapterWord);
        }
        else {
            Toast.makeText(LibActivity.this,"Không có ghi chú nào cả !",Toast.LENGTH_SHORT).show();
        }
    }
    private void setToolbar(){
        toolbar.setTitle("Từ vựng cá nhân");
        toolbar.inflateMenu(R.menu.menu_start);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.quit:
                        finish();
                        return true;
                    case R.id.menu_search:
                        return true;
                    case R.id.game:
                        startActivity(new Intent(LibActivity.this,GameActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList arrayList;
                if(!s.equals("")) {
                    if (showVoca)
                        arrayList= wordDB.getListSearch(s,idFolder);
                    else
                        arrayList= noteDB.getListSearch(s,idFolder);
                }else {
                    if (showVoca)
                        arrayList = myWordArrayList;
                    else
                        arrayList = myNoteArrayList;
                }
                if(arrayList.size()!=0) {
                    BaseAdapter adapter;
                    if (showVoca)
                        adapter = new AdapterWord(LibActivity.this, arrayList);
                    else
                        adapter = new AdapterNote(LibActivity.this, arrayList);
                    listViewLib.setAdapter(adapter);
                }else {
                    Toast.makeText(LibActivity.this,"Không tìm thấy gì với từ khoá \""+s+"\" !",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void DialogAddNewNote() {
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_add_note);
        ///
        final EditText editText=dialog.findViewById(R.id.editTextNote);
        Button buttonAddNote= dialog.findViewById(R.id.buttonAddNote);
        Button buttonQuitNote= dialog.findViewById(R.id.buttonQuitNote);
        Button buttonFolder= dialog.findViewById(R.id.buttonFolder);
        buttonFolder.setText(myFolder.getName());
        ///
        dialog.show();
        buttonQuitNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();
                if(str.equals(""))
                    Toast.makeText(LibActivity.this,"Note null",Toast.LENGTH_LONG).show();
                else {
                    MyNote myNote= new MyNote(str);
                    myNote.setId(noteDB.newId());
                    myNote.setIdFolder(myFolder.getId());
                    if(!noteDB.insertNote(myNote))
                        Toast.makeText(LibActivity.this,"Lỗi NoteDatabase",Toast.LENGTH_LONG).show();
                    else {
                        myNoteArrayList.add(0,myNote);
                        Toast.makeText(LibActivity.this, "Thêm note thành công!", Toast.LENGTH_LONG).show();
                        setListViewNote();
                        dialog.dismiss();
                    }
                }
            }
        });
    }
    private void DialogAddNewWord(){
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_editword);
        dialog.show();
        final MyWordPro myWordPro= new MyWordPro("");
        final EditText edtWord = dialog.findViewById(R.id.editTextWord);
        final EditText edtHint= dialog.findViewById(R.id.editTextHint);
        Button btnSave= dialog.findViewById(R.id.buttonSave);
        Button btnDel= dialog.findViewById(R.id.buttonDel);
        btnDel.setVisibility(View.GONE);
        Button btnQuit= dialog.findViewById(R.id.buttonQuit);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final Spinner spinerF = dialog.findViewById(R.id.spinnerFolder);
        FolderDB folder = new FolderDB(LibActivity.this);
        final ArrayList<MyFolder> listFolder = folder.getList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(LibActivity.this, android.R.layout.simple_spinner_item, listFolder);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinerF.setAdapter(arrayAdapter);
        int pos =0;
        for(MyFolder i : listFolder){
            if(i.getName().equals(textViewFolder.getText()))
                break ;
            pos++;
        }
        spinerF.setSelection(pos);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWordPro.setId(wordDB.newId());
                myWordPro.setIdFolder(listFolder.get(spinerF.getSelectedItemPosition()).getId());
                myWordPro.setNote(edtHint.getText().toString());
                myWordPro.setWord(edtWord.getText().toString());
                if (myWordPro.getWord().equals(""))
                    Toast.makeText(LibActivity.this, "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
                else {
                    if (!wordDB.isExisted(myWordPro)) {
                        if (wordDB.insertWord(myWordPro)) {
                            dialog.dismiss();
                            if(myWordPro.getIdFolder()==idFolder||idFolder==0) {
                                myWordArrayList.add(0,myWordPro);
                                setListViewVoca();
                            }
                            Toast.makeText(LibActivity.this, "Thêm từ " + myWordPro.getWord() + " thành công !", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LibActivity.this, "Lỗi database !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LibActivity.this, "Từ " + myWordPro.getWord() + " đã tồn tại !", Toast.LENGTH_SHORT).show();
                    }
                }
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
    private  void  getDataFromFolder(){
        myWordArrayList= wordDB.getListFromFolder(myFolder);
        myNoteArrayList= noteDB.getListFromFolder(myFolder);
    }
    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<Void,String,String>
    {
        @Override
        protected String doInBackground(Void... voids) {

            publishProgress("Loading...done!");
            wordDB= new WordProDB(LibActivity.this);
            noteDB= new NoteDB(LibActivity.this);
            createDB();
            Intent intent = getIntent();
            myFolder = new MyFolder(intent.getStringExtra("namefolder"));
            idFolder = intent.getIntExtra("idfolder",0);
            myFolder.setId(idFolder);
            textViewFolder.setText(myFolder.getName());
            getDataFromFolder();
            return "Done !";
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(LibActivity.this,values[0],Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBarLoad.setVisibility(View.GONE);
            Toast.makeText(LibActivity.this,myWordArrayList.size()+" từ vựng",Toast.LENGTH_SHORT).show();
            Toast.makeText(LibActivity.this,myNoteArrayList.size()+" ghi chú",Toast.LENGTH_SHORT).show();
            setListViewVoca();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);
        create();
        new LoadData().execute();
        setToolbar();
        onCreateOptionsMenu(toolbar.getMenu());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showVoca)
                    DialogAddNewNote();
                else
                    DialogAddNewWord();
            }
        });
    }

}
