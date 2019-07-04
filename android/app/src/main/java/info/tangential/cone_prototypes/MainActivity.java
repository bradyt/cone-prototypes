package info.tangential.cone_prototypes;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(new MethodCallHandler() {
      @Override
      public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("pickUri")) {
          String now = now();
          Log.i("pickUri", now);
          result.success(now);
        } else {
          Log.i("pickUri", "Method " + call.method + " did not equal anything");
          result.notImplemented();
        }
      }
    });
  }

  String now() {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS'Z'");
    df.setTimeZone(tz);
    return df.format(new Date());
  }
}
