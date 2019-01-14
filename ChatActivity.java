package com.example.cristian.mentorme;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cristian.mentorme.JavaClasses.Message;
import com.example.cristian.mentorme.ViewHolder.MessageAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity
{

    private Toolbar mToolbar;
    private DatabaseReference mUsers;

    private RecyclerView mRecyclerView;
    private ImageButton mSendText;
    private EditText mEditText;

    private final List<Message> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdaptor mAdaptor;

    private DatabaseReference mChatRef;
    private FirebaseAuth mAuth;

    private String targetUID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        mSendText = (ImageButton)findViewById(R.id.chat_send_message);
        mEditText = (EditText) findViewById(R.id.chat_message);

        mAdaptor = new MessageAdaptor(messagesList);
        mLinearLayout = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayout);

        mRecyclerView.setAdapter(mAdaptor);


        String finalUID = getIntent().getStringExtra("Class");
        if(finalUID.equals("Chat"))
        {
            String uid = getIntent().getStringExtra("target_user");
            setTargetUID(uid);
        }
        if(finalUID.equals("UserProfile"))
        {
            String uid = getIntent().getStringExtra("target_uid");
            setTargetUID(uid);
        }

        changeName();


        mChatRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
       final FirebaseUser user = mAuth.getCurrentUser();

        getMessages();

        mChatRef.child("Chat").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(targetUID))
                {

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + user.getUid() + "/" + targetUID, chatAddMap);
                    chatUserMap.put("Chat/" + targetUID + "/" + user.getUid(), chatAddMap);

                    mChatRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener()
                    {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                        {
                            if(databaseError != null)
                            {
                                Log.d("Chat_LOG", databaseError.getMessage().toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });




        mSendText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendMessage();
            }
        });



    }


    private void getMessages()
    {
        mChatRef.child("messages").child(mAuth.getCurrentUser().getUid()).child(getTargetUID()).addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Message messages = dataSnapshot.getValue(Message.class);

                messagesList.add(messages);
                mAdaptor.notifyDataSetChanged();

                mRecyclerView.scrollToPosition(messagesList.size()-1);
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

    }

    private void sendMessage()
    {

        String message = mEditText.getText().toString();

        if(!TextUtils.isEmpty(message))
        {
            String current_user_ref = "messages/" + mAuth.getCurrentUser().getUid() + "/" + getTargetUID();
            String chat_user_ref = "messages/" + getTargetUID() + "/" + mAuth.getCurrentUser().getUid();

            DatabaseReference user_message_push = mChatRef.child("messages").child(mAuth.getCurrentUser().getUid()).child(getTargetUID()).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("from", mAuth.getCurrentUser().getUid());

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            mEditText.setText("");

            mChatRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener()
            {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                {

                    if(databaseError != null)
                    {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

    }


    @Override
    protected void onStart()
    {
        super.onStart();


    }

    private void changeName()
    {

        mUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(getTargetUID());

        mUsers.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String name = dataSnapshot.child("firstname").getValue().toString();
                getSupportActionBar().setTitle(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    public String getTargetUID()
    {
        return targetUID;
    }

    public void setTargetUID(String targetUID)
    {
        this.targetUID = targetUID;
    }
}
