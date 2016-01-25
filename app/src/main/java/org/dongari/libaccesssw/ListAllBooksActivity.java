package org.dongari.libaccesssw;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;


public class ListAllBooksActivity extends ActionBarActivity {
    public final String LIB_ACCESS_MODULE = "LAMLABActivity";

    public static final int SERIAL_NO_COLUMN_NUM = 0;
    public static final int BOOK_TITLE_COLUMN_NUM = 1;
    public static final int BOOK_AUTHOR_COLUMN_NUM = 2;
    public static final int BOOK_CATEGORY_COLUMN_NUM = 3;
    public static final int TOTAL_BOOKS_COLUMN_NUM = 4;
    public static final int AVAILABLE_BOOKS_COLUMN_NUM = 5;
    public static final int VIEW_BUTTON_COLUMN_NUM = 6;
    public static final int CHECK_IN_BUTTON_COLUMN_NUM = 7;
    public static final int CHECK_OUT_BUTTON_COLUMN_NUM = 8;
    public static final int ADD_MORE_BOOKS_BUTTON_COLUMN_NUM = 9;

    public static final int SERIAL_NO_WIDTH_EMS = 2;
    public static final int BOOK_TITLE_WIDTH_EMS = 15;
    public static final int BOOK_AUTHOR_WIDTH_EMS = 9;
    public static final int BOOK_CATEGORY_WIDTH_EMS = 5;
    public static final int TOTAL_BOOKS_WIDTH_EMS = 4;
    public static final int AVAILABLE_BOOKS_WIDTH_EMS = 4;
    public static final int VIEW_BUTTON_WIDTH_EMS = 4;
    public static final int CHECK_IN_BUTTON_WIDTH_EMS = 4;
    public static final int CHECK_OUT_BUTTON_WIDTH_EMS = 4;
    public static final int ADD_MORE_BOOKS_BUTTON_WIDTH_EMS = 5;

    Cursor  CursorToBooksList;
    String  Purpose;

