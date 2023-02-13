package readability;

import java.util.Scanner;

public class Stage2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        String[] sentences = s.split("[?.!]");
        int count = 0;
        for (String sentence : sentences) {

            count += sentence.trim().split("\\s").length;
        }
        if (count *1.0 / sentences.length > 10)
            System.out.println("HARD");
        else
            System.out.println("EASY");
    }
}
