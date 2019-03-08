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

public class TransactionCategoryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Context context;
    private int width, height;
    private PlainTextFile textFile;
    private ArrayList<String> categories;
    private ArrayList<ArrayList<String>> dataByCategory;
    private String fileName;
    private MediumTextView month;
    private RecyclerView transactions;
    private CategoryTransactionAdapter adapter;

    public TransactionCategoryFragment() {
        // Required empty public constructor
    }

    public static TransactionCategoryFragment newInstance() {
        TransactionCategoryFragment fragment = new TransactionCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_transaction_category, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        textFile = new PlainTextFile(context);
        fileName = mListener.getFileName();

        setCategoriesData();

        setMonthName(view);
        setTransactions(view);

        return view;
    }

    private void setMonthName(View view)
    {
        month = view.findViewById(R.id.fragment_transaction_category_month_name);
        month.setTextSize(width / 33);
        month.setHeight((int)(height * 0.15));
        month.setText(getMonthNames(fileName));
    }

    private void setTransactions(View view)
    {
        transactions = view.findViewById(R.id.fragment_transaction_category_month_recycler_view);
        transactions.setNestedScrollingEnabled(false);

        LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper
                (view.getContext(), LinearLayoutManager.VERTICAL, false);
        transactions.setLayoutManager(layoutManager);

        adapter = new CategoryTransactionAdapter(context, categories, dataByCategory);
        transactions.setAdapter(adapter);
    }

    private void setCategoriesData()
    {
        // info stores the whole data for the month
        ArrayList<String> info = new ArrayList<>();
        try {
            info.addAll(textFile.readByLine(fileName + ".txt"));
        } catch (IOException error)
        {

        }

        Collections.sort(info, new SortFileCategory());

        if (categories == null)
            categories = new ArrayList<>();
        else
            categories.clear();

        if (dataByCategory == null)
            dataByCategory = new ArrayList<>();
        else
            dataByCategory.clear();

        for (int i = 0; i < info.size(); i++)
        {
            // get the category
            String data = info.get(i);

            String type = extractType(data);

            if (type.equals("expenses")) {
                String category = extractCategory(data);

                // get the index of that category in the category list; -1 if there's none
                int index = categories.indexOf(category);

                // if category doesn't exist, add it, initialize new list for dataByCategory list,
                // and find the index where it was added
                if (index < 0) {
                    categories.add(category);
                    ArrayList<String> a = new ArrayList<>();
                    a.add(data);
                    dataByCategory.add(a);
                } else {
                    // get the array list at the index corresponding to the category above and add info to it
                    dataByCategory.get(index).add(data);
                }
            }
        }
    }

    private String extractCategory(String data)
    {
        return data.split(":")[2];
    }
    private String extractType(String data)
    {
        return data.split(":")[1];
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
    }
}
