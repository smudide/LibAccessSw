package org.dongari.libaccesssw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by kmudide on 11/10/2015.
 */
public class AlertMessage {
    public void display(final Context context, String title, String message, final Class<?> cls ) {
        AlertDialog.Builder alertMessage = new AlertDialog.Builder(context);

        alertMessage.setMessage(message);
        alertMessage.setTitle(title);
        alertMessage.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
                dialog.dismiss();
                if (cls != null) {
                    Intent intent = new Intent(context, cls);
                    context.startActivity(intent);
                }
            }
        });
        alertMessage.create();
        alertMessage.show();
    }
}
