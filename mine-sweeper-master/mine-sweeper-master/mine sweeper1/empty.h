#ifndef EMPTY_H_INCLUDED
#define EMPTY_H_INCLUDED

void empty(int n ,int m,int s,int q,char a[n][m],char b[n][m]){/*open the empty cells one after another until reaching a digit*/
int i,j;
    for(i=-1;i<=1;i++){
        for(j=-1;j<=1;j++){
            if(a[s+i][q+j]=='X'&&s+i>=0&&s+i<n&&q+j>=0&&q+j<m){

                if(b[s+i][q+j]==' '){
                    a[s+i][q+j]=' ';

                    empty(n,m,s+i,q+j,a,b);
                }
                else
                    a[s+i][q+j]=b[s+i][q+j];
            }
        }
    }
}


#endif // EMPTY_H_INCLUDED
