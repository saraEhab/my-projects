#ifndef OPEN_H_INCLUDED
#define OPEN_H_INCLUDED

int open(int s,int q,int n,int m,char a[n][m],char b[n][m]){
    int i,j,flag=0;
    char counter='0';
    for(i=-1;i<=1;i++){
        for(j=-1;j<=1;j++){
            if(a[s+i][q+j]=='F'&&s+i>=0&&s+i<n&&q+j>=0&&q+j<m)/*counting the number of flags around the cell*/
                counter++;
        }
    }
    if(a[s][q]==counter){/*making sure the digit in the cell has same numbers of flags around it*/
         for(i=-1;i<=1;i++){
        for(j=-1;j<=1;j++){
                if(s+i>=0&&s+i<n&&q+j>=0&&q+j<m){
                if(a[s+i][q+j]!='F'&&b[s+i][q+j]=='*'){/*condition for losing if the user is putting a wrong flag*/
               a[s+i][q+j]= b[s+i][q+j]='!';
                flag=1;}
                else if(a[s+i][q+j]=='F'&&b[s+i][q+j]!='*')
                    b[s+i][q+j]='_';
        else if(a[s+i][q+j]!='F'){/*opening the cells around the selected cell if the flags are correct*/
            a[s+i][q+j]=b[s+i][q+j];
            if(a[s+i][q+j]==' ')
                empty(n,m,s+i,q+j,a,b);}
        }}
    }
    if(flag==1)
        losing(s,q,n,m,a,b);
}
    return flag;
}

#endif   // OPEN_H_INCLUDED
