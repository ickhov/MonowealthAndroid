package com.monowealth.monowealth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    MediumTextView title;
    RegularButton feedback, licenses;
    private int width, height;
    private Context context;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;

        setTitle(view);
        setFeedback(view);
        setLicenses(view);

        return view;
    }

    private void setTitle(View view)
    {
        title = view.findViewById(R.id.fragment_settings_title);
        title.setTextSize(width / 31);
    }

    private void setFeedback(View view)
    {
        feedback = view.findViewById(R.id.fragment_settings_feedback);
        feedback.setTag("feedback_button");
        feedback.setOnClickListener(this);
    }

    private void setLicenses(View view)
    {
        licenses = view.findViewById(R.id.fragment_settings_licenses);
        licenses.setTag("licenses_button");
        licenses.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString())
        {
            case "feedback_button":
                mListener.openFeedbackFragment();
                break;
            case "licenses_button":
                mListener.openLicensesFragment();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void openFeedbackFragment();
        void openLicensesFragment();
        void hideButtons();
        void showButtons();
    }
}
