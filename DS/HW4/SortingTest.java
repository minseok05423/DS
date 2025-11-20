import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.
                char algo = ' ';

				if (args.length == 4) {
                    return;
                }

				String command = args.length > 0 ? args[0] : br.readLine();

				if (args.length > 0) {
                    args = new String[4];
                }
				
				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'S':	// Search
						algo = DoSearch(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    if (command.charAt(0) != 'S') {
                        for (int i = 0; i < newvalue.length; i++) {
                            System.out.println(newvalue[i]);
                        }
                    } else {
                        System.out.println(algo);
                    }
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Bubble Sort: 인접한 두 원소를 비교하여 큰 값을 뒤로 보내는 정렬 (O(n²))
	public static int[] DoBubbleSort(int[] value)
	{
		int n = value.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (value[j] > value[j + 1]) {
					// 인접 원소 교환
					int temp = value[j];
					value[j] = value[j + 1];
					value[j + 1] = temp;
				}
			}
		}
		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Insertion Sort: 각 원소를 정렬된 부분에 삽입하는 정렬 (O(n²), 거의 정렬된 데이터에 효율적)
	public static int[] DoInsertionSort(int[] value)
	{
		int n = value.length;
		for (int i = 1; i < n; i++) {
			int key = value[i];  // 삽입할 원소
			int j = i - 1;
			// key보다 큰 원소들을 오른쪽으로 이동
			while (j >= 0 && value[j] > key) {
				value[j + 1] = value[j];
				j--;
			}
			value[j + 1] = key;  // key를 올바른 위치에 삽입
		}
		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Heap Sort: 최대 힙을 구성하여 최댓값을 추출하는 정렬 (O(n log n))
	public static int[] DoHeapSort(int[] value)
	{
		int n = value.length;

		// 최대 힙 구성 (상향식 heapify)
		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(value, n, i);
		}

		// 힙에서 원소를 하나씩 추출하여 정렬
		for (int i = n - 1; i > 0; i--) {
			// 루트(최댓값)를 배열 끝으로 이동
			int temp = value[0];
			value[0] = value[i];
			value[i] = temp;

			// 축소된 힙에 대해 heapify 수행
			heapify(value, i, 0);
		}

		return value;
	}

	// 힙 속성 유지 (부모 노드가 자식 노드보다 큼)
	private static void heapify(int[] arr, int n, int i)
	{
		int largest = i;        // 현재 노드
		int left = 2 * i + 1;   // 왼쪽 자식
		int right = 2 * i + 2;  // 오른쪽 자식

		// 왼쪽 자식이 현재 노드보다 크면
		if (left < n && arr[left] > arr[largest]) {
			largest = left;
		}

		// 오른쪽 자식이 가장 큰 노드보다 크면
		if (right < n && arr[right] > arr[largest]) {
			largest = right;
		}

		// 가장 큰 노드가 현재 노드가 아니면 교환 후 재귀 호출
		if (largest != i) {
			int swap = arr[i];
			arr[i] = arr[largest];
			arr[largest] = swap;

			heapify(arr, n, largest);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Merge Sort: 분할 정복 방식으로 배열을 나누고 병합하는 정렬 (O(n log n), 안정적)
	public static int[] DoMergeSort(int[] value)
	{
		if (value.length <= 1) {
			return value;
		}
		mergeSortHelper(value, 0, value.length - 1);
		return value;
	}

	// 배열을 재귀적으로 분할하고 병합
	private static void mergeSortHelper(int[] arr, int left, int right)
	{
		if (left < right) {
			int mid = left + (right - left) / 2;

			// 왼쪽 절반 정렬
			mergeSortHelper(arr, left, mid);
			// 오른쪽 절반 정렬
			mergeSortHelper(arr, mid + 1, right);

			// 정렬된 두 부분 병합
			merge(arr, left, mid, right);
		}
	}

	// 두 정렬된 부분 배열을 하나로 병합
	private static void merge(int[] arr, int left, int mid, int right)
	{
		int n1 = mid - left + 1;  // 왼쪽 배열 크기
		int n2 = right - mid;     // 오른쪽 배열 크기

		// 임시 배열 생성
		int[] leftArr = new int[n1];
		int[] rightArr = new int[n2];

		// 데이터 복사
		for (int i = 0; i < n1; i++) {
			leftArr[i] = arr[left + i];
		}
		for (int j = 0; j < n2; j++) {
			rightArr[j] = arr[mid + 1 + j];
		}

		// 병합 과정
		int i = 0, j = 0, k = left;

		// 두 배열을 비교하며 작은 값을 먼저 배치
		while (i < n1 && j < n2) {
			if (leftArr[i] <= rightArr[j]) {
				arr[k] = leftArr[i];
				i++;
			} else {
				arr[k] = rightArr[j];
				j++;
			}
			k++;
		}

		// 남은 원소 복사
		while (i < n1) {
			arr[k] = leftArr[i];
			i++;
			k++;
		}

		while (j < n2) {
			arr[k] = rightArr[j];
			j++;
			k++;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Quick Sort: 피벗을 기준으로 분할 정복하는 정렬 (평균 O(n log n), 최악 O(n²))
	public static int[] DoQuickSort(int[] value)
	{
		if (value.length <= 1) {
			return value;
		}
		quickSortHelper(value, 0, value.length - 1);
		return value;
	}

	// 배열을 재귀적으로 분할하고 정렬
	private static void quickSortHelper(int[] arr, int low, int high)
	{
		if (low < high) {
			// 피벗을 기준으로 분할하고 피벗 위치 반환
			int pi = partition(arr, low, high);

			// 피벗 왼쪽 부분 정렬
			quickSortHelper(arr, low, pi - 1);
			// 피벗 오른쪽 부분 정렬
			quickSortHelper(arr, pi + 1, high);
		}
	}

	// 피벗을 기준으로 배열 분할 (피벗보다 작은 값은 왼쪽, 큰 값은 오른쪽)
	private static int partition(int[] arr, int low, int high)
	{
		int pivot = arr[high];  // 마지막 원소를 피벗으로 선택
		int i = low - 1;        // 작은 원소들의 마지막 인덱스

		// 피벗보다 작은 원소를 왼쪽으로 이동
		for (int j = low; j < high; j++) {
			if (arr[j] < pivot) {
				i++;
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}

		// 피벗을 올바른 위치에 배치
		int temp = arr[i + 1];
		arr[i + 1] = arr[high];
		arr[high] = temp;

		return i + 1;  // 피벗의 최종 위치 반환
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Radix Sort: 자릿수별로 정렬하는 방식 (O(d×n), d는 최대 자릿수, 작은 자릿수에 효율적)
	public static int[] DoRadixSort(int[] value)
	{
		if (value.length == 0) {
			return value;
		}

		// 최대 절댓값을 찾아 자릿수 결정
		int max = Math.abs(value[0]);
		for (int i = 1; i < value.length; i++) {
			if (Math.abs(value[i]) > max) {
				max = Math.abs(value[i]);
			}
		}

		// 각 자릿수에 대해 counting sort 수행 (1의 자리부터 시작)
		for (int exp = 1; max / exp > 0; exp *= 10) {
			countingSortByDigit(value, exp);
		}

		return value;
	}

	// 특정 자릿수를 기준으로 counting sort 수행
	private static void countingSortByDigit(int[] arr, int exp)
	{
		int n = arr.length;
		int[] output = new int[n];
		int[] count = new int[19]; // -9 ~ 9 범위 (-9+9=0 ~ 9+9=18)

		// 각 자릿수 값의 빈도 계산
		for (int i = 0; i < n; i++) {
			int digit = (arr[i] / exp) % 10;
			count[digit + 9]++;  // 음수 처리를 위해 +9
		}

		// 누적 합으로 변환 (실제 위치 계산)
		for (int i = 1; i < 19; i++) {
			count[i] += count[i - 1];
		}

		// 출력 배열 구성 (안정 정렬 유지를 위해 뒤에서부터)
		for (int i = n - 1; i >= 0; i--) {
			int digit = (arr[i] / exp) % 10;
			output[count[digit + 9] - 1] = arr[i];
			count[digit + 9]--;
		}

		// 결과를 원본 배열에 복사
		for (int i = 0; i < n; i++) {
			arr[i] = output[i];
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Search: 데이터 특성을 O(n) 시간에 분석하여 최적의 정렬 알고리즘 선택
    public static char DoSearch(int[] value)
	{
		int n = value.length;

		if (n == 0) {
			return 'Q';
		}

		// 최적화 1: 작은 배열은 Insertion Sort가 효율적
		if (n < 50) {
			return 'I';
		}

		// 최적화 2: 단일 패스로 모든 지표 동시 계산 (O(n))
		int max = 0;                // 최대 절댓값 (자릿수 계산용)
		int sortedPairs = 0;        // 오름차순 쌍 개수
		int descendingPairs = 0;    // 내림차순 쌍 개수
		int duplicatePairs = 0;     // 중복 쌍 개수

		for (int i = 0; i < n; i++) {
			int absVal = Math.abs(value[i]);
			if (absVal > max) {
				max = absVal;
			}

			// 인접한 원소 관계 분석
			if (i > 0) {
				if (value[i - 1] < value[i]) {
					sortedPairs++;
				} else if (value[i - 1] > value[i]) {
					descendingPairs++;
				} else {
					duplicatePairs++;
				}
			}
		}

		// 지표 계산
		int digits = (max == 0) ? 1 : (int) Math.log10(max) + 1;  // 최대 자릿수
		double sortedRatio = (double) sortedPairs / (n - 1);      // 정렬 비율
		double descendingRatio = (double) descendingPairs / (n - 1);  // 역순 비율
		double duplicateRatio = (double) duplicatePairs / (n - 1);    // 중복 비율

		// 최적화 3: 1-2자릿수는 Radix Sort가 최적
		if (digits <= 2) {
			return 'R';
		}

		// 최적화 4: 3자릿수도 Radix Sort 유리
		if (digits == 3) {
			return 'R';
		}

		// 최적화 5: 역순 데이터 감지 → Quick Sort 최악 케이스 회피
		if (descendingRatio >= 0.7) {
			return 'M';  // Merge Sort는 모든 패턴에 안정적
		}

		// 최적화 6: 배열 크기에 따른 적응형 정렬 임계값
		if (n < 5000) {
			if (sortedRatio >= 0.65) {
				return 'I';  // 작은 배열 + 어느정도 정렬 → Insertion
			}
		} else {
			if (sortedRatio >= 0.80) {
				return 'I';  // 큰 배열 + 매우 정렬 → Insertion
			}
		}

		// 최적화 7: 중복이 많은 경우
		if (duplicateRatio >= 0.15) {
			// 큰 배열은 Merge가 캐시 효율성 좋음
			return (n > 10000) ? 'M' : 'H';
		}

		// 최적화 8: 4자릿수 + 큰 배열은 Radix 고려
		if (digits == 4 && n >= 10000) {
			return 'R';
		}

		// 기본값: 일반적인 랜덤 데이터는 Quick Sort
		return 'Q';
	}

	private static int getMaxDigits(int[] value)
	{
		int max = 0;
		for (int i = 0; i < value.length; i++) {
			int absVal = Math.abs(value[i]);
			if (absVal > max) {
				max = absVal;
			}
		}

		if (max == 0) return 1;

		int digits = 0;
		while (max > 0) {
			digits++;
			max /= 10;
		}
		return digits;
	}

	private static double estimateDuplicateRatio(int[] value)
	{
		int n = value.length;
		if (n == 0) return 0.0;

		// Use simple hash table with limited size for O(n) complexity
		int tableSize = Math.min(n, 10000);
		int[] hashTable = new int[tableSize];
		int collisions = 0;

		for (int i = 0; i < n; i++) {
			int hash = Math.abs(value[i] % tableSize);
			if (hashTable[hash] != 0) {
				collisions++;
			}
			hashTable[hash]++;
		}

		return (double) collisions / n;
	}

	private static double getSortedRatio(int[] value)
	{
		int n = value.length;
		if (n <= 1) return 1.0;

		int sortedPairs = 0;
		for (int i = 0; i < n - 1; i++) {
			if (value[i] <= value[i + 1]) {
				sortedPairs++;
			}
		}

		return (double) sortedPairs / (n - 1);
	}
}
