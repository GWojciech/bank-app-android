package pl.kielce.tu.pai2.mobile.bank.bankapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.model.CreditCard;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.model.CreditCardAdapter;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.services.InputStreamStringConverter;

public class CreditCardActivity extends AppCompatActivity {

    private JSONArray jsonArray;
    private ArrayList<CreditCard> creditCardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        new CreditCardTask().execute((Void) null);
    }


    private void displayListView() {

        final CreditCardAdapter creditCardAdapter = new CreditCardAdapter(getApplicationContext(), R.layout.information_about_credit_cards, creditCardList);
        ListView listView = (ListView) findViewById(R.id.credit_cards_listView);
        listView.setAdapter(creditCardAdapter);
    }

    private class CreditCardTask extends AsyncTask<Void, Void, Boolean> {

        private JSONArray bankAccounts;

        JSONArray getData(String URLAddress){
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


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            bankAccounts = getData("/bankAccount/"+ LoginActivity.idClient);
            if(bankAccounts!=null) {
                try {
                    jsonArray = getData("/card/"+bankAccounts.getJSONObject(0).optInt("idBankAccount"));
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            else
                return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                JSONObject jsonObject;
                CreditCard creditCard;
                Integer idCreditCard;
                String name;
                String creditCardNumber;
                String pinCode;
                String state;
                String expirationDate;
                String type;
                Double dayLimit;
                Double monthLimit;
                Integer idBankAccount;

                try {
                for(int i=0; i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    idCreditCard = jsonObject.getInt("idCreditCard");
                    name = jsonObject.getString("name");
                    creditCardNumber = jsonObject.getString("creditCardNumber");
                    pinCode = jsonObject.getString("pinCode");
                    state = jsonObject.getString("state");
                    expirationDate = jsonObject.getString("expirationDate");
                    type = jsonObject.getString("type");
                    dayLimit = jsonObject.getDouble("dayLimit");
                    monthLimit = jsonObject.getDouble("monthLimit");
                    idBankAccount = jsonObject.getJSONObject("idBankAccount").getInt("idBankAccount");
                    creditCard = new CreditCard(idCreditCard, name, creditCardNumber, pinCode, state, expirationDate, type, dayLimit, monthLimit, idBankAccount);
                    creditCardList.add(creditCard);
                    }
                    displayListView();
                    }catch (JSONException e) {
                    e.printStackTrace();

                }

            } else {
                finish();
                Toast.makeText(getApplicationContext(), "Błąd!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
