package core.console;

import java.io.InputStream;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;

import static core.console.ParallelType.*;

public class Console {
    private InputStream inputStream;
    private PrintStream printStream;

    private static final ArrayDeque<String> commands = new ArrayDeque<String>();

    public Console() {
        this.inputStream = System.in;
        this.printStream = System.out;
    }

    public Console(Mat Image, InputStream inputStream, PrintStream printStream) {
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

    public Profiler processCommands() {
        while (!commands.isEmpty()) {
            String[] command = commands.pop().split(" ");
            this.printStream.println("Command processing begins: " + command[0]);
            switch (command.length) {
                case 1:
                    switch (command[0]) {
                        case "/help":
                            this.printStream.println("Help list. Commands:\n" +
                                    "/exit - to exit program\n" +
                                    "/load [path] - to load image. Path cannot be null.\n" +
                                    "/save [path] - to save image. Path cannot be null.\n" +
                                    "/help - to call list of possible commands" +
                                    "/stream [filter type] [path1] [path2] ... - on-line processing of array of images" +
                                    "   [filter type] - type of filter applying to image. Possible variants:\n" +
                                    "/convolution [filter type] [parallel type] [amount of threads] - to make image convolution." +
                                    "   [filter type] - type of filter applying to image. Possible variants:\n" +
                                    "       - blur\n" +
                                    "       - gaussian_blur\n" +
                                    "       - motion_blur\n" +
                                    "       - find_edges\n" +
                                    "       - sharpen\n" +
                                    "       - emboss\n" +
                                    "       - id\n" +
                                    "   [parallel type] - type of parallel convolution execution, Possible variants:\n" +
                                    "       - 1(sequential)\n" +
                                    "       - 2(pixel)\n" +
                                    "       - 3(rows)\n" +
                                    "       - 4(cols)\n" +
                                    "   [amount of thread] - amount of threads. Any integer umber >1. Note: if you want to use 1(sequential) type of parallelism, make this option equal to any integer number" +
                                    "WARNING: YOU CANNOT MAKE CONVOLUTION WITHOUT LOADING IMAGE BEFORE IT");
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
                            Mat ReadedImage = opencv_imgcodecs.imread(command[1], 0);
                            if (ReadedImage.empty()) {
                                printStream.println("Couldn't find file with path " + command[1]);
                            } else {
                                printStream.println("File " + command[1] + "loaded successfully");
                                printStream.println(ReadedImage); //dedug to-remove
                                return new Profiler(ProfilerType.NEW, ReadedImage);
                            }
                            break;
                        case "/save":
                            this.printStream.println("Saving file...");
                            return new Profiler(ProfilerType.SAVE, new String[]{command[1]});
                    }

                case 4:
                    switch ((command[0])) {
                        case "/convolution": {
                            int threadsCount;
                            ParallelType ParallelType = null;
                            int arg;
                            try {
                                arg = Integer.parseInt(command[2]);
                                printStream.println("ARGUMENT 2 IS " + arg);

                            } catch (NumberFormatException e) {
                                printStream.println("Argument " + command[2] + " parse failed");
                                incorrectCommand(Arrays.toString(command));
                                break;
                            }
                            try {
                                threadsCount = Integer.parseInt(command[3]);
                            } catch (NumberFormatException e) {
                                printStream.println("Argument " + command[3] + " parse failed");
                                incorrectCommand(Arrays.toString(command));
                                break;
                            }
                            //У меня тут сломался свитч-кейс в джаве, поэтому так
                            if (arg == 2) {
                                ParallelType = PIXEL;
                            } else if (arg == 3) {
                                ParallelType = ROWS;
                            } else if (arg == 4) {
                                ParallelType = COLS;
                            } else if (arg == 5) {
                                ParallelType = FRGM;
                            } else {
                                incorrectCommand(Arrays.toString(command));
                                break;
                            }
                            return new Profiler(ProfilerType.PROCESS, decideFilter(command[1]), ParallelType, threadsCount);
                        }
                        case "/stream":
                            continue;
                        default:
                            incorrectCommand(Arrays.toString(command));
                            break;
                    }

                default:
                    if (command.length > 2 && Objects.equals(command[0], "/stream")) {
                        FilterType filter = decideFilter(command[1]);
                        if (filter != null) {
                            return new Profiler(ProfilerType.STREAM, filter, ROWS, ((int) (16) / (command.length - 2)) + 1);
                        }
                    }
                    incorrectCommand(Arrays.toString(command));
                    break;

            }
        }
        return new Profiler(ProfilerType.EMPTY);
    }

    private FilterType decideFilter(String fType) {
        switch (fType) {
            case "blur": {
                return FilterType.BLUR;
            }
            case "gaussian_blur": {
                return FilterType.GBLUR;
            }
            case "motion_blur": {
                return FilterType.MBLUR;
            }
            case "find_edges": {
                return FilterType.FEDGES;
            }
            case "sharpen": {
                return FilterType.SHARP;
            }
            case "emboss": {
                return FilterType.EMBOSS;
            }
            case "id": {
                return FilterType.ID;
            }
            default:
                return null;
        }
    }
}
