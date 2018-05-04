#include <iostream>
#include <regex>
#include <fstream>
#include <vector>
#include<string>
#include<algorithm>
#include <list>


using namespace std;
vector<string> symbolicNames;
vector<string> jumpOperands;
string startLabel = "";
vector<int> repeatedSymbolicnames;


bool checkRepeatedSymbolicnames(string symbolicname) {
    for (int index = 0; index < symbolicNames.size(); index++) {
        if (symbolicname == symbolicNames[index]) {
            return true;
        }
    }
    return false;
}

void findSymbolicNames(vector<string> lineParts, int lineIndex) {
    string label = lineParts[0];
    string opCode = lineParts[1];
    string operand = lineParts[2];

    if (opCode == "BYTE" || opCode == "WORD" || opCode == "RESB" || opCode == "RESW") {
        if (checkRepeatedSymbolicnames(label)) {
            repeatedSymbolicnames.push_back(lineIndex);
            //cout << "Error repeated symbolic name : " << label << endl;
        } else {
            symbolicNames.push_back(label);
            cout << "symbole name : " << label << endl;
        }
    }
    if (opCode == "J" || opCode == "JEQ" || opCode == "JLT" || opCode == "JGT" || opCode == "JSUB") {
        jumpOperands.push_back(operand);
        cout << "jump operand : " << operand << endl;
    }

}

bool checkLabelCorrectness(string opCode, string label, int index) {
    regex startOfOperand;
    bool check = true;
    bool flag = false;
    if (opCode == "RMO" || opCode == "LDCH" || opCode == "STCH" || opCode == "ADD" || opCode == "SUB" ||
        opCode == "MUL" || opCode == "DIV" || opCode == "ADDR" || opCode == "SUBR" || opCode == "MULR" ||
        opCode == "DIVR" || opCode == "COMP" || opCode == "COMPR" || opCode == "J" || opCode == "JEQ" ||
        opCode == "JLT" || opCode == "JGT" || opCode == "TIX" || opCode == "TIXR" || opCode == "JSUB" ||
        opCode == "RSUB" || opCode == "TD" || opCode == "RD" || opCode == "WD" ||
        regex_match(opCode, startOfOperand.assign("(ST)([AXLBSTF])")) ||
        regex_match(opCode, startOfOperand.assign("(LD)([AXLBSTF])"))) {

        for (int index = 0; index < symbolicNames.size(); index++) {
            if (label == symbolicNames[index]) {
                flag = true;
                break;
            }
        }
        for (int index = 0; index < jumpOperands.size(); index++) {
            if (label == jumpOperands[index]) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (label != "") {
                check = false;
            }
        }

    } else if (opCode == "BYTE" || opCode == "RESB" || opCode == "RESW" || opCode == "WORD") {

    } else if (opCode == "START") {
        startLabel = label;
    } else if (opCode == "END" || opCode == "ORG" || opCode == "BASE" || opCode == "NOBASE" || opCode == "LTORG") {
        if (label != "") {
            check = false;
        }
    } else {
        cout << "     ***Error in opCode :" << opCode << "in line :" << index << " isn't defined in the language"
             << endl;
    }
    return check;
}

