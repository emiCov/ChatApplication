package com.emi.clientSide;

import java.io.*;
import java.net.Socket;


public class ReadThread extends Thread {
    private final Socket socket;
    private final ChatClient chatClient;

    public ReadThread(Socket socket, ChatClient chatClient) {
        this.socket = socket;
        this.chatClient = chatClient;
    }

    @Override
    public void run() {

        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(input))) {

            String serverMessage;

            do {
                serverMessage = reader.readLine();
                if (serverMessage.contains("New user connected"))
                    chatClient.addUser(serverMessage.substring(20));
                else if (serverMessage.contains("has quit"))
                    chatClient.removeUser(serverMessage.split(" ")[0]);
                else
                    System.out.println(serverMessage);

            } while (!serverMessage.equalsIgnoreCase("bye"));

        } catch (IOException e) {
            System.out.println("ReadThread exception " + e.getMessage());
        }
    }
}
