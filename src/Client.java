import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("1. 添加联系人\n2. 删除联系人\n3. 更新联系人\n4. 列出联系人\n0. 退出");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("姓名: ");
                        String name = scanner.nextLine();
                        System.out.print("住址: ");
                        String address = scanner.nextLine();
                        System.out.print("电话: ");
                        String phone = scanner.nextLine();
                        out.writeObject("ADD");
                        out.writeObject(new Contact(name, address, phone));
                        System.out.println(in.readObject());
                        break;
                    case 2:
                        System.out.print("输入要删除的联系人姓名: ");
                        String nameToRemove = scanner.nextLine();
                        out.writeObject("REMOVE");
                        out.writeObject(nameToRemove);
                        System.out.println(in.readObject());
                        break;
                    case 3:
                        System.out.print("输入更新的联系人姓名: ");
                        String updateName = scanner.nextLine();
                        System.out.print("住址: ");
                        String updateAddress = scanner.nextLine();
                        System.out.print("电话: ");
                        String updatePhone = scanner.nextLine();
                        out.writeObject("UPDATE");
                        out.writeObject(new Contact(updateName, updateAddress, updatePhone));
                        System.out.println(in.readObject());
                        break;
                    case 4:
                        out.writeObject("LIST");
                        List<Contact> contactList = (List<Contact>) in.readObject();
                        for (Contact contact : contactList) {
                            System.out.println("姓名: " + contact.getName() + ", 住址: " + contact.getAddress() + ", 电话: " + contact.getPhone());
                        }
                        break;
                    case 0:
                        System.out.println("退出程序");
                        return;
                    default:
                        System.out.println("无效选择");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}