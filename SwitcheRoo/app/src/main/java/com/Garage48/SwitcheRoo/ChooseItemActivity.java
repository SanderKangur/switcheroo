package com.Garage48.SwitcheRoo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.Garage48.SwitcheRoo.Cards.cards;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseItemActivity extends AppCompatActivity {

    String[] names = {"Öömask", "Lühikesed püksid", "Alukad", "Pluusid", "Useless Developer"};
    int[] images = {R.drawable.mask, R.drawable.puksid, R.drawable.alukad, R.drawable.pluusid, R.drawable.prugi};

    private FirebaseAuth mAuth;

    private String currentUId;

    private DatabaseReference usersDb;

    private Iterable<DataSnapshot> list;

    private Button mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mConfirm = (Button) findViewById(R.id.matchbutton);
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 1);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        usersDb.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              //if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)) {

              if (dataSnapshot.exists() && dataSnapshot.getKey().equals(currentUId)) {
                  list = dataSnapshot.child("items").getChildren();

              }
          }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        ChoiceAdapter adapter = new ChoiceAdapter(this, names, images);
        ListView grid = (ListView)findViewById(R.id.grid);
        Log.i("pask", "activity");
        grid.setAdapter(adapter);
    }


}
