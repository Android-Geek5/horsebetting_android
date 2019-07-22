package com.thrillingpicks.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    static String TAG = CommonUtils.class.getSimpleName();
    public static String USER = "ThrillingPicksUser";

    /*check Empty string validation*/
    public static boolean validateForNull(Context context, EditText p_editText, String p_nullMsg) {
        boolean m_isValid = false;
        try {
            try {
                if (p_editText != null && p_nullMsg != null) {
                    if (TextUtils.isEmpty(p_editText.getText().toString().trim())) {
                        try {
                            Toast.makeText(context, "" + p_nullMsg, Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        m_isValid = false;
                    } else {
                        m_isValid = true;
                    }
                }
            } catch (Throwable p_e) {
                p_e.printStackTrace(); // Error handling if application crashes
            }
            return m_isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return m_isValid;
        }
    }

    /*check internet connection */
    public static boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            /*  connected to the internet*/
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    /* connected to wifi*/
                    return true;

                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    /* connected to the mobile provider's data plan*/
                    return true;

                }
            } else {
                /* not connected to the internet*/
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /*check email Pattern  */
    public static boolean validateEmail(Context context, EditText p_editText, String p_nullMsg, String p_invalidMsg) {
        boolean m_isValid = false;
        try {
            if (p_editText != null) {
                if (validateForNull(context, p_editText, p_nullMsg)) {
                    Pattern m_pattern = Pattern.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
                    Matcher m_matcher = m_pattern.matcher(p_editText.getText().toString().trim());
                    if (!m_matcher.matches() && p_editText.getText().toString().trim().length() > 0) {
                        m_isValid = false;
                        try {
                            Toast.makeText(context, "" + p_nullMsg, Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        m_isValid = true;
                    }
                } else {
                    m_isValid = false;
                }
            } else {
                m_isValid = false;
            }
        } catch (Throwable p_e) {
            p_e.printStackTrace(); // Error handling if application crashes
        }
        return m_isValid;
    }

    /*chack name patteren */
    public static boolean checkNameValidation(Context context, EditText editText, String msg) {
        try {
            Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
            Matcher ms = ps.matcher(editText.getText().toString());
            boolean bs = ms.matches();
            if (bs) {

                return true;
            } else {
                Log.e("check_name_validion", "false");
                Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*check password length */
    public static boolean validPassword(Context context, EditText editText, String msg) {
        try {
            Pattern ps = Pattern.compile("^(?=.*[0-9])[a-zA-Z0-9\\!\\@\\#\\$\\%\\&\\*]{6,}");

            Matcher ms = ps.matcher(editText.getText().toString());
            boolean bs = ms.matches();
            if (bs) {
                return true;
            } else {
                Log.e("check_name_validion", "false");
                Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    /*show keyboard */
    public static void showKeyboard(final EditText ettext, final Context context) {
        try {
            ettext.requestFocus();
            ettext.postDelayed(new Runnable() {
                                   @Override
                                   public void run() {
                                       InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                       keyboard.showSoftInput(ettext, 0);
                                   }
                               }
                    , 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*check confirm or email same*/
    public static boolean EmailMatch(Context context, EditText email, EditText confirmEmail, String msg) {
        try {
            if (email.getText().toString().trim().equals(confirmEmail.getText().toString().trim())) {
                return true;
            } else {
                Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*check confirm or email same*/
    public static boolean PasswordMatch(Context context, EditText email, EditText confirmEmail, String msg) {
        try {
            if (email.getText().toString().trim().equals(confirmEmail.getText().toString().trim())) {
                return true;
            } else {
                Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*check string null or not*/
    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }


    /*comare two dates*/
    public static String compareDate(String user_subscripuser_subscription_end_datetion_end_date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Log.e(TAG, "Today date:--" + dateFormat.format(date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date current_date = null, subs_end_date = null;
        try {
            current_date = sdf.parse(dateFormat.format(date));
            subs_end_date = sdf.parse(user_subscripuser_subscription_end_datetion_end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("date1 : " + sdf.format(current_date));
        System.out.println("date2 : " + sdf.format(subs_end_date));

        if (current_date.compareTo(subs_end_date) > 0) {

            Log.e(TAG, "Date1 is after Date2");
            return "after";
        } else if (current_date.compareTo(subs_end_date) < 0) {

            Log.e(TAG, "Date1 is before Date2");
            return "before";
        } else if (current_date.compareTo(subs_end_date) == 0) {
            Log.e(TAG, "Date1 is equal to Date2");
            return "equal";

        } else {
            return "";
        }

    }

    /*create MD5 key*/
    public static String convertMD5(String hashtext1) {
        try {
            String key = hashtext1;
            Log.e("fdjhfdjdfmfd", "" + key);
            // Static getIn+stance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(key.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;

        }
        // For specifying wrong message digest algorithms
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }


    }
    public static String getTomorrowDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();

        Log.e(TAG, "Today date:--" + dateFormat.format(dt));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date current_date = null, subs_end_date = null;
        try {
            current_date = sdf.parse(dateFormat.format(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("date1 : " + sdf.format(current_date));
return sdf.format(current_date);
    }

    public static String getTodayDate(){
        //Date format yyyy-MM-dd HH:ss:ss
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        Log.e(TAG, "Today date:--" + dateFormat.format(date));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date current_date = null, subs_end_date = null;
        try {
            current_date = sdf.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("date1 : " + sdf.format(current_date));
        return sdf.format(current_date);
    }

}
