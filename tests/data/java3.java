/*
 * Created on 27. sep.. 2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.interpreter.adapter.ecj;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.spoofax.DebugUtil;
import org.spoofax.NotImplementedException;
import org.spoofax.interpreter.terms.BasicStrategoArrayList;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoInt;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoPlaceholder;
import org.spoofax.interpreter.terms.IStrategoReal;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.IStrategoTuple;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.interpreter.terms.ITermPrinter;
import org.spoofax.interpreter.terms.InlinePrinter;

public class ECJFactory implements ITermFactory {

    private static final int ARRAY_ACCESS = 1;
    private static final int PACKAGE_DECLARATION = 2;
    private static final int NONE = 3;
    private static final int BOOLEAN_LITERAL = 4;
    private static final int BOOLEAN_TYPE = 5;
    private static final int BREAK_STATEMENT = 6;
    private static final int CATCH_CLAUSE = 7;
    private static final int CHARACTER_LITERAL = 8;
    private static final int CLASS_INSTANCE_CREATION = 9;
    private static final int ANNOTATION_TYPE_DECLARATION = 10;
    private static final int ENUM_DECLARATION = 11;
    private static final int TYPE_DECLARATION = 12;
    private static final int ANNOTATION_TYPE_MEMBER_DECLARATION = 13;
    private static final int ENUM_CONSTANT_DECLARATION = 14;
    private static final int FIELD_DECLARATION = 15;
    private static final int INITIALIZER = 16;
    private static final int METHOD_DECLARATION = 17;
    private static final int BLOCK_COMMENT = 18;
    private static final int JAVADOC = 19;
    private static final int LINE_COMMENT = 20;
    private static final int COMPILATION_UNIT = 21;
    private static final int MARKER_ANNOTATION = 22;
    private static final int NORMAL_ANNOTATION = 23;
    private static final int SINGLE_MEMBER_ANNOTATION = 24;
    private static final int ARRAY_CREATION = 25;
    private static final int ARRAY_INITIALIZER = 26;
    private static final int ASSIGNMENT = 27;
    private static final int CAST_EXPRESSION = 28;
    private static final int CONDITIONAL_EXPRESSION = 29;
    private static final int FIELD_ACCESS = 30;
    private static final int INFIX_EXPRESSION = 31;
    private static final int INSTANCEOF_EXPRESSION = 32;
    private static final int METHOD_INVOCATION = 33;
    private static final int QUALIFIED_NAME = 34;
    private static final int SIMPLE_NAME = 35;
    private static final int NULL_LITERAL = 36;
    private static final int NUMBER_LITERAL = 37;
    private static final int PARENTHESIZED_EXPRESSION = 38;
    private static final int POSTFIX_EXPRESSION = 39;
    private static final int PREFIX_EXPRESSION = 40;
    private static final int STRING_LITERAL = 41;
    private static final int SUPER_FIELD_ACCESS = 42;
    private static final int THIS_EXPRESSION = 43;
    private static final int VARIABLE_DECLARATION_EXPRESSION = 44;
    private static final int IMPORT_DECLARATION = 45;
    private static final int MEMBER_REF = 46;
    private static final int MEMBER_VALUE_PAIR = 47;
    private static final int METHOD_REF = 48;
    private static final int METHOD_REF_PARAMETER = 49;
    private static final int MODIFIER = 50;
    private static final int MODIFIER_KEYWORD = 51;
    private static final int POSTFIX_EXPRESSION_OPERATOR = 52;
    private static final int PREFIX_EXPRESSION_OPERATOR = 53;
    private static final int ASSERT_STATEMENT = 54;
    private static final int BLOCK = 55;
    private static final int CONSTRUCTOR_INVOCATION = 56;
    private static final int CONTINUE_STATEMENT = 57;
    private static final int DO_STATEMENT = 58;
    private static final int EMPTY_STATEMENT = 59;
    private static final int ENHANCED_FOR_STATEMENT = 60;
    private static final int EXPRESSION_STATEMENT = 61;
    private static final int FOR_STATEMENT = 62;
    private static final int IF_STATEMENT = 63;
    private static final int LABELED_STATEMENT = 64;
    private static final int RETURN_STATEMENT = 65;
    private static final int SUPER_CONSTRUCTOR_INVOCATION = 66;
    private static final int SWITCH_CASE = 67;
    private static final int SWITCH_STATEMENT = 68;
    private static final int SYNCHRONIZED_STATEMENT = 69;
    private static final int THROW_STATEMENT = 70;
    private static final int TRY_STATEMENT = 71;
    private static final int TYPE_DECLARATION_STATEMENT = 72;
    private static final int TYPE_LITERAL = 73;
    private static final int VARIABLE_DECLARATION_STATEMENT = 74;
    private static final int WHILE_STATEMENT = 75;
    private static final int SUPER_METHOD_INVOCATION = 76;
    private static final int TAG_ELEMENT = 77;
    private static final int TEXT_ELEMENT = 78;
    private static final int ARRAY_TYPE = 79;
    private static final int DOUBLE_TYPE = 80;
    private static final int FLOAT_TYPE = 81;
    private static final int INT_TYPE = 82;
    private static final int LONG_TYPE = 83;
    private static final int PARAMETERIZED_TYPE = 84;
    private static final int PRIMITIVE_TYPE = 85;
    private static final int QUALIFIED_TYPE = 86;
    private static final int SIMPLE_TYPE = 87;
    private static final int WILDCARD_TYPE = 88;
    private static final int TYPE_PARAMETER = 89;
    private static final int SINGLE_VARIABLE_DECLARATION = 90;
    private static final int VARIABLE_DECLARATION_FRAGMENT = 91;
    private static final int BYTE_TYPE = 92;
    private static final int IMPORT_REFERENCE = 93;
    private static final int ANONYMOUS_CLASS_DECLARATION = 94;
    private static final int ASSIGNMENT_OPERATOR = 95;
	private static ASTMatcher astMatcher;
        
    private Map<String,Integer> ctorNameToIndexMap;
    private AST ast;
    
    public ECJFactory(AST ast) {
        this.ast = ast;
        initCtorMap();
    }
    
    public ECJFactory() {
        initCtorMap();
    }
    
    private void initCtorMap() {
        ctorNameToIndexMap = new HashMap<String,Integer>();
        ctorNameToIndexMap.put("ArrayAccess", ARRAY_ACCESS);
        ctorNameToIndexMap.put("PackageDeclaration", PACKAGE_DECLARATION);
        ctorNameToIndexMap.put("BooleanLiteral", BOOLEAN_LITERAL);
        ctorNameToIndexMap.put("BooleanType", BOOLEAN_TYPE);
        ctorNameToIndexMap.put("ByteType", BYTE_TYPE);
        ctorNameToIndexMap.put("BreakStatement", BREAK_STATEMENT);
        ctorNameToIndexMap.put("CatchClause", CATCH_CLAUSE);
        ctorNameToIndexMap.put("CharacterLiteral", CHARACTER_LITERAL);
        ctorNameToIndexMap.put("ClassInstanceCreation", CLASS_INSTANCE_CREATION);
        ctorNameToIndexMap.put("AnnotationTypeDeclaration", ANNOTATION_TYPE_DECLARATION);
        ctorNameToIndexMap.put("EnumDeclaration", ENUM_DECLARATION);
        ctorNameToIndexMap.put("TypeDeclaration", TYPE_DECLARATION);
        ctorNameToIndexMap.put("AnnotationTypeMemberDeclaration", ANNOTATION_TYPE_MEMBER_DECLARATION);
        ctorNameToIndexMap.put("EnumConstantDeclaration", ENUM_CONSTANT_DECLARATION);
        ctorNameToIndexMap.put("FieldDeclaration", FIELD_DECLARATION);
        ctorNameToIndexMap.put("Initializer", INITIALIZER);
        ctorNameToIndexMap.put("MethodDeclaration", METHOD_DECLARATION);
        ctorNameToIndexMap.put("BlockComment", BLOCK_COMMENT);
        ctorNameToIndexMap.put("Javadoc", JAVADOC);
        ctorNameToIndexMap.put("LineComment", LINE_COMMENT);
        ctorNameToIndexMap.put("CompilationUnit", COMPILATION_UNIT);
        ctorNameToIndexMap.put("MarkerAnnotation", MARKER_ANNOTATION);
        ctorNameToIndexMap.put("NormalAnnotation", NORMAL_ANNOTATION);
        ctorNameToIndexMap.put("SingleMemberAnnotation", SINGLE_MEMBER_ANNOTATION);
        ctorNameToIndexMap.put("ArrayCreation", ARRAY_CREATION);
        ctorNameToIndexMap.put("ArrayInitializer", ARRAY_INITIALIZER);
        ctorNameToIndexMap.put("Assignment", ASSIGNMENT);
        ctorNameToIndexMap.put("CastExpression", CAST_EXPRESSION);
        ctorNameToIndexMap.put("ConditionalExpression", CONDITIONAL_EXPRESSION);
        ctorNameToIndexMap.put("FieldAccess", FIELD_ACCESS);
        ctorNameToIndexMap.put("InfixExpression", INFIX_EXPRESSION);
        ctorNameToIndexMap.put("InstanceofExpression", INSTANCEOF_EXPRESSION);
        ctorNameToIndexMap.put("MethodInvocation", METHOD_INVOCATION);
        ctorNameToIndexMap.put("QualifiedName", QUALIFIED_NAME);
        ctorNameToIndexMap.put("SimpleName", SIMPLE_NAME);
        ctorNameToIndexMap.put("NullLiteral", NULL_LITERAL);
        ctorNameToIndexMap.put("NumberLiteral", NUMBER_LITERAL);
        ctorNameToIndexMap.put("ParenthesizedExpression", PARENTHESIZED_EXPRESSION);
        ctorNameToIndexMap.put("PostfixExpression", POSTFIX_EXPRESSION);
        ctorNameToIndexMap.put("PrefixExpression", PREFIX_EXPRESSION);
        ctorNameToIndexMap.put("StringLiteral", STRING_LITERAL);
        ctorNameToIndexMap.put("SuperFieldAccess", SUPER_FIELD_ACCESS);
        ctorNameToIndexMap.put("ThisExpression", THIS_EXPRESSION);
        ctorNameToIndexMap.put("VariableDeclarationExpression", VARIABLE_DECLARATION_EXPRESSION);
        ctorNameToIndexMap.put("ImportDeclaration", IMPORT_DECLARATION);
        ctorNameToIndexMap.put("MemberRef", MEMBER_REF);
        ctorNameToIndexMap.put("MemberValuePair", MEMBER_VALUE_PAIR);
        ctorNameToIndexMap.put("MethodRef", METHOD_REF);
        ctorNameToIndexMap.put("MethodRefParameter", METHOD_REF_PARAMETER);
        ctorNameToIndexMap.put("Modifier", MODIFIER);
        ctorNameToIndexMap.put("ModifierKeyword", MODIFIER_KEYWORD);
        ctorNameToIndexMap.put("PackageDeclaration", PACKAGE_DECLARATION);
        ctorNameToIndexMap.put("PostfixExpressionOperator", POSTFIX_EXPRESSION_OPERATOR);
        ctorNameToIndexMap.put("PrefixExpressionOperator", PREFIX_EXPRESSION_OPERATOR);
        ctorNameToIndexMap.put("AssertStatement", ASSERT_STATEMENT);
        ctorNameToIndexMap.put("Block", BLOCK);
        ctorNameToIndexMap.put("ConstructorInvocation", CONSTRUCTOR_INVOCATION);
        ctorNameToIndexMap.put("ContinueStatement", CONTINUE_STATEMENT);
        ctorNameToIndexMap.put("DoStatement", DO_STATEMENT);
        ctorNameToIndexMap.put("EmptyStatement", EMPTY_STATEMENT);
        ctorNameToIndexMap.put("EnhancedForStatement", ENHANCED_FOR_STATEMENT);
        ctorNameToIndexMap.put("ExpressionStatement", EXPRESSION_STATEMENT);
        ctorNameToIndexMap.put("ForStatement", FOR_STATEMENT);
        ctorNameToIndexMap.put("IfStatement", IF_STATEMENT);
        ctorNameToIndexMap.put("LabeledStatement", LABELED_STATEMENT);
        ctorNameToIndexMap.put("ReturnStatement", RETURN_STATEMENT);
        ctorNameToIndexMap.put("SuperConstructorInvocation", SUPER_CONSTRUCTOR_INVOCATION);
        ctorNameToIndexMap.put("SwitchCase", SWITCH_CASE);
        ctorNameToIndexMap.put("SwitchStatement", SWITCH_STATEMENT);
        ctorNameToIndexMap.put("SynchronizedStatement", SYNCHRONIZED_STATEMENT);
        ctorNameToIndexMap.put("ThrowStatement", THROW_STATEMENT);
        ctorNameToIndexMap.put("TryStatement", TRY_STATEMENT);
        ctorNameToIndexMap.put("TypeDeclarationStatement", TYPE_DECLARATION_STATEMENT);
        ctorNameToIndexMap.put("TypeLiteral", TYPE_LITERAL);
        ctorNameToIndexMap.put("VariableDeclarationStatement", VARIABLE_DECLARATION_STATEMENT);
        ctorNameToIndexMap.put("WhileStatement", WHILE_STATEMENT);
        ctorNameToIndexMap.put("SuperMethodInvocation", SUPER_METHOD_INVOCATION);
        ctorNameToIndexMap.put("TagElement", TAG_ELEMENT);
        ctorNameToIndexMap.put("TextElement", TEXT_ELEMENT);
        ctorNameToIndexMap.put("ArrayType", ARRAY_TYPE);
        ctorNameToIndexMap.put("DoubleType", DOUBLE_TYPE);
        ctorNameToIndexMap.put("FloatType", FLOAT_TYPE);
        ctorNameToIndexMap.put("IntType", INT_TYPE);
        ctorNameToIndexMap.put("LongType", LONG_TYPE);
        ctorNameToIndexMap.put("ParameterizedType", PARAMETERIZED_TYPE);
        ctorNameToIndexMap.put("PrimitiveType", PRIMITIVE_TYPE);
        ctorNameToIndexMap.put("QualifiedType", QUALIFIED_TYPE);
        ctorNameToIndexMap.put("SimpleType", SIMPLE_TYPE);
        ctorNameToIndexMap.put("WildcardType", WILDCARD_TYPE);
        ctorNameToIndexMap.put("TypeParameter", TYPE_PARAMETER);
        ctorNameToIndexMap.put("SingleVariableDeclaration", SINGLE_VARIABLE_DECLARATION);
        ctorNameToIndexMap.put("VariableDeclarationFragment", VARIABLE_DECLARATION_FRAGMENT);
        ctorNameToIndexMap.put("None", NONE);
        ctorNameToIndexMap.put("ImportReference", IMPORT_REFERENCE);
        ctorNameToIndexMap.put("AnonymousClassDeclaration", ANONYMOUS_CLASS_DECLARATION);
        ctorNameToIndexMap.put("AssignmentOperator", ASSIGNMENT_OPERATOR);
    }
    
    public IStrategoTerm parseFromFile(String path) throws IOException {
        throw new NotImplementedException();
    }

    public IStrategoTerm parseFromStream(InputStream inputStream) throws IOException {
        throw new NotImplementedException();
    }

    public IStrategoTerm parseFromString(String text) {
        if(text.equals("()")) {
            return makeTuple();
        } else if(text.charAt(0) == '"') {
            return makeString(text.substring(1).substring(0, text.length() - 2));
        }
        throw new NotImplementedException();
    }

    public void unparseToFile(IStrategoTerm t, OutputStream ous) throws IOException {
        InlinePrinter pp = new InlinePrinter();
        t.prettyPrint(pp);
        ous.write(pp.getString().getBytes());
    }

    public void unparseToFile(IStrategoTerm t, Writer out) throws IOException {
        ITermPrinter tp = new InlinePrinter();
        t.prettyPrint(tp);
        out.write(tp.getString());
    }

    public boolean hasConstructor(String s, int i) {
    	// FIXME also check generic factory
    	return ctorNameToIndexMap.containsKey(s);
    }

    private List<ASTNode> getAnnotations(IStrategoTerm term) {
        return ((WrappedASTNodeList)term).getWrappee();
    }

    private Javadoc getJavadoc(IStrategoTerm term) {
        return ((WrappedJavadoc)term).getWrappee();
    }
    
    public IStrategoPlaceholder makePlaceholder(IStrategoTerm template) {
        throw new NotImplementedException();
    }

    @Deprecated
    public IStrategoAppl makeAppl(IStrategoConstructor ctr, IStrategoList terms) {
        return makeAppl(ctr, terms.getAllSubterms());
    }
    
    public IStrategoTerm annotateTerm(IStrategoTerm term, IStrategoList annotations) {
    	if(term instanceof ECJAnnoWrapper) {
    		return new ECJAnnoWrapper(((ECJAnnoWrapper)term).getWrappee(), annotations);
    	} else {
    		return new ECJAnnoWrapper(term, annotations);
    	}
    }

    public IStrategoAppl makeAppl(IStrategoConstructor ctr, IStrategoTerm... kids) {
        IStrategoAppl t = constructASTNode(ctr, kids);
        if(t == null) {
            if(DebugUtil.isDebugging()) {
                System.err.println("Generic fallback for:");
                System.err.println("Construct: " + ctr.getName() + "/" + ctr.getArity() + " with " + kids.length + " kids");
                for(int i = 0; i < kids.length; i++) {
                    if(kids[i] instanceof WrappedASTNodeList) {
                        WrappedASTNodeList l = (WrappedASTNodeList)kids[i];
                        if(!l.isEmpty()) 
                            System.err.println("  [" + l.get(0) + "]");
                        else
                            System.err.println("  " + l + " - empty");
                    } else
                        System.err.println("  " + kids[i]);
                }
            }
            return ctr.instantiate(this, kids);
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    private IStrategoAppl constructASTNode(IStrategoConstructor ctr, IStrategoTerm[] kids) {
        int index = ctorNameToIndex(ctr);
        switch(index) {
        case ANNOTATION_TYPE_DECLARATION: {
            if((!isJavadoc(kids[0]) && !isNone(kids[0]))
                    || !isExtendedModifierList(kids[1]) 
                    || !isSimpleName(kids[2]) 
                    || !isBodyDeclarationList(kids[3]))
                return null;
            AnnotationTypeDeclaration x = ast.newAnnotationTypeDeclaration();
            if(isNone(kids[0]))
                x.setJavadoc(null);
            else
                x.setJavadoc(asJavadoc(kids[0]));
            x.modifiers().addAll(asExtendedModifierList(kids[1]));
            x.setName(asSimpleName(kids[2]));
            x.bodyDeclarations().addAll(asBodyDeclarationList(kids[3]));
            return wrap(x);
        }
        case ANNOTATION_TYPE_MEMBER_DECLARATION: {
            if((!isJavadoc(kids[0]) && !isNone(kids[0]))
                    || !isModifierList(kids[1]) 
                    || !isType(kids[2]) 
                    || !isSimpleName(kids[3]) 
                    || (!isExpression(kids[4]) && !isNone(kids[4])))
                return null;
            AnnotationTypeMemberDeclaration x = ast.newAnnotationTypeMemberDeclaration();
            x.setJavadoc(asJavadoc(kids[0]));
            x.modifiers().addAll(asModifierList(kids[1]));
            x.setType(asType(kids[2]));
            x.setName(asSimpleName(kids[3]));
            if(isNone(kids[4]))
                x.setDefault(null);
            else
                x.setDefault(asExpression(kids[4]));
            return wrap(x);
        }
        case ANONYMOUS_CLASS_DECLARATION: {
            if(!isBodyDeclarationList(kids[0]))
                return null;
            AnonymousClassDeclaration x = ast.newAnonymousClassDeclaration();
            x.bodyDeclarations().addAll(asBodyDeclarationList(kids[0]));
            return wrap(x);
        }
        case ARRAY_ACCESS: { 
            if(!isExpression(kids[0]) || !isExpression(kids[1]))
                return null;
            ArrayAccess x = ast.newArrayAccess();
            x.setArray(asExpression(kids[0]));
            x.setIndex(asExpression(kids[1]));
            return wrap(x);
        }
        case ARRAY_CREATION: {
            if(!isArrayType(kids[0]) 
                    || !isExpressionList(kids[1]) 
                    || (!isArrayInitializer(kids[2]) && !isNone(kids[2])))
                return null;
            ArrayCreation x = ast.newArrayCreation();
            x.setType(asArrayType(kids[0]));
            x.dimensions().addAll(asExpressionList(kids[1]));
            if(isNone(kids[2]))
                x.setInitializer(null);
            else
                x.setInitializer(asArrayInitializer(kids[2]));
            return wrap(x);
        }
        case ARRAY_INITIALIZER: {
            if(!isExpressionList(kids[0]))
                return null;
            ArrayInitializer x = ast.newArrayInitializer();
            x.expressions().addAll(asExpressionList(kids[0]));
            return wrap(x);
        }
        case ARRAY_TYPE: {
            if(!isType(kids[0]) 
                    || !isInt(kids[1]) 
                    || !isType(kids[2]))
                return null;
            ArrayType x = ast.newArrayType(asType(kids[2]), asInt(kids[1]));
            x.setComponentType(asType(kids[0]));
            return wrap(x);
        }
        case ASSERT_STATEMENT: {
            if(!isExpression(kids[0]) || !(isExpression(kids[1]) || isNone(kids[1])))
                return null;
            AssertStatement x = ast.newAssertStatement();
            x.setExpression(asExpression(kids[0]));
            if(isNone(kids[1]))
            	x.setMessage(null);
            else 
            	x.setMessage(asExpression(kids[1]));
            return wrap(x);
        }
        case ASSIGNMENT: {
            if(!isAssignmentOperator(kids[0])
            		|| !isExpression(kids[1]) 
            		|| !isExpression(kids[2]))
                return null;
            Assignment x = ast.newAssignment();
            x.setOperator(asAssignmentOperator(kids[0]));
            x.setLeftHandSide(asExpression(kids[1]));
            x.setRightHandSide(asExpression(kids[2]));
            return wrap(x);
        }
        case ASSIGNMENT_OPERATOR: {
            if(!isString(kids[0]))
                return null;
            return wrap(Assignment.Operator.toOperator(asString(kids[0])));
        }
        case BLOCK: {
            if(!isStatementList(kids[0]))
                return null;
            Block x = ast.newBlock();
            x.statements().addAll(asStatementList(kids[0]));
            return wrap(x);
        }
        case BLOCK_COMMENT: {
            BlockComment x = ast.newBlockComment();
            return wrap(x);
        }
        case BOOLEAN_LITERAL: {
            if(!isInt(kids[0]))
                return null;
            return wrap(ast.newBooleanLiteral(asInt(kids[0]) == 1));
        }
        case BOOLEAN_TYPE: {
            return wrap(ast.newPrimitiveType(PrimitiveType.BOOLEAN));
        }
        case BREAK_STATEMENT: {
            if(!isSimpleName(kids[0]) && !isNone(kids[0]))
                return null;
            BreakStatement x = ast.newBreakStatement();
            if(isNone(kids[0]))
                x.setLabel(null);
            else
                x.setLabel(asSimpleName(kids[0]));
            return wrap(x);
        }
        case BYTE_TYPE: {
            return wrap(ast.newPrimitiveType(PrimitiveType.BYTE));
        }
        case CAST_EXPRESSION: {
            if(!isType(kids[0]) || !isExpression(kids[1]))
                return null;
            CastExpression x = ast.newCastExpression();
            x.setType(asType(kids[0]));
            x.setExpression(asExpression(kids[1]));
            return wrap(x);
        }
        case CATCH_CLAUSE: {
            if(!isSingleVariableDeclaration(kids[0]) || !isBlock(kids[1]))
                return null;
            CatchClause x = ast.newCatchClause();
            x.setException(asSingleVariableDeclaration(kids[0]));
            x.setBody(asBlock(kids[1]));
            return wrap(x);
        }
        case CHARACTER_LITERAL: {
            if(!isInt(kids[0]))
                return null;
            CharacterLiteral x = ast.newCharacterLiteral();
            x.setCharValue((char)asInt(kids[0]));
            return wrap(x);
        }
        case CLASS_INSTANCE_CREATION: {
            if((!isExpression(kids[0]) && !isNone(kids[0])) 
                    || !isType(kids[1]) 
                    || (!isAnonymousClassDeclaration(kids[2]) && !isNone(kids[2]))  
                    || !isExpressionList(kids[3]))
                return null;
            ClassInstanceCreation x = ast.newClassInstanceCreation();
            if(isNone(kids[0])) 
                x.setExpression(null);
            else 
                x.setExpression(asExpression(kids[0]));
            x.setType(asType(kids[1]));
            if(isNone(kids[2]))
                x.setAnonymousClassDeclaration(null);
            else
                x.setAnonymousClassDeclaration(asAnonymousClassDeclaration(kids[2]));
            x.arguments().addAll(asExpressionList(kids[3]));
            return wrap(x);
        }
        case COMPILATION_UNIT: {
            if((!isPackageDeclaration(kids[0]) && !isNone(kids[0])) 
                    || !isImportDeclarationList(kids[1]) 
                    || !isAbstractTypeDeclarationList(kids[2]))
                return null;
            CompilationUnit x = ast.newCompilationUnit();
            if(isNone(kids[0]))
                x.setPackage(null);
            else 
                x.setPackage(asPackageDeclaration(kids[0]));
            x.imports().addAll(asImportDeclarationList(kids[1]));
            x.types().addAll(asAbstractTypeDeclarationList(kids[2]));
            return wrap(x);
        }
        case CONDITIONAL_EXPRESSION: {
            if(!isExpression(kids[0]) 
                    || !isExpression(kids[1]) 
                    || !isExpression(kids[2]))
                return null;
            ConditionalExpression x = ast.newConditionalExpression();
            x.setExpression(asExpression(kids[0]));
            x.setThenExpression(asExpression(kids[1]));
            x.setElseExpression(asExpression(kids[2]));
            return wrap(x);
        }
        case CONSTRUCTOR_INVOCATION: {
            if(!isExpressionList(kids[0]))
                return null;
            ConstructorInvocation x = ast.newConstructorInvocation();
            x.arguments().addAll(asExpressionList(kids[0]));
            return wrap(x);
        }
        case CONTINUE_STATEMENT: {
            if(!isSimpleName(kids[0]) && !isNone(kids[0]))
                return null;
            ContinueStatement x = ast.newContinueStatement();
            if(isNone(kids[0]))
                x.setLabel(null);
            else
                x.setLabel(asSimpleName(kids[0]));
            return wrap(x);
        }
        case DO_STATEMENT: {
            DoStatement x = ast.newDoStatement();
            x.setExpression(asExpression(kids[0]));
            x.setBody(asStatement(kids[1]));
            return wrap(x);
        }
        case DOUBLE_TYPE: {
            return wrap(ast.newPrimitiveType(PrimitiveType.DOUBLE));
        }
        case EMPTY_STATEMENT: {
            return wrap(ast.newEmptyStatement());
        }
        case ENHANCED_FOR_STATEMENT: {
            if(!isSingleVariableDeclaration(kids[0]) 
                    || !isExpression(kids[1])
                    || !isStatement(kids[2]))
                return null;
            EnhancedForStatement x = ast.newEnhancedForStatement();
            x.setParameter(asSingleVariableDeclaration(kids[0]));
            x.setExpression(asExpression(kids[1]));
            x.setBody(asStatement(kids[2]));
            return wrap(x);
        }
        case ENUM_CONSTANT_DECLARATION: {
            if((!isJavadoc(kids[0]) && !isNone(kids[0]))
                    || !isModifierList(kids[1]) 
                    || !isSimpleName(kids[2]) 
                    || !isExpressionList(kids[3]) 
                    || !isAnonymousClassDeclaration(kids[4]))
                return null;
            EnumConstantDeclaration x = ast.newEnumConstantDeclaration();
            if(isNone(kids[0]))
                x.setJavadoc(null);
            else
                x.setJavadoc(asJavadoc(kids[0]));
            x.modifiers().addAll(asModifierList(kids[1]));
            x.setName(asSimpleName(kids[2]));
            x.arguments().addAll(asExpressionList(kids[3]));
            x.setAnonymousClassDeclaration(asAnonymousClassDeclaration(kids[4]));
            return wrap(x);
        }
        case ENUM_DECLARATION: {
            if((!isJavadoc(kids[0]) && !isNone(kids[0]))
                    || !isModifierList(kids[1])
                    || !isSimpleName(kids[2])
                    || !isTypeList(kids[3])
                    || !isEnumConstantDeclarationList(kids[4])
                    || !isBodyDeclarationList(kids[5]))
                return null;
            EnumDeclaration x = ast.newEnumDeclaration();
            if(isNone(kids[0]))
                x.setJavadoc(null);
            else
                x.setJavadoc(asJavadoc(kids[0]));
            x.modifiers().addAll(asModifierList(kids[1]));
            x.setName(asSimpleName(kids[2]));
            x.superInterfaceTypes().addAll(asTypeList(kids[3]));
            x.enumConstants().addAll(asEnumConstantDeclarationList(kids[4]));
            x.bodyDeclarations().addAll(asBodyDeclarationList(kids[5]));
            return wrap(x);
        }
        case EXPRESSION_STATEMENT: {
            if(!isExpression(kids[0]))
                return null;
            return wrap(ast.newExpressionStatement(asExpression(kids[0])));
        }
        case FIELD_ACCESS: {
            if(!isExpression(kids[0]) || !isSimpleName(kids[1]))
                return null;
            FieldAccess x = ast.newFieldAccess();
            x.setExpression(asExpression(kids[0]));
            x.setName(asSimpleName(kids[1]));
            return wrap(x);
        }
        case FIELD_DECLARATION: {
            if((!isJavadoc(kids[0]) && !isNone(kids[0]))
                    || !isModifierList(kids[1])
                    || !isType(kids[2])
                    || !isNonEmptyVariableDeclarationFragmentList(kids[3]))
                return null;
            List y = asFragmentList(kids[3]);
            FieldDeclaration x = ast.newFieldDeclaration((VariableDeclarationFragment)y.remove(0));
            if(isNone(kids[0]))
                x.setJavadoc(null);
            else
                x.setJavadoc(asJavadoc(kids[0]));
            x.modifiers().addAll(asModifierList(kids[1]));
            x.setType(asType(kids[2]));
            x.fragments().addAll(y);
            return wrap(x);
        }
        case FLOAT_TYPE: {
            return wrap(ast.newPrimitiveType(PrimitiveType.FLOAT));
        }
        case FOR_STATEMENT: {
            if(!isExpressionList(kids[0]) 
                    || (!isExpression(kids[1]) && !isNone(kids[1]))
                    || !isExpressionList(kids[2])
                    || !isStatement(kids[3]))
                return null;
            ForStatement x = ast.newForStatement();
            x.initializers().addAll(asExpressionList(kids[0]));
            if(isNone(kids[1]))
                x.setExpression(null);
            else
                x.setExpression(asExpression(kids[1]));
            x.updaters().addAll(asExpressionList(kids[2]));
            x.setBody(asStatement(kids[3]));
            return wrap(x);
        }
        case IF_STATEMENT: {
            if(!isExpression(kids[0])
                    || !isStatement(kids[1])
                    || (!isStatement(kids[2]) && !isNone(kids[2])))
                return null;
            IfStatement x = ast.newIfStatement();
            x.setExpression(asExpression(kids[0]));
            x.setThenStatement(asStatement(kids[1]));
            if(isNone(kids[2]))
                x.setElseStatement(null);
            else
                x.setElseStatement(asStatement(kids[2]));
            return wrap(x);
        }
        case IMPORT_DECLARATION: {
            if(!isName(kids[0]) && !isInt(kids[1]) && !isInt(kids[2]))
                return null;
            ImportDeclaration x = ast.newImportDeclaration();
            x.setName(asName(kids[0]));
            x.setStatic(((IStrategoInt)kids[1]).intValue() > 0);
            x.setOnDemand(((IStrategoInt)kids[2]).intValue() > 0);
            return wrap(x);
        }
        case INFIX_EXPRESSION: {
            if(!isOperator(kids[0])
                    || !isExpression(kids[1])
                    || !isExpression(kids[2])
                    || !isExpressionList(kids[3]))
                return null;
            InfixExpression x = ast.newInfixExpression();
            x.setOperator(asOperator(kids[0]));
            x.setLeftOperand(asExpression(kids[1]));
            x.setRightOperand(asExpression(kids[2]));
            x.extendedOperands().addAll(asExpressionList(kids[3]));
            return wrap(x);
        }
        case INITIALIZER: {
            if(!isBlock(kids[0]))
                return null;
            Initializer x = ast.newInitializer();
            x.setBody(asBlock(kids[0]));
            return wrap(x);
            
        }
        case INSTANCEOF_EXPRESSION: {
            if(!isExpression(kids[0])
                    || !isType(kids[1]))
                return null;
            InstanceofExpression x = ast.newInstanceofExpression();
            x.setLeftOperand(asExpression(kids[0]));
            x.setRightOperand(asType(kids[1]));
            return wrap(x);
        }
        case INT_TYPE: {
            return wrap(ast.newPrimitiveType(PrimitiveType.INT));
        }
        case JAVADOC: {
            if(!isTagElementList(kids[0]))
                return null;
            Javadoc x = ast.newJavadoc();
            x.tags().addAll(asTagElementList(kids[0]));
            return wrap(x);
        }
        case LABELED_STATEMENT: {
            if(!isSimpleName(kids[0]) 
                    || !isStatement(kids[1]))
                return null;
            LabeledStatement x = ast.newLabeledStatement();
            x.setLabel(asSimpleName(kids[0]));
            x.setBody(asStatement(kids[1]));
            return wrap(x);
        }
        case LINE_COMMENT: {
            LineComment x = ast.newLineComment();
            return wrap(x);
        }
        case LONG_TYPE: {
            return wrap(ast.newPrimitiveType(PrimitiveType.INT));
        }
        case MARKER_ANNOTATION: {
            if(!isName(kids[0]))
                return null;
            MarkerAnnotation x = ast.newMarkerAnnotation();
            x.setTypeName(asName(kids[0]));
            return wrap(x);
            
        }
        case MEMBER_REF: {
            if(!isSimpleName(kids[0]) || !isName(kids[1]))
                return null;
            MemberRef x = ast.newMemberRef();
            x.setName(asSimpleName(kids[0]));
            x.setQualifier(asName(kids[1]));
            return wrap(x);
        }
        case MEMBER_VALUE_PAIR: {
            if(!isSimpleName(kids[0]) || !isExpression(kids[1]))
                return null;
            MemberValuePair x = ast.newMemberValuePair();
            x.setName(asSimpleName(kids[0]));
            x.setValue(asExpression(kids[1]));
            return wrap(x);
        }
        case METHOD_DECLARATION: {
            if((!isJavadoc(kids[0]) && !isNone(kids[0]))
                    || !isExtendedModifierList(kids[1])
                    || (!isType(kids[2]) && !isNone(kids[2]))
                    || !isTypeParameterList(kids[3])
                    || !isSimpleName(kids[4])
                    || !isSingleVariableDeclarationList(kids[5])
                    || !isNameList(kids[6])
                    || (!isBlock(kids[7]) && !isNone(kids[7])))
                return null;
            MethodDeclaration x = ast.newMethodDeclaration();
            if(isNone(kids[0]))
                x.setJavadoc(null);
            else
                x.setJavadoc(asJavadoc(kids[0]));
            x.modifiers().addAll(asExtendedModifierList(kids[1]));
            if(isNone(kids[2])) {
                x.setReturnType2(null);
                x.setConstructor(true);
            } else
                x.setReturnType2(asType(kids[2]));
            x.typeParameters().addAll(asTypeParameterList(kids[3]));
            x.setName(asSimpleName(kids[4]));
            x.parameters().addAll(asSingleVariableDeclarationList(kids[5]));
            x.thrownExceptions().addAll(asNameList(kids[6]));
            if(isNone(kids[7]))
                x.setBody(null);
            else
                x.setBody(asBlock(kids[7]));
            return wrap(x);
        }
        case METHOD_INVOCATION: {
            if((!isExpression(kids[0]) && !isNone(kids[0]))
                    || !isSimpleName(kids[1])
                    || !isTypeList(kids[2])
                    || !isExpressionList(kids[3]))
                return null;
            MethodInvocation x = ast.newMethodInvocation();
            if(isNone(kids[0]))
                x.setExpression(null);
            else 
                x.setExpression(asExpression(kids[0]));
            x.setName(asSimpleName(kids[1]));
            x.typeArguments().addAll(asTypeList(kids[2]));
            x.arguments().addAll(asExpressionList(kids[3]));
            return wrap(x);
        }
        case METHOD_REF: {
            if(!isSimpleName(kids[0])
                    || !isName(kids[1])
                    || !isMethodRefParameterList(kids[2]))
                return null;
            MethodRef x = ast.newMethodRef();
            x.setName(asSimpleName(kids[0]));
            x.setQualifier(asName(kids[1]));
            x.parameters().addAll(asMethodRefParameterList(kids[2]));
            return wrap(x);
        }
        case METHOD_REF_PARAMETER: {
            if(!isType(kids[0])
                    || !(isSimpleName(kids[1]) || isNone(kids[1])))
                return null;
            MethodRefParameter x = ast.newMethodRefParameter();
            x.setType(asType(kids[0]));
            if(isNone(kids[1]))
            	x.setName(null);
            else
            	x.setName(asSimpleName(kids[1]));
            return wrap(x);
        }
        case MODIFIER: {
            if(!isModifierKeyword(kids[0]))
                return null;
            return wrap(ast.newModifier(asModifierKeyword(kids[0])));
        }
        case MODIFIER_KEYWORD: {
            if(!isInt(kids[0]))
                return null;
            return wrap(Modifier.ModifierKeyword.fromFlagValue(asInt(kids[0])));
        }
        case NONE:
            return None.INSTANCE;
        case NORMAL_ANNOTATION: {
            if(!isName(kids[0]) || !isMemberValuePairList(kids[1]))
                return null;
            NormalAnnotation x = ast.newNormalAnnotation();
            x.setTypeName(asName(kids[0]));
            x.values().addAll(asMemberValuePairList(kids[1]));
            return wrap(x);
        }
        case NULL_LITERAL: {
            return wrap(ast.newNullLiteral());
        }
        case NUMBER_LITERAL: {
            if(!isString(kids[0]))
                return null;
            return wrap(ast.newNumberLiteral(asString(kids[0])));
        }
        case PACKAGE_DECLARATION: {
            if((!isJavadoc(kids[0]) && !isNone(kids[0]))
                    || !isAnnotations(kids[1])
                    || !isName(kids[2]))
                return null;
            
            PackageDeclaration pd = ast.newPackageDeclaration();
            if(isNone(kids[0]))
                pd.setJavadoc(null);
            else
                pd.setJavadoc(getJavadoc(kids[0]));
            pd.annotations().addAll(getAnnotations(kids[1]));
            pd.setName(asName(kids[2]));
            return wrap(pd);
        }
        case PARAMETERIZED_TYPE: {
            if(!isType(kids[0]) || !isTypeList(kids[1]))
                return null;
            ParameterizedType x = ast.newParameterizedType(asType(kids[0]));
            x.typeArguments().addAll(asTypeList(kids[1]));
            return wrap(x);
        }
        case PARENTHESIZED_EXPRESSION: {
            if(!isExpression(kids[0]))
                return null;
            ParenthesizedExpression x = ast.newParenthesizedExpression();
            x.setExpression(asExpression(kids[0]));
            return wrap(x);
        }
        case POSTFIX_EXPRESSION: {
            if(!isPostfixOperator(kids[0]) || !isExpression(kids[1]))
                return null;
            PostfixExpression x = ast.newPostfixExpression();
            x.setOperator(asPostfixOperator(kids[0]));
            x.setOperand(asExpression(kids[1]));
            return wrap(x);
        }
        case POSTFIX_EXPRESSION_OPERATOR: {
            if(!isString(kids[0]))
                return null;
            return wrap(PostfixExpression.Operator.toOperator(asString(kids[0])));
        }
        case PREFIX_EXPRESSION: {
            if(!isPrefixOperator(kids[0]) || !isExpression(kids[1]))
                return null;
            PrefixExpression x = ast.newPrefixExpression();
            x.setOperator(asPrefixOperator(kids[0]));
            x.setOperand(asExpression(kids[1]));
            return wrap(x);
        }
        case PREFIX_EXPRESSION_OPERATOR: {
            if(!isString(kids[0]))
                return null;
            return wrap(PrefixExpression.Operator.toOperator(asString(kids[0])));
        }
        case PRIMITIVE_TYPE: {
            if(!isString(kids[0]))
                return null;
            return wrap(ast.newPrimitiveType(asTypeCode(kids[0])));
        }
        case QUALIFIED_NAME: {
            if(!isName(kids[0]) || !isSimpleName(kids[1]))
                return null;
            return wrap(ast.newQualifiedName(asName(kids[0]), asSimpleName(kids[1])));
        }
        case QUALIFIED_TYPE: {
            if(!isType(kids[0]) || !isSimpleName(kids[1]))
                return null;
            return wrap(ast.newQualifiedType(asType(kids[0]), asSimpleName(kids[1])));
        }
        case RETURN_STATEMENT: {
            if(!(isExpression(kids[0]) || isNone(kids[0])))
                return null;
            ReturnStatement x = ast.newReturnStatement();
            if(isNone(kids[0]))
            	x.setExpression(null);
            else 
            	x.setExpression(asExpression(kids[0]));
            return wrap(x);
        }
        case SIMPLE_NAME: {
            if(!isString(kids[0]))
                return null;
            return wrap(ast.newSimpleName(asString(kids[0])));
        }
        case SIMPLE_TYPE: {
            if(!isName(kids[0]))
                return null;
            return wrap(ast.newSimpleType(asName(kids[0])));
        }
        case SINGLE_MEMBER_ANNOTATION: {
            if(!isName(kids[0]) || !isExpression(kids[1]))
                return null;
            SingleMemberAnnotation x = ast.newSingleMemberAnnotation();
            x.setTypeName(asName(kids[0]));
            x.setValue(asExpression(kids[1]));
            return wrap(x);
        }
        case SINGLE_VARIABLE_DECLARATION: {
            if(!isModifierList(kids[0])
                    || !isType(kids[1])
                    || !isName(kids[2])
                    || !isInt(kids[3])
                    || (!isExpression(kids[4]) && !isNone(kids[4])))
                return null;
            SingleVariableDeclaration x = ast.newSingleVariableDeclaration();
            x.modifiers().addAll(asModifierList(kids[0]));
            x.setType(asType(kids[1]));
            x.setName(asSimpleName(kids[2]));
            x.setExtraDimensions(asInt(kids[3]));
            if(isNone(kids[4])) 
                x.setInitializer(null);
            else
                x.setInitializer(asExpression(kids[4]));
            return wrap(x);
        }
        case STRING_LITERAL: {
            if(!isString(kids[0]))
                return null;
            StringLiteral x = ast.newStringLiteral();
            x.setLiteralValue(asString(kids[0]));
            return wrap(x);
        }
        case SUPER_CONSTRUCTOR_INVOCATION: {
            if((!isExpression(kids[0]) && !isNone(kids[0])) 
                    || !isTypeList(kids[1]) 
                    || !isExpressionList(kids[2]))
                return null;
            SuperConstructorInvocation x = ast.newSuperConstructorInvocation();
            if(isNone(kids[0]))
                x.setExpression(null);
            else
                x.setExpression(asExpression(kids[0]));
            x.typeArguments().addAll(asTypeList(kids[1]));
            x.arguments().addAll(asExpressionList(kids[2]));
            return wrap(x);
        }
        case SUPER_FIELD_ACCESS: {
            if((!isName(kids[0]) && !isNone(kids[0])) 
                    || !isSimpleName(kids[1]))
                return null;
            SuperFieldAccess x = ast.newSuperFieldAccess();
            if(isNone(kids[0]))
                x.setQualifier(null);
            else
                x.setQualifier(asName(kids[0]));
            x.setName(asSimpleName(kids[1]));
            return wrap(x);
        }
        case SUPER_METHOD_INVOCATION: {
            if((!isName(kids[0]) && !isNone(kids[0])) 
                    || !isTypeList(kids[1])
                    || !isSimpleName(kids[2])
                    || !isExpressionList(kids[3]))
                return null;
            SuperMethodInvocation x = ast.newSuperMethodInvocation();
            if(isNone(kids[0]))
                x.setQualifier(null);
            else
                x.setQualifier(asName(kids[0]));
            x.typeArguments().addAll(asTypeList(kids[1]));
            x.setName(asSimpleName(kids[2]));
            x.arguments().addAll(asExpressionList(kids[3]));
            return wrap(x);
        }
        case SWITCH_CASE: {
            if(!isExpression(kids[0]) && !isNone(kids[0]))
                return null;
            SwitchCase x = ast.newSwitchCase();
            if(isNone(kids[0]))
                x.setExpression(null);
            else
                x.setExpression(asExpression(kids[0]));
            return wrap(x);
        }
        case SWITCH_STATEMENT: {
            if(!isExpression(kids[0])
                    || !isStatementList(kids[1]))
                return null;
            SwitchStatement x = ast.newSwitchStatement();
            x.setExpression(asExpression(kids[0]));
            x.statements().addAll(asStatementList(kids[1]));
            return wrap(x);
        }
        case SYNCHRONIZED_STATEMENT: {
            if(!isExpression(kids[0])
                    || !isBlock(kids[1]))
                return null;
            SynchronizedStatement x = ast.newSynchronizedStatement();
            x.setExpression(asExpression(kids[0]));
            x.setBody(asBlock(kids[1]));
            return wrap(x);
        }
        case TAG_ELEMENT: {
            if((!isString(kids[0]) && !isNone(kids[0])) 
                    || !isASTNodeList(kids[1]))
                return null;
            TagElement x = ast.newTagElement();
            if(isNone(kids[0]))
                x.setTagName(null);
            else 
                x.setTagName(asString(kids[0]));
            x.fragments().addAll(asASTNodeList(kids[1]));
            return wrap(x);
        }
        case TEXT_ELEMENT: {
            if(!isString(kids[0]))
                return null;
            TextElement x = ast.newTextElement();
            x.setText(asString(kids[0]));
            return wrap(x);
        }
        case THIS_EXPRESSION: {
            if(!isName(kids[0]) && !isNone(kids[0]))
                return null;
            ThisExpression x = ast.newThisExpression();
            if(isNone(kids[0]))
                x.setQualifier(null);
            else 
                x.setQualifier(asName(kids[0]));
            return wrap(x);
        }
        case THROW_STATEMENT: {
            if(!isExpression(kids[0]))
                return null;
            ThrowStatement x = ast.newThrowStatement();
            x.setExpression(asExpression(kids[0]));
            return wrap(x);
        }
        case TRY_STATEMENT: {
            if(!isBlock(kids[0]) 
                    || !isCatchClauseList(kids[1]) 
                    || (!isBlock(kids[2]) && !isNone(kids[2])))
                return null;
            TryStatement x = ast.newTryStatement();
            x.setBody(asBlock(kids[0]));
            x.catchClauses().addAll(asCatchClauseList(kids[1]));
            if(isNone(kids[2]))
                x.setFinally(null);
            else 
                x.setFinally(asBlock(kids[2]));
            return wrap(x);
        }
        case TYPE_DECLARATION: {
            if((!isJavadoc(kids[0]) && !isNone(kids[0]))
                    || !isModifierList(kids[1])
                    || !isSimpleName(kids[2])
                    || !isTypeParameterList(kids[3])
                    || (!isType(kids[4]) && !isNone(kids[4]))
                    || !isTypeList(kids[5])
                    || !isBodyDeclarationList(kids[6])
                    || !isInt(kids[7]))
                return null;
            TypeDeclaration x = ast.newTypeDeclaration();
            if(isNone(kids[0]))
                x.setJavadoc(null);
            else
                x.setJavadoc(asJavadoc(kids[0]));
            x.modifiers().addAll(asModifierList(kids[1]));
            x.setName(asSimpleName(kids[2]));
            x.typeParameters().addAll(asTypeParameterList(kids[3]));
            if(isNone(kids[4])) 
                x.setSuperclassType(null);
            else 
                x.setSuperclassType(asType(kids[4]));
            x.superInterfaceTypes().addAll(asTypeList(kids[5]));
            x.bodyDeclarations().addAll(asBodyDeclarationList(kids[6]));
            x.setInterface(asInt(kids[7]) == 1);
            return wrap(x);
        }
        case TYPE_DECLARATION_STATEMENT: {
            if(!isTypeDecl(kids[0]))
                return null;
            return wrap(ast.newTypeDeclarationStatement(asTypeDecl(kids[0])));
        }
        case TYPE_LITERAL: {
            if(!isType(kids[0]))
                return null;
            TypeLiteral x = ast.newTypeLiteral();
            x.setType(asType(kids[0]));
            return wrap(x);
        }
        case TYPE_PARAMETER: {
            if(!isSimpleName(kids[0]) || !isTypeList(kids[1]))
                return null;
            TypeParameter x = ast.newTypeParameter();
            x.setName(asSimpleName(kids[0]));
            x.typeBounds().addAll(asTypeList(kids[1]));
            return wrap(x);
        }
        case VARIABLE_DECLARATION_EXPRESSION: {
            if(!isModifierList(kids[0]) 
                    || !isType(kids[1]) 
                    || !isNonEmptyVariableDeclarationFragmentList(kids[2]))
                return null;
            List y = asFragmentList(kids[2]);
            VariableDeclarationExpression x = ast.newVariableDeclarationExpression((VariableDeclarationFragment)y.remove(0));
            x.modifiers().addAll(asModifierList(kids[0]));
            x.setType(asType(kids[1]));
            x.fragments().addAll(y);
            return wrap(x);
        }
        case VARIABLE_DECLARATION_FRAGMENT: {
            if(!isSimpleName(kids[0]) 
                    || !isInt(kids[1]) 
                    || (!isExpression(kids[2]) && !isNone(kids[2])))
                return null;
            VariableDeclarationFragment x = ast.newVariableDeclarationFragment();
            x.setName(asSimpleName(kids[0]));
            x.setExtraDimensions(asInt(kids[1]));
            if(isNone(kids[2]))
                x.setInitializer(null);
            else
                x.setInitializer(asExpression(kids[2]));
            return wrap(x);
        }
        case VARIABLE_DECLARATION_STATEMENT: {
            if(!isModifierList(kids[0]) 
                    || !isType(kids[1]) 
                    || !isNonEmptyVariableDeclarationFragmentList(kids[2]))
                return null;
            List y = asFragmentList(kids[2]);
            VariableDeclarationStatement x = ast.newVariableDeclarationStatement((VariableDeclarationFragment)y.remove(0));
            x.modifiers().addAll(asModifierList(kids[0]));
            x.setType(asType(kids[1]));
            x.fragments().addAll(y);
            return wrap(x);
        }
        case WHILE_STATEMENT: {
            if(!isExpression(kids[0]) || !isStatement(kids[1]))
                return null;
            WhileStatement x = ast.newWhileStatement();
            x.setExpression(asExpression(kids[0]));
            x.setBody(asStatement(kids[1]));
            return wrap(x);
        }
        case WILDCARD_TYPE: {
            if(!isType(kids[0]) && !isNone(kids[0]))
                return null;
            WildcardType x = ast.newWildcardType();
            if(isNone(kids[0]))
                x.setBound(null);
            else
                x.setBound(asType(kids[0]));
            return wrap(x);
        }
        default:
            return null;
        }
    }

    private Javadoc asJavadoc(IStrategoTerm term) {
        Javadoc x = ((WrappedJavadoc)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (Javadoc)ASTNode.copySubtree(ast, x);
    }

    @SuppressWarnings("unchecked")
    private Collection asExtendedModifierList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asExtendedModifier(k));
        }
        return r;    
    }

    private IExtendedModifier asExtendedModifier(IStrategoTerm term) {
    	final IExtendedModifier x = ((IWrappedExtendedModifier)term).getModifierWrappee();
    	final ASTNode an = ((ASTNode)x);
    	if(an.getParent() == null && an.getAST() == ast)
    		return x;
    	else 
    		return (IExtendedModifier)ASTNode.copySubtree(ast, an);
    }

    private boolean isExtendedModifierList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() > 0) 
                return isExtendedModifier(list.head());
            return true;
        }
        return false;
    }

    private boolean isExtendedModifier(IStrategoTerm term) {
        return term instanceof IWrappedExtendedModifier;
    }

    @SuppressWarnings("unchecked")
    private Collection asTypeParameterList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asTypeParameter(k));
        }
        return r;    
    }

    private TypeParameter asTypeParameter(IStrategoTerm term) {
        TypeParameter x = ((WrappedTypeParameter)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (TypeParameter)ASTNode.copySubtree(ast, x);
    }

    @SuppressWarnings("unchecked")
    private Collection asTagElementList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asTagElement(k));
        }
        return r;    
    }


    private TagElement asTagElement(IStrategoTerm term) {
    	TagElement x = ((WrappedTagElement)term).getWrappee(); 
        return x.getParent() == null && x.getAST() == ast ? x : (TagElement)ASTNode.copySubtree(ast, x);
    }

    @SuppressWarnings("unchecked")
    private Collection asAbstractTypeDeclarationList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asAbstractTypeDeclaration(k));
        }
        return r;    
        
    }

    private Operator asOperator(IStrategoTerm term) {
        String s = ((IStrategoString)term).stringValue();
        return InfixExpression.Operator.toOperator(s);
    }


    @SuppressWarnings("unchecked")
    private Collection asNameList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asName(k));
        }
        return r;    
    }

    @SuppressWarnings("unchecked")
    private Collection asSingleVariableDeclarationList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asSingleVariableDeclaration(k));
        }
        return r;    
    }

    private AbstractTypeDeclaration asAbstractTypeDeclaration(IStrategoTerm term) {
        AbstractTypeDeclaration x = ((WrappedAbstractTypeDeclaration)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (AbstractTypeDeclaration)ASTNode.copySubtree(ast, x);
    }

    @SuppressWarnings("unchecked")
    private Collection asMethodRefParameterList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asMethodRefParameter(k));
        }
        return r;    
    }

    @SuppressWarnings("unchecked")
    private Collection asImportDeclarationList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asImportDeclaration(k));
        }
        return r;    
    }

    private ImportDeclaration asImportDeclaration(IStrategoTerm term) {
        ImportDeclaration x = ((WrappedImportDeclaration)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (ImportDeclaration)ASTNode.copySubtree(ast, x);
    }

    private PackageDeclaration asPackageDeclaration(IStrategoTerm term) {
        PackageDeclaration x = ((WrappedPackageDeclaration)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (PackageDeclaration)ASTNode.copySubtree(ast, x);
    }


    @SuppressWarnings("unchecked")
    private Collection asEnumConstantDeclarationList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asEnumConstantDeclaration(k));
        }
        return r;    
    }

    private EnumConstantDeclaration asEnumConstantDeclaration(IStrategoTerm term) {
        EnumConstantDeclaration x = ((WrappedEnumConstantDeclaration)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (EnumConstantDeclaration)ASTNode.copySubtree(ast, x);
    }


    @SuppressWarnings("unchecked")
    private Collection asASTNodeList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asASTNode(k));
        }
        return r;    
    }

    private ASTNode asASTNode(IStrategoTerm term) {
        ASTNode x = ((WrappedASTNode)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : ASTNode.copySubtree(ast, x);
    }

    private MethodRefParameter asMethodRefParameter(IStrategoTerm term) {
        MethodRefParameter x = ((WrappedMethodRefParameter)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (MethodRefParameter)ASTNode.copySubtree(ast, x);
    }

    @SuppressWarnings("unchecked")
    private Collection asMemberValuePairList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asMemberValuePair(k));
        }
        return r;    
    }

    private MemberValuePair asMemberValuePair(IStrategoTerm term) {
        MemberValuePair x = ((WrappedMemberValuePair)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (MemberValuePair)ASTNode.copySubtree(ast, x);
    }

    private Code asTypeCode(IStrategoTerm term) {
        return PrimitiveType.toCode(((IStrategoString)term).stringValue());
    }

    private ModifierKeyword asModifierKeyword(IStrategoTerm term) {
        return ((WrappedModifierKeyword)term).getWrappee();
    }

    private Name asName(IStrategoTerm term) {
        Name x = ((WrappedName)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (Name)ASTNode.copySubtree(ast, x);
    }

    @SuppressWarnings("unchecked")
    private Collection asCatchClauseList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asCatchClause(k));
        }
        return r;    
    }

    private AbstractTypeDeclaration asTypeDecl(IStrategoTerm term) {
        AbstractTypeDeclaration x = ((WrappedAbstractTypeDeclaration)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (AbstractTypeDeclaration)ASTNode.copySubtree(ast, x);
    }

    @SuppressWarnings("unchecked")
    private List asFragmentList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asVariableDeclarationFragment(k));
        }
        return r;    
    }

    private ArrayInitializer asArrayInitializer(IStrategoTerm term) {
        ArrayInitializer x = ((WrappedArrayInitializer)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (ArrayInitializer)ASTNode.copySubtree(ast, x);
    }

    private ArrayType asArrayType(IStrategoTerm term) {
        ArrayType x = ((WrappedArrayType)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (ArrayType)ASTNode.copySubtree(ast, x);
    }

    private Type asType(IStrategoTerm term) {
        Type x = ((WrappedType)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (Type)ASTNode.copySubtree(ast, x);
    }

    private SingleVariableDeclaration asSingleVariableDeclaration(IStrategoTerm term) {
        SingleVariableDeclaration x = ((WrappedSingleVariableDeclaration)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (SingleVariableDeclaration)ASTNode.copySubtree(ast, x);
    }

    private Block asBlock(IStrategoTerm term) {
        Block x = ((WrappedBlock)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (Block)ASTNode.copySubtree(ast, x);
    }

    private PrefixExpression.Operator asPrefixOperator(IStrategoTerm term) {
        return ((WrappedPrefixExpressionOperator)term).getWrappee();
    }

    @SuppressWarnings("unchecked")
    private Collection asTypeList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asType(k));
        }
        return r;    
    }

    @SuppressWarnings("unchecked")
    private Collection asBodyDeclarationList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asBodyDeclaration(k));
        }
        return r;
    }

    private Expression asExpression(IStrategoTerm term) {
        Expression x = ((WrappedExpression) term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (Expression)ASTNode.copySubtree(ast, x);
    }

    private int asInt(IStrategoTerm term) {
        return ((IStrategoInt)term).intValue();
    }

    @SuppressWarnings("unchecked")
    private Collection asExpressionList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asExpression(k));
        }
        return r;
    }

    private PostfixExpression.Operator asPostfixOperator(IStrategoTerm term) {
        return ((WrappedPostfixExpressionOperator)term).getWrappee();
    }

    private Assignment.Operator asAssignmentOperator(IStrategoTerm term) {
        return ((WrappedAssignmentOperator)term).getWrappee();
    }

    private BodyDeclaration asBodyDeclaration(IStrategoTerm k) {
        BodyDeclaration x = ((WrappedBodyDeclaration)k).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (BodyDeclaration)ASTNode.copySubtree(ast, x);
    }

    private SimpleName asSimpleName(IStrategoTerm term) {
        SimpleName x = ((WrappedSimpleName)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (SimpleName)ASTNode.copySubtree(ast, x);
    }

    private Modifier asModifier(IStrategoTerm term) {
        Modifier x = ((WrappedModifier)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (Modifier)ASTNode.copySubtree(ast, x);
    }

    private VariableDeclarationFragment asVariableDeclarationFragment(IStrategoTerm term) {
    	VariableDeclarationFragment x = ((WrappedVariableDeclarationFragment)term).getWrappee();
    	return x.getParent() == null && x.getAST() == ast ? x : (VariableDeclarationFragment) ASTNode.copySubtree(ast, x);
    }

    private AnonymousClassDeclaration asAnonymousClassDeclaration(IStrategoTerm term) {
        AnonymousClassDeclaration x = ((WrappedAnonymousClassDeclaration)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (AnonymousClassDeclaration) ASTNode.copySubtree(ast, x);
    }

    private CatchClause asCatchClause(IStrategoTerm term) {
    	CatchClause x = ((WrappedCatchClause)term).getWrappee();
    	return x.getParent() == null && x.getAST() == ast ? x : (CatchClause) ASTNode.copySubtree(ast, x);
    }

    private String asString(IStrategoTerm term) {
        return ((IStrategoString)term).stringValue();
    }

    @SuppressWarnings("unchecked")
    private Collection asStatementList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asStatement(k));
        }
        return r;
    }

    private Statement asStatement(IStrategoTerm term) {
        Statement x = ((WrappedStatement)term).getWrappee();
        return x.getParent() == null && x.getAST() == ast ? x : (Statement)ASTNode.copySubtree(ast, x); 
    }

    private boolean isAbstractTypeDeclarationList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoTerm[] kids = ((IStrategoList)term).getAllSubterms();
            for(IStrategoTerm k : kids) {
                if(!isAbstractTypeDeclaration(k))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean isAbstractTypeDeclaration(IStrategoTerm term) {
        return term instanceof WrappedAbstractTypeDeclaration;
    }

    private boolean isNone(IStrategoTerm term) {
        return term instanceof None;
    }

    private boolean isName(IStrategoTerm term) {
        return term instanceof WrappedName;
    }

    private boolean isAnnotations(IStrategoTerm term) {
        return term instanceof WrappedASTNodeList;
    }

    private boolean isJavadoc(IStrategoTerm term) {
        return term instanceof WrappedJavadoc;
    }

    private boolean isMethodRefParameterList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoTerm[] kids = ((IStrategoList)term).getAllSubterms();
            for(IStrategoTerm k : kids) { 
                if(!isMethodRefParameter(k))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean isMethodRefParameter(IStrategoTerm term) {
        return term instanceof WrappedMethodRefParameter;
    }

    private boolean isNameList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() > 0) 
                return isName(list.head());
            return true;
        }
        return false;
    }

    private boolean isSingleVariableDeclarationList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() > 0) 
                return isSingleVariableDeclaration(list.head());
            return true;
        }
        return false;
    }

    private boolean isTypeParameterList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() > 0) 
                return isTypeParameter(list.head());
            return true;
        }
        return false;
    }

    private boolean isTypeParameter(IStrategoTerm term) {
        return term instanceof WrappedTypeParameter;
    }

    private boolean isTagElementList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoTerm[] kids = term.getAllSubterms();
            for(int i = 0; i < kids.length; i++)
                if(!isTagElement(kids[i]))
                    return false;
        }
        return true;
    }

    private boolean isTagElement(IStrategoTerm term) {
        return term instanceof WrappedTagElement;
    }

    private boolean isOperator(IStrategoTerm term) {
        return term instanceof IStrategoString 
        && InfixExpression.Operator.toOperator(((IStrategoString)term).stringValue()) != null;
    }

    private boolean isEnumConstantDeclarationList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() > 0) 
                return isEnumConstantDeclaration(list.head());
            return true;
        }
        return false;
    }

    private boolean isEnumConstantDeclaration(IStrategoTerm term) {
        return term instanceof WrappedEnumConstantDeclaration;
    }

    private boolean isImportDeclarationList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() > 0) 
                return isImportDeclaration(list.head());
            return true;
        }
        return false;
    }

    private boolean isImportDeclaration(IStrategoTerm term) {
        return term instanceof WrappedImportDeclaration;
    }

    private boolean isPackageDeclaration(IStrategoTerm term) {
        return term instanceof WrappedPackageDeclaration;
    }

    private boolean isMemberValuePairList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() > 0) 
                return isMemberValuePair(list.head());
            return true;
        }
        return false;
    }

    private boolean isMemberValuePair(IStrategoTerm term) {
        return term instanceof WrappedMemberValuePair;
    }
   
    private boolean isModifierKeyword(IStrategoTerm term) {
        return term instanceof WrappedModifierKeyword;
    }

    private boolean isASTNodeList(IStrategoTerm term) {
        return term instanceof WrappedASTNodeList;
    }

    private boolean isString(IStrategoTerm term) {
        return term instanceof IStrategoString;
    }

    private boolean isCatchClauseList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() > 0) 
                return isCatchClause(list.head());
            return true;
        }
        return false;
    }

    private boolean isCatchClause(IStrategoTerm term) {
        return term instanceof WrappedCatchClause;
    }

    private boolean isBodyDeclarationList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoTerm[] kids = ((IStrategoList)term).getAllSubterms();
            for(int i = 0; i < kids.length; i++)
                if(!isBodyDeclaration(kids[i]))
                    return false;
            return true;
        }
        return false;
    }

    private boolean isTypeDecl(IStrategoTerm term) {
        return term instanceof WrappedAbstractTypeDeclaration;
    }

    private boolean isNonEmptyVariableDeclarationFragmentList(IStrategoTerm term) {
        // Must contain at least one element
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            if(list.size() < 0)
                return false;
            return isVariableDeclarationFragment(list.head());
            
        }
        return false;
    }

    private boolean isVariableDeclarationFragment(IStrategoTerm term) {
        return term instanceof WrappedVariableDeclarationFragment;
    }

    private boolean isAnonymousClassDeclaration(IStrategoTerm term) {
        return term instanceof WrappedAnonymousClassDeclaration;
    }

    private boolean isBlock(IStrategoTerm term) {
        return term instanceof WrappedBlock;
    }

    private boolean isSingleVariableDeclaration(IStrategoTerm term) {
        return term instanceof WrappedSingleVariableDeclaration;
    }

    private boolean isPrefixOperator(IStrategoTerm term) {
        return term instanceof WrappedPrefixExpressionOperator;
    }

    private boolean isPostfixOperator(IStrategoTerm term) {
        return term instanceof WrappedPostfixExpressionOperator;
    }

    private boolean isAssignmentOperator(IStrategoTerm term) {
        return term instanceof WrappedAssignmentOperator;
    }

    private boolean isTypeList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
        	for(IStrategoTerm t : ((IStrategoList)term).getAllSubterms()) {
        		if(!isType(t))
        			return false;
        	}
        	return true;
        }
        return false;
    }

    private boolean isStatementList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoList list = (IStrategoList)term;
            for(IStrategoTerm t : list.getAllSubterms()) { 
                if(!isStatement(t))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean isStatement(IStrategoTerm term) {
        return term instanceof WrappedStatement;
    }

    private boolean isInt(IStrategoTerm term) {
        return term instanceof IStrategoInt;
    }


    private boolean isExpressionList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoTerm[] kids = ((IStrategoList)term).getAllSubterms();
            for(IStrategoTerm k : kids) {
                if(!isExpression(k))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean isArrayInitializer(IStrategoTerm term) {
        return term instanceof WrappedArrayInitializer;
    }


    private boolean isArrayType(IStrategoTerm term) {
        return term instanceof WrappedArrayType;
    }

    private boolean isSimpleName(IStrategoTerm term) {
        return term instanceof WrappedSimpleName;
    }

    private boolean isType(IStrategoTerm term) {
        return term instanceof WrappedType;
    }

    @SuppressWarnings("unchecked")
    private Collection asModifierList(IStrategoTerm term) {
        IStrategoTerm[] kids = term.getAllSubterms();
        List r = new ArrayList(kids.length);
        for(IStrategoTerm k : kids) {
            r.add(asModifier(k));
        }
        return r;
    }

    private boolean isModifierList(IStrategoTerm term) {
        if(term instanceof IStrategoList) {
            IStrategoTerm[] kids = ((IStrategoList)term).getAllSubterms();
            for(IStrategoTerm k : kids) {
                if(!isModifier(k))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean isModifier(IStrategoTerm term) {
        return term instanceof WrappedModifier;
    }

    private boolean isBodyDeclaration(IStrategoTerm term) {
        return term instanceof WrappedBodyDeclaration;
    }

    private boolean isExpression(IStrategoTerm term) {
        return term instanceof WrappedExpression;
    }

    private int ctorNameToIndex(IStrategoConstructor ctr) {
        Integer x = ctorNameToIndexMap.get(ctr.getName());
        return x == null ? -1 : x.intValue();
    }

    public IStrategoConstructor makeConstructor(String string, int arity) {
        return new ASTCtor(string, arity);
    }

    public IStrategoInt makeInt(int i) {
        return new WrappedInt(i);
    }

    @SuppressWarnings("unchecked")
    public IStrategoList makeList(IStrategoTerm... terms) {
        
        boolean mustUseGeneric = false;
        for(IStrategoTerm t : terms)
            if(!(t instanceof WrappedASTNode))
                mustUseGeneric = true;
        
        if(mustUseGeneric) {
            return new WrappedGenericList(terms);
        }
        
        List<ASTNode> r = new ArrayList();
        for(IStrategoTerm t : terms)
            r.add(((WrappedASTNode)t).getWrappee());
        return new WrappedASTNodeList(r);
    }

    public IStrategoList makeList(Collection<IStrategoTerm> terms) {
    	return makeList(terms.toArray(new IStrategoTerm[0]));
    }
    
    @Deprecated
    public final IStrategoList makeList(IStrategoTerm head, IStrategoList tail) {
        return makeListCons(head, tail);
    }
    
    public IStrategoList makeListCons(IStrategoTerm head, IStrategoList tail) {
        // TODO: handle list prepending in ECJFactory
        return tail.prepend(head);
    }

    public IStrategoReal makeReal(double d) {
        return new WrappedReal(d);
    }

    public IStrategoString makeString(String s) {
        return new WrappedString(s);
    }

    public IStrategoTuple makeTuple(IStrategoTerm... terms) {
        return new WrappedTuple(terms);
    }

    public static IStrategoAppl wrap(Javadoc javadoc) {
        if(javadoc == null)
            return None.INSTANCE;
        else
            return new WrappedJavadoc(javadoc);
    }

    @SuppressWarnings("unchecked")
    public static IStrategoTerm wrap(List list) {
        if(list == null)
            return None.INSTANCE;
        else
            return new WrappedASTNodeList(list);
    }

    static IStrategoTerm wrapName(Name name) {
        
        if(name == null)
            return None.INSTANCE;
        
        if(name instanceof QualifiedName)
            return wrap((QualifiedName) name);
        if(name instanceof SimpleName)
            return wrap((SimpleName) name);
        
        throw new NotImplementedException("Unknown Name type: " + name.getClass());
    }

    static IStrategoAppl wrap(SimpleName name) {
        if(name == null)
            return None.INSTANCE;
        else
            return new WrappedSimpleName(name);
    }

    private static IStrategoAppl wrap(QualifiedName name) {
        if(name == null)
            return None.INSTANCE;
        else
            return new WrappedQualifiedName(name);
    }

    @SuppressWarnings("unchecked")
	public static IStrategoTerm genericWrap(ASTNode node) {
        
        if(node instanceof ImportDeclaration)
            return wrap((ImportDeclaration) node);
        if(node instanceof Name)
            return wrapName((Name) node);
        if(node instanceof List)
            return wrap((List) node);
        if(node instanceof Javadoc)
            return wrap((Javadoc) node);
        if(node instanceof CompilationUnit)
            return wrap((CompilationUnit) node);
        if(node instanceof PackageDeclaration)
            return wrap((PackageDeclaration) node);
        if(node instanceof TypeDeclaration)
            return wrap((TypeDeclaration) node);
        if(node instanceof MethodDeclaration)
            return wrap((MethodDeclaration) node);
        if(node instanceof SingleVariableDeclaration)
            return wrap((SingleVariableDeclaration) node);
        if(node instanceof Expression)
            return wrapExpression((Expression) node);
        if(node instanceof VariableDeclarationFragment)
            return wrap((VariableDeclarationFragment) node);
        if(node instanceof AnonymousClassDeclaration)
            return wrap((AnonymousClassDeclaration) node);
        if(node instanceof BodyDeclaration)
            return wrapBody((BodyDeclaration) node);
        if(node instanceof CatchClause)
            return wrap((CatchClause) node);
        if(node instanceof Comment) 
            return wrapComment((Comment) node);
        if(node instanceof MemberRef)
            return wrap((MemberRef) node);
        if(node instanceof MemberValuePair)
            return wrap((MemberValuePair) node);
        if(node instanceof MethodRef)
            return wrap((MethodRef) node);
        if(node instanceof MethodRefParameter)
            return wrap((MethodRefParameter) node);
        if(node instanceof Modifier)
            return wrap((Modifier) node);
        if(node instanceof PackageDeclaration)
            return wrap((PackageDeclaration) node);
        if(node instanceof Statement)
            return wrapStatement((Statement) node);
        if(node instanceof TagElement)
            return wrap((TagElement) node);
        if(node instanceof TextElement)
            return wrap((TextElement) node);
        if(node instanceof Type)
            return wrapType((Type) node);
        if(node instanceof TypeParameter)
            return wrap((TypeParameter) node);
        if(node instanceof VariableDeclaration)
            return wrapVarDecl((VariableDeclaration) node);
        
        if(node == null)
            return None.INSTANCE;
        
        throw new NotImplementedException("Unknown ASTNode type" + node.getClass());
    }

    private static IStrategoTerm wrapVarDecl(VariableDeclaration decl) {
        if(decl instanceof SingleVariableDeclaration)
            return wrap((SingleVariableDeclaration) decl);
        if(decl instanceof VariableDeclarationFragment)
            return wrap((VariableDeclarationFragment) decl);
        
        throw new NotImplementedException();
    }

    private static IStrategoTerm wrapComment(Comment comment) {
        if(comment instanceof BlockComment)
            return wrap((BlockComment) comment);
        if(comment instanceof Javadoc)
            return wrap((Javadoc) comment);
        if(comment instanceof LineComment)
            return wrap((LineComment) comment);
        
        throw new NotImplementedException();
    }

    private static IStrategoAppl wrap(LineComment comment) {
        if(comment == null)
            return None.INSTANCE;
        else
            return new WrappedLineComment(comment);
    }

    private static IStrategoAppl wrap(BlockComment comment) {
        if(comment == null)
            return None.INSTANCE;
        else
            return new WrappedBlockComment(comment);
    }

    private static IStrategoTerm wrapBody(BodyDeclaration decl) {
       
        if(decl instanceof AbstractTypeDeclaration)
            return wrapTypeDecl((AbstractTypeDeclaration) decl);
        if(decl instanceof AnnotationTypeMemberDeclaration)
            return wrap((AnnotationTypeMemberDeclaration) decl);
        if(decl instanceof EnumConstantDeclaration)
            return wrap((EnumConstantDeclaration) decl);
        if(decl instanceof FieldDeclaration)
            return wrap((FieldDeclaration) decl);
        if(decl instanceof Initializer)
            return wrap((Initializer) decl);
        if(decl instanceof MethodDeclaration)
            return wrap((MethodDeclaration) decl);
        
        throw new NotImplementedException();
    }

    private static IStrategoAppl wrap(AnnotationTypeMemberDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedAnnotationTypeMemberDeclaration(declaration);
    }

    private static IStrategoAppl wrap(EnumConstantDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedEnumConstantDeclaration(declaration);
    }

    private static IStrategoAppl wrap(FieldDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedFieldDeclaration(declaration);
    }

    private static IStrategoAppl wrap(Initializer initializer) {
        if(initializer == null)
                return None.INSTANCE;
        else
            return new WrappedInitializer(initializer);
    }

    private static IStrategoAppl wrap(TypeParameter parameter) {
        if(parameter == null)
            return None.INSTANCE;
        else
            return new WrappedTypeParameter(parameter);
    }

    private static IStrategoAppl wrap(TextElement element) {
        if(element == null)
            return None.INSTANCE;
        else
            return new WrappedTextElement(element);
    }

    private static IStrategoAppl wrap(TagElement element) {
        if(element == null)
            return None.INSTANCE;
        else
            return new WrappedTagElement(element);
    }

    static IStrategoTerm wrapStatement(Statement stat) {
        
        if(stat == null)
            return None.INSTANCE;
        
        if(stat instanceof ExpressionStatement)
            return wrap((ExpressionStatement) stat);
        if(stat instanceof VariableDeclarationStatement)
            return wrap((VariableDeclarationStatement) stat);
        if(stat instanceof AssertStatement)
            return wrap((AssertStatement) stat);
        if(stat instanceof Block)
            return wrap((Block) stat);
        if(stat instanceof BreakStatement)
            return wrap((BreakStatement) stat);
        if(stat instanceof ConstructorInvocation)
            return wrap((ConstructorInvocation) stat);
        if(stat instanceof ContinueStatement)
            return wrap((ContinueStatement) stat);
        if(stat instanceof DoStatement)
            return wrap((DoStatement) stat);
        if(stat instanceof EmptyStatement)
            return wrap((EmptyStatement) stat);
        if(stat instanceof EnhancedForStatement)
            return wrap((EnhancedForStatement) stat);
        if(stat instanceof ForStatement)
            return wrap((ForStatement) stat);
        if(stat instanceof IfStatement)
            return wrap((IfStatement) stat);
        if(stat instanceof LabeledStatement)
            return wrap((LabeledStatement) stat);
        if(stat instanceof ReturnStatement)
            return wrap((ReturnStatement) stat);
        if(stat instanceof SuperConstructorInvocation)
            return wrap((SuperConstructorInvocation) stat);
        if(stat instanceof SwitchCase)
            return wrap((SwitchCase) stat);
        if(stat instanceof SwitchStatement)
            return wrap((SwitchStatement) stat);
        if(stat instanceof SynchronizedStatement)
            return wrap((SynchronizedStatement) stat);
        if(stat instanceof ThrowStatement)
            return wrap((ThrowStatement) stat);
        if(stat instanceof TryStatement)
            return wrap((TryStatement) stat);
        if(stat instanceof TypeDeclarationStatement)
            return wrap((TypeDeclarationStatement) stat);
        if(stat instanceof WhileStatement)
            return wrap((WhileStatement) stat);

        throw new NotImplementedException();
    }

    private static IStrategoAppl wrap(IfStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedIfStatement(statement);
    }

    private static IStrategoAppl wrap(SuperConstructorInvocation invocation) {
        if(invocation == null)
            return None.INSTANCE; 
        else
            return new WrappedSuperConstructorInvocation(invocation);
    }

    private static IStrategoAppl wrap(SwitchCase switchcase) {
        if(switchcase == null)
            return None.INSTANCE; 
        else
            return new WrappedSwitchCase(switchcase);
    }

    private static IStrategoAppl wrap(SwitchStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedSwitchStatement(statement);
    }

    private static IStrategoAppl wrap(SynchronizedStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedSynchronizedStatement(statement);
    }

    private static IStrategoAppl wrap(ThrowStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedThrowStatement(statement);
    }

    private static IStrategoAppl wrap(TryStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedTryStatement(statement);
    }

    private static IStrategoAppl wrap(TypeDeclarationStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedTypeDeclarationStatement(statement);
    }

    private static IStrategoAppl wrap(WhileStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedWhileStatement(statement);
    }

    private static IStrategoAppl wrap(ReturnStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedReturnStatement(statement);
    }

    private static IStrategoAppl wrap(LabeledStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedLabeledStatement(statement);
    }

    private static IStrategoAppl wrap(ForStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedForStatement(statement);
    }

    private static IStrategoAppl wrap(EnhancedForStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedEnhancedForStatement(statement);
    }

    private static IStrategoAppl wrap(EmptyStatement statement) {   
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedEmptyStatement(statement);
    }

    private static IStrategoAppl wrap(DoStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedDoStatement(statement);
    }

    private static IStrategoAppl wrap(ContinueStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedContinueStatement(statement);
    }

    private static IStrategoAppl wrap(ConstructorInvocation invocation) {
        if(invocation == null)
            return None.INSTANCE;
        else
            return new WrappedConstructorInvocation(invocation);

    }

    private static IStrategoAppl wrap(BreakStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedBreakStatement(statement);
    }

    static IStrategoAppl wrap(Block block) {
        if(block == null)
            return None.INSTANCE;
        else
            return new WrappedBlock(block);
    }

    private static IStrategoAppl wrap(AssertStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedAssertStatement(statement);
    }

    private static IStrategoAppl wrap(Modifier modifier) {
        if(modifier == null)
            return None.INSTANCE;
        else
            return new WrappedModifier(modifier);
    }

    private static IStrategoAppl wrap(MethodRefParameter parameter) {
        if(parameter == null)
            return None.INSTANCE;
        else
            return new WrappedMethodRefParameter(parameter);
    }

    private static IStrategoAppl wrap(MethodRef ref) {
        if(ref == null)
            return None.INSTANCE;
        else
            return new WrappedMethodRef(ref);
    }

    private static IStrategoAppl wrap(MemberValuePair pair) {
        if(pair == null)
            return None.INSTANCE;
        else
            return new WrappedMemberValuePair(pair);
    }

    private static IStrategoAppl wrap(MemberRef ref) {
        if(ref == null)
            return None.INSTANCE;
        else
            return new WrappedMemberRef(ref);
    }

    private static IStrategoAppl wrap(CatchClause clause) {
        if(clause == null)
            return None.INSTANCE;
        else
            return new WrappedCatchClause(clause);
    }

    static IStrategoAppl wrap(AnonymousClassDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedAnonymousClassDeclaration(declaration);
    }

    private static IStrategoAppl wrap(VariableDeclarationFragment fragment) {
        if(fragment == null)
            return None.INSTANCE;
        else
            return new WrappedVariableDeclarationFragment(fragment);
    }

    private static IStrategoAppl wrap(VariableDeclarationStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedVariableDeclarationStatement(statement);
    }

    static IStrategoAppl wrap(ExpressionStatement statement) {
        if(statement == null)
            return None.INSTANCE;
        else
            return new WrappedExpressionStatement(statement);
    }

    static IStrategoAppl wrap(SingleVariableDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedSingleVariableDeclaration(declaration);
    }

    private static IStrategoAppl wrap(MethodDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedMethodDeclaration(declaration);
    }

    public static IStrategoAppl wrap(TypeDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedTypeDeclaration(declaration);
    }

    public static IStrategoAppl wrap(CompilationUnit unit) {
        if(unit == null)
            return None.INSTANCE;
        else
            return new WrappedCompilationUnit(unit);
    }

    public static IStrategoAppl wrap(PackageDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedPackageDeclaration(declaration);
    }

    static IStrategoAppl wrap(ImportDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedImportDeclaration(declaration);
    }

    public IStrategoTerm parseFromTree(ASTNode n) {
        return genericWrap(n);
    }

    public static IStrategoTerm wrap(int val) {
        return new WrappedInt(val);
    }

    public static IStrategoAppl wrap(ArrayType type) {
        if(type == null)
            return None.INSTANCE;
        else
            return new WrappedArrayType(type);
    }

    public static IStrategoTerm wrap(String identifier) {
        if(identifier == null)
            return None.INSTANCE;
        else
            return new WrappedString(identifier);
    }

    static IStrategoTerm wrapExpression(Expression expr) {

        if(expr == null)
            return None.INSTANCE;

        if(expr instanceof Annotation)
            return wrapAnnotation((Annotation) expr);
        if(expr instanceof ArrayAccess)
            return wrap((ArrayAccess) expr);
        if(expr instanceof ArrayCreation)
            return wrap((ArrayCreation) expr);
        if(expr instanceof ArrayInitializer)
            return wrap((ArrayInitializer) expr);
        if(expr instanceof Assignment)
            return wrap((Assignment) expr);
        if(expr instanceof BooleanLiteral)
            return wrap((BooleanLiteral) expr);
        if(expr instanceof CastExpression)
            return wrap((CastExpression) expr);
        if(expr instanceof CharacterLiteral)
            return wrap((CharacterLiteral) expr);
        if(expr instanceof ClassInstanceCreation)
            return wrap((ClassInstanceCreation) expr);
        if(expr instanceof ConditionalExpression)
            return wrap((ConditionalExpression) expr);
        if(expr instanceof FieldAccess)
            return wrap((FieldAccess) expr);
        if(expr instanceof InfixExpression)
            return wrap((InfixExpression) expr);
        if(expr instanceof InstanceofExpression)
            return wrap((InstanceofExpression) expr);
        if(expr instanceof MethodInvocation)
            return wrap((MethodInvocation) expr);
        if(expr instanceof Name)
            return wrapName((Name) expr);
        if(expr instanceof NullLiteral)
            return wrap((NullLiteral) expr);
        if(expr instanceof NumberLiteral)
            return wrap((NumberLiteral) expr);
        if(expr instanceof ParenthesizedExpression)
            return wrap((ParenthesizedExpression) expr);
        if(expr instanceof PostfixExpression)
            return wrap((PostfixExpression) expr);
        if(expr instanceof PrefixExpression)
            return wrap((PrefixExpression) expr);
        if(expr instanceof StringLiteral)
            return wrap((StringLiteral) expr);
        if(expr instanceof SuperFieldAccess)
            return wrap((SuperFieldAccess) expr);
        if(expr instanceof SuperMethodInvocation)
            return wrap((SuperMethodInvocation) expr);
        if(expr instanceof ThisExpression)
            return wrap((ThisExpression) expr);
        if(expr instanceof TypeLiteral)
            return wrap((TypeLiteral) expr);
        if(expr instanceof VariableDeclarationExpression)
            return wrap((VariableDeclarationExpression) expr);
        
        throw new NotImplementedException("Unknown Expression Type:" + expr.getClass());
    }

    private static IStrategoTerm wrapAnnotation(Annotation anno) {
        
        if(anno instanceof MarkerAnnotation)
            return wrap((MarkerAnnotation) anno);
        if(anno instanceof NormalAnnotation)
            return wrap((NormalAnnotation) anno);
        if(anno instanceof SingleMemberAnnotation)
            return wrap((SingleMemberAnnotation) anno);

        throw new NotImplementedException();
    }

    private static IStrategoAppl wrap(SingleMemberAnnotation annotation) {
        if(annotation == null)
            return None.INSTANCE;
        else
            return new WrappedSingleMemberAnnotation(annotation);
    }

    private static IStrategoAppl wrap(NormalAnnotation annotation) {
        if(annotation == null)
            return None.INSTANCE;
        else
            return new WrappedNormalAnnotation(annotation);
    }

    private static IStrategoAppl wrap(MarkerAnnotation annotation) {
        if(annotation == null) 
            return None.INSTANCE;
        else
            return new WrappedMarkerAnnotation(annotation);
    }

    private static IStrategoAppl wrap(VariableDeclarationExpression expression) {
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedVariableDeclarationExpression(expression);
    }

    private static IStrategoAppl wrap(TypeLiteral literal) {
        if(literal == null)
            return None.INSTANCE;
        else
            return new WrappedTypeLiteral(literal);
    }

    private static IStrategoAppl wrap(ThisExpression expression) {
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedThisExpression(expression);
    }

    private static IStrategoAppl wrap(SuperMethodInvocation invocation) {
        if(invocation == null)
            return None.INSTANCE;
        else
            return new WrappedSuperMethodInvocation(invocation);
    }

    private static IStrategoAppl wrap(SuperFieldAccess access) {
        if(access == null)
            return None.INSTANCE;
        else
            return new WrappedSuperFieldAccess(access);
    }

    private static IStrategoAppl wrap(StringLiteral literal) {
        if(literal == null) 
            return None.INSTANCE;
        else
            return new WrappedStringLiteral(literal);
    }

    private static IStrategoAppl wrap(PrefixExpression expression) {
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedPrefixExpression(expression);
    }

    private static IStrategoAppl wrap(PostfixExpression expression) {
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedPostfixExpression(expression);
    }

    private static IStrategoAppl wrap(ParenthesizedExpression expression) {
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedParenthesizedExpression(expression);
    }

    private static IStrategoAppl wrap(NumberLiteral literal) {
        if(literal == null)
            return None.INSTANCE;
        else
            return new WrappedNumberLiteral(literal);
    }

    private static IStrategoAppl wrap(NullLiteral literal) {
        if(literal == null)
            return None.INSTANCE;
        else
            return new WrappedNullLiteral(literal);
    }

    private static IStrategoAppl wrap(MethodInvocation invocation) {
        if(invocation == null)
            return None.INSTANCE;
        else
            return new WrappedMethodInvocation(invocation);
    }

    private static IStrategoAppl wrap(InstanceofExpression expression) {
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedInstanceofExpression(expression);
    }

    private static IStrategoAppl wrap(InfixExpression expression) {
        
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedInfixExpression(expression);

/*
        InfixExpression.Operator op = expression.getOperator();
        
        if(op == InfixExpression.Operator.PLUS)
            return new WrappedPlus(expression); 
        if(op == InfixExpression.Operator.MINUS)
            return new WrappedMinus(expression); 
        if(op == InfixExpression.Operator.TIMES)
            return new WrappedTimes(expression); 
        if(op == InfixExpression.Operator.DIVIDE)
            return new WrappedDivide(expression); 

        throw new NotImplementedException("Unknown InfixExpression Operator " + expression.getO);
*/  
    }

    private static IStrategoAppl wrap(FieldAccess access) {
        if(access == null)
            return None.INSTANCE;
        else
            return new WrappedFieldAccess(access);
    }

    private static IStrategoAppl wrap(ConditionalExpression expression) {
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedConditionalExpression(expression);
    }

    private static IStrategoAppl wrap(ClassInstanceCreation creation) {
        if(creation == null)
            return None.INSTANCE;
        else
            return new WrappedClassInstanceCreation(creation);
    }

    private static IStrategoAppl wrap(CharacterLiteral literal) {
        if(literal == null)
            return None.INSTANCE;
        else
            return new WrappedCharacterLiteral(literal);
    }

    private static IStrategoAppl wrap(CastExpression expression) {
        if(expression == null)
            return None.INSTANCE;
        else
            return new WrappedCastExpression(expression);
    }

    private static IStrategoAppl wrap(BooleanLiteral literal) {
        if(literal == null)
            return None.INSTANCE;
        else
            return new WrappedBooleanLiteral(literal);
    }

    private static IStrategoAppl wrap(Assignment assignment) {
        if(assignment == null)
            return None.INSTANCE;
        else
            return new WrappedAssignment(assignment);
    }

    private static IStrategoAppl wrap(ArrayInitializer initializer) {
        if(initializer == null)
            return None.INSTANCE;
        else 
            return new WrappedArrayInitializer(initializer);
    }

    private static IStrategoAppl wrap(ArrayCreation creation) {
        if(creation == null)
            return None.INSTANCE;
        else
            return new WrappedArrayCreation(creation);
    }

    private static IStrategoAppl wrap(ArrayAccess access) {
        if(access == null)
            return None.INSTANCE;
        else
            return new WrappedArrayAccess(access);
    }

    public static IStrategoTerm wrapType(Type type) {
        
        if(type instanceof ArrayType)
            return wrap((ArrayType) type);
        if(type instanceof ParameterizedType)
            return wrap((ParameterizedType) type);
        if(type instanceof PrimitiveType)
            return wrap((PrimitiveType) type);
        if(type instanceof QualifiedType)
            return wrap((QualifiedType) type);
        if(type instanceof SimpleType)
            return wrap((SimpleType) type);
        if(type instanceof WildcardType)
            return wrap((WildcardType) type);
        
        if(type == null)
            return None.INSTANCE;
        
        throw new NotImplementedException(" " + type.getClass());
    }

    private static IStrategoAppl wrap(WildcardType type) {
        if(type == null)
            return None.INSTANCE;
        else
            return new WrappedWildcardType(type);
    }

    private static IStrategoAppl wrap(SimpleType type) {
        if(type == null)
            return None.INSTANCE;
        else
            return new WrappedSimpleType(type);
    }

    private static IStrategoAppl wrap(QualifiedType type) {
        if(type == null)
            return None.INSTANCE;
        else
            return new WrappedQualifiedType(type);
    }

    private static IStrategoAppl wrap(PrimitiveType type) {
        
        if(type == null)
            return None.INSTANCE;
        else
            return new WrappedPrimitiveType(type);
        /*
        if(type.getPrimitiveTypeCode() == PrimitiveType.INT)
            return new WrappedIntType(type);
        if(type.getPrimitiveTypeCode() == PrimitiveType.BOOLEAN)
            return new WrappedBooleanType(type);
        if(type.getPrimitiveTypeCode() == PrimitiveType.DOUBLE)
            return new WrappedDoubleType(type);
        if(type.getPrimitiveTypeCode() == PrimitiveType.FLOAT)
            return new WrappedFloatType(type);
        if(type.getPrimitiveTypeCode() == PrimitiveType.BYTE)
            return new WrappedByteType(type);
        if(type.getPrimitiveTypeCode() == PrimitiveType.LONG)
            return new WrappedLongType(type);
        
        throw new NotImplementedException();
        */
    }

    private static IStrategoAppl wrap(ParameterizedType type) {
        if(type == null)
            return None.INSTANCE;
        else
            return new WrappedParameterizedType(type); 
    }

    public static IStrategoAppl wrap(ModifierKeyword keyword) {
        if(keyword == null)
            return None.INSTANCE;
        else 
            return new WrappedModifierKeyword(keyword);
    }

    public static IStrategoAppl wrap(PostfixExpression.Operator operator) {
        if(operator == null)
            return None.INSTANCE;
        else
            return new WrappedPostfixExpressionOperator(operator);
    }

    public static IStrategoAppl wrap(PrefixExpression.Operator operator) {
        if(operator == null)
            return None.INSTANCE;
        else
            return new WrappedPrefixExpressionOperator(operator);
    }

    public static IStrategoTerm wrapTypeDecl(AbstractTypeDeclaration decl) {
        if(decl instanceof AnnotationTypeDeclaration)
            return wrap((AnnotationTypeDeclaration) decl);
        if(decl instanceof EnumDeclaration)
            return wrap((EnumDeclaration) decl);
        if(decl instanceof TypeDeclaration)
            return wrap((TypeDeclaration) decl);
        
        throw new NotImplementedException();
    }

    private static IStrategoAppl wrap(EnumDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedEnumDeclaration(declaration);
    }

    private static IStrategoAppl wrap(AnnotationTypeDeclaration declaration) {
        if(declaration == null)
            return None.INSTANCE;
        else
            return new WrappedAnnotationTypeDeclaration(declaration);
    }

    public static IStrategoTerm wrap(ITypeBinding binding) {
        if(binding == null)
            return None.INSTANCE;
        else
            return new WrappedITypeBinding(binding);
    }

    public static IStrategoTerm wrap(ITypeBinding[] bindings) {
        IStrategoTerm[] terms = new IStrategoTerm[bindings.length];
        for(int i = 0, sz = bindings.length; i < sz; i++)
            terms[i] = ECJFactory.wrap(bindings[i]);
        return new WrappedGenericList(terms);
    }

    public static IStrategoTerm wrap(ITypeParameter[] parameters) {
        final IStrategoTerm[] terms = new IStrategoTerm[parameters.length];
        for(int i = 0, sz = parameters.length; i < sz; i++)
            terms[i] = ECJFactory.wrap(parameters[i]);
        return new WrappedGenericList(terms);
    }
    
    public static IStrategoTerm wrap(IField[] fields) {
        IStrategoTerm[] terms = new IStrategoTerm[fields.length];
        for(int i = 0, sz = fields.length; i < sz; i++)
            terms[i] = ECJFactory.wrap(fields[i]);
        return new WrappedGenericList(terms);
    }


    private static IStrategoTerm wrap(IField field) {
    	if(field == null)
    		return None.INSTANCE;
    	else
    		return new WrappedIField(field);
	}

	public static IStrategoTerm wrap(ITypeParameter binding) {
        if(binding == null)
            return None.INSTANCE;
        else
            return new WrappedITypeParameter(binding);
    }

    public static IStrategoTerm wrap(IProject proj) {
        if(proj == null)
            return None.INSTANCE;
        else
            return new WrappedIProject(proj);
    }

    public static IStrategoTerm wrap(String[] strs) {
        IStrategoTerm[] r = new IStrategoTerm[strs.length];
        for(int i = 0; i < r.length; i++)
            r[i] = wrap(strs[i]);
        return new WrappedGenericList(r);
    }

    public static IStrategoTerm wrap(IMethodBinding mb) {
        if(mb == null)
            return None.INSTANCE;
        else
            return new WrappedIMethodBinding(mb);
    }

    public void setAST(AST ast) {
        this.ast = ast;
    }

    @Deprecated
    public IStrategoAppl replaceAppl(IStrategoConstructor constructor, IStrategoTerm[] kids, IStrategoTerm old) {
        return replaceAppl(constructor, kids, (IStrategoAppl)old);
    }
    
    public IStrategoAppl replaceAppl(IStrategoConstructor constructor, IStrategoTerm[] kids, IStrategoAppl old) {
        final IStrategoAppl r = makeAppl(constructor, kids);
        // FIXME None should be in a different hierarchy than other WrappedASTNodes
        if(r instanceof WrappedASTNode && old instanceof WrappedASTNode) {
            final WrappedASTNode n = (WrappedASTNode)r;
            final WrappedASTNode o = (WrappedASTNode)old;
            final ASTNode nn = n.getWrappee();
            final ASTNode on = o.getWrappee();
            if(nn != null && on != null)
                nn.setSourceRange(on.getStartPosition(), on.getLength());
        }
        return r;
    }
    
    public IStrategoTuple replaceTuple(IStrategoTerm[] kids, IStrategoTuple old) {
        return makeTuple(kids);
    }
    
    public IStrategoList replaceList(IStrategoTerm[] kids, IStrategoList old) {
        return makeList(kids);
    }

    public static IStrategoTerm wrap(IType t) {
        if(t == null)
            return None.INSTANCE;
        else
            return new WrappedIType(t);
    }

    public static IStrategoTerm wrap(IJavaProject jp) {
        if(jp == null)
            return None.INSTANCE;
        else
            return new WrappedIJavaProject(jp);
    }

    public static IStrategoTerm wrap(ICompilationUnit cu) {
        if(cu == null)
            return None.INSTANCE;
        else
            return new WrappedICompilationUnit(cu);
    }

    public static IStrategoTerm wrap(IFile file) {
        if(file == null)
            return None.INSTANCE;
        else
            return new WrappedIFile(file);
    }

    public static IStrategoTerm wrap(IBinding binding) {
        if(binding == null)
            return None.INSTANCE;
        else
            return new WrappedIBinding(binding);
    }

	public static IStrategoAppl wrap(Assignment.Operator operator) {
		if(operator == null)
			return None.INSTANCE;
		else
			return new WrappedAssignmentOperator(operator);
	}

	public static IStrategoTerm wrap(ITypeHierarchy th) {
		if(th == null)
			return None.INSTANCE;
		else
			return new WrappedITypeHierarchy(th);
	}

	public static IStrategoTerm wrapAmbName(String name) {
		if(name == null)
			return None.INSTANCE;
		else 
			return new AmbName(name);
	}

	public static IStrategoTerm wrapDottedName(String name) {
		if(name == null)
			return None.INSTANCE;
		else 
			return new DottedName(name);
	}

	public static IStrategoTerm fullyGenericWrap(Object o) {
		if(o instanceof String)
			return wrap((String)o);
		if(o instanceof Integer)
			return wrap((Integer)o);
		if(o instanceof Boolean)
			return wrap(((Boolean)o) ? 1 : 0);
		if(o instanceof ASTNode)
			return genericWrap((ASTNode)o);
		if(o instanceof IType)
			return wrap((IType)o);
		
		throw new NotImplementedException(" " + o.getClass());
	}

	public AST getAST() {
		return ast;
	}

	public static IStrategoTerm wrap(IJavaElement el) {
		if(el == null)
			return None.INSTANCE;
		else
			return new WrappedIJavaElement(el);
	}

	public static IStrategoAppl wrapSignature(String s) {
		if(s == null)
			return None.INSTANCE;
		else
			return new WrappedActualTypeSignature(s);
		
	}

	public static IStrategoTerm wrapSignatures(String[] signatures) {
		if(signatures == null)
			return None.INSTANCE;
		else {
			IStrategoAppl[] ws = new WrappedActualTypeSignature[signatures.length];
			for(int i = 0; i < signatures.length; i++)
				ws[i] = wrapSignature(signatures[i]);
			return new BasicStrategoArrayList(ws);
		}

	}

	public static ASTMatcher getMatcher() {
		if(astMatcher == null)
			astMatcher = new ASTMatcher();
		return astMatcher;
	}

}
