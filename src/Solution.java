class Solution
{
    public static void main(String[] args) {
        int pages=1;
        int sum=1;
        for (int i=1;i<1076;i++)
        {
            sum+=Math.ceil(Math.log(i)/Math.log(10));
            System.out.println(i +" "+sum);;
        }
    }
}