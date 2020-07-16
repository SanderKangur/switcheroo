package com.Garage48.SwitcheRoo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.Garage48.SwitcheRoo.Cards.arrayAdapter;
import com.Garage48.SwitcheRoo.Cards.cards;
import com.Garage48.SwitcheRoo.Matches.MatchesActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private cards cards_data[];
    private com.Garage48.SwitcheRoo.Cards.arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    private String currentUId;

    private DatabaseReference usersDb;

    private Iterable<DataSnapshot> list;

    SwipeFlingAdapterView flingContainer;

    ListView listView;
    List<cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        //checkUserSex();
        getOppositeSexUsers();

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                cards obj = (cards) dataObject;
                String userId = obj.getItemId();
                usersDb.child(obj.getparentId()).child("items").child(obj.getItemId()).child("connections").child("nope").child(currentUId).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                usersDb.child(obj.getparentId()).child("items").child(obj.getItemId()).child("connections").child("yeps").child(currentUId).setValue(true);

                Intent intent = new Intent(MainActivity.this, ChooseItemActivity.class);
                startActivityForResult(intent, 666);

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "New Connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



    public void getOppositeSexUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)) {

                if (dataSnapshot.exists() && dataSnapshot.getKey().equals(currentUId)) {
                    String profileImageUrl = "default";
                    String parentID = dataSnapshot.getKey();
                    list = dataSnapshot.child("items").getChildren();
                }


                    if (dataSnapshot.exists() && !dataSnapshot.getKey().equals(currentUId)) {
                        String profileImageUrl = "default";
                        String parentID = dataSnapshot.getKey();
                        Iterable<DataSnapshot> list = dataSnapshot.child("items").getChildren();
                        for (DataSnapshot snap : list) {

                            if (!snap.child("connections").child("nope").hasChild(currentUId) && !snap.child("connections").child("yeps").hasChild(currentUId)) {
                                profileImageUrl = "default";
                                if (!snap.child("profileImageUrl").getValue().equals("default")) {
                                    profileImageUrl = snap.child("profileImageUrl").getValue().toString();
                                }
                                cards item = new cards(snap.getKey(), parentID, snap.child("name").getValue().toString(), snap.child("desc").getValue().toString(), profileImageUrl);
                                rowItems.add(item);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
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
    }


    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
        startActivity(intent);
        return;
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
    }

    public void likeBtnAction(View view){
        flingContainer.getTopCardListener().selectRight();
    }

    public void cancelBtnAction(View view){
        flingContainer.getTopCardListener().selectLeft();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 666) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(MainActivity.this, "New Connection", Toast.LENGTH_LONG).show();

                String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                usersDb.child(currentUId).child("connections").child("matches").child("lJg8NAEqWgSaaOgxeuRBlaYRwlY2").child("ChatId").setValue(key);
                usersDb.child("lJg8NAEqWgSaaOgxeuRBlaYRwlY2").child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
            }

        }
    }
}