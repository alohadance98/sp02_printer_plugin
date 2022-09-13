import 'dart:convert';
import 'dart:developer';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:screenshot/screenshot.dart';
import 'package:sp02_printer_plugin/sp02_printer_plugin.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  void printImage() async {
    String resultValue;
    try {
      Uint8List byte;
      final controller = ScreenshotController();
      byte = await controller.captureFromWidget(_buildPrintTicket1());
      final image = base64Encode(byte);
      final Future<int> result = Sp02PrinterPlugin.goPrint(image);

      resultValue = 'Print: $result';
    } on PlatformException catch (e) {
      resultValue = "Print failed: '${e.message}'.";
    }
  }

  void printPaperFeed() {
    String resultValue;
    try {
      final Future<int> result = Sp02PrinterPlugin.goPaperFeed(150);
      resultValue = 'Print: $result';
    } on PlatformException catch (e) {
      resultValue = "Print failed: '${e.message}'.";
    }
  }

  void getStatus() async {
    String resultValue;
    try {
      final Future<int> result = Sp02PrinterPlugin.getStatus();
      int x = await result;
      resultValue = 'Print: $x';
      log(resultValue);
    } on PlatformException catch (e) {
      resultValue = "Print failed: '${e.message}'.";
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              ElevatedButton(
                  onPressed: () {
                    printImage();
                  },
                  child: const Text('print image')),
              const SizedBox(
                height: 15,
              ),
              ElevatedButton(
                  onPressed: () {
                    printPaperFeed();
                  },
                  child: const Text('paper feed')),
              const SizedBox(
                height: 15,
              ),
              ElevatedButton(
                  onPressed: () {
                    getStatus();
                  },
                  child: const Text('get status')),
            ],
          ),
        ),
      ),
    );
  }
}

Container _buildPrintTicket1() {
  return Container(
    color: Colors.white,
    width: 200,
    height: 400,
    child: Column(
      children: [
        const SizedBox(
          height: 8,
        ),
        const Align(
          alignment: Alignment.center,
          child: Text(
            "VÉ XE",
            style: TextStyle(
                fontWeight: FontWeight.bold, fontSize: 32, color: Colors.black),
          ),
        ),
        const SizedBox(
          height: 16,
        ),
        const Align(
          alignment: Alignment.center,
          child: Text(
            'An',
            style: TextStyle(color: Colors.black),
            // style: TextStyle(fontSize: 16),
          ),
        ),
        const SizedBox(
          height: 8,
        ),
        const Align(
          alignment: Alignment.center,
          child: Text(
            "Tel: 0916767869",
            style: TextStyle(fontSize: 16, color: Colors.black),
          ),
        ),
        const SizedBox(
          height: 8,
        ),
        // Divider(height: 1, color: Colors.black),
        const Text(
          "--------------------------------------------------------------------------------",
          style: TextStyle(fontSize: 20, color: Colors.black),
          maxLines: 1,
        ),
        const SizedBox(
          height: 8,
        ),
        const Align(
          alignment: Alignment.centerLeft,
          child: Text(
            "Từ điểm: Bến xe Bãi cháy",
            // style: TextStyle(fontSize: 16),
            style: TextStyle(color: Colors.black),
          ),
        ),
        const SizedBox(
          height: 8,
        ),
        const Align(
          alignment: Alignment.centerLeft,
          child: Text(
            "Đến điểm: Đông Triều",
            // style: TextStyle(fontSize: 16),
            style: TextStyle(color: Colors.black),
          ),
        ),
        const SizedBox(
          height: 8,
        ),
        const Text(
          "--------------------------------------------------------------------------------",
          style: TextStyle(fontSize: 20, color: Colors.black),
          maxLines: 1,
        ),

        const SizedBox(
          height: 8,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            const Align(
              alignment: Alignment.centerLeft,
              child: Text(
                "GIÁ VÉ",
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.bold,
                    color: Colors.black),
              ),
            ),
            Expanded(child: Container()),
            const Align(
              alignment: Alignment.centerRight,
              child: Text(
                "10.000đ",
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.bold,
                    color: Colors.black),
              ),
            ),
          ],
        ),
        const SizedBox(
          height: 8,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            const Align(
              alignment: Alignment.centerLeft,
              child: Text(
                "TIỀN HÀNG",
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.bold,
                    color: Colors.black),
              ),
            ),
            Expanded(child: Container()),
            const Align(
              alignment: Alignment.centerRight,
              child: Text(
                "50.000đ",
                style: TextStyle(
                    fontSize: 17,
                    fontWeight: FontWeight.bold,
                    color: Colors.black),
              ),
            ),
          ],
        ),

        const Text(
          "========================================================================",
          style: TextStyle(fontSize: 20, color: Colors.black),
          maxLines: 1,
        ),
        const SizedBox(
          height: 16,
        ),
        const Text(
          "Xin cảm ơn!",
          style: TextStyle(fontWeight: FontWeight.bold, color: Colors.black),
        ),
        const SizedBox(
          height: 16,
        ),
        const Text(
          "25/04/2022 18:32",
          //style: TextStyle(fontWeight: FontWeight.bold),
          style: TextStyle(color: Colors.black),
        )
      ],
    ),
  );
}
