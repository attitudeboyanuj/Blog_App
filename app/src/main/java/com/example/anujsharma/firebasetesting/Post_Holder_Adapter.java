package com.example.anujsharma.firebasetesting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Post_Holder_Adapter extends RecyclerView.ViewHolder {
    View mView;


    public Post_Holder_Adapter(View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        // item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemlongClick(v, getAdapterPosition());
                return true;
            }
        });


    }

    public void setDetails(Context ctx, String title, String image, String description)
    {
        //views
        TextView mTitleView = mView.findViewById(R.id.PostTitle);
        TextView mDescriptionView = mView.findViewById(R.id.PostDescription);
        ImageView mImageView = mView.findViewById(R.id.PostImage);
        //set data to view
        mTitleView.setText(title);
        mDescriptionView.setText(description);
        Picasso.with(ctx).load(image).into(mImageView);



    }

    private Post_Holder_Adapter.ClickListener mClickListener;

    //interface to send callbacks
    public interface ClickListener
    {
        void onItemClick(View view, int Position);
        void onItemlongClick(View view, int Position);
    }

    public void setOnClickListener(Post_Holder_Adapter.ClickListener clickListener){
        mClickListener = clickListener;

    }

}
