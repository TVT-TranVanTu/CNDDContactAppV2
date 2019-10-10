package com.example.crudcontactapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.crudcontactapp.adapter.CustomAdapter;
import com.example.crudcontactapp.data.DBManager;
import com.example.crudcontactapp.model.Students;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText editAddress;
    private EditText edtPhoneNumber;
    private EditText edtEmail;
    private EditText edtId;
    private Button btnSave;
    private Button btnList;
    private Button btnUpdate;
    private ListView lvStudent;
    private DBManager dbManager;
    private CustomAdapter customAdapter;
    private List<Students> studentList;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this);
        initWidget();
        studentList = dbManager.getAllStudent();
        setAdapter();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,nhap.class);
                startActivity(intent);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Students student = createStudent();
                        if (student != null) {
                            dbManager.addStudent(student);
                        }
                        updateListStudent();
                        setAdapter();
                    }
                });
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Students student = createStudent();
                if (student != null) {
                    dbManager.addStudent(student);
                }
                updateListStudent();
                setAdapter();
            }
        });
        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Students student = studentList.get(position);
                edtId.setText(String.valueOf(student.getmID()));
                edtName.setText(student.getmName());
                editAddress.setText(student.getmAddress());
                edtEmail.setText(student.getmEmail());
                edtPhoneNumber.setText(student.getmPhoneNumber());
                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Students student = new Students();
                student.setmID(Integer.parseInt(String.valueOf(edtId.getText())));
                student.setmName(edtName.getText()+"");
                student.setmAddress(editAddress.getText()+"");
                student.setmEmail(edtEmail.getText()+"");
                student.setmPhoneNumber(edtPhoneNumber.getText()+"");
                int result = dbManager.updateStudent(student);
                if(result>0){
                    updateListStudent();
                }
                btnSave.setEnabled(true);
                btnUpdate.setEnabled(false);
            }
        });
        lvStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Students student = studentList.get(position);
                int result = dbManager.deleteStudent(student.getmID());
                if(result>0){
                    Toast.makeText(MainActivity.this, "Delete successfuly", Toast.LENGTH_SHORT).show();
                    updateListStudent();
                }else{
                    Toast.makeText(MainActivity.this, "Delete fail", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private Students createStudent() {
        String name = edtName.getText().toString();
        String address = String.valueOf(editAddress.getText());
        String phoneNumber = edtPhoneNumber.getText() + "";
        String email = edtEmail.getText().toString();

        Students student = new Students(name, address, phoneNumber, email);
        return student;
    }

    private void initWidget() {
        edtName = (EditText) findViewById(R.id.edt_name);
        editAddress = (EditText) findViewById(R.id.edt_address);
        edtPhoneNumber = (EditText) findViewById(R.id.edt_number);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        btnSave = (Button) findViewById(R.id.btn_save);
        lvStudent = (ListView) findViewById(R.id.lv_student);
        edtId = (EditText) findViewById(R.id.edt_id);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnList=(Button) findViewById(R.id.btn_list);
    }

    private void setAdapter() {
        if (customAdapter == null) {
            customAdapter = new CustomAdapter(this, R.layout.list_students, studentList);
            lvStudent.setAdapter(customAdapter);
        }else{
            customAdapter.notifyDataSetChanged();
            lvStudent.setSelection(customAdapter.getCount()-1);
        }
    }
    public void updateListStudent(){
        studentList.clear();
        studentList.addAll(dbManager.getAllStudent());
        if(customAdapter!= null){
            customAdapter.notifyDataSetChanged();
        }
    }

}