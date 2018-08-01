package com.example.cgaima.squaa.ProfileFragements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.adapters.profileAdapter;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EventUpcoming extends Fragment {
    @BindView(R.id.rvEventHistory) RecyclerView rvGrid;
    private profileAdapter mAdapter;
    private List<Event> events;


    private FragmentActivity listener;

    // Required empty public constructor
    public EventUpcoming() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.e("EventHistory", "Event history fragment created");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_history, container, false);
        ButterKnife.bind(this, view);
        HttpURLConnection connection;
        OutputStreamWriter request;



//        try {
//            URL url = null;
//            String response = null;
//            String parameters = "param1=value1&param2=value2";
//            url = new URL("http://www.somedomain.com/sendGetData.php");
//            //create the connection
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
//            //set the request method to GET
//            connection.setRequestMethod("GET");
//            //get the output stream from the connection you created
//            request = new OutputStreamWriter(connection.getOutputStream());
//            //write your data to the ouputstream
//            request.write(parameters);
//            request.flush();
//            request.close();
//            String line = "";
//            //create your inputsream
//            InputStreamReader isr = new InputStreamReader(
//                    connection.getInputStream());
//            //read in the data from input stream, this can be done a variety of ways
//            BufferedReader reader = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            //get the string version of the response data
//            response = sb.toString();
//            //do what you want with the data now
//
//            //always remember to close your input and output streams
//            isr.close();
//            reader.close();
//        } catch (IOException e) {
//            Log.e("HTTP GET:", e.toString());
//        }


        return view;
    }



}
