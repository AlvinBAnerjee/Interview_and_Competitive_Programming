package _12_Tree;

public class _1_print_nodes_at_distance_k {
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
            print_at_k(node1,2,1);

        }
        static void print_at_k(node root,int k,int current)
        {
            if (root==null)
                return;
            if (current==k)
                System.out.print(root.data+" ");
            else
            {
                print_at_k(root.left,k,current+1);
                print_at_k(root.right,k,current+1);
            }

        }


    }
}
