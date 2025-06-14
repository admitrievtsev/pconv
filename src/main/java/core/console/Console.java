package core.console;

import java.io.InputStream;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;

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
                            return new Profiler(ProfilerType.SAVE, command[1]);
                    }

                case 4:

                    switch ((command[0])) {
                        case "/convolution": {
                            int ThreadsCount;
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
                                ThreadsCount = Integer.parseInt(command[3]);
                            } catch (NumberFormatException e) {
                                printStream.println("Argument " + command[3] + " parse failed");
                                incorrectCommand(Arrays.toString(command));
                                break;
                            }
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
                            switch (command[1]) {
                                case "blur": {
                                    return new Profiler(ProfilerType.PROCESS, FilterType.BLUR, ParallelType, ThreadsCount);
                                }
                                case "gaussian_blur": {
                                    return new Profiler(ProfilerType.PROCESS, FilterType.GBLUR, ParallelType, ThreadsCount);
                                }
                                case "motion_blur": {
                                    return new Profiler(ProfilerType.PROCESS, FilterType.MBLUR, ParallelType, ThreadsCount);
                                }
                                case "find_edges": {
                                    return new Profiler(ProfilerType.PROCESS, FilterType.FEDGES, ParallelType, ThreadsCount);
                                }
                                case "sharpen": {
                                    return new Profiler(ProfilerType.PROCESS, FilterType.SHARP, ParallelType, ThreadsCount);
                                }
                                case "emboss": {
                                    return new Profiler(ProfilerType.PROCESS, FilterType.EMBOSS, ParallelType, ThreadsCount);
                                }
                                case "id": {
                                    return new Profiler(ProfilerType.PROCESS, FilterType.ID, ParallelType, ThreadsCount);
                                }
                            }
                        }

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
        return new Profiler(ProfilerType.EMPTY);
    }

}
