package com.transility.welloculus.ui;

/**
 * Created by vinit.tiwari on 6/22/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.transility.welloculus.R;

public class ProfileActivity extends Activity implements OnItemClickListener
{
    /** Called when the activity is first created. */

    ListView lview;
    ListViewAdapter lviewAdapter;

    private final static String month[] = {"Given name","Family Name","Phone number","Email","Gender",
            "City","BirthDate"};

    private final static String number[] = {"sneha", "Bansal","+918156457458",
            "sneha.bansal@gmail.com","Female","indore",
            "06/06/2015"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        lview = (ListView) findViewById(R.id.listViewUserAttributes);
        lviewAdapter = new ListViewAdapter(this, month, number);

        System.out.println("adapter => "+lviewAdapter.getCount());

        lview.setAdapter(lviewAdapter);

        lview.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        // TODO Auto-generated method stub
        Toast.makeText(this,"API to edit profile "+month[position], Toast.LENGTH_SHORT).show();
    }
}