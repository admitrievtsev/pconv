package core;


import core.console.Console;
import core.console.FilterType;
import core.console.ParallelType;
import core.console.Profiler;
import core.convoulter.Convoluter;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Benchmark {


    public static void main(String[] args) throws Exception {
        try {
            FileWriter writer = new FileWriter("src/main/result.csv");


            Mat Reference = opencv_imgcodecs.imread("src/main/resources/test_1.bmp", 0);
            ArrayList<Long> results = new ArrayList<Long>();
            Convoluter worker = new Convoluter();
            int repeat_times = 50; //define repeat number here
            FilterType filter = FilterType.GBLUR; //define Filter Type here
            ParallelType ptype = ParallelType.ROWS; //define Parallel Type here
            int threads = 128; //define threads number here
            for (int i = 0; i < repeat_times; i++) {
                worker.setImage(Reference);
                long time_start = System.currentTimeMillis();
                worker.convolution(filter, ptype, threads);
                long time_finish = System.currentTimeMillis() - time_start;
                writer.append(Long.toString(time_finish) + ",");
                System.out.println("Finished " + i);

            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}