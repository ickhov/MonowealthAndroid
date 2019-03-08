/*
    This class sets up the view that shows
    the category lists when the user pressed
    "Pick a category" in TransactionSaveFragment
    and TransactionUpdateFragment
 */

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CategoryPickerFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private Context context;
    private int width, height;
    private PlainTextFile textFile;
    private ArrayList<String> categories;
    private String type;
    private LinearLayout top;
    private ImageButton add;
    private MediumTextView title;
    private RecyclerView items;
    private CategoryPickerAdapter adapter;

    public CategoryPickerFragment() {
        // Required empty public constructor
    }

    public static CategoryPickerFragment newInstance() {
        CategoryPickerFragment fragment = new CategoryPickerFragment();
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
        View view = inflater.inflate(R.layout.fragment_category_picker, container, false);

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
        top = view.findViewById(R.id.fragment_category_picker_top);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, (int)(height * 0.15));
        top.setLayoutParams(layoutParams);

        setTitle(view);
        setAdd(view);
    }

    private void setTitle(View view)
    {
        title = view.findViewById(R.id.fragment_category_picker_title);
        title.setTextSize(width / 33);
    }

    private void setAdd(View view)
    {
        add = view.findViewById(R.id.fragment_category_picker_add);
        add.setTag("add_button");
        add.setOnClickListener(this);
    }

    private void setItems(View view)
    {
        items = view.findViewById(R.id.fragment_category_picker_recycler_view);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, (int)(height * 0.85));
        layoutParams.addRule(RelativeLayout.BELOW, R.id.fragment_category_picker_top);
        items.setLayoutParams(layoutParams);

        LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper
                (view.getContext(), LinearLayoutManager.VERTICAL, false);
        items.setLayoutManager(layoutManager);

        categories = new ArrayList<>();

        try {
            categories.addAll(textFile.readCategory("current", type));
        } catch (IOException error) {

        }

        adapter = new CategoryPickerAdapter(context, categories);
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
        type = mListener.getType();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // insert to current category
    public void insert(String info)
    {

        try {
            // write info to category file in storage
            boolean categoryExistInAll = textFile.readCategory("all", type).contains(info);
            boolean categoryExistInCurrent = categories.contains(info);

            if (categoryExistInCurrent) {
                Toast.makeText(context,
                        "This category already exists.", Toast.LENGTH_LONG).show();
            } else {
                if (!categoryExistInAll) {
                    textFile.writeCategory("all", type, info);
                    mListener.updateDataAdapterCategories();
                }

                textFile.writeCategory("current", type, info);
                categories.add(info);
                Collections.sort(categories);
                int position = categories.indexOf(info);
                adapter.notifyItemInserted(position);
                adapter.notifyItemRangeChanged(position, categories.size() - position);
            }
        } catch (IOException error) {
            Toast.makeText(context,
                    "There was a problem adding the new category\nPlease try again later.", Toast.LENGTH_LONG).show();
        }
    }

    // remove from current category
    public void remove(String info)
    {
        try {
            // delete category from storage
            int position = categories.indexOf(info);
            textFile.deleteCategory("current", type, info, categories);
            categories.remove(info);
            adapter.notifyItemRemoved(position);
        } catch (IOException error) {
            Toast.makeText(context,
                    "There was a problem deleting the category\nPlease try again later.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString())
        {
            case "add_button":
                mListener.openCategoryPickerDialog();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        String getType();
        void openCategoryPickerDialog();
        void updateDataAdapterCategories();
        void hideButtons();
    }
}
