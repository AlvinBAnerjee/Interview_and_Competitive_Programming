package _12_Tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class _1LevelOrderLineByLine {
    static class Node{
        int data;
        Node left;
        Node right;
    }
    void printLevelOrder(Node root)
    {
        Deque<Node> deque=new LinkedList<>();
        deque.addLast(root);
        deque.addLast(null);
        while (!deque.isEmpty())
        {
            Node temp=deque.removeFirst();
            if (temp==null)
            {
                System.out.println();
                if (deque.size()==0)
                    break;
                deque.addLast(null);
                continue;
            }

            System.out.print(temp.data+" , ");
            if (temp.left!=null)
            deque.addLast(temp.left);

            if (temp.right!=null)
                deque.addLast(temp.right);

        }
    }
}
