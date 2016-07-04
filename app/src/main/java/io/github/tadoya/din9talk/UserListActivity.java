package io.github.tadoya.din9talk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import io.github.tadoya.din9talk.BaseActivity;
import io.github.tadoya.din9talk.ChattingActivity;
import io.github.tadoya.din9talk.R;
import io.github.tadoya.din9talk.StartActivity;
import io.github.tadoya.din9talk.firebaseui.FirebaseListAdapter;
import io.github.tadoya.din9talk.models.User;

public class UserListActivity extends BaseActivity {

    private static final String TAG = "UserList";
    DatabaseReference mDatabase;

    FirebaseListAdapter<User> mUserListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /**
         * Permission denied DB 문제 해결하기
         */
        ListView listView = (ListView)findViewById(R.id.user_list_View);

        // Set up FirebaseAdapter with the Query
        Query userQuery = getQuery(mDatabase);


        mUserListAdapter = new FirebaseListAdapter<User>(this, User.class, R.layout.single_active_list, userQuery) {
            @Override
            protected void populateView(View v, final User model, final int position) {

                final String toToken = model.getToken();
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                        intent.putExtra(ChattingActivity.TO_TOKEN, toToken);
                        startActivity(intent);
                    }
                });

                TextView listNameView = (TextView) v.findViewById(R.id.text_view_list_name);
                listNameView.setText(model.getUserName());
            }
        };
        listView.setAdapter(mUserListAdapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserListAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getEmail()+ "Signed out..", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, StartActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Query getQuery(DatabaseReference databaseReference) {
        Query usersQuery = databaseReference.child("users")
                .limitToFirst(100);
        // [END recent_posts_query]

        return usersQuery;
    }
}
