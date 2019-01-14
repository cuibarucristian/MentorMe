package com.example.cristian.mentorme.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cristian.mentorme.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfferViewHolder extends RecyclerView.ViewHolder
{
    public CircleImageView offerUserImage;
    public TextView offerFullName;
    public TextView offerUserRole;
    public TextView offerTitle;
    public TextView offerDescription;
    public TextView offerPrice;
    public TextView offerDeadline;
    public Button offerProfile;


    public OfferViewHolder(View itemView)
    {
        super(itemView);

        offerUserImage = (CircleImageView) itemView.findViewById(R.id.offer_user_image);
        offerFullName = (TextView) itemView.findViewById(R.id.offer_user_full_name);
        offerUserRole = (TextView)itemView.findViewById(R.id.offer_user_role);
        offerTitle = (TextView) itemView.findViewById(R.id.offer_user_title);
        offerDescription = (TextView) itemView.findViewById(R.id.offer_user_offer_description);
        offerPrice = (TextView) itemView.findViewById(R.id.offer_user_price);
        offerDeadline = (TextView) itemView.findViewById(R.id.offer_user_deadline);
        offerProfile = (Button) itemView.findViewById(R.id.offer_single_view_button_profile);
    }
}
