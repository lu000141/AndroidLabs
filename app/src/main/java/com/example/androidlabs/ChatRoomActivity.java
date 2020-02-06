package com.example.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;

    private MyListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);


        editText = findViewById(R.id.editTextChatMsg);
        ListView listConv = findViewById(R.id.listConversation);
        adapter = new MyListAdapter(this, R.id.listConversation);
        listConv.setAdapter(adapter);

        listConv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

//                Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(listConv.getContext());
                String message = "The selected row is: "+position+"\n"+"The database id id: "+id;
                builder.setMessage(message).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getApplicationContext(),"Test，select Yes",Toast.LENGTH_LONG).show();
                        Message msg = adapter.getItem(position);
                        adapter.remove(msg);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getApplicationContext(),"Test，select No",Toast.LENGTH_LONG).show();

                    }
                });

                builder.show();

                return true;
            }
        });

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        Button buttonReceived = findViewById(R.id.buttonReceive);
        buttonReceived.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String input = editText.getText().toString();

        if (input.length() == 0)
            return;

        switch (v.getId()) {
            case R.id.buttonSend:
                adapter.add(new Message(input, MessageType.SENT));
                break;
            case R.id.buttonReceive:
                adapter.add(new Message(input, MessageType.RECEIVED));
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
        editText.setText("");
    }


    /**
     * MessageType Enum Type
     */
    private enum MessageType { SENT, RECEIVED }

    /**
     * Message representing class
     */
    private class Message {
        private String message;
        private MessageType type;

        Message(String message, MessageType type) {
            this.message = message;
            this.type = type;
        }

        String getMessage() {
            return message;
        }

        MessageType getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    /**
     * Customized List Adapter, with built-in container for Message
     */
    private class MyListAdapter extends ArrayAdapter<Message> {
        private LayoutInflater inflater;


        MyListAdapter(Context context, int resource) {
            super(context, resource);
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Message message = getItem(position);

            View view = null;
            TextView textView = null;

            if (message.getType() == MessageType.SENT) {
                view = inflater.inflate(R.layout.chat_message_sent, null);
                textView = view.findViewById(R.id.textViewSent);

            } else if (message.getType() == MessageType.RECEIVED) {
                view = inflater.inflate(R.layout.chat_message_received, null);
                textView = view.findViewById(R.id.textViewReceived);
            }
            textView.setText(message.getMessage());

            return view;
        }
    }
}
