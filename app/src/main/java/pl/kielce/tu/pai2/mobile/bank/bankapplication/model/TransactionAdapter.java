package pl.kielce.tu.pai2.mobile.bank.bankapplication.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.R;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private TransactionFilter filter;
    private ArrayList<Transaction> originalList;
    private ArrayList<Transaction> transactionList;

    public TransactionAdapter(Context context, int textViewResourceId, ArrayList<Transaction> transactionList) {
        super(context, textViewResourceId, transactionList);
        this.transactionList = new ArrayList<Transaction>();
        this.transactionList.addAll(transactionList);
        this.originalList = new ArrayList<Transaction>();
        this.originalList.addAll(transactionList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater trainingInflater = LayoutInflater.from(getContext());
        View transactionView = trainingInflater.inflate(R.layout.informations_about_transaction, parent, false);

        TextView date = (TextView) transactionView.findViewById(R.id.date_transaction_TextView);
        TextView recipient = transactionView.findViewById(R.id.recipient_transaction_recipient);
        TextView sender = transactionView.findViewById(R.id.sender_transaction_TextView);
        TextView title = (TextView) transactionView.findViewById(R.id.title_transaction_TextView);
        TextView amount = (TextView) transactionView.findViewById(R.id.amount_transaction_TextView);
        TextView balance = (TextView) transactionView.findViewById(R.id.balance_transaction_TextView);

        Transaction currentTransaction = transactionList.get(position);
        date.setText(currentTransaction.getDate());
        recipient.setText(currentTransaction.getRecipient());
        sender.setText(currentTransaction.getSender());
        title.setText(currentTransaction.getTitle());
        amount.setText(currentTransaction.getAmount().toString());
        balance.setText(currentTransaction.getBalance().toString());

//        trainingImage.setImageResource(R.drawable.ic_fitness_center_black_24dp);
        return transactionView;


    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TransactionFilter();
        }
        return filter;
    }

    private class TransactionFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<Transaction> filteredItems = new ArrayList<Transaction>();

                for(int i = 0, l = originalList.size(); i < l; i++)
                {
                    Transaction country = originalList.get(i);
                    if(country.toString().toLowerCase().contains(constraint))
                        filteredItems.add(country);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originalList;
                    result.count = originalList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            transactionList = (ArrayList<Transaction>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = transactionList.size(); i < l; i++)
                add(transactionList.get(i));
            notifyDataSetInvalidated();
        }
    }


}


