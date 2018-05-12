function [roots,time] = GeneralMethod(strf)
tstart=tic;
syms x;
 equation=evalin(symengine,strf);
 xrange=-10:.1:10;
 k=1;
 rootx(1)=0;
 for i=2:length(xrange)
[xrnew,counter,epslon_a,time]=bisection(strf,xrange(i-1),xrange(i),50,.00000001,0);
 if(epslon_a<=.00000001)
     rootx(k)=round(xrnew,4);
     k=k+1;
 else
  [root,iterations,epslon,time]=secantF(strf,xrange(i-1),xrange(i),.00000001,50,0);   
     if(epslon<=.00000001)
      rootx(k)=round(root,4);
      k=k+1;
     end 
     
 end 
 end
 rootx=unique(rootx);
 roots= rootx;
 
  save('plotData.txt','equation','rootx')
 time=toc(tstart);
 
end
