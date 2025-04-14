package core;


import core.console.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class App {
    public static Console console;
    public static void main(String[] args) {
        System.out.println("Image convolution\nCopyright 2025, Alexei Dmitrievtsev\nSPDX-License-Identifier WTFPL");
        console = new Console();
        Scanner scanner = new Scanner(console.getInputStream());

        while (true){
            try {
                System.out.print("> ");
                String command = scanner.nextLine();
                Console.addCommand(command);
                console.processCommands();
            }
            catch (java.util.NoSuchElementException e){
                System.out.println("No Such Element Found");
            }
        }

    }
}