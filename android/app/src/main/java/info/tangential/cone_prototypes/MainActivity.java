package info.tangential.cone_prototypes;

import android.app.Activity;
import android.app.Fragment;
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
import java.lang.Exception;
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
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null, null);
        try {
          if (cursor != null && cursor.moveToFirst()) {
            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            Log.i("metadata", "Display Name: " + displayName);

            sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            String size = null;
            if (!cursor.isNull(sizeIndex)) {
              size = cursor.getString(sizeIndex);
            } else {
              size = "Unknown";
            }
            Log.i("metadata", "Size: " + size);
          }
        } finally {
          cursor.close();
        }
        result.put("uri", uri.decode(uri.toString()));
        result.put("displayName", displayName);
        result.put("sizeIndex", Integer.toString(sizeIndex));
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
