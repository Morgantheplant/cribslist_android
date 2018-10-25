package com.codepath.cribslist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.cribslist.R;
import com.codepath.cribslist.adapters.ItemAdapter;
import com.codepath.cribslist.models.Item;
import com.codepath.cribslist.models.User;
import com.codepath.cribslist.network.CribslistClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AccountActivity extends AppCompatActivity {
    private static final String TITLE_TEXT = "My Account";
    int numberOfColumns = 2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Item> items;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(TITLE_TEXT);

        mRecyclerView = findViewById(R.id.rvItems);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        items = new ArrayList<>();
        mAdapter = new ItemAdapter(items);
        mRecyclerView.setAdapter(mAdapter);

        loadAccount();
    }

    private void loadAccount() {
        CribslistClient.getAccountDetail(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                try {
                    user = new User(response);
                    items.addAll(user.getItems());
                    mAdapter.notifyDataSetChanged();

                    ImageView iv = findViewById(R.id.ivProfileImage);
                    TextView tvName = findViewById(R.id.tvName);
                    TextView tvEmail = findViewById(R.id.tvEmail);

                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_stroller_24dp)
                            .error(R.drawable.ic_stroller_24dp);

                    Glide.with(AccountActivity.this)
                            .load(user.getUserPhotoURL())
                            .transition(withCrossFade())
                            .apply(options)
                            .into(iv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("DEBUG", user.toString());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }
}