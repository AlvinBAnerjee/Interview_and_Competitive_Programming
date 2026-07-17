package _13_BST;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class _2_Avl_Tree_Implementation {
    static class Node
    {
        int data;
        Node left;
        Node right;
        int height;
        Node(int data)
        {
            this.data=data;
            this.left=null;
            this.right=null;
            this.height=1;
        }
    }
    static class Avl_Tree
    {
        Node root;
        Avl_Tree()
        {
            root=null;
        }
        Node getRoot()
        {
            return root;
        }
        int height(Node root)
        {
            if (root==null)
                return 0;
            else
                return root.height;
        }
         int getBalanceFactor(Node root)
        {
            if (root==null)
                return 0;
            return height(root.left)-height(root.right);
        }
         Node insert(Node root,int key)
        {
            if (root==null)
                return new Node(key);
            if (key<root.data)
            {
                root.left=insert(root.left,key);
            }
            else if (key>root.data)
            {
                root.right=insert(root.right,key);
            }
            else
                return root;
            root.height=Math.max(height(root.left),height(root.right))+1;
            int balance=getBalanceFactor(root);
            if (balance>1&&root.left.data>key)
            {
                //ll imbalance
                return rrotation(root);

            }
            if (balance>1&&root.left.data<key)
            {
                //lr imbalance
                root.left=lrotation(root.left);
                return rrotation(root);

            }
            if (balance<-1&&key>root.right.data)
            {
                //rr imbalance
                return lrotation(root);

            }
            if (balance<-1&&key<root.right.data)
            {
                //rl imbalance
                root.right=rrotation(root.right);
                return lrotation(root.right);

            }
            return root;
        }
        Node lrotation(Node root)
        {
            Node new_root=root.right;
            Node y=new_root.left;
            new_root.left=root;
            root.right=y;

            root.height=Math.max(height(root.left),height(root.right))+1;
            new_root.height=Math.max(height(new_root.left),height(new_root.right))+1;

            return new_root;
        }
        Node rrotation(Node root)
        {
            Node new_root=root.left;
            Node z=new_root.right;

            new_root.right=root;
            root.left=z;

            root.height=Math.max(height(root.left),height(root.right))+1;
            new_root.height=Math.max(height(new_root.left),height(new_root.right))+1;
            return new_root;
        }
         void generateTree()throws IOException
        {
            FileWriter fw=new FileWriter("display.dot");
            PrintWriter pw=new PrintWriter(fw);
            pw.println("digraph G{");

            Queue<Node> queue=new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty())
            {
                Node top=queue.remove();
                if (top.left!=null) {
                    queue.add(top.left);
                    pw.println(top.data+"->"+top.left.data+";");
                }
                else
                {
                    pw.println("nullleft"+top.data+"[label=null];");
                    pw.println(top.data+"->"+"nullleft"+top.data+";");
                }
                if (top.right!=null) {
                    queue.add(top.right);
                    pw.println(top.data+"->"+top.right.data+";");

                }
                else
                {
                    pw.println("nullright"+top.data+"[label=null];");
                    pw.println(top.data+"->"+"nullright"+top.data+";");
                }
            }
            pw.println("}");
            pw.close();



        }
    }

    public static void main(String[] args) throws IOException{
        Scanner sc=new Scanner(System.in);
        Avl_Tree at=new Avl_Tree();
        int item=sc.nextInt();
        while (item!=-1) {
            at.root = at.insert(at.root, item);
            item=sc.nextInt();
        }
        at.generateTree();



    }
}
/*
system("dot -Tpng display.dot -o filename.png");
        system("open filename.png");
 */