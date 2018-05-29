package com.nsbm.shashik.news;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "Activity2";
    private ProgressDialog mProgressDialog;
    private DocumentSnapshot lastResult;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final View msg_vie = findViewById(R.id.msg_view);

//        readData(db, msg_vie);
        loadNews();
        Button btn = msg_vie.findViewById((R.id.more_button));
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                requestMore();
                loadNews();

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void sendScroll() {
        final ScrollView scrollView = findViewById(R.id.scroll_layout);

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }).start();
    }

    public void requestMore() {
        LinearLayout msgLayout = findViewById(R.id.msg_layout);

        TextView msg1 = new TextView(getApplicationContext());
        int i = 1000;
        msg1.setId(i++);
        msg1.setPadding((int) (20 * getApplicationContext().getResources().getDisplayMetrics().density),
                (int) (8 * getApplicationContext().getResources().getDisplayMetrics().density),
                (int) (20 * getApplicationContext().getResources().getDisplayMetrics().density),
                (int) (8 * getApplicationContext().getResources().getDisplayMetrics().density));
        msg1.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins((int) (250 * getApplicationContext().getResources().getDisplayMetrics().density),
                (int) (10 * getApplicationContext().getResources().getDisplayMetrics().density),
                (int) (10 * getApplicationContext().getResources().getDisplayMetrics().density),
                0);
        params.width = (int) (100 * getApplicationContext().getResources().getDisplayMetrics().density);
        msg1.setLayoutParams(params);
//                                msg1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        msg1.setTextColor(getResources().getColor(R.color.white));
        msg1.setBackgroundResource(R.drawable.req_msg_view);
        msg1.setText("Give me some");
        msgLayout.addView(msg1);
    }

    public void loadNews() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("News are coming........");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        Query query;
        if (lastResult == null) {
            query = db.collection("News")
                    .limit(2);
        } else {
            query = db.collection("News")
                    .startAfter(lastResult)
                    .limit(2);
        }

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        mProgressDialog.dismiss();

                        LinearLayout msgLayout = findViewById(R.id.msg_layout);
                        int i = 0;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            TextView msg1 = new TextView(getApplicationContext());
//
                            msg1.setId(i++);
                            msg1.setPadding((int) (20 * getApplicationContext().getResources().getDisplayMetrics().density),
                                    (int) (8 * getApplicationContext().getResources().getDisplayMetrics().density),
                                    (int) (20 * getApplicationContext().getResources().getDisplayMetrics().density),
                                    (int) (8 * getApplicationContext().getResources().getDisplayMetrics().density));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins((int) (15 * getApplicationContext().getResources().getDisplayMetrics().density),
                                    (int) (10 * getApplicationContext().getResources().getDisplayMetrics().density),
                                    (int) (20 * getApplicationContext().getResources().getDisplayMetrics().density),
                                    0);
                            params.width = (int) (300 * getApplicationContext().getResources().getDisplayMetrics().density);
                            msg1.setLayoutParams(params);
//                                msg1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                            msg1.setTextColor(getResources().getColor(R.color.white));
                            msg1.setBackgroundResource(R.drawable.msg_view);
                            msg1.setText(documentSnapshot.get("Summary").toString());
                            msgLayout.addView(msg1);
                            sendScroll();

                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());


                        }

                        if (queryDocumentSnapshots.size() > 0) {


                            lastResult = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
