function [queryResult,time,f] = lagrange (xi,yi,n,query)
file= fopen('print data.txt','w'); 
tic;
i = 1;
syms F(x)
F(x) = 0;
while i < n+2 
  syms L(x)
  L(x) = 1;
  j = 1;
  if j==i
        j = j+1;
  end
  while j < n+2
    L(x)=L(x)*((x - xi(j))/(xi(i) - xi(j)));
    j = j + 1;
     if j==i
        j = j+1;
      end
  end
   F(x)= F(x) + (L(x) * yi(i));
   fprintf(file,'F(x)=%s\n',char(F(x)));
   i = i + 1;
end
f = F(x);
f = simplify(f);
for i=1:length(query)
    queryResult(i)=F(query(i));
end
firstx=xi(1)
endx=xi(n+1)
save('plotDataPart2.txt','F','firstx','endx');
time = toc;
fclose(file)
end