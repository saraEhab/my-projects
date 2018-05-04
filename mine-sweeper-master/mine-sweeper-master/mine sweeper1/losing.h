#ifndef LOSING_H_INCLUDED
#define LOSING_H_INCLUDED
void wait ( float sec );


void losing(int s,int q,int n,int m,char a[n][m],char b[n][m]){
 HANDLE  hConsole;
  hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
    int i,j;
    for(i=0;i<n;i++){
        for(j=0;j<m;j++){
            if(a[i][j]=='F'){
                if(b[i][j]!='*'){
                b[i][j]='_';
                }
                else
                    b[i][j]='F';
            }
            if(a[i][j]=='X'){
                if(b[i][j]=='*')
                    b[i][j]='M';}}
            }
            SetConsoleTextAttribute(hConsole, 13);
printf("       ::::::::      :::     ::::    ::::  :::::::::: \n"); wait(.25);
printf("      :+:    :+:   :+: :+:   +:+:+: :+:+:+ :+:        \n"); wait(.25);
printf("      +:+         +:+   +:+  +:+ +:+:+ +:+ +:+        \n"); wait(.25);
printf("      :#:        +#++:++#++: +#+  +:+  +#+ +#++:++#   \n"); wait(.25);SetConsoleTextAttribute(hConsole, 10);
printf("      +#+   +#+# +#+     +#+ +#+       +#+ +#+        \n"); wait(.25);
printf("      #+#    #+# #+#     #+# #+#       #+# #+#        \n"); wait(.25);
printf("       ########  ###     ### ###       ### ########## \n"); wait(.25);

printf("\t ::::::::  :::     ::: :::::::::: :::::::::  \n"); wait(.25);
printf("\t:+:    :+: :+:     :+: :+:        :+:    :+: \n"); wait(.25);
printf("\t+:+    +:+ +:+     +:+ +:+        +:+    +:+ \n"); wait(.25);
printf("\t+#+    +:+ +#+     +:+ +#++:++#   +#++:++#:  \n"); wait(.25);SetConsoleTextAttribute(hConsole, 13);
printf("\t+#+    +#+  +#+   +#+  +#+        +#+    +#+ \n"); wait(.25);
printf("\t#+#    #+#   #+#+#+#   #+#        #+#    #+# \n"); wait(.25);
printf("\t ########      ###     ########## ###    ### \n"); wait(.25);

    SetConsoleTextAttribute(hConsole, 12);

printf("\n\n\t\tPress ENTER to continue");
getchar();


}


#endif // LOSING_H_INCLUDED
