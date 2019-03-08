/*
    This class sets up the view for the
    transaction page when the user clicked
    on the pie chart.
 */

package com.monowealth.monowealth;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class TransactionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Context context;
    private int width, height;
    private Typeface quicksand;
    private PlainTextFile textFile;
    private ArrayList<String> info;
    private String fileName;
    private MediumTextView month;
    private RegularTextView weekToWeek, income, expenses;
    private BarChart weeklySpendingBarChart;
    private LinearLayout layout;
    private RecyclerView transactions;
    private MonthTransactionAdapter adapter;
    private double incomeAmount, expensesAmount;
    private CardView allTransactionsCardView;

    public TransactionFragment() {
        // Required empty public constructor
    }

    public static TransactionFragment newInstance() {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        quicksand = Typeface.createFromAsset(context.getAssets(), "Quicksand-Regular.ttf");
        textFile = new PlainTextFile(context);
        fileName = mListener.getFileName();
        incomeAmount = mListener.getIncomeAmount();
        expensesAmount = mListener.getExpenseAmount();

        setInformation();
        setMonthName(view);
        setWeekToWeek(view);
        setWeeklySpendingBarChart(view);

        // only display bar chart if expense > $0
        if (expensesAmount > 0) {
            weekToWeek.setVisibility(View.VISIBLE);
            weeklySpendingBarChart.setVisibility(View.VISIBLE);
        } else {
            weekToWeek.setVisibility(View.GONE);
            weeklySpendingBarChart.setVisibility(View.GONE);
        }

        setAllTransactionsCardView(view);
        setIncome(view);
        setExpenses(view);
        setDescriptionLabel(view);
        setTransactions(view);

        return view;
    }

    private void setMonthName(View view)
    {
        month = view.findViewById(R.id.fragment_transaction_month_name);
        month.setTextSize(width / 33);
        month.setHeight((int)(height * 0.15));
        month.setText(getMonthNames(fileName));
    }

    private void setWeekToWeek(View view)
    {
        weekToWeek = view.findViewById(R.id.fragment_transaction_week_to_week);
        weekToWeek.setHeight((int)(height * 0.03));
    }

    private void setWeeklySpendingBarChart(View view)
    {
        weeklySpendingBarChart = view.findViewById(R.id.fragment_transaction_weekly_spending_bar_chart);
        setBarDataSet();
        weeklySpendingBarChart.setFitBars(true);
        weeklySpendingBarChart.getLegend().setEnabled(false);
        weeklySpendingBarChart.setDescription(new Description());
        weeklySpendingBarChart.getDescription().setText("");
        weeklySpendingBarChart.setHighlightPerTapEnabled(false);
        weeklySpendingBarChart.setHighlightFullBarEnabled(false);
        weeklySpendingBarChart.setHighlightPerDragEnabled(false);
        weeklySpendingBarChart.setDoubleTapToZoomEnabled(false);

        XAxis xAxis = weeklySpendingBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setYOffset(-height / 400);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTypeface(quicksand);
        xAxis.setTextSize(width / 75);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(4);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.format(Locale.US, "Week %d", (int)value);
            }
        });

        YAxis leftAxis = weeklySpendingBarChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTypeface(quicksand);
        leftAxis.setTextSize(width / 75);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.format(Locale.US, "$%.0f", value);
            }
        });

        YAxis rightAxis = weeklySpendingBarChart.getAxisRight();
        rightAxis.setEnabled(false);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) weeklySpendingBarChart.getLayoutParams();
        layoutParams.height = (int)(height * 0.4);

        weeklySpendingBarChart.invalidate();
    }

    private void setAllTransactionsCardView(View view)
    {
        allTransactionsCardView = view.findViewById(R.id.fragment_transaction_all_transactions_card_view);
        allTransactionsCardView.setRadius(width / 20);
    }

    private void setIncome(View view)
    {
        income = view.findViewById(R.id.fragment_transaction_income);
        income.setWidth((int)(width * 0.5));
        income.setHeight((int)(height * 0.06));
        income.setTextSize(width / 65);

        StringBuilder builder = new StringBuilder();
        builder.append("Income: $");
        builder.append(String.format(Locale.ENGLISH, "%,.2f", incomeAmount));

        income.setText(builder);
    }

    private void setExpenses(View view)
    {
        expenses = view.findViewById(R.id.fragment_transaction_expenses);
        expenses.setWidth((int)(width * 0.5));
        expenses.setHeight((int)(height * 0.06));
        expenses.setTextSize(width / 65);

        StringBuilder builder = new StringBuilder();
        builder.append("Expenses: $");
        builder.append(String.format(Locale.ENGLISH, "%,.2f", expensesAmount));

        expenses.setText(builder);
    }

    private void setDescriptionLabel(View view)
    {
        layout = view.findViewById(R.id.fragment_transaction_description_layout);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();
        params.height = (int)(height * 0.05);

        layout.setBackgroundColor(Color.BLACK);
    }

    private void setTransactions(View view)
    {
        transactions = view.findViewById(R.id.fragment_transaction_month_recycler_view);

        LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper
                (view.getContext(), LinearLayoutManager.VERTICAL, false);
        transactions.setLayoutManager(layoutManager);

        adapter = new MonthTransactionAdapter(context, info);
        transactions.setAdapter(adapter);
    }

    private void setInformation()
    {
        // fill in monthData for each month name //
        if (info == null)
            info = new ArrayList<>();
        else
            info.clear();

        try {
            info.addAll(textFile.readByLine(fileName + ".txt"));
        } catch (IOException error)
        {

        }
    }

    private void setBarDataSet()
    {
        // get the file month and year
        String[] monthYear = mListener.getFileName().split("_");
        int month = getMonthInt(monthYear[0]);
        int year = Integer.parseInt(monthYear[1]);

        // use month and year to get the number of days in this particular calendar date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        // initialize values for data set
        ArrayList<BarEntry> entries = new ArrayList<>();
        float[] values = new float[4];

        // fill in labels with Week 1, Week 2, Week 3, and Week 4
        // fill values with 0 in case there's no spending for the week
        for (int i = 0; i < 4; i++)
        {
            // pre-fill values to have all 0
            values[i] = 0f;
        }

        for (int i = 0; i < info.size(); i++) {
            // get transactions in the list
            String[] temp = info.get(i).split(":");

            if ((temp[1]).equals("expenses")) {
                // use day as the index value
                int day = Integer.parseInt(temp[7]);

                int index = (int) Math.floor((day - 1) / 7);

                if (index > 3)
                    index = 3;

                // add amount to the existing value for the index
                values[index] += Float.parseFloat(temp[4]);
            }
        }

        for (int i = 0; i < 4; i++)
        {
            entries.add(new BarEntry(i + 1, values[i]));
        }

        BarDataSet set = new BarDataSet(entries, "");
        set.setValueTextColor(Color.WHITE);
        set.setValueTypeface(quicksand);
        set.setValueTextSize(width / 70);
        set.setColor(Color.WHITE);

        BarData data = new BarData(set);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format(Locale.US, "$%,.2f", value);
            }
        });

        weeklySpendingBarChart.setData(data);
    }

    private String getMonthNames(String name)
    {
        String[] file = name.split("_");
        return file[0] + " " + file[1];
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        mListener.hideButtons();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.showButtons();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        String getFileName();
        void hideButtons();
        void showButtons();
        double getIncomeAmount();
        double getExpenseAmount();
    }
}
