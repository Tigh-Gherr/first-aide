package com.firstadie.csftcarroll.b00641329.firstaide.ui.WeatherActivity;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstadie.csftcarroll.b00641329.firstaide.R;

import java.util.List;

/**
 * Created by tigh on 12/11/17.
 */

public class ClothingRecyclerViewAdapter extends RecyclerView.Adapter<ClothingRecyclerViewAdapter.ClothingViewHolder> {

    List<Clothing> mClothes;

    public ClothingRecyclerViewAdapter(List<Clothing> clothes) {
        mClothes = clothes;
    }

    @Override
    public ClothingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_clothing, parent, false);

        return new ClothingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClothingViewHolder holder, int position) {
        Clothing item = mClothes.get(position);
        String title = item.getTitle();
        int imageRes = item.getIcon();

        holder.mClothingTitleTextView.setText(title);
        holder.mClothingIconImageView.setImageResource(imageRes);
    }

    @Override
    public int getItemCount() {
        return mClothes != null ? mClothes.size() : 0;
    }

    public class ClothingViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView mClothingTitleTextView;
        private AppCompatImageView mClothingIconImageView;

        public ClothingViewHolder(View itemView) {
            super(itemView);

            mClothingTitleTextView = itemView.findViewById(R.id.textview_clothing);
            mClothingIconImageView = itemView.findViewById(R.id.imageview_clothing);
        }
    }
}
