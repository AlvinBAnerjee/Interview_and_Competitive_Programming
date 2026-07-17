package _12_Tree;

import java.util.Deque;
import java.util.LinkedList;
//https://www.youtube.com/watch?v=wwv7xcNUhRA&t=774s
public class _10_Burn_Tree_From_any_node {
    static class TreeNode {
        int data;
        TreeNode left;
        TreeNode right;
        TreeNode(int data)
        {
            this.data=data;
            this.left=null;
            this.right=null;
        }

    }

    public static void main(String[] args) {
        TreeNode node1=new TreeNode(1);
        TreeNode node2=new TreeNode(2);
        TreeNode node3=new TreeNode(3);
        TreeNode node4=new TreeNode(4);
        TreeNode node5=new TreeNode(5);
        TreeNode node6=new TreeNode(6);
        node1.left=node2;
        node1.right=node4;
        node2.right=node3;
        node4.right=node5;
        node5.left=node6;
         /*
                1
              /   \
             2     4
              \     \
               3     5
                    /
                   6

                 */
        burnTree(node1,1);
        burnTreeTest(node1,1);
    }
    static void burnTree(TreeNode root, int targetNode)
    {
        Deque<TreeNode> deque=new LinkedList<>();
        burnTreeUtil(root,targetNode,deque);
        while (!deque.isEmpty())
        {
            int size=deque.size();
            while (size-->0)
            {
                TreeNode temp=deque.removeFirst();
                System.out.print(temp.data+ " ");
                if (temp.left!=null)
                    deque.addLast(temp.left);
                if (temp.right!=null)
                    deque.addLast(temp.right);
            }
            System.out.println();
        }

    }
    static int burnTreeUtil(TreeNode root, int targetNode, Deque<TreeNode> deque)
    {
        if (root==null)
            return 0;
        if (root.data==targetNode)
        {
            System.out.println(targetNode);
            if (root.left!=null)
                deque.addLast(root.left);
            if (root.right!=null)
                deque.addLast(root.right);
            return 1;
        }
        int a=burnTreeUtil(root.left,targetNode,deque);
        if (a==1)
        {
            int size= deque.size();;
            while (size-->0)
            {
                TreeNode temp=deque.removeFirst();
                System.out.print(temp.data+" ");
                if (temp.left!=null)
                    deque.addLast(temp.left);
                if (temp.right!=null)
                    deque.addLast(temp.right);
            }
            System.out.println(root.data);
            if (root.right!=null)
                deque.addLast(root.right);
            return 1;
        }
        int b=burnTreeUtil(root.right,targetNode,deque);
        if (b==1)
        {
            int size= deque.size();;
            while (size-->0)
            {
                TreeNode temp=deque.removeFirst();
                System.out.print(temp.data+" ");
                if (temp.left!=null)
                    deque.addLast(temp.left);
                if (temp.right!=null)
                    deque.addLast(temp.right);
            }
            System.out.println(root.data);
            if (root.left!=null)
                deque.addLast(root.left);
            return 1;
        }
        return 0;
    }

    static void burnTreeTest(TreeNode root,int targetNode)
    {
        Deque<TreeNode> deque=new LinkedList<>();
        burnTreeUtilTest(root, targetNode,deque);
        while (!deque.isEmpty())
        {
            int size=deque.size();
            while (size!=0)
            {
                size--;
                TreeNode temp=deque.removeFirst();
                if (temp.left!=null)
                    deque.addLast(temp.left);
                if (temp.right!=null)
                    deque.addLast(temp.right);
                System.out.print(temp.data+" , ");
            }
            System.out.println();
        }
    }
    static int burnTreeUtilTest(TreeNode root,int targetNode,Deque<TreeNode> deque)
    {
        if (root==null)return 0;
        if (root.data==targetNode)
        {
            System.out.println(root.data);
            if (root.left!=null)
                deque.addLast(root.left);
            if (root.right!=null)
                deque.addLast(root.right);
            return 1;
        }
        int a=burnTreeUtilTest(root.left,targetNode,deque);
        if (a==1)
        {
            int size= deque.size();
            while (size>0)
            {
                size--;
                TreeNode temp=deque.removeFirst();
                if (temp.left!=null)
                    deque.addLast(temp.left);
                if (temp.right!=null)
                    deque.addLast(temp.right);
                System.out.print(temp.data+",");
            }
            System.out.println(root.data);
            if (root.right!=null)
                deque.addLast(root.right);
            return 1;
        }

        int b=burnTreeUtilTest(root.right,targetNode,deque);
        if (b==1)
        {
            int size= deque.size();
            while (size>0)
            {
                size--;
                TreeNode temp=deque.removeFirst();
                if (temp.left!=null)
                    deque.addLast(temp.left);
                if (temp.right!=null)
                    deque.addLast(temp.right);
                System.out.print(temp.data+",");
            }
            System.out.println(root.data);
            if (root.left!=null)
                deque.addLast(root.left);
            return 1;
        }
        return 0;
    }



}
