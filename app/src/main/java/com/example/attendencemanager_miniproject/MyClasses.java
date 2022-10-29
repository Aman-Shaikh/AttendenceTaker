package com.example.attendencemanager_miniproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyClasses extends AppCompatActivity {
    RecyclerView recview;
    ArrayList<model> datalist;
    FirebaseFirestore db;
    myadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes);

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();
        adapter=new myadapter(this,datalist);
        recview.setAdapter(adapter);

        db=FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Classes").whereEqualTo("TeacherId",user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            model obj=d.toObject(model.class);
                            datalist.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}