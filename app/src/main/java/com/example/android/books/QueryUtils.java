package com.example.android.books;

/**
 * Created by jesus on 30/05/17.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.books.BookActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving book data from USGS.
 */
public final class QueryUtils {

    private QueryUtils() {
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Book> extractBooks(String requestUrl) {
        Log.i(LOG_TAG, "extracBooks is called...");
        //Create a url object (the website with the information about the books)

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Book} object by parsing out information
     * about the first book from the input bookJSON string.
     */
    private static List<Book> extractFeatureFromJson(String BookJSON) {

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(BookJSON)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(BookJSON);
            JSONArray featureArray = baseJsonResponse.getJSONArray("items");

            // If there are results in the features array
            if (featureArray.length() > 0) {
                // Extract out the first feature (which is a book)

                // looping through the books
                for (int i = 0; i < featureArray.length(); i++) {

                    //Crete a JsonObject for the current Book
                    JSONObject currentBook = featureArray.getJSONObject(i);
                    //We look for the JSONObject propertiesBook
                    JSONObject propertiesBook = currentBook.getJSONObject("volumeInfo");
                    JSONArray authors = propertiesBook.getJSONArray("authors");
                    JSONObject imageBookJSON = propertiesBook.getJSONObject("imageLinks");


                    //Inside the propertiesBook JSON Object we can find the values for bookTitle, bookSubtitle
                    //and the imageLinks; inside imageLinks an image about the book.

                    //Inside the properties book JSON Object we can find an array with the authors of the book.

                    //Book title
                    String bookTitle = propertiesBook.getString("title");
                    //Book subtitle
                    String bookSubtitle;
                    try {
                        bookSubtitle = propertiesBook.getString("subtitle");
                    } catch (Exception e){
                        bookSubtitle = null;
                    }
                    //Book image
                    String imageBook = imageBookJSON.getString("smallThumbnail");
                    //Book web
                    String web = propertiesBook.getString("previewLink");
                    //Book authors
                    int numAuthors = authors.length();
                    final String[] bookauthors = new String[numAuthors];
                    for (int j = 0; j < numAuthors; j++) {
                        bookauthors[j] = authors.getString(j);
                    }

                    //We create a new Book Object with the data we have got from the JSON Object

                    Book book = new Book(bookTitle,bookSubtitle,bookauthors,imageBook, web);

                    //we add the book to the list of books
                    books.add(book);
                }
                // Create a new {@link Event} object
                return books;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return null;
    }
}