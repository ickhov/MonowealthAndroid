package com.monowealth.monowealth;
/*
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.monowealth.monowealth.DataHandler.Room.Transaction;

import java.util.ArrayList;

public class TransactionReader {

    private Context context;
    private String year, month;

    public TransactionReader(@NonNull Context context, @NonNull String year, @NonNull String month)
    {
        this.context = context;
        this.year = year;
        this.month = month;
    }

    public ArrayList<Transaction> readTransactions(String type)
    {
        return readData(type);
    }

    private ArrayList<Transaction> readData(String typeToRead)
    {

        ArrayList<Transaction> transactions = new ArrayList<>();

        TransactionReaderDbHelper dbHelper = new TransactionReaderDbHelper(context);

        String sortOrder = dbHelper.buildSortOrder
                (new String[]{TransactionReaderDbHelper.TRANSACTION_COLUMN_YEAR,
                        TransactionReaderDbHelper.TRANSACTION_COLUMN_MONTH,
                        TransactionReaderDbHelper.TRANSACTION_COLUMN_DAY}, 1);

        Cursor cursor;

        if (!TextUtils.isEmpty(typeToRead)) {

            String selection = dbHelper.buildSelection(new String[]{
                    TransactionReaderDbHelper.TRANSACTION_COLUMN_TYPE,
                    TransactionReaderDbHelper.TRANSACTION_COLUMN_YEAR,
                    TransactionReaderDbHelper.TRANSACTION_COLUMN_MONTH});
            String[] selectionArgs = {typeToRead, year, month};

            cursor = dbHelper.readTransaction(selection, selectionArgs, sortOrder);

        } else {
            String selection = dbHelper.buildSelection(new String[]{
                    TransactionReaderDbHelper.TRANSACTION_COLUMN_YEAR,
                    TransactionReaderDbHelper.TRANSACTION_COLUMN_MONTH});
            String[] selectionArgs = {year, month};

            cursor = dbHelper.readTransaction(selection, selectionArgs, sortOrder);
        }

        while (cursor.moveToNext())
        {
            int id = cursor.getInt
                    (cursor.getColumnIndex("id"));
            String type = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_TYPE));
            String category = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_CATEGORY));
            String name = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_NAME));
            String amount = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_AMOUNT));
            String year = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_YEAR));
            String month = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_MONTH));
            String day = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_DAY));
            String location = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_LOCATION));
            String note = cursor.getString
                    (cursor.getColumnIndex(TransactionReaderDbHelper.TRANSACTION_COLUMN_NOTE));

            //Transaction transaction = new Transaction(id, type, category, name, amount, year, month, day, location, note);

            //transactions.add(transaction);
        }

        return transactions;
    }
}
*/