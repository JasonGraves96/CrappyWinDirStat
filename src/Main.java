//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the directory you want to read");
        Scanner scnr = new Scanner(System.in);
        String path = scnr.nextLine();
        Folder userFold = new Folder();

            try {
                userFold.read(Path.of(path));
                System.out.println(userFold.toString());
            } catch (IOException e) {
                System.out.println("Not a valid folder");
            }

    }
}