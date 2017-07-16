package com.example.android.books;

/**
 * Created by jesus on 30/05/17.
 */

public class Book {

    // Title of the book
    private String mbookSubtitle;

    // Subittle of the book
    private String mbookTitle;

    // Image of the book
    private String mbookImage;


    /** Author of the book */
    private String [] mauthors;

    //Web for more information about the book
    private String mweb;

    /*
    * Create a new Book data object.
    *
    * @param bookTitle is the bookTitle
    * @param bookSubtitle, the bookSubtitle
    * @param authors are the authors of the book
    * @param imageBook is an image about the book
    * param web is a web with more information about the book
    * */
    public Book(String bookTitle, String bookSubtitle, String [] authors, String imageBook, String web) {
        mbookTitle = bookTitle;
        mbookSubtitle = bookSubtitle;
        mauthors = authors;
        mbookImage = imageBook;
        mweb = web;

    }

    /**
     * Get the title of the book
     */
    public String getBookTitle() {
        return mbookTitle;
    }

    /**
     * Get the subtitle of the book
     */
    public String getBookSubTitle(){
        return mbookSubtitle;
    }

    /**
     * Get the author of the book
     */
    public String getBookImage() {
        return mbookImage;
    }

    /**
     * Get the author of the book
     */
    public String [] getAuthor() {
        return mauthors;
    }

    /*
    Get a web for mor information about the book
     */

    public String getMweb(){
        return mweb;
    }
}
