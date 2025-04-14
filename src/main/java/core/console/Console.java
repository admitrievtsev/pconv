package core.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.List;

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
    public void processCommands() {
        while (!commands.isEmpty()) {
            String[] command = commands.pop().split(" ");
            if (command.length >= 1) {
                this.printStream.println("Command processing begins: " + command[0]);
                switch (command[0]){
                    case "help":
                        this.printStream.println("TO DO");
                        break;
                    case "exit":
                        System.exit(0);
                        break;
                    case "convolution":
                        this.printStream.println("TO DO");
                        break;
                }
            }
        }
    }

}
