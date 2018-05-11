//package pl.kielce.tu.pai2.mobile.bank.bankapplication.model;
//
//import android.view.View;
//import android.widget.Filter;
//
//import java.util.ArrayList;
//import pl.kielce.tu.pai2.mobile.bank.bankapplication.model.Transaction;
//
//public class TransactionFilter extends Filter
//{
//    private ArrayList<Transaction> originalList=null;
//    private ArrayList<Transaction> countryList;
//    final TransactionAdapter transactionAdapter;
//
//    public TransactionFilter(ArrayList<Transaction> transactionList, TransactionAdapter transactionAdapter) {
//        super();
//        this.countryList=transactionList;
//        this.transactionAdapter=transactionAdapter;
//        this.originalList=transactionList;
//
//    }
//
//    @Override
//    protected FilterResults performFiltering(CharSequence constraint) {
//
//        constraint = constraint.toString().toLowerCase();
//        FilterResults result = new FilterResults();
//        if(constraint != null && constraint.toString().length() > 0)
//        {
//            ArrayList<Transaction> filteredItems = new ArrayList<Transaction>();
//
//            for(int i = 0, l = originalList.size(); i < l; i++)
//            {
//                Transaction country = originalList.get(i);
//                if(country.toString().toLowerCase().contains(constraint))
//                    filteredItems.add(country);
//            }
//            result.count = filteredItems.size();
//            result.values = filteredItems;
//        }
//        else
//        {
//            synchronized(this)
//            {
//                result.values = originalList;
//                result.count = originalList.size();
//            }
//        }
//        return result;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    protected void publishResults(CharSequence constraint,
//                                  FilterResults results) {
//
//        countryList = (ArrayList<Transaction>)results.values;
//        transactionAdapter.notifyDataSetChanged();
//        transactionAdapter.clear();
//        for(int i = 0, l = countryList.size(); i < l; i++)
//            transactionAdapter.add(countryList.get(i));
//        transactionAdapter.notifyDataSetInvalidated();
//    }
//}
//
//
//
//