    ScrollView           verticalScrollView;
    HorizontalScrollView horizontalScrollView;
    TableLayout          tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_books);

        CursorToBooksList = LibraryTransaction.getSingleton().getCursorToBooksList();
        Purpose = LibraryTransaction.getSingleton().getPurpose();
        if (CursorToBooksList == null) {
            CursorToBooksList = LibAccessDbHelper.getSingleton(getApplicationContext()).listAllBooks();
            Log.i(LIB_ACCESS_MODULE, "ListAllBooksActivity class onCreate, cursor.count = " + CursorToBooksList.getCount());
            if ((CursorToBooksList == null) || ((CursorToBooksList != null) && (CursorToBooksList.getCount() == 0))) {
                // put error message
                new AlertMessage().display(ListAllBooksActivity.this, "Error", "Zero books found", MainActivity.class);
                return;
            }
        }
        verticalScrollView = (ScrollView)findViewById(R.id.LABAVSV);
        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.LABAHSV);
        tableLayout = (TableLayout)findViewById(R.id.LABATableId);
        listBooks();
     }

    public class AddMoreBooksButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int userInterestedRow = v.getId()/10 - 1 ;
            LibraryTransaction.getSingleton().setUserInterestedRow(userInterestedRow);
            CursorToBooksList.moveToPosition(userInterestedRow);

            Book book = new Book(CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_TITLE)),
                    CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_AUTHOR)),
                    CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_CATEGORY)));

            Log.i(LIB_ACCESS_MODULE, "onClick of AddMoreBooksButtonClickListener - book = " + book.toString());

            LibraryTransaction.getSingleton().setCheckingInBook(null);
            LibraryTransaction.getSingleton().setCheckingOutBook(null);
            LibraryTransaction.getSingleton().setIncrementingBook(book);

            Intent intent = new Intent(getApplicationContext(), AddMoreBooksActivity.class);
            startActivity(intent);
        }
    }

    public class ViewButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int userInterestedRow = v.getId()/10 - 1;
            LibraryTransaction.getSingleton().setUserInterestedRow(userInterestedRow);
            CursorToBooksList.moveToPosition(userInterestedRow);
            Book book = new Book(CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_TITLE)),
                    CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_AUTHOR)),
                    CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_CATEGORY)));

            LibraryTransaction.getSingleton().setCheckingInBook(null);
            LibraryTransaction.getSingleton().setCheckingOutBook(book);
            LibraryTransaction.getSingleton().setIncrementingBook(null);
            LibraryTransaction.getSingleton().setCursorToBooksList(CursorToBooksList);

            Log.i(LIB_ACCESS_MODULE, "onClick of ViewButtonClickListener - book = " + book.toString());

            Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
            startActivity(intent);
        }
    }

    public class CheckInButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int userInterestedRow = v.getId()/10 - 1;
            LibraryTransaction.getSingleton().setUserInterestedRow(userInterestedRow);
            CursorToBooksList.moveToPosition(userInterestedRow);
            Book book = new Book(CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_TITLE)),
                    CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_AUTHOR)),
                    CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_CATEGORY)));

            Log.i(LIB_ACCESS_MODULE, "onClick of CheckInButtonClickListener - book = " + book.toString());

            LibraryTransaction.getSingleton().setCheckingInBook(book);
            LibraryTransaction.getSingleton().setCheckingOutBook(null);
            LibraryTransaction.getSingleton().setIncrementingBook(null);
            LibraryTransaction.getSingleton().setCursorToBooksList(CursorToBooksList);

            Intent intent = new Intent(getApplicationContext(), IssuedToActivity.class);
            startActivity(intent);
        }
    }

    public class CheckOutButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int userInterestedRow = v.getId()/10 - 1;
            LibraryTransaction.getSingleton().setUserInterestedRow(userInterestedRow);
            CursorToBooksList.moveToPosition(userInterestedRow);
            Book book = new Book(CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_TITLE)),
                    CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_AUTHOR)),
                    CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_CATEGORY)));

            Log.i(LIB_ACCESS_MODULE, "onClick of CheckOutButtonClickListener - book = " + book.toString());
            LibraryTransaction.getSingleton().setCheckingOutBook(book);
            LibraryTransaction.getSingleton().setCheckingInBook(null);
            LibraryTransaction.getSingleton().setIncrementingBook(null);

            Intent intent = new Intent(getApplicationContext(), StudentDetailsActivity.class);
            startActivity(intent);
        }
    }

    public void listBooks() {
        TableRow tableRow;
        TableRow.LayoutParams tableRowParams;
        int row;

        Log.i(LIB_ACCESS_MODULE, "listBooks - called");

        verticalScrollView.setVerticalScrollBarEnabled(true);
        horizontalScrollView.setHorizontalScrollBarEnabled(true);

        // create header row
        tableRow = new TableRow(this);
        tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(tableRowParams);

        tableRow.addView(new LibAccessHelper().createTextView(this, "S.No", 0, SERIAL_NO_COLUMN_NUM, SERIAL_NO_WIDTH_EMS));
        tableRow.addView(new LibAccessHelper().createTextView(this, "Book Title", 0, BOOK_TITLE_COLUMN_NUM, BOOK_TITLE_WIDTH_EMS));
        tableRow.addView(new LibAccessHelper().createTextView(this, "Book Author", 0, BOOK_AUTHOR_COLUMN_NUM, BOOK_AUTHOR_WIDTH_EMS));
        tableRow.addView(new LibAccessHelper().createTextView(this, "Book Category", 0, BOOK_CATEGORY_COLUMN_NUM, BOOK_CATEGORY_WIDTH_EMS));
        tableRow.addView(new LibAccessHelper().createTextView(this, "Total Books", 0, TOTAL_BOOKS_COLUMN_NUM, TOTAL_BOOKS_WIDTH_EMS));
        tableRow.addView(new LibAccessHelper().createTextView(this, "Available Books", 0, AVAILABLE_BOOKS_COLUMN_NUM, AVAILABLE_BOOKS_WIDTH_EMS));
        tableRow.addView(new LibAccessHelper().createTextView(this, "IssuedTo", 0, VIEW_BUTTON_COLUMN_NUM, VIEW_BUTTON_WIDTH_EMS));
        if (Purpose.equals(LibraryTransaction.LIST_PURPOSE_CHECK_IN)) {
            tableRow.addView(new LibAccessHelper().createTextView(this, LibraryTransaction.LIST_PURPOSE_CHECK_IN, 0, CHECK_IN_BUTTON_COLUMN_NUM,
                    CHECK_IN_BUTTON_WIDTH_EMS));
        }
        else if (Purpose.equals(LibraryTransaction.LIST_PURPOSE_CHECK_OUT)) {
            tableRow.addView(new LibAccessHelper().createTextView(this, LibraryTransaction.LIST_PURPOSE_CHECK_OUT, 0, CHECK_OUT_BUTTON_COLUMN_NUM,
                    CHECK_OUT_BUTTON_WIDTH_EMS));
        }
        else if (Purpose.equals(LibraryTransaction.LIST_PURPOSE_ADD_MODE_BOOKS)) {
            tableRow.addView(new LibAccessHelper().createTextView(this, LibraryTransaction.LIST_PURPOSE_ADD_MODE_BOOKS, 0, ADD_MORE_BOOKS_BUTTON_COLUMN_NUM,
                    ADD_MORE_BOOKS_BUTTON_WIDTH_EMS));
        }
        else if (Purpose.equals(LibraryTransaction.LIST_PURPOSE_ALL)) {
            tableRow.addView(new LibAccessHelper().createTextView(this, LibraryTransaction.LIST_PURPOSE_CHECK_IN, 0, CHECK_IN_BUTTON_COLUMN_NUM,
                    CHECK_IN_BUTTON_WIDTH_EMS));
            tableRow.addView(new LibAccessHelper().createTextView(this, LibraryTransaction.LIST_PURPOSE_CHECK_OUT, 0, CHECK_OUT_BUTTON_COLUMN_NUM,
                    CHECK_OUT_BUTTON_WIDTH_EMS));
            tableRow.addView(new LibAccessHelper().createTextView(this, LibraryTransaction.LIST_PURPOSE_ADD_MODE_BOOKS, 0, ADD_MORE_BOOKS_BUTTON_COLUMN_NUM,
                    ADD_MORE_BOOKS_BUTTON_WIDTH_EMS));
        }

        tableLayout.addView(tableRow);
        if (CursorToBooksList.moveToFirst()) {
            row = 1;
            do {
                tableRow = new TableRow(this);
                tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                tableRow.setLayoutParams(tableRowParams);

                tableRow.addView(new LibAccessHelper().createTextView(this, Integer.toString(row), row, SERIAL_NO_COLUMN_NUM, SERIAL_NO_WIDTH_EMS));
                tableRow.addView(new LibAccessHelper().createTextView(this,
                        CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_TITLE)), row, BOOK_TITLE_COLUMN_NUM, BOOK_TITLE_WIDTH_EMS));
                tableRow.addView(new LibAccessHelper().createTextView(this,
                        CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_AUTHOR)), row, BOOK_AUTHOR_COLUMN_NUM, BOOK_AUTHOR_WIDTH_EMS));
                tableRow.addView(new LibAccessHelper().createTextView(this,
                        CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_BOOK_CATEGORY)), row, BOOK_CATEGORY_COLUMN_NUM, BOOK_CATEGORY_WIDTH_EMS));

                String totalBooksStr = new String(CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_TOTAL_BOOKS)));
                String availableBooksStr = new String(CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_AVAILABLE_BOOKS)));

                tableRow.addView(new LibAccessHelper().createTextView(this, totalBooksStr, row, BOOK_TITLE_COLUMN_NUM, TOTAL_BOOKS_WIDTH_EMS));
                tableRow.addView(new LibAccessHelper().createTextView(this, availableBooksStr, row, BOOK_AUTHOR_COLUMN_NUM, AVAILABLE_BOOKS_WIDTH_EMS));

                if (!totalBooksStr.equals(availableBooksStr)) {
                    tableRow.addView(new LibAccessHelper().createButton(this, "View", row, VIEW_BUTTON_COLUMN_NUM, VIEW_BUTTON_WIDTH_EMS, new ViewButtonClickListener()));
                } else {
                    tableRow.addView(new LibAccessHelper().createSpaceView(this, row, VIEW_BUTTON_COLUMN_NUM, VIEW_BUTTON_WIDTH_EMS));
                }

                if (Purpose.equals(LibraryTransaction.LIST_PURPOSE_CHECK_IN)) {
                    if (!totalBooksStr.equals(availableBooksStr)) {
                        tableRow.addView(new LibAccessHelper().createButton(this, LibraryTransaction.LIST_PURPOSE_CHECK_IN, row, CHECK_IN_BUTTON_COLUMN_NUM,
                                CHECK_IN_BUTTON_WIDTH_EMS, new CheckInButtonClickListener()));
                    } else {
                        tableRow.addView(new LibAccessHelper().createSpaceView(this, row, CHECK_IN_BUTTON_COLUMN_NUM, CHECK_IN_BUTTON_WIDTH_EMS));
                    }
                }

                if (Purpose.equals(LibraryTransaction.LIST_PURPOSE_CHECK_OUT)) {
                    if (!availableBooksStr.equals("0")) {
                        tableRow.addView(new LibAccessHelper().createButton(this, LibraryTransaction.LIST_PURPOSE_CHECK_OUT, row, CHECK_OUT_BUTTON_COLUMN_NUM,
                                CHECK_OUT_BUTTON_WIDTH_EMS, new CheckOutButtonClickListener()));
                    } else {
                        tableRow.addView(new LibAccessHelper().createSpaceView(this, row, CHECK_OUT_BUTTON_COLUMN_NUM, CHECK_OUT_BUTTON_WIDTH_EMS));
                    }
                }

                if (Purpose.equals(LibraryTransaction.LIST_PURPOSE_ADD_MODE_BOOKS)) {
                    tableRow.addView(new LibAccessHelper().createButton(this, LibraryTransaction.LIST_PURPOSE_ADD_MODE_BOOKS, row, ADD_MORE_BOOKS_BUTTON_COLUMN_NUM,
                            ADD_MORE_BOOKS_BUTTON_WIDTH_EMS, new AddMoreBooksButtonClickListener()));
                }
                else if (Purpose.equals(LibraryTransaction.LIST_PURPOSE_ALL)) {
                    if (!totalBooksStr.equals(availableBooksStr)) {
                        tableRow.addView(new LibAccessHelper().createButton(this, LibraryTransaction.LIST_PURPOSE_CHECK_IN, row, CHECK_IN_BUTTON_COLUMN_NUM,
                                CHECK_IN_BUTTON_WIDTH_EMS, new CheckInButtonClickListener()));
                    }
                    else {
                        tableRow.addView(new LibAccessHelper().createSpaceView(this, row, CHECK_IN_BUTTON_COLUMN_NUM, CHECK_IN_BUTTON_WIDTH_EMS));
                    }
                    if (!availableBooksStr.equals("0")) {
                        tableRow.addView(new LibAccessHelper().createButton(this, LibraryTransaction.LIST_PURPOSE_CHECK_OUT, row, CHECK_OUT_BUTTON_COLUMN_NUM,
                                CHECK_OUT_BUTTON_WIDTH_EMS, new CheckOutButtonClickListener()));
                    }
                    else {
                        tableRow.addView(new LibAccessHelper().createSpaceView(this, row, CHECK_OUT_BUTTON_COLUMN_NUM, CHECK_OUT_BUTTON_WIDTH_EMS));
                    }
                    tableRow.addView(new LibAccessHelper().createButton(this, LibraryTransaction.LIST_PURPOSE_ADD_MODE_BOOKS, row, ADD_MORE_BOOKS_BUTTON_COLUMN_NUM,
                            ADD_MORE_BOOKS_BUTTON_WIDTH_EMS, new AddMoreBooksButtonClickListener()));
                }
                tableLayout.addView(tableRow);
                row++;
            } while (CursorToBooksList.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_all_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
