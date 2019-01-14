package com.example.cristian.mentorme.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cristian.mentorme.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersViewHolder extends RecyclerView.ViewHolder
{
    public CircleImageView mCircleImageView;
    public TextView mFullName;
    public TextView mStatus; //Role
    public Button mButton;
    View view;


    public UsersViewHolder(View itemView)
    {
        super(itemView);

        view = itemView;

        mCircleImageView = (CircleImageView) view.findViewById(R.id.users_single_view_image);
        mFullName = (TextView) view.findViewById(R.id.users_single_fullname);
        mStatus = (TextView) view.findViewById(R.id.users_single_status);
        mButton = (Button) view.findViewById(R.id.users_single_see_profile);

    }
}
