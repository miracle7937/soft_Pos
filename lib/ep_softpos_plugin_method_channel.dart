import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'ep_softpos_plugin_platform_interface.dart';

/// An implementation of [EpSoftposPluginPlatform] that uses method channels.
class MethodChannelEpSoftposPlugin extends EpSoftposPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('ep_softpos_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future launchSDK(BuildContext context) async {
    // final version = await methodChannel.invokeMethod('launchSDK');
    // return version;
    // InAppPOS().start(context, "20");
  }

  // @override
  // Future<Function> processPayment(
  //     {required String amount, required String userId}) {
  //
  // }

  @override
  Future processPayment(
      {required String amount, required String userId}) async {
    Map data = {"amount": amount, "userId": userId};
    final version = await methodChannel.invokeMethod('processPayment', data);
    return version;
  }
}
