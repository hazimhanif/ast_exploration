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
    static int i=0;
    static int recurse_level =0;
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

            
            makeChild();
            
 
            System.out.println(in);
            System.out.println(level);
            System.out.println(recurse_level);

	}
    
    public static void recurse(ArrayList<Object> child){
        
        recurse_level++;
        //if(level.size()-1!=recurse_level)
           
        
        int j=-1;
        int childsize;
        do{
            childsize = 0;
            j++;
            if(j==child.size()-1){
                recurse((ArrayList<Object>)child.get(0));
                return;
            }else if(child.get(j).getClass().getSimpleName().equals("String")){
                childsize= 999;
                continue;
            }else{
                
            }
           childsize = ((ArrayList<Object>)child.get(j)).size();
        }while(!(childsize<3));
                
        temp.add(in.get(i));
        in.remove(i);
        temp2 = (ArrayList<Object>) child.get(j);
        temp2.add(temp.clone());
        child.set(j,temp2.clone());

        temp.clear();
        temp2.clear();
        i--;
    
    }
    
    public static void makeChild(){
        
        if(in.size()==1)
            return;
        
        int lvlcounter = 1;
        level.add(inlvl.get(0));
        
        for(i=1;i<in.size();i++){
            recurse_level=0;
            
            if(inlvl.get(lvlcounter)!=level.get(level.size()-1))
                level.add(inlvl.get(lvlcounter));
            
            if(i==1){
                // first child
                temp.add(in.get(i));
                in.set(i, temp.clone());
                temp.clear();
            }else if(inlvl.get(i)>inlvl.get(i-1)){

                 //child
                recurse(in);
                
            }else{
                 //same level

                temp.add(in.get(i));
                in.set(i, temp.clone());
                temp.clear();
            }
            lvlcounter++;
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
