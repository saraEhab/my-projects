#ifndef GAMEPLAY_H_INCLUDED
#define GAMEPLAY_H_INCLUDED

void gamePlay(int n,int m,char a[n][m],char b[n][m],int flags,int question,int moves,int totalTime,time_t start,time_t end){
 print(n,m,flags,question,moves,totalTime,a);
 int t=totalTime;
int flag=1;
int k;
 HANDLE  hConsole;
  hConsole = GetStdHandle(STD_OUTPUT_HANDLE);
  k=9;
  SetConsoleTextAttribute(hConsole, k);

   while(flag){/*scan the position and the order*/
   int s,q;
   char p;/*string of actions*/
SetConsoleTextAttribute(hConsole, 3);
     printf("\n(O) To open the cell\t\t(F) To put a flag\n\n(?) To put a question mark\t(U) To unmark"
            "\n\n\t\t(S) For save");
            SetConsoleTextAttribute(hConsole, 10);
    printf("\n\nEnter Your action : ");

    fflush(stdin);
    while (scanf(" %c",&p)&&(getchar()!='\n'))
    {
        fflush(stdin);
        SetConsoleTextAttribute(hConsole, 12);
        printf("\n\nchoose one between (O) & (F) & (?) & (U) & (S) : ");
    }

   int i=0,j=0;
   while(p!='F'&&p!='f'&&p!='O'&&p!='o'&&p!='?' &&p!='U'&&p!='u'&&p!='s'&&p!='S'){
        SetConsoleTextAttribute(hConsole, 12);
        printf("\n\nchoose one between (O) & (F) & (?) & (U) & (S) : ");
        fflush(stdin);
        scanf(" %c",&p);
        }
        fflush(stdin);
        if(p=='s'||p=='S'){
                end = time(NULL);
		totalTime=t+difftime(end, start);/*t at the beginning of the game ==0 if the user save the game then load it t will equal the previous time*/
		int i,j;
		for(i=0;i<n;i++){/*making the empty places ==0 to save*/
            for(j=0;j<m;j++){
                if(a[i][j]==' ')
                    a[i][j]='0';
                if(b[i][j]==' ')
                    b[i][j]='0';
            }
		}
        save(n,m,a,b,totalTime,flags,question,moves);
        main();

    }
    SetConsoleTextAttribute(hConsole, 10);
    printf("\n\nEnter The next row number : ");
    s=scan(n-1,0);/*s must be between n-1 &0*/
    printf("\nEnter The next column number : ");
    q=scan(m-1,0);/*q must be between m-1 &0*/

    if(p=='F'||p=='f'){
        if(a[s][q]=='X'){
        a[s][q]='F';
        flags++;}
        else
            moves--;}
    else if(p=='?'){
        if(a[s][q]=='X'){
        a[s][q]='?';
        question++;}
        else
            moves--;}
    else if(p=='U'||p=='u'){/*u refers to unmark*/
        if(a[s][q]=='F'||a[s][q]=='f')
            flags--;
        else if(a[s][q]=='?')
        question--;
        if(a[s][q]=='F'|| a[s][q]=='?')
        a[s][q]='X';
        else
            moves--;
        }
    else if(p=='O'||p=='o'){
        if(a[s][q]=='F'){/*if the user wants to open a cell marked with flag*/
                SetConsoleTextAttribute(hConsole, 10);
            printf("\n\nDo you want to open it? Y/N\n");
            char z;/*z refers to yes or no*/
            int flag=1;
            while (scanf(" %c",&z)&&getchar()!='\n')
    {
        fflush(stdin);
        printf("\n\nEnter Y/N : ");
    }

            if (z=='Y'||z=='y'){
                flag=0;
                a[s][q]=b[s][q];
            }
                else if (z=='n'||z=='N'){
                    flag=0;
                    continue;
                }
                while(flag==1){
                        SetConsoleTextAttribute(hConsole, 12);
                    printf("\n\nEnter Y/N : ");
                    scanf(" %c",&z);
                    if (z=='Y'||z=='y'){
                flag=0;
                a[s][q]=b[s][q];
            }
                else if (z=='n'||z=='N'){
                    flag=0;

                }
                }
        }
    if(a[s][q]!='X')
        if(open(s,q,n,m,a,b))
        break;
           if(b[s][q]=='*'){/*condition for losing*/
                b[s][q]='!';
        losing(s,q,n,m,a,b);
            break;
            }
    else
        a[s][q]=b[s][q];
        if(a[s][q]==' '){
            empty(n,m,s,q,a,b);
        }

        }

 moves++;
        end = time(NULL);/*the time at the end when losing*/
		totalTime=t+difftime(end, start);/*calculate the total time after losing*/
    print(n,m,flags,question,moves,totalTime,a);
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

                    break;}}

    end = time(NULL);
		totalTime=t+difftime(end, start);
    if(flag==0){

    winning(n,m,totalTime,moves);
    print(n,m,flags,question,moves,totalTime,a);
    wait(2);
    main();
    }
    wait(2);
    print(n,m,flags,question,moves,totalTime,b);
        SetConsoleTextAttribute(hConsole, 12);

    printf("\n\n\t\tPress ENTER to continue");
    getchar();
    main();

}

#endif // GAMEPLAY_H_INCLUDED
