/*
    This class sets up the pie chart of
    the MainActivity.java for data recycler view
 */

package com.monowealth.monowealth;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.Locale;

public class MainMenuMonthlyOverviewAdapter extends RecyclerView.Adapter<MainMenuMonthlyOverviewAdapter.MyViewHolder> {

    private OnAdapterInteractionListener mListener;         // handle button presses on pie chart
    private Context context;
    private int[] colors;                                   // colors to use for pie chart
    private ArrayList<String> monthNames;                   // list of month names from storage (ONLY FOR NAME VIEW)
    private ArrayList<ArrayList<String[]>> monthData;       // list of data for each month
    private ArrayList<String> categories;                   // store all the expenses categories
    private int width, height;                              // store width and height
    private Typeface quicksand;                             // default quicksand
    private double budgetTotal;

    public MainMenuMonthlyOverviewAdapter(Context context, ArrayList<String> monthNames,
                                          ArrayList<ArrayList<String[]>> monthData,
                                          ArrayList<String> categories)
    {
        mListener = (OnAdapterInteractionListener) context;
        this.context = context;
        this.monthNames = monthNames;
        this.monthData = monthData;
        this.categories = categories;
        colors = context.getResources().getIntArray(R.array.colors);
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        quicksand = Typeface.createFromAsset(context.getAssets(), "Quicksand-Regular.ttf");
        setHasStableIds(true);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnChartGestureListener {

        PieChart pieChart;
        RegularTextView name, income, expenses, budget, noExpenses;
        String month;
        double incomeAmount, expensesAmount;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.main_menu_monthly_overview_month);
            name.setHeight((int)(height * 0.07));
            name.setTextSize(width / 35);
            name.setTag("name");
            name.setOnClickListener(this);

            income = itemView.findViewById(R.id.main_menu_monthly_overview_income);
            income.setWidth((int)(width * 0.5));
            income.setHeight((int)(height * 0.06));
            income.setTextSize(width / 65);
            income.setTag("income");
            income.setOnClickListener(this);

            expenses = itemView.findViewById(R.id.main_menu_monthly_overview_expenses);
            expenses.setWidth((int)(width * 0.5));
            expenses.setHeight((int)(height * 0.06));
            expenses.setTextSize(width / 65);
            expenses.setTag("expenses");
            expenses.setOnClickListener(this);

            budget = itemView.findViewById(R.id.main_menu_monthly_overview_budget);
            budget.setHeight((int)(height * 0.06));
            budget.setTextSize(width / 65);
            budget.setTag("budget");
            budget.setOnClickListener(this);

            noExpenses = itemView.findViewById(R.id.main_menu_monthly_overview_no_expenses);
            noExpenses.setHeight((int)(height * 0.45));
            noExpenses.setPadding(width / 80, 0 , width / 80, 0);
            noExpenses.setTextSize(width / 50);

            pieChart = itemView.findViewById(R.id.main_menu_monthly_overview_pie_chart);
            pieChart.setOnChartGestureListener(this);
            pieChart.getLegend().setEnabled(false);
            pieChart.animateY(2500);
            pieChart.setNoDataText("");
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setEntryLabelTypeface(quicksand);
            pieChart.setEntryLabelTextSize(width / 65);
            pieChart.setDescription(new Description());
            pieChart.getDescription().setText("");
            pieChart.setDrawHoleEnabled(false);
            pieChart.setHighlightPerTapEnabled(false);

            ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) pieChart.getLayoutParams();
            params2.height = (int)(height * 0.55);

