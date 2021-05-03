package view;

import java.util.Scanner;

public class PrinterAndScanner {

    private static PrinterAndScanner printerAndScanner = null;
    private Scanner input;

    private PrinterAndScanner(){
        this.input = new Scanner(System.in);
    }

    public static PrinterAndScanner getInstance(){
        if (printerAndScanner == null)
            printerAndScanner = new PrinterAndScanner();
        return printerAndScanner;
    }

    public String scanNextLine(){
        return input.nextLine();
    }

    public void printNextLine(String string){
        System.out.println(string);
    }

    public void printString(StringBuilder string){
        System.out.println(string);
    }
}
