package com.monowealth.monowealth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BudgetFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private int width, height;
    private PlainTextFile textFile;
    private ArrayList<String> budget;
    private String type;
    private LinearLayout top;
    private ImageButton add;
    private MediumTextView title;
    private RecyclerView items;
    private BudgetViewAdapter adapter;

    public BudgetFragment() {
        // Required empty public constructor
    }

    public static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        textFile = new PlainTextFile(context);

        setTop(view);
        setItems(view);

        return view;
    }

    private void setTop(View view)
    {
        top = view.findViewById(R.id.fragment_budget_top);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) top.getLayoutParams();
        params.height = (int)(height * 0.15);

        setTitle(view);
        setAdd(view);
    }

    private void setTitle(View view)
    {
        title = view.findViewById(R.id.fragment_budget_title);
        title.setTextSize(width / 33);
    }

    private void setAdd(View view)
    {
        add = view.findViewById(R.id.fragment_budget_add);
        add.setTag("add_button");
        add.setOnClickListener(this);
    }

    private void setItems(View view)
    {
        items = view.findViewById(R.id.fragment_budget_recycler_view);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) items.getLayoutParams();
        params.height = (int)(height * 0.85);

        LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper
                (view.getContext(), LinearLayoutManager.VERTICAL, false);
        items.setLayoutManager(layoutManager);

        budget = new ArrayList<>();

        try {
            budget.addAll(textFile.readBudget());
        } catch (IOException error) {

        }

        adapter = new BudgetViewAdapter(context, budget);
        items.setAdapter(adapter);
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
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString())
        {
            case "add_button":
                mListener.openBudgetCreatorDialog();
                break;
        }
    }

    // insert to current category
    public void insert(String info)
    {

        try {
            // write info to category file in storage
            boolean budgetExist = budget.contains(info);

            if (budgetExist) {
                Toast.makeText(context,
                        "This budget already exists.", Toast.LENGTH_LONG).show();
            } else {
                textFile.writeBudget(info);
                budget.add(info);
                Collections.sort(budget);
                int position = budget.indexOf(info);
                adapter.notifyItemInserted(position);
                adapter.notifyItemRangeChanged(position, budget.size() - position);
            }
        } catch (IOException error) {
            Toast.makeText(context,
                    "There was a problem adding the new budget\nPlease try again later.", Toast.LENGTH_LONG).show();
        }
    }

    // remove from current category
    public void remove(String info)
    {
        try {
            // delete category from storage
            int position = budget.indexOf(info);
            textFile.deleteBudget(info, budget);
            budget.remove(info);
            adapter.notifyItemRemoved(position);
        } catch (IOException error) {
            Toast.makeText(context,
                    "There was a problem deleting the budget\nPlease try again later.", Toast.LENGTH_LONG).show();
        }
    }

    public interface OnFragmentInteractionListener {
        void hideButtons();
        void openBudgetCreatorDialog();
    }
}
