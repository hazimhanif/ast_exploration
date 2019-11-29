

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    static final String [] nodeTypeBlacklist = {
	"SOURCE_FILE", "ITERATION", "FUNCTION_DEF","SELECTION","STATEMENTS"
    };
  
    static HashMap<String,Integer> nodeBlacklist;   
    
    static
    {	
	initializeNodeBlacklist();
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

    public static void printCSV(CodeTreeNode node)
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
	
	if(nodeTypeIsBlacklisted(type)){
	    codeStr = "";
	}else{
	    codeStr = node.codeStr;
	}
	
	pw.println(type + '\t' + node.startPos + '\t' + node.level + '\t' + codeStr);

    if(type.contains("STATEMENTS")||type.contains("_STATEMENT")||type.equals("VAR_DECL")||type.equals("FUNCTION_DEF")||type.equals("SELECTION")||type.equals("ITERATION")){
		if(!statement.equals("[")){
	    	System.out.println(statement.substring(0, statement.length()-2)+"]");
	    }
	    statement = "[";
    }

    if(type=="LEAF_NODE")
    	if(codeStr.equals(";")||codeStr.equals(",")||codeStr.equals("")){

    	}else{
	    	if(codeStr.equals("}")|| codeStr.equals("{")){
		    	if(!statement.equals("[")){
					System.out.println(statement.substring(0, statement.length()-2)+"]");
		    		statement = "["+codeStr+", ";
		    	}
		    	statement = "["+codeStr+", ";
	    	}else{
	        	statement = statement + codeStr+", ";
	    	}

//        if(statement.charAt(statement.length() - 1)==']')
//        	System.out.println(statement);

    	}

	
	int numberOfChildren = node.children.size();
	for(int i = 0; i < numberOfChildren; i++){
	    CodeTreeNode child = (CodeTreeNode) node.children.get(i);
	    printCSV(child);
	}

    }

    private static boolean nodeTypeIsBlacklisted(String type)
    {
	return nodeBlacklist.containsKey(type);
    }
    

}
