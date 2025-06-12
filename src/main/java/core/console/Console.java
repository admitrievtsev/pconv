package core.console;

import java.io.InputStream;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Console {
    private InputStream inputStream;
    private PrintStream printStream;
    private static ArrayDeque<String> commands = new ArrayDeque<String>();

    public Console() {
        this.inputStream = System.in;
        this.printStream = System.out;
    }

    public Console(InputStream inputStream, PrintStream printStream) {
        this.inputStream = inputStream;
        this.printStream = printStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }

    public static void addCommand(String message) {
        commands.addLast(message);
    }

    private void incorrectCommand(String message) {
        this.printStream.println("Incorrect command or flags " + message + ", try /help to see possible commands. ");
    }

    public void processCommands() {
        while (!commands.isEmpty()) {
            String[] command = commands.pop().split(" ");
            this.printStream.println("Command processing begins: " + command[0]);
            switch (command.length) {
                case 1:
                    switch (command[0]) {
                        case "/help":
                            this.printStream.println("TO DO");
                            break;
                        case "/exit":
                            System.exit(0);
                            break;
                        default:
                            incorrectCommand(Arrays.toString(command));
                            break;
                    }
                    break;
                case 2:
                    switch (command[0]) {
                        case "/load":
                            this.printStream.println("Loading file...");
                            Mat img = opencv_imgcodecs.imread("src/main/resources/test.jpg");
                            printStream.println(img);
                            break;
                        case "/convolution":
                            switch (command[1]) {

                            }
                            this.printStream.println("TO DO");
                            break;
                        default:
                            incorrectCommand(Arrays.toString(command));
                            break;
                    }
                    break;
                default:
                    incorrectCommand(Arrays.toString(command));
                    break;

            }
        }
    }

}
