/*
    This class fills in the gap between months
    For example, if we have January and April,
    then this class will add February and
    March in between
 */

package com.monowealth.monowealth;

import java.util.ArrayList;

public class MonthGapFiller {

    public MonthGapFiller() { }

    public ArrayList<String> fillGap(String[] months)
    {
        // list of months after filling in the gap
        ArrayList<String> items = new ArrayList<>();

        // store the month name and year to combine as a string
        int monthCounter = 0;
        int yearCounter = 0;
        String name = "";

        // loop through two months at a time to find their gap and insert missing months
        for (int i = 0; i < months.length - 1; i++)
        {
            // first and second month to compare
            String[] first = (months[i]).split("_");
            String[] second = (months[i + 1]).split("_");

            // first and second month int
            int month1 = getMonthInt(first[0]);
            int month2 = getMonthInt(second[0]);

            // first and second year int
            int year1 = Integer.parseInt(first[1]);
            int year2 = Integer.parseInt(second[1]);

            // number of month gap between the two months
            int gap = ((year2 - year1) * 12) + (month2 - month1);

            // start with the beginning month
            monthCounter = month1;

            // start with the beginning year
            yearCounter = year1;

            for (int j = 0; j < gap; j++)
            {
                name = getMonth(monthCounter);
                items.add(name + "_" + yearCounter);

                // month = December so reset and increment year
                // else increment month
                if (monthCounter == 11)
                {
                    monthCounter = 0;
                    yearCounter++;
                } else {
                    monthCounter++;
                }
            }
        }

        name = getMonth(monthCounter);
        items.add(name + "_" + yearCounter);

        return items;

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

}
