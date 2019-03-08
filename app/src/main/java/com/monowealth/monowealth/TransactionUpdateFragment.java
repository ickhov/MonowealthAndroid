/*
    This class sets up the view for
    updating income and expenses
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

public class TransactionUpdateFragment extends Fragment implements View.OnClickListener {

    MediumTextView title;
    MediumEditText name, amount, location;
    MediumButton category, date;
    RegularButtonRounded update, delete, cancel;
    LinearLayout linearLayout;
    boolean sameView;

    private int width;
    private int height;
    private String type;
    private String[] information;
    private int month, day, year;

    private OnFragmentInteractionListener mListener;
    private Context context;

    public TransactionUpdateFragment() {
        // Required empty public constructor
    }

    public static TransactionUpdateFragment newInstance() {
        TransactionUpdateFragment fragment = new TransactionUpdateFragment();
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
        View view = inflater.inflate(R.layout.fragment_transaction_update, container, false);

        context = view.getContext();
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        information = mListener.getInformation();
        type = mListener.getType();

        setTitle(view);
        setMenuLayout(view);
        setUpdate(view);
        setDelete(view);
        setCancel(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!sameView) {
            sameView = true;
            setInfo();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sameView = false;
    }

    private void setInfo()
    {
        if (information != null) {
            name.setText(information[3]);
            amount.setText(information[4]);

            String dateFormat = information[6] + " "
                    + getTwoDigit(Integer.parseInt(information[7])) + ", "
                    + information[5];
            date.setText(dateFormat);

            category.setText(information[2]);

            this.month = getMonthInt(information[6]);
            this.day = Integer.parseInt(information[7]);
            this.year = Integer.parseInt(information[5]);

            if (information.length > 8)
                location.setText(information[8]);
            else
                location.setText("");
        }
    }

    private void setTitle(View view)
    {
        title = view.findViewById(R.id.fragment_transaction_update_title);
        String capitalize = type.substring(0, 1).toUpperCase() + type.substring(1);
        title.setHeight((int)(height * 0.15));
        title.setText(capitalize);
        title.setTextSize(width / 31);
    }

    private void setMenuLayout(View view)
    {
        linearLayout = view.findViewById(R.id.fragment_transaction_update_linear_layout);
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
        name = view.findViewById(R.id.fragment_transaction_update_name);
    }

    private void setAmount(View view)
    {
        amount = view.findViewById(R.id.fragment_transaction_update_amount);
    }

    private void setDate(View view)
    {
        date = view.findViewById(R.id.fragment_transaction_update_date);
        date.setTag("date_button");
        date.setOnClickListener(this);
    }

    private void setCategory(View view)
    {
        category = view.findViewById(R.id.fragment_transaction_update_category);
        category.setTag("category_button");
        category.setOnClickListener(this);
    }

    private void setLocation(View view)
    {
        location = view.findViewById(R.id.fragment_transaction_update_location);
    }

    private void setUpdate(View view)
    {
        update = view.findViewById(R.id.fragment_transaction_update_save);
        update.setWidth((int)(width * 0.3));
        update.setHeight((int)(height * 0.07));
        update.setTextSize(width / 70);
        update.setTag("update_button");
        update.setOnClickListener(this);
    }

    private void setDelete(View view)
    {
        delete = view.findViewById(R.id.fragment_transaction_update_delete);
        delete.setWidth((int)(width * 0.3));
        delete.setHeight((int)(height * 0.07));
        delete.setTextSize(width / 70);
        delete.setTag("delete_button");
        delete.setOnClickListener(this);
    }

    private void setCancel(View view)
    {
        cancel = view.findViewById(R.id.fragment_transaction_update_cancel);
        cancel.setWidth((int)(width * 0.3));
        cancel.setHeight((int)(height * 0.07));
        cancel.setTextSize(width / 70);
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
        mListener.resetDetailInformation();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString())
        {
            case "cancel_button":
                mListener.closeFragment();
                break;
            case "update_button":
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

                    String[] info = new String[size];
                    info[0] = id;
                    info[1] = type;
                    info[2] = categoryText;
                    info[3] = nameText;
                    info[4] = amountText;
                    info[5] = String.valueOf(year);
                    info[6] = getMonth(month);
                    info[7] = getTwoDigit(day);

                    if (size > 8)
                        info[8] = locationText;

                    mListener.updateTransaction(information, info);
                    mListener.closeFragment();
                }
                else
                    Toast.makeText(v.getContext(), "You haven't filled out everything yet.", Toast.LENGTH_LONG).show();
                break;
            case "date_button":
                mListener.openDatePicker();
                break;
            case "category_button":
                mListener.openCategoryPicker();
                break;
            case "delete_button":
                mListener.deleteTransaction(information);
                mListener.closeFragment();
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

    public interface OnFragmentInteractionListener {
        String[] getInformation();
        String getType();
        void openDatePicker();
        void openCategoryPicker();
        void updateTransaction(String[] oldInfo, String[] newInfo);
        void deleteTransaction(String[] info);
        void closeFragment();
        void hideButtons();
        void resetDetailInformation();
    }
}
