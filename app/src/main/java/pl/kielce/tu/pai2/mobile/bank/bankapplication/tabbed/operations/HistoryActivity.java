package pl.kielce.tu.pai2.mobile.bank.bankapplication.tabbed.operations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.R;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.model.Transaction;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.model.TransactionAdapter;

public class HistoryActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tabbed_operations_history, container, false);
        displayListView(rootView);

        return rootView;
    }

    private void displayListView(View view) {

        //Array list of countries
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
        Transaction transaction = new Transaction("11/12/2017", "Szymon Jarząbek", "Wojciech Gołąbek", "Za projekt", 21.32, 432.21);
        transactionList.add(transaction);
        transaction = new Transaction("11/12/2017", "Szymon Jarząbek", "Wojciech Gołąbek", "Za projekt", 21.32, 432.21);
        transactionList.add(transaction);
        transaction = new Transaction("21/03/2017", "Szymon Jarząbek", "Szymon Dudek", "Za obiad", 13.00, 445.21);
        transactionList.add(transaction);
        transaction = new Transaction("11/02/2017", "Jakub Gałązka", "Szymon Jarząbek", "Za buty", 44.32, 420.21);
        transactionList.add(transaction);
        transaction = new Transaction("11/01/2017", "Szymon Jarząbek", "Wojciech Gołąbek", "Opłata manipulacyjna", 21.32, 441.53);
        transactionList.add(transaction);
        transaction = new Transaction("11/12/2016", "ITI NEOVISION S.A.", "Szymon Jarząbek", "KGV-321-3123", 21.32, 420.21);
        transactionList.add(transaction);
        transaction = new Transaction("10/01/2015", "Szymon Jarząbek", "Wojciech Gołąbek", "Za milczenie", 21.32, 432.21);
        transactionList.add(transaction);

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
}


