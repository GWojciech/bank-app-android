package pl.kielce.tu.pai2.mobile.bank.bankapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initListeners();
    }

    private void initListeners(){
        Button button = (Button) findViewById(R.id.operations_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, OperationsActivity.class));
            }
        });

        button = (Button) findViewById(R.id.bank_transfer_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, BankTransferActivity.class));
            }
        });

        button = (Button) findViewById(R.id.cards_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, CreditCardActivity.class));
            }
        });
    }
}
