/*
    This class sets up the view for
    adding new income and expense.
 */

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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class TransactionSaveFragment extends Fragment implements View.OnClickListener {

    MediumTextView title;
    MediumEditText name, amount, location;
    MediumButton category, date;
    RegularButtonRounded cancel, save;
    LinearLayout linearLayout;
    boolean reset;

    private int width, height;
    private String type;
    private int month, day, year;

    private OnFragmentInteractionListener mListener;
    private Context context;

    public TransactionSaveFragment() {
        // Required empty public constructor
    }

    public static TransactionSaveFragment newInstance() {
        TransactionSaveFragment fragment = new TransactionSaveFragment();
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
        View view = inflater.inflate(R.layout.fragment_transaction_save, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        type = mListener.getType();

        setTitle(view);
        setLinearLayout(view);
        setSave(view);
        setCancel(view);

        return view;
    }

    private void setTitle(View view)
    {
        title = view.findViewById(R.id.fragment_transaction_save_title);
        String capitalize = "New " + type.substring(0, 1).toUpperCase() + type.substring(1);
        title.setHeight((int)(height * 0.15));
        title.setText(capitalize);
        title.setTextSize(width / 31);
    }

    private void setLinearLayout(View view)
    {
        linearLayout = view.findViewById(R.id.fragment_transaction_save_linear_layout);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.height = (int)(height * 0.52);
        layoutParams.setMargins(width / 20, 0 , width / 20, 0);

        setName(view);
        setAmount(view);
        setDate(view);
        setCategory(view);
        setLocation(view);
    }

    private void setName(View view)
    {
        name = view.findViewById(R.id.fragment_transaction_save_name);
    }

    private void setAmount(View view)
    {
        amount = view.findViewById(R.id.fragment_transaction_save_amount);
    }

    private void setDate(View view)
    {
        date = view.findViewById(R.id.fragment_transaction_save_date);
        date.setTag("date_button");
        date.setOnClickListener(this);
    }

    private void setCategory(View view)
    {
        category = view.findViewById(R.id.fragment_transaction_save_category);
        category.setTag("category_button");
        category.setOnClickListener(this);
    }

    private void setLocation(View view)
    {
        location = view.findViewById(R.id.fragment_transaction_save_location);
    }

    private void setSave(View view)
    {
        save = view.findViewById(R.id.fragment_transaction_save_save);
        save.setWidth((int)(width * 0.35));
        save.setHeight((int)(height * 0.07));
        save.setTextSize(width / 65);
        save.setTag("save_button");
        save.setOnClickListener(this);
    }

    private void setCancel(View view)
    {
        cancel = view.findViewById(R.id.fragment_transaction_save_cancel);
        cancel.setWidth((int)(width * 0.35));
        cancel.setHeight((int)(height * 0.07));
        cancel.setTextSize(width / 65);
        cancel.setTag("cancel_button");
        cancel.setOnClickListener(this);
    }

    private String getTwoDigit(int number)
    {
        NumberFormat formatter = new DecimalFormat("00");
        return formatter.format(number);
    }

    public void setDateText(int month, int day, int year)
    {
        String dateFormat = getMonth(month) + " "
                + getTwoDigit(day) + ", "
                + year;
        date.setText(dateFormat);

        this.month = month;
        this.day = day;
        this.year = year;
    }

    public void setCategoryText(String label)
    {
        category.setText(label);
    }

    private void reset()
    {
        name.setText("");
        amount.setText("");
        date.setText(R.string.trans_date);
        category.setText(R.string.trans_category);
        location.setText("");
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
    public void onResume() {
        super.onResume();

        if (reset) {
            reset = false;
            reset();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reset = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString())
        {
            case "cancel_button":
                mListener.closeFragment();
                break;
            case "save_button":
                String categoryText = category.getText().toString();
                String dateText = date.getText().toString();
                String nameText = name.getEditableText().toString();
                String amountText = amount.getEditableText().toString();
                String locationText = location.getEditableText().toString();

                if (!isNotComplete(categoryText, dateText, nameText, amountText)) {
                    String id = String.valueOf(year) + getMonth(month) + getTwoDigit(day)
                            + categoryText + amountText + nameText
                            + String.valueOf(new Random().nextInt(10001));

                    int size = 8;
                    if (!locationText.isEmpty())
                        size++;

                    String[] information = new String[size];
                    information[0] = id;
                    information[1] = type;
                    information[2] = categoryText;
                    information[3] = nameText;
                    information[4] = amountText;
                    information[5] = String.valueOf(year);
                    information[6] = getMonth(month);
                    information[7] = getTwoDigit(day);

                    if (size > 8)
                        information[8] = locationText;

                    mListener.saveTransaction(information);
                    mListener.closeFragment();
                }
                else
                    Toast.makeText(v.getContext(),
                            "You haven't filled out everything or your amount format is wrong.",
                            Toast.LENGTH_LONG).show();
                break;
            case "date_button":
                mListener.openDatePicker();
                break;
            case "category_button":
                mListener.openCategoryPicker();
                break;
        }
    }

    private boolean isNotComplete(String category, String date, String name, String amount)
    {
        boolean amountWrongFormat = false;

        int size = amount.length();
        if(amount.contains("."))
        {
            int dotIndex = amount.indexOf(".");
            if (dotIndex != (size - 3))
                amountWrongFormat = true;
        }

        return category.equals("Pick a category")
                || date.equals("Pick a date")
                || name.isEmpty() || amount.isEmpty()
                || amountWrongFormat;
    }

    private String getMonth(int month)
    {
        switch (month){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            default:
                return "December";
        }
    }

    public interface OnFragmentInteractionListener {
        String getType();
        void openDatePicker();
        void openCategoryPicker();
        void saveTransaction(String[] info);
        void closeFragment();
        void hideButtons();
        void showButtons();
    }
}
