package com.example.cristian.mentorme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.mentorme.Fragments.ChatFragment;
import com.example.cristian.mentorme.Fragments.OffersFragment;
import com.example.cristian.mentorme.Fragments.PagerAdapter;
import com.example.cristian.mentorme.Fragments.UsersFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
{

   //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mImageReference;

    //TabLayout
    private Toolbar mToolbar;

    //Application layout elements
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;

    //Navigation view
    private CircleImageView navHeaderImage;
    private NavigationView mNavigationView;
    private TextView navHeaderEmail;

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("MentorMe");


        //All elements related to the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        navHeaderImage = (CircleImageView)mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_image);
        navHeaderEmail = (TextView)mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_email);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.personal_profile:

                       Intent userProfile = new Intent(MainActivity.this, UserProfileActivity.class);
                        userProfile.putExtra("Class","Drawer");
                        userProfile.putExtra("UID_1", user.getUid());
                        startActivity(userProfile);


                        Toast.makeText(getApplicationContext(), "Personal profile", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.personal_profile_settings:
                        Toast.makeText(getApplicationContext(), "Profile settings", Toast.LENGTH_SHORT).show();
                        break;
                }


                return true;
            }
        });



        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch(item.getItemId())
        {
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        super.onBackPressed();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
        {
           sendToStart();
        }


        String UID = user.getUid();
        mImageReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

        mImageReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String URI = dataSnapshot.child("image").getValue().toString();
               Picasso.with(getBaseContext()).load(URI).into(navHeaderImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        navHeaderEmail.setText(user.getEmail());

    }

    private void sendToStart()
    {
        Intent sendToRegister = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(sendToRegister);
        finish();
    }


    //test it
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.log_out, menu);

        return true;
    }





}
