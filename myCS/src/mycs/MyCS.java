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
    static ArrayList<Integer> level = new ArrayList();
    static ArrayList<Object> in = new ArrayList();
    static ArrayList<Integer> inlvl = new ArrayList();
    static ArrayList<Object> temp = new ArrayList();
    static ArrayList<Object> temp2 = new ArrayList();
            
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

            
            in.add("=");
            in.add("x");
            in.add("2");
            in.add("z");
            in.add("y");
            in.add("k");
            in.add("q");
            in.add("a");
            inlvl.add(10);
            inlvl.add(11);
            inlvl.add(11);
            inlvl.add(11);
            inlvl.add(12);
            inlvl.add(12);
            inlvl.add(12);
            inlvl.add(13);
            
            System.out.println(in);
            System.out.println(inlvl);
            System.out.println("");

            level.add(inlvl.get(0));
            makeChild();
            
 
            System.out.println(in);
            System.out.println(level);
	}
    
    public static void makeChild(){
        
        for(int i=1;i<in.size();i++){
             if(i==1){
                 // first child
                 temp.add(in.get(i));
                 in.set(i, temp.clone());
                 temp.clear();
                 level.add(inlvl.get(i));
             }else if(inlvl.get(i)>inlvl.get(i-1)){

                  //child
                 int j=1;
                
                if(inlvl.get(i)!=level.get(level.size()-1))
                      level.add(inlvl.get(i));
                 
                 while(!(((ArrayList<Object>)in.get(j)).size()<3)){
                     j++;
                 }

                 temp.add(in.get(i));
                 in.remove(i);
                 temp2 = (ArrayList<Object>)in.get(j);
                 temp2.add(temp.clone());
                 in.set(j, temp2.clone());
                 
                 temp.clear();
                 temp2.clear();
                 i--;
             }else{
                  //same level
                  
                 temp.add(in.get(i));
                 in.set(i, temp.clone());
                 temp.clear();
             }
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
