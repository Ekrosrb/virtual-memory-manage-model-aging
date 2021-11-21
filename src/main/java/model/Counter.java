package model;

public class Counter {
    private long pageFaultCount;
    private long processCount;
    private long maxVirtualMemorySize;
    private long writeToFSCount;

    public Counter(){

    }

    public void incPageFault(){
        pageFaultCount++;
    }


    public void incProcessCount(){
        processCount++;
    }

    public void incWriteToFSCount(){
        writeToFSCount++;
    }

    public void setMaxVirtualMemorySize(int size){
        if(maxVirtualMemorySize < size){
            maxVirtualMemorySize = size;
        }
    }

    public void printCounter(){
        System.out.println(PrintUtils.format("Page fault: ", 26) + pageFaultCount);
        System.out.println(PrintUtils.format("Processes: ", 26) + processCount);
        System.out.println(PrintUtils.format("Write to FS: ", 26) + writeToFSCount);
        System.out.println(PrintUtils.format("Max virtual memory size:", 26) + maxVirtualMemorySize);
    }
}
