package model.processor;

import lombok.Getter;
import model.Log;
import model.OS;
import model.PrintUtils;
import model.Utils;
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
    public static int tact = 0;
    private final int quantum;

    public Processor(long physicalMemoryPages, int quantum) {
        id = 0;
        this.quantum = quantum;
        processList = new LinkedList<>();
        memoryManager = new MemoryManager(physicalMemoryPages);
    }

    public void addProcess(int time, int pages) {
        String log = "Add process " + id + "  " + time + "/" + pages + " time/pages";
        Log.info(PROCESSOR, log);
        PrintUtils.saveMessageInFile(log);
        List<VirtualPage> virtualPages = Stream.generate(VirtualPage::new).limit(pages).collect(Collectors.toList());
        processList.addLast(new Process(id++, time, virtualPages));
        memoryManager.getVirtualMemory().addPages(virtualPages);

        OS.COUNTER.incProcessCount();
        OS.COUNTER.setMaxVirtualMemorySize(memoryManager.getVirtualMemory().getMemory().size());

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

        Log.info(PROCESSOR, "Execute process " + process.getId());

        for(int i = 0; i < quantum && process.getTime() > 0; i++, Processor.tact++){
            process.decTime();
                VirtualPage page = process.getRandomPage();
                Log.info(PROCESS, "Using " + page.getPageNumber() +
                        " page. Active pages - " + process.getActive().size());
                memoryManager.execute(page, process.isModify());
        }
    }

    private void finishProcesses(List<Process> processes) {
        if(processes.isEmpty()){
            return;
        }
        processes.forEach(process -> {
            memoryManager.removePages(process.getPages());
            String log = "Finish process " + process.getId();
            Log.info(PROCESSOR, log);
            PrintUtils.saveMessageInFile(log);
        });
        processList.removeAll(processes);
    }



}
