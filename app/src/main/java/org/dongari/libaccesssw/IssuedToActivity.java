package org.dongari.libaccesssw;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;


public class IssuedToActivity extends ActionBarActivity  implements View.OnClickListener{

    public static final int SERIAL_NO_COLUMN_NUM = 0;
    public static final int STUDENT_FULL_NAME_WITH_CLASS_STRING_COLUMN_NUM = 1;
    public static final int SELECT_BUTTON_COLUMN_NUM = 2;

    public static final int SERIAL_NO_WIDTH_EMS = 2;
    public static final int STUDENT_FULL_NAME_WITH_CLASS_STRING_WIDTH_EMS = 50;
    public static final int SELECT_BUTTON_WIDTH_EMMS = 8;

    Cursor CursorToBooksList;
    int    UserInterestedRow;
    Book   CheckingInBook;
    String[] IssuedTo;
    Student BookReturningStudent;

    ScrollView verticalScrollView;
    HorizontalScrollView horizontalScrollView;

    TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_to);

        CursorToBooksList = LibraryTransaction.getSingleton().getCursorToBooksList();
        UserInterestedRow = LibraryTransaction.getSingleton().getUserInterestedRow();
        CheckingInBook = LibraryTransaction.getSingleton().getCheckingInBook();

        verticalScrollView = (ScrollView)findViewById(R.id.ITAVSV);
        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.ITAHSV);
        tableLayout = (TableLayout) findViewById(R.id.ITATableId);


        displayBorrowers();
    }

    public void displayBorrowers() {
        TableRow    tableRow;
        TableRow.LayoutParams tableRowParams;

        verticalScrollView.setVerticalScrollBarEnabled(true);
        horizontalScrollView.setHorizontalScrollBarEnabled(true);

        // info row
        tableRow = new TableRow(this);
        tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(tableRowParams);

        tableRow.addView(new LibAccessHelper().createTextView(this, "   ", 0, SERIAL_NO_COLUMN_NUM, SERIAL_NO_WIDTH_EMS));

        tableRow.addView(new LibAccessHelper().createTextView(this, "Book, " + CheckingInBook.toString(), 0,
                STUDENT_FULL_NAME_WITH_CLASS_STRING_COLUMN_NUM, STUDENT_FULL_NAME_WITH_CLASS_STRING_WIDTH_EMS));

        tableRow.addView(new LibAccessHelper().createTextView(this, "   ", 0, SELECT_BUTTON_COLUMN_NUM, SELECT_BUTTON_WIDTH_EMMS));
        tableLayout.addView(tableRow);

        // header row
        tableRow = new TableRow(this);
        tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(tableRowParams);

        tableRow.addView(new LibAccessHelper().createTextView(this, "S.No", 1, SERIAL_NO_COLUMN_NUM, SERIAL_NO_WIDTH_EMS));
        tableRow.addView(new LibAccessHelper().createTextView(this, "Issued To", 1, STUDENT_FULL_NAME_WITH_CLASS_STRING_COLUMN_NUM,
                STUDENT_FULL_NAME_WITH_CLASS_STRING_WIDTH_EMS));

        tableRow.addView(new LibAccessHelper().createTextView(this, "Select student returning above book", 1, SELECT_BUTTON_COLUMN_NUM, SELECT_BUTTON_WIDTH_EMMS));

        tableLayout.addView(tableRow);

        CursorToBooksList.moveToPosition(UserInterestedRow);

        IssuedTo = CursorToBooksList.getString(CursorToBooksList.getColumnIndex(LibAccessDbHelper.COLUMN_NAME_ISSUED_TO)).split("\n");

        for (int row = 1; row <= IssuedTo.length; row++) {
            tableRow = new TableRow(this);
            tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
            tableRow.setLayoutParams(tableRowParams);

            tableRow.addView(new LibAccessHelper().createTextView(this, row + ".", 1 + row, SERIAL_NO_COLUMN_NUM, SERIAL_NO_WIDTH_EMS));
            tableRow.addView(new LibAccessHelper().createTextView(this, IssuedTo[row - 1], 1 + row, STUDENT_FULL_NAME_WITH_CLASS_STRING_COLUMN_NUM,
                    STUDENT_FULL_NAME_WITH_CLASS_STRING_COLUMN_NUM));

            tableRow.addView(new LibAccessHelper().createButton(this, "Select", 1 + row, SELECT_BUTTON_COLUMN_NUM, SELECT_BUTTON_WIDTH_EMMS, this));

            tableLayout.addView(tableRow);
        }
    }

    public void onClick(View v) {
        int row = v.getId()/10;
        String studentStr = IssuedTo[row - 2];

        BookReturningStudent = new Student();
        BookReturningStudent.splitStringIntoStudent(studentStr);
        if (LibAccessDbHelper.getSingleton(this).checkIn(CheckingInBook, BookReturningStudent)) {
             new AlertMessage().display(IssuedToActivity.this, "Success", "Successfully checked in", MainActivity.class);
        }
        else {
             new AlertMessage().display(IssuedToActivity.this, "Error", "Failed to check in", null);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ListAllBooksActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_issued_to, menu);
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
