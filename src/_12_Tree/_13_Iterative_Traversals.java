package _12_Tree;

import java.util.Stack;
import java.util.*;

public class _13_Iterative_Traversals {
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
        inorder(node1);
        //preorderIterative(node1);

    }
    static void inorder(node root)
    {
        Stack<node> stack=new Stack<>();
        node current=root;
        while (true)
        {
            if (stack.isEmpty()&& current==null)
                break;
           while (current!=null)
           {
               stack.push(current);
               current=current.left;
           }
           current=stack.pop();
           System.out.print(current.data+" ");
           current=current.right;
        }
        System.out.println();
    }
    public ArrayList<Integer> inorderTraversal(node A) {
        ArrayList<Integer> list = new ArrayList<>();
        Stack<node> stack = new Stack<>();
        while (A != null) {
            stack.push(A);
            A = A.left;
        }
        while (!stack.isEmpty()) {
            node removed = stack.pop();
            list.add(removed.data);
            node right = removed.right;
            while (right != null) {
                stack.push(right);
                right = right.left;
            }
        }
        return list;
    }
    static void preorderIterative(node node)
    {
        if (node == null)
        {
            return;
        }
        Stack<node> st = new Stack<node>();
        node curr = node;
        while (curr != null || !st.isEmpty())
        {
            while (curr != null)
            {
                System.out.print(curr.data + " ");

                if (curr.right != null)
                    st.push(curr.right);

                curr = curr.left;
            }
            if (!st.isEmpty())
            {
                curr = st.pop();
            }
        }
    }
}
