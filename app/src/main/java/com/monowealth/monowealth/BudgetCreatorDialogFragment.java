package com.monowealth.monowealth;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BudgetCreatorDialogFragment extends DialogFragment implements View.OnClickListener {

    private OnDialogFragmentInteractionListener mListener;
    MediumTextView title;
    MediumEditText name, maxAmount;
    RegularButton add;
    Context context;
    int width, height;

    public BudgetCreatorDialogFragment() {
        // Required empty public constructor
    }

    public static BudgetCreatorDialogFragment newInstance() {
        BudgetCreatorDialogFragment fragment = new BudgetCreatorDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget_creator_dialog, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;

        if (getDialog() != null && getDialog().getWindow() != null) {
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(width / 13);
            shape.setColor(Color.TRANSPARENT);
            getDialog().getWindow().setBackgroundDrawable(shape);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        setTitle(view);
        setName(view);
        setMaxAmount(view);
        setAdd(view);

        return view;
    }

    private void setTitle(View view)
    {
        title = view.findViewById(R.id.fragment_budget_creator_dialog_title);
        title.setTextSize(width / 50);
    }

    private void setName(View view)
    {
        name = view.findViewById(R.id.fragment_budget_creator_dialog_name);
    }

    private void setMaxAmount(View view)
    {
        maxAmount = view.findViewById(R.id.fragment_budget_creator_dialog_max_amount);
    }

    private void setAdd(View view)
    {
        add = view.findViewById(R.id.fragment_budget_creator_dialog_add);
        add.setTag("add_button");
        add.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getDialog() != null && getDialog().getWindow() != null) {
            ViewGroup.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
            layoutParams.width = (int)(width * 0.7);
            layoutParams.height = (int)(height * 0.35);
            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) layoutParams);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogFragmentInteractionListener) {
            mListener = (OnDialogFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        String n = name.getEditableText().toString();
        String m = maxAmount.getEditableText().toString();
        if (!n.isEmpty() && !m.isEmpty()) {
            switch (v.getTag().toString()) {
                case "add_button":
                    name.setText("");
                    maxAmount.setText("");
                    mListener.addBudget(n + ":" + m);
                    break;
            }
        } else {
            Toast.makeText(context,
                    "You haven't filled out the form yet.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public interface OnDialogFragmentInteractionListener {
        void addBudget(String category);
    }
}
