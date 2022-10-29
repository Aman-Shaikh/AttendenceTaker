package com.example.attendencemanager_miniproject;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class CheckProxy extends AppCompatActivity {
    // on below line we are creating variable
    // for our array list and swipe deck.
    private SwipeDeck cardStack;
    private ArrayList<StudentModel> studentModelArrayList;
    private MyDBHandler sqlitedb;
    private FirebaseFirestore fstore;
    AlertDialog.Builder builder;



    // Storing the data in file with name as Android_28-10-2022

    String attendance,subjectName,filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_proxy);

        builder = new AlertDialog.Builder(CheckProxy.this);

        fstore = FirebaseFirestore.getInstance();
        WriteBatch batch = fstore.batch();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = formatter.format(date);

        Bundle extras = getIntent().getExtras();
        subjectName = extras.getString("SubjectName");


        studentModelArrayList = getProxyStudentList();

        attendance = "Date : "+currentDate+"\n";
        attendance += "Subject : "+studentModelArrayList.get(0).getSubjectName()+"\n";

        filename = "Proxy_"+subjectName+"2_"+currentDate+".txt";

        // on below line we are initializing our array list and swipe deck.
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        // on below line we are creating a variable for our adapter class and passing array list to it.
        final DeckAdapter adapter = new DeckAdapter(studentModelArrayList, this);

        // on below line we are setting adapter to our card stack.
        cardStack.setAdapter(adapter);

        // on below line we are setting event callback to our card stack.
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                if(position!=studentModelArrayList.size()-1)
                {

                    String studentId = studentModelArrayList.get(position).getStudentId();

                    String docPath = "/" + subjectName + "/" + studentId + "/" + currentDate;
                    DocumentReference df = fstore.collection("Students").document(docPath);

                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Date", currentDate);
                    userInfo.put("Attendance", "Absent");
                    attendance += studentId+"-Absent\n";
                    batch.set(df, userInfo);
                }
                else{

                    String studentId = studentModelArrayList.get(position).getStudentId();

                    String docPath = "/" + subjectName + "/" + studentId + "/" + currentDate;
                    DocumentReference df = fstore.collection("Students").document(docPath);
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Date", currentDate);
                    userInfo.put("Attendance", "Absent");
                    attendance += studentId+"-Absent\n";
                    batch.set(df, userInfo);


                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showToast("attendance saved into database successfully");


                            storeAttendance(filename);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("!Could not store attendance into database due to some Error check CheckProxy Activity");
                        }
                    });
                }
            }

            @Override
            public void cardSwipedRight(int position) {

                if(position!=studentModelArrayList.size()-1)
                {

                    String studentId = studentModelArrayList.get(position).getStudentId();

                    String docPath = "/" + subjectName + "/" + studentId + "/" + currentDate;
                    DocumentReference df = fstore.collection("Students").document(docPath);

                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Date", currentDate);
                    userInfo.put("Attendance", "Present");
                    attendance += studentId+"-Present\n";
                    batch.set(df, userInfo);
                }
                else{

                    String studentId = studentModelArrayList.get(position).getStudentId();

                    String docPath = "/" + subjectName + "/" + studentId + "/" + currentDate;
                    DocumentReference df = fstore.collection("Students").document(docPath);

                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Date", currentDate);
                    userInfo.put("Attendance", "Present");
                    attendance += studentId+"-Present\n";
                    batch.set(df, userInfo);

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showToast("Attendance saved into database successfully");


                            storeAttendance(filename);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("!Could not store attendance into database due to some Error check CheckProxy Activity");
                        }
                    });
                }
            }

            @Override
            public void cardClicked(int position) {
                showToast("Swap Right or Left");
            }

            @Override
            public void cardsDepleted() {
                // this method is called when no card is present

            }

            public void cardActionDown() {
                showToast("Swipe Right or Left");
            }


            public void cardActionUp() {
                showToast("Swipe Right or Left");
            }
        });
    }



    private ArrayList<StudentModel> getProxyStudentList()
    {
        sqlitedb = new MyDBHandler(this);
        ArrayList<StudentModel> arr = new ArrayList<>();

        ArrayList<RangeModel> rm =sqlitedb.getRangeFromSQLite(subjectName);


        for(int i=0;i<rm.size();i++)
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (int j=rm.get(i).firstno; j<=rm.get(i).lastno; j++)
            { list.add(j); }

            Collections.shuffle(list);

            for (int j=1; j<=3; j++)
            {
                int randomIndex = list.get(j);

                if(randomIndex>=1&&randomIndex<=9)
                {
                    arr.add(new StudentModel(rm.get(i).prefix+"00"+randomIndex,subjectName,R.drawable.logo));
                }
                else if(randomIndex>=10&randomIndex<=99)
                {
                    arr.add(new StudentModel(rm.get(i).prefix+"0"+randomIndex,subjectName,R.drawable.logo));
                }
                else
                {
                    arr.add(new StudentModel(rm.get(i).prefix+randomIndex,subjectName,R.drawable.logo));
                }
            }
        }
        return arr;
    }

    private void storeAttendance(String filename)
    {

        FileOutputStream fileOutputStream = null;
        try {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(folder, filename);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(attendance.getBytes());

            createNotification();

        } catch (Exception e) {
            e.printStackTrace();
            showToast("Error in file");
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();


                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    private void createNotification() {

        String title = "Attendance Downloaded";
        String details = "Attendace is downloaded in dowloads folder";
        Notification.Builder nb = new Notification.Builder(CheckProxy.this);
        nb.setSmallIcon(R.drawable.logo);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        nb.setLargeIcon(bitmap);
        nb.setContentTitle(title);
        nb.setContentText(details);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            String channelId = "your_channel_id";
            NotificationChannel channel = new NotificationChannel(channelId,"channel title",NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
            nb.setChannelId(channelId);
        }
        nm.notify(0,nb.build());
        startActivity(new Intent(this,TeachersActivity.class));
        finish();

    }



    private void showToast(String str)
    {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
