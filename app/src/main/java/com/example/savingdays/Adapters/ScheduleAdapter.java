package com.example.savingdays.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingdays.Model.Schedule;
import com.example.savingdays.R;

import java.util.List;
import java.util.Locale;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private final List<Schedule> list;
    private OnItemClickListener onItemClickListener;

    public ScheduleAdapter(List<Schedule> list) {
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtTitle;
        private final TextView txtStartDate;
        private final TextView txtEndDate;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtScheduleTitle);
            txtStartDate = itemView.findViewById(R.id.txtScheduleStartDate);
            txtEndDate = itemView.findViewById(R.id.txtScheduleEndDate);
        }

        public void bind(Schedule model, OnItemClickListener listener) {

            String strStartDate = String.format(Locale.getDefault(),
                    "%d년 %d월 %d일",
                    model.getStartDate().getYear(),
                    model.getStartDate().getMonthValue(),
                    model.getStartDate().getDayOfMonth());

            String strEndDate = String.format(Locale.getDefault(),
                    "%d년 %d월 %d일",
                    model.getEndDate().getYear(),
                    model.getEndDate().getMonthValue(),
                    model.getEndDate().getDayOfMonth());

            txtTitle.setText(model.getTitle());
            txtStartDate.setText(strStartDate);
            txtEndDate.setText(strEndDate);

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
                .inflate(R.layout.item_schedule, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Schedule item = list.get(position);
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