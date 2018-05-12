function time=gauess_jorden(eqns)
%input:
%A:coeffitions matrix
%B:eqns resultMatrix
%output:
%x:result of each variable
%A=[1 1 2;-1 -2 3;3 7 4];
%B=[8;1;10];
%eqns = 'x1+x2+2*x3=8, -x1-2*x2+3*x3=1, 3*x1+7*x2+4*x3 = 10';
tic;
file= fopen('print data.txt','w');
[A,B]=convertEqu(eqns);
c=[A,B];
siz=size(c);
length=siz(1);
x=zeros(length,1);
width=siz(2);
    for (i=1:length)
        for(j=1:length)
            if i~=j
                c(j,:)=c(j,:)-c(i,:).*c(j,i)./c(i,i);
            end
        end
    end
    for (i=1:length)
        x(i)=c(i,width)/c(i,i);
    end
    fprintf(file,'         xi:\n');
    for i=1:size(x)
        fprintf(file,'%20.14f\n',x(i));
    end
    time=toc;     
end