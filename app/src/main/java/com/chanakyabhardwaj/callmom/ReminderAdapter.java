package com.chanakyabhardwaj.callmom;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cb on 11/23/14.
 */
public class ReminderAdapter extends ArrayAdapter<Reminder> {
    private Context context;

    public ReminderAdapter(Context context, ArrayList<Reminder> reminders) {
        super(context, 0, reminders);
        this.context = context;
    }

    private Uri getPictureForReminder(Reminder rem){
        String picture;
        Cursor cursor = this.context.getContentResolver().query(Uri.parse(rem.getContact()), null, null, null, null);
        cursor.moveToFirst();

        int pictureIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
        picture = cursor.getString(pictureIndex);
        if(picture != null){
            return Uri.parse(picture);
        } else {
            return null;
        }
    }

    private String getNameForReminder(Reminder rem){
        String name;
        Cursor cursor = this.context.getContentResolver().query(Uri.parse(rem.getContact()), null, null, null, null);
        cursor.moveToFirst();

        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        name = cursor.getString(nameIndex);

        return name;
    }

    private String getNumberForReminder(Reminder rem){
        String phoneNumber;
        Cursor cursor = this.context.getContentResolver().query(Uri.parse(rem.getContact()), null, null, null, null);
        cursor.moveToFirst();

        int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        phoneNumber = cursor.getString(numberIndex);

        return phoneNumber;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Reminder reminder = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_item, parent, false);
        }

        // Find the sub-views
        ImageView contactPicture = (ImageView) convertView.findViewById(R.id.contactPicture);
        final TextView contactName = (TextView) convertView.findViewById(R.id.contactName);
        TextView contactNumber = (TextView) convertView.findViewById(R.id.contactNumber);
        TextView timeInterval = (TextView) convertView.findViewById(R.id.timeInterval);
        Button deleteBtn = (Button) convertView.findViewById(R.id.deleteReminder);


        //Set up the listener for the delete button.
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(context instanceof MainActivity){
                    ((MainActivity)context).deleteReminder(position);
                }

            }
        });

        // Populate the data into the sub-views
        Uri picture = this.getPictureForReminder(reminder);
        if(picture == null){
            contactPicture.setImageResource(R.drawable.ic_launcher);
        } else {
            contactPicture.setImageURI(this.getPictureForReminder(reminder));
        }

        contactName.setText(this.getNameForReminder(reminder));
        contactNumber.setText(this.getNumberForReminder(reminder));
        timeInterval.setText(String.valueOf(reminder.getInterval()));

        // Return the completed view to render on screen
        return convertView;
    }
}
