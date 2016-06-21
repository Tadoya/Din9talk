package io.github.tadoya.din9talk;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
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


public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    private FirebaseAuth mAuth;

    static String Token="";
    static boolean isToken=false;
    static boolean isStaySignIn;
    static Button getTokenButton;
    static Button signInButton;

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getTokenButton = (Button) findViewById(R.id.getTokenButton);
        signInButton = (Button) findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();
        Log.d("stay","Start_currentuser:" + mAuth.getCurrentUser().getEmail());

        if (mAuth.getCurrentUser() != null) {
            signInButton.setText("Sign Out");
        }

        if(signInButton.getText().equals("Sign Out")){
            getTokenatStart();
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signInButton.getText().equals("Sign In")) {
                    Intent signIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(signIntent);
                }
                else{
                    signInButton.setText("Sign In");
                    signInButton.setBackgroundColor(Color.parseColor("#80cbc4"));
                    FirebaseAuth.getInstance().signOut();
                }
            }
        });


        getTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Token.isEmpty() && !isToken)  {
                    Token = FirebaseInstanceId.getInstance().getToken();
                    Log.d(TAG, "InstanceID token: " + Token);
                    Toast.makeText(StartActivity.this, "Token = " +Token, Toast.LENGTH_SHORT).show();
                    getTokenButton.setText("START");
                    isToken = true;

                    // Write a message to the database
                    myRef.child("tadoya").child("token").setValue(Token);
                }
                else {
                    Intent chattingIntent = new Intent(getApplicationContext(), ChattingActivity.class);
                    chattingIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(chattingIntent);
                    finish();
                }
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
            Log.d("stay","destroy:"+isStaySignIn);
            FirebaseAuth.getInstance().signOut();
        }
    }

    public static void getTokenatStart(){
        // Read a message from the database(Once)
        myRef.child("tadoya").child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tadoyaToken = (String) dataSnapshot.getValue();
                Log.d(TAG,tadoyaToken);
                Token = tadoyaToken;
                isToken = true;
                getTokenButton.setText("START");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"token reading error");
                isToken = false;
            }
        });
    }
}
