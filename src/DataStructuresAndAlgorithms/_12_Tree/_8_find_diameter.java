package _12_Tree;

public class _8_find_diameter {
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
        int ans[]={0};
        diameter(node1,ans);
        System.out.println(ans[0]);

    }
    static int diameter(node root,int ans[])
    {
        if (root==null)
            return 0;
        int left_height=diameter(root.left,ans);
        int right_height=diameter(root.right,ans);
        ans[0]=Math.max(left_height+1+right_height,ans[0]);
        return Math.max(left_height,right_height)+1;
    }
}
