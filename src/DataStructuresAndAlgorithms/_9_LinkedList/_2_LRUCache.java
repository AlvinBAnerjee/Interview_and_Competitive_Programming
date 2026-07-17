package _9_LinkedList;

import java.util.HashMap;

public class _2_LRUCache {
    static class Node{
        int key;
        int value;
        Node left;
        Node right;
        Node(int key,int value)
        {
            this.key=key;
            this.value=value;
            this.left=null;
            this.right= null;
        }
    }
    static class DLL{
        Node head;
        Node tail;
        void addBeg(Node n)
        {
            if (head==null)
            {
                head=n;
                tail=n;
                return ;
            }
            n.right=head;
            head.left=n;
            head=n;
        }
        void remove(Node n)
        {
            if (head==n&&n==tail)
            {
                head=null;
                tail=null;
            }
            else if (n==head)
            {
                head=head.right;
                head.left=null;
            }
            else if (n==tail)
            {
                tail=tail.left;
                tail.right=null;
            }
            else
            {
                n.left.right=n.right;
                n.right.left=n.left;
            }
        }
        Node removeEnd()
        {
            Node temp=tail;
            if (head==tail)
            {

                head=null;
                tail=null;
            }
            else
            {
                tail=tail.left;
                tail.right=null;
            }
            return temp;
        }
        void traverse()
        {
            Node t=head;
            while (t!=null)
            {
                System.out.print(t.value+"->");
                t=t.right;

            }
            System.out.println();
        }
    }
    static class LRUCache{
        int capacity;
        int size;
        DLL ll;
        HashMap<Integer,Node> map=new HashMap<>();
        public LRUCache(int capacity) {
            this.capacity=capacity;
            this.size=0;
            this.ll=new DLL();
            this.map=new HashMap<>();
        }

        public int get(int key) {
            if (!map.containsKey(key))
                return -1;
            int value=map.get(key).value;
            Node temp=map.get(key);
            ll.remove(temp);
            Node newNode=new Node(key,value);
            ll.addBeg(newNode);
            map.put(key,newNode);
            return value;
        }

        public void put(int key, int value) {
            if (map.containsKey(key))
            {
                Node temp=map.get(key);
                ll.remove(temp);
                Node newNode=new Node(key,value);
                ll.addBeg(newNode);
                map.put(key,newNode);
            }
            else
            {
                if (size==capacity)
                {
                    Node temp=ll.removeEnd();
                    map.remove(temp.key);
                    size--;
                }
                Node newNode=new Node(key,value);
                ll.addBeg(newNode);
                map.put(key,newNode);
                size++;
            }
        }
    }

    public static void main(String[] args) {
        LRUCache cache=new LRUCache(2);
        cache.put(1,1);
        cache.put(2,2);
        System.out.println(cache.get(1));
        cache.put(3,3);
        System.out.println(cache.get(2));
        cache.put(4,4);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
        cache.put(3,69);
        cache.put(5,5);
        System.out.println(cache.get(3));
        System.out.println(cache.get(5));











    }


}
