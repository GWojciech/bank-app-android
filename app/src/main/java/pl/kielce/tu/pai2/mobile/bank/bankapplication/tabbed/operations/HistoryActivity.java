package pl.kielce.tu.pai2.mobile.bank.bankapplication.tabbed.operations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.R;

public class HistoryActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tabbed_operations_history, container, false);

        return rootView;
    }
}
