package com.chanakyabhardwaj.callmom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener {
    //Final, statics
    private static final String SHARED_PREFS_FILE = "callmom_prefs";
    private static final String SHARED_PREFS_KEY = "reminders";
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final long DEFAULT_INTERVAL = 1 * 60 * 60 * 1000;


    //Reference to the views
    Button pickerButton;
    ListView remindersList;

    //Data
    ArrayList<Reminder> reminders;
    ReminderAdapter remindersAdapter;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Attach listener on the picker button
        pickerButton = (Button)findViewById(R.id.picker_btn);
        pickerButton.setOnClickListener(this);

        this.prepareReminders();
    }

    //this function is responsible for:
    //1. set up the ArrayList - reminders
    //2. setup the ListView - remindersLit
    //3. Connect the ListView with the ArrayList using a ReminderAdapter
    private void prepareReminders(){
        mSharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE);
        String remindersAsString = mSharedPreferences.getString(SHARED_PREFS_KEY, "");

        if(remindersAsString.length()>0){
            Gson gson = new Gson();
            reminders = gson.fromJson(remindersAsString, new TypeToken<ArrayList<Reminder>>(){}.getType());
        } else {
            reminders = new ArrayList<Reminder>();
        }

        remindersList = (ListView)findViewById(R.id.remindersList);
        remindersAdapter = new ReminderAdapter(this, reminders);
        remindersList.setAdapter(remindersAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //This is the click listener for the picker button.
    //It starts an intent to select a contact.
    @Override
    public void onClick(View v) {
        Intent pickContactIntent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, CONTACT_PICKER_RESULT);
    }

    //This function receives the selected contact.
    //It parses the chosenContact and pushes it to the ArrayList.
    //it also stores the updated ArrayList in the SharedPreferences.
    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent intent ) {
        super.onActivityResult( requestCode, resultCode, intent );
        if ( requestCode == CONTACT_PICKER_RESULT ) {
            if ( resultCode == RESULT_OK ) {
                Uri chosenContact = intent.getData();
                this.addReminder(chosenContact);
            }
        }
    }

    protected void addReminder(Uri chosenContact){
        Reminder newReminder = new Reminder(chosenContact.toString(), DEFAULT_INTERVAL);

        for(Reminder rem : reminders){
            if(rem.getContact().equals(chosenContact.toString())){
                Toast.makeText(this, "Duplicate. Not adding.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        reminders.add(newReminder);
        remindersAdapter.notifyDataSetChanged();

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String reminderString = new Gson().toJson(reminders);
        editor.putString(SHARED_PREFS_KEY, reminderString);
        editor.apply();
    }

    protected void deleteReminder(int position){
        reminders.remove(position);
        remindersAdapter.notifyDataSetChanged();

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String reminderString = new Gson().toJson(reminders);
        editor.putString(SHARED_PREFS_KEY, reminderString);
        editor.apply();
    }
}
