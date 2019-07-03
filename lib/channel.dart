import 'package:flutter/services.dart';

class UriPicker {
  static const _channel =
      const MethodChannel('cone_prototypes.tangential.info/pick_uri');

  static Future<String> pickUri() async {
    final String uri = await _channel.invokeMethod<String>('pickUri');
    return uri;
  }
}
