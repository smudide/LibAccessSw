package org.dongari.libaccesssw;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    public final String LIB_ACCESS_MODULE = "LAMMainActivity";

    Button SearchBtn;
    Button CheckOutBtn;
    Button CheckInBtn;
    Button AddABookBtn;
    Button ListAllBooksBtn;
    Button ContactUsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create lib access db
        LibAccessDbHelper.getSingleton(this);
        addListeners();
    }

    public void addListeners() {
        SearchBtn = (Button) findViewById(R.id.MASearchBtnId);
        CheckOutBtn = (Button) findViewById(R.id.MACheckOutBtnId);
        CheckInBtn = (Button) findViewById(R.id.MACheckInBtnId);
        AddABookBtn = (Button) findViewById(R.id.MAAddABookBtnId);
        ListAllBooksBtn = (Button) findViewById(R.id.MAListAllBooksBtnId);
        ContactUsBtn = (Button) findViewById(R.id.MAContactUsBtnId);

        SearchBtn.setOnClickListener(new SearchBtnListener());
        CheckOutBtn.setOnClickListener(new CheckOutBtnListener());
        CheckInBtn.setOnClickListener(new CheckInBtnListener());
        AddABookBtn.setOnClickListener(new AddABookBtnListener());
        ListAllBooksBtn.setOnClickListener(new ListAllBooksBtnListener());
        ContactUsBtn.setOnClickListener(new ContactUsBtnListener());
    }

    public class SearchBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click
            Log.i(LIB_ACCESS_MODULE, "SearchBtnListener class onClick is called");
            LibraryTransaction.getSingleton().setCursorToBooksList(null);
            LibraryTransaction.getSingleton().setPurpose(LibraryTransaction.LIST_PURPOSE_ALL);
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }
    }

    public class CheckOutBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click
            Log.i(LIB_ACCESS_MODULE, "CheckOutBtnListmer class onClick is called");
            LibraryTransaction.getSingleton().setCursorToBooksList(null);
            LibraryTransaction.getSingleton().setPurpose(LibraryTransaction.LIST_PURPOSE_CHECK_OUT);
            Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
            startActivity(intent);
        }
    }

    public class CheckInBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click
            Log.i(LIB_ACCESS_MODULE, "CheckInBtnListener class onClick is called");
            LibraryTransaction.getSingleton().setCursorToBooksList(null);
            LibraryTransaction.getSingleton().setPurpose(LibraryTransaction.LIST_PURPOSE_CHECK_IN);
            Intent intent = new Intent(getApplicationContext(), CheckInActivity.class);
            startActivity(intent);
        }
    }

    public class AddABookBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click
            Log.i(LIB_ACCESS_MODULE, "AddABookBtnListener class onClick is called");
            Intent intent = new Intent(getApplicationContext(), AddABookActivity.class);
            startActivity(intent);
        }
    }

    public class ListAllBooksBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click
            Log.i(LIB_ACCESS_MODULE, "ListAllBooksBtnListener class onClick is called");
            LibraryTransaction.getSingleton().setCursorToBooksList(null);
            LibraryTransaction.getSingleton().setPurpose(LibraryTransaction.LIST_PURPOSE_ALL);
            Intent intent = new Intent(getApplicationContext(), ListAllBooksActivity.class);
            startActivity(intent);
        }
    }

    public class ContactUsBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click
            Log.i(LIB_ACCESS_MODULE, "ContactUsBtnListener class onClick is called");
            Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
