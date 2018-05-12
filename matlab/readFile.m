function[methodName,equation,interval,tolarance,maxIt]=readFile(filePath)
 fileReader =fopen(filePath,'r')
    methodName=fgetl(fileReader);
    equation=fgetl(fileReader);
    interval=fgetl(fileReader);
    tolarance=fgetl(fileReader);
    maxIt=fgetl(fileReader);
fclose(fileReader)
end