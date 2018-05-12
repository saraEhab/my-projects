function [root,noOfIterations,epslon_a,time]= NewtonMethod(strf,x0,iter_max,es,next)
tic;
if(next~=0)
    iter_max=next;
end
file= fopen('print data.txt','w');
if(iter_max<0||es<0)
    fprintf(file,'there is wrong in input\n');
      fclose(file);
       root=0
        noOfIterations=0
        epslon_a=1000
        time=0
    return;
end
fun=evalin(symengine,strf);
dfun= diff(fun);
syms x;
xt(1)=x0;
f(1)=vpa(subs(fun,x,xt(1)));
df(1)=vpa(subs(dfun,x,xt(1)));
if(df(1)==0)
  fprintf(file,'there is infinte loop\n');
  fclose(file);
  root=0
        noOfIterations=0
        epslon_a=1000
        time=0
  return
end ;

Ea(1)=0;
eap(1)=0;
for i=2:iter_max
    xt(i)=xt(i-1)-f(i-1)/df(i-1);
        f(i)=vpa(subs(fun,x,xt(i)));
        df(i)=vpa(subs(dfun,x,xt(i)));
        if(df(i)==0)
          fprintf(file,'there is infinte loop\n');
            fclose(file);
            root=0
        noOfIterations=0
        epslon_a=1000
        time=0
          return
        end 
        Ea(i)=abs(xt(i)-xt(i-1));
        eap(i)=abs(Ea(i)/xt(i))*100;
        if(abs(xt(i)-xt(i-1))<es)
                 fprintf(file,'newton raphson will converge\n');
                 break;
        end 
        if(f(i)==0)
                 fprintf(file,'newton raphson will converge\n');
                 break;
        end 
end
if(i>=iter_max)
     fprintf(file,'newton raphson not reach the tolerance\n');   
end
n=length(xt);
    k=1:n;
     fprintf(file,'   it               x                       f(x)                         Ea                 ea');
     fprintf(file,'\n');
       out=[k;xt;f;Ea;eap];
       time =toc;
        l=[xt(n)-5 xt(n)+5];
          u=-4:4;

    fprintf(file,'%5.0f      %20.14f     %20.14f      %20.14f      %20.14f\n',out); 
    v=df(n)*(u-xt(n))+f(n);
 root=xt(n);
epslon_a=eap(n);
noOfIterations=n;

  % fprintf('the time excution : %20.6f',time);
    %byms7 l adem w y3ml save ll values l gdeda
   save('plotData.txt','fun','u','v')
   %  fplot(fun,l);
   fclose(file)
end

    

    