/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.SQliteExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    private ExpenseManager expenseManager;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        try {
            expenseManager = new SQliteExpenseManager(context);
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAccount() {
//        assertTrue(false);
        expenseManager.addAccount("1152", "abc", "check2", 500.0);
        List<String> accountNumbs = expenseManager.getAccountNumbersList();
        assertTrue(accountNumbs.contains("1152"));

    }

    @Test
    public void testUpdateBalance() {
        try {
            Account account = expenseManager.getAccountsDAO().getAccount("123a");
            double currentBalance = account.getBalance();
            expenseManager.getAccountsDAO().updateBalance("123a", ExpenseType.INCOME, 500.0);

            assertEquals(currentBalance + 500.0, expenseManager.getAccountsDAO().getAccount("123a").getBalance(), 0.0);
        } catch (InvalidAccountException e) {
            assertTrue(false);
//           fail("Test fail");
        }
    }

    @Test
    public void testLogTransaction() {


        try {
            expenseManager.updateAccountBalance("1152", 11, 05, 2022, ExpenseType.EXPENSE, "150.0");
            List<Transaction> transactionList = expenseManager.getTransactionsDAO().getAllTransactionLogs();
            Transaction trans= transactionList.get(transactionList.size()-1);
            assertEquals("1152",trans.getAccountNo());
            assertEquals(ExpenseType.EXPENSE,trans.getExpenseType());
            assertEquals(150,trans.getAmount(),0.0);


            } catch (InvalidAccountException invalidAccountException) {
            invalidAccountException.printStackTrace();
        }
        }


    }
