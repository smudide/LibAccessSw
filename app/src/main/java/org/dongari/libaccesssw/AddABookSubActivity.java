package org.dongari.libaccesssw;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AddABookSubActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    public final String LIB_ACCESS_MODULE = "LAMAddABookSubActivity";

    EditText BookTitleEditText;
    EditText BookAuthorEditText;
    Spinner  BookCategorySpinner;
    EditText NumberOfBooksEditText;

    Button AddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_abook_sub);

        addListeners();
    }

    public void addListeners() {
        BookTitleEditText = (EditText) findViewById(R.id.ABSABookTitleEditTextId);
        BookAuthorEditText = (EditText) findViewById(R.id.ABSABookAuthorEditTextId);
        BookCategorySpinner = (Spinner) findViewById(R.id.ABSABookCategorySpinnerId);
        NumberOfBooksEditText = (EditText) findViewById(R.id.ABSANumberOfBooksEditTextId);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.book_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BookCategorySpinner.setAdapter(adapter);

        BookCategorySpinner.setOnItemSelectedListener(this);

        AddBtn = (Button) findViewById(R.id.ABSAAddBtnId);
        AddBtn.setOnClickListener(new AddBtnListener());
    }

    public class AddBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click

            Log.i(LIB_ACCESS_MODULE, "AddBtnListener class onClick - called");

            String bookTitle = BookTitleEditText.getText().toString();
            String bookAuthor = BookAuthorEditText.getText().toString();
            String bookCategory = String.valueOf(BookCategorySpinner.getSelectedItem());
            String numberOfBooksStr = NumberOfBooksEditText.getText().toString();
            int numberOfBooks;

            if (bookTitle != null) {
                bookTitle = bookTitle.trim();
            }

            if (bookAuthor != null) {
                bookAuthor = bookAuthor.trim();
            }

            if ((bookTitle == null) ||((bookTitle != null) && (bookTitle.length() == 0))) {
                new AlertMessage().display(AddABookSubActivity.this, "Error", "Book title is missing", null);
                return;
            }
            if ((bookAuthor == null) || ((bookAuthor != null) && (bookAuthor.length() == 0))) {
                new AlertMessage().display(AddABookSubActivity.this, "Error", "Book author is missing", null);
                return;
            }
            if ((bookCategory == null) || ((bookCategory != null) && (bookCategory.equals(new String("Choose Book Category"))))) {
                new AlertMessage().display(AddABookSubActivity.this, "Error", "Book Category is missing", null);
                return;
            }

            try {
                numberOfBooks = Integer.valueOf(numberOfBooksStr);
            }
            catch (NumberFormatException ex) {
                new AlertMessage().display(AddABookSubActivity.this, "Error", "Number of books must be positive integer", null);
                return;
            }

            if (numberOfBooks <= 0) {
                new AlertMessage().display(AddABookSubActivity.this, "Error", "Number of books must be a positive integer", null);
                return;
            }

            Log.i(LIB_ACCESS_MODULE, "AddBtnListener - bookTitle = " + bookTitle + ", bookAuthor = " + bookAuthor + ", bookCategory = "
                    + bookCategory + ", NumberOfBooks " + numberOfBooks);

            Book book = new Book(bookTitle, bookAuthor, bookCategory);

            // search whether this book was added before. If it's not a new book, then increment the count
            Cursor cursor = LibAccessDbHelper.getSingleton(getApplicationContext()).exactSearchABook(book);

            boolean retCode;
            if ((cursor == null) || ((cursor != null) && (cursor.getCount() == 0))) {
                Log.i(LIB_ACCESS_MODULE, "AddBtnListener - no book found, creating new entry (" + cursor.getCount() +")");
                retCode = LibAccessDbHelper.getSingleton(getApplicationContext()).addANewBook(book, numberOfBooks);
            }
            else {
                Log.i(LIB_ACCESS_MODULE, "AddBtnListener - same book found, incrementing count (" + cursor.getCount() +")");
                retCode = LibAccessDbHelper.getSingleton(getApplicationContext()).addMoreBooksToExistingBook(cursor, numberOfBooks);
            }
            if (retCode) {
                // put success message
                new AlertMessage().display(AddABookSubActivity.this, "Success", book.toString() + " is added to the library", AddABookActivity.class);
            }
            else {
                new AlertMessage().display(AddABookSubActivity.this, "Error", book.toString() + " could not added to the library", AddABookActivity.class);
            }
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_abook_sub, menu);
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
