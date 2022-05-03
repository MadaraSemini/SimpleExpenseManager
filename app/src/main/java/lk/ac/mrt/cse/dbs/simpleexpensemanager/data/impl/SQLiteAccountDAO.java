package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SQLiteAccountDAO extends DatabaseHelper implements AccountDAO {
    public SQLiteAccountDAO(@Nullable Context context) {
        super(context);

    }

    @Override
    public List<String> getAccountNumbersList() {

        List<String> accountNumbers = new ArrayList<String>();

        String selectQuery = "select accountNo from accounts";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){

            do{
                accountNumbers.add(cursor.getString(0));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList =new ArrayList<Account>();

        String selectQuery = "select * from accounts";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){

            do{
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getInt(3);

                Account account = new Account(accountNo,bankName,accountHolderName,balance);
                accountList.add(account);


            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from accounts where accountNo = '"+ accountNo + "' ;";
        Cursor cursor = db.rawQuery(selectQuery,null);

        String bankName = cursor.getString(1);
        String accountHolderName = cursor.getString(2);
        double balance = cursor.getInt(3);

        Account account = new Account(accountNo,bankName,accountHolderName,balance);

        cursor.close();
        db.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        List<String> accountNumbers = this.getAccountNumbersList();
        if (!accountNumbers.contains(account.getAccountNo())) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv= new ContentValues();
            cv.put("accountNo", account.getAccountNo());
            cv.put("bankName",account.getBankName());
            cv.put("accountHolderName",account.getAccountHolderName());
            cv.put("balance",account.getBalance());

            db.insert("accounts",null, cv);
            //yfyfytfyfu

        }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        List<String> accountNumbers = this.getAccountNumbersList();
        if (!accountNumbers.contains(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        String deleteQuery = "delete from accounts where accountNo = '"+ accountNo + "' ;";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        List<String> accountNumbers = this.getAccountNumbersList();
        if (!accountNumbers.contains(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "select balance from accounts where accountNo = '"+ accountNo +"' ;";

        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        double balance = cursor.getInt(0);
        switch(expenseType){
            case EXPENSE:
                String updateQuery1 = "update accounts set balance = "+ (balance-amount) +" where accountNo = '"+accountNo+"' ;";
                db.execSQL(updateQuery1);
//                balance  -= amount;
                break;
            case INCOME:
                String updateQuery2 = "update accounts set balance = "+ (balance+amount) +" where accountNo = '"+accountNo+"' ;";
                db.execSQL(updateQuery2);
//                balance  += amount;
                break;
        }

//        String updateQuery = "update accounts set balance = "+ (balance-amount) +" where accountNo = '"+accountNo+"' ;";
//        db.execSQL(updateQuery);

        cursor.close();
        db.close();

    }
}
