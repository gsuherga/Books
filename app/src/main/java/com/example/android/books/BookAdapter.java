package com.example.android.books;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jesus on 30/05/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context. Used to inflate the layout file.
     * @param books   the list of the books
     */
    public BookAdapter(Activity context, List<Book> books) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for three textviews, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, books);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_book, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        final Book currentBook = getItem(position);

        //Find ImageView to put the image about the book
        ImageView bookPhoto = (ImageView) listItemView.findViewById(R.id.book_image);

        // Find the TextViews in the single_book layout
        TextView bookTitle = (TextView) listItemView.findViewById(R.id.book_title);
        TextView bookSubtitle = (TextView) listItemView.findViewById(R.id.book_subtitle);
        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.book_author);

        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView

        //Title
        bookTitle.setText(currentBook.getBookTitle());
        //Subtitle
        if (currentBook.getBookSubTitle() != null) {
            bookSubtitle.setText(currentBook.getBookSubTitle());
        } else {
            bookSubtitle.setVisibility(View.GONE);
        }
        //Authors

        if (currentBook.getAuthor() != null) {
            String[] authors = currentBook.getAuthor();
            bookAuthor.setText(Arrays.toString(authors).replaceAll("\\[|\\]", ""));

          /*  for (int j = 0; j < authors.length; j++) {
                Log.v("Authors", currentBook.getBookTitle()+authors[j]);
            } */
        } else {
            bookAuthor.setText(R.string.no_authors);
        }

        if (currentBook.getBookImage() != null) {
            //Get the image about the book and set the ImageView
            Picasso.with(getContext()).load(Uri.parse(currentBook.getBookImage())).into(bookPhoto);
        } else {
            Picasso.with(getContext()).load(R.drawable.no_cover_image).into(bookPhoto);
        }

        //Get the ListView so we can click on them to open the web with more information

        View text = listItemView.findViewById(R.id.bookList);

        //Set the onClickListener method.
        //
        text.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentBook.getMweb()));
                getContext().startActivity(intent);

            }
        });

        // Return the whole list item layout
        // so that it can be shown in the ListView
        return listItemView;
    }
}
