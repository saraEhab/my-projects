function [root,noOfIterations,epslon_a,time] = BirgeVieta(strf,x0,iter_max,es,next)
tic;
if(next~=0)
    iter_max=next;
end
file = fopen('print data.txt','w');
if(iter_max<0||es<0)
    fprintf(file,'there is wrong in input\n');
     root=0
        noOfIterations=0
        epslon_a=1000
        time=0
          fclose(file);
    return;
end
fun=evalin(symengine,strf);
dfun= diff(fun);
syms x;
a = coeffs(fun);
a = fliplr(a);
if(length(a)==1)
     fprintf(file,'error in equation\n'); 
     root=0
        noOfIterations=0
        epslon_a=1000
        time=0
        fclose(file);
     return;
end
xt(1)=x0;
n=length(a);
Ea(1)=0;
eap(1)=0;
for i=2:iter_max
   b(1)=a(1);
   for j=2 : n
       b(j)=a(j)+xt(i-1)*b(j-1);
   end 
     c(1)=b(1);
   for j=2 : n-1
       c(j)=b(j)+xt(i-1)*c(j-1);
   end
   if(c(n-1)==0)
       fprintf(file,'there is infinte loop\n'); 
       root=0
        noOfIterations=0
        epslon_a=1000
        time=0
        fclose(file);
       return
   end
   xt(i)=xt(i-1)-(b(n)/c(n-1));
   
        Ea(i)=abs(xt(i)-xt(i-1));
        eap(i)=abs(Ea(i)/xt(i))*100;
        if(abs(xt(i)-xt(i-1))<es)
                 fprintf(file,'the method will converge\n');
                 break;
        end 
        if(b(n)==0)
             fprintf(file,'the method will converge\n');
                 break;
        end 
end    
time =toc;
if(i>=iter_max)
     fprintf(file,'this method not reach the tolerance\n');   
end

n=length(xt);
    k=1:n;
     fprintf(file,'   it               x                                   Ea                 ea');
     fprintf(file,'\n');
       out=[k;xt;Ea;eap];
        l=[xt(n)-5 xt(n)+5];
          u=-4:4;
 
    fprintf(file,'%5.0f      %20.14f         %20.14f      %20.14f\n',out);
      v=vpa(subs(dfun,x,xt(n)))*(u-xt(n))+vpa(subs(fun,x,xt(n)));
 root=xt(n);
epslon_a=eap(n);
noOfIterations=n;


    save('plotData.txt','fun','u','v')
% fplot(fun,l);
fclose(file);
end

    