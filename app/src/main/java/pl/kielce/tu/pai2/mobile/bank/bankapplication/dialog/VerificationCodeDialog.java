package pl.kielce.tu.pai2.mobile.bank.bankapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.R;

public class VerificationCodeDialog extends AppCompatDialogFragment {

    private static String code;
    private EditText editText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.verification_code_dialog, null);
        editText = (EditText) view.findViewById(R.id.editTextDialog);
        builder.setView(view)
                .setTitle("Wpisz kod potwierdzający")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Anulowano", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Potwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        code = editText.getText().toString();
                        dialog.dismiss();


                    }
                });

        return builder.create();
    }

    public String getCode(){return code;}
}
