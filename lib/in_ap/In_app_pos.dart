import 'package:flutter/material.dart';

import '../../screens/amount_pad_screen.dart';
import '../ep_softpos_plugin.dart';
import '../utils/transaction_type.dart';

class InAppPOS {
  start(BuildContext context, String userId) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (_) => PosAmountScreen(
                  transactionType: TransactionType.purchase,
                  onSelectAmount: (amount) {
                    EpSoftposPlugin().ePayment(amount: amount, userID: userId);
                  },
                )));
  }
}
