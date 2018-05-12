    function [root,noOfIt,epslon_a,time] = CallMethod(choosenMethod,equation,xl,xu,maxIterations,es,next)

        if choosenMethod == 1
    % bisection
    [root,noOfIt,epslon_a,time] = bisection(equation,xl,xu,maxIterations,es,next);
        elseif choosenMethod == 2
    %false position
    [root,noOfIt,epslon_a,time] = falseposition(equation,xl,xu,maxIterations,es,next)
        elseif choosenMethod == 3
        %fixed point
        [root,noOfIt,epslon_a,time]=fixedpoint(equation,xl,maxIterations,next);
        elseif choosenMethod == 4
            %newton
            [root,noOfIt,epslon_a,time] = NewtonMethod(equation ,xl,maxIterations,es,next);

        elseif choosenMethod == 5
            %secant
            [root,noOfIt,epslon_a,time]=secantF(equation,xl,xu,es,maxIterations,next);
        elseif choosenMethod == 6
            %birge vieta
            [root,noOfIt,epslon_a,time] = BirgeVieta(equation ,xl,maxIterations,es,next);

        elseif choosenMethod == 7
        %gauss jordan
        root=""
        noOfIt=""
        epslon_a=""
        time=gauess_jorden(equation)

        elseif choosenMethod == 8
            %jordan with pivoting
            root=""
            noOfIt=""
            epslon_a=""
            time=jordenWithPartialPivoting(equation)
        elseif choosenMethod == 9
            %9.General method
            epslon_a=""
            noOfIt=""
            [root,time] = GeneralMethod(equation)
        end
    return