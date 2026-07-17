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

        System.out.print("Inorder (stack): ");
        inorder(node1);

        System.out.print("Preorder (stack): ");
        preorderIterative(node1);

        System.out.print("Postorder (two stacks): ");
        postorderTwoStacks(node1);

        System.out.print("Postorder (one stack, lastVisited): ");
        postorderOneStack(node1);

        System.out.print("Inorder (Morris, O(1) space): ");
        morrisInorder(node1);

        System.out.print("Preorder (Morris, O(1) space): ");
        morrisPreorder(node1);

        System.out.print("Inorder again after Morris traversals (tree must be unchanged): ");
        inorder(node1);
    }
    // Single-stack inorder: push the entire left spine, pop (visit), then
    // repeat on the popped node's right subtree. Popping always yields the
    // next-smallest unvisited node because everything smaller is already
    // sitting deeper in the stack.
    static void inorder(node root)
    {
        Stack<node> stack=new Stack<>();
        node current=root;
        while (!stack.isEmpty() || current!=null)
        {
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
    static void preorderIterative(node root)
    {
        if (root == null) return;
        Stack<node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty())
        {
            node current = stack.pop();
            System.out.print(current.data + " ");
            if (current.right != null) stack.push(current.right);
            if (current.left != null) stack.push(current.left);
        }
        System.out.println();
    }

    // Two-stack postorder: push root, then always push left then right onto
    // stack1 so right ends up on top and gets popped first. That pop order
    // (root, right, left) recorded into stack2 gives postorder when reversed.
    static void postorderTwoStacks(node root) {
        if (root == null) {
            System.out.println();
            return;
        }
        Stack<node> stack1 = new Stack<>();
        Stack<node> stack2 = new Stack<>();
        stack1.push(root);
        while (!stack1.isEmpty()) {
            node curr = stack1.pop();
            stack2.push(curr);
            if (curr.left != null) stack1.push(curr.left);
            if (curr.right != null) stack1.push(curr.right);
        }
        while (!stack2.isEmpty()) {
            System.out.print(stack2.pop().data + " ");
        }
        System.out.println();
    }

    // Single-stack postorder: keep going left, pushing as we go. At a null
    // left, peek the stack - if the right child exists and hasn't been
    // visited yet, descend into it; otherwise the node is done, visit it.
    // "lastVisited" is what makes this safe: without it we couldn't tell
    // whether we arrived at peekNode after finishing its right subtree
    // (visit now) or are seeing it for the first time (still need to
    // explore right before it counts as done).
    static void postorderOneStack(node root) {
        Stack<node> stack = new Stack<>();
        node lastVisited = null;
        node curr = root;
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                node peekNode = stack.peek();
                if (peekNode.right != null && lastVisited != peekNode.right) {
                    curr = peekNode.right;
                } else {
                    System.out.print(peekNode.data + " ");
                    lastVisited = stack.pop();
                }
            }
        }
        System.out.println();
    }

    // Morris inorder: O(1) space, no stack. For each node with a left child,
    // find its inorder predecessor (rightmost node in the left subtree) and
    // thread predecessor.right -> curr temporarily, so that after finishing
    // the left subtree we can climb back up without a stack. The second time
    // we reach curr via the thread, we remove it (restoring the tree) and
    // move right.
    static void morrisInorder(node root) {
        node curr = root;
        while (curr != null) {
            if (curr.left == null) {
                System.out.print(curr.data + " ");
                curr = curr.right;
            } else {
                node pred = curr.left;
                while (pred.right != null && pred.right != curr) {
                    pred = pred.right;
                }
                if (pred.right == null) {
                    pred.right = curr;
                    curr = curr.left;
                } else {
                    pred.right = null;
                    System.out.print(curr.data + " ");
                    curr = curr.right;
                }
            }
        }
        System.out.println();
    }

    // Morris preorder: same threading trick as Morris inorder, but print
    // curr the first time we visit it (before descending left) instead of
    // when the thread is unwound.
    static void morrisPreorder(node root) {
        node curr = root;
        while (curr != null) {
            if (curr.left == null) {
                System.out.print(curr.data + " ");
                curr = curr.right;
            } else {
                node pred = curr.left;
                while (pred.right != null && pred.right != curr) {
                    pred = pred.right;
                }
                if (pred.right == null) {
                    System.out.print(curr.data + " ");
                    pred.right = curr;
                    curr = curr.left;
                } else {
                    pred.right = null;
                    curr = curr.right;
                }
            }
        }
        System.out.println();
    }
}