            // initial setup for which view will be shown to user
            noExpenses.setVisibility(View.GONE);
            pieChart.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View v) {
            switch (v.getTag().toString()) {
                case "budget":
                    mListener.onBudgetClicked(month);
                    break;
                default:
                    if (incomeAmount > 0 || expensesAmount > 0)
                        mListener.onTransactionClicked(month, incomeAmount, expensesAmount);
                    break;
            }
        }

        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {
            if (!(incomeAmount == 0) || !(expensesAmount == 0))
                mListener.onCircleProgressClicked(month);
        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

        }
    }


    @NonNull
    @Override
    public MainMenuMonthlyOverviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_overview_monthly_menu_main, viewGroup, false);

        return new MainMenuMonthlyOverviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuMonthlyOverviewAdapter.MyViewHolder myViewHolder, int i) {
        // hold the current position of the adapter
        int index = myViewHolder.getAdapterPosition();

        myViewHolder.month = monthNames.get(index);

        // month and year of the data
        myViewHolder.name.setText(getMonthNames(myViewHolder.month.split("_")));

        myViewHolder.incomeAmount = sum("income", index);
        // display data for income and expenses
        StringBuilder builder1 = new StringBuilder();
        builder1.append("Income: $");
        builder1.append(String.format(Locale.ENGLISH, "%,.2f", myViewHolder.incomeAmount));

        // calculate sum of expenses
        myViewHolder.expensesAmount = sum("expenses", index);

        StringBuilder builder2 = new StringBuilder();
        builder2.append("Expenses: $");
        builder2.append(String.format(Locale.ENGLISH, "%,.2f", myViewHolder.expensesAmount));

        budgetTotal = mListener.calculateBudgetTotal();

        StringBuilder builder3 = new StringBuilder();
        builder3.append("Budget: ");

        if (budgetTotal == 0)
            builder3.append("Not Available");
        else
            builder3.append(String.format(Locale.ENGLISH, "$%,.2f", budgetTotal));

        // show noExpenses view if there's no expenses to show
        // else show the pie chart
        if (myViewHolder.incomeAmount >= 0 && myViewHolder.expensesAmount == 0) {
            myViewHolder.noExpenses.setVisibility(View.VISIBLE);
            myViewHolder.pieChart.setVisibility(View.GONE);
        } else {
            myViewHolder.noExpenses.setVisibility(View.GONE);
            myViewHolder.pieChart.setVisibility(View.VISIBLE);
            displayData(index, myViewHolder.pieChart);
        }

        myViewHolder.income.setText(builder1);
        myViewHolder.expenses.setText(builder2);
        myViewHolder.budget.setText(builder3);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mListener.changeBackgroundColor(holder.name.getText().toString().split(" ")[0]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return monthNames.size();
    }

    private void displayData(int index, PieChart pieChart)
    {
        // store information for each month
        ArrayList<String[]> information = new ArrayList<>(monthData.get(index));

        if (information.size() > 0) {
            // store the entries in the pie chart
            ArrayList<PieEntry> entries = new ArrayList<>();

            for (int i = 0; i < categories.size(); i++) {
                // store each category in expenses_categories.txt
                String category = categories.get(i);

                // amount for each category
                float amount = 0;

                // iterate through each data for the month to split up the category
                for (int j = 0; j < information.size(); j++) {
                    // store the data to compare
                    String[] info = information.get(j);

                    // if data is an expenses and match the category name, then add the amount
                    if (info[1].equals("expenses") && category.equals(info[2]))
                        amount += Double.parseDouble(info[4]);
                }

                if (amount > 0)
                    entries.add(new PieEntry(amount, category));
            }

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(colors);
            set.setValueTextColor(Color.BLACK);
            set.setValueTypeface(quicksand);
            set.setValueTextSize(width / 65);
            PieData data = new PieData(set);
            data.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.format(Locale.US, "$%,.2f", value);
                }
            });
            pieChart.setData(data);
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }

    private String getMonthNames(String[] name)
    {
        return name[0] + " " + name[1];
    }

    private double sum(String type, int index)
    {
        double total = 0.0;

        // store the data for the specific month
        ArrayList<String[]> temp = new ArrayList<>(monthData.get(index));

        // add up the sum for the specific type for the specific month
        for (int i = 0; i < temp.size(); i++) {
            String[] info = temp.get(i);

            if (info[1].equals(type)) {
                total += Double.parseDouble(info[4]);
            }
        }

        return total;
    }

    public interface OnAdapterInteractionListener
    {
        void onCircleProgressClicked(String fileName);
        void onTransactionClicked(String fileName, double incomeAmount, double expensesAmount);
        void onBudgetClicked(String fileName);
        void changeBackgroundColor(String monthName);
        double calculateBudgetTotal();
    }
}
