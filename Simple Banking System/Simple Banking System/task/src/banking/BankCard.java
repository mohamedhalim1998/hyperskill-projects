package banking;

import java.util.Objects;
import java.util.Random;

public class BankCard {
    private int id;
    private String number;
    private String pin;
    private int balance;

    public BankCard() {
    }

    public BankCard(int id, String number, String pin, int balance) {
        this.id = id;
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    public BankCard(String number, String pin) {
        this.number = number;
        this.pin = pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankCard bankCard = (BankCard) o;
        return Objects.equals(number, bankCard.number) && Objects.equals(pin, bankCard.pin);
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }


    public void deposit(int balance) {
        this.balance += balance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, pin);
    }


    @Override
    public String toString() {
        return "BankCard{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", pin='" + pin + '\'' +
                ", balance=" + balance +
                '}';
    }

    public static BankCard generateNewCard() {
        Random random = new Random();
        String num = "400000" + String.format("%09d", ((int) (random.nextDouble() * 1_000_000_000L)));
        int sum = 0;
        for (int i = 0; i < num.length(); i++) {
            int digit = Character.getNumericValue(num.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
            }
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
        }
        int checksum = (10 - (sum % 10));
        if (checksum == 10) {
            checksum = 0;
        }
        num += checksum;
        String pin = String.format("%04d", random.nextInt(10000));
        return new BankCard(num, pin);
    }

    public static boolean checkCardNumber(String num) {
        int sum = 0;
        for (int i = 0; i < num.length() - 1; i++) {
            int digit = Character.getNumericValue(num.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
            }
            if (digit > 9) {
                digit -= 9;
            }
            sum += digit;
        }
        sum += Character.getNumericValue(num.charAt(num.length() - 1));
        return sum % 10 == 0;
    }

    public void withdraw(int balance) {
        this.balance -= balance;
    }
}