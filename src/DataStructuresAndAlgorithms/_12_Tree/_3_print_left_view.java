package _12_Tree;

public class _3_print_left_view {
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
        print_left_view(node1,1);
    }
    static int max_level_so_far=0;
    static void print_left_view(node root,int current)//based on pre_order
    {
        if (root==null)
            return;
        if (current>max_level_so_far)
        {
            System.out.print(root.data+" ");
            max_level_so_far=current;
        }
        print_left_view(root.left,current+1);
        print_left_view(root.right,current+1);


    }

}
