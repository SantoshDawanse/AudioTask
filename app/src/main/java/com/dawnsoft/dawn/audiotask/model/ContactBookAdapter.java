package com.dawnsoft.dawn.audiotask.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawnsoft.dawn.audiotask.R;

import java.util.List;

public class ContactBookAdapter extends ArrayAdapter<ContactBook>{
    private Context context;
    private List<ContactBook> contactBooks;
    public ContactBookAdapter(@NonNull Context context, @NonNull List<ContactBook> objects) {
        super(context, 0, objects);
        this.context = context;
        this.contactBooks = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
        }

        ContactBook currentData = getItem(position);
        TextView contactName = view.findViewById(R.id.contactName);
        contactName.setText(currentData.getName());
        TextView phoneNumber = view.findViewById(R.id.phoneNumber);
        phoneNumber.setText(currentData.getPhoneNumber());
        CheckBox isChecked = view.findViewById(R.id.checkContact);
        isChecked.setChecked(currentData.getIsSelected());

        RelativeLayout contactListItem = view.findViewById(R.id.contact_list_item);
        contactListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, ((ListView) parent).getAdapter().getItemId(position));
            }
        });
        return view;
    }

    //checking whether the checkbox is checked or not
    public void isSelected(boolean check){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, null);
        CheckBox checkBox = view.findViewById(R.id.checkContact);
        checkBox.setChecked(check);
    }

}
