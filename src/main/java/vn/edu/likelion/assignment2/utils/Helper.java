package vn.edu.likelion.assignment2.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

public class Helper {
    private static Scanner scanner = new Scanner(System.in);

    public static String encodingStringBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public static int getUserInput() throws NumberFormatException {
        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid input, please enter again !");
        }

        return choice;
    }

    public static Sheet getSheet (String nameFile) throws IOException {
        // get file excel
        FileInputStream fis = new FileInputStream(new File(nameFile));

        // create workbook
        Workbook workbook = new XSSFWorkbook(fis);

        // lấy cái sheet ở trong workbook trên

        return workbook.getSheetAt(0);
    }

}
