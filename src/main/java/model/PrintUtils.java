package model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import model.memory.MemoryManager;
import model.memory.pages.VirtualPage;
import model.processor.Processor;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static model.Log.LogType.MEMORY_MANAGER;
import static model.Log.LogType.OUT_TAG;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrintUtils {
    private static boolean IS_FIRST = true;
    public static void saveMessageInFile(String message){
        writeInFile(message + "\n");
    }

    public static void saveMemoryStatusInFile(MemoryManager memoryManager){
        final int size = 5;

        StringBuilder sb = new StringBuilder("\n-- TACT ").append(Processor.tact)
                .append(" --\n")
                .append("\nMemory status, phys page free: " + memoryManager.getFree().size() +  " {\n\n№    PPN  P    R    M    Aging\n");

        for (VirtualPage page : memoryManager.getVirtualMemory().getMemory()) {
            sb.append(format(page.getPageNumber(), size))
                    .append(format(page.isPresent() ? page.getPhysicalPageNumber() : page.isInFs() ? "FS" : "X", size))
                    .append(format(page.isPresent(), size))
                    .append(format(page.isReference(), size))
                    .append(format(page.isModify(), size))
                    .append(page.isPresent() ? formatBinary16(Long.toBinaryString(page.getPriority())) : "X")
                    .append("\n");
        }
        sb.append("}\n\n");
        writeInFile(sb.toString());
    }

    @SneakyThrows
    private static void writeInFile(String log){
        File file = new File("lastLog.txt");
        if(IS_FIRST) {
            Files.delete(Path.of(file.getPath()));
            IS_FIRST = false;
            Files.createFile(Path.of(file.getPath()));
        }
        try(FileOutputStream fos = new FileOutputStream(file, true)){
            fos.write(log.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }
    }

    public static void printMemory(MemoryManager memoryManager) {
        final int size = 5;
        Log.info(MEMORY_MANAGER, "Memory status {");
        Log.info(OUT_TAG, "№    PPN  P    R    M    Aging");
        memoryManager.getVirtualMemory().getMemory().forEach(page -> {
            String sb = format(page.getPageNumber(), size) +
                    format(page.isPresent() ? page.getPhysicalPageNumber() : page.isInFs() ? "FS" : "X", size) +
                    format(page.isPresent(), size) +
                    format(page.isReference(), size) +
                    format(page.isModify(), size) +
                    (page.isPresent() ? formatBinary16(Long.toBinaryString(page.getPriority())) : "X");
            Log.info(OUT_TAG, sb);
        });
        Log.info(OUT_TAG, "}");
    }

    public static String format(Object value, int size){
        String str = value.toString();
        if(str.equals("true")){
            str = "1";
        }
        if(str.equals("false")){
            str = "0";
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < size){
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String formatBinary16(String str){

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < 16){
            sb.insert(0, "0");
        }

        return sb.toString();
    }
}
