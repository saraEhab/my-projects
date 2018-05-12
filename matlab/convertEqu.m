function[A,B]=convertEqu(eqns)
%input:
%eqns:array of linear equations 
%output:
%A:vector of Coeffitions of variables in linear eqns
%B:vector of the result of all linear eqns
%Var:variables or sympoles in eqns x,y,z
syms x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10
syms x11 x12 x13 x14 x15 x16 x17 x18 x19 x20
syms x21 x22 x23 x24 x25 x26 x27 x28 x29 x30
syms x31 x32 x33 x34 x35 x36 x37 x38 x39 x40
syms x41 x42 x43 x44 x45 x46 x47 x48 x49 x50 
syms x51 x52 x53 x54 x55 x56 x57 x58 x59 x60
syms  x61 x62 x63 x64 x65 x66 x67 x68 x69 x70
syms x71 x72 x73 x74 x75 x76 x77 x78 x79 x80 
syms x81 x82 x83 x84 x85 x86 x87 x88 x89 x90 
syms x91 x92 x93 x94 x95 x96 x97 x98 x99 x100 x y z


 equations=evalin(symengine,eqns);
[A,B] = equationsToMatrix(equations);

