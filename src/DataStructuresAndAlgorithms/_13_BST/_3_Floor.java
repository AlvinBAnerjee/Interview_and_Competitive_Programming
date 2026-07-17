package _13_BST;

public class _3_Floor {
    static class Node{
        int data;
        Node left;
        Node right;
        Node(int data)
        {
            this.data=data;
            this.left=null;
            this.right=null;
        }
    }
    static Node findFloor(Node root,int key)
    {
        Node res=null;
        while (root!=null)
        {
            if (root.data==key)
                return root;
            else if (root.data>key)
            {
                root=root.left;
            }
            else
            {
                res=root;
                root=root.right;
            }
        }
        return res;
    }
}
