import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:sp02_printer_plugin/sp02_printer_plugin_method_channel.dart';

void main() {
  MethodChannelSp02PrinterPlugin platform = MethodChannelSp02PrinterPlugin();
  const MethodChannel channel = MethodChannel('sp02_printer_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
