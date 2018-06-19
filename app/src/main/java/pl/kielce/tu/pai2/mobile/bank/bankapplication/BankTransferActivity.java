package pl.kielce.tu.pai2.mobile.bank.bankapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.dialog.VerificationCodeDialog;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.services.InputStreamStringConverter;

public class BankTransferActivity extends AppCompatActivity {

    private EditText mAmount;
    private EditText mAccountNumber;
    private EditText mRecipient;
    private EditText mDescription;
    private EditText mStreet;
    private EditText mPostalCode;
    private EditText city;
    private boolean decision;

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
                if(isAmountValid()&&isAccountNumberValid()&&isOtherFieldsValid()) {
                    new BankTransferTask().execute((Void) null);
                    if(decision) {
                        VerificationCodeDialog verificationCodeDialog = new VerificationCodeDialog();
                        verificationCodeDialog.show(getSupportFragmentManager(), "Dialog");
                    }
                }
            }
        });

    }
    boolean isAmountValid(){
        mAmount = (EditText) findViewById(R.id.amount_EditText);
        mAmount.setError(null);
        String text = mAmount.getText().toString();
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        if(decimalPlaces<=2){
            return true;
        }
        else{
            mAmount.setError("Niepoprawna kwota!");
            return false;
        }
    }

    boolean isAccountNumberValid(){
        mAccountNumber = (EditText) findViewById(R.id.account_number_EditText);
        String text = mAccountNumber.getText().toString();
        if((text.length()==26)){
            return true;
        }
        else {
            mAccountNumber.setError("Niepoprawny nr konta!");
            return false;
        }
    }
    boolean isOtherFieldsValid() {
        mRecipient = (EditText) findViewById(R.id.recipient_EditText);
        mDescription = (EditText) findViewById(R.id.description_EditText);
        if (isNotEmptyField(mRecipient) && isNotEmptyField(mDescription)) {
            return true;
        } else
            return false;
    }


    boolean isNotEmptyField(EditText editText){
        editText.setError(null);
        if(editText.getText().toString().length()>2){
            return true;
        }
        else{
            editText.setError("Niepoprawne dane!");
            return false;
        }
    }

    public class BankTransferTask extends AsyncTask<Void, Void, Boolean> {


        JSONArray bankAccounts;


        JSONArray getClientBankAccounts(){
            URL url = null;
            try {
                url = new URL(getString(R.string.URL_address)+"/bankAccount/"+ LoginActivity.idClient);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(false);
                connection.setDoInput(true);
                JSONArray jsonArray;
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String text = InputStreamStringConverter.streamToString(in);
                in.close();

                if(connection.getResponseCode()==200) {

                    jsonArray = new JSONArray(text);
                    connection.disconnect();
                    return jsonArray;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
            }

            void send() {
                URL url = null;
                mAmount = findViewById(R.id.amount_EditText);
                mAccountNumber = findViewById(R.id.account_number_EditText);
                mRecipient = findViewById(R.id.recipient_EditText);
                mDescription = findViewById(R.id.description_EditText);
                mStreet = findViewById(R.id.street_EditText);
                mPostalCode = findViewById(R.id.postal_number_EditText);
                city = findViewById(R.id.city_EditText);
                try {

                    url = new URL(getString(R.string.URL_address) + "/transfer/register");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "text/plain");
                    connection.setRequestProperty("Accept",  "text/plain");
                    connection.setRequestProperty("Response-Type", "text/plain");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObjectToAccount = new JSONObject();
                    jsonObjectToAccount.put("recipientAccount", mAccountNumber.getText().toString());
//                    jsonObjectToAccount.put("idInternalAccount", null);
//                    jsonObjectToAccount.put("idExternalAccount", null);
                    jsonObject.put("fromAccount", bankAccounts.getJSONObject(0));
                    jsonObject.put("dateOfOrder", new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date()));
                    jsonObject.put("dateOfExecution", new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date()));
                    jsonObject.put("recipient",mRecipient.getText().toString() );
                    jsonObject.put("amount", mAmount.getText().toString());
                    jsonObject.put("address", city.getText().toString()+", "+mStreet.getText().toString()+" ,"+mPostalCode.getText().toString());
                    jsonObject.put("description",mDescription.getText().toString());
                    jsonObject.put("state","CURRENT");
                    jsonObject.put("amountStateBefore", bankAccounts.getJSONObject(0).getString("amount"));
                    jsonObject.put("toAccount", jsonObjectToAccount);
                    DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                    os.writeBytes(URLEncoder.encode(jsonObject.toString(),"UTF-8"));
                    os.flush();
                    os.close();
                    if(connection.getResponseCode()==200){

                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        String text = InputStreamStringConverter.streamToString(in);
                        in.close();
                    }
                    connection.disconnect();



                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            bankAccounts = getClientBankAccounts();
            send();


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                decision = true;
                //finish();
            } else {
                decision=false;
            }
        }

        @Override
        protected void onCancelled() {

        }
    }


}
