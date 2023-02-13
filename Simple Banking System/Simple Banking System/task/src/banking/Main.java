package banking;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection conn;

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-fileName")) {
                initDatabase(args[i + 1]);
            }
        }
        while (true) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit\n");
            int num = scanner.nextInt();
            switch (num) {
                case 0:
                    System.out.println("Bye!");
                    conn.close();
                    return;
                case 1:
                    BankCard card = BankCard.generateNewCard();
                    insert(card);
                    System.out.printf("Your card has been created\n" +
                            "Your card number:\n" +
                            "%s\n" +
                            "Your card PIN:\n" +
                            "%s\n", card.getNumber(), card.getPin());
                    break;
                case 2:
                    if (startTransaction(scanner)) {
                        conn.close();
                        return;
                    }
                    break;
            }
        }
    }

    private static void insert(BankCard card) {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, card.getNumber());
            pstmt.setString(2, card.getPin());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void initDatabase(String name) {
        try {
            String url = "jdbc:sqlite:" + name;
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	number  text,\n"
                    + "	pin  text,\n"
                    + "	balance INTEGER DEFAULT 0\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean startTransaction(Scanner scanner) {
        System.out.println("Enter your card number: ");
        String num = scanner.next();
        System.out.println("Enter your PIN: ");
        String pin = scanner.next();
        BankCard card = getCard(num, pin);
        System.out.println(card);
        if (card == null) {
            System.out.println("Wrong card number or PIN!");
            return false;
        }
        System.out.println("You have successfully logged in!");
        while (true) {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            int choose = scanner.nextInt();
            switch (choose) {
                case 0:
                    System.out.println("Bye!");
                    return true;
                case 1:
                    System.out.println("Balance: " + card.getBalance());
                    break;
                case 2:
                    System.out.println("Enter income: ");
                    int balance = scanner.nextInt();
                    deposit(card, balance);
                    System.out.println("Income was added!");
                    break;
                case 3:
                    startTransfer(scanner, card);
                    break;
                case 4:
                    closeAccount(card);
                    return false;
                case 5:
                    return false;


            }

        }
    }

    private static void closeAccount(BankCard card) {
        String sql = "DELETE FROM card WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, card.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void startTransfer(Scanner scanner, BankCard from) {
        System.out.println("Transfer");
        System.out.println("Enter your card number: ");
        String num = scanner.next();
        if (!BankCard.checkCardNumber(num)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }
        BankCard to = getCard(num);
        if (to == null) {
            System.out.println("Such a card does not exist.");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int amount = scanner.nextInt();
        if (amount > from.getBalance()) {
            System.out.println("Not enough money!");
            return;
        }


        try {

            conn.setAutoCommit(false);
            withdraw(from, amount);
            deposit(to, amount);
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("Success!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private static BankCard getCard(String num) {
        String sql = String.format("SELECT * FROM card WHERE number = %s", num);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return new BankCard(rs.getInt("id"), rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void deposit(BankCard card, int balance) {
        card.deposit(balance);
        String sql = "UPDATE card SET balance = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, card.getBalance());
            pstmt.setInt(2, card.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void withdraw(BankCard card, int balance) {
        card.withdraw(balance);
        String sql = "UPDATE card SET balance = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, card.getBalance());
            pstmt.setInt(2, card.getId());
            pstmt.executeUpdate();
            System.out.println("Income was added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static BankCard getCard(String num, String pin) {
        String sql = String.format("SELECT * FROM card WHERE number = %s AND pin = %s", num, pin);
        System.out.println(sql);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return new BankCard(rs.getInt("id"), rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}