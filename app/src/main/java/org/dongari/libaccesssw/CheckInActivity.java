package org.dongari.libaccesssw;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CheckInActivity extends ActionBarActivity {

    public final String LIB_ACCESS_MODULE = "LAMCheckInActivity";

    EditText StudentFirstNameEditText;
    EditText StudentSurnameEditText;
    Button   FindBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        addListeners();
    }
    public void addListeners() {
        StudentFirstNameEditText = (EditText) findViewById(R.id.CIStudentFirstNameEditTextId);
        StudentSurnameEditText = (EditText) findViewById(R.id.CIStudentSurnameEditTextId);
        FindBtn = (Button) findViewById(R.id.CIFindBtnId);
        FindBtn.setOnClickListener(new FindBtnListener());
    }

    public class FindBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click

            Log.i(LIB_ACCESS_MODULE, "FindBtnListener class onClick - called");

            String studentFirstName = StudentFirstNameEditText.getText().toString();
            String studentSurname = StudentSurnameEditText.getText().toString();

            if ((studentFirstName == null) || (studentSurname == null)) {
                new AlertMessage().display(CheckInActivity.this, "Error", "Student first name and/or surname missing", null);
                return;
            }
            studentFirstName = studentFirstName.trim();
            studentSurname = studentSurname.trim();

            if ((studentFirstName.length() == 0) && (studentSurname.length() == 0)) {
                new AlertMessage().display(CheckInActivity.this, "Error", "Both student first name and surname are missing. Enter at least on filed", null);
                return;
            }

            Student student = new Student(studentFirstName, studentSurname, "0");

            Log.i(LIB_ACCESS_MODULE, "FindBtnListener - student = " + student.toString());

            Cursor cursor = LibAccessDbHelper.getSingleton(CheckInActivity.this).listCheckedOutBooksToAStudent(student);

            if ((cursor != null) && (cursor.getCount() > 0)) {
                LibraryTransaction.getSingleton().setCursorToBooksList(cursor);
                LibraryTransaction.getSingleton().setPurpose(LibraryTransaction.LIST_PURPOSE_CHECK_IN);
                Intent intent = new Intent(getApplicationContext(), ListAllBooksActivity.class);
                startActivity(intent);
            }
            else {
                new AlertMessage().display(CheckInActivity.this, "Error", "No book has been checked out to " + student.toString(), null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_in, menu);
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
