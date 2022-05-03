package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "190200X.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDB) {
        String createAccountTable="CREATE TABLE accounts(accountNo TEXT PRIMARY KEY , accountHolderName TEXT, bankName TEXT , balance REAL);";
        String createTransactionTable ="CREATE TABLE transactions(transactionId INTEGER PRIMARY KEY AUTOINCREMENT, accountNo TEXT , date TEXT , type TEXT, amount REAL, FOREIGN KEY (accountNo) REFERENCES accountd(accountNo) ON DELETE CASCADE ON UPDATE CASCADE);";

        sqLiteDB.execSQL(createAccountTable);
        sqLiteDB.execSQL(createTransactionTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}