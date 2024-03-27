package com.chin.common;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Some utility functions
 * @author Chin
 */
public class Util {
    /**
     * Add a horizontal line separator to a ViewGroup
     * @param activity
     * @param view The view to add the line to
     */
    public static void addLineSeparator(Activity activity, ViewGroup view) {
        View tmpView = new View(activity);
        tmpView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        tmpView.setBackgroundColor(0xff444444);//dark grey
        view.addView(tmpView);
    }

    /**
     * Add a row with 2 TextView (e.g. Title/Description) to a table.
     * @param table The table to add the row to
     * @param textView1String The text of the first Textview
     * @param textView2String The text of the second TextView
     * @param showLineSeparator true if a line separator is added after the new row
     */
    public static void addRowWithTwoTextView(Activity activity,
            TableLayout table, String textView1String, String textView2String, boolean showLineSeparator) {
        TableRow tr = new TableRow(activity);
        TextView tv1 = new TextView(activity); tv1.setText(textView1String); tr.addView(tv1);
        TextView tv2 = new TextView(activity); tv2.setText(textView2String); tr.addView(tv2);
        table.addView(tr);
        if (showLineSeparator) addLineSeparator(activity, table);
    }


    /**
     * Add a row with 2 TextView (e.g. Title/Description) to a table.
     * @param table The table to add the row to
     * @param textView1String The text of the first Textview
     * @param textView2String The text of the second TextView
     * @param typeface The typeface for the two TextView
     * @param showLineSeparator true if a line separator is added after the new row
     */
    public static void addRowWithTwoTextView(Activity activity,
            TableLayout table, String textView1String, String textView2String, boolean showLineSeparator, Typeface typeface) {
        TableRow tr = new TableRow(activity);
        TextView tv1 = new TextView(activity); tv1.setText(textView1String); tr.addView(tv1);
        TextView tv2 = new TextView(activity); tv2.setText(textView2String); tr.addView(tv2);
        table.addView(tr);
        if (showLineSeparator) addLineSeparator(activity, table);
        tv1.setTypeface(typeface); tv2.setTypeface(typeface);
    }

    /**
     * Add a row with 1 TextView to a table.
     * @param table The table to add the row to
     * @param textViewString The text of the first Textview
     * @param showLineSeparator true if a line separator is added after the new row
     */
    public static void addRowWithOneTextView(Activity activity,
            TableLayout table, String textViewString, boolean showLineSeparator) {
        TableRow tr = new TableRow(activity);
        TextView tv1 = new TextView(activity); tv1.setText(textViewString); tr.addView(tv1);
        table.addView(tr);
        if (showLineSeparator) addLineSeparator(activity, table);
    }

    /**
     * Add a row with n TextView to a table.
     * @param table The table to add the row to
     * @param numTextView The number of TextView to add
     * @param textViewString The text of the Textviews
     * @param showLineSeparator true if a line separator is added after the new row
     */
    public static void addRowWithNTextView(Activity activity,
            TableLayout table, int numTextView, String[] textViewString, boolean showLineSeparator) {
        TableRow tr = new TableRow(activity);
        for (int i = 0; i<numTextView; i++) {
            TextView tv = new TextView(activity);
            tv.setText(textViewString[i]);
            tr.addView(tv);
        }
        table.addView(tr);
        if (showLineSeparator) addLineSeparator(activity, table);
    }

    /**
     * Add a blank row to a table, for formatting/aesthetic purposes
     * @param activity
     * @param table The table to add the blank row to
     */
    public static void addBlankRow(Activity activity, TableLayout table) {
        TableRow trtmp = new TableRow(activity);
        TextView tvtmp = new TextView(activity);
        trtmp.addView(tvtmp);
        table.addView(trtmp);
    }

    /**
     * Determines the network connectivity status
     * @param context
     * @return true if there is connectivity, false if not
     */
    public static boolean hasNetworkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                              activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void replaceView(View oldView, View newView) {
        ViewGroup parent = (ViewGroup) oldView.getParent();
        int index = parent.indexOfChild(oldView);
        parent.removeView(oldView);
        parent.addView(newView, index);
    }

    @SuppressLint("NewApi")
    /**
     * Check for new version
     * @param activity Current activity
     * @param latestVersionUrl Github API url to check for latest version
     * @param pageToGoTo Page to go to if there's a new version
     * @param displayNoNewVersion Whether to display a dialog if there's no new version
     */
    public static void checkNewVersion(final Activity activity, String latestVersionUrl, final String pageToGoTo, boolean displayNoNewVersion) {
        try {
            // there's a rate limit of 60 request/hour
            String jsonString = new NetworkTask().execute(latestVersionUrl).get();
            JSONObject myJSON = new JSONObject(jsonString);

            String s_latestVer = myJSON.getString("tag_name");
            String s_currentVer = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
            Version currentVer = new Version(s_currentVer);
            Version latestVer = new Version(s_latestVer);
            if (latestVer.compareTo(currentVer) > 0) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(pageToGoTo));
                            activity.startActivity(browse);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("New version " + s_latestVer + " is available. Do you want to update?")
                       .setPositiveButton("Yes", dialogClickListener)
                       .setNegativeButton("No", dialogClickListener)
                       .show();
            }
            else if (displayNoNewVersion) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("There's no new version available.")
                       .setPositiveButton("Ok", (dialog, id) -> {
                       });
                builder.create().show();
            }
        } catch (Exception e) {
            Log.i("BBDB", "Unable to get new version");
        }
    }
}
