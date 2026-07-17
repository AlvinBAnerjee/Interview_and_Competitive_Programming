package _9_LinkedList;

public class _1_LinkedList_Loop_Detection {
    public static void main(String[] args) {
        Node n1=new Node(1,null);
        Node n2=new Node(2,null);
        Node n3=new Node(3,null);
        Node n4=new Node(4,null);
        Node n5=new Node(5,null);
        Node n6=new Node(6,null);
        Node n7=new Node(7,null);
        n1.next=n2;
        n2.next=n3;
        n3.next=n4;
        n4.next=n5;
        n5.next=n6;
        n6.next=n7;
        n7.next=null;

        // Time to find the loop

        Node fast=n1;
        Node slow=n1;
        do
        {

            if(!(fast.next==null||fast.next.next==null))
            {
                fast=fast.next;
            }
            if (slow.next!=null)
            {
                slow=slow.next;
            }
            if (fast==slow) {
                System.out.println("Yo bby");
                break;
            }
        }while(fast!=null);

    }
    static  class Node{
        int value;
        Node next;
        Node(int value,Node next)
        {
            this.value=value;
            this.next=next;
        }
    }
}

