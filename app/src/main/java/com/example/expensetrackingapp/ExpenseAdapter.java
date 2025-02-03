package com.example.expensetrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private final List<Item> expenses;

    public ExpenseAdapter(Context context, List<Item> expenses) {
        super(context, R.layout.list_view_items, expenses);
        this.context = context;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_items, parent, false);
        }

        Item expense = expenses.get(position);

        TextView categoryTextView = convertView.findViewById(R.id.categoryTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView priceTextView = convertView.findViewById(R.id.priceTextView);

        // Set data
        categoryTextView.setText(expense.getCategory());
        descriptionTextView.setText(expense.getDescription());
        dateTextView.setText(expense.getDate());
        priceTextView.setText("$" + expense.getAmount());

        return convertView;
    }
}

