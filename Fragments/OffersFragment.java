package com.example.cristian.mentorme.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cristian.mentorme.CreateOfferActivity;
import com.example.cristian.mentorme.JavaClasses.Offer;
import com.example.cristian.mentorme.JavaClasses.User;
import com.example.cristian.mentorme.R;
import com.example.cristian.mentorme.UserProfileActivity;
import com.example.cristian.mentorme.ViewHolder.OfferViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OffersFragment extends Fragment
{

    private FloatingActionButton mFab;

    private FirebaseRecyclerOptions<Offer> options;
    private FirebaseRecyclerAdapter<Offer, OfferViewHolder> adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mOfferReference;

    private RecyclerView offersRecyclerView;


    public OffersFragment()
    {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_offers, container, false);

        mAuth = FirebaseAuth.getInstance();
        offersRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_offers_recycler_view);
        offersRecyclerView.setHasFixedSize(true);


        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getContext(), CreateOfferActivity.class));
               //Snackbar.make(getView(), "sdad", 33).setAction("Action", null).show();
            }
        });


        mOfferReference = FirebaseDatabase.getInstance().getReference().child("Offers");
        options = new FirebaseRecyclerOptions.Builder<Offer>().setQuery(mOfferReference, Offer.class).build();

        adapter = new FirebaseRecyclerAdapter<Offer, OfferViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull OfferViewHolder holder, final int position, @NonNull final Offer model)
            {
                holder.offerFullName.setText(model.getFirstName() + " " + model.getSecondName());
                holder.offerUserRole.setText(model.getRole());
                holder.offerTitle.setText(model.getTitle());
                holder.offerDescription.setText(model.getTitle());
                holder.offerDeadline.setText("Deadline: "+model.getDeadline().getDay() + "/" +
                        model.getDeadline().getMonth()+ "/" + model.getDeadline().getYear());
                holder.offerPrice.setText("Price: " + model.getPrice() + " DKK");

                Picasso.with(getContext()).load(model.getImageURI()).into(holder.offerUserImage);

                holder.offerProfile.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        String UID_3 = model.getUid();

                        Toast.makeText(getContext(), UID_3, Toast.LENGTH_SHORT).show();

                        Intent userProfile = new Intent(getContext(), UserProfileActivity.class);
                        userProfile.putExtra("Class","Offers");
                        userProfile.putExtra("UID_3", UID_3);
                        startActivity(userProfile);

                    }
                });

            }

            @NonNull
            @Override
            public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_single_view, parent, false);

                return new OfferViewHolder(view);
            }
        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        offersRecyclerView.setLayoutManager(linearLayoutManager);
        adapter.startListening();
        offersRecyclerView.setAdapter(adapter);


        return view;
    }



}
