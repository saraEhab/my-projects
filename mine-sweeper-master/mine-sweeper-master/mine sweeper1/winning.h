#ifndef WINNING_H_INCLUDED
#define WINNING_H_INCLUDED
void wait ( float sec );
void winning(int n,int m,int totalTime,int moves){
 HANDLE  hConsole;
  hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
   double score;

   printf("\n\n\t\tEnter Your Name : ");
   char user[30];/*string including the user name*/
        gets(user);

        int i=0;
        while(user[i]!='\0'){/*convert the capital letters in the user name to small letters */
            if(user[i]>='A'&&user[i]<='Z'){
                user[i]+='a'-'A';
            }
            i++;
        }
        score=(pow(n,4) * pow(m,4))/(totalTime * moves);
        printf("\n\t\tYour Score : %.0lf\n\n",score);
FILE *name;/*file containing the users name*/
FILE *so;/*file containing the users scores*/
name=fopen("name.txt","r");
so=fopen("so.txt","r");
char *na[100];
int sos[100];
if(name!=NULL){
i=0;
for(i=0;i<100&&!(feof(name));i++){/*feof return 1 at the end of the file*/
    na[i] = malloc(100 * sizeof(char));/*reserve a place in the memory to use the array of strings na*/
    sos[i] = malloc(100 * sizeof(char));
    fscanf(name," %s",na[i]);/*scan the names from the file to the array of strings na*/
    fscanf(so," %d",&sos[i]);/*scan the scores from the file to the array sos*/
}
int j=i-1;
int flag=1;
i=0;
while(i<j){
  if(strstr( na[i] , user)!=NULL){/*searching for the user name in the array of strings na*/
    if(strcmp(na[i],user)==0){/*strcmp return 0 if na[i] and user are the same size*/
        flag=0;
  if((int)score>sos[i])/*comparing the current score with the previous score for the same user*/
    sos[i]=(int)score;
    break;
  }}
  i++;
}
if(flag==1){/*flag=1 means that it's the first time for the user to play the game*/
    na[i]=user;/*putting the user name in the array of strings*/
    sos[i]=(int)score;/*putting the user score in the array */
    j++;
}
flag=1;
int max=0,k,r,tmp1,tmp2;/*arrange the array of scores and the array of strings according to the scores from max to min*/
for(i=0;i<j;i++){
        flag=1;
        max=0;
    for(k=i;k<j;k++){
        if(sos[k]>max){
            max=sos[k];
            r=k;
            flag=0;
        }
    }
    if(flag==0){
    tmp1=sos[i];
    sos[i]=sos[r];
    sos[r]=tmp1;
    tmp2=na[i];
    na[i]=na[r];
    na[r]=tmp2;}
}
name=fopen("name.txt","w");/*rewrite the users name after arranging them*/
so=fopen("so.txt","w");/*rewrite the users scores after arranging them*/
i=0;
while(i<j){
    fprintf(name,"%s\n",na[i]);
    fprintf(so,"%d\n",sos[i]);
    i++;
}
}
else{
     name=fopen("name.txt","w");
so=fopen("so.txt","w");
    fprintf(name,"%s\n",user);
    fprintf(so,"%d\n",(int)score);
}
  fclose(name);
fclose(so);
SetConsoleTextAttribute(hConsole, 13);
printf("\t:::       ::: ::::::::::: ::::    :::\n"); wait(.25);
printf("\t:+:       :+:     :+:     :+:+:   :+:\n"); wait(.25);
printf("\t+:+       +:+     +:+     :+:+:+  +:+\n"); wait(.25);
printf("\t+#+  +:+  +#+     +#+     +#+ +:+ +#+\n"); wait(.25);SetConsoleTextAttribute(hConsole, 10);
printf("\t+#+ +#+#+ +#+     +#+     +#+  +#+#+#\n"); wait(.25);
printf("\t #+#+# #+#+#      #+#     #+#   #+#+#\n"); wait(.25);
printf("\t  ###   ###   ########### ###    ####\n"); wait(.25);
    SetConsoleTextAttribute(hConsole, 12);
printf("\n\n\t\tPress ENTER to continue");
getchar();
}

#endif // WINNING_H_INCLUDED
