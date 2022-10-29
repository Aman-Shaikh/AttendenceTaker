package com.example.attendencemanager_miniproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>
{
   ArrayList<model> datalist;
   Context context;

    public myadapter(Context context, ArrayList<model> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
      holder.TeacherName.setText("Teacher Name :"+datalist.get(position).getTeacherName());
      holder.SubjectName.setText("Subject Name :"+datalist.get(position).getSubject());
      holder.ClassYear.setText("Class Year :"+datalist.get(position).getClassYear());
      holder.ClassSem.setText("Class Sem :"+datalist.get(position).getClassSem());

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
       TextView TeacherName,SubjectName,ClassYear,ClassSem;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            TeacherName=itemView.findViewById(R.id.myclasses_teacher_name);
            SubjectName=itemView.findViewById(R.id.myclasses_subject_name);
            ClassYear=itemView.findViewById(R.id.myclasses_class_year);
            ClassSem=itemView.findViewById(R.id.myclasses_class_sem);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {

            int position = this.getAdapterPosition();
            model classDetails = datalist.get(position);
            String teacherName = classDetails.getTeacherName();
            String subjectName = classDetails.getSubject();
            String classYear = classDetails.getClassYear();
            String classSem = classDetails.getClassSem();

            Intent intent = new Intent(context, TakeAttendance.class);
            intent.putExtra("TeacherName",teacherName);
            intent.putExtra("SubjectName",subjectName);
            intent.putExtra("ClassYear",classYear);
            intent.putExtra("ClassSem",classSem);
            context.startActivity(intent);
        }
    }
}
