package com.example.justdo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoneActivity extends AppCompatActivity {

    private List<DoneTask> mList_DoneTask;
    private DoneTaskAdapter mDoneTaskAdapter;
    private RecyclerView mdonerecyclerview;
    Button mBackButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        setContentView(R.layout.done_tasks);
        super.onCreate(savedInstanceState);

        mBackButton = findViewById(R.id.goBackButton);
        mList_DoneTask = new ArrayList<>();
        mdonerecyclerview = findViewById(R.id.donetasks_recyclerView);
        mdonerecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mDoneTaskAdapter = new DoneTaskAdapter(mList_DoneTask,DoneActivity.this);
        mdonerecyclerview.setAdapter(mDoneTaskAdapter);

        db.collection("DoneTasks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mList_DoneTask.clear();

                for (int i=0; i<queryDocumentSnapshots.size();i++){
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);

                    if(documentSnapshot.exists()) {

                        String id = documentSnapshot.getId();
                        String nTitle = documentSnapshot.getString("Title");
                        String ndates = documentSnapshot.getString("Date");

                        DoneTask mDonetask = new DoneTask (id, nTitle , ndates);

                        mList_DoneTask.add(mDonetask);
                    }
                }
                mDoneTaskAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoneActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
