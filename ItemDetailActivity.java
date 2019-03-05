package com.example.harish.lets_bake;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.harish.lets_bake.BakeModel.Step;

import java.util.ArrayList;


public class ItemDetailActivity extends Activity implements View.OnClickListener {

    public static final String Thumbnail = "thumbnailurl";
    public static final String VideoUrl = "VideoUrl";
    public static final String Description = "description";
    public static final String Short_Description = "short_desc";
    ArrayList<Step> mlist;
    int position;
    String videourl, desc, shortdescription, thumbnail;
    Button button_next;
    Button button_previous;
    Toolbar toolbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        button_next = findViewById(R.id.next);
        button_previous = findViewById(R.id.previous);
        button_previous.setEnabled(false);
        toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        position = getIntent().getIntExtra("pos", 0);
        mlist = new ArrayList<>();
        thumbnail = getIntent().getStringExtra(Thumbnail);
        videourl = getIntent().getStringExtra(VideoUrl);
        shortdescription = getIntent().getStringExtra(Short_Description);
        desc = getIntent().getStringExtra(Description);
        mlist = (ArrayList<Step>) getIntent().getSerializableExtra("desList");


        if (position > 0) {
            button_previous.setEnabled(true);

        }
        if (position == mlist.size() - 1) {
            button_next.setEnabled(false);
        }

        button_previous.setOnClickListener(this);
        button_next.setOnClickListener(this);

        if (savedInstanceState != null) {

            thumbnail = savedInstanceState.getString(Thumbnail);
            shortdescription = savedInstanceState.getString(Short_Description);
            videourl = savedInstanceState.getString(VideoUrl);
            desc = savedInstanceState.getString(Description);
        } else {
            Bundle arguments = new Bundle();
            arguments.putString(VideoUrl, videourl);
            arguments.putString(Description, desc);
            arguments.putString(Thumbnail, thumbnail);
            arguments.putString(Short_Description, shortdescription);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(VideoUrl, videourl);
        outState.putString(Description, desc);
        outState.putString(Thumbnail, thumbnail);
        outState.putString(Short_Description, shortdescription);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.previous:
                position--;
                button_next.setEnabled(true);
                if (position == 0) {
                    button_previous.setEnabled(false);
                }


                desc = mlist.get(position).getDescription();
                videourl = mlist.get(position).getVideoURL();
                shortdescription = mlist.get(position).getShortDescription();
                thumbnail = mlist.get(position).getThumbnailURL();
                Bundle arguments = new Bundle();
                arguments.putString(VideoUrl, videourl);
                arguments.putString(Description, desc);
                arguments.putString(Short_Description, shortdescription);
                arguments.putString(Thumbnail, thumbnail);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();

                break;

            case R.id.next:
                button_previous.setEnabled(true);
                position++;
                if (position == mlist.size() - 1) {
                    button_next.setEnabled(false);
                } else {
                    button_next.setEnabled(true);
                }

                desc = mlist.get(position).getDescription();
                videourl = mlist.get(position).getVideoURL();
                shortdescription = mlist.get(position).getShortDescription();
                thumbnail = mlist.get(position).getThumbnailURL();
                Bundle arguments1 = new Bundle();
                arguments1.putString(VideoUrl, videourl);
                arguments1.putString(Description, desc);
                arguments1.putString(Short_Description, shortdescription);
                arguments1.putString(Thumbnail, thumbnail);
                ItemDetailFragment fragment1 = new ItemDetailFragment();
                fragment1.setArguments(arguments1);
                getFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment1)
                        .commit();
                break;
        }


    }
}
