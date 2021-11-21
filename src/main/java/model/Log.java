package model;

public class Log {

    public enum LogType{
        PROCESSOR, PROCESS, VIRTUAL_MEMORY, PHYSICAL_MEMORY, MEMORY_MANAGER, BACKGROUND_PROCESS, OUT_TAG, AGING
    }

    public static void info(LogType logType, String info){
        if(logType == LogType.OUT_TAG){
            System.out.println("    " + info);
        }else {
            System.out.println(PrintUtils.format("[" + logType.name() + "]", 25) + info);
        }
    }
}
