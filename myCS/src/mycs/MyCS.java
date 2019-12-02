package mycs;


import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;

import java.io.*;

import java.io.IOException;
import java.util.ArrayList;

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
    
		String dirPath = "input";
                File dir = new File(dirPath);
                File[] files = dir.listFiles();
                if (files.length == 0) {
                    System.out.println("The directory is empty");
                } else {
                    for (File aFile : files) {
                    
                        String inputFilename = "input/"+aFile.getName();
                        CPPGrammarParser.code_return ast = parseFile(inputFilename);
                        codeTree.initializeFromAST(ast.tree);
                        codeTree.print(inputFilename);
                        
                    }
                }
                

	    } catch (Exception e) {
		e.printStackTrace();
	    }
            
            
            ArrayList<Object> in = new ArrayList();
            ArrayList<Integer> inlvl = new ArrayList();
            ArrayList<Object> out = new ArrayList();
            ArrayList<Object> temp = new ArrayList();
            ArrayList<Object> temp2 = new ArrayList();
            
            in.add("=");
            in.add("x");
            in.add("2");
            inlvl.add(10);
            inlvl.add(11);
            inlvl.add(11);
            
            System.out.println(in);
            System.out.println(inlvl);
            int head =0;
            
            for(int i=1;i<in.size();i++){
                if(i==1){
                    // first child
                    head = inlvl.get(i-1);
                    temp.add(in.get(i));
                    in.set(i, temp.clone());
                    temp.clear();
                }else if(inlvl.get(i)>inlvl.get(i-1)){
                    // child
                    

                }else{
                    // same level
                    temp.add(in.get(i));
                    in.set(i, temp.clone());
                    temp.clear();
                }
            }
            
//                    temp.add(in.get(i));
//                    in.remove(i);
//                    temp2 = (ArrayList<Object>)in.get(i-1);
//                    temp2.add(temp.get(0));
//                    in.set(i-1, temp2.clone());
//                    temp.clear();
//                    temp2.clear();
            
            System.out.println(in);
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
