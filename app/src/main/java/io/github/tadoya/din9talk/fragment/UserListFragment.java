package io.github.tadoya.din9talk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import io.github.tadoya.din9talk.ChattingActivity;
import io.github.tadoya.din9talk.MainActivity;
import io.github.tadoya.din9talk.R;
import io.github.tadoya.din9talk.models.User;
import io.github.tadoya.din9talk.viewholder.MyViewHolder;

/**
 * Created by choiseongsik on 2016. 7. 5..
 */

public class UserListFragment extends ListFragment {

    private static final String TAG = "UserListFragment";
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<User, MyViewHolder> mAdapter;
    private LinearLayoutManager mManager;

    public UserListFragment() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "mDatabase set");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        Log.d(TAG, "mManager, mRecycler set");

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<User, MyViewHolder>(User.class, R.layout.recyclerview,
                MyViewHolder.class, postsQuery) {

            @Override
            protected void populateViewHolder(final MyViewHolder viewHolder, final User model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                Log.d(TAG, ""+postKey);

                final String toToken = model.getToken();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), ChattingActivity.class);
                        intent.putExtra(ChattingActivity.TO_TOKEN, toToken);
                        startActivity(intent);
                    }
                });
                viewHolder.bindToItem(model);

            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query usersQuery = databaseReference.child("users")
                .limitToFirst(100);
        // [END recent_posts_query]

        return usersQuery;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
}
