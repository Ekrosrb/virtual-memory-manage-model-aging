package model;

public class Utils {
    public static long rand(long min, long max){
        return (long) (Math.random() * (max - min) + min);
    }

    public static int rand(int min, int max){
        return (int) (Math.random() * (max - min) + min);
    }
}
