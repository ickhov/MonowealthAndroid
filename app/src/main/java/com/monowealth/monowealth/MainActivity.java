/*  Data storage code
    0 = id;
    1 = type;
    2 = category;
    3 = name;
    4 = amount;
    5 = year;
    6 = month;
    7 = day;
    8 = location;

    // example format for transaction storage
    id:type:category:name:amount:year:month:day:location

    Budget storage code
    0 = category
    1 = max amount

    // example format for budget storage
    category:max
 */

package com.monowealth.monowealth;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        TransactionSaveFragment.OnFragmentInteractionListener,
        TransactionUpdateFragment.OnFragmentInteractionListener,
        DatePickerDialogFragment.OnDialogFragmentInteractionListener,
        CategoryPickerFragment.OnFragmentInteractionListener,
        CategoryPickerAdapter.OnAdapterInteractionListener,
        TransactionFragment.OnFragmentInteractionListener,
        MainMenuMonthlyOverviewAdapter.OnAdapterInteractionListener,
        MonthTransactionAdapter.OnAdapterInteractionListener,
        LicensesFragment.OnFragmentInteractionListener,
        FeedbackFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        CategoryPickerDialogFragment.OnDialogFragmentInteractionListener,
        TransactionCategoryFragment.OnFragmentInteractionListener,
        BudgetTransactionFragment.OnFragmentInteractionListener,
        BudgetFragment.OnFragmentInteractionListener,
        BudgetViewAdapter.OnAdapterInteractionListener,
        BudgetCreatorDialogFragment.OnDialogFragmentInteractionListener {

    public int screenWidth, screenHeight;                       // screen width and height
    public Typeface quicksand;                                  // quicksand quicksand
    private ConstraintLayout main;                              // main layout reference
    private PlainTextFile textFile;                             // Text File operations
    private TypedArray backgrounds;                             // background gradients
    private LinearLayout top;                                   // top part of main layout
    private MediumTextView title;                               // title "Monowealth" in main layout
    private ImageButton settings;                               // the settings button in main layout
    private LicensesFragment licensesFragment;                  // third party license fragment
    private FeedbackFragment feedbackFragment;                  // user feedback or new feature request
    private SettingsFragment settingsFragment;                  // lead to a view to open feedback and licenses fragment
    private FloatingActionButton newExpenses, newIncome;        // new expenses and income button in main layout
    private TransactionSaveFragment expenseFragment, incomeFragment;  // expense and income fragment when user click floating button
    private TransactionUpdateFragment updateFragment;           // update fragment
    private DialogFragment datePickerFragment;                  // dialog fragment (mini fragment) for picking dates
    private CategoryPickerFragment categoryPickerFragment;      // fragment for picking a category
    private CategoryPickerDialogFragment categoryPickerDialogFragment;  // adding new category
    private TransactionFragment monthFragment;                  // month fragment when user pressed on the income, expenses, or month name
    private TransactionCategoryFragment monthCategoryFragment;  // month category fragment when user pressed pie chart
    private BudgetTransactionFragment budgetTransactionFragment;                      // budget fragment when user pressed budget button
    private String fileName;                                    // store file name, ONLY FOR USE IN TRANSACTION FRAGMENT AND TRANSACTION CATEGORY FRAGMENT
    private String type;                                        // store type (income, expenses, transactions), ONLY
                                                                // FOR USE IN TRANSACTION DETAIL FRAGMENT
    private String[] detailInformation;                         // store data to use in TransactionSaveFragment.java
    private double inAmt, exAmt;                                // income amount and expense amount to use in TransactionFragment
    private BudgetFragment budgetFragment;                      // budget fragment
    private BudgetCreatorDialogFragment budgetCreatorDialogFragment;        // create a new budget
    // MainMenuMonthlyOverviewAdapter.java //
    // ----------------------------------- //
    private RecyclerView data;                                  // set the scrollable view in main layout
    private MainMenuMonthlyOverviewAdapter overviewAdapter;     // adapter for scrollable view in main layout
    private ArrayList<String> monthNames;                   // list of month names from storage
    private ArrayList<ArrayList<String[]>> monthData;       // list of data for each month
    private ArrayList<String> categories;                   // store all the expenses categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // dimension of the screen
        screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;

        // default quicksand for the app
        quicksand = Typeface.createFromAsset(getApplicationContext().getAssets(), "Quicksand-Regular.ttf");

        // sort background based on the monthNames
        backgrounds = getApplicationContext().getResources().obtainTypedArray(R.array.twelve_gradients);

        main = findViewById(R.id.main_activity);
        main.setBackground(backgrounds.getDrawable(Calendar.getInstance().get(Calendar.MONTH)));

        textFile = new PlainTextFile(getApplicationContext());

        initializeFile();

        updateDataAdapterMonthNames();
        setTop();
        setData();
        setNewExpenses();
        setNewIncome();

    }

    private void initializeFile()
    {
        try {
            File file = new File(getApplicationContext().getFilesDir().getAbsolutePath());
            int size = file.listFiles().length;

            if (size == 0)
            {
                Calendar calendar = Calendar.getInstance();
                String fName = getMonth(calendar.get(Calendar.MONTH)) + "_" + calendar.get(Calendar.YEAR) + ".txt";
                File f = new File(getApplicationContext().getFilesDir().getAbsolutePath(), fName);
                f.createNewFile();
            }

        } catch (IOException error)
        {

        }
    }

    // set the top part of the app
    private void setTop() {
        top = findViewById(R.id.main_activity_top);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) top.getLayoutParams();
        layoutParams.height = (int)(screenHeight * 0.15);

        setTitle();
        setSettings();
    }


    // set the title of the main screen
    private void setTitle()
    {
        title = findViewById(R.id.main_activity_title);
        title.setTextSize(screenWidth / 33);
    }

    // set the settings button of the main screen
    private void setSettings()
    {
        settings = findViewById(R.id.main_activity_settings);
        settings.setTag("settings_button");
        settings.setOnClickListener(this);
    }

    //-- Methods to set up data RecyclerView in MainActivity.java --//
    // init data and use helper methods to set up other stuff
    private void setData() {
        data = findViewById(R.id.main_activity_middle);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) data.getLayoutParams();
        layoutParams.height = (int)(screenHeight * 0.85);
        data.setPadding(0, 0, 0, (int)(screenHeight * 0.05));

        setDataLayout();
        setDataAdapter();

        // ensure that the RecyclerView snap in place when user stop scrolling
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(data);
    }

    // set the layout manager for data RecyclerView
    private void setDataLayout()
    {
        // init layout manager and set it to menu
        LinearLayoutManagerWrapper layoutManager = new LinearLayoutManagerWrapper
                (getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        Calendar calendar = Calendar.getInstance();
        int index = monthNames.indexOf(getMonth(calendar.get(Calendar.MONTH)) + "_" + calendar.get(Calendar.YEAR));
        layoutManager.scrollToPosition(index);
        data.setLayoutManager(layoutManager);
    }

    // set the adapter for data RecyclerView
    private void setDataAdapter()
    {
        updateDataAdapterMonthNames();
        updateDataAdapterMonthData();
        updateDataAdapterCategories();
        overviewAdapter = new MainMenuMonthlyOverviewAdapter(this, monthNames, monthData, categories);
        data.setAdapter(overviewAdapter);
    }

    private void scrollToCurrentMonth(String fileName)
    {
        int index = monthNames.indexOf(fileName);
        data.getLayoutManager().scrollToPosition(index);
    }

    // set/update the information used in the adapter
    private void updateDataAdapterMonthNames()
    {
        // sort and fill in month gap //
        if (monthNames == null)
            monthNames = new ArrayList<>();
        else
            monthNames.clear();

        monthNames.addAll(sortAndFillTheMonths(textFile.getFileNames()));

    }

    // set/update the information used in the adapter
    private void updateDataAdapterMonthData()
    {
        // fill in monthData for each month name //
        if (monthData == null)
            monthData = new ArrayList<>();
        else
            monthData.clear();

        try {
            for (String n : monthNames) {
                monthData.add(textFile.read(n + ".txt"));
            }
        } catch (IOException error)
        {

        }
    }

    // set/update the information used in the adapter
    @Override
    public void updateDataAdapterCategories()
    {
        // fill in the categories for expenses to display in data adapter //
        if (categories == null)
            categories = new ArrayList<>();
        else
            categories.clear();

        try {
            categories.addAll(textFile.readCategory("all", "expenses"));
        } catch (IOException error)
        {

        }
    }

    @Override
    public double calculateBudgetTotal()
    {
        ArrayList<String> budgets = new ArrayList<>();
        try {
            budgets.addAll(textFile.readBudget());
        } catch (IOException error)
        {

        }

        if (!budgets.isEmpty())
        {
            double total = 0.0;
            for (int i = 0; i < budgets.size(); i++)
                total += Double.parseDouble(budgets.get(i).split(":")[1]);

            return total;
        }

        return 0.0;
    }

    // fill up monthName ArrayList to create monthData ArrayList and pass to adapter as parameter
    private ArrayList<String> sortAndFillTheMonths(String[] names)
    {
        ArrayList<String> lists = new ArrayList<>(Arrays.asList(names));
        Collections.sort(lists, new SortFileNames());

        for(int i = 0; i < lists.size(); i++) {
            names[i] = lists.get(i);
        }

        if(!(names.length == 1)) {
            lists.clear();
            lists.addAll(new MonthGapFiller().fillGap(names));
        }

        return lists;
    }

    // insert data from expenses and income button
    private void insert(String[] info)
    {
        String fileName = info[6] + "_" + info[5];

        try {
            // write info to storage
            textFile.write(info);

            boolean monthExist = monthNames.contains(fileName);

            if (!monthExist)
                updateDataAdapterMonthNames();

            updateDataAdapterMonthData();

            int position = monthNames.indexOf(fileName);

            // if monthName already has the month so just update the monthData and change the specific month view
            // else month is added and sorted so it will shift things starting at position
            if (monthExist)
                overviewAdapter.notifyItemChanged(position);
            else {
                overviewAdapter.notifyItemInserted(position);
                overviewAdapter.notifyItemRangeChanged(position, monthNames.size() - position);
            }

        } catch (IOException error) {
            Toast.makeText(getApplicationContext(),
                    "There was a problem adding the new data\nPlease try again later.", Toast.LENGTH_LONG).show();
        }
    }

    // remove data from delete button
    private void remove(String[] info)
    {
        String fileName = info[6] + "_" + info[5];

        try {
            // delete info from storage
            int position = monthNames.indexOf(fileName);
            boolean fileDeleted = textFile.delete(info, monthData.get(position));

            // update information to reflect the removal
            if (fileDeleted) {
                // make the current month and year file is still there
                Calendar calendar = Calendar.getInstance();
                String fName = getMonth(calendar.get(Calendar.MONTH)) + "_" + calendar.get(Calendar.YEAR) + ".txt";
                File f = new File(getApplicationContext().getFilesDir().getAbsolutePath(), fName);
                if (!f.exists())
                    f.createNewFile();

                updateDataAdapterMonthNames();
            }

            updateDataAdapterMonthData();

            // file for that month is completely deleted or not
            // if file is deleted, then remove
            // if file is not deleted, then just change the adapter in that position
            if (fileDeleted)
                overviewAdapter.notifyItemRemoved(position);
            else
                overviewAdapter.notifyItemChanged(position);

        } catch (IOException error) {
            Toast.makeText(getApplicationContext(),
                    "There was a problem deleting the data\nPlease try again later.", Toast.LENGTH_LONG).show();
        }
    }

    // insert data from expenses and income button
    private void update(String[] oldInfo, String[] newInfo)
    {
        String oldFileName = oldInfo[6] + "_" + oldInfo[5];
        String newFileName = newInfo[6] + "_" + newInfo[5];
        boolean sameFileName = oldFileName.equals(newFileName);

        try {
            // write info to storage
            int oldPosition = monthNames.indexOf(oldFileName);
            boolean oldFileDeleted = textFile.update(oldInfo, newInfo, monthData.get(oldPosition));

            boolean newMonthExist = monthNames.contains(newFileName);

            // update information to reflect the update
            // if not the same file name AND (new file doesn't exist in monthNames array OR old file is deleted completely)
            // then that means that there's a file changes to account for
            if (!sameFileName && (!newMonthExist || oldFileDeleted))
                updateDataAdapterMonthNames();

            updateDataAdapterMonthData();

            // if same file name, then just change at position
            // else insert and remove accordingly
            if (sameFileName)
            {
                overviewAdapter.notifyItemChanged(oldPosition);
            } else {
                int newPosition = monthNames.indexOf(newFileName);

                if (newMonthExist && oldFileDeleted) {
                    overviewAdapter.notifyItemRemoved(oldPosition);
                    overviewAdapter.notifyItemChanged(newPosition);
                } else if (newMonthExist) {
                    overviewAdapter.notifyItemChanged(oldPosition);
                    overviewAdapter.notifyItemChanged(newPosition);
                } else if (oldFileDeleted) {
                    overviewAdapter.notifyItemRemoved(oldPosition);
                    overviewAdapter.notifyItemInserted(newPosition);
                    overviewAdapter.notifyItemRangeChanged(newPosition, monthNames.size() - newPosition);
                } else {
                    overviewAdapter.notifyItemChanged(oldPosition);
                    overviewAdapter.notifyItemInserted(newPosition);
                    overviewAdapter.notifyItemRangeChanged(newPosition, monthNames.size() - newPosition);
                }
            }
        } catch (IOException error) {
            Toast.makeText(getApplicationContext(),
                    "There was a problem updating the data\nPlease try again later.", Toast.LENGTH_LONG).show();
        }
    }

    //-- End --//


    // set the new expense floating button of the main screen
    private void setNewExpenses()
    {
        newExpenses = findViewById(R.id.floatingActionButton_subtract);
        newExpenses.setCustomSize((int)(screenHeight * 0.1));
        newExpenses.setTag("new_expenses_button");
        newExpenses.setOnClickListener(this);

    }

    // set the new income floating button of the main screen
    private void setNewIncome()
    {
        newIncome = findViewById(R.id.floatingActionButton_add);
        newIncome.setCustomSize((int)(screenHeight * 0.1));
        newIncome.setTag("new_income_button");
        newIncome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (settings.isEnabled() && newExpenses.isEnabled() && newIncome.isEnabled()) {
            switch (v.getTag().toString()) {
                case "settings_button":
                    settings.setEnabled(false);
                    openSettingsFragment();
                    break;
                case "new_expenses_button":
                    newExpenses.setEnabled(false);
                    openExpenseFragment();
                    break;
                case "new_income_button":
                    newIncome.setEnabled(false);
                    openIncomeFragment();
                    break;

            }
        }
    }

    @Override
    public void openBudgetFragment() {
        if (budgetFragment == null)
            budgetFragment = new BudgetFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, budgetFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openSettingsFragment()
    {
        if (settingsFragment == null)
            settingsFragment = new SettingsFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, settingsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openFeedbackFragment()
    {
        if (feedbackFragment == null)
            feedbackFragment = new FeedbackFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, feedbackFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openLicensesFragment() {
        if (licensesFragment == null)
            licensesFragment = new LicensesFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, licensesFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void sendFeedback(String subject)
    {
        String[] addresses = {"ickhov@gmail.com"};

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }

    // show the new income/expense floating button
    @Override
    public void showButtons()
    {
        newExpenses.show();
        newIncome.show();
    }

    @Override
    public void openBudgetCreatorDialog() {
        dismissKeyboard();

        if (budgetCreatorDialogFragment == null)
            budgetCreatorDialogFragment = new BudgetCreatorDialogFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("");
        budgetCreatorDialogFragment.show(fragmentTransaction, null);
    }

    @Override
    public void resetDetailInformation() {
        detailInformation = null;
    }

    // hide the new income/expense floating button
    @Override
    public void hideButtons()
    {
        newExpenses.hide();
        newIncome.hide();
    }

    // open the new expense fragment
    private void openExpenseFragment()
    {
        if (expenseFragment == null)
            expenseFragment = new TransactionSaveFragment();

        type = "expenses";

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, expenseFragment)
                .addToBackStack(null)
                .commit();
    }

    // open the new income fragment
    private void openIncomeFragment()
    {
        if (incomeFragment == null)
            incomeFragment = new TransactionSaveFragment();

        type = "income";

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, incomeFragment)
                .addToBackStack(null)
                .commit();
    }

    // open the update fragment
    private void openUpdateFragment()
    {
        if (updateFragment == null)
            updateFragment = new TransactionUpdateFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, updateFragment, "UpdateFragment")
                .addToBackStack(null)
                .commit();
    }

    // open date picker; called from the income/expense fragment
    @Override
    public void openDatePicker() {
        dismissKeyboard();

        if (datePickerFragment == null)
            datePickerFragment = new DatePickerDialogFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("");
        datePickerFragment.show(fragmentTransaction, null);
    }

    @Override
    public void returnDate(int month, int day, int year) {
        onBackPressed();

        if (detailInformation == null) {
            switch (type) {
                case "income":
                    incomeFragment.setDateText(month, day, year);
                    break;
                case "expenses":
                    expenseFragment.setDateText(month, day, year);
                    break;
            }
        } else {
            updateFragment.setDateText(month, day, year);
        }
    }

    // open category picker; called from the income/expense fragment
    @Override
    public void openCategoryPicker() {
        dismissKeyboard();

        if (categoryPickerFragment == null)
            categoryPickerFragment = new CategoryPickerFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .add(R.id.main_activity, categoryPickerFragment, "CategoryPickerFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openCategoryPickerDialog() {
        dismissKeyboard();

        if (categoryPickerDialogFragment == null)
            categoryPickerDialogFragment = new CategoryPickerDialogFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack("");
        categoryPickerDialogFragment.show(fragmentTransaction, null);
    }

    @Override
    public void addCategory(String category) {
        closeFragment();
        categoryPickerFragment.insert(category);
    }

    @Override
    public void deleteCategory(String category) {
        categoryPickerFragment.remove(category);
    }

    @Override
    public String[] getInformation() {
        return detailInformation;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void saveTransaction(String[] info) {
        insert(info);

        String fileName = info[6] + "_" + info[5];
        scrollToCurrentMonth(fileName);
    }

    @Override
    public void updateTransaction(String[] oldInfo, String[] newInfo) {
        update(oldInfo, newInfo);
        adjustAmount(0, oldInfo);
        adjustAmount(1, newInfo);
        String fileName = newInfo[6] + "_" + newInfo[5];
        scrollToCurrentMonth(fileName);
    }

    @Override
    public void deleteTransaction(String[] info) {
        remove(info);
        adjustAmount(0, info);
        Calendar calendar = Calendar.getInstance();
        scrollToCurrentMonth(calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR));
    }

    @Override
    public void closeFragment() {
        dismissKeyboard();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!settings.isEnabled())
            settings.setEnabled(true);

        if (!newExpenses.isEnabled())
            newExpenses.setEnabled(true);

        if (!newIncome.isEnabled())
            newIncome.setEnabled(true);
    }

    // dismiss the on-screen keyboard
    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText() && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    // set the category in income/expense fragment; called from CategoryPickerAdapter.java
    @Override
    public void returnCategory(String category) {
        onBackPressed();

        if (detailInformation == null) {
            switch (type) {
                case "income":
                    incomeFragment.setCategoryText(category);
                    break;
                case "expenses":
                    expenseFragment.setCategoryText(category);
                    break;
            }
        } else {
            updateFragment.setCategoryText(category);
        }
    }

    // open transaction fragment for a specific month
    private void openMonthTransaction(String fileName)
    {
        this.fileName = fileName;

        if (monthFragment == null)
            monthFragment = new TransactionFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, monthFragment)
                .addToBackStack(null)
                .commit();
    }

    // open transaction category fragment for a specific month
    private void openMonthCategoryTransaction(String fileName)
    {
        this.fileName = fileName;

        if (monthCategoryFragment == null)
            monthCategoryFragment = new TransactionCategoryFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, monthCategoryFragment)
                .addToBackStack(null)
                .commit();
    }

    // open transaction category fragment for a specific month
    private void openBudgetTransaction(String fileName)
    {
        this.fileName = fileName;

        if (budgetTransactionFragment == null)
            budgetTransactionFragment = new BudgetTransactionFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(R.id.main_activity, budgetTransactionFragment)
                .addToBackStack(null)
                .commit();
    }

    // MainMenuMonthlyOverviewAdapter.java //
    // ----------------------------------- //
    @Override
    public void onCircleProgressClicked(String fileName) {
        openMonthCategoryTransaction(fileName);
    }

    @Override
    public void onTransactionClicked(String fileName, double incomeAmount, double expensesAmount) {
        inAmt = incomeAmount;
        exAmt = expensesAmount;
        openMonthTransaction(fileName);
    }

    @Override
    public void onBudgetClicked(String fileName) {
        openBudgetTransaction(fileName);
    }

    @Override
    public double getIncomeAmount() {
        return inAmt;
    }

    @Override
    public double getExpenseAmount() {
        return exAmt;
    }

    @Override
    public void changeBackgroundColor(String monthName) {
        // get the index to access the 12 background gradient colors
        int index = getMonthInt(monthName);
        // change the background color every time user scroll to a new position
        main.setBackground(backgrounds.getDrawable(index % 12));

    }


    // MonthTransactionAdapter.java
    @Override
    public void onTransactionClicked(String[] info) {
        detailInformation = info;

        type = info[1];

        openUpdateFragment();
    }

    // TransactionFragment.java
    @Override
    public String getFileName() {
        return fileName;
    }

    private String getMonth(int month)
    {
        switch (month){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            default:
                return "December";
        }
    }

    private int getMonthInt(String month)
    {
        switch (month){
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            default:
                return 11;
        }
    }

    private void adjustAmount(int id, String[] data)
    {
        if (id == 0) {
            if (data[1].equals("income")) {
                inAmt -= Double.parseDouble(data[4]);
            } else {
                exAmt -= Double.parseDouble(data[4]);
            }
        } else {
            if (data[1].equals("income")) {
                inAmt += Double.parseDouble(data[4]);
            } else {
                exAmt += Double.parseDouble(data[4]);
            }
        }
    }

    @Override
    public void addBudget(String budget) {
        closeFragment();
        budgetFragment.insert(budget);
        overviewAdapter.notifyItemRangeChanged(0, monthNames.size());
    }

    @Override
    public void deleteBudget(String budget) {
        budgetFragment.remove(budget);
        overviewAdapter.notifyItemRangeChanged(0, monthNames.size());
    }
}
