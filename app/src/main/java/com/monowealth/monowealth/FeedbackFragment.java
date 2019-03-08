package com.monowealth.monowealth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FeedbackFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private Context context;
    private int width, height;
    private MediumTextView title;
    private RegularTextView description;
    private LinearLayout buttonLayout;
    private RegularButton userFeedback, newFeature;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    public static FeedbackFragment newInstance() {
        FeedbackFragment fragment = new FeedbackFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;

        setTitle(view);
        setDescription(view);
        setButtonLayout(view);

        return view;
    }

    private void setTitle(View view)
    {
        title = view.findViewById(R.id.fragment_feedback_title);
        title.setTextSize(width / 33);
        title.setHeight((int)(height * 0.15));
    }

    private void setDescription(View view)
    {
        description = view.findViewById(R.id.fragment_feedback_description);
        description.setPadding((int)(width * 0.1), 0, (int)(width * 0.1), 0);
        description.setHeight((int)(height * 0.15));
    }

    private void setButtonLayout(View view)
    {
        buttonLayout = view.findViewById(R.id.fragment_feedback_button_layout);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) buttonLayout.getLayoutParams();
        layoutParams.height = (int)(height * 0.15);
        buttonLayout.setPadding((int)(width * 0.07), 0, (int)(width * 0.07), 0);

        setUserFeedback(view);
        setNewFeature(view);
    }

    private void setUserFeedback(View view)
    {
        userFeedback = view.findViewById(R.id.fragment_feedback_user_feedback);
        userFeedback.setTag("user_feedback_button");
        userFeedback.setOnClickListener(this);
    }

    private void setNewFeature(View view)
    {
        newFeature = view.findViewById(R.id.fragment_feedback_new_feature);
        newFeature.setTag("new_feature_button");
        newFeature.setOnClickListener(this);
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
            case "user_feedback_button":
                mListener.sendFeedback("User Feedback");
                break;
            case "new_feature_button":
                mListener.sendFeedback("New Feature Request");
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void sendFeedback(String subject);
        void hideButtons();
    }
}
