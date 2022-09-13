package com.example.sp02_printer_plugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;

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
  IPosPrinterService mIPosPrinterService;
  private static final String TAG = "Scanglesp02";
  private final int PRINTER_NORMAL = 0;

  public ServiceConnection connectService = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mIPosPrinterService = IPosPrinterService.Stub.asInterface(service);
      Log.i(TAG, "***** connectService:");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mIPosPrinterService = null;
    }
  };

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    Intent intent = new Intent();
    intent.setPackage("com.iposprinter.iposprinterservice");
    intent.setAction("com.iposprinter.iposprinterservice.IPosPrintService");
    flutterPluginBinding.getApplicationContext().startService(intent);
    flutterPluginBinding.getApplicationContext().bindService(intent, connectService, Context.BIND_AUTO_CREATE);
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sp02_printer_plugin");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("goPrint")) {
      String image = call.argument("arg");
      int resultValue = goPrint(image);
      result.success(resultValue);
    } else if (call.method.equals("goPaperFeed")) {
      int line = call.argument("arg");
      int resultValue = goPaperFeed(line);
      result.success(resultValue);
    } else if (call.method.equals("getPrinterStatus")) {
      int resultValue = getPrinterStatus();
      result.success(resultValue);
    } else {
      result.notImplemented();
    }
  }

  public int goPrint(String image) {
    int resultValue = -1;

    if (getPrinterStatus() == PRINTER_NORMAL) {
//            printText(Text);
      printImage(image);
      resultValue = 1;
    }
    return resultValue;
  }

  public int goPaperFeed(int line) {
    int resultValue = -1;

    if (getPrinterStatus() == PRINTER_NORMAL) {
      feedPaper(line);
      resultValue = 1;
    }
    return resultValue;
  }

  private void feedPaper(int line) {
    try {
//            mIPosPrinterService.printerFeedLines(line, callback);
      mIPosPrinterService.printerPerformPrint(line, callback);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  private void printImage(String image) {
    int alignment = 1;
    int bitmapSize = 16;
    byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
    Bitmap mBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    // Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
    try {
      mIPosPrinterService.printBitmap(alignment, bitmapSize, mBitmap, callback);
      mIPosPrinterService.printerPerformPrint(70, callback);
    }catch (RemoteException e) {
      e.printStackTrace();
    }

  }

  public int getPrinterStatus() {
    int printerStatus = -1;
    try {
      printerStatus = mIPosPrinterService.getPrinterStatus();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    Log.i(TAG, "#### printerStatus:" + printerStatus);
    return printerStatus;
  }

  public IPosPrinterCallback callback = new IPosPrinterCallback.Stub() {
    @Override
    public void onRunResult(final boolean isSuccess) throws RemoteException {
      Log.i(TAG, "result callback:" + isSuccess + "\n");
    }

    @Override
    public void onReturnString(final String value) throws RemoteException {
      Log.i(TAG, "result callback:" + value + "\n");
    }
  };

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    binding.getApplicationContext().unbindService(connectService);
    channel.setMethodCallHandler(null);
  }
}
