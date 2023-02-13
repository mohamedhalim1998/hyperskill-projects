package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Stage4 {
    public static void main(String[] args) throws IOException {
        //countSyllable("simple");
        String s = readFileAsString(args[0]);
        int sentences = s.split("[?.!]").length;
        int words = s.split(" ").length;
        int syllable = Arrays.stream(s.split(" ")).mapToInt(Stage4::countSyllable).sum();
        int polySyllable = (int) Arrays.stream(s.split(" ")).map(Stage4::countSyllable).filter(i -> i > 2).count();
        int chars = s.replaceAll(" ", "").length();
        System.out.println("The text is:\n" + s);
        System.out.println("Words: " + words);
        System.out.println("Characters: " + chars);
        System.out.println("Sentences: " + sentences);
        System.out.println("Syllables: " + syllable);
        System.out.println("Polysyllables: " + polySyllable);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        String method = scanner.next();
        double ariScore = ARI(sentences, words, chars);
        double fkScore = FK(sentences, words, syllable);
        double smogScore = SMOG(sentences, polySyllable);
        double clScore = CL(sentences, words, chars);
        int ariAge = ARI(ariScore);
        int fkAge = FK(fkScore);
        int smogAge = SMOG(smogScore);
        int clAge = CL(clScore);
        System.out.println();
        switch (method) {
            case "ARI" -> System.out.printf("Automated Readability Index: %s (about %d-year-olds).\n", formatFloat(ariScore), ariAge);
            case "FK" -> System.out.printf("Flesch–Kincaid readability tests: %s (about %d-year-olds)\n", formatFloat(fkScore), fkAge);
            case "SMOG" -> System.out.printf("Simple Measure of Gobbledygook: %s (about %d-year-olds).\n", formatFloat(smogScore), smogAge);
            case "CL" -> System.out.printf("Coleman–Liau index: %s (about %d-year-olds).\n", formatFloat(clScore), clAge);
            case "all" -> {
                System.out.printf("Automated Readability Index: %s (about %d-year-olds).\n", formatFloat(ariScore), ariAge);
                System.out.printf("Flesch–Kincaid readability tests: %s (about %d-year-olds)\n", formatFloat(fkScore), fkAge);
                System.out.printf("Simple Measure of Gobbledygook: %s (about %d-year-olds).\n", formatFloat(smogScore), smogAge);
                System.out.printf("Coleman–Liau index: %s (about %d-year-olds).\n", formatFloat(clScore), clAge);
            }
        }
        System.out.printf("\n\nThis text should be understood in average by %s-year-olds.", formatFloat(
                (ariAge + fkAge + smogAge + clAge) / 4.0
        ));


    }

    private static int CL(double clScore) {
        return ARI(clScore + 1);
    }

    private static int SMOG(double smogScore) {
        return ARI(smogScore);
    }

    private static int FK(double fkScore) {
        return ARI(fkScore);
    }

    private static int ARI(double ariScore) {
        ariScore = Math.ceil(ariScore);
        if (ariScore == 14)
            return 22;
        return (int) (ariScore + 5);
    }

    private static double CL(int sentences, int words, int chars) {
        double l = chars * 100.0 / words;
        double s = sentences * 100.0 / words;
        return 0.0588 * l - 0.296 * s - 15.8;
    }

    private static double SMOG(int sentences, int polySyllable) {
        return 1.043 * Math.sqrt(polySyllable * 30.0 / sentences) + 3.1291;
    }

    private static double FK(int sentences, int words, int syllable) {
        return .39 * words / sentences + 11.8 * syllable / words - 15.59;
    }

    private static double ARI(int sentences, int words, int chars) {
        return 4.71 * chars / words + .5 * words / sentences - 21.43;
    }

    private static String formatFloat(double score) {
        int sc = (int) (score * 100);
        score = sc / 100.0;
        return String.format("%.2f", score);
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static int countSyllable(String word) {
        word = word.toLowerCase();
        if (!Character.isAlphabetic(word.charAt(word.length() - 1))) {
            word = word.substring(0, word.length() - 1);
        }
        boolean prevIsVowel = false;
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if ("aeiouy".indexOf(word.charAt(i)) != -1) {
                if (prevIsVowel || (i == word.length() - 1 && word.charAt(i) == 'e')) {
                    continue;
                }
                prevIsVowel = true;
                count++;
            } else {
                prevIsVowel = false;
            }
        }
       // System.out.println(word + ": " + Math.max(1, count));

        return Math.max(1, count);
    }
}
