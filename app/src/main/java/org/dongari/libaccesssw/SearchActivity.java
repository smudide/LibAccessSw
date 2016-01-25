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

import static java.lang.String.*;


public class SearchActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
    public final String LIB_ACCESS_MODULE = "LAMSearchActivity";
    EditText BookTitleEditText;
    EditText BookAuthorEditText;
    Spinner  BookCategorySpinner;

    Button   SearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        addListeners();
    }

    public void addListeners() {
        BookTitleEditText = (EditText) findViewById(R.id.SABookTitleEditTextId);
        BookAuthorEditText = (EditText) findViewById(R.id.SABookAuthorEditTextId);
        BookCategorySpinner = (Spinner) findViewById(R.id.SABookCategorySpinnerId);
        SearchBtn = (Button) findViewById(R.id.SASearchBtnId);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.book_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BookCategorySpinner.setAdapter(adapter);

        BookCategorySpinner.setOnItemSelectedListener(this);
        SearchBtn.setOnClickListener(new SearchBtnListener());
    }

    public class SearchBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click

            Log.i(LIB_ACCESS_MODULE, "SearchBtnListener class onClick - called");

            String bookTitle = BookTitleEditText.getText().toString();
            String bookAuthor = BookAuthorEditText.getText().toString();
            String bookCategory = String.valueOf(BookCategorySpinner.getSelectedItem());

            if ((bookTitle == null) || (bookAuthor == null) || (bookCategory == null)) {
                new AlertMessage().display(SearchActivity.this, "Error", "Book title, author and/or category is missing", null);
                return;
            }

            bookTitle = bookTitle.trim();
            bookAuthor = bookAuthor.trim();

            if ((bookTitle.length() == 0) && (bookAuthor.length() == 0) && (bookCategory.equals(new String("Choose Book Category")))) {
                new AlertMessage().display(SearchActivity.this, "Error", "Book title, author and category are missing. Enter at lease one field", null);
                return;
            }
            Log.i(LIB_ACCESS_MODULE, "SearchBtnListener - bookTitle.length = " + bookTitle.length() +
                    ", bookAuthor.length = " + bookAuthor.length() + ", bookCategory.length = " + bookCategory.length());
            Log.i(LIB_ACCESS_MODULE, "SearchBtnListener - bookTitle = " + bookTitle + ", bookAuthor = " + bookAuthor + ", bookCategory = " + bookCategory);

            Book book = new Book(bookTitle, bookAuthor, bookCategory);
            Cursor cursor = LibAccessDbHelper.getSingleton(getApplicationContext()).searchABook(book);
            if ((cursor == null) || ((cursor != null) && (cursor.getCount() == 0))) {
                // put error message
                new AlertMessage().display(SearchActivity.this, "Error", "Failed to find \n" + book.toString() + " book(s)", null);
                return;
            }
            else {
                 // purpose is already set in addABookActivity or mainActivity
                LibraryTransaction.getSingleton().setCursorToBooksList(cursor);
                Intent intent = new Intent(getApplicationContext(), ListAllBooksActivity.class);
                startActivity(intent);
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
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
