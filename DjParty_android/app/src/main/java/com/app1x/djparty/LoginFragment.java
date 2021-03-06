package com.app1x.djparty;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnLoginFragmentInteractionListener mListener;

    View view;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_login, container, false);
        Button joinButton = (Button) view.findViewById(R.id.join_button);
        joinButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.join_button:
                if (MainActivity.mParties!=null) {
                    setButtonEnabled(false);

                    EditText partyNameField = (EditText) view.findViewById(R.id.party_name);
                    final String partyName = partyNameField.getText().toString();

                    EditText partyPassField = (EditText) view.findViewById(R.id.party_pass);
                    final String partyPass = partyPassField.getText().toString();
                    Log.i(TAG, "pass: "+partyPass);

                    EditText guestNameField = (EditText) view.findViewById(R.id.guest_name);
                    final String guestName = guestNameField.getText().toString();

                    mListener.onJoinPressed(partyName, partyPass, guestName);

//                    Log.i("run txn", TAG);
//                    MainActivity.mParties.child(partyName).runTransaction(new Transaction.Handler
//                            () {
//                        @Override
//                        public Transaction.Result doTransaction(MutableData mutableData) {
//                            Object party= mutableData.getValue();
//                            if (party!=null) {
//                                Log.i(TAG, party.toString());
//                                Log.i(TAG, mutableData.getClass().toString());
////                                if (party["password"]!=partyPass)
//                            } else {
////                                Object party= {"guestList": {}, "password": partyPass};  //create
//                                // new party
//                                MainActivity.mAmPartyHost= true;
//                            }
//
////                            if (mutableData.)
////                            Post p = mutableData.getValue(Post.class);
////                            if (p == null) {
////                                return Transaction.success(mutableData);
////                            }
////
////                            if (p.stars.containsKey(getUid())) {
////                                // Unstar the post and remove self from stars
////                                p.starCount = p.starCount - 1;
////                                p.stars.remove(getUid());
////                            } else {
////                                // Star the post and add self to stars
////                                p.starCount = p.starCount + 1;
////                                p.stars.put(getUid(), true);
////                            }
////
////                            // Set value and report transaction success
////                            mutableData.setValue(p);
//                            return Transaction.success(mutableData);
//                        }
//
//                        @Override
//                        public void onComplete(DatabaseError databaseError, boolean b,
//                                               DataSnapshot dataSnapshot) {
//                            // Transaction completed
//                            Log.d(TAG, "postTransaction:onComplete:" + databaseError);
//                        }
//                    });
                }

                break;
        }
//        mListener.onFragmentInteraction(v);

    }
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(View uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setEditTextText(int id, String newText) {
        EditText editText= (EditText) view.findViewById(id);
        editText.setText(newText);
    }
    public void setEditTextHint(int id, String newHint) {
        EditText editText= (EditText) view.findViewById(id);
        editText.setHint(newHint);
    }
    public void setButtonEnabled(boolean enabled) {
        Button button= (Button) view.findViewById(R.id.join_button);
        button.setEnabled(enabled);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginFragmentInteractionListener {
//        void onLoginFragmentInteraction(View v);
        void onJoinPressed(String partyName, String partyPass, String guestName);
    }
}
