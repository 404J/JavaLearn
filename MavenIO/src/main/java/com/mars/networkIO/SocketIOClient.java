package com.mars.networkIO;

import java.io.*;
import java.net.Socket;

public class SocketIOClient {
    public static void main(String[] args) {

        try {
            Socket client = new Socket("linux-1",9090);
            OutputStream out = client.getOutputStream();
            InputStream in = System.in;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while(true){
                String line = reader.readLine();
                if(line != null ){
                    byte[] bb = line.getBytes();
                    for (byte b : bb) {
                        out.write(b);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
