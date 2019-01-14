package com.example.cristian.mentorme.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.cristian.mentorme.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder
{
    public CircleImageView profilePicture;
  public  TextView lastMessage;
   public TextView fullName;
   public TextView date;
   public View mView;

    public ChatViewHolder(View itemView)
    {
        super(itemView);

        mView = itemView;

        profilePicture = (CircleImageView) itemView.findViewById(R.id.single_chat_picture);
        lastMessage = (TextView) itemView.findViewById(R.id.single_chat_last_text);
        fullName = (TextView) itemView.findViewById(R.id.single_chat_textView);
        date = (TextView) itemView.findViewById(R.id.single_chat_date);

    }
}
