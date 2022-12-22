package _20_SegmentTree_BinaryIndexedTree;

import java.util.Arrays;

public class _0_SegmentTreeImplementation {
    public static  void main(String args[])
    {
        int arr[]={10,20,30,40};
        SegmentTree st=new SegmentTree(arr);
        System.out.println(Arrays.toString(st.arr));
        System.out.println(Arrays.toString(st.tree));
        System.out.println(st.query(1,3));
        st.update(1,1);
        System.out.println(st.query(1,3));


    }
    static class SegmentTree{
        int arr[];
        int tree[];
        SegmentTree(int arr[])
        {
            this.arr=arr;
            constructTree(arr);
        }
        void constructTree(int arr[])
        {
            int n=arr.length;
            tree=new int[4*n];
            constructTreeUtil(0,n-1,0);
        }
        int constructTreeUtil(int segmentStart,int segmentEnd,int treeIndex)
        {
            if (segmentStart==segmentEnd)
            {
                tree[treeIndex]=arr[segmentStart];
                return tree[treeIndex];
            }
            int mid=(segmentEnd+segmentStart)/2;
            tree[treeIndex]=constructTreeUtil(segmentStart,mid,2*treeIndex+1)+constructTreeUtil(mid+1,segmentEnd,2*treeIndex+2);
            return tree[treeIndex];
        }
        int query(int queryStartIndex,int queryEndIndex)
        {
            return queryUtil(0,arr.length-1,0,queryStartIndex,queryEndIndex);
        }
        int queryUtil(int segmentStart,int segmentEnd,int index,int queryStartIndex,int queryEndIndex)
        {
            if (queryEndIndex<segmentStart||queryStartIndex>segmentEnd) return 0;
            if (queryStartIndex<=segmentStart && queryEndIndex>=segmentEnd) return tree[index];
            int mid=(segmentStart+segmentEnd)/2;
            return queryUtil(segmentStart,mid,2*index+1,queryStartIndex,queryEndIndex)+
                    queryUtil(mid+1,segmentEnd,2*index+2,queryStartIndex,queryEndIndex);

        }
        void update(int index,int value)
        {
            int diff=value-arr[index];
             updateUtil(0,arr.length-1,0,index,diff);
        }
        void updateUtil(int segmentStart,int segmentEnd,int segmentIndex,int index, int diff)
        {
            if (index<segmentStart||index>segmentEnd)return;
            tree[segmentIndex]=tree[segmentIndex]+diff;
            if (segmentStart<segmentEnd)
            {
                int mid=(segmentStart+segmentEnd)/2;
                updateUtil(segmentStart,mid,2*segmentIndex+1,index,diff);
                updateUtil(mid+1,segmentEnd,2*segmentIndex+2,index,diff);
            }
        }

    }
}
