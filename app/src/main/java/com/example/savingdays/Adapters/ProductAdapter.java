package com.example.savingdays.Adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingdays.Model.Product;
import com.example.savingdays.R;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<Product> list;
    private OnItemClickListener onItemClickListener;

    public ProductAdapter(List<Product> list) {
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

            txtTitle = itemView.findViewById(R.id.txtProductTitle);
            txtType = itemView.findViewById(R.id.txtProductType);
            txtOpenDate = itemView.findViewById(R.id.txtProductOpenDate);
            txtDueDate = itemView.findViewById(R.id.txtProductDueDate);
        }

        public void bind(Product model, OnItemClickListener listener) {

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
            txtType.setText(Product.getTypeName(model.getType()));
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
                .inflate(R.layout.item_product, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product item = list.get(position);
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