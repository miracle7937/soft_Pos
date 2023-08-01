import 'package:flutter/material.dart';

import '../../screens/amount_pad_screen.dart';
import '../ep_softpos_plugin.dart';
import '../screens/in_app_money_pad.dart';
import '../utils/transaction_type.dart';

class InAppPOS {
  start(BuildContext context, String userId) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (_) => PosAmountScreen(
                  transactionType: TransactionType.purchase,
                  onSelectAmount: (amount) {
                    Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (_) => InAppPOSAccountSelectionScreen(
                                  onSelectAccountType: (accountType) {
                                    EpSoftposPlugin().ePayment(
                                        amount: amount, userID: userId);
                                  },
                                )));
                  },
                )));
  }
}
