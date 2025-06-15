package core;


import core.console.Console;
import core.console.Profiler;
import core.convoulter.Convoluter;
import org.bytedeco.opencv.global.opencv_imgcodecs;

import java.util.Scanner;

public class App {


    public static void main(String[] args) {
        System.out.println("Image convolution\nCopyright 2025, Alexei Dmitrievtsev\nSPDX-License-Identifier WTFPL");
        Console console = new Console();
        Convoluter convoluter = new Convoluter();
        Profiler profiler;

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
                        if (convoluter.getImage() != null && !(profiler.getType() != null & profiler.getThreadsCount() < 1)) {
                            convoluter.convolution(profiler.getFilter(), profiler.getParallelType(), profiler.getThreadsCount());
                        }
                        break;
                    case NEW:
                        convoluter.setImage(profiler.getImage());
                        break;
                    case STREAM:
                        Profiler finalProfiler = profiler;
                        new Thread(() -> convoluter.stream(finalProfiler.getPaths(), finalProfiler.getFilter(), finalProfiler.getThreadsCount())).start();
                        break;
                    case SAVE:
                        if (convoluter.getImage() != null) {
                            if (!opencv_imgcodecs.imwrite(profiler.getPaths()[0], convoluter.getImage())) {
                                System.out.println("Failed to save file");
                            } else {
                                System.out.println("File successfully saved");
                            }
                        }
                        break;
                }
            } catch (java.util.NoSuchElementException e) {
                System.out.println("No Such Element Found");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}