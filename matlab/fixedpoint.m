function [xr,i,ea,Elapsedtime]=fixedPoint(str,x0,itr,err,next)
file= fopen('print data.txt','w');
tstart=tic;
str2=strcat(str,'+x');
g=str2func(['@(x)' str2]);    
xr=x0;
i=0;
xr_old=xr;
xr=feval(g,xr_old);
ea=100;
iteration=[]
while (ea>err && i<itr)
    
    xr_old=xr;
    xr=feval(g,xr_old);
    if(xr~=0)
        ea=abs((xr-xr_old)/xr)*100;
    end
    i=i+1;
    iteration(i)=i;
    xold(i)=xr_old;
    epsolll(i)=ea;
    rrr(i)=abs((xr-xr_old));
end


fprintf(file,'  iteration             x            absolute error                relative error\n');
out=[iteration;xold;rrr;epsolll]; 
 fprintf(file,'%5.0f      %20.14f     %20.14f      %20.14f     \n',out);  
n=length(xold);
 l=[xold(n)-5 xold(n)+5];
 u=[0 10];
    
j=2;
nLine=1;
x =[xold(nLine) xold(nLine)];
y =[0 xold(nLine+1)];
x1=x
y1=y
xdata=[]
ydata=[]
while(j<i)
    if(mod(j,2) )
        x =[xold(nLine) xold(nLine)];
        y =[xold(nLine) xold(nLine+1)];
    else
        x=[xold(nLine) xold(nLine+1)];
        y=[xold(nLine+1) xold(nLine+1)];
        nLine=nLine+1;
    end
        xdata(j-1,1)=x(1)
        xdata(j-1,2)=x(2)
         ydata(j-1,1)=y(1)
        ydata(j-1,2)=y(2)
    j=j+1;
end
 save('plotData.txt','g','l','u','x1','y1');
 xlswrite('xData.xlsx',xdata)
 xlswrite('yData.xlsx',ydata) 
Elapsedtime=toc(tstart);
  fclose(file);

end