bool checkOperandCorrectness(string opCode, string operand) {
    bool check = true;
    regex startOfOperand;
    if (opCode == "RMO" || opCode == "ADDR" || opCode == "SUBR" || opCode == "MULR" ||
        opCode == "DIVR" || opCode == "COMPR") {
        //take two registers


        if (!regex_match(operand, startOfOperand.assign("([AXLBSTF])(/,)([AXLBSTF])"))) {
            check = false;
        }

    } else if (regex_match(opCode, startOfOperand.assign("(ST)([AXLBSTF])")) ||
               regex_match(opCode, startOfOperand.assign("(LD)([AXLBSTF])")) || opCode == "ADD" ||
               opCode == "SUB" || opCode == "MUL" || opCode == "DIV" || opCode == "COMP" || opCode == "TIX" ||
               opCode == "TD" || opCode == "RD" || opCode == "WD") {
        bool flag = false;
        for (int index = 0; index < symbolicNames.size(); index++) {
            if (operand == symbolicNames[index]) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            regex startOfOperand;
            if (operand.length() < 5) {
                if (!regex_match(operand, startOfOperand.assign("(#)/d*")) ||
                    !regex_match(operand, startOfOperand.assign("(@)/d*"))) {

                    check = false;
                }
            }
        }
    } else if (opCode == "LDCH" || opCode == "STCH") {

        bool flag = false;
        string subOperand = "";

        if (operand[operand.length() - 2] == ',' && operand[operand.length() - 1] == 'X') {
            subOperand += operand.substr(0, operand.length() - 2);
        } else {
            subOperand += operand;
        }
        for (int index = 0; index < symbolicNames.size(); index++) {
            if (subOperand == symbolicNames[index]) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            check = false;
        }
    } else if (opCode == "J" || opCode == "JEQ" ||
               opCode == "JLT" || opCode == "JGT" || opCode == "JSUB") {
        if (operand[0] == ' ') {
            check = false;
        }
    } else if (opCode == "TIXR") {
        if (!regex_match(operand, startOfOperand.assign("([AXLBSTF])"))) {
            check = false;
        }
    } else if (opCode == "RSUB" || opCode == "NOBASE" || opCode == "LTORG") {
        if (operand != "                  ") {
            check = false;
        }
    } else if (opCode == "BYTE") {
        regex startOfOpCode;
        if (!regex_match(operand, startOfOpCode.assign("(C')[A-Z]/'")) ||
            !regex_match(operand, startOfOpCode.assign("(X')[A-Z1-9]/'"))) {
            check = false;
        }
    } else if (opCode == "WORD") {
        regex startOfOpCode;
        if (operand.length() <= 4) {
            if (!regex_match(operand, startOfOpCode.assign("/d*"))) {
                if (operand != "                  ") {
                    check = false;
                }
            }
        } else {
            bool flag = false;
            for (int index = 0; index < symbolicNames.size(); index++) {
                if (operand == symbolicNames[index]) {
                    flag = true;
                }
            }
            if (!flag) {
                check = false;
            }
        }

    } else if (opCode == "RESW" || opCode == "RESB") {
        regex startOfOpCode;
        if (operand.length() <= 4) {
            if (!regex_match(operand, startOfOpCode.assign("/d*"))) {
                check = false;
            }
        }
    } else if (opCode == "START") {
        regex startOfOpCode;
        if (operand.length() <= 4) {
            if (!regex_match(operand, startOfOpCode.assign("/d*"))) {
                if (operand != "                  ") {
                    check = false;
                }
            }
        }
    } else if (opCode == "END") {
        if (operand != startLabel) {
            if (operand != "") {
                check = false;
            }
        }
    } else if (opCode == "ORG") {
        bool flag = false;
        for (int index = 0; index < symbolicNames.size(); index++) {
            if (operand == symbolicNames[index]) {
                flag = true;
            }
        }
        if (!flag) {
            check = false;
        }

    } else if (opCode == "EQU" || opCode == "BASE") {
        bool flag = false;
        for (int index = 0; index < symbolicNames.size(); index++) {
            if (operand == symbolicNames[index]) {
                flag = true;
            }
        }
        if (!flag) {
            regex startOfOpCode;
            if (operand.length() <= 4) {
                if (!regex_match(operand, startOfOpCode.assign("/d*"))) {
                    check = false;
                }
            }
        }
    }
}

string parse(vector<string> lineParts, int index) {
    string label = lineParts[0];
    string opCode = lineParts[1];
    string operand = lineParts[2];

    if (!checkLabelCorrectness(opCode, label, index)) {
        return "Error in label";
    }
    if (checkOperandCorrectness(opCode, operand)) {
        return "Error in operand ";
    }
    return "correct";

}

bool spacesCheck(string line) {
    bool isCorrect = false;
    regex str;


    if (line[line.length() - 1] == ' ') {//check for the spaces at the end of the line
        isCorrect = false;
    }
//check the start
    if ((line[0] >= 'a' && line[0] <= 'z') || (line[0] >= 'A' && line[0] <= 'Z')) {
        isCorrect = true;
    } else if (line.substr(0, 7) == "       ") {
        isCorrect = true;
    } else {
        isCorrect = false;
        //error in line format
    }
//check the opCode
    if (line[9] >= 'A' && line[9] <= 'Z') {
        isCorrect = true;
    } else {
        isCorrect = false;
    }
    //check the operand
    if (line.length() >= 17) {
        if ((line[17] >= 'A' && line[17] <= 'Z') || line[17] == '#' || (line[17] >= '0' && line[17] <= '9')) {
            isCorrect = true;
        } else {
            isCorrect = false;
        }
    }
//check the comments
    if (line.length() >= 35) {
        if (line[35] == '.') {
            isCorrect = true;
        } else {
            isCorrect = false;
        }
    }

    return isCorrect;
}

vector<string> spliter(string line) {
    vector<string> lineParts;

    if (spacesCheck(line)) {
        string opCode = "";
        string label = "";
        string operand = "";
        string comment = "";

        for (int index = 0; index < line.length(); index++) {
            if (index < 8 && line[index] != ' ') {
                label += line[index];
            } else if (index > 8 && index < 15 && line[index] != ' ') {
                opCode += line[index];
            } else if (index > 16 && index < 35 && line[index] != ' ') {
                operand += line[index];
            } else if (index >= 35 && index < 66 && line[index] != ' ') {
                if (line[35] == '.') {
                    comment += line[index];
                }
            }
        }
        lineParts.push_back(label);
        lineParts.push_back(opCode);
        lineParts.push_back(operand);
        lineParts.push_back(comment);
    }
    return lineParts;
}

