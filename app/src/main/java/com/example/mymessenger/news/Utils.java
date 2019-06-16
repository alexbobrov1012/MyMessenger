package com.example.mymessenger.news;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.example.mymessenger.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static boolean validateInputChannel(String name, String link, Context context) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(link)) {
            Toast hint = Toast.makeText(context.getApplicationContext(), R.string.error_dialog_empty,
                    Toast.LENGTH_SHORT);
            hint.show();
            return false;
        } else if (!Patterns.WEB_URL.matcher(link).matches()) {
            Toast hint = Toast.makeText(context.getApplicationContext(), R.string.error_dialog_link_valid,
                    Toast.LENGTH_SHORT);
            hint.show();
            return false;
        } else if (!link.toLowerCase().contains("rss") && !link.toLowerCase().contains("atom")) {
            Toast hint = Toast.makeText(context.getApplicationContext(), R.string.error_dialog_link_unsup,
                    Toast.LENGTH_SHORT);
            hint.show();
            return false;
        }
        return true;
    }



    public static String parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.getDefault());
        String result = dateString;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        try {
            Date date = format.parse(dateString);
            cal.setTime(date);
            int minutes = cal.get(Calendar.MINUTE);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            if(minutes < 10) {
                result = String.format(Locale.getDefault(),"%d:0%d", hours, minutes);
            } else {
                result = String.format(Locale.getDefault(),"%d:%d", hours, minutes);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

}
