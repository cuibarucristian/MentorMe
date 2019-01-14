package com.example.cristian.mentorme.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cristian.mentorme.ChatActivity;
import com.example.cristian.mentorme.JavaClasses.Chat;
import com.example.cristian.mentorme.R;
import com.example.cristian.mentorme.ViewHolder.ChatViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChatFragment extends Fragment
{

    //Firebase References
    private DatabaseReference mChatsReference;
    private DatabaseReference mMessagesReference;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private String currentUserId;


    FirebaseRecyclerOptions<Chat> options;
    FirebaseRecyclerAdapter<Chat, ChatViewHolder> adapter;
    RecyclerView mRecyclerView;

    public ChatFragment()
    {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
       View view = inflater.inflate(R.layout.fragment_chat, container, false);
       mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_chat_recycler_view);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        mChatsReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId);
        mChatsReference.keepSynced(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        mMessagesReference = FirebaseDatabase.getInstance().getReference().child("messages").child(currentUserId);


        options = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(mMessagesReference, Chat.class).build();

        adapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull Chat model)
            {

                final String list_user_id = getSnapshots().getSnapshot(position).getKey();
                holder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent goToChat = new Intent(getContext(), ChatActivity.class);
                        goToChat.putExtra("Class", "Chat");
                        goToChat.putExtra("target_user", list_user_id);
                        startActivity(goToChat);

                    }
                });


                Query lastMessageQuery = mMessagesReference.child(list_user_id).limitToLast(1);
                lastMessageQuery.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        String data = dataSnapshot.child("message").getValue().toString();
                        holder.lastMessage.setText(data);
                        Log.d("Message", data);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
                    {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        final String firstname = dataSnapshot.child("firstname").getValue().toString();
                        holder.fullName.setText(firstname);
                        String URI = dataSnapshot.child("image").getValue().toString();
                        Picasso.with(getActivity()).load(URI).into(holder.profilePicture);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });


            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chat_user,parent, false);

                return new ChatViewHolder(view);
            }
        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();


        return view;

    }

    @Override
    public void onStart()
    {
        super.onStart();

        adapter.startListening();

    }


    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}
