#include<conio.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <windows.h>
#include "scan.h"
#include "print.h"
#include "empty.h"
#include "mine.h"
#include "losing.h"
#include "winning.h"
#include "open.h"
#include "save.h"
#include "gamePlay.h"
#include "wait.h"

int main()
{
    int k,g,c;
    HANDLE  hConsole;

  hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
  SetConsoleTextAttribute(hConsole, 9);

    int n,m,j,flag=1,flag1=1,flags=0,question=0,moves=0,totalTime=0;

     double score;
system("cls");
SetConsoleTextAttribute(hConsole, 10);
printf("\t                       t   EW:        ,ft       f#i \n"); wait(.25);
printf("\t            ..       : Ej  E##;       t#E     .E#t           \n"); wait(.25);
printf("\t           ,W,     .Et E#, E###t      t#E    i#W,           \n"); wait(.25);SetConsoleTextAttribute(hConsole, 11);
printf("\t          t##,    ,W#t E#t E#fE#f     t#E   L#D.           \n"); wait(.25);
printf("\t         L###,   j###t E#t E#t D#G    t#E :K#Wfff;        \n"); wait(.25);
printf("\t       .E#j##,  G#fE#t E#t E#t  f#E.  t#E i##WLLLLt         \n"); wait(.25);SetConsoleTextAttribute(hConsole, 13);
printf("\t      ;WW; ##,:K#i E#t E#t E#t   t#K: t#E  .E#L           \n"); wait(.25);
printf("\t     j#E.  ##f#W,  E#t E#t E#t    ;#W,t#E    f#E:       \n"); wait(.25);
printf("\t   .D#L    ###K:   E#t E#t E#t     :K#D#E     ,WW;        \n"); wait(.25);SetConsoleTextAttribute(hConsole, 14);
printf("\t  :K#t     ##D.    E#t E#t E#t      .E##E      .D#;        \n"); wait(.25);
printf("\t  ...      #G      ..  E#t ..         G#E        tt       \n"); wait(.25);
printf("\t           j           ,;.             fE                   \n"); wait(.25);

printf("        .                          ,;        ,;                  ,; \n"); wait(.25);
printf("        ;W                        f#i       f#i t               f#i j.\n"); wait(.25);
printf("       f#E          ;           .E#t      .E#t  ED.           .E#t  EW,\n"); wait(.25);SetConsoleTextAttribute(hConsole, 13);
printf("     .E#f         .DL          i#W,      i#W,   E#K:         i#W,   E##j\n"); wait(.25);
printf("    iWW;  f.     :K#L     LWL L#D.      L#D.    E##W;       L#D.    E###D.\n"); wait(.25);
printf("   L##LffiEW:   ;W##L   .E#f:K#Wfff;  :K#Wfff;  E#E##t    :K#Wfff;  E#jG#W;\n"); wait(.25);SetConsoleTextAttribute(hConsole, 11);
printf("  tLLG##L E#t  t#KE#L  ,W#; i##WLLLLt i##WLLLLt E#ti##f   i##WLLLLt E#t t##f\n"); wait(.25);
printf("    ,W#i  E#t f#D.L#L t#K:   .E#L      .E#L     E#t ;##D.  .E#L     E#t  :K#E:\n"); wait(.25);
printf("   j#E.   E#jG#f  L#LL#G       f#E:      f#E:   E#ELLE##K:   f#E:   E#KDDDD###i\n"); wait(.25);SetConsoleTextAttribute(hConsole, 10);
printf(" .D#j     E###;   L###j         ,WW;      ,WW;  E#L;;;;;;,    ,WW;  E#f,t#Wi,,,\n"); wait(.25);
printf(",WK,      E#K:    L#W;           .D#;      .D#; E#t            .D#; E#t  ;#W:\n"); wait(.25);
printf("EG.       EG      LE.              tt        tt E#t              tt DWi   ,KK:\n"); wait(.25);


while(1){
 wait(.3);
fflush(stdin);
fflush(stdout);
SetConsoleTextAttribute(hConsole, 10);
printf("\n\n\t\t\t\t(1)START NEW GAME  ");
SetConsoleTextAttribute(hConsole, 11);
printf("\n\n\t\t\t\t(2)LOAD GAME ");
SetConsoleTextAttribute(hConsole, 13);
printf("\n\n\t\t\t\t(3)LEADERBOARD ");
SetConsoleTextAttribute(hConsole, 14);
printf("\n\n\t\t\t\t(4)EXIT ");
SetConsoleTextAttribute(hConsole, 10);
  printf("\n\n ENTER your action : ");
int h;/*h from 1 to 4; (1-start new game;2-load;3-leader board;4-exit)*/
h=scan(4,1);
if(h==1){/*start new game*/
int i=0;

     time_t start,end;
     SetConsoleTextAttribute(hConsole, 10);
    printf("\nThe numbers of rows : ");
    n=scan(30,2);/*n must be between 2 and 30*/
    printf("\nThe numbers of columns : ");
    m=scan(30,2);/*m must be between 2 and 30*/

    FILE *coordinates;
    coordinates=fopen("coordinates.txt","w");
    fprintf(coordinates,"%d %d",n,m);
    fclose(coordinates);
    char a[n][m];/* the array a of n rows and m columns is the x array*/
    for(i=0;i<n;i++){
        for(j=0;j<m;j++){
            a[i][j]='X';
        }
    }


        totalTime=0;/*at the beginning of the game*/

    print(n,m,flags,question,moves,totalTime,a);


   char b[n][m];

   for(i=0;i<n;i++){ /*initialize array b with zero*/
        for(j=0;j<m;j++){
            b[i][j]='0';
        }
    }
    while(flag1){
fflush(stdin);
        fflush(stdout);
        SetConsoleTextAttribute(hConsole, 10);
    /*g,c is the first point which the user chooses*/
    printf("Enter The first row number to open : ");
    g=scan(n-1,0);
    printf("\nEnter The first column number to open : ");
    c=scan(m-1,0);
    start = time(NULL);
    if(g<0||g>n||c<0||c>m){/*if g isn't between 0 and n-1*/
            SetConsoleTextAttribute(hConsole, 12);
            printf("error\nchoose another point:");
           continue;
    }
    flag1=0;
    }
    mine(g,c,n,m,b);/*distributing the mines according to the first point which couldn't be mine*/
        totalTime=0;
    moves=1;
   a[g][c]=b[g][c];
    if(a[g][c]==' ')
        empty(n,m,g,c,a,b);
for(i=0;i<n;i++){/*condition for winning*/
        for(j=0;j<m;j++){
                if(a[i][j]==b[i][j]||((a[i][j]=='F'||a[i][j]=='X')&&b[i][j]=='*')){
                    flag=0;
                }
                else{
                    flag=1;
                    break;
                }}
                if(flag==1)

                    break;}
		totalTime=1;
    if(flag==0){

    winning(n,m,totalTime,moves);
    print(n,m,flags,question,moves,totalTime,a);
    wait(2);
    main();
    }
         gamePlay(n,m,a,b, flags, question, moves, totalTime,start,end);


        }



   else if(h==2){      /*load*/
        int i,j;

    FILE *s;
    s=fopen("s.txt","r");
    if(s!=NULL){
    fscanf(s,"%d %d\n",&n,&m);
    fscanf(s,"%d %d %d %d\n",&totalTime,&flags,&question,&moves);
    char a[n][m],b[n][m],tp[m];
    for(i=0;i<n;i++){
        for(j=0;j<m;j++){
            fscanf(s,"%c ",&a[i][j] );
            if(a[i][j]=='0')
                a[i][j]=' ';
        }
    }
    fprintf(s,"\n");
    for(i=0;i<n;i++){
        for(j=0;j<m;j++){
            fscanf(s,"%c ",&b[i][j] );
            if(b[i][j]=='0')
                b[i][j]=' ';
        }
    }

    fclose(s);
 time_t start,end;
start = time(NULL);
   gamePlay(n,m,a,b,flags, question, moves, totalTime,start,end);}
   else{/*if the file s is empty it means that the user didn't save a game before */
            SetConsoleTextAttribute(hConsole, 12);

     printf("\t\t\t\t EMPTY");
        printf("\n\n\t\t\t\tPress ENTER to continue");
        getchar();
        main();
   }
   }
    else if(h==3){/*leader board*/

char user[30];
FILE *name;/*file contains the name of the users*/
FILE *so;/*file contains the score of the users*/
    name=fopen("name.txt","r");
    so=fopen("so.txt","r");
    if(name!=NULL){
        while(!feof(name)){
    fscanf(name," %s ",&user);
    fscanf(so," %lf ",&score);

SetConsoleTextAttribute(hConsole, 10);

    printf("\n\n\t\t%s ",user);
SetConsoleTextAttribute(hConsole, 11);

    printf("\t\t\t\t%.0lf ",score);

    }

fclose(name);
    fclose(so);
    SetConsoleTextAttribute(hConsole, 12);

    printf("\n\n\t\tPress ENTER to continue");
    getchar();
    //wait(2);
    main();}
    else{/*if the file name is empty it means that it's the first user to play this game and there isn't a leader board*/
                SetConsoleTextAttribute(hConsole, 12);

        printf("\t\t\t\t EMPTY");
        printf("\n\n\t\t\t\tPress ENTER to continue");
        getchar();
        main();
    }


    }
    else if(h==4){
        exit(0);
   }


}
   return(0);
}
