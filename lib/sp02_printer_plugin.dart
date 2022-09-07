import 'package:flutter/services.dart';

class Sp02PrinterPlugin {
  static const methodChannel = MethodChannel('sp02_printer_plugin');

  static Future<int> goPrint(String image) async {
    final int result =
        await methodChannel.invokeMethod('goPrint', {"arg": image});
    return result;
  }

  static Future<int> goPaperFeed(int number) async {
    final int result =
        await methodChannel.invokeMethod('goPaperFeed', {"arg": number});
    return result;
  }

  static Future<int> getStatus() async {
    final int result = await methodChannel.invokeMethod('getPrinterStatus');
    return result;
  }

  static Future<bool?> bindingPrinter() async {
    final bool? result = await methodChannel.invokeMethod('bindPrinterService');
    return result;
  }

  static Future<bool?> unbindingPrinter() async {
    final bool? result =
        await methodChannel.invokeMethod('unbindPrinterService');
    return result;
  }
}
