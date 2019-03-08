package com.monowealth.monowealth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;

import java.util.ArrayList;
import java.util.Locale;

public class CategoryTransactionAdapter extends RecyclerView.Adapter<CategoryTransactionAdapter.MyViewHolder> {

    private ArrayList<String> categories;
    private ArrayList<ArrayList<String>> dataByCategory;
    private int width, height;
    private Context context;

    public CategoryTransactionAdapter(Context context,
                                      ArrayList<String> categories,
                                      ArrayList<ArrayList<String>> dataByCategory)
    {
        this.context = context;
        this.categories = categories;
        this.dataByCategory = dataByCategory;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        setHasStableIds(true);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        LinearLayout descriptionLayout;
        RegularTextView title, date, name, amount, total;
        RecyclerView transactions;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.adapter_category_transaction_card_view);
            cardView.setRadius(width / 20);

            descriptionLayout = itemView.findViewById(R.id.adapter_category_transaction_description_layout);
            ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) descriptionLayout.getLayoutParams();
            params2.height = (int)(height * 0.05);

            title = itemView.findViewById(R.id.adapter_category_transaction_title);
            title.setWidth((int)(width * 0.5));
            title.setHeight((int)(height * 0.07));
            title.setTextSize(width / 60);

            total = itemView.findViewById(R.id.adapter_category_transaction_total);
            total.setWidth((int)(width * 0.5));
            total.setHeight((int)(height * 0.07));
            total.setTextSize(width / 60);

            date = itemView.findViewById(R.id.adapter_category_transaction_date);

            name = itemView.findViewById(R.id.adapter_category_transaction_name);

            amount = itemView.findViewById(R.id.adapter_category_transaction_amount);

            transactions = itemView.findViewById(R.id.adapter_category_transaction_recycler_view);

            LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper
                    (context, LinearLayoutManager.VERTICAL, false);
            transactions.setLayoutManager(layoutManager);

        }
    }

    @NonNull
    @Override
    public CategoryTransactionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_category_transaction, viewGroup, false);

        return new CategoryTransactionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryTransactionAdapter.MyViewHolder myViewHolder, int i) {
        int index = myViewHolder.getAdapterPosition();

        myViewHolder.title.setText(categories.get(index));

        MonthTransactionAdapter adapter = new MonthTransactionAdapter(context, dataByCategory.get(index));
        myViewHolder.transactions.setAdapter(adapter);

        float total = 0f;
        ArrayList<String> specificCategory = dataByCategory.get(index);

        for (int j = 0; j < specificCategory.size(); j++)
            total += Float.parseFloat(specificCategory.get(j).split(":")[4]);

        myViewHolder.total.setText(String.format(Locale.US, "$%,.2f", total));

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
