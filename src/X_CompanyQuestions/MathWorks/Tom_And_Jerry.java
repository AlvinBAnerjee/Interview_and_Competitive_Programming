package X_CompanyQuestions.MathWorks;

import java.util.*;

public class Tom_And_Jerry {
    /*
    3 3
0 1 0 1 0 1 0 2 2
1 1
     */
    static class Pair{
        int x;
        int y;
        Pair(int x, int y)
        {
            this.x=x;
            this.y=y;
        }
    }
    static  int visitedAll=0;

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        int maze[][]=new int[n][m];
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {
                maze[i][j]=sc.nextInt();
            }
        }
        int jerryx=sc.nextInt();
        int jerryy=sc.nextInt();
        System.out.println( minMoves(maze,jerryx, jerryy,n,m ));
    }
    public static int minMoves(int maze[][], int jerryx, int jerryy, int n, int m)
    {
        ArrayList<Pair> cheese=new ArrayList<>();
        cheese.add(new Pair(0,0));
        for(int i=0;i<n;i++)
        {
            for (int j=0;j<m;j++)
            {
                boolean x=(maze[i][j]==2)?true: false;
                if(!x)
                {
                    continue;
                }
                cheese.add(new Pair(i,j));
            }
        }
        int size=cheese.size();
        visitedAll=(1<<size)-1;
        int dist[][][]=new int[n][m][size];
        for(int i=0;i<size;i++)
        {
            setDist(dist,i, n,m,cheese, maze);
        }

        int dp[][]=new int[size][1024];
        for(int arr[]: dp)
            Arrays.fill(arr,-1);
        //Arrays.fill(dp,-1);
        int ans=tsp(0,1,jerryx, jerryy,cheese,maze,n,m, dist, dp);
        if(ans>=1000000000) return -1;
        return ans;
    }
    static void setDist(int dist[][][], int i, int n, int m,ArrayList<Pair> cheese,int maze[][])
    {
        for(int k=0;k<n;k++)
        {
            for(int j=0;j<m;j++)
            {
                dist[k][j][i]=1000000000;
            }
        }
        boolean visited[][]=new boolean[n][m];
        int pos[][]={{1,0},{0,1},{-1,0},{0,-1}};

        Queue<Pair> q=new LinkedList<>();
        int x=cheese.get(i).x;
        int y=cheese.get(i).y;
        q.add(new Pair(x, y));
        dist[x][y][i]=0;
        visited[x][y]=true;
        while(!q.isEmpty())
        {
            Pair top=q.remove();
            x=top.x;
            y=top.y;
            for(int k=0;k<4;k++)
            {
                int a=x+pos[k][0];
                int b=y+pos[k][1];
                if(a>=0 && a<=n-1 && b>=0 && b<=m-1 && !visited[a][b] && maze[a][b]!=1)
                {
                    visited[a][b]=true;
                    dist[a][b][i]=dist[x][y][i]+1;
                    q.add(new Pair(a,b));
                }
            }
        }


    }

    static int tsp(int currCheese, int mask, int jerryx, int jerryy, ArrayList<Pair> cheese, int maze[][], int n, int m, int dist[][][], int dp[][])
    {
       // System.out.println(mask);
        if(mask==visitedAll)return dist[jerryx][jerryy][currCheese];
        if(dp[currCheese][mask]!=-1)return dp[currCheese][mask];;
        int min=Integer.MAX_VALUE;
        for(int i=0;i<cheese.size();i++)
        {
            if( ((mask>>i) &1 )==0)
            {
                int cx=cheese.get(i).x;
                int cy=cheese.get(i).y;
                min=Math.min(min, dist[cx][cy][currCheese]+ tsp(i, mask | (1<<i) ,jerryx, jerryy, cheese,maze,n,m, dist, dp));
            }
        }
        dp[currCheese][mask]=min;
        return  min;
    }

}
