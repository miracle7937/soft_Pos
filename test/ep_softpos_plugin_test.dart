import 'package:flutter_test/flutter_test.dart';
import 'package:ep_softpos_plugin/ep_softpos_plugin.dart';
import 'package:ep_softpos_plugin/ep_softpos_plugin_platform_interface.dart';
import 'package:ep_softpos_plugin/ep_softpos_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockEpSoftposPluginPlatform 
    with MockPlatformInterfaceMixin
    implements EpSoftposPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final EpSoftposPluginPlatform initialPlatform = EpSoftposPluginPlatform.instance;

  test('$MethodChannelEpSoftposPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelEpSoftposPlugin>());
  });

  test('getPlatformVersion', () async {
    EpSoftposPlugin epSoftposPlugin = EpSoftposPlugin();
    MockEpSoftposPluginPlatform fakePlatform = MockEpSoftposPluginPlatform();
    EpSoftposPluginPlatform.instance = fakePlatform;
  
    expect(await epSoftposPlugin.getPlatformVersion(), '42');
  });
}
