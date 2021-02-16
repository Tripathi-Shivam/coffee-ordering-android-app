package com.example.coffeeordering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    int noOfCoffees = 2;
    public void displayQuantity(int noOfCoffees){
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(noOfCoffees));
    }

    public void increment(View v){
        if(noOfCoffees == 100){
            Toast.makeText(this, "You cannot order more than 100 Coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        noOfCoffees++;
        displayQuantity(noOfCoffees);
    }

    public void decrement(View v){
        if(noOfCoffees == 1){
            Toast.makeText(this, "You cannot order less than 1 Coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        noOfCoffees--;
        displayQuantity(noOfCoffees);
    }

    public void displayOrderSummary(String summary){
        TextView orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(summary);
    }

    public String createOrderSummary(String name, int total, boolean hasWhippedCream, boolean hasChocolate){
        String summary = getString(R.string.order_summary_name, name) +
                "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream) +
                "\n" + getString(R.string.order_summary_chocolate, hasChocolate) +
                "\n" + getString(R.string.order_summary_quantity, noOfCoffees) +
                "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(total)) +
                "\n" + getString(R.string.thank_you);
        return summary;

    }
    public void order(View v){
        CheckBox whippedCreamCheckbox = findViewById(R.id.whipped_cream);
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasWhippedCream = whippedCreamCheckbox.isChecked();
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int total = calculatePrice(hasWhippedCream, hasChocolate);

        EditText nameEditText = findViewById(R.id.name_field);
        String name = nameEditText.getText().toString();
        String summary = createOrderSummary(name, total, hasWhippedCream, hasChocolate);
        displayOrderSummary(summary);

        composeEmail(summary);
    }

    public void composeEmail(String body){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        //intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "tripathishivam11@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order summary");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    public int calculatePrice(boolean hasWhippedCream, boolean hasChocolate){
        int price = 5;
        if(hasWhippedCream) price++;
        if(hasChocolate) price += 2;
        return price * noOfCoffees;
    }
}