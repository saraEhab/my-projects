function f =converttofun( str )
f = str2func(['@(x)' str]);
end