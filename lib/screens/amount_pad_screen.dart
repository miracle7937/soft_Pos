import 'package:flutter/material.dart';

import '../reuseable_widget/amount_screen.dart';
import '../reuseable_widget/buttons.dart';
import '../reuseable_widget/custom_snack_bar.dart';
import '../utils/transaction_type.dart';

class PosAmountScreen extends StatefulWidget {
  final TransactionType transactionType;
  final String? title;
  final Function(String)? onSelectAmount;
  const PosAmountScreen(
      {Key? key,
      required this.transactionType,
      this.onSelectAmount,
      this.title})
      : super(key: key);

  @override
  State<PosAmountScreen> createState() => _PosAmountScreenState();
}

class _PosAmountScreenState extends State<PosAmountScreen> {
  String amount = "0";
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Column(
          children: [
            const Spacer(),
            AmountScreen(
                title: widget.title,
                codeLength: 6,
                codeVerify: (v) {
                  setState(() {
                    amount = v;
                  });
                  return Future.value(true);
                }),
            EPButton(
              onTap: () {
                if (num.tryParse(amount)! < 200) {
                  customSnackBar(context,
                      message:
                          "Transactions below NGN200 cannot be processed, please.");
                } else {
                  Navigator.pop(context);
                  widget.onSelectAmount!(amount);
                }
              },
              title: 'Charge',
            ),
            const Spacer(),
          ],
        ),
      ),
    );
  }
}
