package _12_Tree;

import java.util.LinkedList;
import java.util.Queue;

public class _5_find_max_width {
    static class  node{
        int data;
        node left;
        node right;
        node(int data)
        {
            this.data=data;
            this.left=null;
            this.right=null;
        }
    }

    public static void main(String[] args) {

                /*
                1
              /   \
             2     4
              \     \
               3     5
                    /
                   6

                 */

        node node1=new node(1);
        node node2=new node(2);
        node node3=new node(3);
        node node4=new node(4);
        node node5=new node(5);
        node node6=new node(6);
        node1.left=node2;
        node1.right=node4;
        node2.right=node3;
        node4.right=node5;
        node5.left=node6;
        System.out.println(maxwidth(node1));

    }
    static int maxwidth(node root)
    {
        if (root == null)
            return 0;
        int maxwidth = 0;

        Queue<node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty())
        {
            int count = q.size();
            maxwidth = Math.max(maxwidth, count);
            while (count-- > 0) {
                node temp = q.remove();
                if (temp.left != null)
                {
                    q.add(temp.left);
                }
                if (temp.right != null)
                {
                    q.add(temp.right);
                }
            }
        }
        return maxwidth;
    }

}
