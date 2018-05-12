function [queryResult,Elapsedtime,fun] =newtonInterpolation(xi,yi,n,query)
file= fopen('print data.txt','w'); 
tstart=tic;
matrix=zeros(n+1,n);
for i=2:n+1
    matrix(i,1)=(yi(i)-yi(i-1))/(xi(i)-xi(i-1));
end

for j=2:n
    for i=j+1:n+1
        matrix(i,j)=(matrix(i,j-1)-matrix(i-1,j-1))/(xi(i)-xi(i-j));
    end
end

syms f(x);
f(x)=yi(1);
fprintf(file,'f(x)= %s\n',char(f))
col=1;
for j=1:n
    syms g(x);
    g(x)=matrix(j+1,col);
    for i=1:j
        g(x)=(x-xi(i))*(g(x));
    end
    f(x)=g(x)+f(x);
fprintf(file,'f(x)= %s\n',char(f))
    col=col+1;
end
fun=f(x);
fun=simplify(fun);
fprintf(file,'f(x)= %s\n',char(fun))

for i=1:length(query)
    queryResult(i)=f(query(i));
end
Elapsedtime=toc(tstart);
firstx=xi(1)
endx=xi(n+1)
save('plotDataPart2.txt','f','firstx','endx');
fclose(file)
end