package com.emi.serverSide;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {
    private final Socket socket;
    private final ChatServer chatServer;
    PrintWriter writer;
    BufferedReader reader;

    public UserThread(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            // sends a list of connected users
            printUsers();

            // adds this user to the Map
            String userName = reader.readLine();
            chatServer.addUser(userName, this);

            // sends everyone a message about who has connected
            String serverMessage = "New user connected: " + userName;
            chatServer.broadcastToEveryone(serverMessage, this);

            String clientMessage;

            while (true) {
                clientMessage = reader.readLine();

                if (clientMessage.equalsIgnoreCase("bye"))
                    break;

                if (!clientMessage.contains("@"))
                    continue;

                // splits the received message into recipient and message
                String messageReceiver = clientMessage.substring(1,clientMessage.indexOf(' '));
                clientMessage = clientMessage.substring(clientMessage.indexOf(' ') + 1);
                serverMessage = "\"" + userName + "\": " + clientMessage;

                if (messageReceiver.equals("everyone"))
                    chatServer.broadcastToEveryone(serverMessage, this);
                else if (chatServer.getUserNames().contains(messageReceiver))
                    chatServer.broadcastToUser(serverMessage, chatServer.getUserThread(messageReceiver));
                else
                    chatServer.broadcastToUser("The user you specified does not exist", this);
            }

            // removes the user after he said "bye" and closes the socket
            chatServer.removeUser(userName, this);
            socket.close();

            // tells everyone who has quit
            serverMessage = userName + " has quit";
            chatServer.broadcastToEveryone(serverMessage, this);

        } catch (IOException e) {
            System.out.println("UserThread exception " + e.getMessage());
        }
    }

    void printUsers() {
        if (!chatServer.hasUsers())
            writer.println("Connected users: " + chatServer.getUserNames());
        else
            writer.println("No other users connected");
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}
