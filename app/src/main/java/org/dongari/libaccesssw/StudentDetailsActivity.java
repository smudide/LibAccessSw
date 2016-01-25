package org.dongari.libaccesssw;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class StudentDetailsActivity extends ActionBarActivity {
    public final String LIB_ACCESS_MODULE = "LAMSDActivity";

    EditText StudentFirstNameEditText;
    EditText StudentSurnameEditText;
    EditText StudentClassEditText;

    Button SubmitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        addListeners();
    }

    public void addListeners() {
        StudentFirstNameEditText = (EditText) findViewById(R.id.SDAStudentFirstNameEditTextId);
        StudentSurnameEditText = (EditText) findViewById(R.id.SDAStudentSurnametEditTextId);
        StudentClassEditText = (EditText) findViewById(R.id.SDAStudentClassEditTextId);
        SubmitBtn = (Button) findViewById(R.id.SDASubmitBtnId);

        SubmitBtn.setOnClickListener(new SubmitBtnListener());
    }

    public class SubmitBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click

            Log.i(LIB_ACCESS_MODULE, "SubmitBtnListener class onClick - called");

            String studentFirstName = StudentFirstNameEditText.getText().toString();
            String studentSurname = StudentSurnameEditText.getText().toString();
            String studentClassStr = StudentClassEditText.getText().toString();;

            if ((studentFirstName == null) || (studentSurname == null) || (studentClassStr == null)) {
                new AlertMessage().display(StudentDetailsActivity.this, "Error", "Student first name, surname and/or class is missing", null);
                return;
            }

            studentFirstName = studentFirstName.trim();
            studentSurname = studentSurname.trim();
            studentClassStr = studentClassStr.trim();

            if ((studentFirstName.length() == 0) || (studentSurname.length() == 0) || (studentClassStr.length() == 0)) {
                new AlertMessage().display(StudentDetailsActivity.this, "Error", "Student first name, surname and/or class is missing", null);
                return;
            }

            int studentClass;
            try {
                studentClass = Integer.valueOf(studentClassStr);
            }
            catch (NumberFormatException ex) {
                new AlertMessage().display(StudentDetailsActivity.this, "Error", "Student class must be a positive integer", null);
                return;
            }

            if (studentClass <= 0) {
                new AlertMessage().display(StudentDetailsActivity.this, "Error", "Student class must be a positive integer", null);
                return;
            }

            Log.i(LIB_ACCESS_MODULE, "SubmitBtnListener - studentFirstName = " + studentFirstName + ", studentSurname = "
                    + studentSurname + ", studentClass = " + studentClass);

            Book book = LibraryTransaction.getSingleton().getCheckingOutBook();
            Student student = new Student(studentFirstName, studentSurname, studentClassStr);

            boolean retCode= LibAccessDbHelper.getSingleton(getApplicationContext()).checkOut(book, student);
            if (retCode) {
                new AlertMessage().display(StudentDetailsActivity.this, "Success", "Successfully checked out. Now borrower can take the book", MainActivity.class);
            }
            else {
                new AlertMessage().display(StudentDetailsActivity.this, "Error", "Failed to check out", null);
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
        getMenuInflater().inflate(R.menu.menu_student_details, menu);
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
