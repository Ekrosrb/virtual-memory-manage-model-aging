package model.processor;

import lombok.Getter;
import model.Log;
import model.PrintUtils;
import model.memory.MemoryManager;
import model.memory.pages.VirtualPage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.Log.LogType.*;

@Getter
public class Processor {

    private final MemoryManager memoryManager;

    private final Deque<Process> processList;
    private long id;
    private static final int QUANTUM = 4;

    public Processor(long physicalMemoryPages) {
        id = 0;
        processList = new LinkedList<>();
        memoryManager = new MemoryManager(physicalMemoryPages);
    }

    public void addProcess(int time, int pages) {
        Log.info(PROCESSOR, "Add process: " + time + "/" + pages + " time/pages");
        List<VirtualPage> virtualPages = Stream.generate(VirtualPage::new).limit(pages).collect(Collectors.toList());
        processList.addLast(new Process(id++, time, virtualPages));
        memoryManager.getVirtualMemory().addPages(virtualPages);
    }

    public void execute() {
        List<Process> finished = new ArrayList<>();

        processList.forEach(process -> {
            Log.info(PROCESSOR, "--------------------------------------");
            executeProcess(process);

            if (process.getTime() <= 0) {
                finished.add(process);
            }
            Log.info(PROCESSOR, "--------------------------------------");
            memoryManager.updateReference();
        });

        PrintUtils.printMemory(memoryManager);

        finishProcesses(finished);
    }

    private void executeProcess(Process process){
        for(int i = 0; i < QUANTUM && process.getTime() > 0; i++){
            process.decTime();
            VirtualPage page = process.getRandomPage();

            Log.info(PROCESSOR, "Execute process " + process.getId() +
                    ". Using " + page.getPageNumber() + " page.");

            memoryManager.execute(page, process.isModify());
        }
    }

    private void finishProcesses(List<Process> processes) {
        processes.forEach(process -> memoryManager.removePages(process.getPages()));
        processList.removeAll(processes);
        Log.info(PROCESSOR, "Finished " + processes.size() + " processes.");
    }



}
