package com.example.expensetrackingapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHandler dbHandler;  // Global DBHandler for database interactions
    private ListView expenseListView;  // Global ListView for displaying expenses
    private ExpenseAdapter adapter;  // Global ExpenseAdapter for the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Ensure this layout exists

        // Initialize global objects
        dbHandler = new DBHandler(this);
        expenseListView = findViewById(R.id.simpleListView);

        if (expenseListView == null) {
            Toast.makeText(this, "Error: ListView not found in layout", Toast.LENGTH_LONG).show();
            return;
        }

        // Refresh the expense list initially
        refreshExpenseList(dbHandler.getAllExpenses());

        Button menu = findViewById(R.id.menuButton);
        if (menu != null) {
            menu.setOnClickListener(this::showPopupMenu);
        } else {
            Toast.makeText(this, "Error: Menu button not found in layout", Toast.LENGTH_LONG).show();
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.add_expense) {
                AddNewExpenseDialog();
            } else if (id == R.id.clear_expenses) {
                clearAllExpenses();
            } else if (id == R.id.show_all_expenses) {
                refreshExpenseList(dbHandler.getAllExpenses());
            } else if (id == R.id.find_by_category) {
                showFilterDialog("Filter by Category", "category");
            } else if (id == R.id.find_by_date) {
                showFilterDialog("Filter by Date", "date");
            } else {
                return false;
            }
            return true;
        });
    }

    private void AddNewExpenseDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.add_expense_dialog);


        final EditText inputCategory = dialog.findViewById(R.id.inputCategory);
        final EditText inputDescription = dialog.findViewById(R.id.inputDescription);
        final EditText inputDate = dialog.findViewById(R.id.inputDate);
        final EditText inputPrice = dialog.findViewById(R.id.inputPrice);

        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        Button confirmButton = dialog.findViewById(R.id.confirmButton);

        if (inputCategory == null || inputDescription == null || inputDate == null || inputPrice == null
                || cancelButton == null || confirmButton == null) {
            Toast.makeText(this, "Error: Dialog layout is missing required views", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            return;
        }

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        confirmButton.setOnClickListener(v -> {
            String category = inputCategory.getText().toString();
            String description = inputDescription.getText().toString();
            String price = inputPrice.getText().toString();
            String date = inputDate.getText().toString();

            if (category.isEmpty() || description.isEmpty() || price.isEmpty() || date.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double parsedPrice = Double.parseDouble(price);
                Item newExpense = new Item(0, price, category, description, date);
                dbHandler.addExpense(newExpense);

                Toast.makeText(getApplicationContext(), "Expense added successfully!", Toast.LENGTH_SHORT).show();
                refreshExpenseList(dbHandler.getAllExpenses());
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid price. Please enter a valid number.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showFilterDialog(String title, String filterType) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(filterType.equals("category") ? R.layout.show_by_category : R.layout.show_by_date);

        final EditText inputField = dialog.findViewById(filterType.equals("category") ? R.id.input_Category : R.id.input_Date);
        Button cancelButton = dialog.findViewById(R.id.cancel_Button);
        Button submitButton = dialog.findViewById(R.id.submitButton);

        if (inputField == null || cancelButton == null || submitButton == null) {
            Toast.makeText(this, "Error: Dialog layout is missing required views", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            return;
        }

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        submitButton.setOnClickListener(v -> {
            String input = inputField.getText().toString();

            if (input.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill out the field", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Item> filteredExpenses = filterType.equals("category") ? dbHandler.getAllExpensesbyCategory(input) : dbHandler.getAllExpensesbyDate(input);
            refreshExpenseList(filteredExpenses);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void clearAllExpenses() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete All Expenses?")
                .setMessage("Are you sure you want to clear all expenses?")
                .setPositiveButton("Confirm", (dialogInterface, i) -> {
                    dbHandler.clearDatabase();
                    refreshExpenseList(dbHandler.getAllExpenses());
                    Toast.makeText(MainActivity.this, "All expenses cleared!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void refreshExpenseList(List<Item> expenses) {
        if (expenses == null) {
            Toast.makeText(this, "Error: Expense list is null", Toast.LENGTH_LONG).show();
            return;
        }

        adapter = new ExpenseAdapter(this, expenses);
        expenseListView.setAdapter(adapter);
    }
}
