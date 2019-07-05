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
  bool _isExpanded = false;
  String expandedValue;
  String headerValue;

  Future getUri() async {
    var uri = await UriPicker.pickUri();

    setState(() {
      _uri = uri;
      headerValue = uri['displayName'];
      expandedValue = uri['uri'];
    });
  }

  @override
  Widget build(BuildContext context) {
    Uri blah = (_uri == null) ? null : Uri.tryParse(_uri['uri']);
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: SingleChildScrollView(
        child: Card(
          child: ExpansionPanelList(
            expansionCallback: (int index, bool isExpanded) {
              setState(
                () {
                  _isExpanded = !isExpanded;
                },
              );
            },
            children: <ExpansionPanel>[
              ExpansionPanel(
                headerBuilder: (BuildContext context, bool isExpanded) =>
                    ListTile(
                      title: Text('Display name'),
                      subtitle: Text(
                        (_uri != null) ? _uri['displayName'] : null.toString(),
                      ),
                    ),
                canTapOnHeader: true,
                body: Column(
                  children: <Widget>[
                    ListTile(
                      title: Text('Authority'),
                      subtitle: Text(
                        (_uri != null) ? _uri['authority'] : null.toString(),
                      ),
                    ),
                    ListTile(
                      title: Text('Path'),
                      subtitle: Text(
                        (_uri != null) ? _uri['path'] : null.toString(),
                      ),
                    ),
                    ListTile(
                      title: Text('Uri'),
                      subtitle: Text(
                        (blah != null) ? '${blah.toString()}' : null.toString(),
                      ),
                    ),
                    ListTile(
                      title: Text('MIME type'),
                      subtitle: Text(
                        (_uri != null) ? _uri['mimeType'] : null.toString(),
                      ),
                    ),
                    ListTile(
                      title: Text('Contents'),
                      subtitle: Text(
                        (_uri != null) ? _uri['contents'] : null.toString(),
                      ),
                    ),
                  ],
                ),
                isExpanded: _isExpanded,
              ),
            ],
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: getUri,
        tooltip: 'Get URI',
        child: Icon(Icons.add),
      ),
    );
  }
}
