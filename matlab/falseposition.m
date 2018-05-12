function [root,noOfIterations,epslon_a,time]= falseposition(strf,xl,xu,iter_max,es,next)
tstart=tic; 
file= fopen('print data.txt','w'); 
if(next~=0)
    iter_max=next;
 end
   syms x;
   lr=[]
   ur=[]
    equation=evalin(symengine,strf);
    xrnew = 0;
    upperResult = vpa(subs(equation,x,xu));
    lowerResult = vpa(subs(equation,x,xl));
    counter=0;
    plotx =[xl xl xu xu];
    ploty =[0.0 lowerResult upperResult 0.0];
    initialCondition = upperResult*lowerResult;
    if initialCondition>0.0
        fprintf(file,'function has the same sign at the end points');
 root=0
        noOfIterations=0
        epslon_a=1000
        time=0
        fclose(file);
        return
    end
    xt(1)=0;
    xll(1)=xl;
    xuu(1)=xu;
    Ea(1)=100;
    eap(1)=100;
    while counter<iter_max 
        counter=counter+1;
        xrold = xrnew;
        upperResult = vpa(subs(equation,x,xu));
        ur(counter)=upperResult;
        lowerResult = vpa(subs(equation,x,xl));
        lr(counter)=lowerResult;
        xrnew = ((xl * upperResult) - (xu * lowerResult)) / (upperResult - lowerResult);
        xll(counter)=xl;
        xuu(counter)=xu;
        xt(counter)= xrnew;
        middleResult = vpa(subs(equation,x,xrnew));
        accurecy= abs(xrnew-xrold);
        Ea(counter)= accurecy;
        eap(counter)=abs(accurecy/xrnew)*100;
        upperMiddleProduct = upperResult * middleResult;
        lowerMiddleProduct = lowerResult * middleResult;
        if upperMiddleProduct < lowerMiddleProduct 
            xl = xrnew;
        else 
            xu = xrnew;
        end
         if(eap(counter)<es)
            fprintf(file,'falseposition will converge');
                 break; 
        end
    end
if(counter>=iter_max)
     fprintf(file,'falseposition not reach the tolerance');   
end
    n=length(xt);   
    k=1:n;
     fprintf(file,'   it               xl                       xu                      xr                        Ea                             ea\n');
       out=[k;xll;xuu;xt;Ea;eap];
       time =toc(tstart);
    fprintf(file,'%5.0f    %20.14f     %20.14f      %20.14f     %20.14f        %20.14f\n',out); 
 root=xt(n);
epslon_a=eap(n);
noOfIterations=n;

   xlswrite('xllData.xlsx',xll)
   xlswrite('xuuData.xlsx',xuu)
   xlswrite('lrData.xlsx',lr)
   xlswrite('urData.xlsx',ur)
   save('plotData.txt','n','equation','plotx','ploty')

fclose(file)
end