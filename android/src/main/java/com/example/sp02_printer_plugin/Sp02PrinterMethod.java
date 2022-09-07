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

import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;

public class Sp02PrinterMethod {
    private static final String TAG = "Scanglesp02";
    IPosPrinterService mIPosPrinterService;
    private Context _context;
    private final int PRINTER_NORMAL = 0;

    public Sp02PrinterMethod(Context context) {
        this._context = context;
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
//            printText(Text);
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

    public void bindPrinterService() {
        Intent intent = new Intent();
        intent.setPackage("com.iposprinter.iposprinterservice");
        intent.setAction("com.iposprinter.iposprinterservice.IPosPrintService");
        _context.bindService(intent, connectService, Context.BIND_AUTO_CREATE);
    }

    public void unbindPrinterService() {
        _context.unbindService(connectService);
    }

}
