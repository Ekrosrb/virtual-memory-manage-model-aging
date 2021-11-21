package model;

import model.processor.Processor;

public class OS {

    private final Processor processor;

    private final int maxTime;
    private final int maxTable;

    OS(long physicalMemoryPages, int processMaxTime, int processMaxTable) {
        processor = new Processor(physicalMemoryPages);
        this.maxTime = processMaxTime;
        this.maxTable = processMaxTable;
    }

    public void simulate() {
        createRandomProcess();
        createRandomProcess();
        createRandomProcess();
        createRandomProcess();
        while (!processor.getProcessList().isEmpty()) {
            processor.execute();
        }
    }

    private void createRandomProcess() {
        int pages = Utils.rand(1, maxTable);
        int time = Utils.rand(1, maxTime);
        processor.addProcess(time, pages);
    }

    public static void main(String[] args) {
        OS os = new OS(4, 40, 2);
        os.simulate();
    }
}
