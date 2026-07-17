package _12_Tree;
/*
O(logn * logn)
 */
public class _11_no_of_nodes_in_a_Complete_Binary_Tree {
    static  class Node
    {
        int key;
        Node left;
        Node right;
        Node(int k){
            key=k;
            left=right=null;
        }
    }
    public static int countNode(Node root){
        int lh=0,rh=0;
        Node curr=root;
        while(curr!=null){
            lh++;
            curr=curr.left;
        }
        curr=root;
        while(curr!=null){
            rh++;
            curr=curr.right;
        }
        if(lh==rh){
            return (int)Math.pow(2,lh)-1;
        }else{
            return 1+countNode(root.left)+countNode(root.right);
        }
    }
}
