package com.example.androidlabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetailsFragment extends Fragment {
    private boolean isTablet;
    private Bundle dataFromActivity;
    private long messageId;
    private String messageType;
    TextView showId;
    CheckBox isSendMessage;
    Button hideBtn;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        messageId = dataFromActivity.getLong(ChatRoomActivity.MESSAGE_Id);
        messageType = dataFromActivity.getString(ChatRoomActivity.MESSAGE_Type);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        showId = view.findViewById(R.id.msgId);
        showId.setText(messageId + "");

        isSendMessage = view.findViewById(R.id.isSendMsg);

        if (messageType.equals(ChatRoomActivity.MessageType.SENT.toString())) {
            isSendMessage.setChecked(true);
        } else {
            isSendMessage.setChecked(false);
        }

        hideBtn = view.findViewById(R.id.hideFrame);
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Hide Btn",Toast.LENGTH_LONG).show();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = null;
                if(isTablet) {
                    fragment = fm.findFragmentById(R.id.messageFrame);
                }else {
                    fragment = fm.findFragmentById(R.id.emptyMessageDetailFrame);
                }
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        });
        
        return view;
    }

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    public boolean isTablet() {
        return isTablet;
    }
}
