package m.testinguplaod;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.net.URISyntaxException;

/**
 * Created by kjaganmohan on 09/06/16.
 */
public class Utils {

    public static final String urlUpload = "http://www.hirewand.com/silentrecruit/upload?personid=57569834e4b0f525c834db78&filename=abcd.jpg&sessionless=true&authkey=451-c74f9da7-42a1-4592-b717-5422c99ee9bb-234";
    public static final int REQCODE = 1000;
//    public static final String file = "file";
    public static final String filename = "filename";


    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
