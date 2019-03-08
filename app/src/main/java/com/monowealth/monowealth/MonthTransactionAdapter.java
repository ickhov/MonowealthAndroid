/*
    This class sets up the view for
    the transaction page when the user
    clicks on the pie chart in MainActivity.java
 */

package com.monowealth.monowealth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MonthTransactionAdapter extends RecyclerView.Adapter<MonthTransactionAdapter.MyViewHolder> {

    private OnAdapterInteractionListener mListener;
    private Context context;
    private int width, height;
    private ArrayList<String> transactions;

    public MonthTransactionAdapter(Context context, ArrayList<String> transactions)
    {
        mListener = (OnAdapterInteractionListener) context;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        this.transactions = transactions;
        setHasStableIds(true);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layout;
        RegularTextView date, name, amount;
        String[] info;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.adapter_month_transaction_layout);
            layout.setOnClickListener(this);

            date = itemView.findViewById(R.id.adapter_month_transaction_date);
            date.setTextSize(width / 65);

            name = itemView.findViewById(R.id.adapter_month_transaction_name);
            name.setTextSize(width / 65);

            amount = itemView.findViewById(R.id.adapter_month_transaction_amount);
            amount.setTextSize(width / 65);
        }

        @Override
        public void onClick(View v) {
            mListener.onTransactionClicked(info);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_month_transaction, viewGroup, false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, (int)(height * 0.08));
        view.setLayoutParams(params);

        return new MonthTransactionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final String[] temp = transactions.get(myViewHolder.getAdapterPosition()).split(":");

        int background;

        if ((temp[1]).equals("expenses"))
            background = ContextCompat.getColor(context, R.color.modified_salmon);
        else
            background = ContextCompat.getColor(context, R.color.modified_flora);

        myViewHolder.layout.setBackgroundColor(background);

        String dateFormat = getTwoDigit(getMonthInt(temp[6]) + 1)
                + "/" + getTwoDigit(Integer.parseInt(temp[7]))
                + "/" + temp[5];
        myViewHolder.date.setText(dateFormat);
        myViewHolder.name.setText(temp[3]);
        myViewHolder.amount.setText(getAmount(temp[4]));

        myViewHolder.info = new String[temp.length];
        for (int j = 0; j < temp.length; j++)
            myViewHolder.info[j] = temp[j];
    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        if (recyclerView.getContext() instanceof OnAdapterInteractionListener) {
            mListener = (OnAdapterInteractionListener) recyclerView.getContext();
        } else {
            throw new RuntimeException(recyclerView.getContext().toString()
                    + " must implement OnItemClickedListener");
        }

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mListener = null;
        context = null;
    }

    private String getAmount(String dollar)
    {
        return String.format(Locale.US, "$%,.2f", Double.parseDouble(dollar));
    }

    private String getTwoDigit(int number)
    {
        NumberFormat formatter = new DecimalFormat("00");

        return formatter.format(number);
    }

    private int getMonthInt(String month)
    {
        switch (month){
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            default:
                return 11;
        }
    }

    public interface OnAdapterInteractionListener
    {
        void onTransactionClicked(String[] info);
    }
}
