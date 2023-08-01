import 'ep_softpos_plugin_platform_interface.dart';

class EpSoftposPlugin {
  Future<String?> getPlatformVersion() {
    return EpSoftposPluginPlatform.instance.getPlatformVersion();
  }

  Future<void> launchSDK() {
    return EpSoftposPluginPlatform.instance.launchSDK();
  }

  Future ePayment({required String amount, required String userID}) async {
    return EpSoftposPluginPlatform.instance
        .processPayment(amount: amount, userId: userID);
  }
}
