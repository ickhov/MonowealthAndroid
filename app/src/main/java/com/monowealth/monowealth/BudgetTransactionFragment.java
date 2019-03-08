package com.monowealth.monowealth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BudgetTransactionFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private Context context;
    private int width, height;
    private PlainTextFile textFile;
    private String fileName;
    private ArrayList<String> categories, maxList;
    private ArrayList<ArrayList<String>> dataByCategory;
    private MediumTextView month;
    private RegularButtonRounded editBudget;
    private RecyclerView transactions;
    private BudgetTransactionAdapter adapter;

    public BudgetTransactionFragment() {
        // Required empty public constructor
    }

    public static BudgetTransactionFragment newInstance() {
        BudgetTransactionFragment fragment = new BudgetTransactionFragment();
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
        View view = inflater.inflate(R.layout.fragment_budget_transaction, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        textFile = new PlainTextFile(context);
        fileName = mListener.getFileName();

        setCategoriesData();
        setMonthName(view);
        setEditBudget(view);
        setTransactions(view);

        return view;
    }

    private void setMonthName(View view)
    {
        month = view.findViewById(R.id.fragment_budget_transaction_month_name);
        month.setTextSize(width / 33);
        month.setHeight((int)(height * 0.15));
        month.setText(getMonthNames(fileName));
    }

    private void setEditBudget(View view)
    {
        editBudget = view.findViewById(R.id.fragment_budget_transaction_edit_budget);
        editBudget.setTextSize(width / 65);
        editBudget.setWidth((int)(width * 0.45));
        editBudget.setHeight((int)(height * 0.07));
        editBudget.setTag("edit_button");
        editBudget.setOnClickListener(this);
    }

    private void setTransactions(View view)
    {
        transactions = view.findViewById(R.id.fragment_budget_transaction_recycler_view);
        transactions.setNestedScrollingEnabled(false);

        LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper
                (view.getContext(), LinearLayoutManager.VERTICAL, false);
        transactions.setLayoutManager(layoutManager);

        adapter = new BudgetTransactionAdapter(context, categories, maxList, dataByCategory);
        transactions.setAdapter(adapter);
    }

    private void setCategoriesData()
    {
        if (categories == null)
            categories = new ArrayList<>();
        else
            categories.clear();

        if (maxList == null)
            maxList = new ArrayList<>();
        else
            maxList.clear();

        if (dataByCategory == null)
            dataByCategory = new ArrayList<>();
        else
            dataByCategory.clear();

        ArrayList<String> budgets = new ArrayList<>();
        try {
            budgets.addAll(textFile.readBudget());
        } catch (IOException error)
        {

        }

        // if user has a budget, then populate the data
        if (!budgets.isEmpty()) {
            // info stores the whole data for the month
            ArrayList<String> info = new ArrayList<>();
            try {
                info.addAll(textFile.readByLine(fileName + ".txt"));
            } catch (IOException error) {

            }

            Collections.sort(budgets, new SortFileCategory());

            for (int i = 0; i < budgets.size(); i++)
                categories.add(budgets.get(i).split(":")[0]);

            for (int i = 0; i < budgets.size(); i++)
                maxList.add(budgets.get(i).split(":")[1]);

            for (int i = 0; i < budgets.size(); i++)
                dataByCategory.add(new ArrayList<String>());

            for (int i = 0; i < info.size(); i++) {
                // get the category
                String data = info.get(i);

                String type = extractType(data);

                if (type.equals("expenses")) {
                    String category = extractCategory(data);

                    // if category of current data is in the user's budget, then add it to list
                    if (categories.contains(category)) {

                        // get the index of that category in the category list; -1 if there's none
                        int index = categories.indexOf(category);
                        dataByCategory.get(index).add(data);
                    }
                }
            }
        }
    }

    private String getMonthNames(String name)
    {
        String[] file = name.split("_");
        return file[0] + " " + file[1];
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

    private String extractCategory(String data)
    {
        return data.split(":")[2];
    }
    private String extractType(String data)
    {
        return data.split(":")[1];
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.showButtons();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString())
        {
            case "edit_button":
                mListener.openBudgetFragment();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        String getFileName();
        void hideButtons();
        void showButtons();
        void openBudgetFragment();
    }
}
