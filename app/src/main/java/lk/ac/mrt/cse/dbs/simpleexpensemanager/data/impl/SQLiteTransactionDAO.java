package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteTransactionDAO extends DatabaseHelper implements TransactionDAO {
    private List<Transaction> transactions;
    public SQLiteTransactionDAO(@Nullable Context context) {
        super(context);
//        this.transactions=new ArrayList<Transaction>();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String transDate =new SimpleDateFormat("dd-MM-YYYY").format(date);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date",transDate);
        cv.put("accountNo", accountNo);
        cv.put("type", String.valueOf(expenseType));
        cv.put("amount", amount);

        db.insert("transactions", null, cv);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions=new ArrayList<>();
        String selectQuery = "select * from transactions";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){

            do{
                String startdate = cursor.getString(2);
                try {
                    Date transDate = new SimpleDateFormat("dd-MM-YYYY").parse(startdate);
                    String accountNo = cursor.getString(1);

                    String type = cursor.getString(3);
                    ExpenseType expenseType = ExpenseType.valueOf(type.toUpperCase());

                    double amount = cursor.getDouble(4);

                    Transaction transaction = new Transaction(transDate,accountNo,expenseType,amount);
                    transactions.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        transactions = getAllTransactionLogs();
        int size = transactions.size();

        if (size <= limit) {
            return transactions;
        }
        return transactions.subList(size - limit, size);
    }
}
