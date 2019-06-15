package com.example.cone_prototypes;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  private static final String CHANNEL = "samples.flutter.dev/dropbox";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    GeneratedPluginRegistrant.registerWith(this);

    new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
                new MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, Result result) {
                      if (call.method.equals("getDropboxStuff")) {
                        String dropboxStuff = getDropboxStuff();

                        if (dropboxStuff != "") {
                          result.success(dropboxStuff);
                        } else {
                          result.error("UNAVAILABLE", "Dropbox Stuff not available.", null);
                        }
                      } else {
                        result.notImplemented();
                      }
                    }
                });


  }

  private String getDropboxStuff() {
    return "hello world";
  }
}
