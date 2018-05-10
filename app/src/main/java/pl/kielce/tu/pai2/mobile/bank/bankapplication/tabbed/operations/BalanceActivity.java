package pl.kielce.tu.pai2.mobile.bank.bankapplication.tabbed.operations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.R;

public class BalanceActivity extends Fragment implements AdapterView.OnItemSelectedListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tabbed_operations_balance, container, false);
        addSpinner(rootView);


        return rootView;
    }

    private void addSpinner(View view){
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_balance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.accounts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
