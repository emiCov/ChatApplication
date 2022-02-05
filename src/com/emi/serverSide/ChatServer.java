package com.emi.serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChatServer {

    private static final int PORT = 6789;
    private Map<String, UserThread> users = new HashMap<>();

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                newUser.start();
            }

        } catch (IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }

    boolean addUser(String userName, UserThread userThread) {
        if (users.containsKey(userName))
            return false;

        users.put(userName, userThread);
        return true;
    }

    void removeUser(String userName, UserThread aUser) {
        users.remove(userName);
        System.out.println("The user " + userName + " quit");
    }

    boolean hasUsers() {
        return users.isEmpty();
    }

    void broadcastToEveryone(String message, UserThread excludeUser) {
        for (UserThread user : users.values()) {
            if (user != excludeUser)
                user.sendMessage(message);
        }
    }

    void broadcastToUser(String message, UserThread user) {
        user.sendMessage(message);
    }

    Set<String> getUserNames() {
        return users.keySet();
    }

    public UserThread getUserThread(String userName) {
        return users.get(userName);
    }
}
