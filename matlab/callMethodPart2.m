function [queryResult,time,fun] =  callMethodPart2(methodNumber,xi,yi,n,query)
 if methodNumber==1
        %lagranga
        [queryResult,time,fun] = lagrange (xi,yi,n,query)
    elseif methodNumber==2
        %newton
       [queryResult,time,fun] =newtonInterpolation(xi,yi,n,query)
 end
end