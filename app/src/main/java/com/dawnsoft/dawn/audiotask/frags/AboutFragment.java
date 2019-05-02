package com.dawnsoft.dawn.audiotask.frags;


import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dawnsoft.dawn.audiotask.R;
import com.dawnsoft.dawn.audiotask.model.ContactBook;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private TextView url;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        url = view.findViewById(R.id.aboutfrag_url);

        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConnection()) {
                    try {
                        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.blueskycodeworx.com/"));
                        startActivity(urlIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), "No application can handle this request."
                                + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        });


        return view;
    }

    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
