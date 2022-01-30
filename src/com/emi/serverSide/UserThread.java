package com.emi.serverSide;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {
    private final Socket socket;
    private final ChatServer chatServer;
    PrintWriter writer;

    public UserThread(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
    }

    @Override
    public void run() {
        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(input));
             OutputStream output = socket.getOutputStream()) {

            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            chatServer.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            chatServer.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]" + clientMessage;
                chatServer.broadcast(serverMessage, this);


            } while (!clientMessage.equalsIgnoreCase("bye"));

        } catch (IOException e) {
            System.out.println("UserThread error " + e.getMessage());
        }
    }

    void printUsers() {
        if (chatServer.hasUsers())
            writer.println("Connected users: " + chatServer.getUserNames());
        else
            writer.println("No other users connected");
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}
