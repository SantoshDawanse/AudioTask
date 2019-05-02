package com.dawnsoft.dawn.audiotask.frags;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dawnsoft.dawn.audiotask.R;
import com.dawnsoft.dawn.audiotask.model.ContactBook;
import com.dawnsoft.dawn.audiotask.model.ContactBookAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment{

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_SEND_SMS =101 ;
    private static final String SEND_MESSAGE = "www.google.com";

    private ProgressBar progressBar;

    private ListView listView;
    private static ArrayList<ContactBook> contactBookList;

    private ContactBookAdapter adapter;

    //for selected contact
    private ArrayList<String> selectedList;

    //using layout as a button
    LinearLayout sendAppButton;

    public ShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        adapter = new ContactBookAdapter(getActivity(), contactBookList);

        //selected list initialize
        selectedList = new ArrayList<>();

        listView = view.findViewById(R.id.listContacts);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.getId() == R.id.contact_list_item){
                    ContactBook currentData = adapter.getItem(position);

                    if (currentData != null) {
                        if (currentData.getIsSelected() && position >= 0 && position < selectedList.size()){
                            currentData.setSelected(false);
                            //to check the checkbox
                            adapter.isSelected(currentData.getIsSelected());
                            adapter.notifyDataSetChanged();
                            //remove the item from array
                            selectedList.remove(position);
                        }else {
                            currentData.setSelected(true);
                            adapter.isSelected(currentData.getIsSelected());
                            adapter.notifyDataSetChanged();
                            selectedList.add(currentData.getPhoneNumber());
                        }
                    }
                }
            }

        });

        //floating send button
        sendAppButton = view.findViewById(R.id.sendButtonLayout);
        sendAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSendPermission();
            }
        });
        checkContactPermission();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    checkContactPermission();
                }else {
                    Toast.makeText(getActivity(), "You should give access to load contacts.", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    checkSendPermission();
                } else {
                    Toast.makeText(getActivity(), "You should give access to load contacts.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //run time sms permission
    private void checkSendPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            sendSMS();
        }
    }

    //run time contact permissions
    private void checkContactPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overridden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            new LoadContacts().execute();
        }
    }

    private void sendSMS(){
        if (selectedList.size() > 0){
            SmsManager smsManager = SmsManager.getDefault();
            for(int i = 0; i < selectedList.size(); i++){

                smsManager.sendTextMessage(selectedList.get(i), null, SEND_MESSAGE, null, null);

            }
            selectedList.clear();

            Toast.makeText(getActivity(), "Successfully sent.", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getActivity(), "Select contact to send message!!!", Toast.LENGTH_SHORT).show();
    }

    //loading the contacts
    private ArrayList<ContactBook> loadContacts() {

        ArrayList<ContactBook> contactBookArrayList = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    Cursor cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (cursor1.moveToNext()) {
                        String phoneNumber = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactBookArrayList.add(new ContactBook(name, phoneNumber, false));

                    }
                    cursor1.close();
                }
            }
        }
        cursor.close();
        return contactBookArrayList;
    }

    //asynchronous method to load contacts
    private class LoadContacts extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactBookList = loadContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);

            adapter = new ContactBookAdapter(getActivity(), contactBookList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}
