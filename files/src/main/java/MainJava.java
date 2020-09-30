import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainJava {

    public static void main(String[] args) {

        List<Integer> lista = Arrays.asList(1, 3, 4, 5, 3, 2, 6, 7, 8, 8, 3, 2, 4, 5, 6, 3, 2, 3, 4, 6,
                5, 4, 3, 2, 1, 1, 2, 3, 5, 7, 3, 6, 44, 2, 1, 3, 44, 55, 22, 11, 77, 88, 99, 55, 66, 33, 4, 3, 5, 6,
                1, 3, 4, 5, 3, 2, 6, 7, 8, 8, 3, 2, 4, 5, 6, 3, 2, 3, 4, 6,
                1, 3, 4, 5, 3, 2, 6, 7, 8, 8, 3, 2, 4, 5, 6, 3, 2, 3, 4, 6);
        System.out.println(bubbleSort(lista));
    }

    public static int[] bubbleSort(int[] array) {
        int counter = 0;
        int x;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 1 - counter; j++) {
                if (array[j] > array[j + 1]) {
                    x = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = x;
                }
            }
            counter++;
        }
        return array;
    }

    public static List<Integer> bubbleSort(List<Integer> list) {
        long l = System.nanoTime();
        Integer[] rtnArr = new Integer[list.size()];
        list.toArray(rtnArr);
        int counter = 0;
        int x;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - 1 - counter; j++) {
                if (rtnArr[j] > rtnArr[j + 1]) {
                    x = rtnArr[j];
                    rtnArr[j] = rtnArr[j + 1];
                    rtnArr[j + 1] = x;
                }
            }
            counter++;
        }
        System.out.printf("czas w miliSec: %s", System.nanoTime() - l);
        System.out.println("");
        return new ArrayList<>(Arrays.asList(rtnArr));
    }
}
