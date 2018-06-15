package pl.kielce.tu.pai2.mobile.bank.bankapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.dialog.VerificationCodeDialog;

public class BankTransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);
        initListener();
    }

    private void initListener() {
        Button button = (Button) findViewById(R.id.next_bank_tranfer_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificationCodeDialog verificationCodeDialog = new VerificationCodeDialog();
                verificationCodeDialog.show(getSupportFragmentManager(),"Dialog");

            }
        });
    }
}
