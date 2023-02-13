import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("number.txt"));
        int count = 0;
        while (scanner.hasNext()) {
            int x = scanner.nextInt();
            if(x == 0) {
                System.out.println(count);
                return;
            } else if(x % 2 == 0) {
                count++;
            }

        }
        System.out.println(count);

    }
}
