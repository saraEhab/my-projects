function plotPart2(methodNumber)
if methodNumber==1
    %lagrange
    data= load('plotDataPart2.txt','-mat','F','firstx','endx');
    F =getfield(data,'F');
    firstx=getfield(data,'firstx');
    endx=getfield(data,'endx');
    fplot(F,[firstx endx]);
else if methodNumber==2
        %newton
    data= load('plotDataPart2.txt','-mat','f','firstx','endx');
    f =getfield(data,'f');
    firstx=getfield(data,'firstx');
    endx=getfield(data,'endx');
    fplot(f,[firstx endx]);
    end
end
