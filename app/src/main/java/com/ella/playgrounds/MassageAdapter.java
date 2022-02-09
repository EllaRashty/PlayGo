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

import java.util.List;

public class MassageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private final Context context;
    private final List<Message> massageList;
    private final String currentUid;

    public MassageAdapter(Context context, List<Message> massageList, String currentUid) {
        this.context = context;
        this.massageList = massageList;
        this.currentUid = currentUid;
    }

    @Override
    public int getItemViewType(int position) {
        Message massage = massageList.get(position);
        if (massage.getUser().getUid().equals(currentUid))
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        else
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.my_msg_chat, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            View view = LayoutInflater.from(context).inflate(R.layout.other_msg_chat, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message massage = massageList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_SENT)
            ((SentMessageHolder) holder).bind(massage);
        else if (holder.getItemViewType() == VIEW_TYPE_MESSAGE_RECEIVED)
            ((ReceivedMessageHolder) holder).bind(massage);
    }

    @Override
    public int getItemCount() {
        return massageList.size();
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView my_massage, my_sendTime;

        SentMessageHolder(View itemView) {
            super(itemView);
            my_massage = (TextView) itemView.findViewById(R.id.my_massage);
            my_sendTime = (TextView) itemView.findViewById(R.id.me_LBL_time);
        }

        void bind(Message message) {
            my_massage.setText(message.getMessage());
            my_sendTime.setText(message.getTime());
        }
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView other_massage, other_sendTime, other_name;
        ImageView other_img;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            other_massage = (TextView) itemView.findViewById(R.id.other_massages_TV);
            other_sendTime = (TextView) itemView.findViewById(R.id.other_sendTime);
            other_name = (TextView) itemView.findViewById(R.id.other_name);
            other_img = (ImageView) itemView.findViewById(R.id.other_img);
        }

        void bind(Message message) {
            other_massage.setText(message.getMessage());
            if (message.getUser().getImageUrl() != null) {
                Glide.with(context)
                        .load(message.getUser().getImageUrl())
                        .into(other_img);
            }
            other_sendTime.setText(message.getTime());
            other_name.setText(message.getUser().getAdultName());
        }
    }
}