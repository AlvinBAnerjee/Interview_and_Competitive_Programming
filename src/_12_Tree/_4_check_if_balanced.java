package _12_Tree;

public class _4_check_if_balanced {
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
        System.out.println(is_balanced(node1));
    }
    static boolean is_balanced(node root)
    {
        if (is_balanced_func(root)!=-1)
            return true;
        return false;
    }
    static int is_balanced_func(node root)
    {
        if (root==null)
            return 0;
        int l=is_balanced_func(root.left);
        int r=is_balanced_func(root.right);
        if (l==-1||r==-1)
            return -1;
        else
        {
            if ((int)Math.abs(r-l)<=1)
            {
                return Math.max(l,r)+1;
            }
            else
                return -1;
        }
    }
}
