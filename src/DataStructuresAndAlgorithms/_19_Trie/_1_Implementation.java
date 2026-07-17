package _19_Trie;

import com.sun.source.tree.Tree;

import javax.swing.tree.TreeNode;

public class    _1_Implementation {
    static class node{
        node children[]=new node[26];
        boolean isEnd;
    }
    static class Tries{
        node root;
        Tries()
        {
            root=new node();
        }
        void insert(String key)
        {
            node current=root;
            for (int i=0;i<key.length();i++)
            {
                int index=key.charAt(i)-'A';
                if (current.children[index]==null)
                {
                    current.children[index]=new node();
                }
                current=current.children[index];
            }
            current.isEnd=true;
        }
        boolean search(String key)
        {
            node current=root;
            for (int i=0;i<key.length();i++)
            {
                int index=key.charAt(i)-'A';
                if (current.children[index]==null)
                {
                    return false;
                }
                current=current.children[index];
            }
            return current.isEnd;
        }
        node delete(String key)
        {
            return delete(root,key,0);
        }
        node delete(node root,String key,int i)
        {
            if (root==null)
                return null;
            if (i==key.length())
            {
                root.isEnd=false;
                if (isEmpty(root))
                    root=null;
                return root;
            }
            int index=key.charAt(i)-'A';
            root.children[index]=delete(root.children[index],key,i+1);
            if (isEmpty(root) && root.isEnd==false)
                root=null;
            return root;
        }
        boolean isEmpty(node n)
        {
            for (int i=0;i<26;i++)
            {
                if (n.children[i]!=null)
                    return false;
            }
            return true;
        }

    }
}
