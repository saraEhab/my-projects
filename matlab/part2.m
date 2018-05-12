function varargout = part2(varargin)
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @part2_OpeningFcn, ...
                   'gui_OutputFcn',  @part2_OutputFcn, ...
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
%C:\Users\TOSIBA-PC\Downloads\newton.txt

% --- Executes just before part2 is made visible.
function part2_OpeningFcn(hObject, eventdata, handles, varargin)
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);


% --- Outputs from this function are returned to the command line.
function varargout = part2_OutputFcn(hObject, eventdata, handles) 

varargout{1} = handles.output;



function path_Callback(hObject, eventdata, handles)
filePath=get(handles.path,'String')
if filePath~=""
     [methodNumber,n,xi,yi,query]=readFilePart2(filePath)
 [queryResult,time,fun] =  callMethodPart2(str2num(methodNumber),str2num(xi),str2num(yi),str2num(n),str2num(query))
    set(handles.queryresult,'String',char(queryResult))
    set(handles.time,'String',time)
    set(handles.text12,'String',char(fun))
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
    old_str=[]
    tmp_str=[]
    new_str=[]
end
 


function path_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function xi_Callback(hObject, eventdata, handles)

function xi_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function yi_Callback(hObject, eventdata, handles)

function yi_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function query_Callback(hObject, eventdata, handles)

function query_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function noofpoints_Callback(hObject, eventdata, handles)

function noofpoints_CreateFcn(hObject, eventdata, handles)
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on selection change in popupmenu1.
function popupmenu1_Callback(hObject, eventdata, handles)
 xi = str2num(get (handles.xi,'String'));
    yi= str2num(get (handles.yi,'String'));
    n= str2num(get (handles.noofpoints,'String'));
    query=str2num( get (handles.query,'String'));
methodNumber=get(handles.popupmenu1,'Value')
[queryResult,time,fun] =  callMethodPart2(methodNumber,xi,yi,n,query)
  set(handles.queryresult,'String',char(queryResult))
    set(handles.time,'String',time)
    set(handles.text12,'String',char(fun))
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
    old_str=[]
    tmp_str=[]
    new_str=[]

    

function popupmenu1_CreateFcn(hObject, eventdata, handles)

if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in plot.
function plot_Callback(hObject, eventdata, handles)
filePath=get(handles.path,'String')
methodNumber=""
if filePath==""
methodNumber=get(handles.popupmenu1,'Value')
else
[methodNumber,n,xi,yi,query]=readFilePart2(filePath)    
end
    axes(handles.scheme);
    plotPart2(str2num(methodNumber));
