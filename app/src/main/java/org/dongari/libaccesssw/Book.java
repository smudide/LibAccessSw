package org.dongari.libaccesssw;

/**
 * Created by kmudide on 11/16/2015.
 */
public class Book {
    public String BookTitle;
    public String BookAuthor;
    public String BookCategory;

    public Book() {
        BookTitle = new String("");
        BookAuthor = new String("");
        BookCategory = new String("");
    }

    public Book(String bookTitle, String bookAuthor, String bookCategory) {
        BookTitle = bookTitle;
        BookAuthor = bookAuthor;
        BookCategory = bookCategory;
    }
    public String toString() {
        String string = "";
        if (BookTitle.length() != 0) {
            string += BookTitle;;
        }
        if (BookAuthor.length() != 0) {
            string += " written by " + BookAuthor;
        }
        if (BookCategory.compareTo("Choose Book Category") != 0) {
            string += " (" + BookCategory + ")";
        }
        return string;
    }
}
