package com.emi.clientSide;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
    private final Socket socket;
    private final ChatClient chatClient;
    private Scanner scanner = new Scanner(System.in);
    private PrintWriter writer;

    public WriteThread (Socket socket, ChatClient chatClient) {
        this.socket = socket;
        this.chatClient = chatClient;
    }

    @Override
    public void run() {

        try (OutputStream output = socket.getOutputStream()) {
            writer = new PrintWriter(output, true);

            String message;

            System.out.println("Please enter your name: ");
            message = scanner.nextLine();
            writer.println(message);

            displaySendingRules();

            do {
                System.out.println("Please enter your message");
                message = scanner.nextLine();

                if (message.equalsIgnoreCase("userslist")) {
                    for (String user : chatClient.getUsers())
                        System.out.print(user + " ");
                } else {
                    writer.println(message);
                }

            } while (!message.equalsIgnoreCase("bye"));


        } catch (IOException e) {
            System.out.println("WriteThread exception " + e.getMessage());
        }

    }

    void displaySendingRules() {
        System.out.println("Rules for sending messages:");
        System.out.println("If you want to see the list of usernames, type: userslist");
        System.out.println("If you want to quit, type: bye");

        System.out.println("All the messages will begin with the symbol @, followed by:");
        System.out.println("\toption1: everyone - if you want your message to be sent to everyone");
        System.out.println("\toption1: username - if you want your message to be sent only to that person");
    }

}
