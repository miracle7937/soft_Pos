import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../constant/color.dart';
import '../utils/null_checker.dart';

typedef DeleteCode = void Function();
typedef CodeVerify = Function(String code);

class AmountScreen extends StatefulWidget {
  final int codeLength;
  final bool shuffle;
  final CodeVerify? codeVerify;
  final bool forPin;
  final String? title;

  AmountScreen(
      {this.codeLength = 2,
      this.codeVerify,
      this.shuffle = false,
      this.forPin = true,
      this.title})
      : assert(codeLength > 0),
        assert(codeVerify != null);

  @override
  State<StatefulWidget> createState() => _AmountScreenState();
}

class _AmountScreenState extends State<AmountScreen> {
  var _inputLength = 6;
  var _inputList = <int>[];
  List<int> values = List.generate(9, (i) => i + 1)..add(0);
  TextEditingController controller = TextEditingController(text: 'NGN 0');
  @override
  void initState() {
    if (widget.shuffle) {
      values.shuffle();
    }
    //ToDo check biometrics setup
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          const SizedBox(
            height: 10,
          ),
          Text(
            widget.title ?? 'Enter Your Transaction Amount',
            style: Theme.of(context).textTheme.bodyText1!.copyWith(
                fontWeight: FontWeight.w600, color: EPColors.appMainColor),
          ),

          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 30.0),
            child: TextFormField(
              showCursor: false,
              style: Theme.of(context).textTheme.caption!.copyWith(
                  fontSize: 40.0,
                  fontWeight: FontWeight.bold,
                  color: EPColors.appMainColor),
              textAlign: TextAlign.center,
              controller: controller,
              decoration: const InputDecoration(
                border: InputBorder.none,
              ),
            ),
          ),
          // Text(_inputList.join()),

          // Padding(
          //   padding: EdgeInsets.symmetric(
          //     vertical: 0,
          //   ),
          //   child: Row(
          //     mainAxisAlignment: MainAxisAlignment.center,
          //     children: Iterable<int>.generate(_inputLength)
          //         .map(
          //           (e) => Container(
          //             padding: EdgeInsets.all(10),
          //             child: SizedBox(
          //               child: Container(
          //                 decoration: BoxDecoration(
          //                   color: _inputList.length > e
          //                       ? Colors.black
          //                       : Colors.white,
          //                   border:
          //                       new Border.all(color: Colors.red, width: 2.0),
          //                   borderRadius: BorderRadius.all(
          //                     Radius.circular(5),
          //                   ),
          //                 ),
          //               ),
          //               width: 10,
          //               height: 10,
          //             ),
          //           ),
          //         )
          //         .toList(),
          //   ),
          // ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 50),
            child: AspectRatio(
              aspectRatio: .7,
              child: Wrap(
                children: Iterable<int>.generate(12).map(
                  (e) {
                    switch (e.toInt()) {
                      case 9:
                        {
                          return FractionallySizedBox(
                            widthFactor: 1 / 3,
                            child: AspectRatio(
                              aspectRatio: 1,
                              child: TextButton(
                                child: Text(
                                  ("clear").toString(),
                                  style: TextStyle(
                                    color: EPColors.appMainColor,
                                    fontSize: 20.0,
                                    fontWeight: FontWeight.w400,
                                  ),
                                ),
                                onPressed: () {
                                  clearAll();
                                  setState(() {});
                                },
                              ),
                            ),
                          );
                          // return FractionallySizedBox(
                          //   widthFactor: 1 / 3,
                          //   child: AspectRatio(
                          //     aspectRatio: 1,
                          //     child: Container(
                          //       child: Text("C"),
                          //     ),
                          //   ),
                          // );
                        }
                      case 10:
                        {
                          return FractionallySizedBox(
                            widthFactor: 1 / 3,
                            child: AspectRatio(
                              aspectRatio: 1,
                              child: TextButton(
                                child: Text(
                                  (values[9]).toString(),
                                  style: TextStyle(
                                    color: EPColors.appMainColor,
                                    fontSize: 25.0,
                                    fontWeight: FontWeight.w400,
                                  ),
                                ),
                                onPressed: () {
                                  setState(() {
                                    addChar(values[9]);
                                  });
                                },
                              ),
                            ),
                          );
                        }
                      case 11:
                        {
                          return FractionallySizedBox(
                            widthFactor: 1 / 3,
                            child: AspectRatio(
                              aspectRatio: 1,
                              child: TextButton(
                                child: SizedBox(
                                  height: 50,
                                  width: 50,
                                  child: Center(
                                      child: Icon(
                                    Icons.arrow_back_ios_sharp,
                                    color: EPColors.appMainColor,
                                  )),
                                ),
                                onPressed: () {
                                  setState(() {
                                    undoLastInput();
                                  });
                                },
                              ),
                            ),
                          );
                        }
                      default:
                        {
                          return FractionallySizedBox(
                            widthFactor: 1 / 3,
                            child: AspectRatio(
                              aspectRatio: 1,
                              child: TextButton(
                                child: Text(
                                  (values[e.toInt()]).toString(),
                                  style: TextStyle(
                                    color: EPColors.appMainColor,
                                    fontSize: 25.0,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                onPressed: () {
                                  setState(() {
                                    addChar(values[e.toInt()]);
                                  });
                                },
                              ),
                            ),
                          );
                        }
                    }
                  },
                ).toList(),
              ),
            ),
          ),
        ],
      ),
    );
  }

  checkingEmptyValue() {
    print("hello ${_inputList.join()}");
    var amount =
        _inputList.join().isEmpty ? "NGN 0" : moneyFormatter(_inputList.join());
    controller.text = amount;
    String _amount = _inputList.isEmpty ? "0" : _inputList.join();
    setState(() {});

    widget.codeVerify!(int.parse(_amount).toString()).whenComplete(() {
      setState(() {});
    }).then((onValue) async {
      if (!mounted) return;
      if (onValue) {
        setState(() {});
      } else {
        setState(() {});
      }
    });
  }

  String moneyFormatter(String? amount) {
    if (isEmpty(amount)) {
      return "0";
    }

    NumberFormat currencyFormatter = NumberFormat.currency(
      locale: 'en_US', // Replace with your desired locale
      symbol: 'NGN ', // Replace with your desired currency symbol
      decimalDigits: 0, // Number of decimal places
    );
    return currencyFormatter.format(int.parse(amount!));
  }

  clearAll() {
    controller.text = "NGN0";
    _inputList.clear();
  }

  void addChar(int value) {
    _verifyAmount(value);

    // if (_inputList.isEmpty && value == 0) {
    // } else {
    //   _inputList.add(value);
    // }
    // checkingEmptyValue();
    // print(_inputList);
  }

  void undoLastInput() {
    if (_inputList.isEmpty) {
      return;
    }
    _inputList.removeLast();

    checkingEmptyValue();
  }

  _verifyAmount(int value) async {
    if (_inputList.isEmpty && value == 0) {
      return;
    }
    _inputList.add(value);
    String _amount = _inputList.join();
    controller.text = moneyFormatter(_amount);
    setState(() {});

    widget.codeVerify!(_amount).whenComplete(() {
      setState(() {});
    }).then((onValue) async {
      if (!mounted) return;
      if (onValue) {
        setState(() {});
      } else {
        setState(() {});
      }
    });
  }
}
