package com.example.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.HashMap;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;

    private MyListAdapter adapter;

    private MyDatabaseOpenHelper dbOpener;

    private SQLiteDatabase db;

    private HashMap<Message,DetailsFragment> openedFragments;

    private boolean isTablet;

    public static final String ACTIVITY_NAME = "CHATROOM_ACTIVITY";
    public static final String MESSAGE_Id = "messageId";
    public static final String MESSAGE_Type = "messageType";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        //initialize a hashmap for storing current open fragments
        openedFragments = new HashMap<>();

        //check to see if app run in a tablet
        isTablet = findViewById(R.id.messageFrame) != null;

        editText = findViewById(R.id.editTextChatMsg);
        ListView listConv = findViewById(R.id.listConversation);
        adapter = new MyListAdapter(this, R.id.listConversation);
        listConv.setAdapter(adapter);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        Button buttonReceived = findViewById(R.id.buttonReceive);
        buttonReceived.setOnClickListener(this);

        // TODO: open database, read all the messages and insert them into listview control
        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_MESSAGE_TYPE};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results,db.getVersion());

        //find the column indices:
        int messageTypeIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE_TYPE);
        int messageIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        results.moveToFirst();
        results.moveToPrevious();
        while(results.moveToNext())
        {
            String message = results.getString(messageIndex);
            String messageType = results.getString(messageTypeIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            if (messageType.equals("SENT")) {
                adapter.add(new Message(id, message, MessageType.SENT));
            } else {
                adapter.add(new Message(id, message, MessageType.RECEIVED));
            }
        }

        //set on click event to delete a message item
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
                        db.delete(MyDatabaseOpenHelper.TABLE_NAME,MyDatabaseOpenHelper.COL_ID+"=?",new String[]{msg.getId()+""});
                        adapter.remove(msg);
                        adapter.notifyDataSetChanged();

                        //remove the fragment when a message is deleted
                        if(openedFragments.containsKey(msg)){
                            DetailsFragment detailsFragment = openedFragments.get(msg);
                            detailsFragment.removeFragment();
                            openedFragments.remove(msg);
                        }
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

        //set on click event to show details of a message item
        listConv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message message = adapter.getItem(position);
                //set data to be passed
                Bundle dataToPass = new Bundle();
                dataToPass.putLong(MESSAGE_Id,id);
                dataToPass.putString(MESSAGE_Type,message.getType().toString());
                if(isTablet){
                    //set detail frame
                    DetailsFragment dFrame = new DetailsFragment();
                    dFrame.setArguments(dataToPass);
                    dFrame.setTablet(isTablet);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.messageFrame,dFrame)
                            .addToBackStack("AnyName").commit();

                    //remove former fragments before opening a new fragment
                    if(!openedFragments.isEmpty()){
                        for(DetailsFragment fragment:openedFragments.values()){
                            fragment.removeFragment();
                        }
                    }

                    openedFragments.put(message,dFrame);
                }else{
                    Intent intent = new Intent(ChatRoomActivity.this,EmptyActivity.class);
                    intent.putExtras(dataToPass);
                    startActivity(intent);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String input = editText.getText().toString();

        if (input.length() == 0)
            return;

        // TODO: insert user input into database
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, input);


        long newId = 0;

        switch (v.getId()) {
            case R.id.buttonSend:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE_TYPE, "SENT");
                newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                adapter.add(new Message(newId, input, MessageType.SENT));
                break;
            case R.id.buttonReceive:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE_TYPE, "RECEIVED");
                newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                adapter.add(new Message(newId, input, MessageType.RECEIVED));
                break;
            default:
                break;
        }
        editText.setText("");
    }


    /**
     * MessageType Enum Type
     */
    public enum MessageType { SENT, RECEIVED }

    /**
     * Message representing class
     */
    private class Message {
        private long id;
        private String message;
        private MessageType type;


        public Message(long id, String message, MessageType type) {
            this.id = id;
            this.message = message;
            this.type = type;
        }

        String getMessage() {
            return message;
        }

        MessageType getType() {
            return type;
        }

        public long getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", message='" + message + '\'' +
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

    /**
     * print database information
     * print dateset information
     * @param cursor cursor that containes dataset selected from database
     */
    public void printCursor(Cursor cursor, int version) {

        Log.i(ACTIVITY_NAME,"Database version : "+version);

        int columnNumber = cursor.getColumnCount();
        Log.i(ACTIVITY_NAME, "Column number: " + columnNumber);

        for (int i = 0; i < columnNumber; ++i) {
            Log.i(ACTIVITY_NAME, "Column[" + i + "] name:" + cursor.getColumnName(i));
        }

        int rows = cursor.getCount();
        Log.i(ACTIVITY_NAME, "There are " + rows + " rows in cursor");

        while (cursor.moveToNext()) {
            StringBuilder string = new StringBuilder();
            for (int j = 0; j < columnNumber; ++j)
                string.append(cursor.getString(j) + " ");
            Log.i(ACTIVITY_NAME, string.toString());
        }
    }
}
