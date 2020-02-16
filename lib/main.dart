import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final TextField secondTextField = TextField();
    return Scaffold(
      appBar: AppBar(
        title: Text('focus test'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: <Widget>[
            TextField(
              autofocus: true,
              textInputAction: TextInputAction.next,
              onSubmitted: (_) => FocusScope.of(context)
                  .requestFocus(secondTextField.focusNode),
            ),
            secondTextField,
          ],
        ),
      ),
    );
  }
}
