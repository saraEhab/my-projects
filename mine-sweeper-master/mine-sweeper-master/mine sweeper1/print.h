#ifndef PRINT_H_INCLUDED
#define PRINT_H_INCLUDED
#include <windows.h>
void print(int x,int y,int flags,int question,int moves,int totalTime,char c[x][y]){
    system("cls");
    int i,j,k;
    HANDLE  hConsole;
    //int k;

  hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
    printf("\n\n");
    SetConsoleTextAttribute(hConsole, 2);
    printf("Flags: ");
    SetConsoleTextAttribute(hConsole, 14);
    printf("%d",flags);
    SetConsoleTextAttribute(hConsole, 2);
    printf("    questions: ");
    SetConsoleTextAttribute(hConsole, 14);
    printf("%d",question);
    SetConsoleTextAttribute(hConsole, 2);
    printf("    Moves: ");
    SetConsoleTextAttribute(hConsole, 14);
    printf("%d",moves);
    SetConsoleTextAttribute(hConsole, 2);
    printf("   Time: ");
    SetConsoleTextAttribute(hConsole, 14);
    printf("%d:%d\n\n\t",totalTime/60,totalTime%60);
    for(k=0;k<y;k++){
        SetConsoleTextAttribute(hConsole, 5);
        if(k<10)
        printf("  %d ",k);
        else
        printf(" %d ",k);

}
        printf("\n");
    for(i=0;i<x;i++){
        printf("\t ");
    for(k=0;k<y;k++){
            SetConsoleTextAttribute(hConsole, 11);

        printf("___ ");}
        if(i<10){
                SetConsoleTextAttribute(hConsole, 5);
            printf("\n\n      %d ",i);}
                        else{
                    SetConsoleTextAttribute(hConsole, 5);
                printf("\n\n     %d ",i);}
        for(j=0;j<y;j++){
                SetConsoleTextAttribute(hConsole, 11);
                printf("| ");
                if(c[i][j]=='X'){
                SetConsoleTextAttribute(hConsole, 1);
                printf("%c ",c[i][j]);
                }
                else if(c[i][j]=='F'||c[i][j]=='?'){
        SetConsoleTextAttribute(hConsole, 13);
        printf("%c ",c[i][j]);}
        else if(c[i][j]=='_'||c[i][j]=='M'){
     SetConsoleTextAttribute(hConsole, 4);
        printf("%c ",c[i][j]);}
        else if(c[i][j]=='!'){
            SetConsoleTextAttribute(hConsole, 12);
        printf("%c ",c[i][j]);}
        else{
                SetConsoleTextAttribute(hConsole, 15);
             printf("%c ",c[i][j]);}
        }
        SetConsoleTextAttribute(hConsole, 11);
                printf("| ");

                printf("\n");
        }
        printf("\t ");
            for(k=0;k<y;k++){
                    SetConsoleTextAttribute(hConsole, 11);
                printf("___ ");}


printf("\n\n");
}

#endif // PRINT_H_INCLUDED
