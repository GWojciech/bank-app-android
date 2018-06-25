package pl.kielce.tu.pai2.mobile.bank.bankapplication.tabbed.operations;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.LoginActivity;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.R;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.services.InputStreamStringConverter;

public class BalanceActivity extends Fragment {

    private TextView mAccountNumber;
    private TextView mAmount;
    private TextView mMonthLimit;
    private TextView mDayLimit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tabbed_operations_balance, container, false);
        mAmount = rootView.findViewById(R.id.balance_account_state_balance2);
        mDayLimit = rootView.findViewById(R.id.balance_account_state_balance4);
        mMonthLimit = rootView.findViewById(R.id.balance_account_state_balance6);
        mAccountNumber = rootView.findViewById(R.id.balance_textView);
        new BalanceTask().execute((Void)null);

        return rootView;
    }

//    private void addSpinner(View view){
//        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_balance);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.accounts_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//    }
//
//
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    public class BalanceTask extends AsyncTask<Void, Void, Boolean> {

        private JSONArray bankAccounts;

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


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            bankAccounts = getClientBankAccounts();
            if(bankAccounts!=null)
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                try {
                    mAccountNumber.setText("Konto " + bankAccounts.getJSONObject(0).getString("accountNumber"));
                    mAmount.setText(bankAccounts.getJSONObject(0).getString("amount")+ " PLN");
                    mDayLimit.setText(bankAccounts.getJSONObject(0).getString("dayLimit")+ " PLN");
                    mMonthLimit.setText((bankAccounts.getJSONObject(0).getString("monthLimit"))+" PLN");

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
