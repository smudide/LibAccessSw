package org.dongari.libaccesssw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kmudide on 11/8/2015.
 */
public class LibAccessDbHelper extends SQLiteOpenHelper {

    public final String LIB_ACCESS_MODULE = "LAMDbHelper";

    static LibAccessDbHelper LibAccessDbHelperSingleton = null;

    public static final String LIB_ACCESS_DB_NAME = "LibAccess.db";
    public static final int LIB_ACCESS_DB_VERSION = 1;

    public static final String LIB_ACCESS_DB_TABLE_NAME = "BooksList";

    public static final String COLUMN_NAME_ID = "SerialNo";
    public static final String COLUMN_NAME_BOOK_TITLE = "BookTitle";
    public static final String COLUMN_NAME_BOOK_AUTHOR = "BookAuthor";
    public static final String COLUMN_NAME_BOOK_CATEGORY = "BookCategory";

    public static final String COLUMN_NAME_TOTAL_BOOKS = "TotalBooks";
    public static final String COLUMN_NAME_AVAILABLE_BOOKS = "AvailableBooks";

    public static final String COLUMN_NAME_ISSUED_TO = "IssuedTo";

    public static final String CREATE_TABLE_ENTRIES = "CREATE " + " TABLE " + LIB_ACCESS_DB_TABLE_NAME + " (" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_NAME_BOOK_TITLE + " TEXT NOT NULL, " + COLUMN_NAME_BOOK_AUTHOR + " TEXT NOT NULL, " + COLUMN_NAME_BOOK_CATEGORY + " TEXT NOT NULL, "
            + COLUMN_NAME_TOTAL_BOOKS + " INTEGER NOT NULL, " + COLUMN_NAME_AVAILABLE_BOOKS + " INTEGER NOT NULL, " + COLUMN_NAME_ISSUED_TO + " TEXT )";

    public  static  final String DELETE_TABLE = "DROP TABLE IF EXISTS " + LIB_ACCESS_DB_TABLE_NAME;

