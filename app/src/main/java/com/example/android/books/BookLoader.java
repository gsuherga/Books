package com.example.android.books;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static com.example.android.books.BookActivity.LOG_TAG;

/**
 * Created by jesus on 4/06/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    /** Query URL */
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        Log.i(LOG_TAG, "LOADER IS CALLED");
        mUrl=url;

    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST ON START LOADING CALLED...");
        forceLoad();
    }

    /**
     * This method runs on a background thread and performs the network request.
     * We should not update the UI from a background thread, so we return a list of
     * {@link Book}s as the result.
     */

        @Override
    public List<Book> loadInBackground() {
            Log.i(LOG_TAG, "LOAD IN BACKGROUND IS CALLED");
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (mUrl == null) {
                return null;
            }

            List<Book> books = QueryUtils.extractBooks(mUrl);
            return books;
        }
    }