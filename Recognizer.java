
import java.io.*;
import java.util.Scanner;
//-------------------------------------------------------------------
// Recursive-Descent Recognizer by TOME VANG
// CSc 135 Assignment 1 - Professor Radimsky
// NOTE: I used the Example Parser from SacCT written by Dr. Gordon
// and modified it for assignment 1. 

// HOW TO COMPILE IN LINUX (ATHENA):
// Compile this in linux by typing: javac Recognizer.java
// Then run the program by typing: java Recognizer

// HOW TO COMPILE IN ECLIPSE (WINDOWS):
// Open the file in your workspace.
// Right click and click run as java application

// My program uses the EBNF grammar: 
/*
program ::= P {declare} B {statmt} E ;
declare ::= ident {, ident} : V ;
statmt ::= assnmt | ifstmt | loop | read | output
assnmt  ::= ident ~ exprsn ;
ifstmt  ::= I comprsn @ {statmt} [% {statmt}] &
loop    ::= W comprsn L {statmt} T
read    ::= R ident {, ident } ;
output  ::= O ident {, ident } ;
comprsn ::= ( oprnd opratr oprnd )
exprsn  ::= factor {+ factor}
factor  ::= oprnd {* oprnd}
oprnd   ::= integer | ident | ( exprsn )
opratr  ::= < | = | > | ! 
ident   ::= letter {char}
char    ::= letter | digit
integer ::= digit {digit}
letter  ::= X | Y | Z
digit   ::= 0 | 1 
*/

//---------------------- CLASS VARIABLES AND FUNCTIONS -------------

public class Recognizer
{
	 static String inputString;
	 static int index = 0;
	 static int errorflag = 0;

	 private char token()
	 { 
		 return(inputString.charAt(index)); 
	 }
	
	 private void advancePtr()
	 { 
		 if (index < (inputString.length()-1)) index++; 
	 }
	 private void match(char T)
	 { 
		 if (T == token()) {
			 //System.out.print(T);
			 //System.out.print(token());
			 advancePtr(); 
		 }
		 else error(); 
		 
	 }
	 private void error()
	 {
		 System.out.println("error at position: " + index + " Token: " + token());
		 errorflag = 1;
		 advancePtr();
	 }


// ------------------------ GRAMMAR STARTS HERE ------------------------
	 private void program()
	 { 
		 match('P'); 
		 while ( token() == 'X'|| token() == 'Y' || token() == 'Z')
			 declare();
		 match('B');
		 while (token() == 'X' || token() == 'Y'  || token() == 'Z'
				 || token() == 'I' || token() == 'W' || token() == 'R' ||
				 token() == 'O') 
			 statmt();
		 match('E');
		 match(';');
	 }
	 
	 
	 private void declare()
	 {
		 if ( token() == 'X'|| token() == 'Y' || token() == 'Z')
			 	ident();
		 else error();
		 while (token() == ','){
			 match(',');
			 if ( token() == 'X'|| token() == 'Y' || token() == 'Z')
				 	ident();
			 else error();
		 }
		 match(':');
		 match('V');
		 match(';');
	 }
	 
	 private void statmt()
	 { 
		 if ((token() == 'X') || (token() == 'Y')  || (token() == 'Z')) assnmt();
		 else if (token() == 'I') ifstmt();
		 else if (token() == 'W') loop();
		 else if (token() == 'R') read();
		 else if (token() == 'O') output();
	 }
	 
	 private void assnmt()
	 { 
		 if (token() == 'X' || token() == 'Y'  || token() == 'Z')
			 ident();
		 else error();
		 match('~');
		 exprsn();
		 match(';');
	 }
	 
	 private void ifstmt()
	 { 
		 match('I');
		 comprsn();
		 match('@');
		 while (token() == 'X' || token() == 'Y'  || token() == 'Z'
				 || token() == 'I' || token() == 'W' || token() == 'R' ||
				 token() == 'O'){
			 statmt();
		 }
		 if (token() == '%') 
		 { 
			 match('%');
			 while (token() == 'X' || token() == 'Y'  || token() == 'Z'
					 || token() == 'I' || token() == 'W' || token() == 'R' ||
					 token() == 'O')
				 statmt();
		 }
		 match('&');
	 }
	 
