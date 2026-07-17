package _12_Tree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class _0_Implementation_and_traversal {
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
        System.out.println("inorder"+inorder(node1));
        System.out.println("preorder"+preorder(node1));
        System.out.println("postorder"+postorder(node1));
        System.out.println("level_order "+level_order(node1));
        System.out.println("height "+height(node1));
    }
    static String inorder(node root)
    {
        if (root==null)
            return "";
        String l=inorder(root.left);
        String n=" "+ Integer.toString(root.data);
        String r=inorder(root.right);
        return l+n+r;
    }
    static String preorder(node root)
    {
        if (root==null)
            return "";
        String n=" "+ Integer.toString(root.data);
        String l=preorder(root.left);
        String r=preorder(root.right);
        return n+l+r;
    }
    static String postorder(node root)
    {
        if (root==null)
            return "";

        String l=postorder(root.left);
        String r=postorder(root.right);
        String n=" "+ Integer.toString(root.data);
        return l+r+n;
    }
    static int height(node root)
    {
        if (root==null)
            return 0;
        return Math.max(height(root.left),height(root.right))+1;
    }
    static String level_order(node root)
    {
        if (root==null)
            return "";
        String str="";
        Queue<node> queue=new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty())
        {
            node n=queue.remove();
            if (n.left!=null)
                queue.add(n.left);
            if (n.right!=null)
                 queue.add(n.right);
            str=str+" "+n.data;
        }
        return str;
    }
}
