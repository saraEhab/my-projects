function plotf(methodName)
if methodName==4 || methodName==6
    %4-newton,6-birge veita
   data= load('plotData.txt','-mat','fun','u','v');
    %fun(1) l2ni lw 3mltha fun bs by3ml load l kol l values feha bs l b3dha
    %bysht8lo 3ady
    fun =getfield(data,'fun');
    u=getfield(data,'u');
    v=getfield(data,'v');
    fplot(fun);
     hold on;
     plot(u,v);
     hold off;
elseif methodName ==5
    %secant
   data= load('plotData.txt','-mat','range','fun','SP','SP0','SP1')
    fun =getfield(data,'fun');
    range=getfield(data,'range');
    SP=getfield(data,'SP');
    SP0=getfield(data,'SP0');
    SP1=getfield(data,'SP1');
    hold on
    fplot(fun,range);
    line([SP SP],ylim,'Color',[1 0 0]);
    line([SP0 SP0],ylim,'Color',[1 1 0]);
    line([SP1 SP1],ylim,'Color',[1 0 1]);
elseif methodName ==3
    %fixed point
      data= load('plotData.txt','-mat','g','l','u','x1','y1');
      g =getfield(data,'g');
      u =getfield(data,'l');
      l =getfield(data,'u');
      x1 =getfield(data,'x1');
      y1 =getfield(data,'y1');
      axis([0 5 0 2])
      fplot(g,l);
      hold on;
      plot(u,u);
      hold on;
      line(x1,y1);
     xdata= xlsread('xData.xlsx')
     ydata=xlsread('yData.xlsx')
     line(xdata,ydata);
      elseif methodName ==1
          %bisection
   data= load('plotData.txt','-mat','ur','lr','equation','plotxl','plotyl','plotxu','plotyu','counter');
   equation =getfield(data,'equation');
      plotxl =getfield(data,'plotxl');
     plotyl =getfield(data,'plotyl');
      plotxu =getfield(data,'plotxu');
      plotyu =getfield(data,'plotyu');
      R =xlsread('RData.xlsx');
      ur =getfield(data,'ur');
      lr =getfield(data,'lr');
      counter=getfield(data,'counter');
      i = 1;
      while i <= counter 
            plot([R(i) R(i)],[ur lr]);
            hold on ;
            i=i+1;
       end
      
    fplot (equation);
    hold on;
    plot(plotxl,plotyl,'-o');
    plot(plotxu,plotyu,'-o');
    
elseif methodName ==2
    %false position
     data= load('plotData.txt','-mat','n','equation','plotx','ploty');
     
      n=getfield(data,'n');
      equation=getfield(data,'equation');
      plotx=getfield(data,'plotx');
      ploty=getfield(data,'ploty');
      
      xll=xlsread('xllData.xlsx')
      xuu=xlsread('xuuData.xlsx')
      lr =xlsread('lrData.xlsx');
      ur=xlsread('urData.xlsx');
      
        hold on;
      for i=1:n
          plot([xll(i)  xuu(i)],[lr(i) ur(i)]);
     end     
    fplot (equation);
    plot(plotx,ploty,'-o');
   hold off;
    
elseif methodName ==9
    %general method
    data= load('plotData.txt','-mat','equation','rootx');
     equation =getfield(data,'equation');
     rootx =getfield(data,'rootx');
    fplot(equation,[-10 10]);
     hold on;
   % grid on;
    for l=1:length(rootx)
        scatter(rootx(l),0);
    end
  

end