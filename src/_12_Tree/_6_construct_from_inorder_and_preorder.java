package _12_Tree;

import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.Queue;

public class _6_construct_from_inorder_and_preorder {
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

        int inorder[]={2,3,1,4,6,5};
        int preorder[]={1,2,3,4,5,6};
        node root=generate_tree(inorder,preorder,0,inorder.length-1,0,preorder.length-1);
        System.out.println(inorder(root));
        System.out.println(level_order(root));

    }
    static node generate_tree(int inorder[],int preorder[],int l1,int r1,int l2,int r2)
    {
        if (l1>r1||l1<0||r1>=inorder.length)
            return null;
        int new_element=preorder[l2];
        int inorder_node_index=-1;
        for(int i=l1;i<=r1;i++)
        {
            if (inorder[i]==new_element)
            {
                inorder_node_index=i;
                break;
            }
        }

        int length=inorder_node_index-l1;
        int new_l1=l1;
        int new_r1=inorder_node_index-1;
        int new_l2=l2+1;
        int new_r2=l2+length;
        node root=new node(new_element);
        System.out.println("l1"+new_l1+"r1 "+new_r1+"l2 " +new_l2+"r2 "+new_r2+"len "+length+"index "+inorder_node_index);
        root.left=generate_tree(inorder,preorder,new_l1,new_r1,new_l2,new_r2);
        new_l1=inorder_node_index+1;
        new_r1=r1;
        new_l2=new_r2+1;
        new_r2=r2;
        root.right=generate_tree(inorder,preorder,new_l1,new_r1,new_l2,new_r2);
        return root;

    }

    /*
    my method is complicated better use the compact version

     */
    static int preIndex=0;
    public static node cTree(int in[],int pre[],int is,int ie) {
        if (is > ie) return null;
        node root = new node(pre[preIndex++]);

        int inIndex = is;
        for (int i = is; i <= ie; i++) {
            if (in[i] == root.data) {
                inIndex = i;
                break;
            }
        }
        root.left = cTree(in, pre, is, inIndex - 1);
        root.right = cTree(in, pre, inIndex + 1, ie);
        return root;
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
