package com.example.cristian.mentorme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.mentorme.JavaClasses.Offer;
import com.example.cristian.mentorme.JavaClasses.User;
import com.example.cristian.mentorme.ViewHolder.OfferViewHolder;
import com.example.cristian.mentorme.ViewHolder.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity
{
    private Toolbar mToolbar;

    //User Info
    private CircleImageView mProfilePicture;
    private TextView mFullName;
    private TextView mRole;
    private TextView mSpecialization;
    private TextView mSemester;
    private TextView mBio;


    //User offer's list and send message
    private ImageButton mSendMessage;
    private RecyclerView mRecyclerView;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    //FIREBASE UI
    private    DatabaseReference mUserReference;
    private FirebaseRecyclerOptions<Offer> options;
    private FirebaseRecyclerAdapter<Offer, OfferViewHolder> adapter;


    //TARGET USER UID
    private String targer_UID;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mProfilePicture = (CircleImageView) findViewById(R.id.user_profile_image);
        mFullName = (TextView) findViewById(R.id.user_profile_name);
        mSpecialization = (TextView) findViewById(R.id.user_profile_spec);
        mSemester = (TextView) findViewById(R.id.user_profile_semester);
        mBio = (TextView) findViewById(R.id.user_profile_bio);
        mRole = (TextView) findViewById(R.id.user_profile_role);
        mSendMessage = (ImageButton) findViewById(R.id.user_profile_send_message);


        String className = getIntent().getStringExtra("Class");

        if(className.equals("Drawer"))
        {
            String UID_1 = getIntent().getStringExtra("UID_1");
            setTarger_UID(UID_1);
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID_1);
            mUserReference = FirebaseDatabase.getInstance().getReference().child("Offer-" + UID_1);

            if(currentUser.getUid().equals(UID_1))
            {
                mSendMessage.setVisibility(View.INVISIBLE);
            }

        }

        if(className.equals("UserFragment"))
        {
            String UID_2 = getIntent().getStringExtra("UID_2");
            setTarger_UID(UID_2);
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID_2);
            mUserReference = FirebaseDatabase.getInstance().getReference().child("Offer-" + UID_2);
          if(currentUser.getUid().equals(UID_2))
            {
                mSendMessage.setVisibility(View.INVISIBLE);
            }
        }

        if(className.equals("Offers"))
        {
            String UID_3 = getIntent().getStringExtra("UID_3");
            setTarger_UID(UID_3);
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID_3);
            mUserReference = FirebaseDatabase.getInstance().getReference().child("Offer-" + UID_3);
            if(currentUser.getUid().equals(UID_3))
            {
                mSendMessage.setVisibility(View.INVISIBLE);
            }

        }

        mSendMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
                chatIntent.putExtra("Class", "UserProfile");
                chatIntent.putExtra("target_uid", getTarger_UID());
                startActivity(chatIntent);
            }
        });


        //Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.userProfile_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Offer>().setQuery(mUserReference, Offer.class).build();

        adapter = new FirebaseRecyclerAdapter<Offer, OfferViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull OfferViewHolder holder, int position, @NonNull Offer model)
            {

                holder.offerFullName.setText(model.getFirstName());
                holder.offerUserRole.setText(model.getRole());
                holder.offerTitle.setText(model.getTitle());
                holder.offerDescription.setText(model.getTitle());
                holder.offerDeadline.setText("Deadline: "+model.getDeadline().getDay() + "/" +
                        model.getDeadline().getMonth()+ "/" + model.getDeadline().getYear());
                holder.offerPrice.setText("Price: " + model.getPrice() + " DKK");

                Picasso.with(getApplicationContext()).load(model.getImageURI()).into(holder.offerUserImage);
                holder.offerProfile.setVisibility(View.INVISIBLE);


            }

            @NonNull
            @Override
            public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_single_view, parent, false);

                return new OfferViewHolder(view);
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter.startListening();
        mRecyclerView.setAdapter(adapter);




        mToolbar = (Toolbar) findViewById(R.id.user_profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("User profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






    }

    @Override
    protected void onStart()
    {
        super.onStart();
        adapter.startListening();


        mDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String role = dataSnapshot.child("status").getValue().toString();
                mRole.setText(role);
                String firstName = dataSnapshot.child("firstname").getValue().toString();
                String secondName  = dataSnapshot.child("secondname").getValue().toString();
                mFullName.setText(firstName + " " + secondName);
                String specialization = dataSnapshot.child("specialization").getValue().toString();
                mSpecialization.setText(specialization);
                String semester = dataSnapshot.child("semester").getValue().toString();
                mSemester.setText("Semester: " + semester);
                String bio = dataSnapshot.child("biography").getValue().toString();
                mBio.setText(bio);
                String URI = dataSnapshot.child("image").getValue().toString();
                Picasso.with(getApplicationContext()).load(URI).into(mProfilePicture);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


    }

    @Override
    protected void onStop()
    {
        super.onStop();



    }


    public String getTarger_UID()
    {
        return targer_UID;
    }

    public void setTarger_UID(String targer_UID)
    {
        this.targer_UID = targer_UID;
    }
}
