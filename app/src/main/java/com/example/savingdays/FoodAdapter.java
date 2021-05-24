package com.example.savingdays;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingdays.R;
import com.example.savingdays.Food;

import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private final List<Food> list;
    private OnItemClickListener onItemClickListener;

    public FoodAdapter(List<Food> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtType;
        private final TextView txtOpenDate;
        private final TextView txtDueDate;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtFoodTitle);
            txtType=itemView.findViewById(R.id.txtFoodType);
            txtOpenDate = itemView.findViewById(R.id.txtFoodOpenDate);
            txtDueDate = itemView.findViewById(R.id.txtFoodDueDate);
        }

        public void bind(Food model, OnItemClickListener listener) {

            String strOpenDate = String.format(Locale.getDefault(),
                    "%d년 %d월 %d일",
                    model.getOpenDate().getYear(),
                    model.getOpenDate().getMonthValue(),
                    model.getOpenDate().getDayOfMonth());

            String strDueDate = String.format(Locale.getDefault(),
                    "%d년 %d월 %d일",
                    model.getDueDate().getYear(),
                    model.getDueDate().getMonthValue(),
                    model.getDueDate().getDayOfMonth());

            txtTitle.setText(model.getTitle());
            txtType.setText(Food.getTypeName(model.getType()));
            txtOpenDate.setText(strOpenDate);
            txtDueDate.setText(strDueDate);

            if (listener != null) {
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                });
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Food item = list.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}