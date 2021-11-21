package model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.memory.MemoryManager;

import static model.Log.LogType.MEMORY_MANAGER;
import static model.Log.LogType.OUT_TAG;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrintUtils {
    public static void printMemory(MemoryManager memoryManager) {
        final int size = 5;
        Log.info(MEMORY_MANAGER, "Memory status {");
        Log.info(OUT_TAG, "№    PPN  P    R    M    Aging");
        memoryManager.getVirtualMemory().getMemory().forEach(page -> {
            String sb = format(page.getPageNumber(), size) +
                    format(page.isPresent() ? page.getPhysicalPageNumber() : "X", size) +
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
