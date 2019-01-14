package com.example.cristian.mentorme.ViewHolder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cristian.mentorme.ChatActivity;
import com.example.cristian.mentorme.JavaClasses.Message;
import com.example.cristian.mentorme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class MessageAdaptor extends RecyclerView.Adapter<MessageViewHolder>
{

    List<Message> messages ;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String URI;


    public MessageAdaptor(List<Message> messages)
    {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_1, parent, false);
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position)
    {

        String current_user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id).child("image").push();
        String ss = mRef.getKey();

        Message message = messages.get(position);

        String from_user = message.getFrom();

        if(from_user.equals(current_user_id))
        {

            holder.message.setBackgroundColor(Color.WHITE);
            holder.message.setTextColor(Color.BLACK);

        }else
        {
            holder.message.setBackgroundColor(Color.BLACK);
            holder.message.setTextColor(Color.WHITE);
        }

        holder.message.setText(message.getMessage());




    }





    @Override
    public int getItemCount()
    {
        return messages.size();
    }

    public String getURI()
    {
        return URI;
    }

    public void setURI(String URI)
    {
        this.URI = URI;
    }
}
