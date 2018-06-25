package pl.kielce.tu.pai2.mobile.bank.bankapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ChangePinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        Intent intent = getIntent();
        String pinCode = intent.getStringExtra("pinCode");
        TextView textView = findViewById(R.id.textViewChangePinTest);
        textView.setText("KOD PIN: "+ pinCode);
    }
}
