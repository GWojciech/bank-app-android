package pl.kielce.tu.pai2.mobile.bank.bankapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import pl.kielce.tu.pai2.mobile.bank.bankapplication.services.InputStreamStringConverter;

public class ManagementCardActivity extends AppCompatActivity {
    private TextView textView;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private String operation;
    private int index;
    private Intent intent;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_card);
        mProgressView = (View) findViewById(R.id.progress_card_manager);
        this.intent = getIntent();
        init();
    }

    public void init(){
        final Button buttonChangePin = (Button) findViewById(R.id.change_pin_button);
        final Button buttonChangeLimits = (Button) findViewById(R.id.change_limits_button);
        final Button buttonLockCard = (Button)findViewById(R.id.lock_card_button);
        final Button buttonAccept = (Button) findViewById(R.id.accept_card_button);
        editText1 = findViewById(R.id.card_manager_textView1);
        editText2 = findViewById(R.id.card_manager_textView2);
        editText3 = findViewById(R.id.card_manager_textView3);
        Button buttonCancel = (Button) findViewById(R.id.cancel_button);
        Intent intent = getIntent();
        String cardNumber = intent.getStringExtra("creditCardNumber");
        index = intent.getIntExtra("index", -1);
        textView = findViewById(R.id.card_number_textView);
        textView.setText("Karta nr: "+ cardNumber);

        buttonChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChangePin.setVisibility(View.GONE);
                buttonChangeLimits.setVisibility(View.GONE);
                buttonLockCard.setVisibility(View.GONE);
                buttonAccept.setVisibility(View.VISIBLE);
                editText1.setVisibility(View.VISIBLE);
                editText1.setHint("Podaj aktualny PIN");
                editText2.setVisibility(View.VISIBLE);
                editText2.setHint("Podaj nowy PIN");
                editText3.setVisibility(View.VISIBLE);
                editText3.setHint("Potwierdź nowy PIN");
                operation = "PIN";
            }
        });

        buttonChangeLimits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChangePin.setVisibility(View.GONE);
                buttonChangeLimits.setVisibility(View.GONE);
                buttonLockCard.setVisibility(View.GONE);
                buttonAccept.setVisibility(View.VISIBLE);
                editText1.setVisibility(View.VISIBLE);
                editText1.setHint("Podaj nowy limit dzienny");
                editText1.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
                editText2.setVisibility(View.VISIBLE);
                editText2.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
                editText2.setHint("Podaj nowy limit miesięczny");
                editText3.setVisibility(View.VISIBLE);
                editText3.setHint("Podaj PIN");
                operation = "LIMITS";

            }
        });

        buttonLockCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChangePin.setVisibility(View.GONE);
                buttonChangeLimits.setVisibility(View.GONE);
                buttonLockCard.setVisibility(View.GONE);
                buttonAccept.setVisibility(View.VISIBLE);
                editText3.setVisibility(View.VISIBLE);
                editText3.setHint("Podaj PIN");
                operation = "LOCK";
            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CardManagerTask().execute((Void) null);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    JSONObject setDataToSend(JSONObject jsonObject) throws JSONException {
        String oldPin = jsonObject.getString("pinCode");
        String confirmPin = editText3.getText().toString();

        if(operation.equals("PIN")){
            String oldPinConfirm = editText1.getText().toString();
            String newPin = editText2.getText().toString();
            if(oldPin.equals(oldPinConfirm)&&newPin.length()==4&&newPin.equals(confirmPin)){
                jsonObject.put("pinCode", newPin);
                return jsonObject;
            }
        }
        else if(operation.equals("LIMITS")){
            Double dayLimit = Double.valueOf(editText1.getText().toString());
            Double monthLimit = Double.valueOf(editText2.getText().toString());
            if(oldPin.equals(confirmPin)){
                jsonObject.put("dayLimit", dayLimit);
                jsonObject.put("monthLimit", monthLimit);
                return jsonObject;
            }
        }
        else if(operation.equals("LOCK")){
            if(oldPin.equals(confirmPin)){
                jsonObject.put("state", "LOCKED");
                return jsonObject;
            }
        }
        return null;
    }

    private class CardManagerTask extends AsyncTask<Void, Void, Boolean> {

        private JSONArray bankAccounts;

        JSONObject getCardInfo(String URLAddress){
            URL url = null;
            try {
                url = new URL(getString(R.string.URL_address)+URLAddress);
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
                    return jsonArray.getJSONObject(index);
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

        Boolean send(JSONObject jsonObject) {
            URL url = null;
            try {

                url = new URL(getString(R.string.URL_address) + "/card/changeCard");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);
                connection.setDoInput(false);
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.write(jsonObject.toString().getBytes("UTF-8"));
                os.flush();
                os.close();
                if(connection.getResponseCode()==200) {
                    connection.disconnect();
                    return true;
                }
                connection.disconnect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
            Integer idBankAccount = intent.getIntExtra("idBankAccount", -1);
            if(index!=-1&&idBankAccount!=-1) {
                JSONObject jsonObject = getCardInfo("/card/" + idBankAccount.toString());
                try {
                    jsonObject = setDataToSend(jsonObject);
                    if (send(jsonObject)) {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                showProgress(false);
                finish();
                Toast.makeText(getApplicationContext(), "Udało się!", Toast.LENGTH_SHORT).show();
            } else {
                showProgress(false);
                finish();
                Toast.makeText(getApplicationContext(), "Błąd!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

}
