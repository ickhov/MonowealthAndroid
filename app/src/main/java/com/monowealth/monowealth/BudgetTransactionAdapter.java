package com.monowealth.monowealth;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Locale;

public class BudgetTransactionAdapter extends RecyclerView.Adapter<BudgetTransactionAdapter.MyViewHolder> {

    private ArrayList<String> categories, maxList;
    private ArrayList<ArrayList<String>> dataByCategory;
    private int width, height;
    private Context context;

    public BudgetTransactionAdapter(Context context,
                                    ArrayList<String> categories,
                                    ArrayList<String> maxList,
                                    ArrayList<ArrayList<String>> dataByCategory)
    {
        this.context = context;
        this.categories = categories;
        this.maxList = maxList;
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
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.adapter_budget_transaction_card_view);
            cardView.setRadius(width / 20);

            descriptionLayout = itemView.findViewById(R.id.adapter_budget_transaction_description_layout);
            ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) descriptionLayout.getLayoutParams();
            params1.height = (int)(height * 0.05);

            title = itemView.findViewById(R.id.adapter_budget_transaction_title);
            title.setHeight((int)(height * 0.07));
            title.setTextSize(width / 60);

            total = itemView.findViewById(R.id.adapter_budget_transaction_total);
            total.setHeight((int)(height * 0.07));
            total.setTextSize(width / 60);

            progressBar = itemView.findViewById(R.id.adapter_budget_transaction_progress_bar);
            ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) progressBar.getLayoutParams();
            params2.height = (int)(height * 0.05);
            progressBar.setScaleY(width / 150);

            LayerDrawable progressBarDrawable = (LayerDrawable) progressBar.getProgressDrawable();
            Drawable backgroundDrawable = progressBarDrawable.getDrawable(0);
            Drawable progressDrawable = progressBarDrawable.getDrawable(2);

            backgroundDrawable.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.sky), PorterDuff.Mode.SRC_IN);
            progressDrawable.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.cantaloupe), PorterDuff.Mode.SRC_IN);

            date = itemView.findViewById(R.id.adapter_budget_transaction_date);

            name = itemView.findViewById(R.id.adapter_budget_transaction_name);

            amount = itemView.findViewById(R.id.adapter_budget_transaction_amount);

            transactions = itemView.findViewById(R.id.adapter_budget_transaction_recycler_view);

            LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper
                    (context, LinearLayoutManager.VERTICAL, false);
            transactions.setLayoutManager(layoutManager);

        }
    }

    @NonNull
    @Override
    public BudgetTransactionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_budget_transaction, viewGroup, false);

        return new BudgetTransactionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetTransactionAdapter.MyViewHolder myViewHolder, int i) {
        int index = myViewHolder.getAdapterPosition();

        myViewHolder.title.setText(categories.get(index));

        double total = 0.0;
        double max = Double.parseDouble(maxList.get(index));

        if (!dataByCategory.get(index).isEmpty()) {

            MonthTransactionAdapter adapter = new MonthTransactionAdapter(context, dataByCategory.get(index));
            myViewHolder.transactions.setAdapter(adapter);

            ArrayList<String> specificCategory = dataByCategory.get(index);

            for (int j = 0; j < specificCategory.size(); j++)
                total += Float.parseFloat(specificCategory.get(j).split(":")[4]);
        } else {
            myViewHolder.descriptionLayout.setVisibility(View.GONE);
        }

        double percent = (total / max) * 100.00;

        myViewHolder.progressBar.setProgress((int) (percent));

        StringBuilder builder = new StringBuilder();
        builder.append(String.format(Locale.US, "$%,.2f", total));
        builder.append(" ");
        builder.append(String.format(Locale.US, "(%.1f%%)", percent));
        builder.append(" of ");
        builder.append(String.format(Locale.US, "$%,.2f", max));

        myViewHolder.total.setText(builder);
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
