package mycs;


import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.antlr.runtime.tree.CommonTree;

public class CodeTree
{
    public void initializeFromAST(CommonTree ast)
    {
	root = new CodeTreeNode();
	root.initializeFromASTNode(ast, 0);	
    }

    public void print(String filename) throws IOException
    {
        NodePrinter.initPrinter(filename);
	NodePrinter.printCSV(root);
        NodePrinter.closePrinter();
        System.out.println(NodePrinter.getfunc());
    }
    
    private CodeTreeNode root;
}

class CodeTreeNode
{
    public CodeTreeNode()
    {
	children = new LinkedList<CodeTreeNode>();
    }
    
    public void initializeFromASTNode(CommonTree node, int aLevel)
    {
	level = aLevel;
	astNode = node;
	initializeChildren();
	
	initializeCodeStr();
	initializePosition();
    }
            
    private void initializeChildren()
    {
	children.clear();
	int numberOfChildren = astNode.getChildCount();
	for(int i = 0; i < numberOfChildren; i++){
	    CodeTreeNode child = new CodeTreeNode();
	    CommonTree childAST = (CommonTree) astNode.getChild(i);
	    child.initializeFromASTNode(childAST, level + 1);
	    children.add(child);
	}	
    }
    
    private void initializeCodeStr()
    {		
	if(ASTNodeWrapper.isLeaf(astNode)){
	    	    
	    codeStr = astNode.toString();
	    codeStr = escapeCodeStr(codeStr);
	    
	    return;
	}

	codeStr = "";
	int numberOfChildren = children.size();
	for(int i = 0; i < numberOfChildren; i++){
	    CodeTreeNode child = (CodeTreeNode) children.get(i);
	    if( child.codeStr.equals("") || child.codeStr.equals(" "))
		continue;
	    codeStr += child.codeStr;
	    if(i != numberOfChildren -1) codeStr += STR_SEPERATOR;
	}	
    }
    
    private String escapeCodeStr(String codeStr)
    {
	String retval = codeStr;
	retval = retval.replace("\n", "\\n");
	retval = retval.replace("\t", "\\t");
	return retval;
    }

    private void initializePosition()
    {
	startPos = ASTNodeWrapper.getStartPosition(astNode);
	
	if(ASTNodeWrapper.isLeaf(astNode)){
	    // if required, correct this.
	    endPos = ASTNodeWrapper.getStartPosition(astNode);
	    return;
	}
	
	int lastChildIndex = children.size() - 1;
	CodeTreeNode lastChild = children.get(lastChildIndex);
	endPos = ASTNodeWrapper.getStartPosition(lastChild.astNode);
	
    }

    public String codeStr = "";
    public int level = -1;
    public String startPos;
    public String endPos;

    public LinkedList<CodeTreeNode> children;
    public CommonTree astNode;
    

    public final String STR_SEPERATOR = " ";
    
}

class ASTNodeWrapper
{
    public static boolean isLeaf(CommonTree node)
    {
	return (node.getChildCount() == 0);
    }
    
    public static String getType(CommonTree node)
    {
	return node.toString();
    }
    
    public static String getStartPosition(CommonTree node)
    {
	int pos = node.getCharPositionInLine();
	//return node.getLine() + ":" + pos;
        return node.getLine()+"";
    }
       
}

class NodePrinter
{
    
    static PrintWriter pw ;
    static String statement="[";
    static ArrayList<String> stmnt = new ArrayList();
    static ArrayList<Integer> lvl = new ArrayList();
    static ArrayList<ArrayList<String>> func = new ArrayList();
    static final String [] nodeTypeBlacklist = {
	"SOURCE_FILE", "ITERATION", "FUNCTION_DEF","SELECTION","STATEMENTS"
    };
  
    static HashMap<String,Integer> nodeBlacklist;   
    
    static
    {	
	initializeNodeBlacklist();
    }
  
    public static ArrayList<ArrayList<String>> getfunc(){
        return func;
    }
    
    private static void initializeNodeBlacklist()
    {
	nodeBlacklist = new HashMap<String,Integer>();
	
	for(int i = 0; i < nodeTypeBlacklist.length; i++){
	    nodeBlacklist.put(nodeTypeBlacklist[i], 1);
	}		
    }
    
    public static void initPrinter(String Filename) throws IOException{
        StringTokenizer st = new StringTokenizer(Filename,"/");
        st.nextToken();
        pw = new PrintWriter(new FileWriter("output/"+st.nextToken()+".txt"));
        //pw.println("Type\tLine\tLevel\tCode");
    }
    
    public static  void closePrinter() throws IOException{
        pw.close();
    }

    public static void printCSV (CodeTreeNode node) throws NullPointerException
    {
	String type;
	String codeStr;
	int ascii;
	CommonTree astNode = node.astNode;
	
	if(ASTNodeWrapper.isLeaf(astNode)){	    
	    type = "LEAF_NODE";
	}else{
	    type = ASTNodeWrapper.getType(astNode);
	}
	
	codeStr = node.codeStr;
	
	pw.println(type + '\t' + node.startPos + '\t' + node.level + '\t' + codeStr);

    if(type.contains("STATEMENTS")||type.contains("_STATEMENT")||type.equals("VAR_DECL")||type.equals("FUNCTION_DEF")||type.equals("SELECTION")||type.equals("ITERATION")){
		if(!statement.equals("[")){
	    	//System.out.println(statement.substring(0, statement.length()-2)+"]");
                System.out.println(stmnt);
                System.out.println(lvl);
                func.add(stmnt);
                    System.out.println(func);
                lvl.clear();
                stmnt.clear();

	    }
                
	    statement = "[";
            

    }

    if(type=="LEAF_NODE")
    	if(codeStr.equals(";")||codeStr.equals(",")||codeStr.equals("")||codeStr.equals("(")||codeStr.equals(")")){

    	}else{
	    	if(codeStr.equals("}")|| codeStr.equals("{")){
		    	if(!statement.equals("[")){
                            //System.out.println(statement.substring(0, statement.length()-2)+"]");
                            System.out.println(stmnt);
                            System.out.println(lvl);
                            func.add(stmnt);
                            lvl.clear();
                            stmnt.clear();
                            statement = "["+codeStr+": "+node.level+", ";
                            lvl.add(node.level);
                            stmnt.add(codeStr);
                            func.add(stmnt);
		    	}
		    	statement = "["+codeStr+": "+node.level+", ";
                        lvl.add(node.level);
                        stmnt.add(codeStr);
                        func.add(stmnt);
	    	}else{
	        	statement = statement + codeStr+": "+node.level+", ";
                        lvl.add(node.level);
                        stmnt.add(codeStr);
                        func.add(stmnt);
	    	}

    	}

	//System.out.println("Parent: "+codeStr);
	int numberOfChildren = node.children.size();
	for(int i = 0; i < numberOfChildren; i++){
	    CodeTreeNode child = (CodeTreeNode) node.children.get(i);
	    //System.out.println("Child: "+child.codeStr);
	    printCSV(child);
	}

    }

    private static boolean nodeTypeIsBlacklisted(String type)
    {
	return nodeBlacklist.containsKey(type);
    }
    

}
