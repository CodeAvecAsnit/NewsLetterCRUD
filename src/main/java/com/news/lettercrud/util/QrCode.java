package com.news.lettercrud.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Enumeration;


public class QrCode {
    public static void main(String[] args) throws Exception {
        StringBuilder text = new StringBuilder("http://");
        String localIp = getLocalIp();
        text.append(localIp);
        System.out.println(localIp);
        text.append(":5500");
        System.out.println(text.toString());
        String filePath = "qrcode.png";
        try {
            generateQRCode(text.toString(),filePath);
            System.out.println("Generated");
        }catch (WriterException|IOException e){
            System.out.println("Some error occurred"+e.getMessage());
        }
    }

    static void generateQRCode(String text,String filePath)throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE,200,200);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix,"PNG",path);
    }

    public static String getLocalIp()throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iFace = interfaces.nextElement();
            if (iFace.isLoopback() || !iFace.isUp()) continue;

            Enumeration<InetAddress> addresses = iFace.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
                    return addr.getHostAddress();
                }
            }
        }
        return "localhost";
    }
}
