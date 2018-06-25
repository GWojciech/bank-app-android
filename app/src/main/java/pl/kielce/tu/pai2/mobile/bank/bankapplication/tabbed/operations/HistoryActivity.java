package pl.kielce.tu.pai2.mobile.bank.bankapplication.tabbed.operations;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

import pl.kielce.tu.pai2.mobile.bank.bankapplication.LoginActivity;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.R;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.model.Transaction;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.model.TransactionAdapter;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.services.InputStreamStringConverter;

public class HistoryActivity extends Fragment {

    private ArrayList<Transaction> transactionList = new ArrayList<>();
    private TextView mAccount;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tabbed_operations_history, container, false);
        mAccount = (TextView) rootView.findViewById(R.id.history_textView);
        view = rootView;
        new HistoryTask().execute((Void) null);

        return rootView;
    }

    private void displayListView(View view) {

        final TransactionAdapter transactionAdapter = new TransactionAdapter(view.getContext(), R.layout.informations_about_transaction, transactionList);
        ListView listView = (ListView) view.findViewById(R.id.listViewHistory);
        // Assign adapter to ListView
        listView.setAdapter(transactionAdapter);
        //enables filtering for the contents of the given ListView
        EditText myFilter = (EditText) view.findViewById(R.id.filter_history_TextView);
        listView.setTextFilterEnabled(true);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                transactionAdapter.getFilter().filter(s.toString());
            }
        });

    }


    public class HistoryTask extends AsyncTask<Void, Void, Boolean> {

        private JSONArray bankAccounts;

        JSONArray getClientBankAccounts(Integer id){
            URL url = null;
            try {
                if(id==0) {
                    url = new URL(getString(R.string.URL_address) + "/bankAccount/" + LoginActivity.idClient);
                }
                else{
                    url = new URL(getString(R.string.URL_address) + "/bankAccount/history/"+id.toString());
                }
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

            bankAccounts = getClientBankAccounts(0);
            try {
                Integer idAccount= bankAccounts.getJSONObject(0).optInt("idBankAccount");
                if(idAccount!=null){
                    JSONArray historyArray = getClientBankAccounts(idAccount);
                    historyArray.toString();
                    Transaction transaction;
                    JSONObject jsonObject;
                    String date;
                    String recipient;
                    String sender;
                    String description;
                    String amount;
                    String amountBefore;
                    JSONObject forName;
                    for(int i=0; i<historyArray.length(); i++){
                        jsonObject = historyArray.getJSONObject(i);
                        date = jsonObject.getString("dateOfOrder");
                        recipient = jsonObject.getString("recipient");
                        description = jsonObject.getString("description");
                        forName = jsonObject.getJSONObject("fromAccount").getJSONObject("idClient").getJSONObject("idUser").getJSONObject("idPerson");
                        sender = forName.getString("name") + " " + forName.getString("surname");
                        amount = jsonObject.getString("amount") +" PLN" ;
                        amountBefore = jsonObject.getString("amountStateBefore") + " PLN";
                        transaction= new Transaction(date,recipient, sender, description,amount, amountBefore);
                        transactionList.add(transaction);
                    }

                    return true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                try {
                    mAccount.setText("Konto "+ bankAccounts.getJSONObject(0).getString("accountNumber"));
                    displayListView(view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //finish();
            } else {
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}


