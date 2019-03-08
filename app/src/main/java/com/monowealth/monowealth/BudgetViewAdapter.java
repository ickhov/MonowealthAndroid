/*
    This class sets up the recycler view adapter
    of CategoryPickerFragment.java
 */

package com.monowealth.monowealth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

public class BudgetViewAdapter extends RecyclerView.Adapter<BudgetViewAdapter.MyViewHolder>
{
    private OnAdapterInteractionListener mListener;
    private int width, height;
    private ArrayList<String> budget;

    public BudgetViewAdapter(Context context, ArrayList<String> budget)
    {
        mListener = (OnAdapterInteractionListener) context;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        this.budget = budget;
        setHasStableIds(true);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        RegularTextView name, maxAmount;
        ImageButton delete;
        String label;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.adapter_budget_view_card_view);
            cardView.setRadius(width / 20);

            name = itemView.findViewById(R.id.adapter_budget_view_name);
            name.setWidth((int)(width * 0.5));
            name.setHeight((int)(height * 0.08));
            name.setTextSize(width / 60);

            maxAmount = itemView.findViewById(R.id.adapter_budget_view_max_amount);
            maxAmount.setWidth((int)(width * 0.5));
            maxAmount.setHeight((int)(height * 0.08));
            maxAmount.setTextSize(width / 60);

            delete = itemView.findViewById(R.id.adapter_budget_view_delete);
            delete.setTag("delete_button");
            delete.setOnClickListener(this);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) delete.getLayoutParams();
            params.height = (int)(height * 0.06);
        }

        @Override
        public void onClick(View v) {
            switch (v.getTag().toString())
            {
                case "delete_button":
                    mListener.deleteBudget(label);
                    break;
            }
        }
    }

    @NonNull
    @Override
    public BudgetViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_budget_view, viewGroup, false);

        return new BudgetViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BudgetViewAdapter.MyViewHolder myViewHolder, int i) {
        int index = myViewHolder.getAdapterPosition();
        String[] labels = budget.get(index).split(":");
        myViewHolder.label = budget.get(index);
        myViewHolder.name.setText(labels[0]);
        myViewHolder.maxAmount.setText(labels[1]);
    }

    @Override
    public int getItemCount() {
        return budget.size() ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnAdapterInteractionListener
    {
        void deleteBudget(String budget);
    }
}
