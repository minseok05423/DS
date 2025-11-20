import java.util.*;
import java.io.*;

public class ComprehensiveReportTest {

    static class Statistics {
        int trials;
        long min, max;
        double mean, stddev;

        Statistics(long[] times) {
            this.trials = times.length;
            this.min = Long.MAX_VALUE;
            this.max = Long.MIN_VALUE;
            long sum = 0;

            for (long t : times) {
                if (t < min) min = t;
                if (t > max) max = t;
                sum += t;
            }

            this.mean = (double) sum / trials;

            double sumSquaredDiff = 0;
            for (long t : times) {
                double diff = t - mean;
                sumSquaredDiff += diff * diff;
            }
            this.stddev = Math.sqrt(sumSquaredDiff / trials);
        }

        String toTableCell() {
            if (mean == 0 && min == 0 && max == 0) return "<1";
            return String.format("%.1f", mean);
        }

        String toDetailString() {
            return String.format("%d회/최소:%dms/최대:%dms/평균:%.1fms/표준편차:%.1fms",
                trials, min, max, mean, stddev);
        }
    }

    public static void main(String[] args) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("report_data.txt"));

        writer.println("=".repeat(100));
        writer.println("정렬 알고리즘 성능 측정 실험 - 보고서용 데이터");
        writer.println("=".repeat(100));
        writer.println();

        // Test 1: Random data with varying sizes
        writer.println("■ 실험 1: 랜덤 데이터 크기별 성능 비교");
        writer.println("조건: 랜덤 데이터 (-100,000 ~ 100,000), 각 크기당 10회 시행");
        writer.println("-".repeat(100));
        writer.println();

        int[] sizes = {100, 500, 1000, 5000, 10000, 20000, 50000};

        // Table header
        writer.printf("%-15s", "알고리즘");
        for (int size : sizes) {
            writer.printf("%12s", "n=" + size);
        }
        writer.println();
        writer.println("-".repeat(100));

        // For each algorithm
        String[] algoNames = {"Bubble", "Insertion", "Heap", "Merge", "Quick", "Radix"};
        char[] algoCodes = {'B', 'I', 'H', 'M', 'Q', 'R'};

        for (int a = 0; a < algoNames.length; a++) {
            writer.printf("%-15s", algoNames[a]);

            for (int size : sizes) {
                long[] times = new long[10];
                for (int trial = 0; trial < 10; trial++) {
                    int[] data = generateRandom(size, -100000, 100000);
                    times[trial] = measureSort(data, algoCodes[a]);
                }

                Statistics stats = new Statistics(times);
                writer.printf("%12s", stats.toTableCell());
            }
            writer.println();
        }
        writer.println();
        writer.println("※ 단위: ms (밀리초, 평균값), <1은 측정 불가능 (0ms)");
        writer.println();

        // Test 2: Small digit numbers (Radix advantage)
        writer.println("\n■ 실험 2: 작은 자릿수 데이터 (Radix Sort 유리)");
        writer.println("조건: -999 ~ 999 범위 (1~3자리), 10회 시행");
        writer.println("-".repeat(100));
        writer.println();

        int[] smallDigitSizes = {1000, 5000, 10000, 20000, 50000};

        writer.printf("%-15s", "알고리즘");
        for (int size : smallDigitSizes) {
            writer.printf("%12s", "n=" + size);
        }
        writer.println();
        writer.println("-".repeat(100));

        for (int a = 0; a < algoNames.length; a++) {
            writer.printf("%-15s", algoNames[a]);

            for (int size : smallDigitSizes) {
                long[] times = new long[10];
                for (int trial = 0; trial < 10; trial++) {
                    int[] data = generateRandom(size, -999, 999);
                    times[trial] = measureSort(data, algoCodes[a]);
                }

                Statistics stats = new Statistics(times);
                writer.printf("%12s", stats.toTableCell());
            }
            writer.println();
        }
        writer.println();

        // Test 3: Nearly sorted data (Insertion advantage)
        writer.println("\n■ 실험 3: 거의 정렬된 데이터 (90% 정렬)");
        writer.println("조건: 90% 정렬 상태, 10회 시행");
        writer.println("-".repeat(100));
        writer.println();

        int[] nearlySortedSizes = {1000, 5000, 10000, 20000, 50000};

        writer.printf("%-15s", "알고리즘");
        for (int size : nearlySortedSizes) {
            writer.printf("%12s", "n=" + size);
        }
        writer.println();
        writer.println("-".repeat(100));

        for (int a = 0; a < algoNames.length; a++) {
            writer.printf("%-15s", algoNames[a]);

            for (int size : nearlySortedSizes) {
                long[] times = new long[10];
                for (int trial = 0; trial < 10; trial++) {
                    int[] data = generateNearlySorted(size, 0.9);
                    times[trial] = measureSort(data, algoCodes[a]);
                }

                Statistics stats = new Statistics(times);
                writer.printf("%12s", stats.toTableCell());
            }
            writer.println();
        }
        writer.println();

        // Test 4: Reverse sorted (Quick Sort worst case)
        writer.println("\n■ 실험 4: 역순 정렬 데이터 (Quick Sort 최악의 경우)");
        writer.println("조건: 완전 역순 정렬, 10회 시행");
        writer.println("-".repeat(100));
        writer.println();

        int[] reverseSizes = {1000, 5000, 10000};

        writer.printf("%-15s", "알고리즘");
        for (int size : reverseSizes) {
            writer.printf("%12s", "n=" + size);
        }
        writer.println();
        writer.println("-".repeat(100));

        char[] reverseAlgos = {'H', 'M', 'Q'};
        String[] reverseNames = {"Heap", "Merge", "Quick"};

        for (int a = 0; a < reverseAlgos.length; a++) {
            writer.printf("%-15s", reverseNames[a]);

            for (int size : reverseSizes) {
                long[] times = new long[10];
                for (int trial = 0; trial < 10; trial++) {
                    int[] data = generateReverse(size);
                    times[trial] = measureSort(data, reverseAlgos[a]);
                }

                Statistics stats = new Statistics(times);
                writer.printf("%12s", stats.toTableCell());
            }
            writer.println();
        }
        writer.println();
        writer.println("※ Quick Sort는 역순 데이터에서 O(n²) 성능을 보임");
        writer.println();

        // Test 5: Search algorithm performance
        writer.println("\n■ 실험 5: Search 알고리즘 성능 비교");
        writer.println("조건: 다양한 데이터 패턴, 10회 시행");
        writer.println("-".repeat(100));
        writer.println();

        String[] testNames = {"랜덤(n=50000)", "랜덤(n=10000)", "랜덤(n=2000)",
                              "작은자릿수(n=50000)", "작은자릿수(n=10000)", "작은자릿수(n=2000)",
                              "거의정렬(n=50000)", "거의정렬(n=10000)", "거의정렬(n=2000)",
                              "역순정렬(n=50000)", "역순정렬(n=10000)", "역순정렬(n=2000)"};
        Object[][] testConfigs = {
            {50000, -100000, 100000, "random"},
            {10000, -100000, 100000, "random"},
            {2000, -100000, 100000, "random"},
            {50000, -999, 999, "random"},
            {10000, -999, 999, "random"},
            {2000, -999, 999, "random"},
            {50000, 0, 50000, "sorted90"},
            {10000, 0, 10000, "sorted90"},
            {2000, 0, 2000, "sorted90"},
            {50000, 0, 50000, "reverse"},
            {10000, 0, 10000, "reverse"},
            {2000, 0, 2000, "reverse"}
        };

        writer.printf("%-15s %12s %12s %15s %12s\n", "데이터종류", "전체실행", "Search+선택", "선택된알고리즘", "성능향상");
        writer.println("-".repeat(100));

        for (int i = 0; i < testNames.length; i++) {
            int size = (int) testConfigs[i][0];
            int min = (int) testConfigs[i][1];
            int max = (int) testConfigs[i][2];
            String type = (String) testConfigs[i][3];

            long[] allAlgoTimes = new long[10];
            long[] searchTimes = new long[10];
            Map<Character, Integer> selections = new HashMap<>();

            for (int trial = 0; trial < 10; trial++) {
                int[] data;
                if (type.equals("random")) {
                    data = generateRandom(size, min, max);
                } else if (type.equals("sorted90")) {
                    data = generateNearlySorted(size, 0.9);
                } else {
                    data = generateReverse(size);
                }

                // Measure all algorithms (catch StackOverflow)
                long start = System.nanoTime();
                boolean hadStackOverflow = false;

                try {
                    if (size <= 1000) measureSort(data.clone(), 'B');
                    if (size <= 10000) measureSort(data.clone(), 'I');
                    measureSort(data.clone(), 'H');
                    measureSort(data.clone(), 'M');
                    measureSort(data.clone(), 'Q');
                    measureSort(data.clone(), 'R');
                } catch (StackOverflowError e) {
                    hadStackOverflow = true;
                }

                allAlgoTimes[trial] = hadStackOverflow ? -1 : (System.nanoTime() - start) / 1_000_000;

                // Measure Search + selected
                start = System.nanoTime();
                char choice = SortingTest.DoSearch(data.clone());
                long searchOnly = System.nanoTime() - start;
                long sortTime = measureSort(data.clone(), choice) * 1_000_000;
                searchTimes[trial] = (searchOnly + sortTime) / 1_000_000;

                selections.put(choice, selections.getOrDefault(choice, 0) + 1);
            }

            Statistics allStats = new Statistics(allAlgoTimes);
            Statistics searchStats = new Statistics(searchTimes);

            // Find most selected algorithm
            char mostSelected = ' ';
            int maxCount = 0;
            for (Map.Entry<Character, Integer> e : selections.entrySet()) {
                if (e.getValue() > maxCount) {
                    maxCount = e.getValue();
                    mostSelected = e.getKey();
                }
            }

            double speedup = allStats.mean / searchStats.mean;

            writer.printf("%-15s %10.1fms %10.1fms %15s %11.1f배\n",
                testNames[i], allStats.mean, searchStats.mean,
                String.format("%c(%d/10)", mostSelected, maxCount), speedup);
        }
        writer.println();

        // Detailed statistics for key experiments
        writer.println("\n■ 주요 실험 상세 통계");
        writer.println("-".repeat(100));
        writer.println();

        // Random large data
        writer.println("▶ 랜덤 데이터 (n=50,000):");
        long[] radixTimes = new long[10];
        long[] quickTimes = new long[10];
        for (int trial = 0; trial < 10; trial++) {
            int[] data = generateRandom(50000, -100000, 100000);
            radixTimes[trial] = measureSort(data.clone(), 'R');
            quickTimes[trial] = measureSort(data.clone(), 'Q');
        }
        writer.println("  Radix: " + new Statistics(radixTimes).toDetailString());
        writer.println("  Quick: " + new Statistics(quickTimes).toDetailString());
        writer.println();

        // Reverse data
        writer.println("▶ 역순 데이터 (n=10,000):");
        long[] quickRevTimes = new long[10];
        long[] mergeRevTimes = new long[10];
        for (int trial = 0; trial < 10; trial++) {
            int[] data = generateReverse(10000);
            quickRevTimes[trial] = measureSort(data.clone(), 'Q');
            mergeRevTimes[trial] = measureSort(data.clone(), 'M');
        }
        writer.println("  Quick: " + new Statistics(quickRevTimes).toDetailString());
        writer.println("  Merge: " + new Statistics(mergeRevTimes).toDetailString());
        writer.println();

        writer.close();
        System.out.println("실험 완료! 결과는 report_data.txt 에 저장되었습니다.");
    }

    private static int[] generateRandom(int size, int min, int max) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(max - min + 1) + min;
        }
        return arr;
    }

    private static int[] generateNearlySorted(int size, double ratio) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = i;

        int swaps = (int)((1 - ratio) * size);
        for (int i = 0; i < swaps; i++) {
            int idx1 = rand.nextInt(size);
            int idx2 = rand.nextInt(size);
            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }
        return arr;
    }

    private static int[] generateReverse(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }

    private static long measureSort(int[] data, char algo) {
        int[] testData = data.clone();

        // Warm-up (JIT compilation)
        for (int i = 0; i < 3; i++) {
            int[] temp = data.clone();
            switch (algo) {
                case 'B': SortingTest.DoBubbleSort(temp); break;
                case 'I': SortingTest.DoInsertionSort(temp); break;
                case 'H': SortingTest.DoHeapSort(temp); break;
                case 'M': SortingTest.DoMergeSort(temp); break;
                case 'Q': SortingTest.DoQuickSort(temp); break;
                case 'R': SortingTest.DoRadixSort(temp); break;
            }
        }

        // Actual measurement
        long start = System.nanoTime();

        switch (algo) {
            case 'B': SortingTest.DoBubbleSort(testData); break;
            case 'I': SortingTest.DoInsertionSort(testData); break;
            case 'H': SortingTest.DoHeapSort(testData); break;
            case 'M': SortingTest.DoMergeSort(testData); break;
            case 'Q': SortingTest.DoQuickSort(testData); break;
            case 'R': SortingTest.DoRadixSort(testData); break;
        }

        return (System.nanoTime() - start) / 1_000_000;
    }
}
