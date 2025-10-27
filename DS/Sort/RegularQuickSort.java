package Sort;

public class RegularQuickSort {
    int A[];

    public RegularQuickSort(int B[]) {
        A = B;
    }

    public void quickSort() {
        qSort(0, A.length - 1);
    }

    private void qSort(int p, int r) {
        if (p < r) {
            int q = partition(p, r);
            qSort(p, q - 1);
            qSort(q + 1, r);
        }
    }

    private int partition(int p, int r) {
        int pivot = A[r];
        int tmp;
        int i = p - 1;

        for (int j = p; j <= r - 1; j++) {
            if (A[j] <= pivot) {
                i++;
                tmp = A[j];
                A[j] = A[i];
                A[i] = tmp;
            }
        }

        tmp = A[r];
        A[r] = A[i + 1];
        A[i + 1] = tmp;

        return i + 1;
    }
}
