package Sort;

public class sort {
    int A[];

    public sort(int B[]) {
        A = B;
    }

    public void quickSort() {
        qSort(0, A.length - 1);
    }

    private void qSort(int p, int r) {
        if (p < r) {
            int[] P = partition(p, r);
            int q = P[0];
            int t = P[1];

            qSort(p, q - 1);
            qSort(t + 1, r);            
        }
    }

    private int[] partition(int p, int r) {
        int pivot = A[r];
        int tmp;
        int i = p - 1;
        int dupeNum = 0;

        for (int j = p; j <= r - 1; j++) {
            if (A[j] < pivot) {
                i++;
                tmp = A[j];
                A[j] = A[i];
                A[i] = tmp;
            } else if (A[j] == pivot) {
                dupeNum++;
                tmp = A[i + dupeNum];
                A[i + dupeNum] = A[j];
                A[j] = tmp;
            }
        }

        A[r] = A[i + dupeNum + 1];
        A[i + dupeNum + 1] = pivot;

        return new int[]{i + 1, i + dupeNum + 1}; 
    }

    public void mergeSort() {
        int[] tmp = new int[A.length];
        for (int i = 0; i < A.length; i++) {
            tmp[i] = A[i];
        }
        mSort(tmp, A, 0, A.length - 1);
    }

    private void mSort(int[] src, int[] dest, int p, int r) {
        if (p < r) {
            int q = (p + r) / 2;
            mSort(dest, src, p, q);
            mSort(dest, src, q + 1, r);
            merge(src, dest, p, q, r);
        }
    }

    private void merge(int[] src, int[] dest, int p, int q, int r) {
        int i = p;
        int j = q + 1;
        int k = p;

        while (i <= q && j <= r) {
            if (src[i] <= src[j]) {
                dest[k] = src[i];
                i++;
            } else {
                dest[k] = src[j];
                j++;
            }
            k++;
        }

        while (i <= q) {
            dest[k] = src[i];
            i++;
            k++;
        }

        while (j <= r) {
            dest[k] = src[j];
            j++;
            k++;
        }
    }
}


