package _13_BST;

public class _1_Basic_Operations {
    static class Node{
        int  data;
        Node left;
        Node right;
        Node(int data)
        {
            this.data=data;
            this.left=null;
            this.right=null;
        }

    }
    static class BST{

        Node root;
        BST()
        {
            root=null;
        }
         Node search(Node root,int  key)
         {
             if (root==null)
                 return null;
             if (root.data==key)
                 return root;
             else if(root.data>key)
             {
                 return search(root.left,key);
             }
             else
             {
                 return search(root.right,key);
             }

         }

         Node insert(Node root,int item)
         {

             if(root==null)
             {
                 Node temp=new Node(item);
                 root=temp;
                 if (this.root==null)
                     this.root=root;
                 return root;
             }
             if (root.data>item)
             {

                     root.left=insert(root.left,item);

             }
             else if (root.data<item)
             {
                     root.right=insert(root.right,item);
             }
             return root;


         }
         void inorder(Node root)
         {
             if (root==null)
                 return;
             inorder(root.left);
             System.out.print(root.data+" , ");
             inorder(root.right);
         }
         Node findSuccessor(Node root)
         {
             Node prev=root;
            while (root!=null)
            {
                prev=root;
                root=root.left;
            }
            return prev;
         }
         Node delete(Node root,int x)
         {
             if (root==null)
                 return null;
             if (root.data>x)
             {
                 root.left=delete(root.left,x);
             }
             else if (root.data<x)
             {
                 root.right=delete(root.right,x);
             }
             else
             {
                 if (root.left==null)
                 {
                     return root.right;
                 }
                 else if (root.right==null)
                     return root.left;
                 else
                 {
                     Node succ=findSuccessor(root.right);
                     root.data=succ.data;
                     root.right=delete(root.right,succ.data);
                 }
             }
             return root;
         }




    }
    public static void main(String[] args) {
        BST bst = new BST();
        bst.insert(bst.root, 6);
        bst.insert(bst.root, 3);
        bst.insert(bst.root, 5);
        bst.insert(bst.root, 4);
        bst.insert(bst.root, 7);
        bst.insert(bst.root, 1);

        bst.inorder(bst.root);
        System.out.println();
        bst.delete(bst.root,3);
        bst.inorder(bst.root);

    }
}
