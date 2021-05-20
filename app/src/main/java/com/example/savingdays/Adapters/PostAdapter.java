package com.example.savingdays.Adapters;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingdays.PostInfo;
import com.example.savingdays.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<PostInfo> mDataset;
    private Activity activity;
    private FirebaseFirestore firebaseFirestore;

    static class PostViewHolder extends RecyclerView.ViewHolder {
         CardView cardView;
         PostViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }


    public PostAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
        mDataset = myDataset;
        this.activity = activity;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        final PostViewHolder postViewHolder = new PostViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPopup(v, postViewHolder.getAdapterPosition());
            }
        });

        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        titleTextView.setText(mDataset.get(position).getTitle());

        TextView createdAtTextView = cardView.findViewById(R.id.createAtTextView);
        createdAtTextView.setText(mDataset.get(position).toString());

        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        String contentsList = mDataset.get(position).getContents();
            String contents = contentsList.toString();
            TextView textView = new TextView(activity);
            textView.setLayoutParams(layoutParams);
            textView.setText(contents);
            contentsLayout.addView(textView);

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //수정,삭제 팝업창
    private void showPopup(View v, int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modity:
                        //수정로직

                        //archive(item);
                        return true;
                    case R.id.delete:
                        //삭제로직
                        firebaseFirestore.collection("posts").document(mDataset.get(position).getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startToast("게시글을 삭제하였습니다.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        startToast("게시글을 삭제하지 못하였습니다.");
                                    }
                                });
                        return true;
                    default:
                        return false;
                }            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }

    private void startToast(String msg) { Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();}
}
