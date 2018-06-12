package com.example.anujsharma.firebasetesting;

import android.app.TaskStackBuilder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetailActivity extends AppCompatActivity {

    TextView detailTitle, detailDescription;
    ImageView detailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        //SetTitle
        actionBar.setTitle("PostDetail");
        // set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // initialized views
        detailTitle = findViewById(R.id.dPostTitle);
        detailDescription = findViewById(R.id.dPostDescription);
        detailImage = findViewById(R.id.dPostImage);

        //get data from intent
        byte[] bytes = getIntent().getByteArrayExtra("image");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        //set data to views
        detailTitle.setText(title);
        detailDescription.setText(desc);
        detailImage.setImageBitmap(bmp);


    }

    //handle onBack pressed(go back to previous activity)


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
