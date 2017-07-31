/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.didyoufeelit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.net.URL;

/**
 * Displays the perceived strength of a single earthquake event based on responses from people who
 * felt the earthquake.
 */
public class MainActivity extends AppCompatActivity {

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an {@link AsyncTask} to perform the HTTP request to the given URL
        //on a background thread. When the result is received on the main UI thread,
        //then update the UI
        FetchEarthquakeAsync fetchEarthquakeAsync = new FetchEarthquakeAsync();
        fetchEarthquakeAsync.execute(USGS_REQUEST_URL);
    }

    /**
     * Update the UI with the given earthquake information.
     */
    private void updateUi(Event earthquake) {
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(earthquake.title);

        TextView tsunamiTextView = (TextView) findViewById(R.id.number_of_people);
        tsunamiTextView.setText(getString(R.string.num_people_felt_it, earthquake.numOfPeople));

        TextView magnitudeTextView = (TextView) findViewById(R.id.perceived_magnitude);
        magnitudeTextView.setText(earthquake.perceivedStrength);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */
    private class FetchEarthquakeAsync extends AsyncTask<String, Void, Event> {


        /**
         * This method is invoked on a background thread, so we can perform
         * long running operations like making a network request.
         *
         * It is NOT OK to update UI from a background thread, so we just return an
         * {@link Event} object as the result
         */

        @Override
        //urls is a Var Arg (...), which means it is an array, thus url[0]
        protected Event doInBackground(String... urls) {
            // Perform the HTTP request for earthquake data and process the response.
            Event earthquakeResult = Utils.fetchEarthquakeData(urls[0]);
            return earthquakeResult;
        }

        /**
         * This method is invoked on the main UI thread after the background work has been
         * completed.
         *
         * It is ok to modify the UI within this method. we take the {@link Event} object
         * (which was returned form the doInBackground) and update the views on screen
         */

        @Override
        protected void onPostExecute(Event earthquakeResult) {
            // Update the information displayed to the user.
            updateUi(earthquakeResult);
        }
    }
}
