package com.example.cristian.mentorme.ViewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cristian.mentorme.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewHolder extends RecyclerView.ViewHolder
{
    public CircleImageView profileImage;
    public TextView message;
    public ConstraintLayout mConstr;


    public MessageViewHolder(View itemView)
    {
        super(itemView);

        profileImage = (CircleImageView) itemView.findViewById(R.id.message_image);
        message = (TextView) itemView.findViewById(R.id.message_text);
        mConstr = (ConstraintLayout) itemView.findViewById(R.id.single_message_layout_1);
    }
}
