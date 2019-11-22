/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycs;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;

import java.io.*;

import java.io.IOException;

/**
 *
 * @author mhm1718
 */
public class MyCS {

    static final String separator = "\t";
    
    public static void main(String[] args) throws IOException
	{
	    CodeTree codeTree = new CodeTree();
	    
	    try{
    
		
		String inputFilename = "test2.c";
		CPPGrammarParser.code_return ast = parseFile(inputFilename);
		codeTree.initializeFromAST(ast.tree);
		codeTree.print();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	    
    private static String parseCommandLine(String[] args) throws Exception
	{
	    if(args.length != 1){
		throw new Exception("filename required.");
	    }
	    return args[0];
	}
    
    private static CPPGrammarParser.code_return parseFile(String inputFilename)
	throws IOException, RecognitionException{
	
	ANTLRFileStream antlrFileStream = new ANTLRFileStream(inputFilename) ;
	CPPGrammarLexer lexer = new CPPGrammarLexer(antlrFileStream);
	TokenStream tokenStream = new CommonTokenStream(lexer);
	CPPGrammarParser parser = new CPPGrammarParser(tokenStream );
	return parser.code();
    }
}
