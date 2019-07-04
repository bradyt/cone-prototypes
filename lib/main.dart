import 'package:flutter/material.dart';

import 'package:cone_prototypes/channel.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  Map<dynamic, dynamic> _uri;

  Future getUri() async {
    var uri = await UriPicker.pickUri();

    setState(() {
      _uri = uri;
    });
  }

  @override
  Widget build(BuildContext context) {
    Uri blah = (_uri == null) ? null : Uri.tryParse(_uri['uri']);
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: ListView(
        children: <Widget>[
          Card(
            child: ListTile(
              title: Text(
                (blah != null)
                    ? '${blah.fragment.toString()}'
                    : null.toString(),
                style: Theme.of(context).textTheme.display1,
              ),
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: getUri,
        tooltip: 'Get URI',
        child: Icon(Icons.add),
      ),
    );
  }
}
