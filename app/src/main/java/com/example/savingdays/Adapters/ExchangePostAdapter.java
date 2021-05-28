package com.example.savingdays.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingdays.ExchangePostInfo;
import com.example.savingdays.R;
import com.example.savingdays.listener.OnPostListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ExchangePostAdapter extends RecyclerView.Adapter<ExchangePostAdapter.PostViewHolder> {

    private ArrayList<ExchangePostInfo> mDataset;
    private Activity activity;
    private FirebaseFirestore firebaseFirestore;
    private OnPostListener onPostListener;

    static class PostViewHolder extends RecyclerView.ViewHolder {
         CardView cardView;
         PostViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }


    public ExchangePostAdapter(Activity activity, ArrayList<ExchangePostInfo> myDataset) {
        mDataset = myDataset;
        this.activity = activity;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void setOnPostListener(OnPostListener onPostListener){
        this.onPostListener = onPostListener;
    }

    @Override
    public ExchangePostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        TextView nickNameEditText = cardView.findViewById(R.id.nickNameEditText);
        nickNameEditText.setText(mDataset.get(position).getpublisher());

        TextView createdAtTextView = cardView.findViewById(R.id.createAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        contentsLayout.removeAllViews();
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
                String id = mDataset.get(position).getId();
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        //수정로직
                        onPostListener.onModify(id);
                        return true;
                    case R.id.delete:
                        //삭제로직
                        onPostListener.onDelete(id);
                        return true;
                    default:
                        return false;
                }            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }

}