char getAddressMode(string operand) {
    if (operand.at(0) >= '0' && operand.at(0) <= '9') {
        return ' ';
    }
    return operand.at(0);
}

int getOperandValue(string operand) {
    string operandValue;
    int ovInt = 0;
    char addressMode = getAddressMode(operand);
    if (addressMode == ' ') {
        operandValue = operand;
        stringstream ov(operandValue);
        ov >> ovInt;
    } else if (addressMode == '#' || addressMode == '@') {
        operandValue = operand.substr(1);
        stringstream ov(operandValue);
        ov >> ovInt;
    } else if (addressMode == 'c' || addressMode == 'C' || addressMode == 'x' || addressMode == 'X') {
        ovInt = operand.length() - 3;
    }
    return ovInt;
}

int calculateIncrement(string line) {
    int increment = 0;
    vector<string> lineParts = spliter(line);
    if (lineParts[1] == "RESW") {
        increment = 3 * getOperandValue(lineParts[2]);
    } else if (lineParts[1] == "RESB") {
        increment = getOperandValue(lineParts[2]);
    } else if (lineParts[1] == "BYTE") {
        increment = 1;
    } else if (lineParts[1] == "ADDR") {
        increment = 2;

    } else if (lineParts[1] == "START") {
        if (lineParts[2] != "") {
            increment = getOperandValue(lineParts[2]);
        }
    } else if (lineParts[1].find('+') != -1) {
        increment = 4;
    } else {
        increment = 3;
    }

    return increment;
}

bool commentsCheck(string line) {
    bool check = false;
    if (line[0] == '.') {
        check = true;
    }
    return check;
}

list<string> trueframe(string filename) {
    string SicInstruction[] = {"ADD", "ADDF", "ADDR", "AND", "CLEAR", "COMP", "COMPF", "COMPR", "DIV", "DIVF", "DIVR",
                               "FIX", "FLOAT", "HIO", "JEQ", "JGT", "JLT", "JSUB", "LDA", "LDB", "LDCH", "LDF", "LDL",
                               "LDS", "LDT", "LDX", "LPS", "MUL", "MULF", "MULR", "NORM", "OR", "RD", "RMO", "RSUB",
                               "SHIFTL", "SHIFTR", "SIO", "SSK", "STA", "STB", "STCH", "STF", "STI", "STL", "STS",
                               "STSW", "J", "JSUB" "STT", "STX", "SUB", "SUBF", "SUBR", "SVC", "TD", "TIO", "TIX",
                               "TIXR", "WD"
    };
    string typedata[] = {"BYTE", "END", "RESB", "RESW", "START", "WORD"};
    string line = "";
    list<string> lines;
    ifstream infile(filename);
    while (getline(infile, line)) {
        string comment = "";
        string operand = "";
        string label = "";
        string opcode = "";
        string str = "";
        string trueline = "";
        int flag = 0;
        line += " ";
        for (int i = 0; i < line.size(); i++) {
            if (line.at(i) == '.') {
                if (str.size() != 0) {
                    flag++;
                    transform(str.begin(), str.end(), str.begin(), ::toupper);
                    if (binary_search(SicInstruction, SicInstruction + 59, str) ||
                        binary_search(typedata, typedata + 6, str)) {
                        opcode = str;
                        str = "";
                        flag = 2;
                    } else {
                        if (flag == 2) {
                            opcode = "";
                            operand = "";
                            label = "";
                            comment = "";
                            // cout << line << "\n";
                            // cout << "** Error in opcode" << "\n";
                            break;
                        } else if (flag == 1) {
                            if ((str.at(0) <= 'Z' && str.at(0) >= 'A') || str.at(0) == '_') {
                                label = str;
                                str = "";
                            }

                        } else if (flag == 3) {
                            operand = str;
                            str = "";
                        } else {
                            opcode = "";
                            operand = "";
                            label = "";
                            comment = "";
                            //cout << line << "\n";
                            //cout << "** Error you write more than operand" << "\n";
                            break;
                        }

                    }
                }
                comment = line.substr(i, line.size() - 1);
                break;
            }
            if (line.at(i) == ' ' || line.at(i) == '\t' || line.at(i) == ';' || line.at(i) == '/' ||
                line.at(i) == '?') {
                if (str.size() != 0) {
                    flag++;
                    transform(str.begin(), str.end(), str.begin(), ::toupper);
                    if (binary_search(SicInstruction, SicInstruction + 59, str) ||
                        binary_search(typedata, typedata + 6, str)) {
                        opcode = str;
                        str = "";
                        flag = 2;
                    } else {
                        if (flag == 2) {
                            opcode = "";
                            operand = "";
                            label = "";
                            comment = "";
                            //cout << line << "\n";
                            //cout << "** Error in opcode" << "\n";
                            break;
                        } else if (flag == 1) {
                            if ((str.at(0) <= 'Z' && str.at(0) >= 'A') || str.at(0) == '_') {
                                label = str;
                                str = "";
                            }

                        } else if (flag == 3) {
                            operand = str;
                            str = "";
                        } else {
                            opcode = "";
                            operand = "";
                            label = "";
                            comment = "";
                            //cout << line << "\n";
                            //cout << "** Error you write more than 3 operand" << "\n";
                            break;
                        }

                    }
                }

            } else {
                str += line.at(i);
            }
        }
        if (label != "" || operand != "") {
            if (opcode == "") {
                opcode = "";
                operand = "";
                label = "";
                comment = "";
                //cout << line << "\n";
                //cout << "** Error opcode not found" << "\n";
            }
        }
        if (opcode == "" && operand == "" && comment == "" && label == "") {
            continue;
        }
        if (comment != "" && opcode == "") {
            trueline = comment;
        } else if (label.size() < 9 && operand.size() < 19 && opcode.size() < 7) {
            for (int i = 0; i < 9; i++) {
                trueline += " ";
            }
            trueline.replace(0, label.size(), label);
            trueline += opcode;
            if (operand != "") {
                int n = trueline.size();
                for (int i = n; i < 17; i++) {
                    trueline += " ";
                }
                trueline += operand;
            }
            if (comment != "") {
                int n = trueline.size();
                for (int i = n; i < 35; i++) {
                    trueline += " ";
                }
                trueline += comment;
            }
        } else {
            opcode = "";
            operand = "";
            label = "";
            comment = "";
            // cout << line << "\n";
            //cout << "** Error the names or operand is to long" << "\n";

        }
        lines.push_back(trueline);
    }

    return lines;
}


