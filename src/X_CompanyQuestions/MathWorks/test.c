#include<stdio.h>
void main()
{
struct country{
char c;
float d;
};
struct world
{
int a[3];
char b;
struct country india;
};
struct world st={{1,2,3},'p', 'q', 1.4};

}