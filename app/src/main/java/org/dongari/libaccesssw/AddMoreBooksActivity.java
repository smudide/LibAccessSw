package org.dongari.libaccesssw;

import android.content.Intent;
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
import android.widget.TextView;


public class AddMoreBooksActivity extends ActionBarActivity {
    public final String LIB_ACCESS_MODULE = "LAMAddMoreBooksActivity";

    TextView BookTitleText;
    TextView BookAuthorText;
    TextView  BookCategoryText;
    EditText NumberOfBooksEditText;

    Button AddBtn;
    Book   IncrementingBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_books);

        addListeners();
    }

    public void addListeners() {
        BookTitleText = (TextView) findViewById(R.id.AMBABookTitleTextId);
        BookAuthorText = (TextView) findViewById(R.id.AMBABookAuthorTextId);
        BookCategoryText = (TextView) findViewById(R.id.AMBABookCategoryTextId);

        NumberOfBooksEditText = (EditText) findViewById(R.id.AMBANumberOfBooksEditTextId);

        IncrementingBook = LibraryTransaction.getSingleton().getIncrementingBook();
        BookTitleText.setText(IncrementingBook.BookTitle);
        BookAuthorText.setText(IncrementingBook.BookAuthor);
        BookCategoryText.setText(IncrementingBook.BookCategory);


        AddBtn = (Button) findViewById(R.id.AMBAAddBtnId);
        AddBtn.setOnClickListener(new AddBtnListener());
    }

    public class AddBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click

            Log.i(LIB_ACCESS_MODULE, "AddBtnListener class onClick - called");

            String numberOfBooksStr = NumberOfBooksEditText.getText().toString();
            int numberOfBooks;


            try {
                numberOfBooks = Integer.valueOf(numberOfBooksStr);
            }
            catch (NumberFormatException ex) {
                new AlertMessage().display(AddMoreBooksActivity.this, "Error", "Number of books must be positive integer", null);
                return;
            }

            if (numberOfBooks <= 0) {
                new AlertMessage().display(AddMoreBooksActivity.this, "Error", "Number of books must be a positive integer", null);
                return;
            }

            Log.i(LIB_ACCESS_MODULE, "AddBtnListener - adding more books to, " + IncrementingBook.toString() + ", NumberOfBooks " + numberOfBooks);

            Cursor cursor = LibAccessDbHelper.getSingleton(getApplication()).exactSearchABook(IncrementingBook);

            boolean retCode = LibAccessDbHelper.getSingleton(getApplicationContext()).addMoreBooksToExistingBook(cursor, numberOfBooks);
            if (retCode) {
                // put success message
                new AlertMessage().display(AddMoreBooksActivity.this, "Success", "total books incremented for the book, " + IncrementingBook.toString(),
                        AddABookActivity.class);
            }
            else {
                new AlertMessage().display(AddMoreBooksActivity.this, "Error", "failed to add more books for the book" + IncrementingBook.toString(), null);
            }
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
        getMenuInflater().inflate(R.menu.menu_add_more_books, menu);
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
