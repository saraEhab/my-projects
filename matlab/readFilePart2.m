function[iterpolationNumber,numberOfPoints,samplePoints,Y_Value,queryPoints]=readFilePart2(filePath)
 fileReader =fopen(filePath,'r')
    iterpolationNumber=fgetl(fileReader);
    numberOfPoints=fgetl(fileReader);
    samplePoints=fgetl(fileReader);
    Y_Value=fgetl(fileReader);
    queryPoints=fgetl(fileReader);
fclose(fileReader)
end