void readFromFileAndPrintTheFile(string fileName) {
    string line = "";
    vector<string> temp;
    int lineIndex = 0;
    int increment = 0;
    string check;
    bool startExistance = true;
    bool endExistance = false;
    list<string> lines = trueframe(fileName);
    //now file is opened
    //check the file is opened or not
    while (!lines.empty()) {//print line by line
        line = lines.front();
        lines.pop_front();
        lineIndex++;

        if (!commentsCheck(line)) {
            temp = spliter(line);
            if (temp.empty()) {
                cout << line << endl;
                cout << "      ***Error in indentation" << endl;

            } else {
                check = parse(temp, lineIndex);
                if (startExistance) {
                    startExistance = false;
                    if (startLabel == "") {
                        cout << "      ***Error in opCode START" << endl;

                    }
                }
                if (temp[1] == "END") {
                    endExistance = true;
                }
                if (check == "correct") {
                    bool flag = true;
                    for (int index = 0; index < repeatedSymbolicnames.size(); index++) {
                        if (lineIndex == repeatedSymbolicnames[index]) {
                            flag = false;
                            cout << line << endl;
                            cout << "     ***Error repeated symbolic name " << endl;
                        }
                    }
                    if (flag) {
                        increment += calculateIncrement(line);
                        cout << increment << "  ";
                        cout << line << endl;
                    }
                    flag = true;
                } else if (check == "Error in operand ") {
                    cout << line << endl;
                    cout << "     ***Error in operand " << endl;

                } else if (check == "Error in label") {
                    cout << line << endl;
                    cout << "     ***Error in label" << endl;
                }
            }
        } else {
            cout << line << endl;
        }
    }
    if (!endExistance) {
        cout << "Error in opCode END" << endl;
    }

}

void readFromFileAndExtractSymboleTable(string fileName) {
    string line = "";
    list<string> lines = trueframe(fileName);
    //now file is opened
    int lineIndex = 0;

    //check the file is opened or not
    while (!lines.empty()) {//read line by line
        line = lines.front();
        lines.pop_front();
        lineIndex++;
        if (!commentsCheck(line)) {
            vector<string> temp;
            temp = (spliter(line));
            if (!temp.empty()) {
                findSymbolicNames(temp, lineIndex);
            }
        }
    }
    readFromFileAndPrintTheFile(fileName);
}

int main() {
    string fileName;
    // cout << "Enter the file name : ";
    //cin >> fileName;
    readFromFileAndExtractSymboleTable("T_F.txt");
    return 0;
}
