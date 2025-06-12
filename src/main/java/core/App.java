package core;


import core.console.Console;
import core.console.Profiler;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;

import java.util.Scanner;

public class App {
    private static Console console;
    private static Convoluter convoluter;


    public static void main(String[] args) {
        System.out.println("Image convolution\nCopyright 2025, Alexei Dmitrievtsev\nSPDX-License-Identifier WTFPL");
        console = new Console();
        convoluter = new Convoluter();
        Profiler profiler;
        //debug to-remove
        Mat img = opencv_imgcodecs.imread("src/main/resources/test.bmp", 0);
        System.out.println(img);

        Scanner scanner = new Scanner(console.getInputStream());

        while (true) {
            try {
                System.out.print("> ");
                String command = scanner.nextLine();
                Console.addCommand(command);
                profiler = console.processCommands();
                switch (profiler.getType()) {
                    case EMPTY:
                        break;
                    case PROCESS:
                        break;
                    case NEW:
                        convoluter.setImage(profiler.getImage());
                        break;
                    case SAVE:
                        if (!opencv_imgcodecs.imwrite(profiler.getPath(), convoluter.getImage())) {
                            System.out.println("Failed to save file");
                        } else {
                            System.out.println("File successfully saved");
                        }
                        break;
                }
            } catch (java.util.NoSuchElementException e) {
                System.out.println("No Such Element Found");
            }
        }

    }
}