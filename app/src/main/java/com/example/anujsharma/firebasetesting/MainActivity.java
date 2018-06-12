package com.example.anujsharma.firebasetesting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {



    LinearLayoutManager linearLayoutManager; // for sorting
    SharedPreferences sharedPreferences; //for saving sort settings
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        sharedPreferences = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = sharedPreferences.getString("Sort", "newest"); //where if no setting is selected newsest is default

        if(mSorting.equals("newest"))
        {
            linearLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means newest first
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);

        }

        else if (mSorting.equals("oldest"))
        {
            linearLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means oldest first
            linearLayoutManager.setReverseLayout(false);
            linearLayoutManager.setStackFromEnd(false);
        }


        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.main_item_list);
        mRecyclerView.setHasFixedSize(true);
        //Setting RecyclerView Layout
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //Send Query To DataBase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Data");



    }


    //search data
    private void firebaseSearch(String searchText){

        //convert string entered in searchview to lowercase
        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = mRef.orderByChild("search").startAt(query).endAt(query + "\uf8ff");
        FirebaseRecyclerAdapter<Post_Model_Class, Post_Holder_Adapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Post_Model_Class, Post_Holder_Adapter>(
                        Post_Model_Class.class,
                        R.layout.post_layout,
                        Post_Holder_Adapter.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(Post_Holder_Adapter viewHolder, Post_Model_Class model, int position) {
                        viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getImage(), model.getDescription());

                    }

                    @Override
                    public Post_Holder_Adapter onCreateViewHolder(ViewGroup parent, int viewType) {

                        Post_Holder_Adapter post_holder_adapter = super.onCreateViewHolder(parent, viewType);
                        post_holder_adapter.setOnClickListener(new Post_Holder_Adapter.ClickListener() {
                            @Override
                            public void onItemClick(View view, int Position) {
                                // views
                                TextView mtitleD = view.findViewById(R.id.PostTitle);
                                TextView mdescriptionD = view.findViewById(R.id.PostDescription);
                                ImageView mimageD = view.findViewById(R.id.PostImage);

                                // get data from views
                                String mTitle = mtitleD.getText().toString();
                                String mDescription = mdescriptionD.getText().toString();
                                Drawable drawable = mimageD.getDrawable();
                                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
                                byte[] bytes = bs.toByteArray();
                                intent.putExtra("image", bytes); //put bitmap image as array bytes
                                intent.putExtra("title", mTitle); //put title
                                intent.putExtra("description", mDescription); //put description
                                startActivity(intent); //start activity

                            }

                            @Override
                            public void onItemlongClick(View view, int Position) {
                                //TODO do your own implementation on long item click

                            }
                        });

                        return post_holder_adapter;
                    }


                };
        // set adapter to recycler view
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    //Load Data Into RecyclerView OnStart
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post_Model_Class, Post_Holder_Adapter> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Post_Model_Class, Post_Holder_Adapter>(
                        Post_Model_Class.class,
                        R.layout.post_layout,
                        Post_Holder_Adapter.class,
                        mRef) {

                    @Override
                    protected void populateViewHolder(Post_Holder_Adapter viewHolder, Post_Model_Class model, int position) {
                        viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getImage(), model.getDescription());
                    }

                    @Override
                    public Post_Holder_Adapter onCreateViewHolder(ViewGroup parent, int viewType) {

                        Post_Holder_Adapter post_holder_adapter = super.onCreateViewHolder(parent, viewType);
                        post_holder_adapter.setOnClickListener(new Post_Holder_Adapter.ClickListener() {

                            @Override
                            public void onItemClick(View view, int Position) {
                                    // views
                                    TextView mtitleD = view.findViewById(R.id.PostTitle);
                                    TextView mdescriptionD = view.findViewById(R.id.PostDescription);
                                    ImageView mimageD = view.findViewById(R.id.PostImage);

                                    // get data from views
                                    String mTitle = mtitleD.getText().toString();
                                    String mDescription = mdescriptionD.getText().toString();
                                    Drawable drawable = mimageD.getDrawable();
                                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                                    //pass this data to new activity
                                    Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
                                    byte[] bytes = bs.toByteArray();
                                    intent.putExtra("image", bytes); //put bitmap image as array bytes
                                    intent.putExtra("title", mTitle); //put title
                                    intent.putExtra("description", mDescription); //put description
                                    startActivity(intent); //start activity

                                }

                            @Override
                            public void onItemlongClick(View view, int Position) {
                                //TODO do your own implementation on long item click

                            }
                        });

                        return post_holder_adapter;
                    }



                };


            // set adapter to recycler view
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it present
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter as you type
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    // handle other action bar item clicks here
    if (id == R.id.action_sort){
        //display alert dialog to choose sorting
        showSortDialog();
        return true;
    }

    return super.onOptionsItemSelected(item);
}

    private void showSortDialog() {
        //options to display in dialog
        String[] sortOptions = {"Newest", "Oldest"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By") //set Ttile
                .setIcon(R.drawable.ic_action_sort) //set icon
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the 'which' argument contains the index position of selected item
                        // 0 means 'newest' and 1 means 'oldest'
                        if (which==0)
                        {
                            // sort by newest
                            // edit our shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "newest"); //where 'sort' is the key and 'newest' is the value
                            editor.apply(); //save the value in our shared preferences
                            recreate(); //restart activity take effect
                        }

                        else if (which==1)
                        {
                            // sort by oldest
                            // edit our shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Sort", "oldest"); //where 'sort' is the key and 'oldest' is the value
                            editor.apply(); //save the value in our shared preferences
                            recreate(); //restart activity take effect
                        }

                    }
                });
        builder.show();
    }

    //it will handle exit functionality
    boolean twice;
    @Override
    public void onBackPressed() {

        if(twice == true)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }

        Toast.makeText(MainActivity.this, "Press Once More To Exit", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        },3000);
        twice = true;
    }
}
