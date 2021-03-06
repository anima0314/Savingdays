package com.example.savingdays;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingdays.Adapters.PostAdapter;
import com.example.savingdays.Model.PostInfo;
import com.example.savingdays.Utils.Util;
import com.example.savingdays.listener.OnPostListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class CommunityActivity extends AppCompatActivity {
    private static String TAG = "CommunityActivity";
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<PostInfo> postList;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document != null) {
                        if(document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                            myStartActivity(MemberInitActivity.class);
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        util = new Util(this);
        postList = new ArrayList<PostInfo>();
        postAdapter = new PostAdapter(CommunityActivity.this, postList);
        postAdapter.setOnPostListener(onPostListener);

        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.writeButton).setOnClickListener(onClickListener);
        findViewById(R.id.btnExchange).setOnClickListener(onClickListener);

        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommunityActivity.this));
        recyclerView.setAdapter(postAdapter);

    }

    protected void onResume(){
        super.onResume();
        postUpdate();

    }

    public void onBackPressed() {
        super.onBackPressed();
        myStartActivity(MainActivity.class);
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(String id) {
            Log.e("??????", "??????:" +id);

            firebaseFirestore.collection("posts").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            util.showToast("???????????? ?????????????????????.");
                            postUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            util.showToast("???????????? ???????????? ??????????????????.");
                        }
                    });
        }

        @Override
        public void onModify(String id) {
            Log.e("??????", "?????? :" +id);

        }
    };



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.writeButton:
                    myStartActivity(WritePostActivity.class);
                    break;
                case R.id.btnExchange:
                    myStartActivity(ExchangeCommunityActivity.class);
                    break;
            }
        }
    };

    private void postUpdate(){
        if (firebaseUser != null) {
            CollectionReference collectionReference = firebaseFirestore.collection("posts");
            collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                postList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    postList.add(new PostInfo(
                                            document.getData().get("title").toString(),
                                            document.getData().get("contents").toString(),
                                            document.getData().get("publisher").toString(),
                                            document.getId(),
                                            new Date(document.getDate("createdAt").getTime())
                                    ));



                                }
                                postAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

