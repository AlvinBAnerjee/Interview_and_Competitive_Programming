package _18_DP;

import java.util.Arrays;

public class __17_Evaluate_Boolean_Expression {
    public static void main(String[] args) {
        char symbol[]={'T','T','F','T'};
        char operator[]={'|','&','^'};
        __17_Evaluate_Boolean_Expression ob=new __17_Evaluate_Boolean_Expression();
        ob.solve(symbol,operator);
        //System.out.println(ob.solveRecursive("T|F^F&T|F^F^F^T|T&T^T|F^T^F&F^T|T^F",0,35,1));
        System.out.println(ob.solveDPTopDown("T|F^F&T|F^F^F^T|T&T^T|F^T^F&F^T|T^F"));
    }
    public int solve(char symbol[],char operator[])
    {
        String expression="";
        for (int i=0;i<operator.length;i++)
        {
            expression=expression+symbol[i]+operator[i];
        }
        expression=expression+symbol[symbol.length-1];
        System.out.println(expression);
        return -1;
    }
    public int solveRecursive(String Expression,int i,int j,int isTrue)
    {
        if (i>j)
            return 0;
        if (i==j)
        {
            if (Expression.charAt(i)=='T')
            {
                if (isTrue==1)
                    return 1;
                else
                    return 0;
            }
            else
            {
                if (isTrue==0)
                    return 1;
                else
                    return 0;
            }
        }
        int total=0;
        for (int k=i+1;k<=j-1;k=k+2)
        {
            int leftTrue=solveRecursive(Expression,i,k-1,1);
            int leftFalse=solveRecursive(Expression,i,k-1,0);
            int rightTrue=solveRecursive(Expression,k+1,j,1);
            int rightFalse=solveRecursive(Expression,k+1,j,0);

            if (Expression.charAt(k)=='&')
            {
                if (isTrue==1)
                {
                    total=total+leftTrue*rightTrue;
                }
                else
                {
                    total=total+leftTrue*rightFalse+leftFalse*rightTrue+leftFalse*rightFalse;
                }
            }
            else if (Expression.charAt(k)=='|')
            {
                if (isTrue==1)
                {
                    total=total+leftTrue*rightFalse+leftFalse*rightTrue+leftTrue*rightTrue;
                }
                else
                {
                    total=total+leftFalse*rightFalse;
                }
            }
            else//^
            {
                if (isTrue==1)
                {
                    total=total+leftTrue*rightFalse+leftFalse*rightTrue;
                }
                else
                {
                    total=total+leftFalse*rightFalse+leftTrue*rightTrue;
                }
            }
        }
        return total;
    }
    public int solveDPTopDown(String Expression)
    {
        int dp[][][]=new int[Expression.length()+1][Expression.length()+1][2];
        for (int arr[][]: dp)
        {
            for (int x[]:arr)
            {
                Arrays.fill(x,-1);
            }
        }
        return solveDPTopDownUtil(Expression,0,Expression.length()-1,1,dp);
    }
    public int solveDPTopDownUtil(String Expression,int i,int j,int isTrue, int dp[][][])
    {

        if (i>j)
        {
            return 0;
        }
        if (i==j)
        {
            if (isTrue == 1)
            {
                return (Expression.charAt(i) == 'T') ? 1 : 0;
            }
            else
            {
                return (Expression.charAt(i) == 'F') ? 1 : 0;
            }
        }
        if (dp[i][j][isTrue] != -1)
            return dp[i][j][isTrue];

        int total=0;
        int leftTrue,leftFalse,rightTrue,rightFalse;

        for (int k=i+1;k<=j-1;k=k+2)
        {


            if (dp[i][k-1][1]!=-1)
             leftTrue=dp[i][k-1][1];
            else
                leftTrue=solveDPTopDownUtil(Expression,i,k-1,1,dp);


            if (dp[i][k-1][0]!=-1)
                leftFalse=dp[i][k-1][0];
            else
                leftFalse=solveDPTopDownUtil(Expression,i,k-1,0,dp);


            if(dp[k+1][j][1]!=-1)
                rightTrue=dp[k+1][j][1];
            else
                rightTrue=solveDPTopDownUtil(Expression,k+1,j,1,dp);


            if (dp[k+1][j][0]!=-1)
                 rightFalse=dp[k+1][j][0];
            else
                rightFalse=solveDPTopDownUtil(Expression,k+1,j,0,dp);



            if (Expression.charAt(k)=='&')
            {
                if (isTrue == 1)
                {
                    total
                            = total + leftTrue * rightTrue;
                }
                else
                {
                    total = total
                            + leftTrue * rightFalse
                            + leftFalse * rightTrue
                            + leftFalse * rightFalse;
                }
            }
            else if (Expression.charAt(k)=='|')
            {
                if (isTrue == 1)
                {
                    total = total
                            + leftTrue * rightTrue
                            + leftTrue * rightFalse
                            + leftFalse * rightTrue;
                }
                else
                {
                    total
                            = total + leftFalse * rightFalse;
                }
            }
            else if(Expression.charAt(k)=='^')//^
            {
                if (isTrue == 1)
                {
                    total = total
                            + leftTrue * rightFalse
                            + leftFalse * rightTrue;
                }
                else
                {
                    total = total
                            + leftTrue * rightTrue
                            + leftFalse * rightFalse;
                }
            }
            else
                System.out.println("shit");
            dp[i][j][isTrue] = total;
        }
        dp[i][j][isTrue]=total;
        return total;

    }
}
