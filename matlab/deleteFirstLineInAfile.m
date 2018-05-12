function deleteFirstLineInAfile
    fileReader = fopen('print data.txt', 'r') ;              
    fgetl(fileReader) ;                                 
    buffer = fread(fileReader, Inf) ;                    % Read rest of the file.
    delete 'print data.txt'
     fclose(fileReader)
     fileReader = fopen('print data.txt', 'w') ;  
    fwrite(fileReader, buffer) ; 
    fclose(fileReader)
    