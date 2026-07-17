package _12_Tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
public class _7_print_spiral {
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
        spiral_level_order(node1);

    }
    static void spiral_level_order(node root)
    {
        if (root==null)
            return ;
        Stack<node> stack1=new Stack<>();
        Stack<node> stack2=new Stack<>();
        stack1.push(root);
        while (!stack1.isEmpty() || !stack2.isEmpty())
        {
            while (!stack1.isEmpty())
            {
                node n1=stack1.pop();
                if (n1.left!=null)
                    stack2.push(n1.left);
                if (n1.right!=null)
                    stack2.push(n1.right);
                System.out.print(n1.data+" ");
            }
            System.out.println();
            while (!stack2.isEmpty())
            {
                node n2=stack2.pop();
                if (n2.right!=null)
                    stack1.push(n2.right);
                if (n2.left!=null)
                    stack1.push(n2.left);
                System.out.print(n2.data+" ");

            }
            System.out.println();
        }
    }

}
