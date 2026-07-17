package _14_Heaps;
import java.util.*;
public class _0_Implementation_for_Prims {
    static class Pair{
        int key;
        int value;
        Pair(int key,int value)
        {
            this.key=key;
            this.value=value;
        }
    }
    static class MinHeap {
        Pair arr[];
        int size;
        int capacity;
        int indexes[];
        MinHeap(int capacity) {
            this.arr = new Pair[capacity];
            this.size = 0;
            this.capacity = capacity;
        }

        MinHeap(Pair input[]) {
            this.arr = Arrays.copyOf(input, input.length);
            this.indexes=new int[input.length];
            for (int i=0;i<input.length;i++)
                indexes[i]=i;
            this.size = arr.length;
            this.capacity = arr.length;
        }

        void build() {
            for (int i = size / 2; i >= 0; i--)
                heapify(i);
        }

        void heapify(int i) {
            int l = 2 * i + 1;
            int r = 2 * i + 2;
            int next = i;
            if (l < size && arr[next].value > arr[l].value) {
                next = l;
            }
            if (r < size && arr[next].value > arr[r].value) {
                next = r;
            }
            if (next != i) {
                swap(i, next);
                heapify(next);
            }

        }

        void swap(int a, int b) {
            int the_element_to_be_swapped_original_node=arr[a].key;
            int the_element_to_be_swapped_with_original_node=arr[b].key;

            indexes[the_element_to_be_swapped_original_node]=b;
            indexes[the_element_to_be_swapped_with_original_node]=a;

            Pair temp = arr[a];
            arr[a] = arr[b];
            arr[b] = temp;
        }

        Pair remove_element() {
            if (size == 0)
                return null;
            Pair top = arr[0];
            swap(0, size - 1);
            size--;
            heapify(0);
            return top;
        }

        void add_element(Pair element) {
            if (size == capacity) {
                System.out.println("Overflow");
                return;
            }
            arr[size] = element;
            size++;
            int i = size - 1;
            while (arr[i].value < arr[(i - 1) / 2].value) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }

        }
        void decrease_node(int node, int value)
        {
            Pair modified_node=new Pair(node,value);
            int  the_current_index_of_the_node=indexes[node];
            decrease_index(the_current_index_of_the_node,modified_node);
        }

        void decrease_index(int index, Pair value)//instead of just index we could simply use hashmap values
        {
            arr[index] = value;
            int i = index;
            while (arr[i].value < arr[(i - 1) / 2].value) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
        }

        Pair peek() {
            return arr[0];
        }
    }

    public static void main(String[] args) {
        Pair nodes[]={new Pair(0,10),new Pair(1,3),new Pair(2,4),new Pair(3,99)};
        MinHeap mh=new MinHeap(nodes);
        for(int i=0;i< mh.size;i++)
        {
            System.out.print("key="+mh.arr[i].key+"value="+mh.arr[i].value+",   ");
        }
        System.out.println();
        System.out.println(Arrays.toString(mh.indexes));
        mh.build();
        for(int i=0;i< mh.size;i++)
        {
            System.out.print("key="+mh.arr[i].key+"value="+mh.arr[i].value+",   ");
        }
        System.out.println();
        System.out.println(Arrays.toString(mh.indexes));

        mh.decrease_node(3,7);

        for(int i=0;i< mh.size;i++)
        {
            System.out.print("key="+mh.arr[i].key+"value="+mh.arr[i].value+",   ");
        }
        System.out.println();
        System.out.println(Arrays.toString(mh.indexes));

    }
}