    private LibAccessDbHelper(Context context) {
        super(context, LIB_ACCESS_DB_NAME, null, LIB_ACCESS_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_ENTRIES);
        }
        catch (SQLException sqlExc){
            Log.e(LIB_ACCESS_MODULE, "Failed to create table, " + LIB_ACCESS_DB_TABLE_NAME);
            return;
        }
        Log.i(LIB_ACCESS_MODULE, "Successfully created table, " + LIB_ACCESS_DB_TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    static public LibAccessDbHelper getSingleton(Context context) {
        if (LibAccessDbHelperSingleton == null) {
            LibAccessDbHelperSingleton = new LibAccessDbHelper(context);
        }
        return LibAccessDbHelperSingleton;
    }

    public boolean addANewBook(Book book, int numberOfBooks) {
        SQLiteDatabase db = getWritableDatabase();

        Log.i(LIB_ACCESS_MODULE, "addANewBook - called");

        Log.i(LIB_ACCESS_MODULE, "addANewBook - received book, " + book.toString());
        Log.i(LIB_ACCESS_MODULE, "addANewBook - received numberOfBooks = " + numberOfBooks);

        // it's new book. insert it now
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME_BOOK_TITLE, book.BookTitle);
        initialValues.put(COLUMN_NAME_BOOK_AUTHOR, book.BookAuthor);
        initialValues.put(COLUMN_NAME_BOOK_CATEGORY, book.BookCategory);
        initialValues.put(COLUMN_NAME_TOTAL_BOOKS, numberOfBooks);
        initialValues.put(COLUMN_NAME_AVAILABLE_BOOKS, numberOfBooks);

        long insertResult = db.insert(LIB_ACCESS_DB_TABLE_NAME, null, initialValues);
        if (insertResult == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addMoreBooksToExistingBook(Cursor cursor, int numberOfBooks) {
        SQLiteDatabase db = getWritableDatabase();

        Log.i(LIB_ACCESS_MODULE, "addMoreBooksToExistingBook - called");
        Log.i(LIB_ACCESS_MODULE, "addMoreBooksToExistingBook - numberOfBooks = " + numberOfBooks);

        if (cursor == null) {
            Log.i(LIB_ACCESS_MODULE, "addMoreBooksToExistingBook - cursor is null");
            return false;
        }

        if (cursor.getCount() != 1) {
            Log.i(LIB_ACCESS_MODULE, "addMoreBooksToExistingBook - cursor should contain only one book");
            return false;
        }

        cursor.moveToFirst();
        int totalBooks = numberOfBooks + Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TOTAL_BOOKS)));
        int availableBooks = numberOfBooks + Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVAILABLE_BOOKS)));
        int serialNo = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));

        ContentValues addMoreBooks = new ContentValues();
        addMoreBooks.put(COLUMN_NAME_TOTAL_BOOKS, totalBooks);
        addMoreBooks.put(COLUMN_NAME_AVAILABLE_BOOKS, availableBooks);

        String[] whereArgs = {String.valueOf(serialNo)};
        if (db.update(LIB_ACCESS_DB_TABLE_NAME, addMoreBooks, COLUMN_NAME_ID + " = ?", whereArgs) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor listAllBooks() {
        SQLiteDatabase db = getWritableDatabase();

        Log.i(LIB_ACCESS_MODULE, "listAllBooks - called");

        Cursor cursor = db.query(LIB_ACCESS_DB_TABLE_NAME, null, null, null, null, null, COLUMN_NAME_BOOK_CATEGORY);
        return cursor;
    }

    public Cursor listCheckedOutBooksToAStudent(Student student) {

        Cursor cursor = null;
        String selection = "";
        ArrayList<String> selectionArgsList = new ArrayList<String>();

        Log.i(LIB_ACCESS_MODULE, "listCheckedOutBooksToAStudent - called");
        Log.i(LIB_ACCESS_MODULE, "listCheckedOutBooksToAStudent - received student " + student.toString());

        SQLiteDatabase db = getWritableDatabase();

        if ((student.StudentFirstName != null) && (student.StudentFirstName.length() != 0)) {
            selection = COLUMN_NAME_ISSUED_TO + " LIKE ? COLLATE NOCASE";
            selectionArgsList.add("%" + student.StudentFirstName + "%");
        }

        if ((student.StudentSurname != null) && (student.StudentSurname.length() != 0)) {
            if ((student.StudentFirstName != null) && (student.StudentFirstName.length() != 0)) {
                selection += " OR ";
            }
            selection += COLUMN_NAME_ISSUED_TO + " LIKE ? COLLATE NOCASE";
            selectionArgsList.add("%" + student.StudentSurname + "%");
        }

        if (selectionArgsList.size() == 0) {
            return null;
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);
        Log.i(LIB_ACCESS_MODULE, "listCheckedOutBooksToAStudent - Calling db.query");
        cursor = db.query(LIB_ACCESS_DB_TABLE_NAME, null, selection, selectionArgs, null, null, null);

        return cursor;
    }


    public Cursor exactSearchABook(Book book) {

        Cursor cursor = null;
        String selection = "";
        ArrayList<String> selectionArgsList = new ArrayList<String>();

        Log.i(LIB_ACCESS_MODULE, "exactSearchABook - called");
        Log.i(LIB_ACCESS_MODULE, "exactSearchABook - received book" + book.toString());

        SQLiteDatabase db = getWritableDatabase();

        if (book.BookTitle.length() != 0) {
            selection += COLUMN_NAME_BOOK_TITLE +" = ? COLLATE NOCASE ";
            selectionArgsList.add(book.BookTitle);
        }

        if (book.BookAuthor.length() != 0) {
            if (book.BookTitle.length() != 0) {
                selection += " AND ";
            }
            selection += COLUMN_NAME_BOOK_AUTHOR +" = ? COLLATE NOCASE ";
            selectionArgsList.add(book.BookAuthor);
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);

        Log.i(LIB_ACCESS_MODULE, "exactSearchABook - Calling db.query");
        cursor = db.query(LIB_ACCESS_DB_TABLE_NAME, null, selection, selectionArgs, null, null, COLUMN_NAME_BOOK_CATEGORY);

        return cursor;
    }

    public Cursor searchABook(Book book) {

        Cursor cursor = null;
        String selection = "";
        ArrayList<String> selectionArgsList = new ArrayList<String>();

        Log.i(LIB_ACCESS_MODULE, "searchABook - called");
        Log.i(LIB_ACCESS_MODULE, "searchABook - received book" + book.toString());

        SQLiteDatabase db = getWritableDatabase();

        if ((book.BookTitle != null) && (book.BookTitle.length() != 0)) {
            selection += COLUMN_NAME_BOOK_TITLE + " LIKE ? COLLATE NOCASE";
            selectionArgsList.add("%" + book.BookTitle + "%");
        }

        if (book.BookAuthor.length() != 0) {
            if (book.BookTitle.length() != 0) {
                selection += " OR ";
            }
            selection += COLUMN_NAME_BOOK_AUTHOR + " LIKE ? COLLATE NOCASE";
            selectionArgsList.add("%" + book.BookAuthor + "%");
        }

        if (book.BookCategory.length() != 0) {
            if ((book.BookTitle.length() != 0) || (book.BookAuthor.length() != 0) ) {
                selection += " OR ";
            }
            selection += COLUMN_NAME_BOOK_CATEGORY + " LIKE ? COLLATE NOCASE";
            selectionArgsList.add("%" + book.BookCategory + "%");
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);

        Log.i(LIB_ACCESS_MODULE, "searchABook - Calling db.query");
        cursor = db.query(LIB_ACCESS_DB_TABLE_NAME, null, selection, selectionArgs, null, null, COLUMN_NAME_BOOK_CATEGORY);

        return cursor;
    }

    public boolean checkIn(Book book, Student student ) {
        Cursor cursor;
        SQLiteDatabase db = getWritableDatabase();

        Log.i(LIB_ACCESS_MODULE, "checkIn - called");

        Log.i(LIB_ACCESS_MODULE, "checkIn - received book" + book.toString());
        Log.i(LIB_ACCESS_MODULE, "checkIn - received student" + student.toString());

        Log.i(LIB_ACCESS_MODULE, "Calling exactSearch method");
        cursor = exactSearchABook(book);

        if ((cursor == null) || ((cursor != null) && (cursor.getCount() != 1))) {
            Log.e(LIB_ACCESS_MODULE, "checkIn - no books found");
            return false;
        }

        cursor.moveToFirst();
        String issuedTo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ISSUED_TO));

        // prevent checking in same book if  user press select button more than once
        if ((issuedTo != null) && (!issuedTo.contains(student.toString()))) {
            Log.e(LIB_ACCESS_MODULE, "checkIn - the book, " + book + " already checked in by, " + student);
            return false;
        }

        int totalBooks = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TOTAL_BOOKS)));
        int availableBooks = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVAILABLE_BOOKS)));
        Log.i(LIB_ACCESS_MODULE, "checkIn - currently issued to : \n" + issuedTo);

        if (totalBooks == availableBooks) {
            Log.e(LIB_ACCESS_MODULE, "checkIn - this book probably already checked in");
            return false;
        }
        ContentValues checkIn = new ContentValues();
        checkIn.put(COLUMN_NAME_AVAILABLE_BOOKS, availableBooks + 1);
        checkIn.put(COLUMN_NAME_ISSUED_TO, removeStudentFromCheckedOutList(issuedTo, student));

        String selection = COLUMN_NAME_BOOK_TITLE + "=? AND " + COLUMN_NAME_BOOK_AUTHOR + "=? AND " + COLUMN_NAME_BOOK_CATEGORY + "=?";
        String[] selectionArg = {book.BookTitle, book.BookAuthor, book.BookCategory};

        if (db.update(LIB_ACCESS_DB_TABLE_NAME, checkIn, selection, selectionArg) > 0) {
            Log.i(LIB_ACCESS_MODULE, "checkIn - successfully checked in");
            return true;
        }
        else {
            Log.i(LIB_ACCESS_MODULE, "checkIn - failed to check in");
            return false;
        }
    }

    public boolean checkOut(Book book, Student student ) {
        Cursor cursor;
        SQLiteDatabase db = getWritableDatabase();

        Log.i(LIB_ACCESS_MODULE, "checkOut - called");

        Log.i(LIB_ACCESS_MODULE, "checkOut - received book, " + book.toString());
        Log.i(LIB_ACCESS_MODULE, "checkOut - received student, " + student.toString());

        Log.i(LIB_ACCESS_MODULE, "Calling extactSearch method");

        cursor = exactSearchABook(book);
        if ((cursor == null) || ((cursor != null) && (cursor.getCount() != 1))) {
            Log.e(LIB_ACCESS_MODULE, "checkOut - no books found");
            return false;
        }

        cursor.moveToFirst();
        String issuedTo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ISSUED_TO));

        // prevent issuing of two of same books to same student.
        if ((issuedTo != null) && (issuedTo.contains(student.toString()))) {
            Log.e(LIB_ACCESS_MODULE, "checkOut - the book, " + book + " already issued to, " + student);
            return false;
        }

        int availableBooks = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_AVAILABLE_BOOKS)));
        Log.i(LIB_ACCESS_MODULE, "checkOut - currently issued to : \n" + issuedTo);

        if (availableBooks == 0) {
            Log.e(LIB_ACCESS_MODULE, "checkOut - there are no books left");
            return false;
        }

        ContentValues checkOut = new ContentValues();
        checkOut.put(COLUMN_NAME_AVAILABLE_BOOKS, availableBooks - 1);
        if ((issuedTo == null) || (issuedTo != null) && (issuedTo.length() == 0)) {
            checkOut.put(COLUMN_NAME_ISSUED_TO, addStudentToCheckedOutList(student));
        }
        else {
            checkOut.put(COLUMN_NAME_ISSUED_TO, issuedTo + addStudentToCheckedOutList(student));
        }
        String selection = COLUMN_NAME_BOOK_TITLE + "=? AND " + COLUMN_NAME_BOOK_AUTHOR + "=? AND " + COLUMN_NAME_BOOK_CATEGORY + "=?";
        String[] selectionArg = {book.BookTitle, book.BookAuthor, book.BookCategory};

        if (db.update(LIB_ACCESS_DB_TABLE_NAME, checkOut, selection, selectionArg) > 0) {
            Log.i(LIB_ACCESS_MODULE, "checkOut - successfully checked in");
            return true;
        }
        else {
            Log.i(LIB_ACCESS_MODULE, "checkOut - failed to check in");
            return false;
        }
    }

    private String addStudentToCheckedOutList(Student student) {
        Log.i(LIB_ACCESS_MODULE, "addStudentToCheckedOutList - returning string, " + student.toString());
        return student.toString();
    }

    private String removeStudentFromCheckedOutList(String issuedTo, Student student) {
        issuedTo = issuedTo.replace(student.toString(), "");
        Log.i(LIB_ACCESS_MODULE, "removeStudentFromCheckedOutList - returning string, " + issuedTo);
        return issuedTo;
    }
}
