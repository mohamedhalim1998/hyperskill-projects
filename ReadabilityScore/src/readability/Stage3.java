package readability;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Stage3 {
    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(new File(args[0]));
        String s = readFileAsString(args[0]);
        int sentences = s.split("[?.!]").length;
        int words = (int) s.chars().filter(c -> c == ' ').count() + 1;
        int chars = s.replaceAll(" ", "").length();
        System.out.println("The text is:\n" + s);
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + chars);
        double score = 4.71 * chars / words + .5 * words / sentences - 21.43;
        int sc = (int) (score * 100);

        score = sc / 100.0;
        System.out.printf("The score is: %.2f\n",score);
        switch ((int) Math.ceil(score)) {
            case 1:
                System.out.println("This text should be understood by 5-6 year-olds.");
                break;
            case 2:
                System.out.println("This text should be understood by 6-7 year-olds.");
                break;
            case 3:
                System.out.println("This text should be understood by 7-8 year-olds.");
                break;
            case 4:
                System.out.println("This text should be understood by 8-9 year-olds.");
                break;
            case 5:
                System.out.println("This text should be understood by 9-10 year-olds.");
                break;
            case 6:
                System.out.println("This text should be understood by 10-11 year-olds.");
                break;
            case 7:
                System.out.println("This text should be understood by 11-12 year-olds.");
                break;
            case 8:
                System.out.println("This text should be understood by 12-13 year-olds.");
                break;
            case 9:
                System.out.println("This text should be understood by 13-14 year-olds.");
                break;
            case 10:
                System.out.println("This text should be understood by 14-15 year-olds.");
                break;
            case 11:
                System.out.println("This text should be understood by 15-16 year-olds.");
                break;
            case 12:
                System.out.println("This text should be understood by 16-17 year-olds.");
                break;
            case 13:
                System.out.println("This text should be understood by 17-18 year-olds.");
                break;
            case 14:
                System.out.println("This text should be understood by 18-22 year-olds.");
                break;
        }


        /*

        The text is:
Readability is the ease with which a reader can understand a written text. In natural language, the readability of text depends on its content and its presentation. Researchers have used various factors to measure readability. Readability is more than simply legibility, which is a measure of how easily a reader can distinguish individual letters or characters from each other. Higher readability eases reading effort and speed for any reader, but it is especially important for those who do not have high reading comprehension. In readers with poor reading comprehension, raising the readability level of a text from mediocre to good can make the difference between success and failure

Words: 108
Sentences: 6
Characters: 580
The score is: 12.86
This text should be understood by 17-18 year-olds.
        * */
//        int count = 0;
//        for (String sentence : sentences) {
//
//            count += sentence.trim().split("\\s").length;
//        }
//        if (count * 1.0 / sentences.length > 10)
//            System.out.println("HARD");
//        else
//            System.out.println("EASY");
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
