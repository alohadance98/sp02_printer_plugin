package com.example.sp02_printer_plugin;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** Sp02PrinterPlugin */
public class Sp02PrinterPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Sp02PrinterMethod sp02PrinterMethod;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sp02_printer_plugin");
    sp02PrinterMethod = new Sp02PrinterMethod(flutterPluginBinding.getApplicationContext());
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("goPrint")) {
      String image = call.argument("arg");
      int resultValue = sp02PrinterMethod.goPrint(image);
      result.success(resultValue);
    } else if (call.method.equals("goPaperFeed")) {
      int line = call.argument("arg");
      int resultValue = sp02PrinterMethod.goPaperFeed(line);
      result.success(resultValue);
    } else if (call.method.equals("getPrinterStatus")) {
      int resultValue = sp02PrinterMethod.getPrinterStatus();
      result.success(resultValue);
    } else if (call.method.equals("bindPrinterService")) {
      sp02PrinterMethod.bindPrinterService();
      result.success(true);
    } else if (call.method.equals("unbindPrinterService")) {
      sp02PrinterMethod.unbindPrinterService();
      result.success(true);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
