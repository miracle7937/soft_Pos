import 'package:flutter/material.dart';

import 'ep_softpos_plugin_platform_interface.dart';
import 'in_ap/In_app_pos.dart';

class EpSoftposPlugin {
  Future<String?> getPlatformVersion() {
    return EpSoftposPluginPlatform.instance.getPlatformVersion();
  }

  Future<void> launchSDK(BuildContext context) async {
    InAppPOS().start(context, "20");
    // return EpSoftposPluginPlatform.instance.launchSDK(context);
  }

  Future ePayment({required String amount, required String userID}) async {
    return EpSoftposPluginPlatform.instance
        .processPayment(amount: amount, userId: userID);
  }
}
