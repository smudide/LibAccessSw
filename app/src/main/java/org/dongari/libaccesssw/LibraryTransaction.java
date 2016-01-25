package org.dongari.libaccesssw;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by kmudide on 12/25/2015.
 */
public class LibraryTransaction {
    public static final String LIB_ACCESS_MODULE = "LAMLibraryTransaction";

    public static final String LIST_PURPOSE_CHECK_OUT = "Check Out";
    public static final String LIST_PURPOSE_CHECK_IN = "Check In";
    public static final String LIST_PURPOSE_ADD_MODE_BOOKS = "Add More Books";
    public static final String LIST_PURPOSE_ALL = "ALL";

    Cursor  CursorToBooksList;
    String  Purpose;

    Book    CheckingInBook;
    Book    CheckingOutBook;
    Book    IncrementingBook;

    Student BookBorrowingStudent;
    Student BookReturnigStudent;
    int     UserInterestedRow;

    static  LibraryTransaction LibraryTransactionSingleton;

    private LibraryTransaction() {

        CursorToBooksList = null;
        Purpose = "";

        CheckingInBook = null;
        CheckingOutBook = null;
        IncrementingBook = null;

        BookBorrowingStudent = null;
        BookReturnigStudent = null;
        UserInterestedRow = -1;
    }

    public void clear() {

        CursorToBooksList = null;
        Purpose = "";

        CheckingInBook = null;
        CheckingOutBook = null;
        IncrementingBook = null;

        BookBorrowingStudent = null;
        BookReturnigStudent = null;
        UserInterestedRow = -1;
    }

    public static LibraryTransaction getSingleton() {
        Log.i(LIB_ACCESS_MODULE, "getSingleton - called");
        if (LibraryTransactionSingleton == null) {
            Log.i(LIB_ACCESS_MODULE, "getSingleton - creating singleton");
            LibraryTransactionSingleton = new LibraryTransaction();
        }
        return LibraryTransactionSingleton;
    }

    public void setCheckingInBook(Book book) {
        CheckingInBook = book;
    }

    public Book getCheckingInBook()
    {
        return CheckingInBook;
    }

    public void setCheckingOutBook(Book book)
    {
        CheckingOutBook = book;
    }

    public Book getCheckingOutBook()
    {
        return CheckingOutBook;
    }

    public void setIncrementingBook(Book book) {
        IncrementingBook = book;
    }

    public Book getIncrementingBook()
    {
        return IncrementingBook;
    }

    public void setBookBorrowingStudent(Student student) {
        BookBorrowingStudent = student;
    }

    public Student getBookBorrowingStudent()
    {
        return BookBorrowingStudent;
    }

    public void setBookReturningStudent(Student student)
    {
        BookReturnigStudent = student;
    }

    public Student  getBookReturningStudent()
    {
        return BookReturnigStudent;
    }

    public Cursor getCursorToBooksList() {
        return CursorToBooksList;
    }

    public void setCursorToBooksList(Cursor cursorToBooksList) { CursorToBooksList = cursorToBooksList; }

    public void setUserInterestedRow(int userInterestedRow) {
        UserInterestedRow = userInterestedRow;
    }

    public int getUserInterestedRow() {
        return UserInterestedRow;
    }
    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getPurpose() {
        return Purpose;
    }
}