	 private void loop()
	 { 
		 match('W');
		 comprsn();
		 match('L');
		 while (token() == 'X'|| token() == 'Y' || token() == 'Z' ||
				 token() == 'I'|| token() == 'W' || token() == 'R' ||
				 token() == 'O') 
			 statmt();
		 match('T');
	 }
	 
	 private void read()
	 { 
		 match('R');
		 ident();
		 
		 while (token() == ','){
			 match(',');
			 if (token() == '0' || token() == '1' || token() == 'X'|| 
				 token() == 'Y' || token() == 'Z')
				 ident();
			 else error();
		 }
		 match(';');
	 }
	 
	 private void output()
	 { 
		 match('O');
	
		 ident();
		 
		 while (token() == ','){
			 match(',');
			 if (token() == '0' || token() == '1' || token() == 'X'|| 
				 token() == 'Y' || token() == 'Z')
				 ident();
			 else error();
		 }
		 match(';');
	 }

	 private void comprsn()
	 { 
		match('(');
	 	oprnd();
	 	opratr();
	 	oprnd();
		match(')');
	 }
	 
	 private void exprsn()
	 { 
		 if ( token() == 'X'|| token() == 'Y' || token() == 'Z' || 
				 token() == '0' || token() == '1' || token() == '(')
		 	factor();
		 else error();
		 while (token() == '+'){
			 match('+');
			 if (token() == 'X'|| token() == 'Y' || token() == 'Z') 
				 factor();
			 else error();
		 }
	 }
	 
	 private void factor() 
	 { 
		 if (token() == '0' || token() == '1' || token() == 'X'|| 
				 token() == 'Y' || token() == 'Z' || token() == '(')
			 oprnd();
		 else error();
		 while (token() == '*'){
			 	match('*');
			 	if (token() == '0' || token() == '1' || token() == 'X'|| 
				 token() == 'Y' || token() == 'Z' || token() == '(') 
			 		oprnd();
			 	else error();
		 }
	 }
	 
	 private void oprnd()
	 { 
		 if (token() == 'X' || token() == 'Y'  || token() == 'Z') ident(); 
		 else if (token() == '0' || token() == '1') integer();
		 else if (token() == '(') {
			 match('(');
			 exprsn();
			 match(')');
		 }
		 else error();
	 }
	 
	 private void opratr()
	 { 
		 if ((token() == '<') || (token() == '=') || (token() == '>')
				 || (token() == '!')) 
			 match(token()); 
		 else error(); 
	 }
	 
	 private void ident()
	 { 
		 if (token() == 'X' || token() == 'Y'  || token() == 'Z')
			 letter();
		 else error();
		 while ((token() == 'X') || (token() == 'Y')  || (token() == 'Z') 
				 || token() == '0' || token () == '1'){
			 character();
		 }
	 }
	 
	 private void character()
	 { 
		 if ((token() == 'X') || (token() == 'Y')  || (token() == 'Z')) letter(); 
		 else digit();
	 }
	 
	 private void integer()
	 { 
		 if (token() == '0' || token() == '1') 
			 digit();
		 else error();
		 while (token() == '0' || token() == '1') 
			 digit(); 
	 }
	 
	 private void letter()
	 { 
		 if ((token() == 'X') || (token() == 'Y') || (token() == 'Z')) 
			 match(token()); 
		 else error(); 
	 }
	 
	 private void digit()
	 { 
		 if ((token() == '0') || (token() == '1')) 
			 match(token()); 
		 else error(); 
	 }
	 
	 
	 
	 
//---------------------------- START FUNCTION --------------------------
	 private void start()
	 {
		 program();
		 match('$');
		 if (errorflag == 0){
		 System.out.println("legal." + "\n");}
		 else {
		 System.out.println("errors found." + "\n");}
	 }
	 
	 
//---------------------------- MAIN FUNCTION --------------------------
	 
	 @SuppressWarnings("resource")
	public static void main (String[] args) throws IOException
	 {
		 Recognizer rec = new Recognizer();
		 Scanner input = new Scanner(System.in);
		 System.out.print("\n" + "enter an expression: ");
		 inputString = input.nextLine();
		 rec.start();
	 }
	
}