import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, Contact> contacts = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器启动，等待客户端连接...");
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                String command;
                while ((command = (String) in.readObject()) != null) {
                    switch (command) {
                        case "ADD":
                            Contact contactToAdd = (Contact) in.readObject();
                            contacts.put(contactToAdd.getName(), contactToAdd);
                            out.writeObject("Contact added.");
                            break;
                        case "REMOVE":
                            String nameToRemove = (String) in.readObject();
                            contacts.remove(nameToRemove);
                            out.writeObject("Contact removed.");
                            break;
                        case "UPDATE":
                            Contact contactToUpdate = (Contact) in.readObject();
                            contacts.put(contactToUpdate.getName(), contactToUpdate);
                            out.writeObject("Contact updated.");
                            break;
                        case "LIST":
                            out.writeObject(new ArrayList<>(contacts.values()));
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

class Contact implements Serializable {
    private String name;
    private String address;
    private String phone;

    public Contact(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}