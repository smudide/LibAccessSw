package org.dongari.libaccesssw;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by kmudide on 11/29/2015.
 */
public class LibAccessHelper {

    public LibAccessHelper() {

    }

    public Space createSpaceView(Context context, int row, int column, int ems)
    {
        Space space = new Space(context);
        TableRow.LayoutParams viewParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);

        space.setId(Integer.parseInt(Integer.toString(row) + Integer.toString(column)));
        viewParams.setMargins(2, 2, 2, 2);
        space.setLayoutParams(viewParams);

        if (row % 2 == 0) {
            space.setBackgroundColor(Color.GREEN);
        }
        else {
            space.setBackgroundColor(Color.GRAY);
        }

        return space;
    }

    public TextView createTextView(Context context, String text, int row, int column, int ems)
    {
        TextView textView = new TextView(context);
        TableRow.LayoutParams viewParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);

        viewParams.setMargins(2, 2, 2, 2);
        textView.setLayoutParams(viewParams);

        textView.setText(text);
        textView.setId(Integer.parseInt(Integer.toString(row) + Integer.toString(column)));
        textView.setGravity(Gravity.CENTER);
        textView.setEms(ems);

        if (row % 2 == 0) {
            textView.setBackgroundColor(Color.GREEN);
        }
        else {
            textView.setBackgroundColor(Color.GRAY);
        }

        return textView;
    }

    public Button createButton(Context context, String text,  int row, int column, int ems, View.OnClickListener onClickListener)
    {
        Button button = new Button(context);
        TableRow.LayoutParams viewParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);

        viewParams.setMargins(2, 2, 2, 2);
        button.setLayoutParams(viewParams);

        button.setId(Integer.parseInt(Integer.toString(row) + Integer.toString(column)));
        button.setText(text);
        button.setEms(ems);
        button.setOnClickListener(onClickListener);
        return button;
    }
}
