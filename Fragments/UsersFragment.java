package com.example.cristian.mentorme.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cristian.mentorme.JavaClasses.User;
import com.example.cristian.mentorme.MainActivity;
import com.example.cristian.mentorme.R;
import com.example.cristian.mentorme.UserProfileActivity;
import com.example.cristian.mentorme.ViewHolder.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */

public class UsersFragment extends Fragment
{

    private RecyclerView mRecyclerView;

    private    DatabaseReference mUserReference;
    private    FirebaseRecyclerOptions<User> options;
    private    FirebaseRecyclerAdapter<User, UsersViewHolder> adapter;
    private    FirebaseAuth mAuth;


    public UsersFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

      View view = inflater.inflate(R.layout.fragment_users, container, false);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.users_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mUserReference = FirebaseDatabase.getInstance().getReference().child("Users");

        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(mUserReference, User.class).build();

        adapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final UsersViewHolder holder, final int position, @NonNull User model)
            {

                Picasso.with(getContext()).load(model.getImage()).into(holder.mCircleImageView);
                holder.mFullName.setText(model.getFirstname() + " " + model.getSecondname());
                holder.mStatus.setText(model.getStatus());

                holder.mButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String UID_2 = getRef(position).getKey();

                        Toast.makeText(getContext(), UID_2, Toast.LENGTH_SHORT).show();

                        Intent userProfile = new Intent(getContext(), UserProfileActivity.class);
                        userProfile.putExtra("Class","UserFragment");
                        userProfile.putExtra("UID_2", UID_2);
                        startActivity(userProfile);

                    }
                });

            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_view,parent, false);

                return new UsersViewHolder(view);
            }

        };





        GridLayoutManager grid = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(grid);
        adapter.startListening();
        mRecyclerView.setAdapter(adapter);






       return view;
    }


}
