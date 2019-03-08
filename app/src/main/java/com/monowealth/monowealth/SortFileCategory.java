/*
    This class sort the file data by date
    and only called in MainActivity.java
 */


package com.monowealth.monowealth;

import java.util.Comparator;

public class SortFileCategory implements Comparator<String>
{
    public int compare(String a, String b)
    {
        String c = a.split(":")[0];
        String d = b.split(":")[0];

        return c.compareTo(d);
    }
}