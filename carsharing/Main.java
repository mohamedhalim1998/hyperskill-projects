package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/";
    static Connection conn;
    static Scanner scanner;

    public static void main(String[] args) {
        boolean initializeDb = false;
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-databaseFileName")) {
                initDatabase(args[i + 1]);
                initializeDb = true;
                break;
            }
        }
        if (!initializeDb)
            initDatabase("someDB");
        scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Log in as a manager\n" +
                    "0. Exit");
            int x = scanner.nextInt();
            switch (x) {
                case 0:
                    return;
                case 1:
                    startMangerRoutine();
            }
        }

    }

    private static void startMangerRoutine() {

        while (true) {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");
            int x = scanner.nextInt();
            switch (x) {
                case 0:
                    return;
                case 1:
                    List<Company> list = getAllCompanies();
                    if (list.size() == 0) {
                        System.out.println("The company list is empty!");
                    } else {
                        System.out.println("Choose the company:");
                        list.forEach(System.out::println);
                        System.out.println("0. Back");
                        int index = scanner.nextInt();
                        if(index > 0)
                        startCompanyRoutine(list.get(index - 1));
                    }
                    break;
                case 2:
                    scanner.nextLine();
                    System.out.println("Enter the company name:");
                    String s = scanner.nextLine();
                    createCompany(s);
            }
        }

    }

    private static void startCompanyRoutine(Company company) {
        while (true) {
            System.out.println("'" + company.getName() + "' company:\n" +
                    "1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            int x = scanner.nextInt();
            switch (x) {
                case 0:
                    return;
                case 1:

                    List<Car> list = getAllCars(company.getId());
                    if (list.size() == 0) {
                        System.out.println("The car list is empty!");
                    } else {
                        System.out.println("Car list:");

                        for (int i = 0; i < list.size(); i++) {
                            System.out.println((i + 1) + ". " + list.get(i).name);
                        }
                    }
                    break;
                case 2:
                    scanner.nextLine(); //skip end line
                    System.out.println("Enter the car name:");
                    String s = scanner.nextLine();
                    createCor(s, company.getId());
            }
        }
    }

    private static void createCor(String name, int id) {
        String sql = "INSERT INTO car(name, company_id) VALUES(?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("The car was created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Car> getAllCars(int id) {
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = " + id;
        //System.out.println(sql);
        List<Car> list = new ArrayList<>();
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    private static void createCompany(String name) {
        String sql = "INSERT INTO company(name) VALUES(?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("The company was created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Company> getAllCompanies() {
        String sql = "SELECT * FROM COMPANY";
        //System.out.println(sql);
        List<Company> list = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Company(rs.getInt("ID"), rs.getString("NAME")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    private static void initDatabase(String name) {
        try {
            Class.forName(JDBC_DRIVER);
            String url = DB_URL + name;
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            String companyTable = "CREATE TABLE IF NOT EXISTS COMPANY (\n"
                    + "	ID INT PRIMARY KEY AUTO_INCREMENT,\n"
                    + "	NAME  VARCHAR UNIQUE NOT NULL \n"
                    + ");";
            String carTable = "CREATE TABLE IF NOT EXISTS CAR (\n"
                    + "	ID INT PRIMARY KEY AUTO_INCREMENT,\n"
                    + "	NAME  VARCHAR UNIQUE NOT NULL, \n"
                    + "	COMPANY_ID  INT NOT NULL, \n"
                    + " foreign key  (COMPANY_ID)  references company(ID)\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(companyTable);
            stmt.execute(carTable);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}