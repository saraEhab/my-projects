function[roots,iterations,epslon,time]=secant(strfun,x0,x1,tol,maxIte,counter)
%inputs:
%fun         function
%x0        first initial guess of the root X(i-1)
%x1        seconed initial guess of the root X(i)
%tol       max tolerance that stop the iteration default value= 0.0001
%maxIte    max number of iteration allawed default value = 50 iterations
%pre       precision that used in calculation
%output:
%approximate root
file= fopen('print data.txt','w');
fun=str2func(['@(x)' strfun]);
i=1;
iterations=0;
roots=[];
error=[];
Xz=[];
Xz(i)=x0;
Xf=[];
Xf(i)=x1;
Fxz=[];
Fxf=[];
Fxa=[];
msg='';
tic;
%fx0=feval(fun,x0);
fx0=feval(fun,x0);
Fxz(i)=fx0;
fx1=feval(fun,x1);
Fxf(i)=fx1;
%calculate x from equation
xa=x1-((fx1*(x0-x1))/(fx0-fx1));
roots(i)=xa;
fxa=feval(fun,xa);
Fxa(i)=fxa;
relatErr=abs(xa-x1)/(abs(xa)+eps);
error(i)=relatErr;
iterations=iterations+1;
%start iterations
if(counter~=0)
    maxIte=counter;
end;
while (relatErr > tol)&& (i < maxIte)
    
    i=i+1;
    x0=x1;
    Xz(i)=x0;
    fx0=fx1;
    Fxz(i)=fx0;
    x1=xa;
    Xf(i)=x1;
    fx1=fxa;
    Fxf(i)=fx1;
    xa=x1-((fx1*(x0-x1))/(fx0-fx1));
    fxa=feval(fun,xa);
    Fxa(i)=fxa;
    relatErr=abs(xa-x1)/(abs(xa)+eps);
    roots(i)=xa;
    error(i)=relatErr;
    iterations=iterations+1;
end
    epslon=relatErr;

time = toc;
if (maxIte == i-1)
    fprintf(file,'secant method did not converge!\n');
     roots=0
        iterations=0
        epslon=1000
        time=0
          fclose(file);
    return;
else
   fprintf(file,'secant method converge!\n');
end
k=1:i;
fprintf(file,'    step      x0        f(x0)      x1       f(x1)      xa       f(xa)     acc');
fprintf(file,'\n');
for k=1:i;

    out=[k'                 Xz(k)'          Fxz(k)'               Xf(k)'          Fxf(k)'             roots(k)'      Fxa(k)'         error(k)'];
     fprintf(file,'%5.0f      %20.14f     %20.14f      %20.14f      %20.14f      %20.14f      %20.14f      %20.14f\n',out);  
end
    range=[x0-10,x0+10];
     SP=xa;
    SP0=x0;
    SP1=x1;
    save('plotData.txt','range','fun','SP','SP0','SP1');
    fclose(file)
    roots=roots(end)
end