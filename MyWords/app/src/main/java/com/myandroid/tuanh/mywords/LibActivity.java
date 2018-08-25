package com.myandroid.tuanh.mywords;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import adapter.AdapterNote;
import adapter.AdapterWord;
import applib.MyFolder;
import applib.MyNote;
import applib.MyWord;
import database.FolderDB;
import database.NoteDB;
import database.SQLiteDataController;
import database.WordDB;
import libmanagement.NoteManagement;
import libmanagement.WordManagement;

public class LibActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton floatingActionButtonSearch,floatingActionButtonAdd,floatingActionButtonDelSearch;
    EditText editTextSearch;
    boolean showSearch= false, showVoca= true; // showVoca : kiem tra cua sao nao dang mo
    ListView listViewLib;
    ArrayList<MyWord> myWordArrayList= new ArrayList<>();
    ArrayList<MyNote> myNoteArrayList= new ArrayList<>();
    ArrayList<MyFolder> myFolderArrayList= new ArrayList<>();
    WordDB wordDB;
    NoteDB noteDB;
    FolderDB folderDB;
    WordManagement wordManagement= new WordManagement();
    NoteManagement noteManagement= new NoteManagement();
    LinearLayout linearLayoutSearch,linearLayoutFolder;
    ProgressBar progressBarLoad;
    MyWord myWord;
    Button buttonFolder;
    Spinner spinnerFolder;
    int idFol=0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_vocabulary:
                    toolbar.setTitle("Từ vựng cá nhân");
                    showVoca= true;
                    linearLayoutFolder.setVisibility(View.VISIBLE);
                    setListViewVoca();
                    return true;
                case R.id.navigation_note:
                    toolbar.setTitle("Sổ tay ghi chú");
                    showVoca= false;
                    linearLayoutFolder.setVisibility(View.GONE);
                    setListViewNote();
                    return true;
            }
            return false;
        }

    };
    private void create()
    {
        toolbar= (Toolbar)findViewById(R.id.my_toolbar);
        floatingActionButtonSearch= (FloatingActionButton)findViewById(R.id.floatingActionButtonSearch);
        floatingActionButtonAdd= (FloatingActionButton)findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonDelSearch= (FloatingActionButton)findViewById(R.id.floatingActionButtonDel);
        editTextSearch= (EditText)findViewById(R.id.editTextSearch);
        listViewLib= (ListView) findViewById(R.id.listViewLib);
        linearLayoutSearch= (LinearLayout)findViewById(R.id.layoutSearch);
        progressBarLoad= (ProgressBar) findViewById(R.id.progressBarLoad);
        buttonFolder= (Button)findViewById(R.id.buttonFolder);
        spinnerFolder= (Spinner)findViewById(R.id.spinnerFolder);
        linearLayoutFolder= (LinearLayout)findViewById(R.id.layoutFolder);
        //////////////////////////////////////////////////
    }
    private void setListViewVoca(){
        AdapterWord adapterWord = new AdapterWord(LibActivity.this, myWordArrayList);
        listViewLib.setAdapter(adapterWord);
    }
    private void setListViewNote(){
        AdapterNote adapterWord = new AdapterNote(LibActivity.this, myNoteArrayList);
        listViewLib.setAdapter(adapterWord);
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
                        linearLayoutSearch.setVisibility(View.VISIBLE);
                        floatingActionButtonAdd.hide();
                        showSearch= true;
                        return true;
                    case R.id.game:
                        startActivity(new Intent(LibActivity.this,GameActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
    private void DialogAddNewNote() {
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_add_note);
        ///
        final EditText editText= (EditText) dialog.findViewById(R.id.editTextNote);
        Button buttonAddNote= (Button) dialog.findViewById(R.id.buttonAddNote);
        Button buttonQuitNote= (Button) dialog.findViewById(R.id.buttonQuitNote);
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
                    if(!noteDB.insertNote(myNote))
                        Toast.makeText(LibActivity.this,"Lỗi db note",Toast.LENGTH_LONG).show();
                    else {
                        noteManagement.addNote(myNote);
                        setListViewNote();
                        Toast.makeText(LibActivity.this, "Thêm note thành công!", Toast.LENGTH_LONG).show();
                        dialog.hide();
                    }
                }
            }
        });
    }

    private void DialogAddNewWord()
    {
        final Dialog dialog1= new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_add_word);
        ///
        final Spinner spinner= (Spinner) dialog1.findViewById(R.id.spinnerType);
        final Spinner spinnerFol= (Spinner) dialog1.findViewById(R.id.spinnerFolder);
        final EditText editTextWord,editTextMean,editTextHint;
        editTextWord=(EditText)dialog1.findViewById(R.id.editTextWord);
        editTextMean=(EditText)dialog1.findViewById(R.id.editTextMeaning);
        editTextHint=(EditText)dialog1.findViewById(R.id.editTextHint);
        Button buttonAdd= dialog1.findViewById(R.id.buttonAdd);
        Button buttonQuit= dialog1.findViewById(R.id.buttonQuit);
        ////
        final ArrayList<String> list= new ArrayList<>();
        list.add("noun");
        list.add("verb");
        list.add("adj");
        list.add("adv");
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(arrayAdapter);

        ArrayAdapter arrayAdapterFol= new ArrayAdapter(this,android.R.layout.simple_spinner_item,myFolderArrayList);
        arrayAdapterFol.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerFol.setAdapter(arrayAdapterFol);
        ///
        dialog1.show();
        myWord = new MyWord();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myWord.setTypeOfWord(list.get(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                myWord.setTypeOfWord(list.get(0));
            }
        });
        spinnerFol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myWord.setIdFolder(myFolderArrayList.get(i).getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                myWord.setIdFolder(0);
            }
        });
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.hide();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWord.setWord(editTextWord.getText().toString());
                myWord.setMeaning(editTextMean.getText().toString());
                myWord.setNote(editTextHint.getText().toString());
                if(addWord(myWord)) {
                    setListViewVoca();
                    Toast.makeText(LibActivity.this, "Thêm từ mới thành công !", Toast.LENGTH_LONG).show();
                    dialog1.hide();
                }
                else
                    Toast.makeText(LibActivity.this,myWord.getWord()+ " đã tồn tại!",Toast.LENGTH_LONG).show();

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
    private boolean addWord(MyWord myWord){
        if(wordManagement.existed(myWord))
            return false;
        else{
            myWord.setId(wordDB.newId());
            wordManagement.addWord(myWord); // cập nhật list View
            if(!wordDB.insertWord(myWord)){
                Toast.makeText(this,"Lỗi db",Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    }
    private void search()
    {
        if(showVoca)
        {
            AdapterWord adapterWord = new AdapterWord(LibActivity.this, wordManagement.search(editTextSearch.getText().toString()));
            listViewLib.setAdapter(adapterWord);
        }else {
            AdapterNote adapterNote= new AdapterNote(LibActivity.this,noteManagement.search(editTextSearch.getText().toString()));
            listViewLib.setAdapter(adapterNote);
        }
    }
    private class LoadData extends AsyncTask<Void,String,String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            wordDB= new WordDB(LibActivity.this);
            noteDB= new NoteDB(LibActivity.this);
            folderDB= new FolderDB(LibActivity.this);
            createDB();
            myWordArrayList= wordDB.getList();
            publishProgress("vocabulary ... done !");
            myNoteArrayList= noteDB.getList();
            publishProgress("notebook ... done !");
            myFolderArrayList= folderDB.getList();
            publishProgress("folder ... done !");
            wordManagement.addListWord(myWordArrayList);
            noteManagement.addListNote(myNoteArrayList);
            myFolderArrayList.add(0,new MyFolder("Tất cả"));
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
            if(showVoca) {
                AdapterWord adapterWord = new AdapterWord(LibActivity.this, myWordArrayList);
                listViewLib.setAdapter(adapterWord);
            }
            else {
                AdapterNote adapterNote= new AdapterNote(LibActivity.this,myNoteArrayList);
                listViewLib.setAdapter(adapterNote);
            }
            folder();
        }
    }
    private void folder()
    {
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item,myFolderArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerFolder.setAdapter(arrayAdapter);
        spinnerFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<MyWord> arrayList = getListFromFolder(myFolderArrayList.get(i));
                AdapterWord adapterWord = new AdapterWord(LibActivity.this, arrayList);
                listViewLib.setAdapter(adapterWord);
                if(i!=0)
                    floatingActionButtonAdd.setVisibility(View.GONE);
                else
                    floatingActionButtonAdd.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AdapterWord adapterWord = new AdapterWord(LibActivity.this, myWordArrayList);
                listViewLib.setAdapter(adapterWord);
                idFol= 0;
                floatingActionButtonAdd.setVisibility(View.VISIBLE);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);
        create();
        new LoadData().execute();
        setListViewVoca();
        setToolbar();
        folder();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        floatingActionButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        floatingActionButtonDelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutSearch.setVisibility(View.GONE);
                floatingActionButtonAdd.show();
                if(showVoca)
                    setListViewVoca();
                else
                    setListViewNote();
                editTextSearch.setText("");
                showSearch = false;
            }
        });
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showVoca)
                    DialogAddNewWord();
                else
                    DialogAddNewNote();
            }
        });
    }
    private ArrayList<MyWord> getListFromFolder(MyFolder myFolder)
    {
        myWordArrayList= wordManagement.getList();
        if(myFolder.getId()==0)
            return myWordArrayList;// nếu là thư mục all thì lấy tất

        ArrayList<MyWord> arrayList = new ArrayList<>();
        for(int i=0; i<myWordArrayList.size();i++){
            if(myWordArrayList.get(i).getIdFolder()==myFolder.getId())
                arrayList.add(myWordArrayList.get(i));
        }
        return  arrayList;
    }
}
