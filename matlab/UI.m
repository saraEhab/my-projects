function varargout = UI(varargin)
global choosenMethod;
equation='';
Maxiterations= '';
XL= '';
es= '';
XU= '';

gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
    'gui_Singleton',  gui_Singleton, ...
    'gui_OpeningFcn', @UI_OpeningFcn, ...
    'gui_OutputFcn',  @UI_OutputFcn, ...
    'gui_LayoutFcn',  [] , ...
    'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before UI is made visible.
function UI_OpeningFcn(hObject, eventdata, handles, varargin)
handles.output = hObject;

guidata(hObject, handles);


% --- Outputs from this function are returned to the command line.
function varargout = UI_OutputFcn(hObject, eventdata, handles)
varargout{1} = handles.output;



function edit1_Callback(hObject, eventdata, handles)



% --- Executes during object creation, after setting all properties.
function edit1_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in FastStep.
function FastStep_Callback(hObject, eventdata, handles)
filePath=get(handles.path,'String')
next=0
if filePath ~= ""
    [methodName,equation,interval,tolarance,Maxiterations]=readFile(filePath)
    [root,noOfIt,epslon_a,time]=CallMethod(str2num(methodName),equation,str2num(interval(2)),str2num(interval(4)),str2num(Maxiterations),str2num(tolarance),next);
    if methodName~= 7 | methodName~= 8 
    set(handles.edit4,'String',noOfIt)
    set(handles.edit2,'String',root)
    set(handles.edit5,'String',epslon_a)
    set(handles.edit6,'String',time)
end
end

set(handles.print, 'String',"");
    fileReader = fopen('print data.txt','r');
    tmp_str=[]
    new_str=[]
    set(handles.print, 'String', new_str); 
    while ~feof(fileReader)
        old_str=get(handles.print, 'String');
          tmp_str=[fgetl(fileReader)];
          new_str=[old_str; {tmp_str}];
        set(handles.print, 'String', new_str);           
    end
    fclose(fileReader);
    old_str=[];
    tmp_str=[]
    new_str=[]


% --- Executes on button press in SingleStep.
function SingleStep_Callback(hObject, eventdata, handles)

filePath=get(handles.path,'String')
next=0
if filePath ~= "" 
    [methodName,equation,interval,tolarance,Maxiterations]=readFile(filePath)
    [root,noOfIt,epslon_a,time]=CallMethod(str2num(methodName),equation,str2num(interval(2)),str2num(interval(4)),str2num(Maxiterations),str2num(tolarance),next);
    if methodName~= 7 | methodName~= 8 
    set(handles.edit4,'String',noOfIt)
    set(handles.edit2,'String',root)
    set(handles.edit5,'String',epslon_a)
    set(handles.edit6,'String',time)
    set(handles.path,'String',"")
end
end

fileReader = fopen('print data.txt','r');
    tmp_str=[]
    new_str=[]
    if ~feof(fileReader)
        old_str=get(handles.print, 'String');
          tmp_str=[fgetl(fileReader)];
          if tmp_str~=-1
          new_str=[old_str; {tmp_str}];
        set(handles.print, 'String', new_str);
        deleteFirstLineInAfile;
          end
    end
    fclose(fileReader);

% --- Executes on button press in Plot.
function Plot_Callback(hObject, eventdata, handles)
filePath=get(handles.path,'String')
next=0
if filePath == ""
choosenMethod=get(handles.methods,'Value');
else
    [methodName,equation,interval,tolarance,Maxiterations]=readFile(filePath)
    [root,noOfIt,epslon_a,time]=CallMethod(str2num(methodName),equation,str2num(interval(2)),str2num(interval(4)),str2num(Maxiterations),str2num(tolarance),next);
    if methodName~= 7 | methodName~= 8 
    set(handles.edit4,'String',noOfIt)
    set(handles.edit2,'String',root)
    set(handles.edit5,'String',epslon_a)
    set(handles.edit6,'String',time)
    choosenMethod=str2num(methodName)
end
end
axes(handles.scheme);
%plotf htrsm b a5r data mwgoda 3ndha fl file
plotf(choosenMethod);

% --- Executes on selection change in methods.
function methods_Callback(hObject, eventdata, handles)
next =0;
    equation = get (handles.edit1,'String');
    Maxiterations= str2num(get (handles.edit3,'String'));
    XL= str2num(get (handles.edit8,'String'));
    es= str2num(get(handles.edit7,'String'));
    XU= str2num(get(handles.edit9,'String'));
    choosenMethod=get(handles.methods,'Value');
    [root,noOfIt,epslon_a,time]=CallMethod(choosenMethod,equation,XL,XU,Maxiterations,es,next);
    

if choosenMethod~= 7 | choosenMethod~= 8 
    set(handles.edit4,'String',noOfIt)
    set(handles.edit2,'String',root)
    set(handles.edit5,'String',epslon_a)
    set(handles.edit6,'String',time)
end

% --- Executes during object creation, after setting all properties.
function methods_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit2_Callback(hObject, eventdata, handles)
function edit2_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit3_Callback(hObject, eventdata, handles)

function edit3_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit4_Callback(hObject, eventdata, handles)

function edit4_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit5_Callback(hObject, eventdata, handles)

function edit5_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit6_Callback(hObject, eventdata, handles)



% --- Executes during object creation, after setting all properties.
function edit6_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in Interpolation.
function Interpolation_Callback(hObject, eventdata, handles)




function edit7_Callback(hObject, eventdata, handles)

function edit7_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit8_Callback(hObject, eventdata, handles)

function edit8_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

    

function edit9_Callback(hObject, eventdata, handles)

function edit9_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function print_Callback(hObject, eventdata, handles)



% --- Executes during object creation, after setting all properties.
function print_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function path_Callback(hObject, eventdata, handles)



% --- Executes during object creation, after setting all properties.
function path_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
