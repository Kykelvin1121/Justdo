package com.example.justdo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity  {
    private EditText mTitle;
    private EditText mDescription;
    private TextView mTasks;
    private TextView ndates;
    private ImageView mcalender;
    private Button nbutton;
    private int mdate , mmonth, myear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mTitle = findViewById(R.id.input_title);
        mDescription = findViewById(R.id.input_description);
        mTasks = findViewById(R.id.Tasks);
        ndates = findViewById(R.id.dates);
        mcalender = findViewById(R.id.Date_picker);
        nbutton = findViewById(R.id.button_ok);

        mcalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calender = Calendar.getInstance();
                mdate = calender.get(Calendar.DAY_OF_MONTH);
                mmonth = calender.get(Calendar.MONTH);
                myear = calender.get(Calendar.YEAR);
                DatePickerDialog date_picker = new DatePickerDialog(AddActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calender.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                        calender.set(Calendar.MONTH, month);
                        calender.set(Calendar.YEAR, year);
                        ndates.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                    }
                },mdate,myear,mmonth);
                date_picker.getDatePicker().setMinDate(System.currentTimeMillis());
                date_picker.show();
            }
        });

        nbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new task
                String mTitle2 = mTitle.getText().toString();
                String ndates2 = ndates.getText().toString();
                String mDescription2 = mDescription.getText().toString();
                if(mTitle2.isEmpty()){
                    mTitle.setError("Title required");
                    mTitle.requestFocus();
                    return;
                }
                else if(ndates2.isEmpty()){
                    ndates.setError("Date required");
                    ndates.requestFocus();
                    return;
                }
                else if(mDescription2.isEmpty()){
                    mDescription.setError("Description required");
                    mDescription.requestFocus();
                    return;
                }
                else{
                Map<String, Object> nTasks = new HashMap<>();
                nTasks.put("Title", mTitle.getText().toString());
                nTasks.put("Date", ndates.getText().toString());
                nTasks.put("Description", mDescription.getText().toString());
                Toast.makeText(AddActivity.this, "Task added successfully!", Toast.LENGTH_LONG).show();

                // Add a new document
                db.collection("nTasks")
                        .add(nTasks)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                }
            }
        });
    }
}
