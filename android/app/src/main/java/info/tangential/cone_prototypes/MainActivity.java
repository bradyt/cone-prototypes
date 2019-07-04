package info.tangential.cone_prototypes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    intent.setType("*/*");
    startActivityForResult(intent, READ_REQUEST_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
    if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      Uri uri = null;
      if (resultData != null) {
        uri = resultData.getData();
        String now = now();
        Log.i("pickUri", now);
        Log.i("pickUri", uri.decode(uri.toString()));
        channelResult.success(now);
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
