/*
    This class sort the file data by date
    and only called in MainActivity.java
 */


package com.monowealth.monowealth;

import java.util.Comparator;

public class SortFileData implements Comparator<String>
{
    public int compare(String a, String b)
    {
        String[] c = a.split(":");
        String[] d = b.split(":");

        int day1 = Integer.parseInt(c[7]);
        int day2 = Integer.parseInt(d[7]);

        int month1 = getMonthInt(c[6]);
        int month2 = getMonthInt(d[6]);

        int year1 = Integer.parseInt(c[5]);
        int year2 = Integer.parseInt(d[5]);

        return (day2 - day1) + ((month2 - month1) * 12) + ((year2 - year1) * 365);
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