package com.example.justdo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DoneTaskAdapter extends RecyclerView.Adapter<DoneTaskAdapter.TaskViewHolder>{
    List<DoneTask> mDoneTasks;
    private Context mContext;

    public DoneTaskAdapter(List<DoneTask> mDoneTasks, Context mContext){
        this.mDoneTasks = mDoneTasks;
        this.mContext = mContext;
    };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.done_tasks_list,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        DoneTask mDoneTask = mDoneTasks.get(position);
        String DoneTaskTitle = mDoneTask.getTask();
        holder.mTasktitle.setText(DoneTaskTitle);
        String DoneTaskDate = mDoneTask.getDate();
        holder.mTaskdate.setText(DoneTaskDate);
    }

    @Override
    public int getItemCount() {
        return mDoneTasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{
        TextView mTasktitle;
        TextView mTaskdate;
        RecyclerView mRecyclerView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            mTasktitle = itemView.findViewById(R.id.done_list_tasks);
            mTaskdate = itemView.findViewById(R.id.done_list_date);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);
        }
    };

    public Context getContext(){
        return mContext;
    }
}
