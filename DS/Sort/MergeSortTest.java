package Sort;

import java.util.Arrays;
import java.util.Random;

public class MergeSortTest {
    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        // Test 1: Basic unsorted array
        int[] test1 = {5, 2, 8, 1, 9};
        int[] expected1 = {1, 2, 5, 8, 9};
        if (runTest(test1, expected1, "Basic unsorted array")) passed++;
        else failed++;

        // Test 2: Already sorted array
        int[] test2 = {1, 2, 3, 4, 5};
        int[] expected2 = {1, 2, 3, 4, 5};
        if (runTest(test2, expected2, "Already sorted array")) passed++;
        else failed++;

        // Test 3: Reverse sorted array
        int[] test3 = {5, 4, 3, 2, 1};
        int[] expected3 = {1, 2, 3, 4, 5};
        if (runTest(test3, expected3, "Reverse sorted array")) passed++;
        else failed++;

        // Test 4: Array with duplicates
        int[] test4 = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        int[] expected4 = {1, 1, 2, 3, 3, 4, 5, 5, 6, 9};
        if (runTest(test4, expected4, "Array with duplicates")) passed++;
        else failed++;

        // Test 5: All same elements
        int[] test5 = {7, 7, 7, 7, 7};
        int[] expected5 = {7, 7, 7, 7, 7};
        if (runTest(test5, expected5, "All same elements")) passed++;
        else failed++;

        // Test 6: Single element
        int[] test6 = {42};
        int[] expected6 = {42};
        if (runTest(test6, expected6, "Single element")) passed++;
        else failed++;

        // Test 7: Two elements
        int[] test7 = {2, 1};
        int[] expected7 = {1, 2};
        if (runTest(test7, expected7, "Two elements")) passed++;
        else failed++;

        // Test 8: Large array with random values
        int[] test8 = {15, 3, 9, 8, 5, 2, 7, 1, 6, 4, 10, 12, 11, 13, 14};
        int[] expected8 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        if (runTest(test8, expected8, "Large array")) passed++;
        else failed++;

        // Test 9: Array with negative numbers
        int[] test9 = {3, -1, 4, -5, 2, 0};
        int[] expected9 = {-5, -1, 0, 2, 3, 4};
        if (runTest(test9, expected9, "Array with negatives")) passed++;
        else failed++;

        // Test 10: Array with many duplicates
        int[] test10 = {5, 2, 5, 1, 5, 2, 5};
        int[] expected10 = {1, 2, 2, 5, 5, 5, 5};
        if (runTest(test10, expected10, "Many duplicates")) passed++;
        else failed++;

        // Test 11: Power of 2 size array
        int[] test11 = {8, 4, 2, 1, 7, 3, 6, 5};
        int[] expected11 = {1, 2, 3, 4, 5, 6, 7, 8};
        if (runTest(test11, expected11, "Power of 2 size (8 elements)")) passed++;
        else failed++;

        // Test 12: Non-power of 2 size array
        int[] test12 = {9, 7, 5, 3, 1};
        int[] expected12 = {1, 3, 5, 7, 9};
        if (runTest(test12, expected12, "Non-power of 2 size (5 elements)")) passed++;
        else failed++;

        // Test 13: Three elements
        int[] test13 = {3, 1, 2};
        int[] expected13 = {1, 2, 3};
        if (runTest(test13, expected13, "Three elements")) passed++;
        else failed++;

        System.out.println("\n========================================");
        System.out.println("RANDOM TESTS");
        System.out.println("========================================");

        // Random tests with different sizes
        if (runRandomTest(10, -100, 100, "Random 10 elements")) passed++;
        else failed++;

        if (runRandomTest(50, -1000, 1000, "Random 50 elements")) passed++;
        else failed++;

        if (runRandomTest(100, 0, 1000, "Random 100 elements")) passed++;
        else failed++;

        if (runRandomTest(1000, -5000, 5000, "Random 1000 elements")) passed++;
        else failed++;

        if (runRandomTest(127, -500, 500, "Random 127 elements (non-power of 2)")) passed++;
        else failed++;

        System.out.println("\n========================================");
        System.out.println("MERGE SORT TEST RESULTS");
        System.out.println("========================================");
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
        System.out.println("Total tests:  " + (passed + failed));
        System.out.println("========================================");
    }

    private static int[] generateRandomArray(int size, int min, int max) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(max - min + 1) + min;
        }
        return arr;
    }

    private static boolean runRandomTest(int size, int min, int max, String testName) {
        int[] input = generateRandomArray(size, min, max);
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        sort sorter = new sort(input);
        sorter.mergeSort();

        boolean result = Arrays.equals(input, expected);

        if (result) {
            System.out.println("✓ PASS: " + testName);
        } else {
            System.out.println("✗ FAIL: " + testName);
            if (size <= 20) {
                System.out.println("  Expected: " + Arrays.toString(expected));
                System.out.println("  Got:      " + Arrays.toString(input));
            } else {
                System.out.println("  First 10 expected: " + Arrays.toString(Arrays.copyOfRange(expected, 0, 10)));
                System.out.println("  First 10 got:      " + Arrays.toString(Arrays.copyOfRange(input, 0, 10)));
            }
        }

        return result;
    }

    private static boolean runTest(int[] input, int[] expected, String testName) {
        sort sorter = new sort(input);
        sorter.mergeSort();

        boolean result = Arrays.equals(input, expected);

        if (result) {
            System.out.println("✓ PASS: " + testName);
        } else {
            System.out.println("✗ FAIL: " + testName);
            System.out.println("  Expected: " + Arrays.toString(expected));
            System.out.println("  Got:      " + Arrays.toString(input));
        }

        return result;
    }
}
