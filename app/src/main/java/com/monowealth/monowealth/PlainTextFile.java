/*
    This reads, write, and update the files
    used to store data for the app.
 */

package com.monowealth.monowealth;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class PlainTextFile {

    private Context context;

    public PlainTextFile(Context context)
    {
        this.context = context;
    }

    public String[] getFileNames() {
        // get file path
        File file = new File(context.getFilesDir().getAbsolutePath());

        // get all the files in the directory
        File[] list = file.listFiles();

        ArrayList<String> fileNames = new ArrayList<>();

        for (File l : list) {
            String fileName = l.getName();

            if (!fileName.equals("all_income_categories.txt")
                    && !fileName.equals("all_expenses_categories.txt")
                    && !fileName.equals("current_income_categories.txt")
                    && !fileName.equals("current_expenses_categories.txt")
                    && !fileName.equals("budget.txt")) {
                fileNames.add((fileName.split("\\."))[0]);
            }
        }

        String[] f = new String[fileNames.size()];
        f = fileNames.toArray(f);

        return f;
    }

    // write to specific file
    public void write(String[] information) throws IOException
    {
        String fileName = information[6] + "_" + information[5] + ".txt";

        ArrayList<String> data = readByLine(fileName);
        data.add(generateInfo(information));
        Collections.sort(data, new SortFileData());

        // get file path
        File file = new File(context.getFilesDir().getAbsolutePath(), fileName);

        // open Okio sink and buffered writer
        Sink fileSink = Okio.sink(file, false);
        BufferedSink sink = Okio.buffer(fileSink);

        // write using UTF-8 encoding
        for (int i = 0; i < data.size(); i++)
            sink.writeUtf8(data.get(i)).writeUtf8("\n");

        // close writer and file sink
        sink.close();
        fileSink.close();
    }

    // write to specific category file
    public void writeCategory(String scope, String type, String information) throws IOException
    {
        String fileName = scope + "_" + type + "_categories.txt";

        ArrayList<String> data = readCategory(scope, type);
        data.add(information);
        Collections.sort(data);

        // get file path
        File file = new File(context.getFilesDir().getAbsolutePath(), fileName);

        // open Okio sink and buffered writer
        Sink fileSink = Okio.sink(file, false);
        BufferedSink sink = Okio.buffer(fileSink);

        // write using UTF-8 encoding
        for (int i = 0; i < data.size(); i++)
            sink.writeUtf8(data.get(i)).writeUtf8("\n");

        // close writer and file sink
        sink.close();
        fileSink.close();
    }

    // write to specific category file
    public void writeBudget(String information) throws IOException
    {
        // get file path
        File file = new File(context.getFilesDir().getAbsolutePath(), "budget.txt");

        // open Okio sink and buffered writer
        Sink fileSink = Okio.sink(file, true);
        BufferedSink sink = Okio.buffer(fileSink);

        // write using UTF-8 encoding
        sink.writeUtf8(information).writeUtf8("\n");

        // close writer and file sink
        sink.close();
        fileSink.close();
    }

    // read a specific file
    public ArrayList<String[]> read(String fileName) throws IOException
    {
        ArrayList<String[]> data = new ArrayList<>();

        // get file path
        File file = new File(context.getFilesDir().getAbsolutePath(), fileName);

        if (file.exists()) {

            // open Okio source and buffered reader
            Source fileSource = Okio.source(file);
            BufferedSource source = Okio.buffer(fileSource);

            // read line by line add them to data as []
            for (String line; (line = source.readUtf8Line()) != null; )
                data.add(line.split(":"));

            // close reader and file source
            source.close();
            fileSource.close();
        }

        return data;
    }

    // read a specific file
    public ArrayList<String> readByLine(String fileName) throws IOException
    {
        ArrayList<String> data = new ArrayList<>();

        // get file path
        File file = new File(context.getFilesDir().getAbsolutePath(), fileName);

        if (file.exists()) {

            // open Okio source and buffered reader
            Source fileSource = Okio.source(file);
            BufferedSource source = Okio.buffer(fileSource);

            // read line by line add them to data as []
            for (String line; (line = source.readUtf8Line()) != null; )
                data.add(line);

            // close reader and file source
            source.close();
            fileSource.close();
        }

        return data;
    }

    // read a specific category file
    public ArrayList<String> readCategory(String scope, String type) throws IOException
    {
        // scope = {all, current}
        String fileName = scope + "_" + type + "_categories.txt";

        ArrayList<String> data = new ArrayList<>();

        // get file path
        File file = new File(context.getFilesDir().getAbsolutePath(), fileName);

        if (file.exists()) {

            // open Okio source and buffered reader
            Source fileSource = Okio.source(file);
            BufferedSource source = Okio.buffer(fileSource);

            // read line by line add them to data as []
            for (String line; (line = source.readUtf8Line()) != null; )
                data.add(line);

            // close reader and file source
            source.close();
            fileSource.close();
        }

        return data;
    }

    // read a specific category file
    public ArrayList<String> readBudget() throws IOException
    {
        ArrayList<String> data = new ArrayList<>();

        // get file path
        File file = new File(context.getFilesDir().getAbsolutePath(), "budget.txt");

        if (file.exists()) {

            // open Okio source and buffered reader
            Source fileSource = Okio.source(file);
            BufferedSource source = Okio.buffer(fileSource);

            // read line by line add them to data as []
            for (String line; (line = source.readUtf8Line()) != null; )
                data.add(line);

            // close reader and file source
            source.close();
            fileSource.close();
        }

        return data;
    }

    public boolean delete(String[] itemToDelete, ArrayList<String[]> items) throws IOException
    {
        String fileName = "";
        boolean fileDeleted = false;

        // find the ID to delete in the array and delete it
        for (int i = 0; i < items.size(); i++)
        {
            String item = items.get(i)[0];
            if (item.equals(itemToDelete[0]))
            {
                String[] temp = items.get(i);
                fileName = temp[6] + "_" + temp[5] + ".txt";
                items.remove(i);
                break;
            }
        }


        if (!fileName.equals(""))
        {
            File file = new File(context.getFilesDir().getAbsolutePath(), fileName);
            fileDeleted = file.delete();

            // if item is not empty, then rewrite items into the file
            if (!items.isEmpty())
            {
                // delete and write the file again
                for (int i = 0; i < items.size(); i++) {
                    write(items.get(i));
                }

                fileDeleted = false;
            }
        }

        return fileDeleted;
    }

    public void deleteCategory(String scope, String type, String information, ArrayList<String> items) throws IOException
    {
        String fileName = "";

        // find the ID to delete in the array and delete it
        for (int i = 0; i < items.size(); i++)
        {
            String item = items.get(i);
            if (item.equals(information))
            {
                fileName = scope + "_" + type + "_categories.txt";
                items.remove(i);
                break;
            }
        }

        if (!fileName.equals(""))
        {
            File file = new File(context.getFilesDir().getAbsolutePath(), fileName);
            file.delete();

            // if item is not empty, then rewrite items into the file
            if (!items.isEmpty())
            {
                // delete and write the file again
                for (int i = 0; i < items.size(); i++) {
                    writeCategory(scope, type, items.get(i));
                }
            }
        }
    }

    public void deleteBudget(String information, ArrayList<String> items) throws IOException
    {
        String fileName = "";

        // find the ID to delete in the array and delete it
        for (int i = 0; i < items.size(); i++)
        {
            String item = items.get(i);
            if (item.equals(information))
            {
                fileName = "budget.txt";
                items.remove(i);
                break;
            }
        }

        if (!fileName.equals(""))
        {
            File file = new File(context.getFilesDir().getAbsolutePath(), fileName);
            file.delete();

            // if item is not empty, then rewrite items into the file
            if (!items.isEmpty())
            {
                // delete and write the file again
                for (int i = 0; i < items.size(); i++) {
                    writeBudget(items.get(i));
                }
            }
        }
    }

    public boolean update(String[] oldInfo, String[] newInfo, ArrayList<String[]> items) throws IOException
    {
        boolean fileDeleted = delete(oldInfo, items);
        write(newInfo);

        return fileDeleted;
    }

    private String generateInfo(String[] item)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(item[0]);

        for (int i = 1; i < item.length; i++) {
            builder.append(":");
            builder.append(item[i]);
        }

        return builder.toString();
    }
}
