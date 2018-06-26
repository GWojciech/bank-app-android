package pl.kielce.tu.pai2.mobile.bank.bankapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.text.SimpleDateFormat;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.services.InputStreamStringConverter;

public class BankTransferActivity extends AppCompatActivity {

    private EditText mAmount;
    private EditText mAccountNumber;
    private EditText mRecipient;
    private EditText mDescription;
    private EditText mStreet;
    private EditText mPostalCode;
    private EditText city;
    private EditText verificationCode;
    private View mProgressView;
    private Button executeButton;
    private Boolean getCode = true;
    private String response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);
        initListener();

    }

    private void initListener() {
        mProgressView = (View) findViewById(R.id.progress_bankTransfer);
        final Button button = (Button) findViewById(R.id.next_bank_tranfer_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAmountValid()&&isAccountNumberValid()&&isOtherFieldsValid()) {
                    setEditTexts();
                    setDisabledTexts();
                    button.setVisibility(View.GONE);
                    executeButton.setVisibility(View.VISIBLE);
                    new BankTransferTask().execute((Void) null);
                }
            }
        });

        executeButton = findViewById(R.id.execute_bank_transfer_button);
        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkVerificationCode()) {
                    new BankTransferTask().execute((Void) null);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
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

    void setEditTexts(){
        mAmount = findViewById(R.id.amount_EditText);
        mAccountNumber = findViewById(R.id.account_number_EditText);
        mRecipient = findViewById(R.id.recipient_EditText);
        mDescription = findViewById(R.id.description_EditText);
        mStreet = findViewById(R.id.street_EditText);
        mPostalCode = findViewById(R.id.postal_number_EditText);
        city = findViewById(R.id.city_EditText);
    }

    void setDisabledTexts(){
        mAmount.setKeyListener(null);
        mAccountNumber.setKeyListener(null);
        mRecipient.setKeyListener(null);
        mDescription.setKeyListener(null);
        mStreet.setKeyListener(null);
        mPostalCode.setKeyListener(null);
        city.setKeyListener(null);
        verificationCode = (EditText) findViewById(R.id.verification_code_editText);
        verificationCode.setVisibility(View.VISIBLE);
    }

    private boolean checkVerificationCode() {
        String codeFromForm = verificationCode.getText().toString();
        response = response.replaceAll("\n","");
        if(response.equals(codeFromForm)){
            response = null;
            getCode = false;
            return true;
        }
        else {
            response = null;
            return false;
        }
    }



    public class BankTransferTask extends AsyncTask<Void, Void, Boolean> {


        private JSONArray bankAccounts=null;
        private JSONObject bankTransfer=null;

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

            void setBankTransfer() throws JSONException {
                bankTransfer = new JSONObject();
                JSONObject jsonObjectToAccount = new JSONObject();
                jsonObjectToAccount.put("recipientAccount", mAccountNumber.getText().toString());
//                    jsonObjectToAccount.put("idInternalAccount", null);
//                    jsonObjectToAccount.put("idExternalAccount", null);
                bankTransfer.put("fromAccount", bankAccounts.getJSONObject(0));
                bankTransfer.put("dateOfOrder", new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
                bankTransfer.put("dateOfExecution", new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
                bankTransfer.put("recipient",mRecipient.getText().toString() );
                bankTransfer.put("amount", mAmount.getText().toString());
                bankTransfer.put("address", city.getText().toString()+", "+mStreet.getText().toString()+" ,"+mPostalCode.getText().toString());
                bankTransfer.put("description",mDescription.getText().toString());
                bankTransfer.put("state","CREATED");
                bankTransfer.put("amountStateBefore", bankAccounts.getJSONObject(0).getString("amount"));
                bankTransfer.put("toAccount", jsonObjectToAccount);
                bankTransfer.put("type", "Zewnętrzny");
            }


            String send(String urlAddress) {
                URL url = null;
                try {

                    url = new URL(getString(R.string.URL_address) + urlAddress);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    setBankTransfer();
                    DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                    os.write(bankTransfer.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();
                    if(connection.getResponseCode()==200) {
                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        String text = InputStreamStringConverter.streamToString(in);
                        in.close();
                        return text;
                    }

                    else {
                        Log.d("Response: ", String.valueOf(connection.getResponseCode()));
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
                return null;
            }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress(true);
        }
                @Override
        protected Boolean doInBackground(Void... params) {
                    // TODO: attempt authentication against a network service.
                    bankAccounts = getClientBankAccounts();
                    if (getCode) {
                        response = send("/transfer/make");
                        if (response==null)
                            return false;
                    } else {
                        response = send("/transfer/register");
                        if (response == null) {
                            return false;
                        }

                    }

                    return true;
                }



        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                if(!getCode){
                    getCode = true;
                    finish();
                    Toast.makeText(getApplicationContext(), "Przelew udany!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Nie udało się wykonać przelewu!", Toast.LENGTH_SHORT).show();
            }
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
