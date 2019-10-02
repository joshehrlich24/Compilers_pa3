package edu.jmu.decaf;

import java.util.*;

import edu.jmu.decaf.Token.Type;

/**
 * Concrete Decaf parser class.
 */
class MyDecafParser extends DecafParser
{
    /**
     * Top-level parsing routine. Begins parsing by calling the LL(1) routine
     * for the start symbol (i.e., the Program non-terminal).
     *
     * @param tokens Input token stream
     * @return Parsed abstract syntax tree
     * @throws InvalidSyntaxException Thrown if a syntax error is encountered
     */
    @Override
    public ASTProgram parse(Queue<Token> tokens) throws InvalidSyntaxException
    {
        return parseProgram(tokens);
    }

    /**
     * LL(1) parsing routine for the Program non-terminal.
     *
     * @param tokens Input token stream
     * @return Parsed abstract syntax tree
     * @throws InvalidSyntaxException Thrown if a syntax error is encountered
     */
    public ASTProgram parseProgram(Queue<Token> tokens)
            throws InvalidSyntaxException
    {
        ASTProgram program = new ASTProgram();

        System.out.println(tokens.toString());
        
        
        // grab source info and pass it through
        if (!tokens.isEmpty()) {
            program.setSourceInfo(tokens.peek().source);
        }

        // Program -> VarDecl*
        //
        // (keep reading variable declarations until input is empty)
        //
        
        while(!tokens.isEmpty()) // While we have tokens in the queue
        {
        	 
             if(isNextTokenKeyword(tokens, "def")) //check if the keyword is def(i.e a function declaration)
             {
             	parseFunction(tokens); // if so, parse the function
             }
             	 
        	 else
             {	 
                 program.variables.add(parseVariable(tokens));
                 System.out.println("added");
             }     	 
        }
        return program;
    }

    private ASTVariable parseVariable(Queue<Token> tokens)
            throws InvalidSyntaxException
    {
        // save source info
        SourceInfo source = getCurrentSourceInfo(tokens);

        // VarDecl -> Type ID ';'
        //
        ASTNode.DataType type = parseType(tokens);
        String name = parseID(tokens);
        matchSymbol(tokens, ";");

        // create AST node
        ASTVariable node = new ASTVariable(name, type);
        node.setSourceInfo(source);
        return node;
    }
    
    private ASTFunction parseFunction(Queue<Token> tokens) throws InvalidSyntaxException
    {
    	//ASTFunction myFunction = new ASTFunction(name, returnType, body)
    	
    	ASTFunction myFunction;
    	ASTNode.DataType type;
    	String id = "";
    	//myFunction.parameters = SomeList
    	ASTBlock body;
    	
    	
    	matchKeyword(tokens, "def"); // recognize and discard def
    	System.out.println("Deleted def");
    	System.out.println(tokens.toString());
    	type = parseType(tokens); // get the type
    	id = parseID(tokens); // get the name
    	
    	matchSymbol(tokens, "(");
    	
    	
    	//Checks for parameters
    	while(!isNextTokenSymbol(tokens, ")"))
    	{
    		if(isNextToken(tokens, Type.KEY)) // This means there are parameters in the function definition
        	{
        		parseType(tokens);
        		parseID(tokens);
        		
        		if(isNextTokenSymbol(tokens, ",")) // There are more parameters
        		{
        			matchSymbol(tokens, ",");
        		}
        		
        	}
    	}
    	
    	matchSymbol(tokens, ")");
    	
    	body = parseBlock(tokens);
    	
    	
    	
    	
    	
    	
    	 
    	myFunction = new ASTFunction(id, type, body) ;   	
    	return myFunction;
    }
    
    private ASTBlock parseBlock(Queue<Token> tokens) throws InvalidSyntaxException
    {
    	ASTBlock body;
    	
    	if(isNextToken(tokens, Type.SYM))
    	{
    		if(isNextTokenSymbol(tokens, "{"))
    		{
    			matchSymbol(tokens, "{"); // Look for and discard open bracket(i.e start of a block)
    		}
    	}
    	
    	
    	if(isNextToken(tokens,Type.KEY))
		{	
    		
    		ASTNode.DataType type = parseType(tokens); // returns ASTNode.DataType.INT/BOOL;
    		
    		System.out.println(tokens.toString());
    		
			if(isNextTokenKeyword(tokens, type.toString()))
			{
				
			}
			
		}
    	
    	if(isNextToken(tokens, Type.SYM))
    	{
    		if(isNextTokenSymbol(tokens, "}"))
    		{
    			matchSymbol(tokens, "}"); // Look for and discard closing bracket(i.e end of a block)
    		}
    	}
    
    	
    	
    	
    	body = new ASTBlock();
    	
    	return body;
    }

    private ASTNode.DataType parseType(Queue<Token> tokens)
            throws InvalidSyntaxException
    {
        // Type -> 'int' | 'bool'
        //
        // (parse valid types)
        //
        if (isNextTokenKeyword(tokens, "int")) {
            matchKeyword(tokens, "int");
            return ASTNode.DataType.INT;
        } else if (isNextTokenKeyword(tokens, "bool")) {
            matchKeyword(tokens, "bool");
            return ASTNode.DataType.BOOL;

        // error handling
        } else if (!tokens.isEmpty()) {
            throw new InvalidSyntaxException("Missing type specifier at " +
                    getCurrentSourceInfo(tokens).toString());
        } else {
            throw new InvalidSyntaxException("Missing type specifier at " +
                    "end of input");
        }
    }

    private String parseID(Queue<Token> tokens)
            throws InvalidSyntaxException
    {
        // parse valid identifier
        if (isNextToken(tokens, Token.Type.ID)) {
            return tokens.poll().text;

        // error handling
        } else if (!tokens.isEmpty()) {
            throw new InvalidSyntaxException("Missing identifier at " +
                    getCurrentSourceInfo(tokens).toString());
        } else {
            throw new InvalidSyntaxException("Missing identifier at " +
                    "end of input");
        }
    }

}

