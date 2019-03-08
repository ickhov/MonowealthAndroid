/*
    This class sort the file names by the
    year first and then the month names
 */

package com.monowealth.monowealth;

import java.util.Comparator;

public class SortFileNames implements Comparator<String>
{
    public int compare(String a, String b)
    {
        String[] c = a.split("_");
        String[] d = b.split("_");

        int month1 = getMonthInt(c[0]);
        int month2 = getMonthInt(d[0]);

        int year1 = Integer.parseInt(c[1]);
        int year2 = Integer.parseInt(d[1]);

        return ((month1 - month2) * 12) + ((year1 - year2) * 365);
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
}
