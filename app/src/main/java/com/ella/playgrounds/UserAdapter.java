package com.ella.playgrounds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final List<User> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final Context context;
    private User user;

    // data is passed into the constructor
    public UserAdapter(Context context, List<User> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_adapter, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        user = mData.get(position);
        holder.user_name.setText(user.getAdultName());
        if (user.getStatus().equals(context.getString(R.string.online)))
            holder.user_status.setImageResource(android.R.drawable.presence_online);
        else if (user.getStatus().equals(context.getString(R.string.offline)))
            holder.user_status.setImageResource(android.R.drawable.presence_busy);
        showUserImage(holder);
    }

    private void showUserImage(ViewHolder holder) {
        if (user.getImageUrl() != null) {
            Glide.with(context)
                    .load(user.getImageUrl())
                    .into(holder.user_IMG);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    User getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    //Holds variables in a View
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_status;
        TextView user_name;
        ShapeableImageView user_IMG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_status = itemView.findViewById(R.id.user_status);
            user_name = itemView.findViewById(R.id.user_name_LBL);
            user_IMG = itemView.findViewById(R.id.user_IMG);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

}
