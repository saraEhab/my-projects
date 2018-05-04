#ifndef SCAN_H_INCLUDED
#define SCAN_H_INCLUDED
#include <windows.h>
int scan(int r,int c){
    HANDLE  hConsole;
  hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
int i,j=0,flag=1;
  char str1[256],str2[256];
   while(flag){
    i=0;
   gets(str1);/* string takes the user inputs' even if they aren't numbers*/
   while(str1[i]!='\0'){
    if((str1[i]<'0'||str1[i]>'9')&& !(str1[i]==' ')){/*check that str1 consist of numbers and if not the user will enter it again*/
        flag=1;
        SetConsoleTextAttribute(hConsole, 12);
   printf("\nPlease, Enter a number between %d & %d : ",r,c);
    break;
   }
   else{
    flag=0;}
   i++;}
   if(flag)
    continue;
    i=0;
   j=0;
   while(str1[i]!='\0'){
   if(str1[i]>='0'&&str1[i]<='9'){
   str2[j]=str1[i];/*moving the digits to another string str2 which containing only digits from 0 to 9*/
   j++;/*j is referring to the number of digits in the number*/
   }
   i++;}
   }

   int n;
   if(j==1)/*if the number consists of one digit*/
   n=str2[0]-'0';/*convert the numbers from char to int by subtracting the char 0 from each digit*/
   else if(j==2)/*if the number consists of two digits*/
    n=str2[1]-'0'+10*(str2[0]-'0');/*n=the first int after subtracting 0 and multiply it with 10 then add the second digit after subtracting 0*/
    if(j>2 || n>r || n<c){
            SetConsoleTextAttribute(hConsole, 12);
        printf("\nPlease, Enter a number between %d & %d : ",r,c);
        n=scan(r,c);}
    return n;}

#endif // SCAN_H_INCLUDED
