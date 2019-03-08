/*
    This class sets up the recycler view adapter
    of CategoryPickerFragment.java
 */

package com.monowealth.monowealth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class CategoryPickerAdapter extends RecyclerView.Adapter<CategoryPickerAdapter.MyViewHolder>
{
    private OnAdapterInteractionListener mListener;
    private int[] colors;
    private int width, height;
    private ArrayList<String> categories;

    public CategoryPickerAdapter(Context context, ArrayList<String> categories)
    {
        mListener = (OnAdapterInteractionListener) context;
        colors = context.getResources().getIntArray(R.array.colors);
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        this.categories = categories;
        setHasStableIds(true);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layout;
        RegularButton submit;
        ImageButton delete;
        String label;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.adapter_category_picker);

            submit = itemView.findViewById(R.id.adapter_category_picker_submit);
            submit.setTag("submit_button");
            submit.setOnClickListener(this);

            delete = itemView.findViewById(R.id.adapter_category_picker_delete);
            delete.setTag("delete_button");
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getTag().toString())
            {
                case "submit_button":
                    mListener.returnCategory(label);
                    break;
                case "delete_button":
                    mListener.deleteCategory(label);
                    break;
            }
        }
    }

    @NonNull
    @Override
    public CategoryPickerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_category_picker, viewGroup, false);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, height / 10);

        view.setLayoutParams(layoutParams);

        return new CategoryPickerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryPickerAdapter.MyViewHolder myViewHolder, int i) {
        int index = myViewHolder.getAdapterPosition();
        myViewHolder.label = categories.get(index);
        myViewHolder.layout.setBackgroundColor(colors[index]);
        myViewHolder.submit.setText(myViewHolder.label);
    }

    @Override
    public int getItemCount() {
        return categories.size() ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnAdapterInteractionListener
    {
        void returnCategory(String category);
        void deleteCategory(String category);
    }
}
