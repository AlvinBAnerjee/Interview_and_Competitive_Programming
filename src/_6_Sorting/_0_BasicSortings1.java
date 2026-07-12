package _6_Sorting;

import java.util.Arrays;

/*
Basic Sortings 1: Bubble Sort, Selection Sort, Insertion Sort

These are the three classic O(n^2) comparison sorts. They're rarely used in
practice for large inputs, but come up constantly as warm-up interview
questions and as building blocks for other algorithms (e.g. insertion sort
is used as the base case inside merge sort / Timsort for small runs).

Bubble Sort:
  Repeatedly walk the array, swapping adjacent elements that are out of
  order. Each pass "bubbles" the largest remaining element to the end.
  Time: O(n^2) worst/average, O(n) best (already sorted, with the early
  exit flag). Space: O(1). Stable: yes.

Selection Sort:
  For each position i, find the minimum of the remaining unsorted suffix
  and swap it into position i.
  Time: O(n^2) always (no early exit possible, always scans the rest).
  Space: O(1). Stable: no (the swap can jump an equal element past another).

Insertion Sort:
  Build the sorted prefix one element at a time: take the next element and
  shift it left past everything bigger than it, like sorting a hand of
  playing cards.
  Time: O(n^2) worst/average, O(n) best (already sorted). Space: O(1).
  Stable: yes. Efficient for small or nearly-sorted arrays.
*/
public class _0_BasicSortings1 {

    public static void main(String[] args) {
        System.out.println("Bubble sort:    " + Arrays.toString(bubbleSort(new int[]{5, 2, 9, 1, 5, 6})));
        System.out.println("Selection sort: " + Arrays.toString(selectionSort(new int[]{5, 2, 9, 1, 5, 6})));
        System.out.println("Insertion sort: " + Arrays.toString(insertionSort(new int[]{5, 2, 9, 1, 5, 6})));
    }

    static int[] bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break; // already sorted, nothing left to do
        }
        return arr;
    }

    static int[] selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) minIdx = j;
            }
            int tmp = arr[i];
            arr[i] = arr[minIdx];
            arr[minIdx] = tmp;
        }
        return arr;
    }

    static int[] insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return arr;
    }
}
