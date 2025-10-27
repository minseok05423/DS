package Sort;

import java.util.Random;
import java.util.Arrays;

public class PerformanceTest {
    public static void main(String[] args) {
        int[] sizes = {100000, 1000000, 10000000};
        Random rand = new Random(42); // Fixed seed for reproducibility

        System.out.println("QuickSort Performance Comparison");
        System.out.println("=================================\n");

        for (int size : sizes) {
            System.out.println("Array size: " + size);

            // Generate random array
            int[] arr1 = generateRandomArray(size, rand);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);

            // Test regular quicksort
            RegularQuickSort regular = new RegularQuickSort(arr1);
            long start = System.nanoTime();
            regular.quickSort();
            long end = System.nanoTime();
            double regularTime = (end - start) / 1_000_000_000.0;

            // Test duplicate-aware quicksort
            sort dupeAware = new sort(arr2);
            start = System.nanoTime();
            dupeAware.quickSort();
            end = System.nanoTime();
            double dupeAwareTime = (end - start) / 1_000_000_000.0;

            System.out.printf("  Regular QuickSort:        %.3f seconds\n", regularTime);
            System.out.printf("  Duplicate-Aware QuickSort: %.3f seconds\n", dupeAwareTime);
            System.out.printf("  Speedup: %.2fx\n\n", regularTime / dupeAwareTime);
        }

        // Test with arrays containing many duplicates
        System.out.println("\n=================================");
        System.out.println("High Duplicate Scenario (50% duplicates)");
        System.out.println("=================================\n");

        for (int size : sizes) {
            System.out.println("Array size: " + size);

            // Generate array with many duplicates
            int[] arr1 = generateDuplicateArray(size, rand, 100);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);

            // Test regular quicksort
            RegularQuickSort regular = new RegularQuickSort(arr1);
            long start = System.nanoTime();
            regular.quickSort();
            long end = System.nanoTime();
            double regularTime = (end - start) / 1_000_000_000.0;

            // Test duplicate-aware quicksort
            sort dupeAware = new sort(arr2);
            start = System.nanoTime();
            dupeAware.quickSort();
            end = System.nanoTime();
            double dupeAwareTime = (end - start) / 1_000_000_000.0;

            System.out.printf("  Regular QuickSort:        %.3f seconds\n", regularTime);
            System.out.printf("  Duplicate-Aware QuickSort: %.3f seconds\n", dupeAwareTime);
            System.out.printf("  Speedup: %.2fx\n\n", regularTime / dupeAwareTime);
        }
    }

    private static int[] generateRandomArray(int size, Random rand) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size);
        }
        return arr;
    }

    private static int[] generateDuplicateArray(int size, Random rand, int uniqueValues) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(uniqueValues);
        }
        return arr;
    }
}
