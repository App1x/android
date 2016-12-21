package com.app1x.djparty;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback,
        LoginFragment.OnLoginFragmentInteractionListener,
        PlayerFragment.OnPlayerFragmentInteractionListener
{

    private static final String TAG= "MainActivityTag";

    //Spotify
    private static final String CLIENT_ID = "7d7061da35dd47f387d4cb4aa309420a";
    private static final String REDIRECT_URI = "com.app1x.djparty://callback";

    private Player mPlayer;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static DatabaseReference mParties;
    public static DatabaseReference mParty;
    public static DatabaseReference mGuestList;
    public static String mMyName;
    public static DatabaseReference mMyStuff;
    public static DatabaseReference mMyPlaylist;

    public static boolean mNoSongPlaying= true;
    public static boolean mAmPartyHost= false;
    public static boolean mAmSongOwner= false;
    public static boolean mNowPlaying;

//    //RequestQueue
//    RequestQueue mQueue;

    //Fragments
    LoginFragment loginFragment;
    PlayerFragment playerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spotify auth
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        //Firebase ref
        mParties = FirebaseDatabase.getInstance().getReference("parties");
        Log.i(TAG, "parties");
        Log.i(TAG, mParties.toString());

        //Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

//        //Instantiate the RequestQueue.
//        mQueue= Volley.newRequestQueue(this);

        //load login fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            loginFragment = new LoginFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            loginFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, loginFragment).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

//        mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error e) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

//    @Override
//    public void onFragmentInteraction(View v) {
//        switch (v.getId()) {
//            case R.id.join_button:
////                String partyName= findViewById(R.id.)
//                break;
//        }
//    }

    //TODO: show play fragment
    public void showPlayPage() {
        playerFragment = new PlayerFragment();
        playerFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, playerFragment).commit();
    }

    @Override
    public void onJoinPressed(final String partyName, final String partyPass, final String
            guestName) {

        Log.i("run txn", TAG);
        MainActivity.mParties.child(partyName).runTransaction(new Transaction.Handler() {
            boolean gotIn= true;

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Party party= mutableData.getValue(Party.class);
                if (party!=null) {
                    Log.i(TAG, "Party Not Null");
                    Log.i(TAG, party.toString());
                    if (!party.password.equals(partyPass)) {
                        Log.i(TAG, "Wrong Password");

                        gotIn= false;
                        return Transaction.success(mutableData);
                    }
                    mAmPartyHost= party.host==guestName;
                } else {
                    Log.i(TAG, "Party Null");
                    party= new Party(partyPass, guestName);
                    mAmPartyHost= true;
                }

                if (party.guestList.get(guestName)==null) {
                    Guest newGuest= new Guest(guestName);
                    newGuest.insertNode(party.guestList, party.guestList
                            .size());
                }
                Log.i(TAG, party.toString());

                // Set value and report transaction success
                mutableData.setValue(party);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                if (gotIn) {
                    mMyName= guestName;
                    mParty= mParties.child(partyName);
                    mGuestList= mParty.child("guestList");
                    mMyStuff= mGuestList.child(guestName);
                    mMyPlaylist= mMyStuff.child("playlist");

                    //TODO: set call am_not_host if not host

                    //TODO: set myPlaylist listener

                    //Listen to guest or playlist changes
                    mGuestList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<Map<String, Node>> t = new
                                    GenericTypeIndicator<Map<String, Node>>() {};
                            Map<String, Node> guestList = dataSnapshot.getValue(t);
//                            Log.i(TAG, guestList.toString());
                            Guest headGuest= (Guest) Guest.findHead(guestList);

                            //populate next up
                            Track nextUpTrack= null;
                            String nextInLine;
                            ArrayList<String> listHtml= new ArrayList<String>();
                            Guest currentGuest= headGuest;
                            while (currentGuest!=null) {
                                Track nextTrack= (Track) Node.findHead(currentGuest.playlist);
                                if (nextTrack!=null) {
                                    if (listHtml.size()==0) {
                                        nextUpTrack= nextTrack;
                                        nextInLine= currentGuest.id;

                                        Map<String, Object> updates= new HashMap<>();
                                        updates.put("nextUp", nextTrack.id);
                                        updates.put("nextInLine", nextInLine);
                                        mParty.updateChildren(updates);
//                                                , new
//                                                DatabaseReference.CompletionListener() {
//                                                    @Override
//                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                                        //TODO: or spotify ended
//                                                        if (mNoSongPlaying) {
//                                                            mNoSongPlaying= false;
//
//                                                            final Node currentlyPlaying;
//                                                            mParty.addListenerForSingleValueEvent
//                                                                    (new ValueEventListener() {
//                                                                        @Override
//                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                                                            currentlyPlaying=
//                                                                        }
//
//                                                                        @Override
//                                                                        public void onCancelled(DatabaseError databaseError) {
//
//                                                                        }
//                                                                    });
//                                                        }
//                                                    }
//                                                });
                                    }
                                    //TODO: push html to listHtml
                                }
                                String nextGuestName= currentGuest.next;
                                currentGuest= (Guest) guestList.get(nextGuestName);
                            }
                            if (nextUpTrack==null) {
                                mParty.child("nextUp").setValue(null);
                            }
                            //TODO: load next up list view
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                    //listen for next up change (non host)
                    mParty.child("currentlyPlaying").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //TODO: load currently playing
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //TODO: listen for host

                    showPlayPage();

                } else {
                    loginFragment.setEditTextHint(R.id.party_name, "Party already exists");
                    loginFragment.setEditTextText(R.id.party_name, "");
                    loginFragment.setEditTextHint(R.id.party_pass, "Wrong password");
                    loginFragment.setEditTextText(R.id.party_pass, "");
                    loginFragment.setButtonEnabled(true);
                }

                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
}