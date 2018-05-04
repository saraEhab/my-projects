#ifndef SAVE_H_INCLUDED
#define SAVE_H_INCLUDED

void save(int n,int m,char a[n][m],char b[n][m],int totalTime,int flags,int question,int moves){


    FILE *s;
    s=fopen("s.txt","w");
    fprintf(s,"%d %d \n",n,m);/*print the coordinates in the file*/
    fprintf(s,"%d %d %d %d\n",totalTime,flags,question,moves);/*print the totalTime,flags,question and moves in the file*/
    int i,j;
    for(i=0;i<n;i++){/*print array a in the file*/
     for(j=0;j<m;j++){
        fprintf(s,"%c ",a[i][j] );
     }
     fprintf(s,"\n");
    }
         fprintf(s,"\n");

     for(i=0;i<n;i++){/*print array b in the file*/
     for(j=0;j<m;j++){
        fprintf(s,"%c ",b[i][j] );
     }
     fprintf(s,"\n");
    }
    fclose(s);


}


#endif // SAVE_H_INCLUDED
