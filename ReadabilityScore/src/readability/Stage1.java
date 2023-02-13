package readability;

import java.util.Scanner;

public class Stage1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        System.out.println(s.length() > 100 ? "HARD" : "EASY");
    }
}
