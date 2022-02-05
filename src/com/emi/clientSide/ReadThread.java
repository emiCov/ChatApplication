package com.emi.clientSide;

import java.io.*;
import java.net.Socket;


public class ReadThread extends Thread {
    private final Socket socket;
    private final ChatClient chatClient;
    private BufferedReader reader;

    public ReadThread(Socket socket, ChatClient chatClient) {
        this.socket = socket;
        this.chatClient = chatClient;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

        } catch (IOException e) {
            System.out.println("ReadThread exception " + e.getMessage());
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                String serverMessage = reader.readLine();
                if (serverMessage == null)
                    break;

                if (serverMessage.contains("Connected users:")) {
                    String[] existingUsers = serverMessage.split("[\\[\\]]")[1].split("[ ,]+");
                    for (String user : existingUsers)
                        chatClient.addUser(user);
                }

                if (serverMessage.contains("New user connected"))
                    chatClient.addUser(serverMessage.substring(20));
                else if (serverMessage.contains("has quit"))
                    chatClient.removeUser(serverMessage.split(" ")[0]);
                else
                    System.out.println(serverMessage);

            } catch (IOException e) {
                System.out.println("Error reading from the server " + e.getMessage());
            }

        }

    }
}
