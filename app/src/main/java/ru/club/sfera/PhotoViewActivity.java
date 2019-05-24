package ru.club.sfera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ru.club.sfera.common.ActivityBase;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoViewActivity extends ActivityBase {

    Toolbar toolbar;

    ImageView mPhotoView;

    PhotoViewAttacher mAttacher;
    ProgressBar mProgressBar;

    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);

        Intent i = getIntent();

        imgUrl = i.getStringExtra("imgUrl");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mPhotoView = (ImageView) findViewById(R.id.photoImageView);

        getSupportActionBar().setTitle("");

        final ImageView imageView = mPhotoView;
        final ProgressBar progressView = mProgressBar;

        Picasso.with(getApplicationContext())
                .load(imgUrl)
                .into(mPhotoView, new Callback() {

                    @Override
                    public void onSuccess() {

                        progressView.setVisibility(View.GONE);

                        mAttacher = new PhotoViewAttacher(mPhotoView);
                    }

                    @Override
                    public void onError() {

                        progressView.setVisibility(View.GONE);
                        imageView.setImageResource(R.drawable.img_loading_error);
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home: {

                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }
}
