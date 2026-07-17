package _6_Sorting;

import java.util.Arrays;

/*
Basic Sortings 2: Merge Sort, Quick Sort, Heap Sort

These are the O(n log n) sorts you actually use / get asked to implement
from scratch in interviews.

Merge Sort:
  Divide and conquer: split the array in half, recursively sort each half,
  then merge the two sorted halves in linear time.
  Time: O(n log n) always (no worst case blowup). Space: O(n) extra for the
  merge buffer. Stable: yes. Good when stability matters or data doesn't
  fit for in-place swapping (e.g. linked lists, external sorting).

Quick Sort:
  Divide and conquer too, but the split point is a chosen "pivot": partition
  the array so everything < pivot ends up left of it and everything >=
  pivot ends up right, then recurse on both sides. No merge step needed
  since by the time recursion bottoms out the whole array is in place.
  Time: O(n log n) average, O(n^2) worst case (e.g. already-sorted input
  with a bad pivot choice - mitigated here by picking a random pivot).
  Space: O(log n) for the recursion stack. Stable: no.
  Usually faster in practice than merge sort due to in-place partitioning
  and better cache locality.

Heap Sort:
  Build a max-heap out of the array, then repeatedly swap the max (root)
  to the end and shrink the heap, sifting the new root down to restore the
  heap property.
  Time: O(n log n) always. Space: O(1), truly in-place (unlike merge sort).
  Stable: no. Rarely fastest in practice (poor cache locality) but its
  O(1) space + guaranteed O(n log n) worst case makes it the sort used
  internally by Arrays.sort() for primitive arrays as a fallback, and the
  underlying idea (heapify / sift-down) is exactly what backs
  PriorityQueue, which comes up constantly in interview problems.
*/
public class _0_BasicSortings2 {

    public static void main(String[] args) {
        System.out.println("Merge sort: " + Arrays.toString(mergeSort(new int[]{5, 2, 9, 1, 5, 6})));
        System.out.println("Quick sort: " + Arrays.toString(quickSort(new int[]{5, 2, 9, 1, 5, 6})));
        System.out.println("Heap sort:  " + Arrays.toString(heapSort(new int[]{5, 2, 9, 1, 5, 6})));
    }

    // ---------- Merge Sort ----------

    static int[] mergeSort(int[] arr) {
        if (arr.length <= 1) return arr;
        mergeSort(arr, 0, arr.length - 1);
        return arr;
    }

    private static void mergeSort(int[] arr, int lo, int hi) {
        if (lo >= hi) return;
        int mid = lo + (hi - lo) / 2;
        mergeSort(arr, lo, mid);
        mergeSort(arr, mid + 1, hi);
        merge(arr, lo, mid, hi);
    }

    private static void merge(int[] arr, int lo, int mid, int hi) {
        int[] merged = new int[hi - lo + 1];
        int i = lo, j = mid + 1, k = 0;
        while (i <= mid && j <= hi) {
            merged[k++] = arr[i] <= arr[j] ? arr[i++] : arr[j++];
        }
        while (i <= mid) merged[k++] = arr[i++];
        while (j <= hi) merged[k++] = arr[j++];
        System.arraycopy(merged, 0, arr, lo, merged.length);
    }

    // ---------- Quick Sort ----------

    static int[] quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
        return arr;
    }

    private static void quickSort(int[] arr, int lo, int hi) {
        if (lo >= hi) return;
        int pivotIdx = lo + (int) (Math.random() * (hi - lo + 1));
        swap(arr, pivotIdx, hi); // move random pivot to the end for Lomuto partitioning
        int p = partition(arr, lo, hi);
        quickSort(arr, lo, p - 1);
        quickSort(arr, p + 1, hi);
    }

    private static int partition(int[] arr, int lo, int hi) {
        int pivot = arr[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (arr[j] < pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, hi);
        return i;
    }

    // ---------- Heap Sort ----------

    static int[] heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(arr, n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);       // move current max to the end
            siftDown(arr, i, 0);   // restore heap property on the shrunk heap
        }
        return arr;
    }

    private static void siftDown(int[] arr, int heapSize, int i) {
        while (true) {
            int largest = i, left = 2 * i + 1, right = 2 * i + 2;
            if (left < heapSize && arr[left] > arr[largest]) largest = left;
            if (right < heapSize && arr[right] > arr[largest]) largest = right;
            if (largest == i) break;
            swap(arr, i, largest);
            i = largest;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
