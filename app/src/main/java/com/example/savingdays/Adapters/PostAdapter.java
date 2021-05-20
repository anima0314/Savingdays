package com.example.savingdays.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingdays.PostInfo;
import com.example.savingdays.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<PostInfo> mDataset;
    private Activity activity;


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
}
