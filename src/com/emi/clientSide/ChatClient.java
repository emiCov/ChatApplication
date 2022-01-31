package com.emi.clientSide;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatClient {
    private static final int PORT = 6789;
    private static final String ADDRESS = "localhost";
    private Set<String> users = new HashSet<>();

    public void execute() {
        try (Socket socket = new Socket(ADDRESS, PORT)) {

            WriteThread output = new WriteThread(socket, this);
            ReadThread input = new ReadThread(socket, this);
            output.start();
            input.start();

        } catch (IOException e) {
            System.out.println("ChatClient error " + e.getMessage());
        }

    }

    void addUser(String newUser) {
        users.add(newUser);
    }

    void removeUser(String userToRemove) {
        users.remove(userToRemove);
    }

    public Set<String> getUsers() {
        return users;
    }
}
