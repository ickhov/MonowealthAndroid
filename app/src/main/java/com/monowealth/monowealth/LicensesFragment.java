package com.monowealth.monowealth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class LicensesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Context context;
    private int width, height;
    private RelativeLayout layout;
    private MediumTextView title;
    private RegularTextView description;

    public LicensesFragment() {
        // Required empty public constructor
    }

    public static LicensesFragment newInstance() {
        LicensesFragment fragment = new LicensesFragment();
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
        View view = inflater.inflate(R.layout.fragment_licenses, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;

        setLayout(view);
        setTitle(view);
        setDescription(view);

        return view;
    }

    private void setLayout(View view)
    {
        layout = view.findViewById(R.id.fragment_licenses_layout);
        layout.setPadding(0, 0, 0, height / 20);
    }

    private void setTitle(View view)
    {
        title = view.findViewById(R.id.fragment_licenses_title);
        title.setTextSize(width / 33);
        title.setHeight((int)(height * 0.15));
    }

    private void setDescription(View view)
    {
        description = view.findViewById(R.id.fragment_licenses_description);
        description.setPadding((int)(width * 0.07), 0, (int)(width * 0.07), 0);
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

    public interface OnFragmentInteractionListener {
        void hideButtons();
    }
}
