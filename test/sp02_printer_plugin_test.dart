import 'package:flutter_test/flutter_test.dart';
import 'package:sp02_printer_plugin/sp02_printer_plugin.dart';
import 'package:sp02_printer_plugin/sp02_printer_plugin_platform_interface.dart';
import 'package:sp02_printer_plugin/sp02_printer_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockSp02PrinterPluginPlatform 
    with MockPlatformInterfaceMixin
    implements Sp02PrinterPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final Sp02PrinterPluginPlatform initialPlatform = Sp02PrinterPluginPlatform.instance;

  test('$MethodChannelSp02PrinterPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSp02PrinterPlugin>());
  });

  test('getPlatformVersion', () async {
    Sp02PrinterPlugin sp02PrinterPlugin = Sp02PrinterPlugin();
    MockSp02PrinterPluginPlatform fakePlatform = MockSp02PrinterPluginPlatform();
    Sp02PrinterPluginPlatform.instance = fakePlatform;
  
    expect(await sp02PrinterPlugin.getPlatformVersion(), '42');
  });
}
