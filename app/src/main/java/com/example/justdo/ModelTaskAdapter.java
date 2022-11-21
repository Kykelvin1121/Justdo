package com.example.justdo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelTaskAdapter extends RecyclerView.Adapter<ModelTaskAdapter.TaskViewHolder> {
    List<ModelTask> mModelTasks;
    private Context mContext;
    private String ndates;
    private String mTitle;
    private String mDescription;
    private String post_key = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelTaskAdapter(List<ModelTask> mModelTasks, Context mContext){
        this.mModelTasks = mModelTasks;
        this.mContext = mContext;
    };

    @NonNull
    @Override
    public ModelTaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lists_item,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        ModelTask mModelTask = mModelTasks.get(position);

        //Show task on lists_items.xml
        String mTaskTitle = mModelTask.getTask();
        holder.mCheckbox.setText(mTaskTitle);

        //Show date on lists_items.xml
        String mTaskDate = mModelTask.getDate();
        holder.mDate.setText(mTaskDate);

        //Go to delete.xml when user click the task
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitle = mModelTask.getTask();
                ndates = mModelTask.getDate();
                mDescription = mModelTask.getDescription();
                post_key = mModelTask.getId();
                deleteData();
            }
        });

        //Toast their task description when user long press the task
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, mModelTask.getDescription(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModelTasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView mCheckbox;
        TextView mDate;
        RecyclerView mRecyclerView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckbox = itemView.findViewById(R.id.checkBox);
            mDate = itemView.findViewById(R.id.date);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);
        }
    };

    public Context getContext(){
        return mContext;
    }

    public void deleteData(){
        AlertDialog.Builder myDialog= new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.delete, null);

        myDialog.setView(mView);
        final  AlertDialog dialog = myDialog.create();

        Button btnDelete = mView.findViewById(R.id.deleteTaskButton);
        Button btnFinish = mView.findViewById(R.id.finishTaskButton);
        ImageView mImageView_Exit = mView.findViewById(R.id.exit);

        db = FirebaseFirestore.getInstance();

        mImageView_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("nTasks").document(post_key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(view.getContext(),"Deleted successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext,MainActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });

                dialog.dismiss();

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> dTasks = new HashMap<>();
                dTasks.put("Title", mTitle);
                dTasks.put("Date", ndates);
                dTasks.put("Description", mDescription);

                // Add a new document with a generated ID
                db.collection("DoneTasks")
                        .add(dTasks)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                mContext.startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                db.collection("nTasks").document(post_key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(view.getContext(),"The task is completed!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext,MainActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
        notifyDataSetChanged();
    }
}
