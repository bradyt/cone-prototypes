package info.tangential.cone_prototypes;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Exception;
import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends FlutterActivity {
  private static final String CHANNEL = "cone_prototypes.tangential.info/pick_uri";
  private static final int READ_REQUEST_CODE = 1;
  Result channelResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(new MethodCallHandler() {
      @Override
      public void onMethodCall(MethodCall call, Result result) {
        channelResult = result;
        if (call.method.equals("pickUri")) {
          performFileSearch();
        } else {
          Log.i("pickUri", "Method " + call.method + " did not equal anything");
          result.notImplemented();
        }
      }
    });
  }

  public void performFileSearch() {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    intent.setType("*/*");
    startActivityForResult(intent, READ_REQUEST_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
    if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      Uri uri = null;
      String displayName = null;
      int sizeIndex = -1;
      HashMap<String, String> result = new HashMap<String, String>();
      if (resultData != null) {
        uri = resultData.getData();
        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null, null);
        try {
          if (cursor != null && cursor.moveToFirst()) {
            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

            sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            String size = null;
            if (!cursor.isNull(sizeIndex)) {
              size = cursor.getString(sizeIndex);
            } else {
              size = "Unknown";
            }
          }
        } finally {
          cursor.close();
        }

        try {
          Log.i("trace", "94");
          InputStream inputStream = contentResolver.openInputStream(uri);
          OutputStream outputStream = contentResolver.openOutputStream(uri);
          Log.i("trace", "96");
          StringBuilder stringBuilder = new StringBuilder();
          Log.i("trace", "98");
          BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
          Log.i("trace", "100");
          String line;
          Log.i("trace", "102");
          while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
          }
          Log.i("trace", "106");
          inputStream.close();
          Log.i("trace", "108");
          result.put("contents", stringBuilder.toString());
          Log.i("trace", "110");
        } catch (FileNotFoundException e) {
          result.put("contents", e.toString());
          Log.i("FNFE", e.toString());
        } catch (Exception e) {
          Log.i("E", e.toString());
        }

        result.put("uri", uri.decode(uri.toString()));
        result.put("displayName", displayName);
        result.put("sizeIndex", Integer.toString(sizeIndex));
        result.put("authority", uri.getAuthority());
        result.put("path", uri.getPath());
        result.put("mimeType", contentResolver.getType(uri));
        channelResult.success(result);
      }
    }
  }

  String now() {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS'Z'");
    df.setTimeZone(tz);
    return df.format(new Date());
  }
}
