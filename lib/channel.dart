import 'package:flutter/services.dart';

class UriPicker {
  static const _channel =
      const MethodChannel('cone_prototypes.tangential.info/pick_uri');

  static Future<Map<dynamic, dynamic>> pickUri() async {
    const name = 'pickUri';
    Map<dynamic, dynamic> uri;
    try {
      uri = await _channel.invokeMethod<Map<dynamic, dynamic>>(name);
    } on PlatformException catch (e) {
      throw 'Called $name, caught platform exception $e';
    } on MissingPluginException {
      print('$name not implemented');
    }
    return uri;
  }
  // once we have a uri
  // we want:
  //   display name and size
  //   can we read
  //   can we write
  //   can we do both
}
