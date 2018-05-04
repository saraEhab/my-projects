#ifndef MINE_H_INCLUDED
#define MINE_H_INCLUDED

void mine(int g,int c,int n ,int m, char b[n][m]){
 int nM,k,i,j;
 nM=1+( n * m )/10;/*calculate the number of mines*/
 time_t t;
   /* Intializing random number generator */
   srand((unsigned) time(&t));
   /* Print nM random numbers i from 0 to n & j from 0 to m */
   for( k = 0; k < nM ;)
   {
    i=rand()%n;
    j=rand()%m;
    if(b[i][j]=='*' || ( i==g && j==c)){ /*the first point can't be mine & no more than one mine in the same place*/
        continue;
    }
    else k++;
     b[i][j]='*';/* distributing the mine random & distributing the numbers around the mine*/
     if(b[i][j-1]=='*'){}
     else if ( j-1>=0)
        b[i][j-1]+=1;
    if(b[i][j+1]=='*'){}
     else if(j+1 <m)
        b[i][j+1]+=1;

    int d,r;
    for (d=-1;d<=1;d+=2){
        for (r=-1;r<=1;r++){
    if(b[i+d][j+r]=='*'){}
     else if( i+d<n && i+d>=0 && j+r>=0 && j+r <m)
            b[i+d][j+r]+=1;}
        }
   }
   for( i = 0 ; i < n ; i++ )/*change zeros with empty places in array b*/
   {
       for(j=0;j<m;j++){
    if(b[i][j]=='0')
        b[i][j]=' ';
   }}
   FILE *fp;/*file shows the distributing of mines in array b*/
   int d,r;
   fp=fopen("mine.txt","w");
   fprintf(fp,"\n");
   for(d=0;d<n;d++){
    for(r=0;r<m;r++){
        fprintf(fp,"%c ",b[d][r]);
    }
    fprintf(fp,"\n");
   }
   fclose(fp);
}

#endif // MINE_H_INCLUDED
