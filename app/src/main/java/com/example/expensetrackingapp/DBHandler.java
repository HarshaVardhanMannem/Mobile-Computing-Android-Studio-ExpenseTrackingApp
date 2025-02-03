package com.example.expensetrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ExpensesTrackingAppDb";
    public static final String TABLE_EXPENSES = "expenses";
    public static final String KEY_ID = "id";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "date";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableString = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_AMOUNT + " REAL NOT NULL, " +
                KEY_CATEGORY + " TEXT NOT NULL, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_DATE + " TEXT NOT NULL" +
                ")";
        db.execSQL(createTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    void clearDatabase() {
        onUpgrade(this.getWritableDatabase(),0,0);
    }

    void addExpense(Item expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DESCRIPTION, expense.getDescription());
        values.put(KEY_DATE, expense.getDate());
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }


    public List<Item> getAllExpenses() {
        List<Item> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Item expense = new Item();
                expense.setId(cursor.getInt(0));
                expense.setAmount(cursor.getString(1));
                expense.setCategory(cursor.getString(2));
                expense.setDescription(cursor.getString(3));
                expense.setDate(cursor.getString(4));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseList;
    }

    public List<Item> getAllExpensesbyCategory(String category) {
        List<Item> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to fetch expenses by category
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES + " WHERE category = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                Item expense = new Item();
                expense.setId(cursor.getInt(0)); // Assuming column 0 is the ID
                expense.setAmount(cursor.getString(1)); // Assuming column 1 is the Amount
                expense.setCategory(cursor.getString(2)); // Assuming column 2 is the Category
                expense.setDescription(cursor.getString(3)); // Assuming column 3 is the Description
                expense.setDate(cursor.getString(4)); // Assuming column 4 is the Date
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }



    public List<Item> getAllExpensesbyDate(String date) {
        List<Item> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to fetch expenses by category
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES + " WHERE date = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                Item expense = new Item();
                expense.setId(cursor.getInt(0)); // Assuming column 0 is the ID
                expense.setAmount(cursor.getString(1)); // Assuming column 1 is the Amount
                expense.setCategory(cursor.getString(2)); // Assuming column 2 is the Category
                expense.setDescription(cursor.getString(3)); // Assuming column 3 is the Description
                expense.setDate(cursor.getString(4)); // Assuming column 4 is the Date
                expenseList.add(expense);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

}

