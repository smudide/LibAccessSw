package org.dongari.libaccesssw;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class AddABookActivity extends ActionBarActivity {

    public final String LIB_ACCESS_MODULE = "LAMAddABookActivity";

    Button AddANewBookButton;
    Button AddMoreBooksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_abook);
        addListeners();
    }

    public void addListeners() {
        AddANewBookButton = (Button) findViewById(R.id.ABAAddANewBookBtnId);
        AddMoreBooksButton = (Button) findViewById(R.id.ABAAddMoreBooksToExistingBtnId);

        AddANewBookButton.setOnClickListener(new AddANewBookBtnListener());
        AddMoreBooksButton.setOnClickListener(new AddMoreBooksBtnListener());
    }

    public class AddANewBookBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click
            Log.i(LIB_ACCESS_MODULE, "AddANewBookBtnListener class onClick - called");
            Intent intent = new Intent(getApplicationContext(), AddABookSubActivity.class);
            startActivity(intent);
        }
    }

    public class AddMoreBooksBtnListener implements View.OnClickListener {
        public void onClick(View v) {
            // Perform action on click
            Log.i(LIB_ACCESS_MODULE, "AddMoreBooksBtnListener class onClick - called");
            LibraryTransaction.getSingleton().setPurpose(LibraryTransaction.LIST_PURPOSE_ADD_MODE_BOOKS);
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_abook, menu);
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
