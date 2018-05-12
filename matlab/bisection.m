function[xrnew,counter,epslon_a,time] = bisection(strf,xl,xu,max_it,es,iterate)
file= fopen('print data.txt','w');
tic;  
if(iterate~=0)
    max_it=iterate;
 end   
syms x;
    equation=evalin(symengine,strf);
    xrnew = 0;
    upperResult = subs(equation,x,xu);
    ur = upperResult;
    lowerResult = subs(equation,x,xl);
    lr = lowerResult;
    plotxl=[xl xl];
    plotyl=[lowerResult upperResult];
    plotxu =[xu xu];
    plotyu =[upperResult lowerResult];
    counter=0;
    initialCondition = upperResult*lowerResult;
    if (str2num(char(initialCondition)) > 0.0)
   % if (initialCondition > 0.0)
        fprintf(file,'function has the same sign at the end points');
        xrnew=0;
        counter=0;
        epslon_a=1000;
        time=0;
        fclose(file);
		return;
    end
    
    while counter<max_it
        counter=counter+1;
        xrold = xrnew;
        L(counter)=xl;
        U(counter)=xu;
        upperResult = subs(equation,x,xu);
        lowerResult = subs(equation,x,xl);
        xrnew = ((xl + xu)/2);
        R(counter)=xrnew;
        Ea(counter)= (abs(xrnew-xrold));
        ea(counter)= (abs((xrnew-xrold)/xrnew))*100;
        epslon_a = ea(counter);
        accurecy = epslon_a;
        C(counter) = counter ;
        middleResult = subs(equation,x,xrnew);
        upperMiddleProduct = upperResult * middleResult;
        lowerMiddleProduct = lowerResult * middleResult;
        if str2num(char(upperMiddleProduct)) < str2num(char(lowerMiddleProduct)) 
            xl = xrnew;
        else 
            xu = xrnew;
        end
        if(accurecy<es)
                 break; 
        end
    end
    save('plotData.txt','lr','ur','equation','plotxl','plotyl','plotxu','plotyu','counter')
    xlswrite('RData.xlsx',R)
    time= toc;
    fprintf(file,'   it               xl                       xu                 xr             Ea                 ea\n');
    iterations = [C;L;U;R;Ea;ea];
    fprintf(file,' %5.0f       %20.14f          %20.14f            %20.14f         %20.14f         %20.14f  \n ', iterations);
fclose(file);
end