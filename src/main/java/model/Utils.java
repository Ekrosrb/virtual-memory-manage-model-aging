package model;

public class Utils {
    public static int rand(int min, int max){
        return (int) (Math.random() * (max - min) + min);
    }
}
