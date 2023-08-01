import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'ep_softpos_plugin_method_channel.dart';

abstract class EpSoftposPluginPlatform extends PlatformInterface {
  EpSoftposPluginPlatform() : super(token: _token);
  static final Object _token = Object();
  static EpSoftposPluginPlatform _instance = MethodChannelEpSoftposPlugin();
  static EpSoftposPluginPlatform get instance => _instance;
  static set instance(EpSoftposPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> launchSDK() {
    throw UnimplementedError('launchSDK has not been implemented.');
  }

  Future<void> processPayment(
      {required String amount, required String userId}) {
    throw UnimplementedError('launchSDK has not been implemented.');
  }
}
