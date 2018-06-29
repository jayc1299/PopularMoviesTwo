package com.android.test.popularmoviestwo.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.test.popularmoviestwo.MovieApi;
import com.android.test.popularmoviestwo.database.AppDatabase;
import com.android.test.popularmoviestwo.objects.Movie;
import com.android.test.popularmoviestwo.objects.PojoMovies;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AsyncGetMovies extends AsyncTask<Context, Void, PojoMovies> {

    private static final String TAG = "AsyncGetMovies";
    private static final int mTimeout = 15000;

    @Override
    protected PojoMovies doInBackground(Context... params) {
        InputStream is = null;
        Context context = params[0];
        MovieApi api = new MovieApi(context);

        try {
            URL url = api.getMoviesUrl();
            Log.d(TAG, "myUrl:" + url.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(mTimeout);
            conn.setConnectTimeout(mTimeout);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);

            Log.d(TAG, "contentAsString: " + contentAsString);

            Gson gson = new Gson();
            AppDatabase mDb = AppDatabase.getInstance(context);
            //Delete all movies
            mDb.movieDao().deleteAllMovies();

            //Convert json to object
            PojoMovies pojoMovies = gson.fromJson(contentAsString, PojoMovies.class);

            //loop through all movies and insert into DB.
            if(pojoMovies != null && pojoMovies.movies != null){
                for (Movie movie : pojoMovies.movies) {
                    mDb.movieDao().insertMovie(movie);
                }
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private String readIt(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }

        return total.toString();
    }
}