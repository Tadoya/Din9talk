package io.github.tadoya.din9talk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


public class StartActivity extends BaseActivity {

    private static final String TAG = "StartActivity";
    private FirebaseAuth mAuth;

    String userUid;
    static boolean isStaySignIn=true;
    static Button startButton;
    static Button signInButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startButton = (Button) findViewById(R.id.startButton);
        signInButton = (Button) findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();

        /*  익명인증테스트
        mAuth.signInAnonymously();
        userUid = mAuth.getCurrentUser().getUid();
        myRef.child(userUid).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
        */


        if (mAuth.getCurrentUser() != null) {
            signInButton.setText("Sign Out");
            signInButton.setBackgroundColor(Color.parseColor("#009688"));

            userUid = mAuth.getCurrentUser().getUid();
            userEmail = mAuth.getCurrentUser().getEmail();
            Log.d("signCycle","Start_currentuser's email:" + userEmail);
            Log.d("signCycle","Start_currentuser:"+ userUid);
        }

        if(signInButton.getText().equals("Sign Out")){
            getTokenatStart(userUid, userEmail);
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signInButton.getText().equals("Sign In")) {
                    Intent signIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(signIntent);
                }
                else{
                    reSign();
                }
            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent chattingIntent = new Intent(getApplicationContext(), ChattingActivity.class);
                Intent userListIntent = new Intent(getApplicationContext(), UserListActivity.class);
                //userListIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(userListIntent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isStaySignIn){
            Log.d("signCycle","destroy:"+isStaySignIn);
            FirebaseAuth.getInstance().signOut();
        }
    }


    /**
     * duplicated user check
     * @param userUid
     * @param userEmail
     */
    public void getTokenatStart(String userUid, final String userEmail){
        // Read a message from the database(Once)
        myRef.child(userUid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String DBToken = (String) dataSnapshot.getValue();
                if(FirebaseInstanceId.getInstance().getToken().equals(DBToken)) {
                    startButton.setEnabled(true);
                    Toast.makeText(StartActivity.this, "Hello!"+'\n'+userEmail, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(StartActivity.this, "Already, signing in..\n Please resign in!", Toast.LENGTH_LONG).show();
                    reSign();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"token reading error");
            }
        });
    }
    public void reSign(){
        startButton.setEnabled(false);
        signInButton.setText("Sign In");
        signInButton.setBackgroundColor(Color.parseColor("#80cbc4"));
        FirebaseAuth.getInstance().signOut();
    }
}
