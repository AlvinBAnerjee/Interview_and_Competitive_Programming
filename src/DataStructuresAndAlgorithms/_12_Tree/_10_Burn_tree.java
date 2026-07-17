package _12_Tree;

public class _10_Burn_tree {
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
    static class Distance{
        int val;
        Distance(int val)
        {
            this.val=val;
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
        burnTime(node1,3,new Distance(-1));
        System.out.println(res);



    }
    static int res=0;
    public static int burnTime(node root, int leaf, Distance dist){
        if(root==null)return 0;
        if(root.data==leaf){dist.val=0;return 1;}
        Distance ldist=new Distance(-1),rdist=new Distance(-1);
        int lh=burnTime(root.left,leaf,ldist);
        int rh=burnTime(root.right,leaf,rdist);

        if(ldist.val!=-1){
            dist.val=ldist.val+1;
            res=Math.max(res,dist.val+rh);
        }
        else if(rdist.val!=-1){
            dist.val=rdist.val+1;
            res=Math.max(res,dist.val+lh);
        }
        return Math.max(lh,rh)+1;
    }

}
