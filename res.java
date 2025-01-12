import java.util.Scanner;
public class res {
   public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);
        System.out.print("Введите формат для чтения : ");
        String formatRead = scanner.nextLine().trim().toLowerCase();
        System.out.print("Нужно расшифровывать: ");
        boolean encryptRead = scanner.nextLine().trim().equalsIgnoreCase("да");
        System.out.print("Нужно разархивировать : ");
        boolean zipRead = scanner.nextLine().trim().equalsIgnoreCase("да");
        readwrite.TypeofReadWrite(formatRead, "read", encryptRead,zipRead);
        System.out.print("Введите формат для записи: ");
        String formatWrite = scanner.nextLine().trim().toLowerCase();
        System.out.print("Нужно шифровать: ");
        boolean encryptWrite = scanner.nextLine().trim().equalsIgnoreCase("да");
        System.out.print("Нужно архивировать: ");
        boolean zipWrite = scanner.nextLine().trim().equalsIgnoreCase("да");
        readwrite.TypeofReadWrite(formatWrite, "write", encryptWrite,zipWrite);
        scanner.close();
}
}

