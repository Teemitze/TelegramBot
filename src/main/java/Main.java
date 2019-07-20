import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter link: ");
        CheckSite checkSite = new CheckSite(in.next().trim());
        checkSite.validateSite();
        in.close();
    }
}