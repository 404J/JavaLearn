package com.mars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketIOServer {

    public static void main(String[] args) {

        ServerSocket server = null;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(9090));
            System.out.println("server up use 9090!");
            while (true) {
                Socket client = server.accept();
                System.out.println("client port: " + client.getPort());
                new Thread(
                    () -> {
                        try {
                            InputStream in = client.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            char[] data = new char[1024];
                            while (true) {

                                int num = reader.read(data);

                                if (num > 0) {
                                    System.out.println("client read some data is :" + num + ", val :" + new String(data, 0, num));
                                } else if (num == 0) {
                                    System.out.println("client read nothing!");
                                    continue;
                                } else {
                                    System.out.println("client read -1...");
                                    System.in.read();
                                    client.close();
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                ).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
