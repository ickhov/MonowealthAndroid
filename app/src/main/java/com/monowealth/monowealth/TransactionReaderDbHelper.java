package com.monowealth.monowealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "transactions.db";
    public static final String TRANSACTION_TABLE_NAME = "data";
    public static final String TRANSACTION_COLUMN_TYPE = "type";
    public static final String TRANSACTION_COLUMN_CATEGORY = "category";
    public static final String TRANSACTION_COLUMN_NAME = "name";
    public static final String TRANSACTION_COLUMN_AMOUNT = "amount";
    public static final String TRANSACTION_COLUMN_YEAR = "year";
    public static final String TRANSACTION_COLUMN_MONTH = "month";
    public static final String TRANSACTION_COLUMN_DAY = "day";
    public static final String TRANSACTION_COLUMN_LOCATION = "location";
    public static final String TRANSACTION_COLUMN_NOTE = "note";

    public TransactionReaderDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TRANSACTION_TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                TRANSACTION_COLUMN_TYPE + " TEXT," +
                TRANSACTION_COLUMN_CATEGORY + " TEXT," +
                TRANSACTION_COLUMN_NAME + " TEXT," +
                TRANSACTION_COLUMN_AMOUNT + " TEXT," +
                TRANSACTION_COLUMN_YEAR + " TEXT," +
                TRANSACTION_COLUMN_MONTH + " TEXT," +
                TRANSACTION_COLUMN_DAY + " TEXT," +
                TRANSACTION_COLUMN_LOCATION + " TEXT," +
                TRANSACTION_COLUMN_NOTE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTransaction(String type, String category, String name, String amount,
                                     String year, String month, String day, String location, String note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_COLUMN_TYPE, type);
        values.put(TRANSACTION_COLUMN_CATEGORY, category);
        values.put(TRANSACTION_COLUMN_NAME, name);
        values.put(TRANSACTION_COLUMN_AMOUNT, amount);
        values.put(TRANSACTION_COLUMN_YEAR, year);
        values.put(TRANSACTION_COLUMN_MONTH, month);
        values.put(TRANSACTION_COLUMN_DAY, day);
        values.put(TRANSACTION_COLUMN_LOCATION, location);
        values.put(TRANSACTION_COLUMN_NOTE, note);
        db.insert(TRANSACTION_TABLE_NAME, null, values);
        return true;
    }

    public int deleteTransaction(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TRANSACTION_TABLE_NAME, "id = ?", new String[] {Integer.toString(id)});
    }

    public boolean updateTransaction(Integer id, String type, String category, String name, String amount,
                                     String year, String month, String day, String location, String note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_COLUMN_TYPE, type);
        values.put(TRANSACTION_COLUMN_CATEGORY, category);
        values.put(TRANSACTION_COLUMN_NAME, name);
        values.put(TRANSACTION_COLUMN_AMOUNT, amount);
        values.put(TRANSACTION_COLUMN_YEAR, year);
        values.put(TRANSACTION_COLUMN_MONTH, month);
        values.put(TRANSACTION_COLUMN_DAY, day);
        values.put(TRANSACTION_COLUMN_LOCATION, location);
        values.put(TRANSACTION_COLUMN_NOTE, note);
        db.update(TRANSACTION_TABLE_NAME, values, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public Cursor readTransaction(String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TRANSACTION_TABLE_NAME, null, selection, selectionArgs, null, null, sortOrder);
    }

    public String buildSelection(String[] selections)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < selections.length; i++)
        {
            builder.append(selections[i]);
            builder.append(" = ?");

            if ((i + 1) < selections.length)
                builder.append(" AND ");
        }

        return builder.toString();
    }

    public String buildSortOrder(String[] sortFilter, int sortCode)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < sortFilter.length; i++)
        {
            builder.append(sortFilter[i]);

            if ((i + 1) < sortFilter.length)
                builder.append(", ");
        }

        if (sortCode == 0)
            builder.append(" ASC");
        else
            builder.append(" DESC");


        return builder.toString();
    }
}
