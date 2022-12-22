package CNS;

import _2_Recursion.Permutation_of_String;

import java.util.Scanner;

public class _4_20 {
    static String key1;
    static String key2;
    public static void main(String[] args) {
        String str="10010111";//the plain text
        String key="1010000010";//the inital 10 bit key

        generateKeys(key);//first we generate the keys
        System.out.println(key1);
        System.out.println(key2);
        encrypt(str);// invoking the encypt function

    }
    static void encrypt(String plainText)
    {
        System.out.println("Plain Text:"+plainText);
        String part1=encryptUtil1(plainText);//first stage of the encryption process
        String part2=encryptUtil2(part1);
        System.out.println("Encrypted Text"+part2);
    }
    static String encryptUtil1(String plainText)
    {
        /*
        10010111
        1010000010
         */
        char PT[]=plainText.toCharArray();
        char IP[]=new char[plainText.length()];//Initial Permutaion

        IP[0]= PT[1];
        IP[1]= PT[5];
        IP[2]= PT[2];
        IP[3]= PT[0];
        IP[4]= PT[3];
        IP[5]= PT[7];
        IP[6]= PT[4];
        IP[7]= PT[6];

        int j=0;

        char L[]=new char[4];//splitting the left 4 bits
        L[0]=IP[j++];
        L[1]=IP[j++];
        L[2]=IP[j++];
        L[3]=IP[j++];

        char R[]=new char[4];//splitting the right 4 bits
        R[0]=IP[j++];
        R[1]=IP[j++];
        R[2]=IP[j++];
        R[3]=IP[j++];

        char EP[]=new char[8];//Expansion permutation of the right bits
        EP[0]=R[3];
        EP[1]=R[0];
        EP[2]=R[1];
        EP[3]=R[2];
        EP[4]=R[1];
        EP[5]=R[2];
        EP[6]=R[3];
        EP[7]=R[0];


//Applying XOR operation on the EP with key1
        EP[0]=(""+(Integer.parseInt(""+EP[0]) ^ Integer.parseInt(""+key1.charAt(0)))).charAt(0);
        EP[1]=(""+(Integer.parseInt(""+EP[1]) ^ Integer.parseInt(""+key1.charAt(1)))).charAt(0);
        EP[2]=(""+(Integer.parseInt(""+EP[2]) ^ Integer.parseInt(""+key1.charAt(2)))).charAt(0);
        EP[3]=(""+(Integer.parseInt(""+EP[3]) ^ Integer.parseInt(""+key1.charAt(3)))).charAt(0);
        EP[4]=(""+(Integer.parseInt(""+EP[4]) ^ Integer.parseInt(""+key1.charAt(4)))).charAt(0);
        EP[5]=(""+(Integer.parseInt(""+EP[5]) ^ Integer.parseInt(""+key1.charAt(5)))).charAt(0);
        EP[6]=(""+(Integer.parseInt(""+EP[6]) ^ Integer.parseInt(""+key1.charAt(6)))).charAt(0);
        EP[7]=(""+(Integer.parseInt(""+EP[7]) ^ Integer.parseInt(""+key1.charAt(7)))).charAt(0);

        int so[][]={
                {1,0,3,2},
                {3,2,1,0},
                {0,2,1,3},
                {3,1,3,2}
        };

        int s1[][]={
                {0,1,2,3},
                {2,0,1,3},
                {3,0,1,0},
                {2,1,0,3}
        };

        char P4[]=new char[4];

        int row=0;
        int column=0;
        if ("00".equals(""+EP[0]+EP[3]))
            row=0;
        else if ("01".equals(""+EP[0]+EP[3]))
            row=1;
        else if ("10".equals(""+EP[0]+EP[3]))
            row=2;
        else if ("11".equals(""+EP[0]+EP[3]))
            row=3;

        if ("00".equals(""+EP[1]+EP[2]))
            column=0;
        else if ("01".equals(""+EP[1]+EP[2]))
            column=1;
        else if ("10".equals(""+EP[1]+EP[2]))
            column=2;
        else if ("11".equals(""+EP[1]+EP[2]))
            column=3;



        if (so[row][column]==0)
        {
            P4[0]='0';
            P4[1]='0';
        }
        else if (so[row][column]==1)
        {
            P4[0]='0';
            P4[1]='1';
        }
        else if (so[row][column]==2)
        {
            P4[0]='1';
            P4[1]='0';
        }else if (so[row][column]==3)
        {
            P4[0]='1';
            P4[1]='1';
        }

        if ("00".equals(""+EP[4]+EP[7]))
            row=0;
        else if ("01".equals(""+EP[4]+EP[7]))
            row=1;
        else if ("10".equals(""+EP[4]+EP[7]))
            row=2;
        else if ("11".equals(""+EP[4]+EP[7]))
            row=3;

        if ("00".equals(""+EP[5]+EP[6]))
            column=0;
        else if ("01".equals(""+EP[5]+EP[6]))
            column=1;
        else if ("10".equals(""+EP[5]+EP[6]))
            column=2;
        else if ("11".equals(""+EP[5]+EP[6]))
            column=3;



        if (s1[row][column]==0)
        {
            P4[2]='0';
            P4[3]='0';
        }
        else if (s1[row][column]==1)
        {
            P4[2]='0';
            P4[3]='1';
        }
        else if (s1[row][column]==2)
        {
            P4[2]='1';
            P4[3]='0';
        }else if (s1[row][column]==3)
        {
            P4[2]='1';
            P4[3]='1';
        }
        P4[0]=(""+(Integer.parseInt(""+P4[0]) ^ Integer.parseInt(""+L[0]))).charAt(0);
        P4[1]=(""+(Integer.parseInt(""+P4[1]) ^ Integer.parseInt(""+L[1]))).charAt(0);
        P4[2]=(""+(Integer.parseInt(""+P4[2]) ^ Integer.parseInt(""+L[2]))).charAt(0);
        P4[3]=(""+(Integer.parseInt(""+P4[3]) ^ Integer.parseInt(""+L[3]))).charAt(0);

        return (new String(R)+new String(P4));//returning the ans from stage one


    }
    static String encryptUtil2(String plainText)
    {
        /*
        10010111
        1010000010
         */
        char PT[]=plainText.toCharArray();
        char IP[]=new char[plainText.length()];//This time no need for changing the PT with Initial Permutaion matrix

        IP[0]= PT[0];
        IP[1]= PT[1];
        IP[2]= PT[2];
        IP[3]= PT[3];
        IP[4]= PT[4];
        IP[5]= PT[5];
        IP[6]= PT[6];
        IP[7]= PT[7];

        int j=0;

        char L[]=new char[4];//Splitting the left 4 bits
        L[0]=IP[j++];
        L[1]=IP[j++];
        L[2]=IP[j++];
        L[3]=IP[j++];

        char R[]=new char[4];//Splitting the right 4 bits
        R[0]=IP[j++];
        R[1]=IP[j++];
        R[2]=IP[j++];
        R[3]=IP[j++];

        char EP[]=new char[8];//Expansion permutation
        EP[0]=R[3];
        EP[1]=R[0];
        EP[2]=R[1];
        EP[3]=R[2];
        EP[4]=R[1];
        EP[5]=R[2];
        EP[6]=R[3];
        EP[7]=R[0];

// Applying XOR on the Expansion Permutaion with key 2
        EP[0]=(""+(Integer.parseInt(""+EP[0]) ^ Integer.parseInt(""+key2.charAt(0)))).charAt(0);
        EP[1]=(""+(Integer.parseInt(""+EP[1]) ^ Integer.parseInt(""+key2.charAt(1)))).charAt(0);
        EP[2]=(""+(Integer.parseInt(""+EP[2]) ^ Integer.parseInt(""+key2.charAt(2)))).charAt(0);
        EP[3]=(""+(Integer.parseInt(""+EP[3]) ^ Integer.parseInt(""+key2.charAt(3)))).charAt(0);
        EP[4]=(""+(Integer.parseInt(""+EP[4]) ^ Integer.parseInt(""+key2.charAt(4)))).charAt(0);
        EP[5]=(""+(Integer.parseInt(""+EP[5]) ^ Integer.parseInt(""+key2.charAt(5)))).charAt(0);
        EP[6]=(""+(Integer.parseInt(""+EP[6]) ^ Integer.parseInt(""+key2.charAt(6)))).charAt(0);
        EP[7]=(""+(Integer.parseInt(""+EP[7]) ^ Integer.parseInt(""+key2.charAt(7)))).charAt(0);



        int so[][]={
                {1,0,3,2},
                {3,2,1,0},
                {0,2,1,3},
                {3,1,3,2}
        };

        int s1[][]={
                {0,1,2,3},
                {2,0,1,3},
                {3,0,1,0},
                {2,1,0,3}
        };

        char P4[]=new char[4];//Permutaion 4

        int row=0;
        int column=0;
        if ("00".equals(""+EP[0]+EP[3]))
            row=0;
        else if ("01".equals(""+EP[0]+EP[3]))
            row=1;
        else if ("10".equals(""+EP[0]+EP[3]))
            row=2;
        else if ("11".equals(""+EP[0]+EP[3]))
            row=3;

        if ("00".equals(""+EP[1]+EP[2]))
            column=0;
        else if ("01".equals(""+EP[1]+EP[2]))
            column=1;
        else if ("10".equals(""+EP[1]+EP[2]))
            column=2;
        else if ("11".equals(""+EP[1]+EP[2]))
            column=3;



        if (so[row][column]==0)
        {
            P4[0]='0';
            P4[1]='0';
        }
        else if (so[row][column]==1)
        {
            P4[0]='0';
            P4[1]='1';
        }
        else if (so[row][column]==2)
        {
            P4[0]='1';
            P4[1]='0';
        }else if (so[row][column]==3)
        {
            P4[0]='1';
            P4[1]='1';
        }

        if ("00".equals(""+EP[4]+EP[7]))
            row=0;
        else if ("01".equals(""+EP[4]+EP[7]))
            row=1;
        else if ("10".equals(""+EP[4]+EP[7]))
            row=2;
        else if ("11".equals(""+EP[4]+EP[7]))
            row=3;

        if ("00".equals(""+EP[5]+EP[6]))
            column=0;
        else if ("01".equals(""+EP[5]+EP[6]))
            column=1;
        else if ("10".equals(""+EP[5]+EP[6]))
            column=2;
        else if ("11".equals(""+EP[5]+EP[6]))
            column=3;



        if (s1[row][column]==0)
        {
            P4[2]='0';
            P4[3]='0';
        }
        else if (s1[row][column]==1)
        {
            P4[2]='0';
            P4[3]='1';
        }
        else if (s1[row][column]==2)
        {
            P4[2]='1';
            P4[3]='0';
        }else if (s1[row][column]==3)
        {
            P4[2]='1';
            P4[3]='1';
        }
        P4[0]=(""+(Integer.parseInt(""+P4[0]) ^ Integer.parseInt(""+L[0]))).charAt(0);
        P4[1]=(""+(Integer.parseInt(""+P4[1]) ^ Integer.parseInt(""+L[1]))).charAt(0);
        P4[2]=(""+(Integer.parseInt(""+P4[2]) ^ Integer.parseInt(""+L[2]))).charAt(0);
        P4[3]=(""+(Integer.parseInt(""+P4[3]) ^ Integer.parseInt(""+L[3]))).charAt(0);

        char temp[]=(new String(P4)+new String(R)).toCharArray();
        char ct[]=new char[temp.length];//Inverse permutation function will be applied here
        ct[0]=temp[3];
        ct[1]=temp[0];
        ct[2]=temp[2];
        ct[3]=temp[4];
        ct[4]=temp[6];
        ct[5]=temp[1];
        ct[6]=temp[7];
        ct[7]=temp[5];

        return (new String(ct));


    }
    static void generateKeys(String key)
    {
        char K[]=key.toCharArray();
        char PermutedKey1[]=new char[K.length];
        PermutedKey1[1-1]=K[3-1];
        PermutedKey1[2-1]=K[5-1];
        PermutedKey1[3-1]=K[2-1];
        PermutedKey1[4-1]=K[7-1];
        PermutedKey1[5-1]=K[4-1];
        PermutedKey1[6-1]=K[10-1];
        PermutedKey1[7-1]=K[1-1];
        PermutedKey1[8-1]=K[9-1];
        PermutedKey1[9-1]=K[8-1];
        PermutedKey1[10-1]=K[6-1];

        //System.out.println(new String(PermutedKey1));

        char temp=PermutedKey1[0];
        PermutedKey1[0]=PermutedKey1[1];
        PermutedKey1[1]=PermutedKey1[2];
        PermutedKey1[2]=PermutedKey1[3];
        PermutedKey1[3]=PermutedKey1[4];
        PermutedKey1[4]=temp;

        temp=PermutedKey1[5];
        PermutedKey1[5]=PermutedKey1[6];
        PermutedKey1[6]=PermutedKey1[7];
        PermutedKey1[7]=PermutedKey1[8];
        PermutedKey1[8]=PermutedKey1[9];
        PermutedKey1[9]=temp;

        //System.out.println(new String(PermutedKey1));

        char PermutationKey2[]=new char[8];

        PermutationKey2[0]=PermutedKey1[5];
        PermutationKey2[1]=PermutedKey1[2];
        PermutationKey2[2]=PermutedKey1[6];
        PermutationKey2[3]=PermutedKey1[3];
        PermutationKey2[4]=PermutedKey1[7];
        PermutationKey2[5]=PermutedKey1[4];
        PermutationKey2[6]=PermutedKey1[9];
        PermutationKey2[7]=PermutedKey1[3];

        //System.out.println(new String(PermutationKey2));//k1
        key1=new String(PermutationKey2);


        temp=PermutedKey1[0];

        PermutedKey1[0]=PermutedKey1[1];
        PermutedKey1[1]=PermutedKey1[2];
        PermutedKey1[2]=PermutedKey1[3];
        PermutedKey1[3]=PermutedKey1[4];
        PermutedKey1[4]=temp;

        temp=PermutedKey1[5];
        PermutedKey1[5]=PermutedKey1[6];
        PermutedKey1[6]=PermutedKey1[7];
        PermutedKey1[7]=PermutedKey1[8];
        PermutedKey1[8]=PermutedKey1[9];
        PermutedKey1[9]=temp;

        temp=PermutedKey1[0];

        PermutedKey1[0]=PermutedKey1[1];
        PermutedKey1[1]=PermutedKey1[2];
        PermutedKey1[2]=PermutedKey1[3];
        PermutedKey1[3]=PermutedKey1[4];
        PermutedKey1[4]=temp;

        temp=PermutedKey1[5];
        PermutedKey1[5]=PermutedKey1[6];
        PermutedKey1[6]=PermutedKey1[7];
        PermutedKey1[7]=PermutedKey1[8];
        PermutedKey1[8]=PermutedKey1[9];
        PermutedKey1[9]=temp;

        //System.out.println(new String(PermutedKey1));

        PermutationKey2[0]=PermutedKey1[5];
        PermutationKey2[1]=PermutedKey1[2];
        PermutationKey2[2]=PermutedKey1[6];
        PermutationKey2[3]=PermutedKey1[3];
        PermutationKey2[4]=PermutedKey1[7];
        PermutationKey2[5]=PermutedKey1[4];
        PermutationKey2[6]=PermutedKey1[9];
        PermutationKey2[7]=PermutedKey1[8];

        //System.out.println(new String(PermutationKey2));//k2

        key2=new String(PermutationKey2);
       // System.out.println(key1);
        //System.out.println(key2);














    }
}
