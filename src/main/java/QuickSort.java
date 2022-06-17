import java.util.ArrayList;

public class QuickSort {

    private QuickSort() {

    }

    private static <T extends Comparable<? super T>> int partition(
            ArrayList<T> array, int begin, int end) {

        T pivot = array.get(end);
        int counter = begin-1;
        for (int i = begin; i < end; i++) {
            if (array.get(i).compareTo(pivot) <= 0) {
                counter++;
                swap(i, counter, array);
            }
        }
        counter++;
        swap(end, counter, array);
        return counter;
    }

    private static <T extends Comparable<? super T>> void swap (
            int i, int counter, ArrayList<T> array) {

        T temp = array.get(i);
        array.set(i, array.get(counter));
        array.set(counter, temp);
    }

    private static <T extends Comparable<? super T>> void quickSort(
            ArrayList<T> array, int begin, int end) {

        if (end <= begin) {
            return;
        }

            int pivot = partition(array, begin, end);
            quickSort(array, begin, pivot-1);
            quickSort(array, pivot + 1, end);
    }

    public static <T extends Comparable<? super T>> void sort(ArrayList<T> array) {
        quickSort(array, 0, array.size()-1);
    }
}
