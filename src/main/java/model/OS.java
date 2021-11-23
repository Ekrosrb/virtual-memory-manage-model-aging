package model;

import lombok.Builder;
import lombok.Data;
import model.processor.Processor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OS {

    private final Processor processor;

    public static final Counter COUNTER = new Counter();

    private final int maxTime;
    private final int maxTable;
    private final int count;

    OS(long physicalMemoryPages, int processMaxTime, int processMaxTable, int processCount, int quantum) {
        processor = new Processor(physicalMemoryPages, quantum);
        this.maxTime = processMaxTime;
        this.maxTable = processMaxTable;
        this.count = processCount;
    }

    public void simulate() {

        List<ProcessInfo> processInfoList = Stream.iterate(0, x -> x + 1).limit(count)
                .map(x -> createRandomProcess()).collect(Collectors.toList());

        while (!processInfoList.isEmpty() || !processor.getProcessList().isEmpty()) {

            if(!processInfoList.isEmpty() && Utils.rand(0, 5) != 0){
                ProcessInfo processInfo = processInfoList.remove(0);
                processor.addProcess(processInfo.getTime(), processInfo.getPages());
            }

            processor.execute();
        }

        COUNTER.printCounter();
    }

    private ProcessInfo createRandomProcess() {
        int pages = Utils.rand(1, maxTable);
        int time = Utils.rand(1, maxTime);
        return ProcessInfo.builder().pages(pages).time(time).build();
    }

    @Data
    @Builder
    private static class ProcessInfo{
        private int time;
        private int pages;
    }

    public static void main(String[] args) {
        OS os = new OS(8, 100, 4, 100, 2);
        os.simulate();
    }
}
