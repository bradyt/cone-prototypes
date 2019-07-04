import 'package:flutter/services.dart';

class UriPicker {
  static const _channel =
      const MethodChannel('cone_prototypes.tangential.info/pick_uri');

  static Future<String> pickUri() async {
    const name = 'pickUri';
    String uri;
    try {
      uri = await _channel.invokeMethod<String>(name);
    } on PlatformException catch (e) {
      throw 'Called $name, caught platform exception $e';
    } on MissingPluginException {
      print('$name not implemented');
    }
    return uri;
  }
}
