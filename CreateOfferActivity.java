package com.example.cristian.mentorme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.mentorme.JavaClasses.Offer;
import com.example.cristian.mentorme.JavaClasses.User;
import com.example.cristian.mentorme.JavaClasses.myDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateOfferActivity extends AppCompatActivity
{








    private Toolbar mToolbar;

    private TextView mTitle;
    private EditText mDescription;
    private EditText mPrice;
    private CalendarView mCalendarView;
    private Button mButton;

    //Firebase
    private DatabaseReference mMainOffer;
    private DatabaseReference mUserOffer;
    private DatabaseReference mUserData;
    private FirebaseAuth mAuth;


    private int OFFER_NUMBER_MAIN =0;
    private int OFFER_NUMBER_USER =0;

    //Simple vars
    private String firstName;
    private String secondName;
    private String image;
    private String role;
    private myDate myDate;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        mAuth = FirebaseAuth.getInstance();


        mToolbar = (Toolbar) findViewById(R.id.create_offer_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add offer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mTitle = (TextView) findViewById(R.id.create_offer_title);
        mDescription = (EditText) findViewById(R.id.create_offer_description_1);
        mPrice = (EditText) findViewById(R.id.create_offer_price);
        mCalendarView = (CalendarView) findViewById(R.id.create_offer_calendar);
        mButton = (Button) findViewById(R.id.create_offer_save);

        CalendarView calendarView = mCalendarView;

        //working
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day)
            {
                month++;
                myDate myDate = new myDate(day, month, year);
                setMyDate(myDate);
                Toast.makeText(getApplicationContext(), getMyDate().getDay() + "/" + getMyDate().getMonth() + "/" + getMyDate().getYear(), Toast.LENGTH_SHORT).show();


            }
        });

        setOFFER_NUMBER_MAIN();
        setOFFER_NUMBER_USER();

        getAndSetUserData(); //working

        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createOffer();
                createOfferForProfile();
            }
        });




    }


    public void createOfferForProfile()
    {
        FirebaseUser user =mAuth.getCurrentUser();

        mUserOffer = FirebaseDatabase.getInstance().getReference().child("Offer-"+user.getUid() ).child(OFFER_NUMBER_USER +"");

        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        int  price = Integer.parseInt(mPrice.getText().toString());
        myDate = getMyDate();
        String UID = user.getUid();

        Offer offer = new Offer(title, description, price, getFirstName(), getSecondName(), getImage(), getRole(), getMyDate(), UID);

        mUserOffer.setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    ++OFFER_NUMBER_USER;
                    Toast.makeText(getApplicationContext(), "Offer added.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



    public void createOffer()
    {
        FirebaseUser user =mAuth.getCurrentUser();

        mMainOffer = FirebaseDatabase.getInstance().getReference().child("Offers").child(OFFER_NUMBER_MAIN +"");

        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        int  price = Integer.parseInt(mPrice.getText().toString());
        myDate = getMyDate();

        Offer offer = new Offer(title, description, price, getFirstName(), getSecondName(), getImage(), getRole(), getMyDate(), user.getUid());

        mMainOffer.setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    ++OFFER_NUMBER_MAIN;
                    Toast.makeText(getApplicationContext(), "Offer added.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    public void getAndSetUserData()
    {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mUserData = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        mUserData.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                String firstName = dataSnapshot.child("firstname").getValue().toString();
                String secondName = dataSnapshot.child("secondname").getValue().toString();
                String imageURI = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                setFirstName(firstName);
                setSecondName(secondName);
                setImage(imageURI);
                setRole(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }


    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSecondName()
    {
        return secondName;
    }

    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }


    public myDate getMyDate()
    {
        return myDate;
    }

    public void setMyDate(myDate myDate)
    {
        this.myDate = myDate;
    }

    private void setOFFER_NUMBER_MAIN()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mMainOffer = FirebaseDatabase.getInstance().getReference().child("Offers");
        mMainOffer.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    OFFER_NUMBER_MAIN =(int) dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void setOFFER_NUMBER_USER()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mMainOffer = FirebaseDatabase.getInstance().getReference().child("Offer-"+user.getUid());
        mMainOffer.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    OFFER_NUMBER_USER =(int) dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


}
