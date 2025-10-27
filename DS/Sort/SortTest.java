package Sort;

import java.util.Arrays;

public class SortTest {
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

        System.out.println("\n========================================");
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
        System.out.println("========================================");
    }

    private static boolean runTest(int[] input, int[] expected, String testName) {
        sort sorter = new sort(input);
        sorter.quickSort();

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
