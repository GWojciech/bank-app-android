package pl.kielce.tu.pai2.mobile.bank.bankapplication.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import pl.kielce.tu.pai2.mobile.bank.bankapplication.ManagementCardActivity;
import pl.kielce.tu.pai2.mobile.bank.bankapplication.R;

public class CreditCardAdapter extends ArrayAdapter<CreditCard> {

//    private ArrayList<CreditCard> originalList;
    private ArrayList<CreditCard> creditCardsList;

    public CreditCardAdapter(Context context, int textViewResourceId, ArrayList<CreditCard> creditCardsList) {
        super(context, textViewResourceId, creditCardsList);
        this.creditCardsList = new ArrayList<CreditCard>();
        this.creditCardsList.addAll(creditCardsList);
//        this.originalList = new ArrayList<CreditCard>();
//        this.originalList.addAll(creditCardsList);

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        final LayoutInflater trainingInflater = LayoutInflater.from(getContext());
        final View transactionView = trainingInflater.inflate(R.layout.information_about_credit_cards, parent, false);

        TextView creditCardNumber = (TextView) transactionView.findViewById(R.id.number_card_textView);
        TextView name = transactionView.findViewById(R.id.name_card_textView);
        TextView state = transactionView.findViewById(R.id.state_card_textView);
        TextView expirationDate = (TextView) transactionView.findViewById(R.id.expiration_date_textView);
        TextView type = (TextView) transactionView.findViewById(R.id.type_card_textView);
        TextView dayLimit = (TextView) transactionView.findViewById(R.id.day_limit_card_textView);
        TextView monthLimit = (TextView) transactionView.findViewById(R.id.month_limit_card_textView);
        Button button = (Button) transactionView.findViewById(R.id.change_pin_card_button);


        final CreditCard creditCard = creditCardsList.get(position);
        creditCardNumber.setText(creditCard.getCreditCardNumber());
        name.setText(creditCard.getName());
        state.setText(creditCard.getState());
        expirationDate.setText(creditCard.getExpirationDate());
        type.setText(creditCard.getType());
        dayLimit.setText(creditCard.getDayLimit().toString()+" PLN");
        monthLimit.setText(creditCard.getMonthLimit().toString()+ " PLN");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(transactionView.getContext(), ManagementCardActivity.class);
                intent.putExtra("idBankAccount", creditCard.getIdBankAccount());
                intent.putExtra("index", position);
                intent.putExtra("creditCardNumber", creditCard.getCreditCardNumber());
                parent.getContext().startActivity(intent);

            }
        });

        return transactionView;


    }


}


