package parser.ast;

public abstract class AbstractVisitor  
{ 
  public abstract void unimplementedVisitor(java.lang.String source);

  public boolean preVisit(ASTNode node)
  { 
    return true;
  }

  public void postVisit(ASTNode node)
  { }

  public void endVisit(Ws node)
  { 
    unimplementedVisitor("endVisit(" + "Ws" + ")");
  }

  public boolean visit(Ws node)
  { 
    unimplementedVisitor("visit(" + "Ws" + ")");
    return true;
  }

  public void endVisit(ShortCom node)
  { 
    unimplementedVisitor("endVisit(" + "ShortCom" + ")");
  }

  public boolean visit(ShortCom node)
  { 
    unimplementedVisitor("visit(" + "ShortCom" + ")");
    return true;
  }

  public void endVisit(LongCom node)
  { 
    unimplementedVisitor("endVisit(" + "LongCom" + ")");
  }

  public boolean visit(LongCom node)
  { 
    unimplementedVisitor("visit(" + "LongCom" + ")");
    return true;
  }

  public void endVisit(Eof node)
  { 
    unimplementedVisitor("endVisit(" + "Eof" + ")");
  }

  public boolean visit(Eof node)
  { 
    unimplementedVisitor("visit(" + "Eof" + ")");
    return true;
  }

  public void endVisit(CommChar node)
  { 
    unimplementedVisitor("endVisit(" + "CommChar" + ")");
  }

  public boolean visit(CommChar node)
  { 
    unimplementedVisitor("visit(" + "CommChar" + ")");
    return true;
  }

  public void endVisit(Asterisk node)
  { 
    unimplementedVisitor("endVisit(" + "Asterisk" + ")");
  }

  public boolean visit(Asterisk node)
  { 
    unimplementedVisitor("visit(" + "Asterisk" + ")");
    return true;
  }

  public void endVisit(ModName_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "ModName_StrategoHost" + ")");
  }

  public boolean visit(ModName_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "ModName_StrategoHost" + ")");
    return true;
  }

  public void endVisit(ModNamePart_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "ModNamePart_StrategoHost" + ")");
  }

  public boolean visit(ModNamePart_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "ModNamePart_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Id_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "Id_StrategoHost" + ")");
  }

  public boolean visit(Id_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "Id_StrategoHost" + ")");
    return true;
  }

  public void endVisit(LId_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "LId_StrategoHost" + ")");
  }

  public boolean visit(LId_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "LId_StrategoHost" + ")");
    return true;
  }

  public void endVisit(LCID_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "LCID_StrategoHost" + ")");
  }

  public boolean visit(LCID_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "LCID_StrategoHost" + ")");
    return true;
  }

  public void endVisit(UCID_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "UCID_StrategoHost" + ")");
  }

  public boolean visit(UCID_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "UCID_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Keyword_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "Keyword_StrategoHost" + ")");
  }

  public boolean visit(Keyword_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "Keyword_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Int_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "Int_StrategoHost" + ")");
  }

  public boolean visit(Int_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "Int_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Real_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "Real_StrategoHost" + ")");
  }

  public boolean visit(Real_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "Real_StrategoHost" + ")");
    return true;
  }

  public void endVisit(String_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "String_StrategoHost" + ")");
  }

  public boolean visit(String_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "String_StrategoHost" + ")");
    return true;
  }

  public void endVisit(StrChar_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "StrChar_StrategoHost" + ")");
  }

  public boolean visit(StrChar_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "StrChar_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Char_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "Char_StrategoHost" + ")");
  }

  public boolean visit(Char_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "Char_StrategoHost" + ")");
    return true;
  }

  public void endVisit(CharChar_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "CharChar_StrategoHost" + ")");
  }

  public boolean visit(CharChar_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "CharChar_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Var0 node)
  { 
    unimplementedVisitor("endVisit(" + "Var0" + ")");
  }

  public boolean visit(Var0 node)
  { 
    unimplementedVisitor("visit(" + "Var0" + ")");
    return true;
  }

  public void endVisit(ID_StrategoHost0 node)
  { 
    unimplementedVisitor("endVisit(" + "ID_StrategoHost0" + ")");
  }

  public boolean visit(ID_StrategoHost0 node)
  { 
    unimplementedVisitor("visit(" + "ID_StrategoHost0" + ")");
    return true;
  }

  public void endVisit(PreTerm_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "PreTerm_StrategoHost" + ")");
  }

  public boolean visit(PreTerm_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "PreTerm_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Term_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "Term_StrategoHost" + ")");
  }

  public boolean visit(Term_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "Term_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Wld0 node)
  { 
    unimplementedVisitor("endVisit(" + "Wld0" + ")");
  }

  public boolean visit(Wld0 node)
  { 
    unimplementedVisitor("visit(" + "Wld0" + ")");
    return true;
  }

  public void endVisit(Wld node)
  { 
    unimplementedVisitor("endVisit(" + "Wld" + ")");
  }

  public boolean visit(Wld node)
  { 
    unimplementedVisitor("visit(" + "Wld" + ")");
    return true;
  }

  public void endVisit(Int0 node)
  { 
    unimplementedVisitor("endVisit(" + "Int0" + ")");
  }

  public boolean visit(Int0 node)
  { 
    unimplementedVisitor("visit(" + "Int0" + ")");
    return true;
  }

  public void endVisit(Real node)
  { 
    unimplementedVisitor("endVisit(" + "Real" + ")");
  }

  public boolean visit(Real node)
  { 
    unimplementedVisitor("visit(" + "Real" + ")");
    return true;
  }

  public void endVisit(Str node)
  { 
    unimplementedVisitor("endVisit(" + "Str" + ")");
  }

  public boolean visit(Str node)
  { 
    unimplementedVisitor("visit(" + "Str" + ")");
    return true;
  }

  public void endVisit(Op node)
  { 
    unimplementedVisitor("endVisit(" + "Op" + ")");
  }

  public boolean visit(Op node)
  { 
    unimplementedVisitor("visit(" + "Op" + ")");
    return true;
  }

  public void endVisit(OpQ node)
  { 
    unimplementedVisitor("endVisit(" + "OpQ" + ")");
  }

  public boolean visit(OpQ node)
  { 
    unimplementedVisitor("visit(" + "OpQ" + ")");
    return true;
  }

  public void endVisit(Explode node)
  { 
    unimplementedVisitor("endVisit(" + "Explode" + ")");
  }

  public boolean visit(Explode node)
  { 
    unimplementedVisitor("visit(" + "Explode" + ")");
    return true;
  }

  public void endVisit(Anno0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno0" + ")");
  }

  public boolean visit(Anno0 node)
  { 
    unimplementedVisitor("visit(" + "Anno0" + ")");
    return true;
  }

  public void endVisit(As0 node)
  { 
    unimplementedVisitor("endVisit(" + "As0" + ")");
  }

  public boolean visit(As0 node)
  { 
    unimplementedVisitor("visit(" + "As0" + ")");
    return true;
  }

  public void endVisit(As node)
  { 
    unimplementedVisitor("endVisit(" + "As" + ")");
  }

  public boolean visit(As node)
  { 
    unimplementedVisitor("visit(" + "As" + ")");
    return true;
  }

  public void endVisit(Sorts node)
  { 
    unimplementedVisitor("endVisit(" + "Sorts" + ")");
  }

  public boolean visit(Sorts node)
  { 
    unimplementedVisitor("visit(" + "Sorts" + ")");
    return true;
  }

  public void endVisit(Constructors node)
  { 
    unimplementedVisitor("endVisit(" + "Constructors" + ")");
  }

  public boolean visit(Constructors node)
  { 
    unimplementedVisitor("visit(" + "Constructors" + ")");
    return true;
  }

  public void endVisit(SortVar node)
  { 
    unimplementedVisitor("endVisit(" + "SortVar" + ")");
  }

  public boolean visit(SortVar node)
  { 
    unimplementedVisitor("visit(" + "SortVar" + ")");
    return true;
  }

  public void endVisit(SortNoArgs node)
  { 
    unimplementedVisitor("endVisit(" + "SortNoArgs" + ")");
  }

  public boolean visit(SortNoArgs node)
  { 
    unimplementedVisitor("visit(" + "SortNoArgs" + ")");
    return true;
  }

  public void endVisit(Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Sort" + ")");
  }

  public boolean visit(Sort node)
  { 
    unimplementedVisitor("visit(" + "Sort" + ")");
    return true;
  }

  public void endVisit(OpDecl node)
  { 
    unimplementedVisitor("endVisit(" + "OpDecl" + ")");
  }

  public boolean visit(OpDecl node)
  { 
    unimplementedVisitor("visit(" + "OpDecl" + ")");
    return true;
  }

  public void endVisit(OpDeclQ node)
  { 
    unimplementedVisitor("endVisit(" + "OpDeclQ" + ")");
  }

  public boolean visit(OpDeclQ node)
  { 
    unimplementedVisitor("visit(" + "OpDeclQ" + ")");
    return true;
  }

  public void endVisit(OpDeclInj node)
  { 
    unimplementedVisitor("endVisit(" + "OpDeclInj" + ")");
  }

  public boolean visit(OpDeclInj node)
  { 
    unimplementedVisitor("visit(" + "OpDeclInj" + ")");
    return true;
  }

  public void endVisit(ConstType node)
  { 
    unimplementedVisitor("endVisit(" + "ConstType" + ")");
  }

  public boolean visit(ConstType node)
  { 
    unimplementedVisitor("visit(" + "ConstType" + ")");
    return true;
  }

  public void endVisit(FunType node)
  { 
    unimplementedVisitor("endVisit(" + "FunType" + ")");
  }

  public boolean visit(FunType node)
  { 
    unimplementedVisitor("visit(" + "FunType" + ")");
    return true;
  }

  public void endVisit(ArgType node)
  { 
    unimplementedVisitor("endVisit(" + "ArgType" + ")");
  }

  public boolean visit(ArgType node)
  { 
    unimplementedVisitor("visit(" + "ArgType" + ")");
    return true;
  }

  public void endVisit(ArgType_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "ArgType_StrategoHost" + ")");
  }

  public boolean visit(ArgType_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "ArgType_StrategoHost" + ")");
    return true;
  }

  public void endVisit(RetType_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "RetType_StrategoHost" + ")");
  }

  public boolean visit(RetType_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "RetType_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Type_StrategoHost0 node)
  { 
    unimplementedVisitor("endVisit(" + "Type_StrategoHost0" + ")");
  }

  public boolean visit(Type_StrategoHost0 node)
  { 
    unimplementedVisitor("visit(" + "Type_StrategoHost0" + ")");
    return true;
  }

  public void endVisit(Type_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "Type_StrategoHost" + ")");
  }

  public boolean visit(Type_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "Type_StrategoHost" + ")");
    return true;
  }

  public void endVisit(Def_StrategoHost0 node)
  { 
    unimplementedVisitor("endVisit(" + "Def_StrategoHost0" + ")");
  }

  public boolean visit(Def_StrategoHost0 node)
  { 
    unimplementedVisitor("visit(" + "Def_StrategoHost0" + ")");
    return true;
  }

  public void endVisit(SVar node)
  { 
    unimplementedVisitor("endVisit(" + "SVar" + ")");
  }

  public boolean visit(SVar node)
  { 
    unimplementedVisitor("visit(" + "SVar" + ")");
    return true;
  }

  public void endVisit(Let node)
  { 
    unimplementedVisitor("endVisit(" + "Let" + ")");
  }

  public boolean visit(Let node)
  { 
    unimplementedVisitor("visit(" + "Let" + ")");
    return true;
  }

  public void endVisit(CallT node)
  { 
    unimplementedVisitor("endVisit(" + "CallT" + ")");
  }

  public boolean visit(CallT node)
  { 
    unimplementedVisitor("visit(" + "CallT" + ")");
    return true;
  }

  public void endVisit(CallDynamic node)
  { 
    unimplementedVisitor("endVisit(" + "CallDynamic" + ")");
  }

  public boolean visit(CallDynamic node)
  { 
    unimplementedVisitor("visit(" + "CallDynamic" + ")");
    return true;
  }

  public void endVisit(SDefT node)
  { 
    unimplementedVisitor("endVisit(" + "SDefT" + ")");
  }

  public boolean visit(SDefT node)
  { 
    unimplementedVisitor("visit(" + "SDefT" + ")");
    return true;
  }

  public void endVisit(ExtSDefInl node)
  { 
    unimplementedVisitor("endVisit(" + "ExtSDefInl" + ")");
  }

  public boolean visit(ExtSDefInl node)
  { 
    unimplementedVisitor("visit(" + "ExtSDefInl" + ")");
    return true;
  }

  public void endVisit(ExtSDef node)
  { 
    unimplementedVisitor("endVisit(" + "ExtSDef" + ")");
  }

  public boolean visit(ExtSDef node)
  { 
    unimplementedVisitor("visit(" + "ExtSDef" + ")");
    return true;
  }

  public void endVisit(VarDec1 node)
  { 
    unimplementedVisitor("endVisit(" + "VarDec1" + ")");
  }

  public boolean visit(VarDec1 node)
  { 
    unimplementedVisitor("visit(" + "VarDec1" + ")");
    return true;
  }

  public void endVisit(ParenStrat node)
  { 
    unimplementedVisitor("endVisit(" + "ParenStrat" + ")");
  }

  public boolean visit(ParenStrat node)
  { 
    unimplementedVisitor("visit(" + "ParenStrat" + ")");
    return true;
  }

  public void endVisit(Fail node)
  { 
    unimplementedVisitor("endVisit(" + "Fail" + ")");
  }

  public boolean visit(Fail node)
  { 
    unimplementedVisitor("visit(" + "Fail" + ")");
    return true;
  }

  public void endVisit(Id0 node)
  { 
    unimplementedVisitor("endVisit(" + "Id0" + ")");
  }

  public boolean visit(Id0 node)
  { 
    unimplementedVisitor("visit(" + "Id0" + ")");
    return true;
  }

  public void endVisit(Match node)
  { 
    unimplementedVisitor("endVisit(" + "Match" + ")");
  }

  public boolean visit(Match node)
  { 
    unimplementedVisitor("visit(" + "Match" + ")");
    return true;
  }

  public void endVisit(Build node)
  { 
    unimplementedVisitor("endVisit(" + "Build" + ")");
  }

  public boolean visit(Build node)
  { 
    unimplementedVisitor("visit(" + "Build" + ")");
    return true;
  }

  public void endVisit(Scope node)
  { 
    unimplementedVisitor("endVisit(" + "Scope" + ")");
  }

  public boolean visit(Scope node)
  { 
    unimplementedVisitor("visit(" + "Scope" + ")");
    return true;
  }

  public void endVisit(Seq node)
  { 
    unimplementedVisitor("endVisit(" + "Seq" + ")");
  }

  public boolean visit(Seq node)
  { 
    unimplementedVisitor("visit(" + "Seq" + ")");
    return true;
  }

  public void endVisit(GuardedLChoice node)
  { 
    unimplementedVisitor("endVisit(" + "GuardedLChoice" + ")");
  }

  public boolean visit(GuardedLChoice node)
  { 
    unimplementedVisitor("visit(" + "GuardedLChoice" + ")");
    return true;
  }

  public void endVisit(StrategyMid_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "StrategyMid_StrategoHost" + ")");
  }

  public boolean visit(StrategyMid_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "StrategyMid_StrategoHost" + ")");
    return true;
  }

  public void endVisit(PrimT node)
  { 
    unimplementedVisitor("endVisit(" + "PrimT" + ")");
  }

  public boolean visit(PrimT node)
  { 
    unimplementedVisitor("visit(" + "PrimT" + ")");
    return true;
  }

  public void endVisit(Some node)
  { 
    unimplementedVisitor("endVisit(" + "Some" + ")");
  }

  public boolean visit(Some node)
  { 
    unimplementedVisitor("visit(" + "Some" + ")");
    return true;
  }

  public void endVisit(One node)
  { 
    unimplementedVisitor("endVisit(" + "One" + ")");
  }

  public boolean visit(One node)
  { 
    unimplementedVisitor("visit(" + "One" + ")");
    return true;
  }

  public void endVisit(All node)
  { 
    unimplementedVisitor("endVisit(" + "All" + ")");
  }

  public boolean visit(All node)
  { 
    unimplementedVisitor("visit(" + "All" + ")");
    return true;
  }

  public void endVisit(ImportTerm node)
  { 
    unimplementedVisitor("endVisit(" + "ImportTerm" + ")");
  }

  public boolean visit(ImportTerm node)
  { 
    unimplementedVisitor("visit(" + "ImportTerm" + ")");
    return true;
  }

  public void endVisit(Module node)
  { 
    unimplementedVisitor("endVisit(" + "Module" + ")");
  }

  public boolean visit(Module node)
  { 
    unimplementedVisitor("visit(" + "Module" + ")");
    return true;
  }

  public void endVisit(Specification node)
  { 
    unimplementedVisitor("endVisit(" + "Specification" + ")");
  }

  public boolean visit(Specification node)
  { 
    unimplementedVisitor("visit(" + "Specification" + ")");
    return true;
  }

  public void endVisit(Imports node)
  { 
    unimplementedVisitor("endVisit(" + "Imports" + ")");
  }

  public boolean visit(Imports node)
  { 
    unimplementedVisitor("visit(" + "Imports" + ")");
    return true;
  }

  public void endVisit(Strategies node)
  { 
    unimplementedVisitor("endVisit(" + "Strategies" + ")");
  }

  public boolean visit(Strategies node)
  { 
    unimplementedVisitor("visit(" + "Strategies" + ")");
    return true;
  }

  public void endVisit(Signature node)
  { 
    unimplementedVisitor("endVisit(" + "Signature" + ")");
  }

  public boolean visit(Signature node)
  { 
    unimplementedVisitor("visit(" + "Signature" + ")");
    return true;
  }

  public void endVisit(Import node)
  { 
    unimplementedVisitor("endVisit(" + "Import" + ")");
  }

  public boolean visit(Import node)
  { 
    unimplementedVisitor("visit(" + "Import" + ")");
    return true;
  }

  public void endVisit(ImportWildcard node)
  { 
    unimplementedVisitor("endVisit(" + "ImportWildcard" + ")");
  }

  public boolean visit(ImportWildcard node)
  { 
    unimplementedVisitor("visit(" + "ImportWildcard" + ")");
    return true;
  }

  public void endVisit(ListVar node)
  { 
    unimplementedVisitor("endVisit(" + "ListVar" + ")");
  }

  public boolean visit(ListVar node)
  { 
    unimplementedVisitor("visit(" + "ListVar" + ")");
    return true;
  }

  public void endVisit(Var node)
  { 
    unimplementedVisitor("endVisit(" + "Var" + ")");
  }

  public boolean visit(Var node)
  { 
    unimplementedVisitor("visit(" + "Var" + ")");
    return true;
  }

  public void endVisit(ID_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "ID_StrategoHost" + ")");
  }

  public boolean visit(ID_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "ID_StrategoHost" + ")");
    return true;
  }

  public void endVisit(BuildDefaultPT node)
  { 
    unimplementedVisitor("endVisit(" + "BuildDefaultPT" + ")");
  }

  public boolean visit(BuildDefaultPT node)
  { 
    unimplementedVisitor("visit(" + "BuildDefaultPT" + ")");
    return true;
  }

  public void endVisit(BuildDefault node)
  { 
    unimplementedVisitor("endVisit(" + "BuildDefault" + ")");
  }

  public boolean visit(BuildDefault node)
  { 
    unimplementedVisitor("visit(" + "BuildDefault" + ")");
    return true;
  }

  public void endVisit(Char1 node)
  { 
    unimplementedVisitor("endVisit(" + "Char1" + ")");
  }

  public boolean visit(Char1 node)
  { 
    unimplementedVisitor("visit(" + "Char1" + ")");
    return true;
  }

  public void endVisit(AnnoList node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoList" + ")");
  }

  public boolean visit(AnnoList node)
  { 
    unimplementedVisitor("visit(" + "AnnoList" + ")");
    return true;
  }

  public void endVisit(NoAnnoList node)
  { 
    unimplementedVisitor("endVisit(" + "NoAnnoList" + ")");
  }

  public boolean visit(NoAnnoList node)
  { 
    unimplementedVisitor("visit(" + "NoAnnoList" + ")");
    return true;
  }

  public void endVisit(App0 node)
  { 
    unimplementedVisitor("endVisit(" + "App0" + ")");
  }

  public boolean visit(App0 node)
  { 
    unimplementedVisitor("visit(" + "App0" + ")");
    return true;
  }

  public void endVisit(App node)
  { 
    unimplementedVisitor("endVisit(" + "App" + ")");
  }

  public boolean visit(App node)
  { 
    unimplementedVisitor("visit(" + "App" + ")");
    return true;
  }

  public void endVisit(RootApp0 node)
  { 
    unimplementedVisitor("endVisit(" + "RootApp0" + ")");
  }

  public boolean visit(RootApp0 node)
  { 
    unimplementedVisitor("visit(" + "RootApp0" + ")");
    return true;
  }

  public void endVisit(RootApp node)
  { 
    unimplementedVisitor("endVisit(" + "RootApp" + ")");
  }

  public boolean visit(RootApp node)
  { 
    unimplementedVisitor("visit(" + "RootApp" + ")");
    return true;
  }

  public void endVisit(Tuple node)
  { 
    unimplementedVisitor("endVisit(" + "Tuple" + ")");
  }

  public boolean visit(Tuple node)
  { 
    unimplementedVisitor("visit(" + "Tuple" + ")");
    return true;
  }

  public void endVisit(List0 node)
  { 
    unimplementedVisitor("endVisit(" + "List0" + ")");
  }

  public boolean visit(List0 node)
  { 
    unimplementedVisitor("visit(" + "List0" + ")");
    return true;
  }

  public void endVisit(ListTail node)
  { 
    unimplementedVisitor("endVisit(" + "ListTail" + ")");
  }

  public boolean visit(ListTail node)
  { 
    unimplementedVisitor("visit(" + "ListTail" + ")");
    return true;
  }

  public void endVisit(SortList node)
  { 
    unimplementedVisitor("endVisit(" + "SortList" + ")");
  }

  public boolean visit(SortList node)
  { 
    unimplementedVisitor("visit(" + "SortList" + ")");
    return true;
  }

  public void endVisit(SortListTl node)
  { 
    unimplementedVisitor("endVisit(" + "SortListTl" + ")");
  }

  public boolean visit(SortListTl node)
  { 
    unimplementedVisitor("visit(" + "SortListTl" + ")");
    return true;
  }

  public void endVisit(SortTuple node)
  { 
    unimplementedVisitor("endVisit(" + "SortTuple" + ")");
  }

  public boolean visit(SortTuple node)
  { 
    unimplementedVisitor("visit(" + "SortTuple" + ")");
    return true;
  }

  public void endVisit(Star node)
  { 
    unimplementedVisitor("endVisit(" + "Star" + ")");
  }

  public boolean visit(Star node)
  { 
    unimplementedVisitor("visit(" + "Star" + ")");
    return true;
  }

  public void endVisit(StarStar node)
  { 
    unimplementedVisitor("endVisit(" + "StarStar" + ")");
  }

  public boolean visit(StarStar node)
  { 
    unimplementedVisitor("visit(" + "StarStar" + ")");
    return true;
  }

  public void endVisit(SDefNoArgs node)
  { 
    unimplementedVisitor("endVisit(" + "SDefNoArgs" + ")");
  }

  public boolean visit(SDefNoArgs node)
  { 
    unimplementedVisitor("visit(" + "SDefNoArgs" + ")");
    return true;
  }

  public void endVisit(SDef node)
  { 
    unimplementedVisitor("endVisit(" + "SDef" + ")");
  }

  public boolean visit(SDef node)
  { 
    unimplementedVisitor("visit(" + "SDef" + ")");
    return true;
  }

  public void endVisit(DefaultVarDec node)
  { 
    unimplementedVisitor("endVisit(" + "DefaultVarDec" + ")");
  }

  public boolean visit(DefaultVarDec node)
  { 
    unimplementedVisitor("visit(" + "DefaultVarDec" + ")");
    return true;
  }

  public void endVisit(Call node)
  { 
    unimplementedVisitor("endVisit(" + "Call" + ")");
  }

  public boolean visit(Call node)
  { 
    unimplementedVisitor("visit(" + "Call" + ")");
    return true;
  }

  public void endVisit(ScopeDefault node)
  { 
    unimplementedVisitor("endVisit(" + "ScopeDefault" + ")");
  }

  public boolean visit(ScopeDefault node)
  { 
    unimplementedVisitor("visit(" + "ScopeDefault" + ")");
    return true;
  }

  public void endVisit(BA node)
  { 
    unimplementedVisitor("endVisit(" + "BA" + ")");
  }

  public boolean visit(BA node)
  { 
    unimplementedVisitor("visit(" + "BA" + ")");
    return true;
  }

  public void endVisit(StrategyAngle node)
  { 
    unimplementedVisitor("endVisit(" + "StrategyAngle" + ")");
  }

  public boolean visit(StrategyAngle node)
  { 
    unimplementedVisitor("visit(" + "StrategyAngle" + ")");
    return true;
  }

  public void endVisit(LChoice node)
  { 
    unimplementedVisitor("endVisit(" + "LChoice" + ")");
  }

  public boolean visit(LChoice node)
  { 
    unimplementedVisitor("visit(" + "LChoice" + ")");
    return true;
  }

  public void endVisit(Rec node)
  { 
    unimplementedVisitor("endVisit(" + "Rec" + ")");
  }

  public boolean visit(Rec node)
  { 
    unimplementedVisitor("visit(" + "Rec" + ")");
    return true;
  }

  public void endVisit(Not0 node)
  { 
    unimplementedVisitor("endVisit(" + "Not0" + ")");
  }

  public boolean visit(Not0 node)
  { 
    unimplementedVisitor("visit(" + "Not0" + ")");
    return true;
  }

  public void endVisit(Where node)
  { 
    unimplementedVisitor("endVisit(" + "Where" + ")");
  }

  public boolean visit(Where node)
  { 
    unimplementedVisitor("visit(" + "Where" + ")");
    return true;
  }

  public void endVisit(Test node)
  { 
    unimplementedVisitor("endVisit(" + "Test" + ")");
  }

  public boolean visit(Test node)
  { 
    unimplementedVisitor("visit(" + "Test" + ")");
    return true;
  }

  public void endVisit(PrimNoArgs node)
  { 
    unimplementedVisitor("endVisit(" + "PrimNoArgs" + ")");
  }

  public boolean visit(PrimNoArgs node)
  { 
    unimplementedVisitor("visit(" + "PrimNoArgs" + ")");
    return true;
  }

  public void endVisit(Prim node)
  { 
    unimplementedVisitor("endVisit(" + "Prim" + ")");
  }

  public boolean visit(Prim node)
  { 
    unimplementedVisitor("visit(" + "Prim" + ")");
    return true;
  }

  public void endVisit(StrCong node)
  { 
    unimplementedVisitor("endVisit(" + "StrCong" + ")");
  }

  public boolean visit(StrCong node)
  { 
    unimplementedVisitor("visit(" + "StrCong" + ")");
    return true;
  }

  public void endVisit(IntCong node)
  { 
    unimplementedVisitor("endVisit(" + "IntCong" + ")");
  }

  public boolean visit(IntCong node)
  { 
    unimplementedVisitor("visit(" + "IntCong" + ")");
    return true;
  }

  public void endVisit(RealCong node)
  { 
    unimplementedVisitor("endVisit(" + "RealCong" + ")");
  }

  public boolean visit(RealCong node)
  { 
    unimplementedVisitor("visit(" + "RealCong" + ")");
    return true;
  }

  public void endVisit(CharCong node)
  { 
    unimplementedVisitor("endVisit(" + "CharCong" + ")");
  }

  public boolean visit(CharCong node)
  { 
    unimplementedVisitor("visit(" + "CharCong" + ")");
    return true;
  }

  public void endVisit(CongQ node)
  { 
    unimplementedVisitor("endVisit(" + "CongQ" + ")");
  }

  public boolean visit(CongQ node)
  { 
    unimplementedVisitor("visit(" + "CongQ" + ")");
    return true;
  }

  public void endVisit(AnnoCong node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoCong" + ")");
  }

  public boolean visit(AnnoCong node)
  { 
    unimplementedVisitor("visit(" + "AnnoCong" + ")");
    return true;
  }

  public void endVisit(StrategyCurly node)
  { 
    unimplementedVisitor("endVisit(" + "StrategyCurly" + ")");
  }

  public boolean visit(StrategyCurly node)
  { 
    unimplementedVisitor("visit(" + "StrategyCurly" + ")");
    return true;
  }

  public void endVisit(EmptyTupleCong node)
  { 
    unimplementedVisitor("endVisit(" + "EmptyTupleCong" + ")");
  }

  public boolean visit(EmptyTupleCong node)
  { 
    unimplementedVisitor("visit(" + "EmptyTupleCong" + ")");
    return true;
  }

  public void endVisit(Strategy node)
  { 
    unimplementedVisitor("endVisit(" + "Strategy" + ")");
  }

  public boolean visit(Strategy node)
  { 
    unimplementedVisitor("visit(" + "Strategy" + ")");
    return true;
  }

  public void endVisit(TupleCong node)
  { 
    unimplementedVisitor("endVisit(" + "TupleCong" + ")");
  }

  public boolean visit(TupleCong node)
  { 
    unimplementedVisitor("visit(" + "TupleCong" + ")");
    return true;
  }

  public void endVisit(ListCongNoTail node)
  { 
    unimplementedVisitor("endVisit(" + "ListCongNoTail" + ")");
  }

  public boolean visit(ListCongNoTail node)
  { 
    unimplementedVisitor("visit(" + "ListCongNoTail" + ")");
    return true;
  }

  public void endVisit(ListCong node)
  { 
    unimplementedVisitor("endVisit(" + "ListCong" + ")");
  }

  public boolean visit(ListCong node)
  { 
    unimplementedVisitor("visit(" + "ListCong" + ")");
    return true;
  }

  public void endVisit(ExplodeCong node)
  { 
    unimplementedVisitor("endVisit(" + "ExplodeCong" + ")");
  }

  public boolean visit(ExplodeCong node)
  { 
    unimplementedVisitor("visit(" + "ExplodeCong" + ")");
    return true;
  }

  public void endVisit(CallNoArgs node)
  { 
    unimplementedVisitor("endVisit(" + "CallNoArgs" + ")");
  }

  public boolean visit(CallNoArgs node)
  { 
    unimplementedVisitor("visit(" + "CallNoArgs" + ")");
    return true;
  }

  public void endVisit(LRule node)
  { 
    unimplementedVisitor("endVisit(" + "LRule" + ")");
  }

  public boolean visit(LRule node)
  { 
    unimplementedVisitor("visit(" + "LRule" + ")");
    return true;
  }

  public void endVisit(SRule node)
  { 
    unimplementedVisitor("endVisit(" + "SRule" + ")");
  }

  public boolean visit(SRule node)
  { 
    unimplementedVisitor("visit(" + "SRule" + ")");
    return true;
  }

  public void endVisit(Choice node)
  { 
    unimplementedVisitor("endVisit(" + "Choice" + ")");
  }

  public boolean visit(Choice node)
  { 
    unimplementedVisitor("visit(" + "Choice" + ")");
    return true;
  }

  public void endVisit(RChoice node)
  { 
    unimplementedVisitor("endVisit(" + "RChoice" + ")");
  }

  public boolean visit(RChoice node)
  { 
    unimplementedVisitor("visit(" + "RChoice" + ")");
    return true;
  }

  public void endVisit(CondChoice node)
  { 
    unimplementedVisitor("endVisit(" + "CondChoice" + ")");
  }

  public boolean visit(CondChoice node)
  { 
    unimplementedVisitor("visit(" + "CondChoice" + ")");
    return true;
  }

  public void endVisit(IfThen node)
  { 
    unimplementedVisitor("endVisit(" + "IfThen" + ")");
  }

  public boolean visit(IfThen node)
  { 
    unimplementedVisitor("visit(" + "IfThen" + ")");
    return true;
  }

  public void endVisit(SwitchChoiceNoOtherwise node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchChoiceNoOtherwise" + ")");
  }

  public boolean visit(SwitchChoiceNoOtherwise node)
  { 
    unimplementedVisitor("visit(" + "SwitchChoiceNoOtherwise" + ")");
    return true;
  }

  public void endVisit(SwitchChoice node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchChoice" + ")");
  }

  public boolean visit(SwitchChoice node)
  { 
    unimplementedVisitor("visit(" + "SwitchChoice" + ")");
    return true;
  }

  public void endVisit(SwitchCase node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchCase" + ")");
  }

  public boolean visit(SwitchCase node)
  { 
    unimplementedVisitor("visit(" + "SwitchCase" + ")");
    return true;
  }

  public void endVisit(AM node)
  { 
    unimplementedVisitor("endVisit(" + "AM" + ")");
  }

  public boolean visit(AM node)
  { 
    unimplementedVisitor("visit(" + "AM" + ")");
    return true;
  }

  public void endVisit(Assign0 node)
  { 
    unimplementedVisitor("endVisit(" + "Assign0" + ")");
  }

  public boolean visit(Assign0 node)
  { 
    unimplementedVisitor("visit(" + "Assign0" + ")");
    return true;
  }

  public void endVisit(OverlayNoArgs node)
  { 
    unimplementedVisitor("endVisit(" + "OverlayNoArgs" + ")");
  }

  public boolean visit(OverlayNoArgs node)
  { 
    unimplementedVisitor("visit(" + "OverlayNoArgs" + ")");
    return true;
  }

  public void endVisit(Overlay node)
  { 
    unimplementedVisitor("endVisit(" + "Overlay" + ")");
  }

  public boolean visit(Overlay node)
  { 
    unimplementedVisitor("visit(" + "Overlay" + ")");
    return true;
  }

  public void endVisit(RDefNoArgs node)
  { 
    unimplementedVisitor("endVisit(" + "RDefNoArgs" + ")");
  }

  public boolean visit(RDefNoArgs node)
  { 
    unimplementedVisitor("visit(" + "RDefNoArgs" + ")");
    return true;
  }

  public void endVisit(RDef node)
  { 
    unimplementedVisitor("endVisit(" + "RDef" + ")");
  }

  public boolean visit(RDef node)
  { 
    unimplementedVisitor("visit(" + "RDef" + ")");
    return true;
  }

  public void endVisit(RDefT node)
  { 
    unimplementedVisitor("endVisit(" + "RDefT" + ")");
  }

  public boolean visit(RDefT node)
  { 
    unimplementedVisitor("visit(" + "RDefT" + ")");
    return true;
  }

  public void endVisit(RuleNoCond node)
  { 
    unimplementedVisitor("endVisit(" + "RuleNoCond" + ")");
  }

  public boolean visit(RuleNoCond node)
  { 
    unimplementedVisitor("visit(" + "RuleNoCond" + ")");
    return true;
  }

  public void endVisit(Rule node)
  { 
    unimplementedVisitor("endVisit(" + "Rule" + ")");
  }

  public boolean visit(Rule node)
  { 
    unimplementedVisitor("visit(" + "Rule" + ")");
    return true;
  }

  public void endVisit(Rules node)
  { 
    unimplementedVisitor("endVisit(" + "Rules" + ")");
  }

  public boolean visit(Rules node)
  { 
    unimplementedVisitor("visit(" + "Rules" + ")");
    return true;
  }

  public void endVisit(Overlays node)
  { 
    unimplementedVisitor("endVisit(" + "Overlays" + ")");
  }

  public boolean visit(Overlays node)
  { 
    unimplementedVisitor("visit(" + "Overlays" + ")");
    return true;
  }

  public void endVisit(Def_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "Def_StrategoHost" + ")");
  }

  public boolean visit(Def_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "Def_StrategoHost" + ")");
    return true;
  }

  public void endVisit(DynRuleScope node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleScope" + ")");
  }

  public boolean visit(DynRuleScope node)
  { 
    unimplementedVisitor("visit(" + "DynRuleScope" + ")");
    return true;
  }

  public void endVisit(ScopeLabels_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "ScopeLabels_StrategoHost" + ")");
  }

  public boolean visit(ScopeLabels_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "ScopeLabels_StrategoHost" + ")");
    return true;
  }

  public void endVisit(ScopeLabels node)
  { 
    unimplementedVisitor("endVisit(" + "ScopeLabels" + ")");
  }

  public boolean visit(ScopeLabels node)
  { 
    unimplementedVisitor("visit(" + "ScopeLabels" + ")");
    return true;
  }

  public void endVisit(GenDynRules node)
  { 
    unimplementedVisitor("endVisit(" + "GenDynRules" + ")");
  }

  public boolean visit(GenDynRules node)
  { 
    unimplementedVisitor("visit(" + "GenDynRules" + ")");
    return true;
  }

  public void endVisit(AddScopeLabel node)
  { 
    unimplementedVisitor("endVisit(" + "AddScopeLabel" + ")");
  }

  public boolean visit(AddScopeLabel node)
  { 
    unimplementedVisitor("visit(" + "AddScopeLabel" + ")");
    return true;
  }

  public void endVisit(UndefineDynRule node)
  { 
    unimplementedVisitor("endVisit(" + "UndefineDynRule" + ")");
  }

  public boolean visit(UndefineDynRule node)
  { 
    unimplementedVisitor("visit(" + "UndefineDynRule" + ")");
    return true;
  }

  public void endVisit(SetDynRule node)
  { 
    unimplementedVisitor("endVisit(" + "SetDynRule" + ")");
  }

  public boolean visit(SetDynRule node)
  { 
    unimplementedVisitor("visit(" + "SetDynRule" + ")");
    return true;
  }

  public void endVisit(AddDynRule node)
  { 
    unimplementedVisitor("endVisit(" + "AddDynRule" + ")");
  }

  public boolean visit(AddDynRule node)
  { 
    unimplementedVisitor("visit(" + "AddDynRule" + ")");
    return true;
  }

  public void endVisit(SetDynRuleMatch node)
  { 
    unimplementedVisitor("endVisit(" + "SetDynRuleMatch" + ")");
  }

  public boolean visit(SetDynRuleMatch node)
  { 
    unimplementedVisitor("visit(" + "SetDynRuleMatch" + ")");
    return true;
  }

  public void endVisit(DynRuleAssign node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleAssign" + ")");
  }

  public boolean visit(DynRuleAssign node)
  { 
    unimplementedVisitor("visit(" + "DynRuleAssign" + ")");
    return true;
  }

  public void endVisit(DynRuleAssignAdd node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleAssignAdd" + ")");
  }

  public boolean visit(DynRuleAssignAdd node)
  { 
    unimplementedVisitor("visit(" + "DynRuleAssignAdd" + ")");
    return true;
  }

  public void endVisit(SetDynRuleDepends node)
  { 
    unimplementedVisitor("endVisit(" + "SetDynRuleDepends" + ")");
  }

  public boolean visit(SetDynRuleDepends node)
  { 
    unimplementedVisitor("visit(" + "SetDynRuleDepends" + ")");
    return true;
  }

  public void endVisit(LabeledDynRuleId node)
  { 
    unimplementedVisitor("endVisit(" + "LabeledDynRuleId" + ")");
  }

  public boolean visit(LabeledDynRuleId node)
  { 
    unimplementedVisitor("visit(" + "LabeledDynRuleId" + ")");
    return true;
  }

  public void endVisit(AddLabelDynRuleId node)
  { 
    unimplementedVisitor("endVisit(" + "AddLabelDynRuleId" + ")");
  }

  public boolean visit(AddLabelDynRuleId node)
  { 
    unimplementedVisitor("visit(" + "AddLabelDynRuleId" + ")");
    return true;
  }

  public void endVisit(DynRuleId node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleId" + ")");
  }

  public boolean visit(DynRuleId node)
  { 
    unimplementedVisitor("visit(" + "DynRuleId" + ")");
    return true;
  }

  public void endVisit(LabeledDynRuleScopeId node)
  { 
    unimplementedVisitor("endVisit(" + "LabeledDynRuleScopeId" + ")");
  }

  public boolean visit(LabeledDynRuleScopeId node)
  { 
    unimplementedVisitor("visit(" + "LabeledDynRuleScopeId" + ")");
    return true;
  }

  public void endVisit(DynRuleScopeId node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleScopeId" + ")");
  }

  public boolean visit(DynRuleScopeId node)
  { 
    unimplementedVisitor("visit(" + "DynRuleScopeId" + ")");
    return true;
  }

  public void endVisit(RDecNoArgs node)
  { 
    unimplementedVisitor("endVisit(" + "RDecNoArgs" + ")");
  }

  public boolean visit(RDecNoArgs node)
  { 
    unimplementedVisitor("visit(" + "RDecNoArgs" + ")");
    return true;
  }

  public void endVisit(RDec node)
  { 
    unimplementedVisitor("endVisit(" + "RDec" + ")");
  }

  public boolean visit(RDec node)
  { 
    unimplementedVisitor("visit(" + "RDec" + ")");
    return true;
  }

  public void endVisit(RDecT node)
  { 
    unimplementedVisitor("endVisit(" + "RDecT" + ")");
  }

  public boolean visit(RDecT node)
  { 
    unimplementedVisitor("visit(" + "RDecT" + ")");
    return true;
  }

  public void endVisit(RuleNames_StrategoHost node)
  { 
    unimplementedVisitor("endVisit(" + "RuleNames_StrategoHost" + ")");
  }

  public boolean visit(RuleNames_StrategoHost node)
  { 
    unimplementedVisitor("visit(" + "RuleNames_StrategoHost" + ")");
    return true;
  }

  public void endVisit(RuleNames node)
  { 
    unimplementedVisitor("endVisit(" + "RuleNames" + ")");
  }

  public boolean visit(RuleNames node)
  { 
    unimplementedVisitor("visit(" + "RuleNames" + ")");
    return true;
  }

  public void endVisit(DynRuleIntersectFix node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleIntersectFix" + ")");
  }

  public boolean visit(DynRuleIntersectFix node)
  { 
    unimplementedVisitor("visit(" + "DynRuleIntersectFix" + ")");
    return true;
  }

  public void endVisit(DynRuleUnionFix0 node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleUnionFix0" + ")");
  }

  public boolean visit(DynRuleUnionFix0 node)
  { 
    unimplementedVisitor("visit(" + "DynRuleUnionFix0" + ")");
    return true;
  }

  public void endVisit(DynRuleUnionFix node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleUnionFix" + ")");
  }

  public boolean visit(DynRuleUnionFix node)
  { 
    unimplementedVisitor("visit(" + "DynRuleUnionFix" + ")");
    return true;
  }

  public void endVisit(DynRuleIntersectUnionFix0 node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleIntersectUnionFix0" + ")");
  }

  public boolean visit(DynRuleIntersectUnionFix0 node)
  { 
    unimplementedVisitor("visit(" + "DynRuleIntersectUnionFix0" + ")");
    return true;
  }

  public void endVisit(DynRuleIntersectUnionFix node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleIntersectUnionFix" + ")");
  }

  public boolean visit(DynRuleIntersectUnionFix node)
  { 
    unimplementedVisitor("visit(" + "DynRuleIntersectUnionFix" + ")");
    return true;
  }

  public void endVisit(DynRuleIntersect node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleIntersect" + ")");
  }

  public boolean visit(DynRuleIntersect node)
  { 
    unimplementedVisitor("visit(" + "DynRuleIntersect" + ")");
    return true;
  }

  public void endVisit(DynRuleUnion node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleUnion" + ")");
  }

  public boolean visit(DynRuleUnion node)
  { 
    unimplementedVisitor("visit(" + "DynRuleUnion" + ")");
    return true;
  }

  public void endVisit(DynRuleIntersectUnion node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleIntersectUnion" + ")");
  }

  public boolean visit(DynRuleIntersectUnion node)
  { 
    unimplementedVisitor("visit(" + "DynRuleIntersectUnion" + ")");
    return true;
  }

  public void endVisit(UnicodeEscape0 node)
  { 
    unimplementedVisitor("endVisit(" + "UnicodeEscape0" + ")");
  }

  public boolean visit(UnicodeEscape0 node)
  { 
    unimplementedVisitor("visit(" + "UnicodeEscape0" + ")");
    return true;
  }

  public void endVisit(LineTerminator node)
  { 
    unimplementedVisitor("endVisit(" + "LineTerminator" + ")");
  }

  public boolean visit(LineTerminator node)
  { 
    unimplementedVisitor("visit(" + "LineTerminator" + ")");
    return true;
  }

  public void endVisit(CarriageReturn node)
  { 
    unimplementedVisitor("endVisit(" + "CarriageReturn" + ")");
  }

  public boolean visit(CarriageReturn node)
  { 
    unimplementedVisitor("visit(" + "CarriageReturn" + ")");
    return true;
  }

  public void endVisit(EndOfFile node)
  { 
    unimplementedVisitor("endVisit(" + "EndOfFile" + ")");
  }

  public boolean visit(EndOfFile node)
  { 
    unimplementedVisitor("visit(" + "EndOfFile" + ")");
    return true;
  }

  public void endVisit(Comment node)
  { 
    unimplementedVisitor("endVisit(" + "Comment" + ")");
  }

  public boolean visit(Comment node)
  { 
    unimplementedVisitor("visit(" + "Comment" + ")");
    return true;
  }

  public void endVisit(EOLCommentChars node)
  { 
    unimplementedVisitor("endVisit(" + "EOLCommentChars" + ")");
  }

  public boolean visit(EOLCommentChars node)
  { 
    unimplementedVisitor("visit(" + "EOLCommentChars" + ")");
    return true;
  }

  public void endVisit(CommentPart node)
  { 
    unimplementedVisitor("endVisit(" + "CommentPart" + ")");
  }

  public boolean visit(CommentPart node)
  { 
    unimplementedVisitor("visit(" + "CommentPart" + ")");
    return true;
  }

  public void endVisit(BlockCommentChars node)
  { 
    unimplementedVisitor("endVisit(" + "BlockCommentChars" + ")");
  }

  public boolean visit(BlockCommentChars node)
  { 
    unimplementedVisitor("visit(" + "BlockCommentChars" + ")");
    return true;
  }

  public void endVisit(EscEscChar node)
  { 
    unimplementedVisitor("endVisit(" + "EscEscChar" + ")");
  }

  public boolean visit(EscEscChar node)
  { 
    unimplementedVisitor("visit(" + "EscEscChar" + ")");
    return true;
  }

  public void endVisit(EscChar node)
  { 
    unimplementedVisitor("endVisit(" + "EscChar" + ")");
  }

  public boolean visit(EscChar node)
  { 
    unimplementedVisitor("visit(" + "EscChar" + ")");
    return true;
  }

  public void endVisit(UnicodeEscape node)
  { 
    unimplementedVisitor("endVisit(" + "UnicodeEscape" + ")");
  }

  public boolean visit(UnicodeEscape node)
  { 
    unimplementedVisitor("visit(" + "UnicodeEscape" + ")");
    return true;
  }

  public void endVisit(Keyword_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "Keyword_JavaObject" + ")");
  }

  public boolean visit(Keyword_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "Keyword_JavaObject" + ")");
    return true;
  }

  public void endVisit(ID_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ID_JavaObject" + ")");
  }

  public boolean visit(ID_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ID_JavaObject" + ")");
    return true;
  }

  public void endVisit(Id node)
  { 
    unimplementedVisitor("endVisit(" + "Id" + ")");
  }

  public boolean visit(Id node)
  { 
    unimplementedVisitor("visit(" + "Id" + ")");
    return true;
  }

  public void endVisit(Public node)
  { 
    unimplementedVisitor("endVisit(" + "Public" + ")");
  }

  public boolean visit(Public node)
  { 
    unimplementedVisitor("visit(" + "Public" + ")");
    return true;
  }

  public void endVisit(Private node)
  { 
    unimplementedVisitor("endVisit(" + "Private" + ")");
  }

  public boolean visit(Private node)
  { 
    unimplementedVisitor("visit(" + "Private" + ")");
    return true;
  }

  public void endVisit(Protected node)
  { 
    unimplementedVisitor("endVisit(" + "Protected" + ")");
  }

  public boolean visit(Protected node)
  { 
    unimplementedVisitor("visit(" + "Protected" + ")");
    return true;
  }

  public void endVisit(Abstract node)
  { 
    unimplementedVisitor("endVisit(" + "Abstract" + ")");
  }

  public boolean visit(Abstract node)
  { 
    unimplementedVisitor("visit(" + "Abstract" + ")");
    return true;
  }

  public void endVisit(Final node)
  { 
    unimplementedVisitor("endVisit(" + "Final" + ")");
  }

  public boolean visit(Final node)
  { 
    unimplementedVisitor("visit(" + "Final" + ")");
    return true;
  }

  public void endVisit(Static node)
  { 
    unimplementedVisitor("endVisit(" + "Static" + ")");
  }

  public boolean visit(Static node)
  { 
    unimplementedVisitor("visit(" + "Static" + ")");
    return true;
  }

  public void endVisit(Native node)
  { 
    unimplementedVisitor("endVisit(" + "Native" + ")");
  }

  public boolean visit(Native node)
  { 
    unimplementedVisitor("visit(" + "Native" + ")");
    return true;
  }

  public void endVisit(Transient node)
  { 
    unimplementedVisitor("endVisit(" + "Transient" + ")");
  }

  public boolean visit(Transient node)
  { 
    unimplementedVisitor("visit(" + "Transient" + ")");
    return true;
  }

  public void endVisit(Volatile node)
  { 
    unimplementedVisitor("endVisit(" + "Volatile" + ")");
  }

  public boolean visit(Volatile node)
  { 
    unimplementedVisitor("visit(" + "Volatile" + ")");
    return true;
  }

  public void endVisit(Synchronized0 node)
  { 
    unimplementedVisitor("endVisit(" + "Synchronized0" + ")");
  }

  public boolean visit(Synchronized0 node)
  { 
    unimplementedVisitor("visit(" + "Synchronized0" + ")");
    return true;
  }

  public void endVisit(StrictFP node)
  { 
    unimplementedVisitor("endVisit(" + "StrictFP" + ")");
  }

  public boolean visit(StrictFP node)
  { 
    unimplementedVisitor("visit(" + "StrictFP" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject9 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject9" + ")");
  }

  public boolean visit(Modifier_JavaObject9 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject9" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject8 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject8" + ")");
  }

  public boolean visit(Modifier_JavaObject8 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject8" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject7 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject7" + ")");
  }

  public boolean visit(Modifier_JavaObject7 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject7" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject6 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject6" + ")");
  }

  public boolean visit(Modifier_JavaObject6 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject6" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject5 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject5" + ")");
  }

  public boolean visit(Modifier_JavaObject5 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject5" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject4 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject4" + ")");
  }

  public boolean visit(Modifier_JavaObject4 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject4" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject3 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject3" + ")");
  }

  public boolean visit(Modifier_JavaObject3 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject3" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject2" + ")");
  }

  public boolean visit(Modifier_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject2" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject1" + ")");
  }

  public boolean visit(Modifier_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject1" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject0" + ")");
  }

  public boolean visit(Modifier_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObject" + ")");
  }

  public boolean visit(Modifier_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObject" + ")");
    return true;
  }

  public void endVisit(DeciLiteral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "DeciLiteral_JavaObject" + ")");
  }

  public boolean visit(DeciLiteral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "DeciLiteral_JavaObject" + ")");
    return true;
  }

  public void endVisit(HexaLiteral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "HexaLiteral_JavaObject" + ")");
  }

  public boolean visit(HexaLiteral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "HexaLiteral_JavaObject" + ")");
    return true;
  }

  public void endVisit(OctaLiteral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "OctaLiteral_JavaObject" + ")");
  }

  public boolean visit(OctaLiteral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "OctaLiteral_JavaObject" + ")");
    return true;
  }

  public void endVisit(DeciNumeral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "DeciNumeral_JavaObject" + ")");
  }

  public boolean visit(DeciNumeral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "DeciNumeral_JavaObject" + ")");
    return true;
  }

  public void endVisit(HexaNumeral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "HexaNumeral_JavaObject" + ")");
  }

  public boolean visit(HexaNumeral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "HexaNumeral_JavaObject" + ")");
    return true;
  }

  public void endVisit(OctaNumeral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "OctaNumeral_JavaObject" + ")");
  }

  public boolean visit(OctaNumeral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "OctaNumeral_JavaObject" + ")");
    return true;
  }

  public void endVisit(Deci node)
  { 
    unimplementedVisitor("endVisit(" + "Deci" + ")");
  }

  public boolean visit(Deci node)
  { 
    unimplementedVisitor("visit(" + "Deci" + ")");
    return true;
  }

  public void endVisit(Hexa node)
  { 
    unimplementedVisitor("endVisit(" + "Hexa" + ")");
  }

  public boolean visit(Hexa node)
  { 
    unimplementedVisitor("visit(" + "Hexa" + ")");
    return true;
  }

  public void endVisit(Octa node)
  { 
    unimplementedVisitor("endVisit(" + "Octa" + ")");
  }

  public boolean visit(Octa node)
  { 
    unimplementedVisitor("visit(" + "Octa" + ")");
    return true;
  }

  public void endVisit(DeciFloatLiteral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "DeciFloatLiteral_JavaObject" + ")");
  }

  public boolean visit(DeciFloatLiteral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "DeciFloatLiteral_JavaObject" + ")");
    return true;
  }

  public void endVisit(HexaFloatLiteral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "HexaFloatLiteral_JavaObject" + ")");
  }

  public boolean visit(HexaFloatLiteral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "HexaFloatLiteral_JavaObject" + ")");
    return true;
  }

  public void endVisit(Float1 node)
  { 
    unimplementedVisitor("endVisit(" + "Float1" + ")");
  }

  public boolean visit(Float1 node)
  { 
    unimplementedVisitor("visit(" + "Float1" + ")");
    return true;
  }

  public void endVisit(Float0 node)
  { 
    unimplementedVisitor("endVisit(" + "Float0" + ")");
  }

  public boolean visit(Float0 node)
  { 
    unimplementedVisitor("visit(" + "Float0" + ")");
    return true;
  }

  public void endVisit(DeciFloatNumeral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "DeciFloatNumeral_JavaObject" + ")");
  }

  public boolean visit(DeciFloatNumeral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "DeciFloatNumeral_JavaObject" + ")");
    return true;
  }

  public void endVisit(DeciFloatDigits_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "DeciFloatDigits_JavaObject" + ")");
  }

  public boolean visit(DeciFloatDigits_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "DeciFloatDigits_JavaObject" + ")");
    return true;
  }

  public void endVisit(DeciFloatExponentPart_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "DeciFloatExponentPart_JavaObject" + ")");
  }

  public boolean visit(DeciFloatExponentPart_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "DeciFloatExponentPart_JavaObject" + ")");
    return true;
  }

  public void endVisit(SignedInteger_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "SignedInteger_JavaObject" + ")");
  }

  public boolean visit(SignedInteger_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "SignedInteger_JavaObject" + ")");
    return true;
  }

  public void endVisit(HexaFloatNumeral_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "HexaFloatNumeral_JavaObject" + ")");
  }

  public boolean visit(HexaFloatNumeral_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "HexaFloatNumeral_JavaObject" + ")");
    return true;
  }

  public void endVisit(HexaSignificand_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "HexaSignificand_JavaObject" + ")");
  }

  public boolean visit(HexaSignificand_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "HexaSignificand_JavaObject" + ")");
    return true;
  }

  public void endVisit(BinaryExponent_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "BinaryExponent_JavaObject" + ")");
  }

  public boolean visit(BinaryExponent_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "BinaryExponent_JavaObject" + ")");
    return true;
  }

  public void endVisit(Bool node)
  { 
    unimplementedVisitor("endVisit(" + "Bool" + ")");
  }

  public boolean visit(Bool node)
  { 
    unimplementedVisitor("visit(" + "Bool" + ")");
    return true;
  }

  public void endVisit(True node)
  { 
    unimplementedVisitor("endVisit(" + "True" + ")");
  }

  public boolean visit(True node)
  { 
    unimplementedVisitor("visit(" + "True" + ")");
    return true;
  }

  public void endVisit(False node)
  { 
    unimplementedVisitor("endVisit(" + "False" + ")");
  }

  public boolean visit(False node)
  { 
    unimplementedVisitor("visit(" + "False" + ")");
    return true;
  }

  public void endVisit(EscapeSeq_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "EscapeSeq_JavaObject0" + ")");
  }

  public boolean visit(EscapeSeq_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "EscapeSeq_JavaObject0" + ")");
    return true;
  }

  public void endVisit(EscapeSeq_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "EscapeSeq_JavaObject" + ")");
  }

  public boolean visit(EscapeSeq_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "EscapeSeq_JavaObject" + ")");
    return true;
  }

  public void endVisit(NamedEscape node)
  { 
    unimplementedVisitor("endVisit(" + "NamedEscape" + ")");
  }

  public boolean visit(NamedEscape node)
  { 
    unimplementedVisitor("visit(" + "NamedEscape" + ")");
    return true;
  }

  public void endVisit(OctaEscape1 node)
  { 
    unimplementedVisitor("endVisit(" + "OctaEscape1" + ")");
  }

  public boolean visit(OctaEscape1 node)
  { 
    unimplementedVisitor("visit(" + "OctaEscape1" + ")");
    return true;
  }

  public void endVisit(OctaEscape20 node)
  { 
    unimplementedVisitor("endVisit(" + "OctaEscape20" + ")");
  }

  public boolean visit(OctaEscape20 node)
  { 
    unimplementedVisitor("visit(" + "OctaEscape20" + ")");
    return true;
  }

  public void endVisit(OctaEscape2 node)
  { 
    unimplementedVisitor("endVisit(" + "OctaEscape2" + ")");
  }

  public boolean visit(OctaEscape2 node)
  { 
    unimplementedVisitor("visit(" + "OctaEscape2" + ")");
    return true;
  }

  public void endVisit(OctaEscape3 node)
  { 
    unimplementedVisitor("endVisit(" + "OctaEscape3" + ")");
  }

  public boolean visit(OctaEscape3 node)
  { 
    unimplementedVisitor("visit(" + "OctaEscape3" + ")");
    return true;
  }

  public void endVisit(LastOcta_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "LastOcta_JavaObject" + ")");
  }

  public boolean visit(LastOcta_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "LastOcta_JavaObject" + ")");
    return true;
  }

  public void endVisit(Char0 node)
  { 
    unimplementedVisitor("endVisit(" + "Char0" + ")");
  }

  public boolean visit(Char0 node)
  { 
    unimplementedVisitor("visit(" + "Char0" + ")");
    return true;
  }

  public void endVisit(Single node)
  { 
    unimplementedVisitor("endVisit(" + "Single" + ")");
  }

  public boolean visit(Single node)
  { 
    unimplementedVisitor("visit(" + "Single" + ")");
    return true;
  }

  public void endVisit(CharContent_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "CharContent_JavaObject0" + ")");
  }

  public boolean visit(CharContent_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "CharContent_JavaObject0" + ")");
    return true;
  }

  public void endVisit(CharContent_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "CharContent_JavaObject" + ")");
  }

  public boolean visit(CharContent_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "CharContent_JavaObject" + ")");
    return true;
  }

  public void endVisit(SingleChar_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "SingleChar_JavaObject" + ")");
  }

  public boolean visit(SingleChar_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "SingleChar_JavaObject" + ")");
    return true;
  }

  public void endVisit(String node)
  { 
    unimplementedVisitor("endVisit(" + "String" + ")");
  }

  public boolean visit(String node)
  { 
    unimplementedVisitor("visit(" + "String" + ")");
    return true;
  }

  public void endVisit(Chars node)
  { 
    unimplementedVisitor("endVisit(" + "Chars" + ")");
  }

  public boolean visit(Chars node)
  { 
    unimplementedVisitor("visit(" + "Chars" + ")");
    return true;
  }

  public void endVisit(StringPart_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "StringPart_JavaObject0" + ")");
  }

  public boolean visit(StringPart_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "StringPart_JavaObject0" + ")");
    return true;
  }

  public void endVisit(StringPart_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "StringPart_JavaObject" + ")");
  }

  public boolean visit(StringPart_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "StringPart_JavaObject" + ")");
    return true;
  }

  public void endVisit(StringChars_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "StringChars_JavaObject" + ")");
  }

  public boolean visit(StringChars_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "StringChars_JavaObject" + ")");
    return true;
  }

  public void endVisit(Null node)
  { 
    unimplementedVisitor("endVisit(" + "Null" + ")");
  }

  public boolean visit(Null node)
  { 
    unimplementedVisitor("visit(" + "Null" + ")");
    return true;
  }

  public void endVisit(PrimType_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "PrimType_JavaObject" + ")");
  }

  public boolean visit(PrimType_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "PrimType_JavaObject" + ")");
    return true;
  }

  public void endVisit(Boolean node)
  { 
    unimplementedVisitor("endVisit(" + "Boolean" + ")");
  }

  public boolean visit(Boolean node)
  { 
    unimplementedVisitor("visit(" + "Boolean" + ")");
    return true;
  }

  public void endVisit(NumType_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "NumType_JavaObject0" + ")");
  }

  public boolean visit(NumType_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "NumType_JavaObject0" + ")");
    return true;
  }

  public void endVisit(NumType_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "NumType_JavaObject" + ")");
  }

  public boolean visit(NumType_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "NumType_JavaObject" + ")");
    return true;
  }

  public void endVisit(Byte node)
  { 
    unimplementedVisitor("endVisit(" + "Byte" + ")");
  }

  public boolean visit(Byte node)
  { 
    unimplementedVisitor("visit(" + "Byte" + ")");
    return true;
  }

  public void endVisit(Short node)
  { 
    unimplementedVisitor("endVisit(" + "Short" + ")");
  }

  public boolean visit(Short node)
  { 
    unimplementedVisitor("visit(" + "Short" + ")");
    return true;
  }

  public void endVisit(Int node)
  { 
    unimplementedVisitor("endVisit(" + "Int" + ")");
  }

  public boolean visit(Int node)
  { 
    unimplementedVisitor("visit(" + "Int" + ")");
    return true;
  }

  public void endVisit(Long node)
  { 
    unimplementedVisitor("endVisit(" + "Long" + ")");
  }

  public boolean visit(Long node)
  { 
    unimplementedVisitor("visit(" + "Long" + ")");
    return true;
  }

  public void endVisit(Char node)
  { 
    unimplementedVisitor("endVisit(" + "Char" + ")");
  }

  public boolean visit(Char node)
  { 
    unimplementedVisitor("visit(" + "Char" + ")");
    return true;
  }

  public void endVisit(Float node)
  { 
    unimplementedVisitor("endVisit(" + "Float" + ")");
  }

  public boolean visit(Float node)
  { 
    unimplementedVisitor("visit(" + "Float" + ")");
    return true;
  }

  public void endVisit(Double node)
  { 
    unimplementedVisitor("endVisit(" + "Double" + ")");
  }

  public boolean visit(Double node)
  { 
    unimplementedVisitor("visit(" + "Double" + ")");
    return true;
  }

  public void endVisit(PackageName node)
  { 
    unimplementedVisitor("endVisit(" + "PackageName" + ")");
  }

  public boolean visit(PackageName node)
  { 
    unimplementedVisitor("visit(" + "PackageName" + ")");
    return true;
  }

  public void endVisit(AmbName0 node)
  { 
    unimplementedVisitor("endVisit(" + "AmbName0" + ")");
  }

  public boolean visit(AmbName0 node)
  { 
    unimplementedVisitor("visit(" + "AmbName0" + ")");
    return true;
  }

  public void endVisit(AmbName node)
  { 
    unimplementedVisitor("endVisit(" + "AmbName" + ")");
  }

  public boolean visit(AmbName node)
  { 
    unimplementedVisitor("visit(" + "AmbName" + ")");
    return true;
  }

  public void endVisit(TypeName0 node)
  { 
    unimplementedVisitor("endVisit(" + "TypeName0" + ")");
  }

  public boolean visit(TypeName0 node)
  { 
    unimplementedVisitor("visit(" + "TypeName0" + ")");
    return true;
  }

  public void endVisit(TypeName node)
  { 
    unimplementedVisitor("endVisit(" + "TypeName" + ")");
  }

  public boolean visit(TypeName node)
  { 
    unimplementedVisitor("visit(" + "TypeName" + ")");
    return true;
  }

  public void endVisit(ExprName0 node)
  { 
    unimplementedVisitor("endVisit(" + "ExprName0" + ")");
  }

  public boolean visit(ExprName0 node)
  { 
    unimplementedVisitor("visit(" + "ExprName0" + ")");
    return true;
  }

  public void endVisit(ExprName node)
  { 
    unimplementedVisitor("endVisit(" + "ExprName" + ")");
  }

  public boolean visit(ExprName node)
  { 
    unimplementedVisitor("visit(" + "ExprName" + ")");
    return true;
  }

  public void endVisit(MethodName0 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodName0" + ")");
  }

  public boolean visit(MethodName0 node)
  { 
    unimplementedVisitor("visit(" + "MethodName0" + ")");
    return true;
  }

  public void endVisit(MethodName node)
  { 
    unimplementedVisitor("endVisit(" + "MethodName" + ")");
  }

  public boolean visit(MethodName node)
  { 
    unimplementedVisitor("visit(" + "MethodName" + ")");
    return true;
  }

  public void endVisit(PackageOrTypeName0 node)
  { 
    unimplementedVisitor("endVisit(" + "PackageOrTypeName0" + ")");
  }

  public boolean visit(PackageOrTypeName0 node)
  { 
    unimplementedVisitor("visit(" + "PackageOrTypeName0" + ")");
    return true;
  }

  public void endVisit(PackageOrTypeName node)
  { 
    unimplementedVisitor("endVisit(" + "PackageOrTypeName" + ")");
  }

  public boolean visit(PackageOrTypeName node)
  { 
    unimplementedVisitor("visit(" + "PackageOrTypeName" + ")");
    return true;
  }

  public void endVisit(TypeArgs node)
  { 
    unimplementedVisitor("endVisit(" + "TypeArgs" + ")");
  }

  public boolean visit(TypeArgs node)
  { 
    unimplementedVisitor("visit(" + "TypeArgs" + ")");
    return true;
  }

  public void endVisit(ActualTypeArg_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ActualTypeArg_JavaObject" + ")");
  }

  public boolean visit(ActualTypeArg_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ActualTypeArg_JavaObject" + ")");
    return true;
  }

  public void endVisit(Wildcard node)
  { 
    unimplementedVisitor("endVisit(" + "Wildcard" + ")");
  }

  public boolean visit(Wildcard node)
  { 
    unimplementedVisitor("visit(" + "Wildcard" + ")");
    return true;
  }

  public void endVisit(WildcardUpperBound node)
  { 
    unimplementedVisitor("endVisit(" + "WildcardUpperBound" + ")");
  }

  public boolean visit(WildcardUpperBound node)
  { 
    unimplementedVisitor("visit(" + "WildcardUpperBound" + ")");
    return true;
  }

  public void endVisit(WildcardLowerBound node)
  { 
    unimplementedVisitor("endVisit(" + "WildcardLowerBound" + ")");
  }

  public boolean visit(WildcardLowerBound node)
  { 
    unimplementedVisitor("visit(" + "WildcardLowerBound" + ")");
    return true;
  }

  public void endVisit(TypeParam node)
  { 
    unimplementedVisitor("endVisit(" + "TypeParam" + ")");
  }

  public boolean visit(TypeParam node)
  { 
    unimplementedVisitor("visit(" + "TypeParam" + ")");
    return true;
  }

  public void endVisit(TypeBound node)
  { 
    unimplementedVisitor("endVisit(" + "TypeBound" + ")");
  }

  public boolean visit(TypeBound node)
  { 
    unimplementedVisitor("visit(" + "TypeBound" + ")");
    return true;
  }

  public void endVisit(TypeParams node)
  { 
    unimplementedVisitor("endVisit(" + "TypeParams" + ")");
  }

  public boolean visit(TypeParams node)
  { 
    unimplementedVisitor("visit(" + "TypeParams" + ")");
    return true;
  }

  public void endVisit(TypeVarId_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "TypeVarId_JavaObject" + ")");
  }

  public boolean visit(TypeVarId_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "TypeVarId_JavaObject" + ")");
    return true;
  }

  public void endVisit(RefType_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "RefType_JavaObject0" + ")");
  }

  public boolean visit(RefType_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "RefType_JavaObject0" + ")");
    return true;
  }

  public void endVisit(RefType_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "RefType_JavaObject" + ")");
  }

  public boolean visit(RefType_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "RefType_JavaObject" + ")");
    return true;
  }

  public void endVisit(ClassOrInterfaceType node)
  { 
    unimplementedVisitor("endVisit(" + "ClassOrInterfaceType" + ")");
  }

  public boolean visit(ClassOrInterfaceType node)
  { 
    unimplementedVisitor("visit(" + "ClassOrInterfaceType" + ")");
    return true;
  }

  public void endVisit(ClassType node)
  { 
    unimplementedVisitor("endVisit(" + "ClassType" + ")");
  }

  public boolean visit(ClassType node)
  { 
    unimplementedVisitor("visit(" + "ClassType" + ")");
    return true;
  }

  public void endVisit(InterfaceType node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceType" + ")");
  }

  public boolean visit(InterfaceType node)
  { 
    unimplementedVisitor("visit(" + "InterfaceType" + ")");
    return true;
  }

  public void endVisit(TypeDecSpec_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "TypeDecSpec_JavaObject" + ")");
  }

  public boolean visit(TypeDecSpec_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "TypeDecSpec_JavaObject" + ")");
    return true;
  }

  public void endVisit(Member node)
  { 
    unimplementedVisitor("endVisit(" + "Member" + ")");
  }

  public boolean visit(Member node)
  { 
    unimplementedVisitor("visit(" + "Member" + ")");
    return true;
  }

  public void endVisit(TypeVar node)
  { 
    unimplementedVisitor("endVisit(" + "TypeVar" + ")");
  }

  public boolean visit(TypeVar node)
  { 
    unimplementedVisitor("visit(" + "TypeVar" + ")");
    return true;
  }

  public void endVisit(ArrayType node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayType" + ")");
  }

  public boolean visit(ArrayType node)
  { 
    unimplementedVisitor("visit(" + "ArrayType" + ")");
    return true;
  }

  public void endVisit(Type_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Type_JavaObject0" + ")");
  }

  public boolean visit(Type_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Type_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Type_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "Type_JavaObject" + ")");
  }

  public boolean visit(Type_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "Type_JavaObject" + ")");
    return true;
  }

  public void endVisit(Lit node)
  { 
    unimplementedVisitor("endVisit(" + "Lit" + ")");
  }

  public boolean visit(Lit node)
  { 
    unimplementedVisitor("visit(" + "Lit" + ")");
    return true;
  }

  public void endVisit(Literal_JavaObject5 node)
  { 
    unimplementedVisitor("endVisit(" + "Literal_JavaObject5" + ")");
  }

  public boolean visit(Literal_JavaObject5 node)
  { 
    unimplementedVisitor("visit(" + "Literal_JavaObject5" + ")");
    return true;
  }

  public void endVisit(Literal_JavaObject4 node)
  { 
    unimplementedVisitor("endVisit(" + "Literal_JavaObject4" + ")");
  }

  public boolean visit(Literal_JavaObject4 node)
  { 
    unimplementedVisitor("visit(" + "Literal_JavaObject4" + ")");
    return true;
  }

  public void endVisit(Literal_JavaObject3 node)
  { 
    unimplementedVisitor("endVisit(" + "Literal_JavaObject3" + ")");
  }

  public boolean visit(Literal_JavaObject3 node)
  { 
    unimplementedVisitor("visit(" + "Literal_JavaObject3" + ")");
    return true;
  }

  public void endVisit(Literal_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "Literal_JavaObject2" + ")");
  }

  public boolean visit(Literal_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "Literal_JavaObject2" + ")");
    return true;
  }

  public void endVisit(Literal_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "Literal_JavaObject1" + ")");
  }

  public boolean visit(Literal_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "Literal_JavaObject1" + ")");
    return true;
  }

  public void endVisit(Literal_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Literal_JavaObject0" + ")");
  }

  public boolean visit(Literal_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Literal_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Literal_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "Literal_JavaObject" + ")");
  }

  public boolean visit(Literal_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "Literal_JavaObject" + ")");
    return true;
  }

  public void endVisit(Class node)
  { 
    unimplementedVisitor("endVisit(" + "Class" + ")");
  }

  public boolean visit(Class node)
  { 
    unimplementedVisitor("visit(" + "Class" + ")");
    return true;
  }

  public void endVisit(VoidClass node)
  { 
    unimplementedVisitor("endVisit(" + "VoidClass" + ")");
  }

  public boolean visit(VoidClass node)
  { 
    unimplementedVisitor("visit(" + "VoidClass" + ")");
    return true;
  }

  public void endVisit(This node)
  { 
    unimplementedVisitor("endVisit(" + "This" + ")");
  }

  public boolean visit(This node)
  { 
    unimplementedVisitor("visit(" + "This" + ")");
    return true;
  }

  public void endVisit(QThis node)
  { 
    unimplementedVisitor("endVisit(" + "QThis" + ")");
  }

  public boolean visit(QThis node)
  { 
    unimplementedVisitor("visit(" + "QThis" + ")");
    return true;
  }

  public void endVisit(Expr node)
  { 
    unimplementedVisitor("endVisit(" + "Expr" + ")");
  }

  public boolean visit(Expr node)
  { 
    unimplementedVisitor("visit(" + "Expr" + ")");
    return true;
  }

  public void endVisit(ArrayInit0 node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayInit0" + ")");
  }

  public boolean visit(ArrayInit0 node)
  { 
    unimplementedVisitor("visit(" + "ArrayInit0" + ")");
    return true;
  }

  public void endVisit(ArrayInit node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayInit" + ")");
  }

  public boolean visit(ArrayInit node)
  { 
    unimplementedVisitor("visit(" + "ArrayInit" + ")");
    return true;
  }

  public void endVisit(FieldDec node)
  { 
    unimplementedVisitor("endVisit(" + "FieldDec" + ")");
  }

  public boolean visit(FieldDec node)
  { 
    unimplementedVisitor("visit(" + "FieldDec" + ")");
    return true;
  }

  public void endVisit(VarDec0 node)
  { 
    unimplementedVisitor("endVisit(" + "VarDec0" + ")");
  }

  public boolean visit(VarDec0 node)
  { 
    unimplementedVisitor("visit(" + "VarDec0" + ")");
    return true;
  }

  public void endVisit(VarDec node)
  { 
    unimplementedVisitor("endVisit(" + "VarDec" + ")");
  }

  public boolean visit(VarDec node)
  { 
    unimplementedVisitor("visit(" + "VarDec" + ")");
    return true;
  }

  public void endVisit(VarDecId_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "VarDecId_JavaObject" + ")");
  }

  public boolean visit(VarDecId_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "VarDecId_JavaObject" + ")");
    return true;
  }

  public void endVisit(ArrayVarDecId node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayVarDecId" + ")");
  }

  public boolean visit(ArrayVarDecId node)
  { 
    unimplementedVisitor("visit(" + "ArrayVarDecId" + ")");
    return true;
  }

  public void endVisit(Dim0 node)
  { 
    unimplementedVisitor("endVisit(" + "Dim0" + ")");
  }

  public boolean visit(Dim0 node)
  { 
    unimplementedVisitor("visit(" + "Dim0" + ")");
    return true;
  }

  public void endVisit(VarInit_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "VarInit_JavaObject0" + ")");
  }

  public boolean visit(VarInit_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "VarInit_JavaObject0" + ")");
    return true;
  }

  public void endVisit(VarInit_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "VarInit_JavaObject" + ")");
  }

  public boolean visit(VarInit_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "VarInit_JavaObject" + ")");
    return true;
  }

  public void endVisit(FieldMod_JavaObject5 node)
  { 
    unimplementedVisitor("endVisit(" + "FieldMod_JavaObject5" + ")");
  }

  public boolean visit(FieldMod_JavaObject5 node)
  { 
    unimplementedVisitor("visit(" + "FieldMod_JavaObject5" + ")");
    return true;
  }

  public void endVisit(FieldMod_JavaObject4 node)
  { 
    unimplementedVisitor("endVisit(" + "FieldMod_JavaObject4" + ")");
  }

  public boolean visit(FieldMod_JavaObject4 node)
  { 
    unimplementedVisitor("visit(" + "FieldMod_JavaObject4" + ")");
    return true;
  }

  public void endVisit(FieldMod_JavaObject3 node)
  { 
    unimplementedVisitor("endVisit(" + "FieldMod_JavaObject3" + ")");
  }

  public boolean visit(FieldMod_JavaObject3 node)
  { 
    unimplementedVisitor("visit(" + "FieldMod_JavaObject3" + ")");
    return true;
  }

  public void endVisit(FieldMod_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "FieldMod_JavaObject2" + ")");
  }

  public boolean visit(FieldMod_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "FieldMod_JavaObject2" + ")");
    return true;
  }

  public void endVisit(FieldMod_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "FieldMod_JavaObject1" + ")");
  }

  public boolean visit(FieldMod_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "FieldMod_JavaObject1" + ")");
    return true;
  }

  public void endVisit(FieldMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "FieldMod_JavaObject0" + ")");
  }

  public boolean visit(FieldMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "FieldMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(FieldMod_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "FieldMod_JavaObject" + ")");
  }

  public boolean visit(FieldMod_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "FieldMod_JavaObject" + ")");
    return true;
  }

  public void endVisit(LocalVarDecStm node)
  { 
    unimplementedVisitor("endVisit(" + "LocalVarDecStm" + ")");
  }

  public boolean visit(LocalVarDecStm node)
  { 
    unimplementedVisitor("visit(" + "LocalVarDecStm" + ")");
    return true;
  }

  public void endVisit(LocalVarDec node)
  { 
    unimplementedVisitor("endVisit(" + "LocalVarDec" + ")");
  }

  public boolean visit(LocalVarDec node)
  { 
    unimplementedVisitor("visit(" + "LocalVarDec" + ")");
    return true;
  }

  public void endVisit(Stm_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "Stm_JavaObject" + ")");
  }

  public boolean visit(Stm_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "Stm_JavaObject" + ")");
    return true;
  }

  public void endVisit(Empty node)
  { 
    unimplementedVisitor("endVisit(" + "Empty" + ")");
  }

  public boolean visit(Empty node)
  { 
    unimplementedVisitor("visit(" + "Empty" + ")");
    return true;
  }

  public void endVisit(Labeled node)
  { 
    unimplementedVisitor("endVisit(" + "Labeled" + ")");
  }

  public boolean visit(Labeled node)
  { 
    unimplementedVisitor("visit(" + "Labeled" + ")");
    return true;
  }

  public void endVisit(ExprStm node)
  { 
    unimplementedVisitor("endVisit(" + "ExprStm" + ")");
  }

  public boolean visit(ExprStm node)
  { 
    unimplementedVisitor("visit(" + "ExprStm" + ")");
    return true;
  }

  public void endVisit(If0 node)
  { 
    unimplementedVisitor("endVisit(" + "If0" + ")");
  }

  public boolean visit(If0 node)
  { 
    unimplementedVisitor("visit(" + "If0" + ")");
    return true;
  }

  public void endVisit(If node)
  { 
    unimplementedVisitor("endVisit(" + "If" + ")");
  }

  public boolean visit(If node)
  { 
    unimplementedVisitor("visit(" + "If" + ")");
    return true;
  }

  public void endVisit(AssertStm0 node)
  { 
    unimplementedVisitor("endVisit(" + "AssertStm0" + ")");
  }

  public boolean visit(AssertStm0 node)
  { 
    unimplementedVisitor("visit(" + "AssertStm0" + ")");
    return true;
  }

  public void endVisit(AssertStm node)
  { 
    unimplementedVisitor("endVisit(" + "AssertStm" + ")");
  }

  public boolean visit(AssertStm node)
  { 
    unimplementedVisitor("visit(" + "AssertStm" + ")");
    return true;
  }

  public void endVisit(Switch node)
  { 
    unimplementedVisitor("endVisit(" + "Switch" + ")");
  }

  public boolean visit(Switch node)
  { 
    unimplementedVisitor("visit(" + "Switch" + ")");
    return true;
  }

  public void endVisit(SwitchBlock node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchBlock" + ")");
  }

  public boolean visit(SwitchBlock node)
  { 
    unimplementedVisitor("visit(" + "SwitchBlock" + ")");
    return true;
  }

  public void endVisit(SwitchGroup node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchGroup" + ")");
  }

  public boolean visit(SwitchGroup node)
  { 
    unimplementedVisitor("visit(" + "SwitchGroup" + ")");
    return true;
  }

  public void endVisit(Case node)
  { 
    unimplementedVisitor("endVisit(" + "Case" + ")");
  }

  public boolean visit(Case node)
  { 
    unimplementedVisitor("visit(" + "Case" + ")");
    return true;
  }

  public void endVisit(Default node)
  { 
    unimplementedVisitor("endVisit(" + "Default" + ")");
  }

  public boolean visit(Default node)
  { 
    unimplementedVisitor("visit(" + "Default" + ")");
    return true;
  }

  public void endVisit(While node)
  { 
    unimplementedVisitor("endVisit(" + "While" + ")");
  }

  public boolean visit(While node)
  { 
    unimplementedVisitor("visit(" + "While" + ")");
    return true;
  }

  public void endVisit(DoWhile node)
  { 
    unimplementedVisitor("endVisit(" + "DoWhile" + ")");
  }

  public boolean visit(DoWhile node)
  { 
    unimplementedVisitor("visit(" + "DoWhile" + ")");
    return true;
  }

  public void endVisit(For0 node)
  { 
    unimplementedVisitor("endVisit(" + "For0" + ")");
  }

  public boolean visit(For0 node)
  { 
    unimplementedVisitor("visit(" + "For0" + ")");
    return true;
  }

  public void endVisit(For node)
  { 
    unimplementedVisitor("endVisit(" + "For" + ")");
  }

  public boolean visit(For node)
  { 
    unimplementedVisitor("visit(" + "For" + ")");
    return true;
  }

  public void endVisit(ForEach node)
  { 
    unimplementedVisitor("endVisit(" + "ForEach" + ")");
  }

  public boolean visit(ForEach node)
  { 
    unimplementedVisitor("visit(" + "ForEach" + ")");
    return true;
  }

  public void endVisit(Break node)
  { 
    unimplementedVisitor("endVisit(" + "Break" + ")");
  }

  public boolean visit(Break node)
  { 
    unimplementedVisitor("visit(" + "Break" + ")");
    return true;
  }

  public void endVisit(Continue node)
  { 
    unimplementedVisitor("endVisit(" + "Continue" + ")");
  }

  public boolean visit(Continue node)
  { 
    unimplementedVisitor("visit(" + "Continue" + ")");
    return true;
  }

  public void endVisit(Return node)
  { 
    unimplementedVisitor("endVisit(" + "Return" + ")");
  }

  public boolean visit(Return node)
  { 
    unimplementedVisitor("visit(" + "Return" + ")");
    return true;
  }

  public void endVisit(Throw node)
  { 
    unimplementedVisitor("endVisit(" + "Throw" + ")");
  }

  public boolean visit(Throw node)
  { 
    unimplementedVisitor("visit(" + "Throw" + ")");
    return true;
  }

  public void endVisit(Synchronized node)
  { 
    unimplementedVisitor("endVisit(" + "Synchronized" + ")");
  }

  public boolean visit(Synchronized node)
  { 
    unimplementedVisitor("visit(" + "Synchronized" + ")");
    return true;
  }

  public void endVisit(Try0 node)
  { 
    unimplementedVisitor("endVisit(" + "Try0" + ")");
  }

  public boolean visit(Try0 node)
  { 
    unimplementedVisitor("visit(" + "Try0" + ")");
    return true;
  }

  public void endVisit(Try node)
  { 
    unimplementedVisitor("endVisit(" + "Try" + ")");
  }

  public boolean visit(Try node)
  { 
    unimplementedVisitor("visit(" + "Try" + ")");
    return true;
  }

  public void endVisit(Catch node)
  { 
    unimplementedVisitor("endVisit(" + "Catch" + ")");
  }

  public boolean visit(Catch node)
  { 
    unimplementedVisitor("visit(" + "Catch" + ")");
    return true;
  }

  public void endVisit(Block node)
  { 
    unimplementedVisitor("endVisit(" + "Block" + ")");
  }

  public boolean visit(Block node)
  { 
    unimplementedVisitor("visit(" + "Block" + ")");
    return true;
  }

  public void endVisit(BlockStm_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "BlockStm_JavaObject0" + ")");
  }

  public boolean visit(BlockStm_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "BlockStm_JavaObject0" + ")");
    return true;
  }

  public void endVisit(ClassDecStm node)
  { 
    unimplementedVisitor("endVisit(" + "ClassDecStm" + ")");
  }

  public boolean visit(ClassDecStm node)
  { 
    unimplementedVisitor("visit(" + "ClassDecStm" + ")");
    return true;
  }

  public void endVisit(BlockStm_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "BlockStm_JavaObject" + ")");
  }

  public boolean visit(BlockStm_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "BlockStm_JavaObject" + ")");
    return true;
  }

  public void endVisit(MethodDec node)
  { 
    unimplementedVisitor("endVisit(" + "MethodDec" + ")");
  }

  public boolean visit(MethodDec node)
  { 
    unimplementedVisitor("visit(" + "MethodDec" + ")");
    return true;
  }

  public void endVisit(MethodDecHead node)
  { 
    unimplementedVisitor("endVisit(" + "MethodDecHead" + ")");
  }

  public boolean visit(MethodDecHead node)
  { 
    unimplementedVisitor("visit(" + "MethodDecHead" + ")");
    return true;
  }

  public void endVisit(DeprMethodDecHead node)
  { 
    unimplementedVisitor("endVisit(" + "DeprMethodDecHead" + ")");
  }

  public boolean visit(DeprMethodDecHead node)
  { 
    unimplementedVisitor("visit(" + "DeprMethodDecHead" + ")");
    return true;
  }

  public void endVisit(ResultType_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ResultType_JavaObject" + ")");
  }

  public boolean visit(ResultType_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ResultType_JavaObject" + ")");
    return true;
  }

  public void endVisit(Void node)
  { 
    unimplementedVisitor("endVisit(" + "Void" + ")");
  }

  public boolean visit(Void node)
  { 
    unimplementedVisitor("visit(" + "Void" + ")");
    return true;
  }

  public void endVisit(Param node)
  { 
    unimplementedVisitor("endVisit(" + "Param" + ")");
  }

  public boolean visit(Param node)
  { 
    unimplementedVisitor("visit(" + "Param" + ")");
    return true;
  }

  public void endVisit(VarArityParam node)
  { 
    unimplementedVisitor("endVisit(" + "VarArityParam" + ")");
  }

  public boolean visit(VarArityParam node)
  { 
    unimplementedVisitor("visit(" + "VarArityParam" + ")");
    return true;
  }

  public void endVisit(VarMod_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "VarMod_JavaObject" + ")");
  }

  public boolean visit(VarMod_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "VarMod_JavaObject" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject7 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject7" + ")");
  }

  public boolean visit(MethodMod_JavaObject7 node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject7" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject6 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject6" + ")");
  }

  public boolean visit(MethodMod_JavaObject6 node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject6" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject5 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject5" + ")");
  }

  public boolean visit(MethodMod_JavaObject5 node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject5" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject4 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject4" + ")");
  }

  public boolean visit(MethodMod_JavaObject4 node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject4" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject3 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject3" + ")");
  }

  public boolean visit(MethodMod_JavaObject3 node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject3" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject2" + ")");
  }

  public boolean visit(MethodMod_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject2" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject1" + ")");
  }

  public boolean visit(MethodMod_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject1" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject0" + ")");
  }

  public boolean visit(MethodMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObject" + ")");
  }

  public boolean visit(MethodMod_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObject" + ")");
    return true;
  }

  public void endVisit(ThrowsDec node)
  { 
    unimplementedVisitor("endVisit(" + "ThrowsDec" + ")");
  }

  public boolean visit(ThrowsDec node)
  { 
    unimplementedVisitor("visit(" + "ThrowsDec" + ")");
    return true;
  }

  public void endVisit(ExceptionType_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ExceptionType_JavaObject" + ")");
  }

  public boolean visit(ExceptionType_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ExceptionType_JavaObject" + ")");
    return true;
  }

  public void endVisit(MethodBody_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "MethodBody_JavaObject" + ")");
  }

  public boolean visit(MethodBody_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "MethodBody_JavaObject" + ")");
    return true;
  }

  public void endVisit(NoMethodBody node)
  { 
    unimplementedVisitor("endVisit(" + "NoMethodBody" + ")");
  }

  public boolean visit(NoMethodBody node)
  { 
    unimplementedVisitor("visit(" + "NoMethodBody" + ")");
    return true;
  }

  public void endVisit(InstanceInit node)
  { 
    unimplementedVisitor("endVisit(" + "InstanceInit" + ")");
  }

  public boolean visit(InstanceInit node)
  { 
    unimplementedVisitor("visit(" + "InstanceInit" + ")");
    return true;
  }

  public void endVisit(StaticInit node)
  { 
    unimplementedVisitor("endVisit(" + "StaticInit" + ")");
  }

  public boolean visit(StaticInit node)
  { 
    unimplementedVisitor("visit(" + "StaticInit" + ")");
    return true;
  }

  public void endVisit(ConstrDec node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrDec" + ")");
  }

  public boolean visit(ConstrDec node)
  { 
    unimplementedVisitor("visit(" + "ConstrDec" + ")");
    return true;
  }

  public void endVisit(ConstrDecHead node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrDecHead" + ")");
  }

  public boolean visit(ConstrDecHead node)
  { 
    unimplementedVisitor("visit(" + "ConstrDecHead" + ")");
    return true;
  }

  public void endVisit(ConstrBody node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrBody" + ")");
  }

  public boolean visit(ConstrBody node)
  { 
    unimplementedVisitor("visit(" + "ConstrBody" + ")");
    return true;
  }

  public void endVisit(AltConstrInv node)
  { 
    unimplementedVisitor("endVisit(" + "AltConstrInv" + ")");
  }

  public boolean visit(AltConstrInv node)
  { 
    unimplementedVisitor("visit(" + "AltConstrInv" + ")");
    return true;
  }

  public void endVisit(SuperConstrInv node)
  { 
    unimplementedVisitor("endVisit(" + "SuperConstrInv" + ")");
  }

  public boolean visit(SuperConstrInv node)
  { 
    unimplementedVisitor("visit(" + "SuperConstrInv" + ")");
    return true;
  }

  public void endVisit(QSuperConstrInv node)
  { 
    unimplementedVisitor("endVisit(" + "QSuperConstrInv" + ")");
  }

  public boolean visit(QSuperConstrInv node)
  { 
    unimplementedVisitor("visit(" + "QSuperConstrInv" + ")");
    return true;
  }

  public void endVisit(ConstrMod_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrMod_JavaObject1" + ")");
  }

  public boolean visit(ConstrMod_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "ConstrMod_JavaObject1" + ")");
    return true;
  }

  public void endVisit(ConstrMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrMod_JavaObject0" + ")");
  }

  public boolean visit(ConstrMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "ConstrMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(ConstrMod_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrMod_JavaObject" + ")");
  }

  public boolean visit(ConstrMod_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ConstrMod_JavaObject" + ")");
    return true;
  }

  public void endVisit(EnumDec node)
  { 
    unimplementedVisitor("endVisit(" + "EnumDec" + ")");
  }

  public boolean visit(EnumDec node)
  { 
    unimplementedVisitor("visit(" + "EnumDec" + ")");
    return true;
  }

  public void endVisit(EnumDecHead node)
  { 
    unimplementedVisitor("endVisit(" + "EnumDecHead" + ")");
  }

  public boolean visit(EnumDecHead node)
  { 
    unimplementedVisitor("visit(" + "EnumDecHead" + ")");
    return true;
  }

  public void endVisit(EnumBody0 node)
  { 
    unimplementedVisitor("endVisit(" + "EnumBody0" + ")");
  }

  public boolean visit(EnumBody0 node)
  { 
    unimplementedVisitor("visit(" + "EnumBody0" + ")");
    return true;
  }

  public void endVisit(EnumBody node)
  { 
    unimplementedVisitor("endVisit(" + "EnumBody" + ")");
  }

  public boolean visit(EnumBody node)
  { 
    unimplementedVisitor("visit(" + "EnumBody" + ")");
    return true;
  }

  public void endVisit(EnumConst node)
  { 
    unimplementedVisitor("endVisit(" + "EnumConst" + ")");
  }

  public boolean visit(EnumConst node)
  { 
    unimplementedVisitor("visit(" + "EnumConst" + ")");
    return true;
  }

  public void endVisit(EnumConstArgs node)
  { 
    unimplementedVisitor("endVisit(" + "EnumConstArgs" + ")");
  }

  public boolean visit(EnumConstArgs node)
  { 
    unimplementedVisitor("visit(" + "EnumConstArgs" + ")");
    return true;
  }

  public void endVisit(EnumBodyDecs node)
  { 
    unimplementedVisitor("endVisit(" + "EnumBodyDecs" + ")");
  }

  public boolean visit(EnumBodyDecs node)
  { 
    unimplementedVisitor("visit(" + "EnumBodyDecs" + ")");
    return true;
  }

  public void endVisit(ConstantDec node)
  { 
    unimplementedVisitor("endVisit(" + "ConstantDec" + ")");
  }

  public boolean visit(ConstantDec node)
  { 
    unimplementedVisitor("visit(" + "ConstantDec" + ")");
    return true;
  }

  public void endVisit(ConstantMod_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "ConstantMod_JavaObject1" + ")");
  }

  public boolean visit(ConstantMod_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "ConstantMod_JavaObject1" + ")");
    return true;
  }

  public void endVisit(ConstantMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "ConstantMod_JavaObject0" + ")");
  }

  public boolean visit(ConstantMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "ConstantMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(ConstantMod_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ConstantMod_JavaObject" + ")");
  }

  public boolean visit(ConstantMod_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ConstantMod_JavaObject" + ")");
    return true;
  }

  public void endVisit(AbstractMethodDec node)
  { 
    unimplementedVisitor("endVisit(" + "AbstractMethodDec" + ")");
  }

  public boolean visit(AbstractMethodDec node)
  { 
    unimplementedVisitor("visit(" + "AbstractMethodDec" + ")");
    return true;
  }

  public void endVisit(DeprAbstractMethodDec node)
  { 
    unimplementedVisitor("endVisit(" + "DeprAbstractMethodDec" + ")");
  }

  public boolean visit(DeprAbstractMethodDec node)
  { 
    unimplementedVisitor("visit(" + "DeprAbstractMethodDec" + ")");
    return true;
  }

  public void endVisit(AbstractMethodMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "AbstractMethodMod_JavaObject0" + ")");
  }

  public boolean visit(AbstractMethodMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "AbstractMethodMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(AbstractMethodMod_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "AbstractMethodMod_JavaObject" + ")");
  }

  public boolean visit(AbstractMethodMod_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "AbstractMethodMod_JavaObject" + ")");
    return true;
  }

  public void endVisit(AnnoDec node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoDec" + ")");
  }

  public boolean visit(AnnoDec node)
  { 
    unimplementedVisitor("visit(" + "AnnoDec" + ")");
    return true;
  }

  public void endVisit(AnnoDecHead node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoDecHead" + ")");
  }

  public boolean visit(AnnoDecHead node)
  { 
    unimplementedVisitor("visit(" + "AnnoDecHead" + ")");
    return true;
  }

  public void endVisit(AnnoMethodDec node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoMethodDec" + ")");
  }

  public boolean visit(AnnoMethodDec node)
  { 
    unimplementedVisitor("visit(" + "AnnoMethodDec" + ")");
    return true;
  }

  public void endVisit(AnnoElemDec_JavaObject3 node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoElemDec_JavaObject3" + ")");
  }

  public boolean visit(AnnoElemDec_JavaObject3 node)
  { 
    unimplementedVisitor("visit(" + "AnnoElemDec_JavaObject3" + ")");
    return true;
  }

  public void endVisit(AnnoElemDec_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoElemDec_JavaObject2" + ")");
  }

  public boolean visit(AnnoElemDec_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "AnnoElemDec_JavaObject2" + ")");
    return true;
  }

  public void endVisit(AnnoElemDec_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoElemDec_JavaObject1" + ")");
  }

  public boolean visit(AnnoElemDec_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "AnnoElemDec_JavaObject1" + ")");
    return true;
  }

  public void endVisit(AnnoElemDec_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoElemDec_JavaObject0" + ")");
  }

  public boolean visit(AnnoElemDec_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "AnnoElemDec_JavaObject0" + ")");
    return true;
  }

  public void endVisit(AnnoElemDec_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoElemDec_JavaObject" + ")");
  }

  public boolean visit(AnnoElemDec_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "AnnoElemDec_JavaObject" + ")");
    return true;
  }

  public void endVisit(Semicolon2 node)
  { 
    unimplementedVisitor("endVisit(" + "Semicolon2" + ")");
  }

  public boolean visit(Semicolon2 node)
  { 
    unimplementedVisitor("visit(" + "Semicolon2" + ")");
    return true;
  }

  public void endVisit(DefaultVal node)
  { 
    unimplementedVisitor("endVisit(" + "DefaultVal" + ")");
  }

  public boolean visit(DefaultVal node)
  { 
    unimplementedVisitor("visit(" + "DefaultVal" + ")");
    return true;
  }

  public void endVisit(InterfaceDec_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceDec_JavaObject" + ")");
  }

  public boolean visit(InterfaceDec_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "InterfaceDec_JavaObject" + ")");
    return true;
  }

  public void endVisit(InterfaceDec node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceDec" + ")");
  }

  public boolean visit(InterfaceDec node)
  { 
    unimplementedVisitor("visit(" + "InterfaceDec" + ")");
    return true;
  }

  public void endVisit(InterfaceDecHead node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceDecHead" + ")");
  }

  public boolean visit(InterfaceDecHead node)
  { 
    unimplementedVisitor("visit(" + "InterfaceDecHead" + ")");
    return true;
  }

  public void endVisit(ExtendsInterfaces node)
  { 
    unimplementedVisitor("endVisit(" + "ExtendsInterfaces" + ")");
  }

  public boolean visit(ExtendsInterfaces node)
  { 
    unimplementedVisitor("visit(" + "ExtendsInterfaces" + ")");
    return true;
  }

  public void endVisit(InterfaceMemberDec_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMemberDec_JavaObject2" + ")");
  }

  public boolean visit(InterfaceMemberDec_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMemberDec_JavaObject2" + ")");
    return true;
  }

  public void endVisit(InterfaceMemberDec_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMemberDec_JavaObject1" + ")");
  }

  public boolean visit(InterfaceMemberDec_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMemberDec_JavaObject1" + ")");
    return true;
  }

  public void endVisit(InterfaceMemberDec_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMemberDec_JavaObject0" + ")");
  }

  public boolean visit(InterfaceMemberDec_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMemberDec_JavaObject0" + ")");
    return true;
  }

  public void endVisit(InterfaceMemberDec_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMemberDec_JavaObject" + ")");
  }

  public boolean visit(InterfaceMemberDec_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMemberDec_JavaObject" + ")");
    return true;
  }

  public void endVisit(Semicolon1 node)
  { 
    unimplementedVisitor("endVisit(" + "Semicolon1" + ")");
  }

  public boolean visit(Semicolon1 node)
  { 
    unimplementedVisitor("visit(" + "Semicolon1" + ")");
    return true;
  }

  public void endVisit(InterfaceMod_JavaObject4 node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMod_JavaObject4" + ")");
  }

  public boolean visit(InterfaceMod_JavaObject4 node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMod_JavaObject4" + ")");
    return true;
  }

  public void endVisit(InterfaceMod_JavaObject3 node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMod_JavaObject3" + ")");
  }

  public boolean visit(InterfaceMod_JavaObject3 node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMod_JavaObject3" + ")");
    return true;
  }

  public void endVisit(InterfaceMod_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMod_JavaObject2" + ")");
  }

  public boolean visit(InterfaceMod_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMod_JavaObject2" + ")");
    return true;
  }

  public void endVisit(InterfaceMod_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMod_JavaObject1" + ")");
  }

  public boolean visit(InterfaceMod_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMod_JavaObject1" + ")");
    return true;
  }

  public void endVisit(InterfaceMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMod_JavaObject0" + ")");
  }

  public boolean visit(InterfaceMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(InterfaceMod_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMod_JavaObject" + ")");
  }

  public boolean visit(InterfaceMod_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMod_JavaObject" + ")");
    return true;
  }

  public void endVisit(ClassDec_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ClassDec_JavaObject" + ")");
  }

  public boolean visit(ClassDec_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ClassDec_JavaObject" + ")");
    return true;
  }

  public void endVisit(ClassDec node)
  { 
    unimplementedVisitor("endVisit(" + "ClassDec" + ")");
  }

  public boolean visit(ClassDec node)
  { 
    unimplementedVisitor("visit(" + "ClassDec" + ")");
    return true;
  }

  public void endVisit(ClassBody node)
  { 
    unimplementedVisitor("endVisit(" + "ClassBody" + ")");
  }

  public boolean visit(ClassBody node)
  { 
    unimplementedVisitor("visit(" + "ClassBody" + ")");
    return true;
  }

  public void endVisit(ClassDecHead node)
  { 
    unimplementedVisitor("endVisit(" + "ClassDecHead" + ")");
  }

  public boolean visit(ClassDecHead node)
  { 
    unimplementedVisitor("visit(" + "ClassDecHead" + ")");
    return true;
  }

  public void endVisit(ClassMod_JavaObject5 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMod_JavaObject5" + ")");
  }

  public boolean visit(ClassMod_JavaObject5 node)
  { 
    unimplementedVisitor("visit(" + "ClassMod_JavaObject5" + ")");
    return true;
  }

  public void endVisit(ClassMod_JavaObject4 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMod_JavaObject4" + ")");
  }

  public boolean visit(ClassMod_JavaObject4 node)
  { 
    unimplementedVisitor("visit(" + "ClassMod_JavaObject4" + ")");
    return true;
  }

  public void endVisit(ClassMod_JavaObject3 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMod_JavaObject3" + ")");
  }

  public boolean visit(ClassMod_JavaObject3 node)
  { 
    unimplementedVisitor("visit(" + "ClassMod_JavaObject3" + ")");
    return true;
  }

  public void endVisit(ClassMod_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMod_JavaObject2" + ")");
  }

  public boolean visit(ClassMod_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "ClassMod_JavaObject2" + ")");
    return true;
  }

  public void endVisit(ClassMod_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMod_JavaObject1" + ")");
  }

  public boolean visit(ClassMod_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "ClassMod_JavaObject1" + ")");
    return true;
  }

  public void endVisit(ClassMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMod_JavaObject0" + ")");
  }

  public boolean visit(ClassMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "ClassMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(ClassMod_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMod_JavaObject" + ")");
  }

  public boolean visit(ClassMod_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ClassMod_JavaObject" + ")");
    return true;
  }

  public void endVisit(SuperDec node)
  { 
    unimplementedVisitor("endVisit(" + "SuperDec" + ")");
  }

  public boolean visit(SuperDec node)
  { 
    unimplementedVisitor("visit(" + "SuperDec" + ")");
    return true;
  }

  public void endVisit(ImplementsDec node)
  { 
    unimplementedVisitor("endVisit(" + "ImplementsDec" + ")");
  }

  public boolean visit(ImplementsDec node)
  { 
    unimplementedVisitor("visit(" + "ImplementsDec" + ")");
    return true;
  }

  public void endVisit(ClassBodyDec_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassBodyDec_JavaObject2" + ")");
  }

  public boolean visit(ClassBodyDec_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "ClassBodyDec_JavaObject2" + ")");
    return true;
  }

  public void endVisit(ClassBodyDec_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassBodyDec_JavaObject1" + ")");
  }

  public boolean visit(ClassBodyDec_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "ClassBodyDec_JavaObject1" + ")");
    return true;
  }

  public void endVisit(ClassBodyDec_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassBodyDec_JavaObject0" + ")");
  }

  public boolean visit(ClassBodyDec_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "ClassBodyDec_JavaObject0" + ")");
    return true;
  }

  public void endVisit(ClassBodyDec_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ClassBodyDec_JavaObject" + ")");
  }

  public boolean visit(ClassBodyDec_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ClassBodyDec_JavaObject" + ")");
    return true;
  }

  public void endVisit(ClassMemberDec_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMemberDec_JavaObject2" + ")");
  }

  public boolean visit(ClassMemberDec_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "ClassMemberDec_JavaObject2" + ")");
    return true;
  }

  public void endVisit(ClassMemberDec_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMemberDec_JavaObject1" + ")");
  }

  public boolean visit(ClassMemberDec_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "ClassMemberDec_JavaObject1" + ")");
    return true;
  }

  public void endVisit(ClassMemberDec_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMemberDec_JavaObject0" + ")");
  }

  public boolean visit(ClassMemberDec_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "ClassMemberDec_JavaObject0" + ")");
    return true;
  }

  public void endVisit(ClassMemberDec_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMemberDec_JavaObject" + ")");
  }

  public boolean visit(ClassMemberDec_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ClassMemberDec_JavaObject" + ")");
    return true;
  }

  public void endVisit(Semicolon0 node)
  { 
    unimplementedVisitor("endVisit(" + "Semicolon0" + ")");
  }

  public boolean visit(Semicolon0 node)
  { 
    unimplementedVisitor("visit(" + "Semicolon0" + ")");
    return true;
  }

  public void endVisit(NewInstance node)
  { 
    unimplementedVisitor("endVisit(" + "NewInstance" + ")");
  }

  public boolean visit(NewInstance node)
  { 
    unimplementedVisitor("visit(" + "NewInstance" + ")");
    return true;
  }

  public void endVisit(QNewInstance node)
  { 
    unimplementedVisitor("endVisit(" + "QNewInstance" + ")");
  }

  public boolean visit(QNewInstance node)
  { 
    unimplementedVisitor("visit(" + "QNewInstance" + ")");
    return true;
  }

  public void endVisit(Expr_JavaObject3 node)
  { 
    unimplementedVisitor("endVisit(" + "Expr_JavaObject3" + ")");
  }

  public boolean visit(Expr_JavaObject3 node)
  { 
    unimplementedVisitor("visit(" + "Expr_JavaObject3" + ")");
    return true;
  }

  public void endVisit(NewArray0 node)
  { 
    unimplementedVisitor("endVisit(" + "NewArray0" + ")");
  }

  public boolean visit(NewArray0 node)
  { 
    unimplementedVisitor("visit(" + "NewArray0" + ")");
    return true;
  }

  public void endVisit(NewArray node)
  { 
    unimplementedVisitor("endVisit(" + "NewArray" + ")");
  }

  public boolean visit(NewArray node)
  { 
    unimplementedVisitor("visit(" + "NewArray" + ")");
    return true;
  }

  public void endVisit(ArrayBaseType_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayBaseType_JavaObject0" + ")");
  }

  public boolean visit(ArrayBaseType_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "ArrayBaseType_JavaObject0" + ")");
    return true;
  }

  public void endVisit(ArrayBaseType_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayBaseType_JavaObject" + ")");
  }

  public boolean visit(ArrayBaseType_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ArrayBaseType_JavaObject" + ")");
    return true;
  }

  public void endVisit(UnboundWld node)
  { 
    unimplementedVisitor("endVisit(" + "UnboundWld" + ")");
  }

  public boolean visit(UnboundWld node)
  { 
    unimplementedVisitor("visit(" + "UnboundWld" + ")");
    return true;
  }

  public void endVisit(Dim node)
  { 
    unimplementedVisitor("endVisit(" + "Dim" + ")");
  }

  public boolean visit(Dim node)
  { 
    unimplementedVisitor("visit(" + "Dim" + ")");
    return true;
  }

  public void endVisit(Expr_JavaObject2 node)
  { 
    unimplementedVisitor("endVisit(" + "Expr_JavaObject2" + ")");
  }

  public boolean visit(Expr_JavaObject2 node)
  { 
    unimplementedVisitor("visit(" + "Expr_JavaObject2" + ")");
    return true;
  }

  public void endVisit(Field node)
  { 
    unimplementedVisitor("endVisit(" + "Field" + ")");
  }

  public boolean visit(Field node)
  { 
    unimplementedVisitor("visit(" + "Field" + ")");
    return true;
  }

  public void endVisit(SuperField node)
  { 
    unimplementedVisitor("endVisit(" + "SuperField" + ")");
  }

  public boolean visit(SuperField node)
  { 
    unimplementedVisitor("visit(" + "SuperField" + ")");
    return true;
  }

  public void endVisit(QSuperField node)
  { 
    unimplementedVisitor("endVisit(" + "QSuperField" + ")");
  }

  public boolean visit(QSuperField node)
  { 
    unimplementedVisitor("visit(" + "QSuperField" + ")");
    return true;
  }

  public void endVisit(Expr_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "Expr_JavaObject1" + ")");
  }

  public boolean visit(Expr_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "Expr_JavaObject1" + ")");
    return true;
  }

  public void endVisit(ArrayAccess node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayAccess" + ")");
  }

  public boolean visit(ArrayAccess node)
  { 
    unimplementedVisitor("visit(" + "ArrayAccess" + ")");
    return true;
  }

  public void endVisit(ArraySubscript node)
  { 
    unimplementedVisitor("endVisit(" + "ArraySubscript" + ")");
  }

  public boolean visit(ArraySubscript node)
  { 
    unimplementedVisitor("visit(" + "ArraySubscript" + ")");
    return true;
  }

  public void endVisit(Invoke node)
  { 
    unimplementedVisitor("endVisit(" + "Invoke" + ")");
  }

  public boolean visit(Invoke node)
  { 
    unimplementedVisitor("visit(" + "Invoke" + ")");
    return true;
  }

  public void endVisit(Method0 node)
  { 
    unimplementedVisitor("endVisit(" + "Method0" + ")");
  }

  public boolean visit(Method0 node)
  { 
    unimplementedVisitor("visit(" + "Method0" + ")");
    return true;
  }

  public void endVisit(Method node)
  { 
    unimplementedVisitor("endVisit(" + "Method" + ")");
  }

  public boolean visit(Method node)
  { 
    unimplementedVisitor("visit(" + "Method" + ")");
    return true;
  }

  public void endVisit(SuperMethod node)
  { 
    unimplementedVisitor("endVisit(" + "SuperMethod" + ")");
  }

  public boolean visit(SuperMethod node)
  { 
    unimplementedVisitor("visit(" + "SuperMethod" + ")");
    return true;
  }

  public void endVisit(QSuperMethod node)
  { 
    unimplementedVisitor("endVisit(" + "QSuperMethod" + ")");
  }

  public boolean visit(QSuperMethod node)
  { 
    unimplementedVisitor("visit(" + "QSuperMethod" + ")");
    return true;
  }

  public void endVisit(GenericMethod node)
  { 
    unimplementedVisitor("endVisit(" + "GenericMethod" + ")");
  }

  public boolean visit(GenericMethod node)
  { 
    unimplementedVisitor("visit(" + "GenericMethod" + ")");
    return true;
  }

  public void endVisit(Expr_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Expr_JavaObject0" + ")");
  }

  public boolean visit(Expr_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Expr_JavaObject0" + ")");
    return true;
  }

  public void endVisit(PostIncr node)
  { 
    unimplementedVisitor("endVisit(" + "PostIncr" + ")");
  }

  public boolean visit(PostIncr node)
  { 
    unimplementedVisitor("visit(" + "PostIncr" + ")");
    return true;
  }

  public void endVisit(PostDecr node)
  { 
    unimplementedVisitor("endVisit(" + "PostDecr" + ")");
  }

  public boolean visit(PostDecr node)
  { 
    unimplementedVisitor("visit(" + "PostDecr" + ")");
    return true;
  }

  public void endVisit(Plus0 node)
  { 
    unimplementedVisitor("endVisit(" + "Plus0" + ")");
  }

  public boolean visit(Plus0 node)
  { 
    unimplementedVisitor("visit(" + "Plus0" + ")");
    return true;
  }

  public void endVisit(Minus0 node)
  { 
    unimplementedVisitor("endVisit(" + "Minus0" + ")");
  }

  public boolean visit(Minus0 node)
  { 
    unimplementedVisitor("visit(" + "Minus0" + ")");
    return true;
  }

  public void endVisit(PreIncr node)
  { 
    unimplementedVisitor("endVisit(" + "PreIncr" + ")");
  }

  public boolean visit(PreIncr node)
  { 
    unimplementedVisitor("visit(" + "PreIncr" + ")");
    return true;
  }

  public void endVisit(PreDecr node)
  { 
    unimplementedVisitor("endVisit(" + "PreDecr" + ")");
  }

  public boolean visit(PreDecr node)
  { 
    unimplementedVisitor("visit(" + "PreDecr" + ")");
    return true;
  }

  public void endVisit(Complement node)
  { 
    unimplementedVisitor("endVisit(" + "Complement" + ")");
  }

  public boolean visit(Complement node)
  { 
    unimplementedVisitor("visit(" + "Complement" + ")");
    return true;
  }

  public void endVisit(Not node)
  { 
    unimplementedVisitor("endVisit(" + "Not" + ")");
  }

  public boolean visit(Not node)
  { 
    unimplementedVisitor("visit(" + "Not" + ")");
    return true;
  }

  public void endVisit(CastPrim node)
  { 
    unimplementedVisitor("endVisit(" + "CastPrim" + ")");
  }

  public boolean visit(CastPrim node)
  { 
    unimplementedVisitor("visit(" + "CastPrim" + ")");
    return true;
  }

  public void endVisit(CastRef node)
  { 
    unimplementedVisitor("endVisit(" + "CastRef" + ")");
  }

  public boolean visit(CastRef node)
  { 
    unimplementedVisitor("visit(" + "CastRef" + ")");
    return true;
  }

  public void endVisit(Expr_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "Expr_JavaObject" + ")");
  }

  public boolean visit(Expr_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "Expr_JavaObject" + ")");
    return true;
  }

  public void endVisit(InstanceOf node)
  { 
    unimplementedVisitor("endVisit(" + "InstanceOf" + ")");
  }

  public boolean visit(InstanceOf node)
  { 
    unimplementedVisitor("visit(" + "InstanceOf" + ")");
    return true;
  }

  public void endVisit(Mul node)
  { 
    unimplementedVisitor("endVisit(" + "Mul" + ")");
  }

  public boolean visit(Mul node)
  { 
    unimplementedVisitor("visit(" + "Mul" + ")");
    return true;
  }

  public void endVisit(Div node)
  { 
    unimplementedVisitor("endVisit(" + "Div" + ")");
  }

  public boolean visit(Div node)
  { 
    unimplementedVisitor("visit(" + "Div" + ")");
    return true;
  }

  public void endVisit(Remain node)
  { 
    unimplementedVisitor("endVisit(" + "Remain" + ")");
  }

  public boolean visit(Remain node)
  { 
    unimplementedVisitor("visit(" + "Remain" + ")");
    return true;
  }

  public void endVisit(Plus node)
  { 
    unimplementedVisitor("endVisit(" + "Plus" + ")");
  }

  public boolean visit(Plus node)
  { 
    unimplementedVisitor("visit(" + "Plus" + ")");
    return true;
  }

  public void endVisit(Minus node)
  { 
    unimplementedVisitor("endVisit(" + "Minus" + ")");
  }

  public boolean visit(Minus node)
  { 
    unimplementedVisitor("visit(" + "Minus" + ")");
    return true;
  }

  public void endVisit(LeftShift node)
  { 
    unimplementedVisitor("endVisit(" + "LeftShift" + ")");
  }

  public boolean visit(LeftShift node)
  { 
    unimplementedVisitor("visit(" + "LeftShift" + ")");
    return true;
  }

  public void endVisit(RightShift node)
  { 
    unimplementedVisitor("endVisit(" + "RightShift" + ")");
  }

  public boolean visit(RightShift node)
  { 
    unimplementedVisitor("visit(" + "RightShift" + ")");
    return true;
  }

  public void endVisit(URightShift node)
  { 
    unimplementedVisitor("endVisit(" + "URightShift" + ")");
  }

  public boolean visit(URightShift node)
  { 
    unimplementedVisitor("visit(" + "URightShift" + ")");
    return true;
  }

  public void endVisit(Lt node)
  { 
    unimplementedVisitor("endVisit(" + "Lt" + ")");
  }

  public boolean visit(Lt node)
  { 
    unimplementedVisitor("visit(" + "Lt" + ")");
    return true;
  }

  public void endVisit(Gt node)
  { 
    unimplementedVisitor("endVisit(" + "Gt" + ")");
  }

  public boolean visit(Gt node)
  { 
    unimplementedVisitor("visit(" + "Gt" + ")");
    return true;
  }

  public void endVisit(LtEq node)
  { 
    unimplementedVisitor("endVisit(" + "LtEq" + ")");
  }

  public boolean visit(LtEq node)
  { 
    unimplementedVisitor("visit(" + "LtEq" + ")");
    return true;
  }

  public void endVisit(GtEq node)
  { 
    unimplementedVisitor("endVisit(" + "GtEq" + ")");
  }

  public boolean visit(GtEq node)
  { 
    unimplementedVisitor("visit(" + "GtEq" + ")");
    return true;
  }

  public void endVisit(Eq node)
  { 
    unimplementedVisitor("endVisit(" + "Eq" + ")");
  }

  public boolean visit(Eq node)
  { 
    unimplementedVisitor("visit(" + "Eq" + ")");
    return true;
  }

  public void endVisit(NotEq node)
  { 
    unimplementedVisitor("endVisit(" + "NotEq" + ")");
  }

  public boolean visit(NotEq node)
  { 
    unimplementedVisitor("visit(" + "NotEq" + ")");
    return true;
  }

  public void endVisit(LazyAnd node)
  { 
    unimplementedVisitor("endVisit(" + "LazyAnd" + ")");
  }

  public boolean visit(LazyAnd node)
  { 
    unimplementedVisitor("visit(" + "LazyAnd" + ")");
    return true;
  }

  public void endVisit(LazyOr node)
  { 
    unimplementedVisitor("endVisit(" + "LazyOr" + ")");
  }

  public boolean visit(LazyOr node)
  { 
    unimplementedVisitor("visit(" + "LazyOr" + ")");
    return true;
  }

  public void endVisit(And node)
  { 
    unimplementedVisitor("endVisit(" + "And" + ")");
  }

  public boolean visit(And node)
  { 
    unimplementedVisitor("visit(" + "And" + ")");
    return true;
  }

  public void endVisit(ExcOr node)
  { 
    unimplementedVisitor("endVisit(" + "ExcOr" + ")");
  }

  public boolean visit(ExcOr node)
  { 
    unimplementedVisitor("visit(" + "ExcOr" + ")");
    return true;
  }

  public void endVisit(Or node)
  { 
    unimplementedVisitor("endVisit(" + "Or" + ")");
  }

  public boolean visit(Or node)
  { 
    unimplementedVisitor("visit(" + "Or" + ")");
    return true;
  }

  public void endVisit(Cond node)
  { 
    unimplementedVisitor("endVisit(" + "Cond" + ")");
  }

  public boolean visit(Cond node)
  { 
    unimplementedVisitor("visit(" + "Cond" + ")");
    return true;
  }

  public void endVisit(CondMid node)
  { 
    unimplementedVisitor("endVisit(" + "CondMid" + ")");
  }

  public boolean visit(CondMid node)
  { 
    unimplementedVisitor("visit(" + "CondMid" + ")");
    return true;
  }

  public void endVisit(Assign node)
  { 
    unimplementedVisitor("endVisit(" + "Assign" + ")");
  }

  public boolean visit(Assign node)
  { 
    unimplementedVisitor("visit(" + "Assign" + ")");
    return true;
  }

  public void endVisit(AssignMul node)
  { 
    unimplementedVisitor("endVisit(" + "AssignMul" + ")");
  }

  public boolean visit(AssignMul node)
  { 
    unimplementedVisitor("visit(" + "AssignMul" + ")");
    return true;
  }

  public void endVisit(AssignDiv node)
  { 
    unimplementedVisitor("endVisit(" + "AssignDiv" + ")");
  }

  public boolean visit(AssignDiv node)
  { 
    unimplementedVisitor("visit(" + "AssignDiv" + ")");
    return true;
  }

  public void endVisit(AssignRemain node)
  { 
    unimplementedVisitor("endVisit(" + "AssignRemain" + ")");
  }

  public boolean visit(AssignRemain node)
  { 
    unimplementedVisitor("visit(" + "AssignRemain" + ")");
    return true;
  }

  public void endVisit(AssignPlus node)
  { 
    unimplementedVisitor("endVisit(" + "AssignPlus" + ")");
  }

  public boolean visit(AssignPlus node)
  { 
    unimplementedVisitor("visit(" + "AssignPlus" + ")");
    return true;
  }

  public void endVisit(AssignMinus node)
  { 
    unimplementedVisitor("endVisit(" + "AssignMinus" + ")");
  }

  public boolean visit(AssignMinus node)
  { 
    unimplementedVisitor("visit(" + "AssignMinus" + ")");
    return true;
  }

  public void endVisit(AssignLeftShift node)
  { 
    unimplementedVisitor("endVisit(" + "AssignLeftShift" + ")");
  }

  public boolean visit(AssignLeftShift node)
  { 
    unimplementedVisitor("visit(" + "AssignLeftShift" + ")");
    return true;
  }

  public void endVisit(AssignRightShift node)
  { 
    unimplementedVisitor("endVisit(" + "AssignRightShift" + ")");
  }

  public boolean visit(AssignRightShift node)
  { 
    unimplementedVisitor("visit(" + "AssignRightShift" + ")");
    return true;
  }

  public void endVisit(AssignURightShift node)
  { 
    unimplementedVisitor("endVisit(" + "AssignURightShift" + ")");
  }

  public boolean visit(AssignURightShift node)
  { 
    unimplementedVisitor("visit(" + "AssignURightShift" + ")");
    return true;
  }

  public void endVisit(AssignAnd node)
  { 
    unimplementedVisitor("endVisit(" + "AssignAnd" + ")");
  }

  public boolean visit(AssignAnd node)
  { 
    unimplementedVisitor("visit(" + "AssignAnd" + ")");
    return true;
  }

  public void endVisit(AssignExcOr node)
  { 
    unimplementedVisitor("endVisit(" + "AssignExcOr" + ")");
  }

  public boolean visit(AssignExcOr node)
  { 
    unimplementedVisitor("visit(" + "AssignExcOr" + ")");
    return true;
  }

  public void endVisit(AssignOr node)
  { 
    unimplementedVisitor("endVisit(" + "AssignOr" + ")");
  }

  public boolean visit(AssignOr node)
  { 
    unimplementedVisitor("visit(" + "AssignOr" + ")");
    return true;
  }

  public void endVisit(LHS_JavaObject1 node)
  { 
    unimplementedVisitor("endVisit(" + "LHS_JavaObject1" + ")");
  }

  public boolean visit(LHS_JavaObject1 node)
  { 
    unimplementedVisitor("visit(" + "LHS_JavaObject1" + ")");
    return true;
  }

  public void endVisit(LHS_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "LHS_JavaObject0" + ")");
  }

  public boolean visit(LHS_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "LHS_JavaObject0" + ")");
    return true;
  }

  public void endVisit(LHS_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "LHS_JavaObject" + ")");
  }

  public boolean visit(LHS_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "LHS_JavaObject" + ")");
    return true;
  }

  public void endVisit(Anno node)
  { 
    unimplementedVisitor("endVisit(" + "Anno" + ")");
  }

  public boolean visit(Anno node)
  { 
    unimplementedVisitor("visit(" + "Anno" + ")");
    return true;
  }

  public void endVisit(SingleElemAnno node)
  { 
    unimplementedVisitor("endVisit(" + "SingleElemAnno" + ")");
  }

  public boolean visit(SingleElemAnno node)
  { 
    unimplementedVisitor("visit(" + "SingleElemAnno" + ")");
    return true;
  }

  public void endVisit(MarkerAnno node)
  { 
    unimplementedVisitor("endVisit(" + "MarkerAnno" + ")");
  }

  public boolean visit(MarkerAnno node)
  { 
    unimplementedVisitor("visit(" + "MarkerAnno" + ")");
    return true;
  }

  public void endVisit(ElemValPair node)
  { 
    unimplementedVisitor("endVisit(" + "ElemValPair" + ")");
  }

  public boolean visit(ElemValPair node)
  { 
    unimplementedVisitor("visit(" + "ElemValPair" + ")");
    return true;
  }

  public void endVisit(ElemVal_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "ElemVal_JavaObject0" + ")");
  }

  public boolean visit(ElemVal_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "ElemVal_JavaObject0" + ")");
    return true;
  }

  public void endVisit(ElemVal_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "ElemVal_JavaObject" + ")");
  }

  public boolean visit(ElemVal_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "ElemVal_JavaObject" + ")");
    return true;
  }

  public void endVisit(ElemValArrayInit0 node)
  { 
    unimplementedVisitor("endVisit(" + "ElemValArrayInit0" + ")");
  }

  public boolean visit(ElemValArrayInit0 node)
  { 
    unimplementedVisitor("visit(" + "ElemValArrayInit0" + ")");
    return true;
  }

  public void endVisit(ElemValArrayInit node)
  { 
    unimplementedVisitor("endVisit(" + "ElemValArrayInit" + ")");
  }

  public boolean visit(ElemValArrayInit node)
  { 
    unimplementedVisitor("visit(" + "ElemValArrayInit" + ")");
    return true;
  }

  public void endVisit(PackageDec node)
  { 
    unimplementedVisitor("endVisit(" + "PackageDec" + ")");
  }

  public boolean visit(PackageDec node)
  { 
    unimplementedVisitor("visit(" + "PackageDec" + ")");
    return true;
  }

  public void endVisit(TypeImportDec node)
  { 
    unimplementedVisitor("endVisit(" + "TypeImportDec" + ")");
  }

  public boolean visit(TypeImportDec node)
  { 
    unimplementedVisitor("visit(" + "TypeImportDec" + ")");
    return true;
  }

  public void endVisit(TypeImportOnDemandDec node)
  { 
    unimplementedVisitor("endVisit(" + "TypeImportOnDemandDec" + ")");
  }

  public boolean visit(TypeImportOnDemandDec node)
  { 
    unimplementedVisitor("visit(" + "TypeImportOnDemandDec" + ")");
    return true;
  }

  public void endVisit(StaticImportDec node)
  { 
    unimplementedVisitor("endVisit(" + "StaticImportDec" + ")");
  }

  public boolean visit(StaticImportDec node)
  { 
    unimplementedVisitor("visit(" + "StaticImportDec" + ")");
    return true;
  }

  public void endVisit(StaticImportOnDemandDec node)
  { 
    unimplementedVisitor("endVisit(" + "StaticImportOnDemandDec" + ")");
  }

  public boolean visit(StaticImportOnDemandDec node)
  { 
    unimplementedVisitor("visit(" + "StaticImportOnDemandDec" + ")");
    return true;
  }

  public void endVisit(TypeDec_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "TypeDec_JavaObject0" + ")");
  }

  public boolean visit(TypeDec_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "TypeDec_JavaObject0" + ")");
    return true;
  }

  public void endVisit(TypeDec_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "TypeDec_JavaObject" + ")");
  }

  public boolean visit(TypeDec_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "TypeDec_JavaObject" + ")");
    return true;
  }

  public void endVisit(Semicolon node)
  { 
    unimplementedVisitor("endVisit(" + "Semicolon" + ")");
  }

  public boolean visit(Semicolon node)
  { 
    unimplementedVisitor("visit(" + "Semicolon" + ")");
    return true;
  }

  public void endVisit(CompilationUnit node)
  { 
    unimplementedVisitor("endVisit(" + "CompilationUnit" + ")");
  }

  public boolean visit(CompilationUnit node)
  { 
    unimplementedVisitor("visit(" + "CompilationUnit" + ")");
    return true;
  }

  public void endVisit(Metavar41 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar41" + ")");
  }

  public boolean visit(Metavar41 node)
  { 
    unimplementedVisitor("visit(" + "Metavar41" + ")");
    return true;
  }

  public void endVisit(Metavar40 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar40" + ")");
  }

  public boolean visit(Metavar40 node)
  { 
    unimplementedVisitor("visit(" + "Metavar40" + ")");
    return true;
  }

  public void endVisit(Metavar39 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar39" + ")");
  }

  public boolean visit(Metavar39 node)
  { 
    unimplementedVisitor("visit(" + "Metavar39" + ")");
    return true;
  }

  public void endVisit(Metavar38 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar38" + ")");
  }

  public boolean visit(Metavar38 node)
  { 
    unimplementedVisitor("visit(" + "Metavar38" + ")");
    return true;
  }

  public void endVisit(Metavar37 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar37" + ")");
  }

  public boolean visit(Metavar37 node)
  { 
    unimplementedVisitor("visit(" + "Metavar37" + ")");
    return true;
  }

  public void endVisit(Metavar36 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar36" + ")");
  }

  public boolean visit(Metavar36 node)
  { 
    unimplementedVisitor("visit(" + "Metavar36" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr80 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr80" + ")");
  }

  public boolean visit(ToMetaExpr80 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr80" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr79 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr79" + ")");
  }

  public boolean visit(ToMetaExpr79 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr79" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr78 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr78" + ")");
  }

  public boolean visit(ToMetaExpr78 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr78" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr77 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr77" + ")");
  }

  public boolean visit(ToMetaExpr77 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr77" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr76 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr76" + ")");
  }

  public boolean visit(ToMetaExpr76 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr76" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr75 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr75" + ")");
  }

  public boolean visit(ToMetaExpr75 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr75" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr74 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr74" + ")");
  }

  public boolean visit(ToMetaExpr74 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr74" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr73 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr73" + ")");
  }

  public boolean visit(ToMetaExpr73 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr73" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr72 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr72" + ")");
  }

  public boolean visit(ToMetaExpr72 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr72" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr71 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr71" + ")");
  }

  public boolean visit(ToMetaExpr71 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr71" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr7 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr7" + ")");
  }

  public boolean visit(ToMetaListExpr7 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr7" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr6 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr6" + ")");
  }

  public boolean visit(ToMetaListExpr6 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr6" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr70 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr70" + ")");
  }

  public boolean visit(ToMetaExpr70 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr70" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr69 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr69" + ")");
  }

  public boolean visit(ToMetaExpr69 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr69" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr5 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr5" + ")");
  }

  public boolean visit(ToMetaListExpr5 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr5" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr4 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr4" + ")");
  }

  public boolean visit(ToMetaListExpr4 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr4" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr68 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr68" + ")");
  }

  public boolean visit(ToMetaExpr68 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr68" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr67 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr67" + ")");
  }

  public boolean visit(ToMetaExpr67 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr67" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr3 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr3" + ")");
  }

  public boolean visit(ToMetaListExpr3 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr3" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr2 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr2" + ")");
  }

  public boolean visit(ToMetaListExpr2 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr2" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr33 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr33" + ")");
  }

  public boolean visit(FromMetaExpr33 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr33" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr32 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr32" + ")");
  }

  public boolean visit(FromMetaExpr32 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr32" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr31 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr31" + ")");
  }

  public boolean visit(FromMetaExpr31 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr31" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr30 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr30" + ")");
  }

  public boolean visit(FromMetaExpr30 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr30" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr29 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr29" + ")");
  }

  public boolean visit(FromMetaExpr29 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr29" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr28 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr28" + ")");
  }

  public boolean visit(FromMetaExpr28 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr28" + ")");
    return true;
  }

  public void endVisit(Metavar35 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar35" + ")");
  }

  public boolean visit(Metavar35 node)
  { 
    unimplementedVisitor("visit(" + "Metavar35" + ")");
    return true;
  }

  public void endVisit(Metavar34 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar34" + ")");
  }

  public boolean visit(Metavar34 node)
  { 
    unimplementedVisitor("visit(" + "Metavar34" + ")");
    return true;
  }

  public void endVisit(Metavar33 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar33" + ")");
  }

  public boolean visit(Metavar33 node)
  { 
    unimplementedVisitor("visit(" + "Metavar33" + ")");
    return true;
  }

  public void endVisit(Metavar32 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar32" + ")");
  }

  public boolean visit(Metavar32 node)
  { 
    unimplementedVisitor("visit(" + "Metavar32" + ")");
    return true;
  }

  public void endVisit(Metavar31 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar31" + ")");
  }

  public boolean visit(Metavar31 node)
  { 
    unimplementedVisitor("visit(" + "Metavar31" + ")");
    return true;
  }

  public void endVisit(Metavar30 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar30" + ")");
  }

  public boolean visit(Metavar30 node)
  { 
    unimplementedVisitor("visit(" + "Metavar30" + ")");
    return true;
  }

  public void endVisit(Metavar29 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar29" + ")");
  }

  public boolean visit(Metavar29 node)
  { 
    unimplementedVisitor("visit(" + "Metavar29" + ")");
    return true;
  }

  public void endVisit(Metavar28 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar28" + ")");
  }

  public boolean visit(Metavar28 node)
  { 
    unimplementedVisitor("visit(" + "Metavar28" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr66 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr66" + ")");
  }

  public boolean visit(ToMetaExpr66 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr66" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr65 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr65" + ")");
  }

  public boolean visit(ToMetaExpr65 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr65" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr64 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr64" + ")");
  }

  public boolean visit(ToMetaExpr64 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr64" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr63 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr63" + ")");
  }

  public boolean visit(ToMetaExpr63 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr63" + ")");
    return true;
  }

  public void endVisit(Metavar27 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar27" + ")");
  }

  public boolean visit(Metavar27 node)
  { 
    unimplementedVisitor("visit(" + "Metavar27" + ")");
    return true;
  }

  public void endVisit(Metavar26 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar26" + ")");
  }

  public boolean visit(Metavar26 node)
  { 
    unimplementedVisitor("visit(" + "Metavar26" + ")");
    return true;
  }

  public void endVisit(Metavar25 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar25" + ")");
  }

  public boolean visit(Metavar25 node)
  { 
    unimplementedVisitor("visit(" + "Metavar25" + ")");
    return true;
  }

  public void endVisit(Metavar24 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar24" + ")");
  }

  public boolean visit(Metavar24 node)
  { 
    unimplementedVisitor("visit(" + "Metavar24" + ")");
    return true;
  }

  public void endVisit(Metavar23 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar23" + ")");
  }

  public boolean visit(Metavar23 node)
  { 
    unimplementedVisitor("visit(" + "Metavar23" + ")");
    return true;
  }

  public void endVisit(MetaTypeVar_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "MetaTypeVar_JavaObject" + ")");
  }

  public boolean visit(MetaTypeVar_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "MetaTypeVar_JavaObject" + ")");
    return true;
  }

  public void endVisit(MetaPrimTypeVar_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "MetaPrimTypeVar_JavaObject" + ")");
  }

  public boolean visit(MetaPrimTypeVar_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "MetaPrimTypeVar_JavaObject" + ")");
    return true;
  }

  public void endVisit(MetaRefTypeVar_JavaObject node)
  { 
    unimplementedVisitor("endVisit(" + "MetaRefTypeVar_JavaObject" + ")");
  }

  public boolean visit(MetaRefTypeVar_JavaObject node)
  { 
    unimplementedVisitor("visit(" + "MetaRefTypeVar_JavaObject" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr62 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr62" + ")");
  }

  public boolean visit(ToMetaExpr62 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr62" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr61 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr61" + ")");
  }

  public boolean visit(ToMetaExpr61 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr61" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr60 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr60" + ")");
  }

  public boolean visit(ToMetaExpr60 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr60" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr59 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr59" + ")");
  }

  public boolean visit(ToMetaExpr59 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr59" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr58 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr58" + ")");
  }

  public boolean visit(ToMetaExpr58 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr58" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr57 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr57" + ")");
  }

  public boolean visit(ToMetaExpr57 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr57" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr27 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr27" + ")");
  }

  public boolean visit(FromMetaExpr27 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr27" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr26 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr26" + ")");
  }

  public boolean visit(FromMetaExpr26 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr26" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr56 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr56" + ")");
  }

  public boolean visit(ToMetaExpr56 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr56" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr55 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr55" + ")");
  }

  public boolean visit(ToMetaExpr55 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr55" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr54 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr54" + ")");
  }

  public boolean visit(ToMetaExpr54 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr54" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr53 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr53" + ")");
  }

  public boolean visit(ToMetaExpr53 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr53" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr52 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr52" + ")");
  }

  public boolean visit(ToMetaExpr52 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr52" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr51 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr51" + ")");
  }

  public boolean visit(ToMetaExpr51 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr51" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr50 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr50" + ")");
  }

  public boolean visit(ToMetaExpr50 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr50" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr49 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr49" + ")");
  }

  public boolean visit(ToMetaExpr49 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr49" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr48 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr48" + ")");
  }

  public boolean visit(ToMetaExpr48 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr48" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr47 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr47" + ")");
  }

  public boolean visit(ToMetaExpr47 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr47" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr46 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr46" + ")");
  }

  public boolean visit(ToMetaExpr46 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr46" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr45 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr45" + ")");
  }

  public boolean visit(ToMetaExpr45 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr45" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr44 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr44" + ")");
  }

  public boolean visit(ToMetaExpr44 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr44" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr43 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr43" + ")");
  }

  public boolean visit(ToMetaExpr43 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr43" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr42 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr42" + ")");
  }

  public boolean visit(ToMetaExpr42 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr42" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr41 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr41" + ")");
  }

  public boolean visit(ToMetaExpr41 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr41" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr40 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr40" + ")");
  }

  public boolean visit(ToMetaExpr40 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr40" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr39 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr39" + ")");
  }

  public boolean visit(ToMetaExpr39 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr39" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr38 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr38" + ")");
  }

  public boolean visit(ToMetaExpr38 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr38" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr37 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr37" + ")");
  }

  public boolean visit(ToMetaExpr37 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr37" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr25 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr25" + ")");
  }

  public boolean visit(FromMetaExpr25 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr25" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr36 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr36" + ")");
  }

  public boolean visit(ToMetaExpr36 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr36" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr35 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr35" + ")");
  }

  public boolean visit(ToMetaExpr35 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr35" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr34 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr34" + ")");
  }

  public boolean visit(ToMetaExpr34 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr34" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr33 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr33" + ")");
  }

  public boolean visit(ToMetaExpr33 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr33" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr32 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr32" + ")");
  }

  public boolean visit(ToMetaExpr32 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr32" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr24 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr24" + ")");
  }

  public boolean visit(FromMetaExpr24 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr24" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr23 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr23" + ")");
  }

  public boolean visit(FromMetaExpr23 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr23" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr31 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr31" + ")");
  }

  public boolean visit(ToMetaExpr31 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr31" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr30 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr30" + ")");
  }

  public boolean visit(ToMetaExpr30 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr30" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr29 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr29" + ")");
  }

  public boolean visit(ToMetaExpr29 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr29" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr1 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr1" + ")");
  }

  public boolean visit(ToMetaListExpr1 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr1" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr0 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr0" + ")");
  }

  public boolean visit(ToMetaListExpr0 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr0" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr22 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr22" + ")");
  }

  public boolean visit(FromMetaExpr22 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr22" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr28 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr28" + ")");
  }

  public boolean visit(ToMetaExpr28 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr28" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr27 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr27" + ")");
  }

  public boolean visit(ToMetaExpr27 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr27" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr26 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr26" + ")");
  }

  public boolean visit(ToMetaExpr26 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr26" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr25 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr25" + ")");
  }

  public boolean visit(ToMetaExpr25 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr25" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr24 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr24" + ")");
  }

  public boolean visit(ToMetaExpr24 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr24" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr23 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr23" + ")");
  }

  public boolean visit(ToMetaExpr23 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr23" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr22 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr22" + ")");
  }

  public boolean visit(ToMetaExpr22 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr22" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr21 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr21" + ")");
  }

  public boolean visit(ToMetaExpr21 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr21" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr20 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr20" + ")");
  }

  public boolean visit(ToMetaExpr20 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr20" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr19 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr19" + ")");
  }

  public boolean visit(ToMetaExpr19 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr19" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr18 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr18" + ")");
  }

  public boolean visit(ToMetaExpr18 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr18" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr17 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr17" + ")");
  }

  public boolean visit(ToMetaExpr17 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr17" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr16 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr16" + ")");
  }

  public boolean visit(ToMetaExpr16 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr16" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr15 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr15" + ")");
  }

  public boolean visit(ToMetaExpr15 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr15" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr14 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr14" + ")");
  }

  public boolean visit(ToMetaExpr14 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr14" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr13 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr13" + ")");
  }

  public boolean visit(ToMetaExpr13 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr13" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr12 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr12" + ")");
  }

  public boolean visit(ToMetaExpr12 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr12" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr11 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr11" + ")");
  }

  public boolean visit(ToMetaExpr11 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr11" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr10 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr10" + ")");
  }

  public boolean visit(ToMetaExpr10 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr10" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr9 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr9" + ")");
  }

  public boolean visit(ToMetaExpr9 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr9" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr8 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr8" + ")");
  }

  public boolean visit(ToMetaExpr8 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr8" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr7 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr7" + ")");
  }

  public boolean visit(ToMetaExpr7 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr7" + ")");
    return true;
  }

  public void endVisit(ToMetaListExpr node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaListExpr" + ")");
  }

  public boolean visit(ToMetaListExpr node)
  { 
    unimplementedVisitor("visit(" + "ToMetaListExpr" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr6 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr6" + ")");
  }

  public boolean visit(ToMetaExpr6 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr6" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr5 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr5" + ")");
  }

  public boolean visit(ToMetaExpr5 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr5" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr4 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr4" + ")");
  }

  public boolean visit(ToMetaExpr4 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr4" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr3 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr3" + ")");
  }

  public boolean visit(ToMetaExpr3 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr3" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr2 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr2" + ")");
  }

  public boolean visit(ToMetaExpr2 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr2" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr1 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr1" + ")");
  }

  public boolean visit(ToMetaExpr1 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr1" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr0 node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr0" + ")");
  }

  public boolean visit(ToMetaExpr0 node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr0" + ")");
    return true;
  }

  public void endVisit(ToMetaExpr node)
  { 
    unimplementedVisitor("endVisit(" + "ToMetaExpr" + ")");
  }

  public boolean visit(ToMetaExpr node)
  { 
    unimplementedVisitor("visit(" + "ToMetaExpr" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr21 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr21" + ")");
  }

  public boolean visit(FromMetaExpr21 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr21" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr20 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr20" + ")");
  }

  public boolean visit(FromMetaExpr20 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr20" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr19 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr19" + ")");
  }

  public boolean visit(FromMetaExpr19 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr19" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr18 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr18" + ")");
  }

  public boolean visit(FromMetaExpr18 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr18" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr17 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr17" + ")");
  }

  public boolean visit(FromMetaExpr17 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr17" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr16 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr16" + ")");
  }

  public boolean visit(FromMetaExpr16 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr16" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr15 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr15" + ")");
  }

  public boolean visit(FromMetaExpr15 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr15" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr14 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr14" + ")");
  }

  public boolean visit(FromMetaExpr14 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr14" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr13 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr13" + ")");
  }

  public boolean visit(FromMetaExpr13 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr13" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr12 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr12" + ")");
  }

  public boolean visit(FromMetaExpr12 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr12" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr11 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr11" + ")");
  }

  public boolean visit(FromMetaExpr11 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr11" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr10 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr10" + ")");
  }

  public boolean visit(FromMetaExpr10 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr10" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr9 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr9" + ")");
  }

  public boolean visit(FromMetaExpr9 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr9" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr8 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr8" + ")");
  }

  public boolean visit(FromMetaExpr8 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr8" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr7 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr7" + ")");
  }

  public boolean visit(FromMetaExpr7 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr7" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr6 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr6" + ")");
  }

  public boolean visit(FromMetaExpr6 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr6" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr5 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr5" + ")");
  }

  public boolean visit(FromMetaExpr5 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr5" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr4 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr4" + ")");
  }

  public boolean visit(FromMetaExpr4 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr4" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr3 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr3" + ")");
  }

  public boolean visit(FromMetaExpr3 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr3" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr2 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr2" + ")");
  }

  public boolean visit(FromMetaExpr2 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr2" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr1 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr1" + ")");
  }

  public boolean visit(FromMetaExpr1 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr1" + ")");
    return true;
  }

  public void endVisit(Metavar22 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar22" + ")");
  }

  public boolean visit(Metavar22 node)
  { 
    unimplementedVisitor("visit(" + "Metavar22" + ")");
    return true;
  }

  public void endVisit(Metavar21 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar21" + ")");
  }

  public boolean visit(Metavar21 node)
  { 
    unimplementedVisitor("visit(" + "Metavar21" + ")");
    return true;
  }

  public void endVisit(Metavar20 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar20" + ")");
  }

  public boolean visit(Metavar20 node)
  { 
    unimplementedVisitor("visit(" + "Metavar20" + ")");
    return true;
  }

  public void endVisit(Metavar19 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar19" + ")");
  }

  public boolean visit(Metavar19 node)
  { 
    unimplementedVisitor("visit(" + "Metavar19" + ")");
    return true;
  }

  public void endVisit(Metavar18 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar18" + ")");
  }

  public boolean visit(Metavar18 node)
  { 
    unimplementedVisitor("visit(" + "Metavar18" + ")");
    return true;
  }

  public void endVisit(Metavar17 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar17" + ")");
  }

  public boolean visit(Metavar17 node)
  { 
    unimplementedVisitor("visit(" + "Metavar17" + ")");
    return true;
  }

  public void endVisit(Metavar16 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar16" + ")");
  }

  public boolean visit(Metavar16 node)
  { 
    unimplementedVisitor("visit(" + "Metavar16" + ")");
    return true;
  }

  public void endVisit(Metavar15 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar15" + ")");
  }

  public boolean visit(Metavar15 node)
  { 
    unimplementedVisitor("visit(" + "Metavar15" + ")");
    return true;
  }

  public void endVisit(Metavar14 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar14" + ")");
  }

  public boolean visit(Metavar14 node)
  { 
    unimplementedVisitor("visit(" + "Metavar14" + ")");
    return true;
  }

  public void endVisit(Metavar13 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar13" + ")");
  }

  public boolean visit(Metavar13 node)
  { 
    unimplementedVisitor("visit(" + "Metavar13" + ")");
    return true;
  }

  public void endVisit(Metavar12 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar12" + ")");
  }

  public boolean visit(Metavar12 node)
  { 
    unimplementedVisitor("visit(" + "Metavar12" + ")");
    return true;
  }

  public void endVisit(Metavar11 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar11" + ")");
  }

  public boolean visit(Metavar11 node)
  { 
    unimplementedVisitor("visit(" + "Metavar11" + ")");
    return true;
  }

  public void endVisit(Metavar10 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar10" + ")");
  }

  public boolean visit(Metavar10 node)
  { 
    unimplementedVisitor("visit(" + "Metavar10" + ")");
    return true;
  }

  public void endVisit(Metavar9 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar9" + ")");
  }

  public boolean visit(Metavar9 node)
  { 
    unimplementedVisitor("visit(" + "Metavar9" + ")");
    return true;
  }

  public void endVisit(Metavar8 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar8" + ")");
  }

  public boolean visit(Metavar8 node)
  { 
    unimplementedVisitor("visit(" + "Metavar8" + ")");
    return true;
  }

  public void endVisit(Metavar7 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar7" + ")");
  }

  public boolean visit(Metavar7 node)
  { 
    unimplementedVisitor("visit(" + "Metavar7" + ")");
    return true;
  }

  public void endVisit(Metavar6 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar6" + ")");
  }

  public boolean visit(Metavar6 node)
  { 
    unimplementedVisitor("visit(" + "Metavar6" + ")");
    return true;
  }

  public void endVisit(Metavar5 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar5" + ")");
  }

  public boolean visit(Metavar5 node)
  { 
    unimplementedVisitor("visit(" + "Metavar5" + ")");
    return true;
  }

  public void endVisit(Metavar4 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar4" + ")");
  }

  public boolean visit(Metavar4 node)
  { 
    unimplementedVisitor("visit(" + "Metavar4" + ")");
    return true;
  }

  public void endVisit(Metavar3 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar3" + ")");
  }

  public boolean visit(Metavar3 node)
  { 
    unimplementedVisitor("visit(" + "Metavar3" + ")");
    return true;
  }

  public void endVisit(Metavar2 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar2" + ")");
  }

  public boolean visit(Metavar2 node)
  { 
    unimplementedVisitor("visit(" + "Metavar2" + ")");
    return true;
  }

  public void endVisit(Metavar1 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar1" + ")");
  }

  public boolean visit(Metavar1 node)
  { 
    unimplementedVisitor("visit(" + "Metavar1" + ")");
    return true;
  }

  public void endVisit(Metavar0 node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar0" + ")");
  }

  public boolean visit(Metavar0 node)
  { 
    unimplementedVisitor("visit(" + "Metavar0" + ")");
    return true;
  }

  public void endVisit(Metavar node)
  { 
    unimplementedVisitor("endVisit(" + "Metavar" + ")");
  }

  public boolean visit(Metavar node)
  { 
    unimplementedVisitor("visit(" + "Metavar" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr0 node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr0" + ")");
  }

  public boolean visit(FromMetaExpr0 node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr0" + ")");
    return true;
  }

  public void endVisit(FromMetaExpr node)
  { 
    unimplementedVisitor("endVisit(" + "FromMetaExpr" + ")");
  }

  public boolean visit(FromMetaExpr node)
  { 
    unimplementedVisitor("visit(" + "FromMetaExpr" + ")");
    return true;
  }

  public void endVisit(ListPlusOfCommChar0 node)
  { 
    unimplementedVisitor("endVisit(" + "ListPlusOfCommChar0" + ")");
  }

  public boolean visit(ListPlusOfCommChar0 node)
  { 
    unimplementedVisitor("visit(" + "ListPlusOfCommChar0" + ")");
    return true;
  }

  public void endVisit(ListStarOfCommChar0 node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfCommChar0" + ")");
  }

  public boolean visit(ListStarOfCommChar0 node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfCommChar0" + ")");
    return true;
  }

  public void endVisit(ListPlusOfModNamePart_StrategoHost0 node)
  { 
    unimplementedVisitor("endVisit(" + "ListPlusOfModNamePart_StrategoHost0" + ")");
  }

  public boolean visit(ListPlusOfModNamePart_StrategoHost0 node)
  { 
    unimplementedVisitor("visit(" + "ListPlusOfModNamePart_StrategoHost0" + ")");
    return true;
  }

  public void endVisit(ListStarOfModNamePart_StrategoHost0 node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfModNamePart_StrategoHost0" + ")");
  }

  public boolean visit(ListStarOfModNamePart_StrategoHost0 node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfModNamePart_StrategoHost0" + ")");
    return true;
  }

  public void endVisit(ListPlusOfStrChar_StrategoHost0 node)
  { 
    unimplementedVisitor("endVisit(" + "ListPlusOfStrChar_StrategoHost0" + ")");
  }

  public boolean visit(ListPlusOfStrChar_StrategoHost0 node)
  { 
    unimplementedVisitor("visit(" + "ListPlusOfStrChar_StrategoHost0" + ")");
    return true;
  }

  public void endVisit(ListStarOfStrChar_StrategoHost0 node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfStrChar_StrategoHost0" + ")");
  }

  public boolean visit(ListStarOfStrChar_StrategoHost0 node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfStrChar_StrategoHost0" + ")");
    return true;
  }

  public void endVisit(ListPlusOfCommentPart0 node)
  { 
    unimplementedVisitor("endVisit(" + "ListPlusOfCommentPart0" + ")");
  }

  public boolean visit(ListPlusOfCommentPart0 node)
  { 
    unimplementedVisitor("visit(" + "ListPlusOfCommentPart0" + ")");
    return true;
  }

  public void endVisit(ListStarOfCommentPart0 node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfCommentPart0" + ")");
  }

  public boolean visit(ListStarOfCommentPart0 node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfCommentPart0" + ")");
    return true;
  }

  public void endVisit(OptDeciFloatExponentPart_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptDeciFloatExponentPart_JavaObject0" + ")");
  }

  public boolean visit(OptDeciFloatExponentPart_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptDeciFloatExponentPart_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptWildcardBound_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptWildcardBound_JavaObject0" + ")");
  }

  public boolean visit(OptWildcardBound_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptWildcardBound_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptTypeBound_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptTypeBound_JavaObject0" + ")");
  }

  public boolean visit(OptTypeBound_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptTypeBound_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptId_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptId_JavaObject0" + ")");
  }

  public boolean visit(OptId_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptId_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptExpr_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptExpr_JavaObject0" + ")");
  }

  public boolean visit(OptExpr_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptExpr_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptConstrInv_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptConstrInv_JavaObject0" + ")");
  }

  public boolean visit(OptConstrInv_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptConstrInv_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptEnumBodyDecs_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptEnumBodyDecs_JavaObject0" + ")");
  }

  public boolean visit(OptEnumBodyDecs_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptEnumBodyDecs_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptEnumConstArgs_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptEnumConstArgs_JavaObject0" + ")");
  }

  public boolean visit(OptEnumConstArgs_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptEnumConstArgs_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ConstantMod_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ConstantMod_JavaObject00" + ")");
  }

  public boolean visit(Anno_JavaObject_ConstantMod_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ConstantMod_JavaObject00" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ConstantMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ConstantMod_JavaObject0" + ")");
  }

  public boolean visit(Anno_JavaObject_ConstantMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ConstantMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_AbstractMethodMod_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_AbstractMethodMod_JavaObject00" + ")");
  }

  public boolean visit(Anno_JavaObject_AbstractMethodMod_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_AbstractMethodMod_JavaObject00" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_AbstractMethodMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_AbstractMethodMod_JavaObject0" + ")");
  }

  public boolean visit(Anno_JavaObject_AbstractMethodMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_AbstractMethodMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptThrows_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptThrows_JavaObject0" + ")");
  }

  public boolean visit(OptThrows_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptThrows_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptDefaultVal_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptDefaultVal_JavaObject0" + ")");
  }

  public boolean visit(OptDefaultVal_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptDefaultVal_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_InterfaceMod_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_InterfaceMod_JavaObject00" + ")");
  }

  public boolean visit(Anno_JavaObject_InterfaceMod_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_InterfaceMod_JavaObject00" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_InterfaceMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_InterfaceMod_JavaObject0" + ")");
  }

  public boolean visit(Anno_JavaObject_InterfaceMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_InterfaceMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptExtendsInterfaces_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptExtendsInterfaces_JavaObject0" + ")");
  }

  public boolean visit(OptExtendsInterfaces_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptExtendsInterfaces_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptTypeParams_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptTypeParams_JavaObject0" + ")");
  }

  public boolean visit(OptTypeParams_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptTypeParams_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptSuper_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptSuper_JavaObject0" + ")");
  }

  public boolean visit(OptSuper_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptSuper_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptInterfaces_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptInterfaces_JavaObject0" + ")");
  }

  public boolean visit(OptInterfaces_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptInterfaces_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptTypeArgs_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "OptTypeArgs_JavaObject00" + ")");
  }

  public boolean visit(OptTypeArgs_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "OptTypeArgs_JavaObject00" + ")");
    return true;
  }

  public void endVisit(OptPackageDec_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptPackageDec_JavaObject0" + ")");
  }

  public boolean visit(OptPackageDec_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptPackageDec_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_MethodMod_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_MethodMod_JavaObject00" + ")");
  }

  public boolean visit(Anno_JavaObject_MethodMod_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_MethodMod_JavaObject00" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_MethodMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_MethodMod_JavaObject0" + ")");
  }

  public boolean visit(Anno_JavaObject_MethodMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_MethodMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ClassMod_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ClassMod_JavaObject00" + ")");
  }

  public boolean visit(Anno_JavaObject_ClassMod_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ClassMod_JavaObject00" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ClassMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ClassMod_JavaObject0" + ")");
  }

  public boolean visit(Anno_JavaObject_ClassMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ClassMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ConstrMod_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ConstrMod_JavaObject00" + ")");
  }

  public boolean visit(Anno_JavaObject_ConstrMod_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ConstrMod_JavaObject00" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ConstrMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ConstrMod_JavaObject0" + ")");
  }

  public boolean visit(Anno_JavaObject_ConstrMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ConstrMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_VarMod_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_VarMod_JavaObject00" + ")");
  }

  public boolean visit(Anno_JavaObject_VarMod_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_VarMod_JavaObject00" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_VarMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_VarMod_JavaObject0" + ")");
  }

  public boolean visit(Anno_JavaObject_VarMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_VarMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_FieldMod_JavaObject00 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_FieldMod_JavaObject00" + ")");
  }

  public boolean visit(Anno_JavaObject_FieldMod_JavaObject00 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_FieldMod_JavaObject00" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_FieldMod_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_FieldMod_JavaObject0" + ")");
  }

  public boolean visit(Anno_JavaObject_FieldMod_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_FieldMod_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptClassBody_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptClassBody_JavaObject0" + ")");
  }

  public boolean visit(OptClassBody_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptClassBody_JavaObject0" + ")");
    return true;
  }

  public void endVisit(OptTypeArgs_JavaObject0 node)
  { 
    unimplementedVisitor("endVisit(" + "OptTypeArgs_JavaObject0" + ")");
  }

  public boolean visit(OptTypeArgs_JavaObject0 node)
  { 
    unimplementedVisitor("visit(" + "OptTypeArgs_JavaObject0" + ")");
    return true;
  }

  public void endVisit(WsSort node)
  { 
    unimplementedVisitor("endVisit(" + "WsSort" + ")");
  }

  public boolean visit(WsSort node)
  { 
    unimplementedVisitor("visit(" + "WsSort" + ")");
    return true;
  }

  public void endVisit(ShortComSort node)
  { 
    unimplementedVisitor("endVisit(" + "ShortComSort" + ")");
  }

  public boolean visit(ShortComSort node)
  { 
    unimplementedVisitor("visit(" + "ShortComSort" + ")");
    return true;
  }

  public void endVisit(LongComSort node)
  { 
    unimplementedVisitor("endVisit(" + "LongComSort" + ")");
  }

  public boolean visit(LongComSort node)
  { 
    unimplementedVisitor("visit(" + "LongComSort" + ")");
    return true;
  }

  public void endVisit(EofSort node)
  { 
    unimplementedVisitor("endVisit(" + "EofSort" + ")");
  }

  public boolean visit(EofSort node)
  { 
    unimplementedVisitor("visit(" + "EofSort" + ")");
    return true;
  }

  public void endVisit(CommCharSort node)
  { 
    unimplementedVisitor("endVisit(" + "CommCharSort" + ")");
  }

  public boolean visit(CommCharSort node)
  { 
    unimplementedVisitor("visit(" + "CommCharSort" + ")");
    return true;
  }

  public void endVisit(AsteriskSort node)
  { 
    unimplementedVisitor("endVisit(" + "AsteriskSort" + ")");
  }

  public boolean visit(AsteriskSort node)
  { 
    unimplementedVisitor("visit(" + "AsteriskSort" + ")");
    return true;
  }

  public void endVisit(ModName_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "ModName_StrategoHostSort" + ")");
  }

  public boolean visit(ModName_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "ModName_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(ModNamePart_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "ModNamePart_StrategoHostSort" + ")");
  }

  public boolean visit(ModNamePart_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "ModNamePart_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Id_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Id_StrategoHostSort" + ")");
  }

  public boolean visit(Id_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Id_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(LId_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "LId_StrategoHostSort" + ")");
  }

  public boolean visit(LId_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "LId_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(LCID_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "LCID_StrategoHostSort" + ")");
  }

  public boolean visit(LCID_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "LCID_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(UCID_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "UCID_StrategoHostSort" + ")");
  }

  public boolean visit(UCID_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "UCID_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Keyword_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Keyword_StrategoHostSort" + ")");
  }

  public boolean visit(Keyword_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Keyword_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Int_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Int_StrategoHostSort" + ")");
  }

  public boolean visit(Int_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Int_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Real_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Real_StrategoHostSort" + ")");
  }

  public boolean visit(Real_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Real_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(String_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "String_StrategoHostSort" + ")");
  }

  public boolean visit(String_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "String_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(StrChar_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "StrChar_StrategoHostSort" + ")");
  }

  public boolean visit(StrChar_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "StrChar_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Char_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Char_StrategoHostSort" + ")");
  }

  public boolean visit(Char_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Char_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(CharChar_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "CharChar_StrategoHostSort" + ")");
  }

  public boolean visit(CharChar_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "CharChar_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Sdecl_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Sdecl_StrategoHostSort" + ")");
  }

  public boolean visit(Sdecl_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Sdecl_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Opdecl_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Opdecl_StrategoHostSort" + ")");
  }

  public boolean visit(Opdecl_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Opdecl_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(ConstType_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "ConstType_StrategoHostSort" + ")");
  }

  public boolean visit(ConstType_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "ConstType_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(FunType_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "FunType_StrategoHostSort" + ")");
  }

  public boolean visit(FunType_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "FunType_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(ArgTypeSort node)
  { 
    unimplementedVisitor("endVisit(" + "ArgTypeSort" + ")");
  }

  public boolean visit(ArgTypeSort node)
  { 
    unimplementedVisitor("visit(" + "ArgTypeSort" + ")");
    return true;
  }

  public void endVisit(ArgType_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "ArgType_StrategoHostSort" + ")");
  }

  public boolean visit(ArgType_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "ArgType_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(RetType_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "RetType_StrategoHostSort" + ")");
  }

  public boolean visit(RetType_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "RetType_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Type_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Type_StrategoHostSort" + ")");
  }

  public boolean visit(Type_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Type_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(SVar_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "SVar_StrategoHostSort" + ")");
  }

  public boolean visit(SVar_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "SVar_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(StrategyParen_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "StrategyParen_StrategoHostSort" + ")");
  }

  public boolean visit(StrategyParen_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "StrategyParen_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(StrategyMid_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "StrategyMid_StrategoHostSort" + ")");
  }

  public boolean visit(StrategyMid_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "StrategyMid_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Module_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Module_StrategoHostSort" + ")");
  }

  public boolean visit(Module_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Module_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(ImportModName_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "ImportModName_StrategoHostSort" + ")");
  }

  public boolean visit(ImportModName_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "ImportModName_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(LID_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "LID_StrategoHostSort" + ")");
  }

  public boolean visit(LID_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "LID_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Var_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Var_StrategoHostSort" + ")");
  }

  public boolean visit(Var_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Var_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(ID_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "ID_StrategoHostSort" + ")");
  }

  public boolean visit(ID_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "ID_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(PreTerm_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "PreTerm_StrategoHostSort" + ")");
  }

  public boolean visit(PreTerm_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "PreTerm_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Sort_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Sort_StrategoHostSort" + ")");
  }

  public boolean visit(Sort_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Sort_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Kind_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Kind_StrategoHostSort" + ")");
  }

  public boolean visit(Kind_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Kind_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(StrategyDef_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "StrategyDef_StrategoHostSort" + ")");
  }

  public boolean visit(StrategyDef_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "StrategyDef_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Typedid_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Typedid_StrategoHostSort" + ")");
  }

  public boolean visit(Typedid_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Typedid_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(StrategyAngleSort node)
  { 
    unimplementedVisitor("endVisit(" + "StrategyAngleSort" + ")");
  }

  public boolean visit(StrategyAngleSort node)
  { 
    unimplementedVisitor("visit(" + "StrategyAngleSort" + ")");
    return true;
  }

  public void endVisit(StrategyCurly_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "StrategyCurly_StrategoHostSort" + ")");
  }

  public boolean visit(StrategyCurly_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "StrategyCurly_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(StrategySort node)
  { 
    unimplementedVisitor("endVisit(" + "StrategySort" + ")");
  }

  public boolean visit(StrategySort node)
  { 
    unimplementedVisitor("visit(" + "StrategySort" + ")");
    return true;
  }

  public void endVisit(SwitchCase_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchCase_StrategoHostSort" + ")");
  }

  public boolean visit(SwitchCase_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "SwitchCase_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Overlay_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Overlay_StrategoHostSort" + ")");
  }

  public boolean visit(Overlay_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Overlay_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(RuleDef_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "RuleDef_StrategoHostSort" + ")");
  }

  public boolean visit(RuleDef_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "RuleDef_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Rule_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Rule_StrategoHostSort" + ")");
  }

  public boolean visit(Rule_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Rule_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Decl_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Decl_StrategoHostSort" + ")");
  }

  public boolean visit(Decl_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Decl_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Def_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Def_StrategoHostSort" + ")");
  }

  public boolean visit(Def_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Def_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(ScopeLabels_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "ScopeLabels_StrategoHostSort" + ")");
  }

  public boolean visit(ScopeLabels_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "ScopeLabels_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(DynRuleDef_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleDef_StrategoHostSort" + ")");
  }

  public boolean visit(DynRuleDef_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "DynRuleDef_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(DynRuleId_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleId_StrategoHostSort" + ")");
  }

  public boolean visit(DynRuleId_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "DynRuleId_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(DynRuleScopeId_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "DynRuleScopeId_StrategoHostSort" + ")");
  }

  public boolean visit(DynRuleScopeId_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "DynRuleScopeId_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(RuleDec_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "RuleDec_StrategoHostSort" + ")");
  }

  public boolean visit(RuleDec_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "RuleDec_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(RuleNames_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "RuleNames_StrategoHostSort" + ")");
  }

  public boolean visit(RuleNames_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "RuleNames_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Strategy_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Strategy_StrategoHostSort" + ")");
  }

  public boolean visit(Strategy_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Strategy_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(LineTerminatorSort node)
  { 
    unimplementedVisitor("endVisit(" + "LineTerminatorSort" + ")");
  }

  public boolean visit(LineTerminatorSort node)
  { 
    unimplementedVisitor("visit(" + "LineTerminatorSort" + ")");
    return true;
  }

  public void endVisit(CarriageReturnSort node)
  { 
    unimplementedVisitor("endVisit(" + "CarriageReturnSort" + ")");
  }

  public boolean visit(CarriageReturnSort node)
  { 
    unimplementedVisitor("visit(" + "CarriageReturnSort" + ")");
    return true;
  }

  public void endVisit(EndOfFileSort node)
  { 
    unimplementedVisitor("endVisit(" + "EndOfFileSort" + ")");
  }

  public boolean visit(EndOfFileSort node)
  { 
    unimplementedVisitor("visit(" + "EndOfFileSort" + ")");
    return true;
  }

  public void endVisit(CommentSort node)
  { 
    unimplementedVisitor("endVisit(" + "CommentSort" + ")");
  }

  public boolean visit(CommentSort node)
  { 
    unimplementedVisitor("visit(" + "CommentSort" + ")");
    return true;
  }

  public void endVisit(EOLCommentCharsSort node)
  { 
    unimplementedVisitor("endVisit(" + "EOLCommentCharsSort" + ")");
  }

  public boolean visit(EOLCommentCharsSort node)
  { 
    unimplementedVisitor("visit(" + "EOLCommentCharsSort" + ")");
    return true;
  }

  public void endVisit(CommentPartSort node)
  { 
    unimplementedVisitor("endVisit(" + "CommentPartSort" + ")");
  }

  public boolean visit(CommentPartSort node)
  { 
    unimplementedVisitor("visit(" + "CommentPartSort" + ")");
    return true;
  }

  public void endVisit(BlockCommentCharsSort node)
  { 
    unimplementedVisitor("endVisit(" + "BlockCommentCharsSort" + ")");
  }

  public boolean visit(BlockCommentCharsSort node)
  { 
    unimplementedVisitor("visit(" + "BlockCommentCharsSort" + ")");
    return true;
  }

  public void endVisit(EscEscCharSort node)
  { 
    unimplementedVisitor("endVisit(" + "EscEscCharSort" + ")");
  }

  public boolean visit(EscEscCharSort node)
  { 
    unimplementedVisitor("visit(" + "EscEscCharSort" + ")");
    return true;
  }

  public void endVisit(EscCharSort node)
  { 
    unimplementedVisitor("endVisit(" + "EscCharSort" + ")");
  }

  public boolean visit(EscCharSort node)
  { 
    unimplementedVisitor("visit(" + "EscCharSort" + ")");
    return true;
  }

  public void endVisit(UnicodeEscapeSort node)
  { 
    unimplementedVisitor("endVisit(" + "UnicodeEscapeSort" + ")");
  }

  public boolean visit(UnicodeEscapeSort node)
  { 
    unimplementedVisitor("visit(" + "UnicodeEscapeSort" + ")");
    return true;
  }

  public void endVisit(Keyword_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Keyword_JavaObjectSort" + ")");
  }

  public boolean visit(Keyword_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Keyword_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Public_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Public_JavaObjectSort" + ")");
  }

  public boolean visit(Public_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Public_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Private_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Private_JavaObjectSort" + ")");
  }

  public boolean visit(Private_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Private_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Protected_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Protected_JavaObjectSort" + ")");
  }

  public boolean visit(Protected_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Protected_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Abstract_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Abstract_JavaObjectSort" + ")");
  }

  public boolean visit(Abstract_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Abstract_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Final_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Final_JavaObjectSort" + ")");
  }

  public boolean visit(Final_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Final_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Static_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Static_JavaObjectSort" + ")");
  }

  public boolean visit(Static_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Static_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Native_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Native_JavaObjectSort" + ")");
  }

  public boolean visit(Native_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Native_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Transient_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Transient_JavaObjectSort" + ")");
  }

  public boolean visit(Transient_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Transient_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Volatile_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Volatile_JavaObjectSort" + ")");
  }

  public boolean visit(Volatile_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Volatile_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Synchronized_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Synchronized_JavaObjectSort" + ")");
  }

  public boolean visit(Synchronized_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Synchronized_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(StrictFP_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "StrictFP_JavaObjectSort" + ")");
  }

  public boolean visit(StrictFP_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "StrictFP_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Modifier_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Modifier_JavaObjectSort" + ")");
  }

  public boolean visit(Modifier_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Modifier_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(DeciNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "DeciNumeral_JavaObjectSort" + ")");
  }

  public boolean visit(DeciNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "DeciNumeral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(HexaNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "HexaNumeral_JavaObjectSort" + ")");
  }

  public boolean visit(HexaNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "HexaNumeral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(OctaNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "OctaNumeral_JavaObjectSort" + ")");
  }

  public boolean visit(OctaNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "OctaNumeral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(IntLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "IntLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(IntLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "IntLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(DeciFloatLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "DeciFloatLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(DeciFloatLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "DeciFloatLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(HexaFloatLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "HexaFloatLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(HexaFloatLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "HexaFloatLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(DeciFloatNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "DeciFloatNumeral_JavaObjectSort" + ")");
  }

  public boolean visit(DeciFloatNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "DeciFloatNumeral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(DeciFloatDigits_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "DeciFloatDigits_JavaObjectSort" + ")");
  }

  public boolean visit(DeciFloatDigits_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "DeciFloatDigits_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(DeciFloatExponentPart_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "DeciFloatExponentPart_JavaObjectSort" + ")");
  }

  public boolean visit(DeciFloatExponentPart_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "DeciFloatExponentPart_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(SignedInteger_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "SignedInteger_JavaObjectSort" + ")");
  }

  public boolean visit(SignedInteger_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "SignedInteger_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(HexaFloatNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "HexaFloatNumeral_JavaObjectSort" + ")");
  }

  public boolean visit(HexaFloatNumeral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "HexaFloatNumeral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(HexaSignificand_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "HexaSignificand_JavaObjectSort" + ")");
  }

  public boolean visit(HexaSignificand_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "HexaSignificand_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(BinaryExponent_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "BinaryExponent_JavaObjectSort" + ")");
  }

  public boolean visit(BinaryExponent_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "BinaryExponent_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(BoolLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "BoolLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(BoolLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "BoolLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Bool_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Bool_JavaObjectSort" + ")");
  }

  public boolean visit(Bool_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Bool_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(EscapeSeq_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "EscapeSeq_JavaObjectSort" + ")");
  }

  public boolean visit(EscapeSeq_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "EscapeSeq_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(NamedEscape_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "NamedEscape_JavaObjectSort" + ")");
  }

  public boolean visit(NamedEscape_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "NamedEscape_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(OctaEscape_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "OctaEscape_JavaObjectSort" + ")");
  }

  public boolean visit(OctaEscape_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "OctaEscape_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(LastOcta_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "LastOcta_JavaObjectSort" + ")");
  }

  public boolean visit(LastOcta_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "LastOcta_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(CharContent_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "CharContent_JavaObjectSort" + ")");
  }

  public boolean visit(CharContent_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "CharContent_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(StringPart_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "StringPart_JavaObjectSort" + ")");
  }

  public boolean visit(StringPart_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "StringPart_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(NullLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "NullLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(NullLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "NullLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(NumType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "NumType_JavaObjectSort" + ")");
  }

  public boolean visit(NumType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "NumType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(IntType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "IntType_JavaObjectSort" + ")");
  }

  public boolean visit(IntType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "IntType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(FloatType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "FloatType_JavaObjectSort" + ")");
  }

  public boolean visit(FloatType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "FloatType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeArgs_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeArgs_JavaObjectSort" + ")");
  }

  public boolean visit(TypeArgs_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeArgs_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ActualTypeArg_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ActualTypeArg_JavaObjectSort" + ")");
  }

  public boolean visit(ActualTypeArg_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ActualTypeArg_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(WildcardBound_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "WildcardBound_JavaObjectSort" + ")");
  }

  public boolean visit(WildcardBound_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "WildcardBound_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeBound_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeBound_JavaObjectSort" + ")");
  }

  public boolean visit(TypeBound_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeBound_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeParams_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeParams_JavaObjectSort" + ")");
  }

  public boolean visit(TypeParams_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeParams_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeVarId_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeVarId_JavaObjectSort" + ")");
  }

  public boolean visit(TypeVarId_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeVarId_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassOrInterfaceType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassOrInterfaceType_JavaObjectSort" + ")");
  }

  public boolean visit(ClassOrInterfaceType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassOrInterfaceType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassType_JavaObjectSort" + ")");
  }

  public boolean visit(ClassType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(InterfaceType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceType_JavaObjectSort" + ")");
  }

  public boolean visit(InterfaceType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "InterfaceType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeDecSpec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeDecSpec_JavaObjectSort" + ")");
  }

  public boolean visit(TypeDecSpec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeDecSpec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeVar_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeVar_JavaObjectSort" + ")");
  }

  public boolean visit(TypeVar_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeVar_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ArrayType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayType_JavaObjectSort" + ")");
  }

  public boolean visit(ArrayType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ArrayType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Literal_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Literal_JavaObjectSort" + ")");
  }

  public boolean visit(Literal_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Literal_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(ClassLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ExprSort node)
  { 
    unimplementedVisitor("endVisit(" + "ExprSort" + ")");
  }

  public boolean visit(ExprSort node)
  { 
    unimplementedVisitor("visit(" + "ExprSort" + ")");
    return true;
  }

  public void endVisit(ArrayInit_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayInit_JavaObjectSort" + ")");
  }

  public boolean visit(ArrayInit_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ArrayInit_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(FieldDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "FieldDec_JavaObjectSort" + ")");
  }

  public boolean visit(FieldDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "FieldDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(VarDecId_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "VarDecId_JavaObjectSort" + ")");
  }

  public boolean visit(VarDecId_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "VarDecId_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Dim_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Dim_JavaObjectSort" + ")");
  }

  public boolean visit(Dim_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Dim_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(VarInit_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "VarInit_JavaObjectSort" + ")");
  }

  public boolean visit(VarInit_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "VarInit_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(LocalVarDecStm_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "LocalVarDecStm_JavaObjectSort" + ")");
  }

  public boolean visit(LocalVarDecStm_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "LocalVarDecStm_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(SwitchBlock_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchBlock_JavaObjectSort" + ")");
  }

  public boolean visit(SwitchBlock_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "SwitchBlock_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(SwitchLabel_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchLabel_JavaObjectSort" + ")");
  }

  public boolean visit(SwitchLabel_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "SwitchLabel_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(CatchClause_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "CatchClause_JavaObjectSort" + ")");
  }

  public boolean visit(CatchClause_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "CatchClause_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Block_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Block_JavaObjectSort" + ")");
  }

  public boolean visit(Block_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Block_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(MethodDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MethodDec_JavaObjectSort" + ")");
  }

  public boolean visit(MethodDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MethodDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(MethodDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MethodDecHead_JavaObjectSort" + ")");
  }

  public boolean visit(MethodDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MethodDecHead_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ResultType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ResultType_JavaObjectSort" + ")");
  }

  public boolean visit(ResultType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ResultType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Throws_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Throws_JavaObjectSort" + ")");
  }

  public boolean visit(Throws_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Throws_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ExceptionType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ExceptionType_JavaObjectSort" + ")");
  }

  public boolean visit(ExceptionType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ExceptionType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(MethodBody_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MethodBody_JavaObjectSort" + ")");
  }

  public boolean visit(MethodBody_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MethodBody_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(InstanceInit_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "InstanceInit_JavaObjectSort" + ")");
  }

  public boolean visit(InstanceInit_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "InstanceInit_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(StaticInit_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "StaticInit_JavaObjectSort" + ")");
  }

  public boolean visit(StaticInit_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "StaticInit_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ConstrDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrDec_JavaObjectSort" + ")");
  }

  public boolean visit(ConstrDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ConstrDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ConstrHead_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrHead_JavaObjectSort" + ")");
  }

  public boolean visit(ConstrHead_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ConstrHead_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ConstrBody_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrBody_JavaObjectSort" + ")");
  }

  public boolean visit(ConstrBody_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ConstrBody_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ConstrInv_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrInv_JavaObjectSort" + ")");
  }

  public boolean visit(ConstrInv_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ConstrInv_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(EnumDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "EnumDec_JavaObjectSort" + ")");
  }

  public boolean visit(EnumDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "EnumDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(EnumDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "EnumDecHead_JavaObjectSort" + ")");
  }

  public boolean visit(EnumDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "EnumDecHead_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(EnumBody_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "EnumBody_JavaObjectSort" + ")");
  }

  public boolean visit(EnumBody_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "EnumBody_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(EnumConst_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "EnumConst_JavaObjectSort" + ")");
  }

  public boolean visit(EnumConst_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "EnumConst_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(EnumConstArgsSort node)
  { 
    unimplementedVisitor("endVisit(" + "EnumConstArgsSort" + ")");
  }

  public boolean visit(EnumConstArgsSort node)
  { 
    unimplementedVisitor("visit(" + "EnumConstArgsSort" + ")");
    return true;
  }

  public void endVisit(EnumBodyDecs_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "EnumBodyDecs_JavaObjectSort" + ")");
  }

  public boolean visit(EnumBodyDecs_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "EnumBodyDecs_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ConstantDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ConstantDec_JavaObjectSort" + ")");
  }

  public boolean visit(ConstantDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ConstantDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ConstantMod_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ConstantMod_JavaObjectSort" + ")");
  }

  public boolean visit(ConstantMod_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ConstantMod_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(AbstractMethodDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "AbstractMethodDec_JavaObjectSort" + ")");
  }

  public boolean visit(AbstractMethodDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "AbstractMethodDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(AbstractMethodMod_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "AbstractMethodMod_JavaObjectSort" + ")");
  }

  public boolean visit(AbstractMethodMod_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "AbstractMethodMod_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(AnnoDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoDec_JavaObjectSort" + ")");
  }

  public boolean visit(AnnoDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "AnnoDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(AnnoDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoDecHead_JavaObjectSort" + ")");
  }

  public boolean visit(AnnoDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "AnnoDecHead_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(AnnoElemDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "AnnoElemDec_JavaObjectSort" + ")");
  }

  public boolean visit(AnnoElemDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "AnnoElemDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(DefaultVal_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "DefaultVal_JavaObjectSort" + ")");
  }

  public boolean visit(DefaultVal_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "DefaultVal_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(InterfaceDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceDec_JavaObjectSort" + ")");
  }

  public boolean visit(InterfaceDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "InterfaceDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(InterfaceDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceDecHead_JavaObjectSort" + ")");
  }

  public boolean visit(InterfaceDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "InterfaceDecHead_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ExtendsInterfaces_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ExtendsInterfaces_JavaObjectSort" + ")");
  }

  public boolean visit(ExtendsInterfaces_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ExtendsInterfaces_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(InterfaceMod_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMod_JavaObjectSort" + ")");
  }

  public boolean visit(InterfaceMod_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMod_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassDec_JavaObjectSort" + ")");
  }

  public boolean visit(ClassDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassBody_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassBody_JavaObjectSort" + ")");
  }

  public boolean visit(ClassBody_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassBody_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassDecHead_JavaObjectSort" + ")");
  }

  public boolean visit(ClassDecHead_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassDecHead_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Super_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Super_JavaObjectSort" + ")");
  }

  public boolean visit(Super_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Super_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Interfaces_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Interfaces_JavaObjectSort" + ")");
  }

  public boolean visit(Interfaces_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Interfaces_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassMemberDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMemberDec_JavaObjectSort" + ")");
  }

  public boolean visit(ClassMemberDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassMemberDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ArrayCreationExpr_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayCreationExpr_JavaObjectSort" + ")");
  }

  public boolean visit(ArrayCreationExpr_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ArrayCreationExpr_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ArrayBaseType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayBaseType_JavaObjectSort" + ")");
  }

  public boolean visit(ArrayBaseType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ArrayBaseType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(DimExpr_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "DimExpr_JavaObjectSort" + ")");
  }

  public boolean visit(DimExpr_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "DimExpr_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(FieldAccess_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "FieldAccess_JavaObjectSort" + ")");
  }

  public boolean visit(FieldAccess_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "FieldAccess_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ArrayAccess_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ArrayAccess_JavaObjectSort" + ")");
  }

  public boolean visit(ArrayAccess_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ArrayAccess_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ArraySubscriptSort node)
  { 
    unimplementedVisitor("endVisit(" + "ArraySubscriptSort" + ")");
  }

  public boolean visit(ArraySubscriptSort node)
  { 
    unimplementedVisitor("visit(" + "ArraySubscriptSort" + ")");
    return true;
  }

  public void endVisit(MethodSpec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MethodSpec_JavaObjectSort" + ")");
  }

  public boolean visit(MethodSpec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MethodSpec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(CondMidSort node)
  { 
    unimplementedVisitor("endVisit(" + "CondMidSort" + ")");
  }

  public boolean visit(CondMidSort node)
  { 
    unimplementedVisitor("visit(" + "CondMidSort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObjectSort" + ")");
  }

  public boolean visit(Anno_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ElemValPair_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ElemValPair_JavaObjectSort" + ")");
  }

  public boolean visit(ElemValPair_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ElemValPair_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ElemVal_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ElemVal_JavaObjectSort" + ")");
  }

  public boolean visit(ElemVal_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ElemVal_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(CompilationUnit_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "CompilationUnit_JavaObjectSort" + ")");
  }

  public boolean visit(CompilationUnit_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "CompilationUnit_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(SwitchGroup_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "SwitchGroup_JavaObjectSort" + ")");
  }

  public boolean visit(SwitchGroup_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "SwitchGroup_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Stm_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Stm_JavaObjectSort" + ")");
  }

  public boolean visit(Stm_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Stm_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(BlockStm_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "BlockStm_JavaObjectSort" + ")");
  }

  public boolean visit(BlockStm_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "BlockStm_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(LocalVarDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "LocalVarDec_JavaObjectSort" + ")");
  }

  public boolean visit(LocalVarDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "LocalVarDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(VarDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "VarDec_JavaObjectSort" + ")");
  }

  public boolean visit(VarDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "VarDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(LHS_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "LHS_JavaObjectSort" + ")");
  }

  public boolean visit(LHS_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "LHS_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(PrimType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "PrimType_JavaObjectSort" + ")");
  }

  public boolean visit(PrimType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "PrimType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(RefType_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "RefType_JavaObjectSort" + ")");
  }

  public boolean visit(RefType_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "RefType_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(MetaTypeVar_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MetaTypeVar_JavaObjectSort" + ")");
  }

  public boolean visit(MetaTypeVar_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MetaTypeVar_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(MetaPrimTypeVar_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MetaPrimTypeVar_JavaObjectSort" + ")");
  }

  public boolean visit(MetaPrimTypeVar_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MetaPrimTypeVar_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(MetaRefTypeVar_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MetaRefTypeVar_JavaObjectSort" + ")");
  }

  public boolean visit(MetaRefTypeVar_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MetaRefTypeVar_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Type_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Type_JavaObjectSort" + ")");
  }

  public boolean visit(Type_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Type_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(PackageDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "PackageDec_JavaObjectSort" + ")");
  }

  public boolean visit(PackageDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "PackageDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(OptPackageDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "OptPackageDec_JavaObject0Sort" + ")");
  }

  public boolean visit(OptPackageDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "OptPackageDec_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ImportDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ImportDec_JavaObjectSort" + ")");
  }

  public boolean visit(ImportDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ImportDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Term_StrategoHostSort node)
  { 
    unimplementedVisitor("endVisit(" + "Term_StrategoHostSort" + ")");
  }

  public boolean visit(Term_StrategoHostSort node)
  { 
    unimplementedVisitor("visit(" + "Term_StrategoHostSort" + ")");
    return true;
  }

  public void endVisit(Name_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Name_JavaObjectSort" + ")");
  }

  public boolean visit(Name_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Name_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Id_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Id_JavaObjectSort" + ")");
  }

  public boolean visit(Id_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Id_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ID_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ID_JavaObjectSort" + ")");
  }

  public boolean visit(ID_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ID_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeParam_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeParam_JavaObjectSort" + ")");
  }

  public boolean visit(TypeParam_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeParam_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(Expr_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "Expr_JavaObjectSort" + ")");
  }

  public boolean visit(Expr_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "Expr_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeDec_JavaObjectSort" + ")");
  }

  public boolean visit(TypeDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassBodyDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassBodyDec_JavaObjectSort" + ")");
  }

  public boolean visit(ClassBodyDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassBodyDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(InterfaceMemberDec_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "InterfaceMemberDec_JavaObjectSort" + ")");
  }

  public boolean visit(InterfaceMemberDec_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "InterfaceMemberDec_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(DeciLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "DeciLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(DeciLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "DeciLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(HexaLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "HexaLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(HexaLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "HexaLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(OctaLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "OctaLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(OctaLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "OctaLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(FloatLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "FloatLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(FloatLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "FloatLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(StringLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "StringLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(StringLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "StringLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(CharLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "CharLiteral_JavaObjectSort" + ")");
  }

  public boolean visit(CharLiteral_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "CharLiteral_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(AmbName_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "AmbName_JavaObjectSort" + ")");
  }

  public boolean visit(AmbName_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "AmbName_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ExprName_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ExprName_JavaObjectSort" + ")");
  }

  public boolean visit(ExprName_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ExprName_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(MethodName_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MethodName_JavaObjectSort" + ")");
  }

  public boolean visit(MethodName_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MethodName_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(TypeName_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "TypeName_JavaObjectSort" + ")");
  }

  public boolean visit(TypeName_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "TypeName_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(PackageOrTypeName_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "PackageOrTypeName_JavaObjectSort" + ")");
  }

  public boolean visit(PackageOrTypeName_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "PackageOrTypeName_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(PackageName_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "PackageName_JavaObjectSort" + ")");
  }

  public boolean visit(PackageName_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "PackageName_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(MethodMod_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "MethodMod_JavaObjectSort" + ")");
  }

  public boolean visit(MethodMod_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "MethodMod_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ClassMod_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ClassMod_JavaObjectSort" + ")");
  }

  public boolean visit(ClassMod_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ClassMod_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ConstrMod_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "ConstrMod_JavaObjectSort" + ")");
  }

  public boolean visit(ConstrMod_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "ConstrMod_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(VarMod_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "VarMod_JavaObjectSort" + ")");
  }

  public boolean visit(VarMod_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "VarMod_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(FieldMod_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "FieldMod_JavaObjectSort" + ")");
  }

  public boolean visit(FieldMod_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "FieldMod_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(FormalParam_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "FormalParam_JavaObjectSort" + ")");
  }

  public boolean visit(FormalParam_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "FormalParam_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(StringChars_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "StringChars_JavaObjectSort" + ")");
  }

  public boolean visit(StringChars_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "StringChars_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(SingleChar_JavaObjectSort node)
  { 
    unimplementedVisitor("endVisit(" + "SingleChar_JavaObjectSort" + ")");
  }

  public boolean visit(SingleChar_JavaObjectSort node)
  { 
    unimplementedVisitor("visit(" + "SingleChar_JavaObjectSort" + ")");
    return true;
  }

  public void endVisit(ListPlusOfCommChar0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListPlusOfCommChar0Sort" + ")");
  }

  public boolean visit(ListPlusOfCommChar0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListPlusOfCommChar0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfCommChar0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfCommChar0Sort" + ")");
  }

  public boolean visit(ListStarOfCommChar0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfCommChar0Sort" + ")");
    return true;
  }

  public void endVisit(ListPlusOfModNamePart_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListPlusOfModNamePart_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListPlusOfModNamePart_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListPlusOfModNamePart_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfModNamePart_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfModNamePart_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfModNamePart_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfModNamePart_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListPlusOfStrChar_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListPlusOfStrChar_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListPlusOfStrChar_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListPlusOfStrChar_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfStrChar_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfStrChar_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfStrChar_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfStrChar_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfSort_StrategoHost1Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfSort_StrategoHost1Sort" + ")");
  }

  public boolean visit(ListStarOfSort_StrategoHost1Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfSort_StrategoHost1Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfOpdecl_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfOpdecl_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfOpdecl_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfOpdecl_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfArgType_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfArgType_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfArgType_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfArgType_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfID_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfID_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfID_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfID_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfDecl_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfDecl_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfDecl_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfDecl_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfImportModName_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfImportModName_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfImportModName_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfImportModName_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfSdecl_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfSdecl_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfSdecl_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfSdecl_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfSort_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfSort_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfSort_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfSort_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfStrategy_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfStrategy_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfStrategy_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfStrategy_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfSwitchCase_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfSwitchCase_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfSwitchCase_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfSwitchCase_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfDef_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfDef_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfDef_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfDef_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfOverlay_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfOverlay_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfOverlay_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfOverlay_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfDynRuleScopeId_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfDynRuleScopeId_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfDynRuleScopeId_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfDynRuleScopeId_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfDynRuleDef_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfDynRuleDef_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfDynRuleDef_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfDynRuleDef_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfTypedid_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfTypedid_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfTypedid_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfTypedid_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfId_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfId_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfId_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfId_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfCharClass1Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfCharClass1Sort" + ")");
  }

  public boolean visit(ListStarOfCharClass1Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfCharClass1Sort" + ")");
    return true;
  }

  public void endVisit(ListPlusOfCommentPart0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListPlusOfCommentPart0Sort" + ")");
  }

  public boolean visit(ListPlusOfCommentPart0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListPlusOfCommentPart0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfCommentPart0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfCommentPart0Sort" + ")");
  }

  public boolean visit(ListStarOfCommentPart0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfCommentPart0Sort" + ")");
    return true;
  }

  public void endVisit(OptDeciFloatExponentPart_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "OptDeciFloatExponentPart_JavaObject0Sort" + ")");
  }

  public boolean visit(OptDeciFloatExponentPart_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "OptDeciFloatExponentPart_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfStringPart_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfStringPart_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfStringPart_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfStringPart_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfCharClass0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfCharClass0Sort" + ")");
  }

  public boolean visit(ListStarOfCharClass0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfCharClass0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfId_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfId_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfId_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfId_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfActualTypeArg_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfActualTypeArg_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfActualTypeArg_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfActualTypeArg_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfClassOrInterfaceType_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfClassOrInterfaceType_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfClassOrInterfaceType_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfClassOrInterfaceType_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfSwitchLabel_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfSwitchLabel_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfSwitchLabel_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfSwitchLabel_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfBlockStm_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfBlockStm_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfBlockStm_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfBlockStm_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfCatchClause_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfCatchClause_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfCatchClause_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfCatchClause_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfExceptionType_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfExceptionType_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfExceptionType_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfExceptionType_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfEnumConst_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfEnumConst_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfEnumConst_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfEnumConst_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject_ConstantMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject_ConstantMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject_ConstantMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject_ConstantMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ConstantMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ConstantMod_JavaObject0Sort" + ")");
  }

  public boolean visit(Anno_JavaObject_ConstantMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ConstantMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject_AbstractMethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject_AbstractMethodMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject_AbstractMethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject_AbstractMethodMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_AbstractMethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_AbstractMethodMod_JavaObject0Sort" + ")");
  }

  public boolean visit(Anno_JavaObject_AbstractMethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_AbstractMethodMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnnoElemDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnnoElemDec_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnnoElemDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnnoElemDec_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAbstractMethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAbstractMethodMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAbstractMethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAbstractMethodMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject_InterfaceMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject_InterfaceMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject_InterfaceMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject_InterfaceMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_InterfaceMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_InterfaceMod_JavaObject0Sort" + ")");
  }

  public boolean visit(Anno_JavaObject_InterfaceMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_InterfaceMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfInterfaceType_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfInterfaceType_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfInterfaceType_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfInterfaceType_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfDimExpr_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfDimExpr_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfDimExpr_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfDimExpr_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfDim_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfDim_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfDim_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfDim_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(OptTypeArgs_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "OptTypeArgs_JavaObject0Sort" + ")");
  }

  public boolean visit(OptTypeArgs_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "OptTypeArgs_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfElemValPair_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfElemValPair_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfElemValPair_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfElemValPair_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfElemVal_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfElemVal_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfElemVal_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfElemVal_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfTypeDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfTypeDec_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfTypeDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfTypeDec_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfVarInit_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfVarInit_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfVarInit_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfVarInit_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfExpr_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfExpr_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfExpr_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfExpr_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfSwitchGroup_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfSwitchGroup_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfSwitchGroup_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfSwitchGroup_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfVarDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfVarDec_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfVarDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfVarDec_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfImportDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfImportDec_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfImportDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfImportDec_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAbstractMethodDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAbstractMethodDec_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAbstractMethodDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAbstractMethodDec_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfTypeParam_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfTypeParam_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfTypeParam_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfTypeParam_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfClassBodyDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfClassBodyDec_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfClassBodyDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfClassBodyDec_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfInterfaceMemberDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfInterfaceMemberDec_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfInterfaceMemberDec_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfInterfaceMemberDec_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject_MethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject_MethodMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject_MethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject_MethodMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_MethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_MethodMod_JavaObject0Sort" + ")");
  }

  public boolean visit(Anno_JavaObject_MethodMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_MethodMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject_ClassMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject_ClassMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject_ClassMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject_ClassMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ClassMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ClassMod_JavaObject0Sort" + ")");
  }

  public boolean visit(Anno_JavaObject_ClassMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ClassMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject_ConstrMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject_ConstrMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject_ConstrMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject_ConstrMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_ConstrMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_ConstrMod_JavaObject0Sort" + ")");
  }

  public boolean visit(Anno_JavaObject_ConstrMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_ConstrMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject_VarMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject_VarMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject_VarMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject_VarMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_VarMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_VarMod_JavaObject0Sort" + ")");
  }

  public boolean visit(Anno_JavaObject_VarMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_VarMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfAnno_JavaObject_FieldMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfAnno_JavaObject_FieldMod_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfAnno_JavaObject_FieldMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfAnno_JavaObject_FieldMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(Anno_JavaObject_FieldMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "Anno_JavaObject_FieldMod_JavaObject0Sort" + ")");
  }

  public boolean visit(Anno_JavaObject_FieldMod_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "Anno_JavaObject_FieldMod_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfFormalParam_JavaObject0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfFormalParam_JavaObject0Sort" + ")");
  }

  public boolean visit(ListStarOfFormalParam_JavaObject0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfFormalParam_JavaObject0Sort" + ")");
    return true;
  }

  public void endVisit(ListStarOfTerm_StrategoHost0Sort node)
  { 
    unimplementedVisitor("endVisit(" + "ListStarOfTerm_StrategoHost0Sort" + ")");
  }

  public boolean visit(ListStarOfTerm_StrategoHost0Sort node)
  { 
    unimplementedVisitor("visit(" + "ListStarOfTerm_StrategoHost0Sort" + ")");
    return true;
  }

  public void endVisit(ASTString node)
  { 
    unimplementedVisitor("endVisit(" + "ASTString" + ")");
  }

  public boolean visit(ASTString node)
  { 
    unimplementedVisitor("visit(" + "ASTString" + ")");
    return true;
  }

  public void endVisit(List node)
  { 
    unimplementedVisitor("endVisit(" + "List" + ")");
  }

  public boolean visit(List node)
  { 
    unimplementedVisitor("visit(" + "List" + ")");
    return true;
  }

  public boolean visit(ASTNode node)
  { 
    if(node instanceof Ws)
      return visit((Ws)node);
    if(node instanceof ShortCom)
      return visit((ShortCom)node);
    if(node instanceof LongCom)
      return visit((LongCom)node);
    if(node instanceof Eof)
      return visit((Eof)node);
    if(node instanceof CommChar)
      return visit((CommChar)node);
    if(node instanceof Asterisk)
      return visit((Asterisk)node);
    if(node instanceof ModName_StrategoHost)
      return visit((ModName_StrategoHost)node);
    if(node instanceof ModNamePart_StrategoHost)
      return visit((ModNamePart_StrategoHost)node);
    if(node instanceof Id_StrategoHost)
      return visit((Id_StrategoHost)node);
    if(node instanceof LId_StrategoHost)
      return visit((LId_StrategoHost)node);
    if(node instanceof LCID_StrategoHost)
      return visit((LCID_StrategoHost)node);
    if(node instanceof UCID_StrategoHost)
      return visit((UCID_StrategoHost)node);
    if(node instanceof Keyword_StrategoHost)
      return visit((Keyword_StrategoHost)node);
    if(node instanceof Int_StrategoHost)
      return visit((Int_StrategoHost)node);
    if(node instanceof Real_StrategoHost)
      return visit((Real_StrategoHost)node);
    if(node instanceof String_StrategoHost)
      return visit((String_StrategoHost)node);
    if(node instanceof StrChar_StrategoHost)
      return visit((StrChar_StrategoHost)node);
    if(node instanceof Char_StrategoHost)
      return visit((Char_StrategoHost)node);
    if(node instanceof CharChar_StrategoHost)
      return visit((CharChar_StrategoHost)node);
    if(node instanceof Var0)
      return visit((Var0)node);
    if(node instanceof ID_StrategoHost0)
      return visit((ID_StrategoHost0)node);
    if(node instanceof PreTerm_StrategoHost)
      return visit((PreTerm_StrategoHost)node);
    if(node instanceof Term_StrategoHost)
      return visit((Term_StrategoHost)node);
    if(node instanceof Wld0)
      return visit((Wld0)node);
    if(node instanceof Wld)
      return visit((Wld)node);
    if(node instanceof Int0)
      return visit((Int0)node);
    if(node instanceof Real)
      return visit((Real)node);
    if(node instanceof Str)
      return visit((Str)node);
    if(node instanceof Op)
      return visit((Op)node);
    if(node instanceof OpQ)
      return visit((OpQ)node);
    if(node instanceof Explode)
      return visit((Explode)node);
    if(node instanceof Anno0)
      return visit((Anno0)node);
    if(node instanceof As0)
      return visit((As0)node);
    if(node instanceof As)
      return visit((As)node);
    if(node instanceof Sorts)
      return visit((Sorts)node);
    if(node instanceof Constructors)
      return visit((Constructors)node);
    if(node instanceof SortVar)
      return visit((SortVar)node);
    if(node instanceof SortNoArgs)
      return visit((SortNoArgs)node);
    if(node instanceof Sort)
      return visit((Sort)node);
    if(node instanceof OpDecl)
      return visit((OpDecl)node);
    if(node instanceof OpDeclQ)
      return visit((OpDeclQ)node);
    if(node instanceof OpDeclInj)
      return visit((OpDeclInj)node);
    if(node instanceof ConstType)
      return visit((ConstType)node);
    if(node instanceof FunType)
      return visit((FunType)node);
    if(node instanceof ArgType)
      return visit((ArgType)node);
    if(node instanceof ArgType_StrategoHost)
      return visit((ArgType_StrategoHost)node);
    if(node instanceof RetType_StrategoHost)
      return visit((RetType_StrategoHost)node);
    if(node instanceof Type_StrategoHost0)
      return visit((Type_StrategoHost0)node);
    if(node instanceof Type_StrategoHost)
      return visit((Type_StrategoHost)node);
    if(node instanceof Def_StrategoHost0)
      return visit((Def_StrategoHost0)node);
    if(node instanceof SVar)
      return visit((SVar)node);
    if(node instanceof Let)
      return visit((Let)node);
    if(node instanceof CallT)
      return visit((CallT)node);
    if(node instanceof CallDynamic)
      return visit((CallDynamic)node);
    if(node instanceof SDefT)
      return visit((SDefT)node);
    if(node instanceof ExtSDefInl)
      return visit((ExtSDefInl)node);
    if(node instanceof ExtSDef)
      return visit((ExtSDef)node);
    if(node instanceof VarDec1)
      return visit((VarDec1)node);
    if(node instanceof ParenStrat)
      return visit((ParenStrat)node);
    if(node instanceof Fail)
      return visit((Fail)node);
    if(node instanceof Id0)
      return visit((Id0)node);
    if(node instanceof Match)
      return visit((Match)node);
    if(node instanceof Build)
      return visit((Build)node);
    if(node instanceof Scope)
      return visit((Scope)node);
    if(node instanceof Seq)
      return visit((Seq)node);
    if(node instanceof GuardedLChoice)
      return visit((GuardedLChoice)node);
    if(node instanceof StrategyMid_StrategoHost)
      return visit((StrategyMid_StrategoHost)node);
    if(node instanceof PrimT)
      return visit((PrimT)node);
    if(node instanceof Some)
      return visit((Some)node);
    if(node instanceof One)
      return visit((One)node);
    if(node instanceof All)
      return visit((All)node);
    if(node instanceof ImportTerm)
      return visit((ImportTerm)node);
    if(node instanceof Module)
      return visit((Module)node);
    if(node instanceof Specification)
      return visit((Specification)node);
    if(node instanceof Imports)
      return visit((Imports)node);
    if(node instanceof Strategies)
      return visit((Strategies)node);
    if(node instanceof Signature)
      return visit((Signature)node);
    if(node instanceof Import)
      return visit((Import)node);
    if(node instanceof ImportWildcard)
      return visit((ImportWildcard)node);
    if(node instanceof ListVar)
      return visit((ListVar)node);
    if(node instanceof Var)
      return visit((Var)node);
    if(node instanceof ID_StrategoHost)
      return visit((ID_StrategoHost)node);
    if(node instanceof BuildDefaultPT)
      return visit((BuildDefaultPT)node);
    if(node instanceof BuildDefault)
      return visit((BuildDefault)node);
    if(node instanceof Char1)
      return visit((Char1)node);
    if(node instanceof AnnoList)
      return visit((AnnoList)node);
    if(node instanceof NoAnnoList)
      return visit((NoAnnoList)node);
    if(node instanceof App0)
      return visit((App0)node);
    if(node instanceof App)
      return visit((App)node);
    if(node instanceof RootApp0)
      return visit((RootApp0)node);
    if(node instanceof RootApp)
      return visit((RootApp)node);
    if(node instanceof Tuple)
      return visit((Tuple)node);
    if(node instanceof List0)
      return visit((List0)node);
    if(node instanceof ListTail)
      return visit((ListTail)node);
    if(node instanceof SortList)
      return visit((SortList)node);
    if(node instanceof SortListTl)
      return visit((SortListTl)node);
    if(node instanceof SortTuple)
      return visit((SortTuple)node);
    if(node instanceof Star)
      return visit((Star)node);
    if(node instanceof StarStar)
      return visit((StarStar)node);
    if(node instanceof SDefNoArgs)
      return visit((SDefNoArgs)node);
    if(node instanceof SDef)
      return visit((SDef)node);
    if(node instanceof DefaultVarDec)
      return visit((DefaultVarDec)node);
    if(node instanceof Call)
      return visit((Call)node);
    if(node instanceof ScopeDefault)
      return visit((ScopeDefault)node);
    if(node instanceof BA)
      return visit((BA)node);
    if(node instanceof StrategyAngle)
      return visit((StrategyAngle)node);
    if(node instanceof LChoice)
      return visit((LChoice)node);
    if(node instanceof Rec)
      return visit((Rec)node);
    if(node instanceof Not0)
      return visit((Not0)node);
    if(node instanceof Where)
      return visit((Where)node);
    if(node instanceof Test)
      return visit((Test)node);
    if(node instanceof PrimNoArgs)
      return visit((PrimNoArgs)node);
    if(node instanceof Prim)
      return visit((Prim)node);
    if(node instanceof StrCong)
      return visit((StrCong)node);
    if(node instanceof IntCong)
      return visit((IntCong)node);
    if(node instanceof RealCong)
      return visit((RealCong)node);
    if(node instanceof CharCong)
      return visit((CharCong)node);
    if(node instanceof CongQ)
      return visit((CongQ)node);
    if(node instanceof AnnoCong)
      return visit((AnnoCong)node);
    if(node instanceof StrategyCurly)
      return visit((StrategyCurly)node);
    if(node instanceof EmptyTupleCong)
      return visit((EmptyTupleCong)node);
    if(node instanceof Strategy)
      return visit((Strategy)node);
    if(node instanceof TupleCong)
      return visit((TupleCong)node);
    if(node instanceof ListCongNoTail)
      return visit((ListCongNoTail)node);
    if(node instanceof ListCong)
      return visit((ListCong)node);
    if(node instanceof ExplodeCong)
      return visit((ExplodeCong)node);
    if(node instanceof CallNoArgs)
      return visit((CallNoArgs)node);
    if(node instanceof LRule)
      return visit((LRule)node);
    if(node instanceof SRule)
      return visit((SRule)node);
    if(node instanceof Choice)
      return visit((Choice)node);
    if(node instanceof RChoice)
      return visit((RChoice)node);
    if(node instanceof CondChoice)
      return visit((CondChoice)node);
    if(node instanceof IfThen)
      return visit((IfThen)node);
    if(node instanceof SwitchChoiceNoOtherwise)
      return visit((SwitchChoiceNoOtherwise)node);
    if(node instanceof SwitchChoice)
      return visit((SwitchChoice)node);
    if(node instanceof SwitchCase)
      return visit((SwitchCase)node);
    if(node instanceof AM)
      return visit((AM)node);
    if(node instanceof Assign0)
      return visit((Assign0)node);
    if(node instanceof OverlayNoArgs)
      return visit((OverlayNoArgs)node);
    if(node instanceof Overlay)
      return visit((Overlay)node);
    if(node instanceof RDefNoArgs)
      return visit((RDefNoArgs)node);
    if(node instanceof RDef)
      return visit((RDef)node);
    if(node instanceof RDefT)
      return visit((RDefT)node);
    if(node instanceof RuleNoCond)
      return visit((RuleNoCond)node);
    if(node instanceof Rule)
      return visit((Rule)node);
    if(node instanceof Rules)
      return visit((Rules)node);
    if(node instanceof Overlays)
      return visit((Overlays)node);
    if(node instanceof Def_StrategoHost)
      return visit((Def_StrategoHost)node);
    if(node instanceof DynRuleScope)
      return visit((DynRuleScope)node);
    if(node instanceof ScopeLabels_StrategoHost)
      return visit((ScopeLabels_StrategoHost)node);
    if(node instanceof ScopeLabels)
      return visit((ScopeLabels)node);
    if(node instanceof GenDynRules)
      return visit((GenDynRules)node);
    if(node instanceof AddScopeLabel)
      return visit((AddScopeLabel)node);
    if(node instanceof UndefineDynRule)
      return visit((UndefineDynRule)node);
    if(node instanceof SetDynRule)
      return visit((SetDynRule)node);
    if(node instanceof AddDynRule)
      return visit((AddDynRule)node);
    if(node instanceof SetDynRuleMatch)
      return visit((SetDynRuleMatch)node);
    if(node instanceof DynRuleAssign)
      return visit((DynRuleAssign)node);
    if(node instanceof DynRuleAssignAdd)
      return visit((DynRuleAssignAdd)node);
    if(node instanceof SetDynRuleDepends)
      return visit((SetDynRuleDepends)node);
    if(node instanceof LabeledDynRuleId)
      return visit((LabeledDynRuleId)node);
    if(node instanceof AddLabelDynRuleId)
      return visit((AddLabelDynRuleId)node);
    if(node instanceof DynRuleId)
      return visit((DynRuleId)node);
    if(node instanceof LabeledDynRuleScopeId)
      return visit((LabeledDynRuleScopeId)node);
    if(node instanceof DynRuleScopeId)
      return visit((DynRuleScopeId)node);
    if(node instanceof RDecNoArgs)
      return visit((RDecNoArgs)node);
    if(node instanceof RDec)
      return visit((RDec)node);
    if(node instanceof RDecT)
      return visit((RDecT)node);
    if(node instanceof RuleNames_StrategoHost)
      return visit((RuleNames_StrategoHost)node);
    if(node instanceof RuleNames)
      return visit((RuleNames)node);
    if(node instanceof DynRuleIntersectFix)
      return visit((DynRuleIntersectFix)node);
    if(node instanceof DynRuleUnionFix0)
      return visit((DynRuleUnionFix0)node);
    if(node instanceof DynRuleUnionFix)
      return visit((DynRuleUnionFix)node);
    if(node instanceof DynRuleIntersectUnionFix0)
      return visit((DynRuleIntersectUnionFix0)node);
    if(node instanceof DynRuleIntersectUnionFix)
      return visit((DynRuleIntersectUnionFix)node);
    if(node instanceof DynRuleIntersect)
      return visit((DynRuleIntersect)node);
    if(node instanceof DynRuleUnion)
      return visit((DynRuleUnion)node);
    if(node instanceof DynRuleIntersectUnion)
      return visit((DynRuleIntersectUnion)node);
    if(node instanceof UnicodeEscape0)
      return visit((UnicodeEscape0)node);
    if(node instanceof LineTerminator)
      return visit((LineTerminator)node);
    if(node instanceof CarriageReturn)
      return visit((CarriageReturn)node);
    if(node instanceof EndOfFile)
      return visit((EndOfFile)node);
    if(node instanceof Comment)
      return visit((Comment)node);
    if(node instanceof EOLCommentChars)
      return visit((EOLCommentChars)node);
    if(node instanceof CommentPart)
      return visit((CommentPart)node);
    if(node instanceof BlockCommentChars)
      return visit((BlockCommentChars)node);
    if(node instanceof EscEscChar)
      return visit((EscEscChar)node);
    if(node instanceof EscChar)
      return visit((EscChar)node);
    if(node instanceof UnicodeEscape)
      return visit((UnicodeEscape)node);
    if(node instanceof Keyword_JavaObject)
      return visit((Keyword_JavaObject)node);
    if(node instanceof ID_JavaObject)
      return visit((ID_JavaObject)node);
    if(node instanceof Id)
      return visit((Id)node);
    if(node instanceof Public)
      return visit((Public)node);
    if(node instanceof Private)
      return visit((Private)node);
    if(node instanceof Protected)
      return visit((Protected)node);
    if(node instanceof Abstract)
      return visit((Abstract)node);
    if(node instanceof Final)
      return visit((Final)node);
    if(node instanceof Static)
      return visit((Static)node);
    if(node instanceof Native)
      return visit((Native)node);
    if(node instanceof Transient)
      return visit((Transient)node);
    if(node instanceof Volatile)
      return visit((Volatile)node);
    if(node instanceof Synchronized0)
      return visit((Synchronized0)node);
    if(node instanceof StrictFP)
      return visit((StrictFP)node);
    if(node instanceof Modifier_JavaObject9)
      return visit((Modifier_JavaObject9)node);
    if(node instanceof Modifier_JavaObject8)
      return visit((Modifier_JavaObject8)node);
    if(node instanceof Modifier_JavaObject7)
      return visit((Modifier_JavaObject7)node);
    if(node instanceof Modifier_JavaObject6)
      return visit((Modifier_JavaObject6)node);
    if(node instanceof Modifier_JavaObject5)
      return visit((Modifier_JavaObject5)node);
    if(node instanceof Modifier_JavaObject4)
      return visit((Modifier_JavaObject4)node);
    if(node instanceof Modifier_JavaObject3)
      return visit((Modifier_JavaObject3)node);
    if(node instanceof Modifier_JavaObject2)
      return visit((Modifier_JavaObject2)node);
    if(node instanceof Modifier_JavaObject1)
      return visit((Modifier_JavaObject1)node);
    if(node instanceof Modifier_JavaObject0)
      return visit((Modifier_JavaObject0)node);
    if(node instanceof Modifier_JavaObject)
      return visit((Modifier_JavaObject)node);
    if(node instanceof DeciLiteral_JavaObject)
      return visit((DeciLiteral_JavaObject)node);
    if(node instanceof HexaLiteral_JavaObject)
      return visit((HexaLiteral_JavaObject)node);
    if(node instanceof OctaLiteral_JavaObject)
      return visit((OctaLiteral_JavaObject)node);
    if(node instanceof DeciNumeral_JavaObject)
      return visit((DeciNumeral_JavaObject)node);
    if(node instanceof HexaNumeral_JavaObject)
      return visit((HexaNumeral_JavaObject)node);
    if(node instanceof OctaNumeral_JavaObject)
      return visit((OctaNumeral_JavaObject)node);
    if(node instanceof Deci)
      return visit((Deci)node);
    if(node instanceof Hexa)
      return visit((Hexa)node);
    if(node instanceof Octa)
      return visit((Octa)node);
    if(node instanceof DeciFloatLiteral_JavaObject)
      return visit((DeciFloatLiteral_JavaObject)node);
    if(node instanceof HexaFloatLiteral_JavaObject)
      return visit((HexaFloatLiteral_JavaObject)node);
    if(node instanceof Float1)
      return visit((Float1)node);
    if(node instanceof Float0)
      return visit((Float0)node);
    if(node instanceof DeciFloatNumeral_JavaObject)
      return visit((DeciFloatNumeral_JavaObject)node);
    if(node instanceof DeciFloatDigits_JavaObject)
      return visit((DeciFloatDigits_JavaObject)node);
    if(node instanceof DeciFloatExponentPart_JavaObject)
      return visit((DeciFloatExponentPart_JavaObject)node);
    if(node instanceof SignedInteger_JavaObject)
      return visit((SignedInteger_JavaObject)node);
    if(node instanceof HexaFloatNumeral_JavaObject)
      return visit((HexaFloatNumeral_JavaObject)node);
    if(node instanceof HexaSignificand_JavaObject)
      return visit((HexaSignificand_JavaObject)node);
    if(node instanceof BinaryExponent_JavaObject)
      return visit((BinaryExponent_JavaObject)node);
    if(node instanceof Bool)
      return visit((Bool)node);
    if(node instanceof True)
      return visit((True)node);
    if(node instanceof False)
      return visit((False)node);
    if(node instanceof EscapeSeq_JavaObject0)
      return visit((EscapeSeq_JavaObject0)node);
    if(node instanceof EscapeSeq_JavaObject)
      return visit((EscapeSeq_JavaObject)node);
    if(node instanceof NamedEscape)
      return visit((NamedEscape)node);
    if(node instanceof OctaEscape1)
      return visit((OctaEscape1)node);
    if(node instanceof OctaEscape20)
      return visit((OctaEscape20)node);
    if(node instanceof OctaEscape2)
      return visit((OctaEscape2)node);
    if(node instanceof OctaEscape3)
      return visit((OctaEscape3)node);
    if(node instanceof LastOcta_JavaObject)
      return visit((LastOcta_JavaObject)node);
    if(node instanceof Char0)
      return visit((Char0)node);
    if(node instanceof Single)
      return visit((Single)node);
    if(node instanceof CharContent_JavaObject0)
      return visit((CharContent_JavaObject0)node);
    if(node instanceof CharContent_JavaObject)
      return visit((CharContent_JavaObject)node);
    if(node instanceof SingleChar_JavaObject)
      return visit((SingleChar_JavaObject)node);
    if(node instanceof String)
      return visit((String)node);
    if(node instanceof Chars)
      return visit((Chars)node);
    if(node instanceof StringPart_JavaObject0)
      return visit((StringPart_JavaObject0)node);
    if(node instanceof StringPart_JavaObject)
      return visit((StringPart_JavaObject)node);
    if(node instanceof StringChars_JavaObject)
      return visit((StringChars_JavaObject)node);
    if(node instanceof Null)
      return visit((Null)node);
    if(node instanceof PrimType_JavaObject)
      return visit((PrimType_JavaObject)node);
    if(node instanceof Boolean)
      return visit((Boolean)node);
    if(node instanceof NumType_JavaObject0)
      return visit((NumType_JavaObject0)node);
    if(node instanceof NumType_JavaObject)
      return visit((NumType_JavaObject)node);
    if(node instanceof Byte)
      return visit((Byte)node);
    if(node instanceof Short)
      return visit((Short)node);
    if(node instanceof Int)
      return visit((Int)node);
    if(node instanceof Long)
      return visit((Long)node);
    if(node instanceof Char)
      return visit((Char)node);
    if(node instanceof Float)
      return visit((Float)node);
    if(node instanceof Double)
      return visit((Double)node);
    if(node instanceof PackageName)
      return visit((PackageName)node);
    if(node instanceof AmbName0)
      return visit((AmbName0)node);
    if(node instanceof AmbName)
      return visit((AmbName)node);
    if(node instanceof TypeName0)
      return visit((TypeName0)node);
    if(node instanceof TypeName)
      return visit((TypeName)node);
    if(node instanceof ExprName0)
      return visit((ExprName0)node);
    if(node instanceof ExprName)
      return visit((ExprName)node);
    if(node instanceof MethodName0)
      return visit((MethodName0)node);
    if(node instanceof MethodName)
      return visit((MethodName)node);
    if(node instanceof PackageOrTypeName0)
      return visit((PackageOrTypeName0)node);
    if(node instanceof PackageOrTypeName)
      return visit((PackageOrTypeName)node);
    if(node instanceof TypeArgs)
      return visit((TypeArgs)node);
    if(node instanceof ActualTypeArg_JavaObject)
      return visit((ActualTypeArg_JavaObject)node);
    if(node instanceof Wildcard)
      return visit((Wildcard)node);
    if(node instanceof WildcardUpperBound)
      return visit((WildcardUpperBound)node);
    if(node instanceof WildcardLowerBound)
      return visit((WildcardLowerBound)node);
    if(node instanceof TypeParam)
      return visit((TypeParam)node);
    if(node instanceof TypeBound)
      return visit((TypeBound)node);
    if(node instanceof TypeParams)
      return visit((TypeParams)node);
    if(node instanceof TypeVarId_JavaObject)
      return visit((TypeVarId_JavaObject)node);
    if(node instanceof RefType_JavaObject0)
      return visit((RefType_JavaObject0)node);
    if(node instanceof RefType_JavaObject)
      return visit((RefType_JavaObject)node);
    if(node instanceof ClassOrInterfaceType)
      return visit((ClassOrInterfaceType)node);
    if(node instanceof ClassType)
      return visit((ClassType)node);
    if(node instanceof InterfaceType)
      return visit((InterfaceType)node);
    if(node instanceof TypeDecSpec_JavaObject)
      return visit((TypeDecSpec_JavaObject)node);
    if(node instanceof Member)
      return visit((Member)node);
    if(node instanceof TypeVar)
      return visit((TypeVar)node);
    if(node instanceof ArrayType)
      return visit((ArrayType)node);
    if(node instanceof Type_JavaObject0)
      return visit((Type_JavaObject0)node);
    if(node instanceof Type_JavaObject)
      return visit((Type_JavaObject)node);
    if(node instanceof Lit)
      return visit((Lit)node);
    if(node instanceof Literal_JavaObject5)
      return visit((Literal_JavaObject5)node);
    if(node instanceof Literal_JavaObject4)
      return visit((Literal_JavaObject4)node);
    if(node instanceof Literal_JavaObject3)
      return visit((Literal_JavaObject3)node);
    if(node instanceof Literal_JavaObject2)
      return visit((Literal_JavaObject2)node);
    if(node instanceof Literal_JavaObject1)
      return visit((Literal_JavaObject1)node);
    if(node instanceof Literal_JavaObject0)
      return visit((Literal_JavaObject0)node);
    if(node instanceof Literal_JavaObject)
      return visit((Literal_JavaObject)node);
    if(node instanceof Class)
      return visit((Class)node);
    if(node instanceof VoidClass)
      return visit((VoidClass)node);
    if(node instanceof This)
      return visit((This)node);
    if(node instanceof QThis)
      return visit((QThis)node);
    if(node instanceof Expr)
      return visit((Expr)node);
    if(node instanceof ArrayInit0)
      return visit((ArrayInit0)node);
    if(node instanceof ArrayInit)
      return visit((ArrayInit)node);
    if(node instanceof FieldDec)
      return visit((FieldDec)node);
    if(node instanceof VarDec0)
      return visit((VarDec0)node);
    if(node instanceof VarDec)
      return visit((VarDec)node);
    if(node instanceof VarDecId_JavaObject)
      return visit((VarDecId_JavaObject)node);
    if(node instanceof ArrayVarDecId)
      return visit((ArrayVarDecId)node);
    if(node instanceof Dim0)
      return visit((Dim0)node);
    if(node instanceof VarInit_JavaObject0)
      return visit((VarInit_JavaObject0)node);
    if(node instanceof VarInit_JavaObject)
      return visit((VarInit_JavaObject)node);
    if(node instanceof FieldMod_JavaObject5)
      return visit((FieldMod_JavaObject5)node);
    if(node instanceof FieldMod_JavaObject4)
      return visit((FieldMod_JavaObject4)node);
    if(node instanceof FieldMod_JavaObject3)
      return visit((FieldMod_JavaObject3)node);
    if(node instanceof FieldMod_JavaObject2)
      return visit((FieldMod_JavaObject2)node);
    if(node instanceof FieldMod_JavaObject1)
      return visit((FieldMod_JavaObject1)node);
    if(node instanceof FieldMod_JavaObject0)
      return visit((FieldMod_JavaObject0)node);
    if(node instanceof FieldMod_JavaObject)
      return visit((FieldMod_JavaObject)node);
    if(node instanceof LocalVarDecStm)
      return visit((LocalVarDecStm)node);
    if(node instanceof LocalVarDec)
      return visit((LocalVarDec)node);
    if(node instanceof Stm_JavaObject)
      return visit((Stm_JavaObject)node);
    if(node instanceof Empty)
      return visit((Empty)node);
    if(node instanceof Labeled)
      return visit((Labeled)node);
    if(node instanceof ExprStm)
      return visit((ExprStm)node);
    if(node instanceof If0)
      return visit((If0)node);
    if(node instanceof If)
      return visit((If)node);
    if(node instanceof AssertStm0)
      return visit((AssertStm0)node);
    if(node instanceof AssertStm)
      return visit((AssertStm)node);
    if(node instanceof Switch)
      return visit((Switch)node);
    if(node instanceof SwitchBlock)
      return visit((SwitchBlock)node);
    if(node instanceof SwitchGroup)
      return visit((SwitchGroup)node);
    if(node instanceof Case)
      return visit((Case)node);
    if(node instanceof Default)
      return visit((Default)node);
    if(node instanceof While)
      return visit((While)node);
    if(node instanceof DoWhile)
      return visit((DoWhile)node);
    if(node instanceof For0)
      return visit((For0)node);
    if(node instanceof For)
      return visit((For)node);
    if(node instanceof ForEach)
      return visit((ForEach)node);
    if(node instanceof Break)
      return visit((Break)node);
    if(node instanceof Continue)
      return visit((Continue)node);
    if(node instanceof Return)
      return visit((Return)node);
    if(node instanceof Throw)
      return visit((Throw)node);
    if(node instanceof Synchronized)
      return visit((Synchronized)node);
    if(node instanceof Try0)
      return visit((Try0)node);
    if(node instanceof Try)
      return visit((Try)node);
    if(node instanceof Catch)
      return visit((Catch)node);
    if(node instanceof Block)
      return visit((Block)node);
    if(node instanceof BlockStm_JavaObject0)
      return visit((BlockStm_JavaObject0)node);
    if(node instanceof ClassDecStm)
      return visit((ClassDecStm)node);
    if(node instanceof BlockStm_JavaObject)
      return visit((BlockStm_JavaObject)node);
    if(node instanceof MethodDec)
      return visit((MethodDec)node);
    if(node instanceof MethodDecHead)
      return visit((MethodDecHead)node);
    if(node instanceof DeprMethodDecHead)
      return visit((DeprMethodDecHead)node);
    if(node instanceof ResultType_JavaObject)
      return visit((ResultType_JavaObject)node);
    if(node instanceof Void)
      return visit((Void)node);
    if(node instanceof Param)
      return visit((Param)node);
    if(node instanceof VarArityParam)
      return visit((VarArityParam)node);
    if(node instanceof VarMod_JavaObject)
      return visit((VarMod_JavaObject)node);
    if(node instanceof MethodMod_JavaObject7)
      return visit((MethodMod_JavaObject7)node);
    if(node instanceof MethodMod_JavaObject6)
      return visit((MethodMod_JavaObject6)node);
    if(node instanceof MethodMod_JavaObject5)
      return visit((MethodMod_JavaObject5)node);
    if(node instanceof MethodMod_JavaObject4)
      return visit((MethodMod_JavaObject4)node);
    if(node instanceof MethodMod_JavaObject3)
      return visit((MethodMod_JavaObject3)node);
    if(node instanceof MethodMod_JavaObject2)
      return visit((MethodMod_JavaObject2)node);
    if(node instanceof MethodMod_JavaObject1)
      return visit((MethodMod_JavaObject1)node);
    if(node instanceof MethodMod_JavaObject0)
      return visit((MethodMod_JavaObject0)node);
    if(node instanceof MethodMod_JavaObject)
      return visit((MethodMod_JavaObject)node);
    if(node instanceof ThrowsDec)
      return visit((ThrowsDec)node);
    if(node instanceof ExceptionType_JavaObject)
      return visit((ExceptionType_JavaObject)node);
    if(node instanceof MethodBody_JavaObject)
      return visit((MethodBody_JavaObject)node);
    if(node instanceof NoMethodBody)
      return visit((NoMethodBody)node);
    if(node instanceof InstanceInit)
      return visit((InstanceInit)node);
    if(node instanceof StaticInit)
      return visit((StaticInit)node);
    if(node instanceof ConstrDec)
      return visit((ConstrDec)node);
    if(node instanceof ConstrDecHead)
      return visit((ConstrDecHead)node);
    if(node instanceof ConstrBody)
      return visit((ConstrBody)node);
    if(node instanceof AltConstrInv)
      return visit((AltConstrInv)node);
    if(node instanceof SuperConstrInv)
      return visit((SuperConstrInv)node);
    if(node instanceof QSuperConstrInv)
      return visit((QSuperConstrInv)node);
    if(node instanceof ConstrMod_JavaObject1)
      return visit((ConstrMod_JavaObject1)node);
    if(node instanceof ConstrMod_JavaObject0)
      return visit((ConstrMod_JavaObject0)node);
    if(node instanceof ConstrMod_JavaObject)
      return visit((ConstrMod_JavaObject)node);
    if(node instanceof EnumDec)
      return visit((EnumDec)node);
    if(node instanceof EnumDecHead)
      return visit((EnumDecHead)node);
    if(node instanceof EnumBody0)
      return visit((EnumBody0)node);
    if(node instanceof EnumBody)
      return visit((EnumBody)node);
    if(node instanceof EnumConst)
      return visit((EnumConst)node);
    if(node instanceof EnumConstArgs)
      return visit((EnumConstArgs)node);
    if(node instanceof EnumBodyDecs)
      return visit((EnumBodyDecs)node);
    if(node instanceof ConstantDec)
      return visit((ConstantDec)node);
    if(node instanceof ConstantMod_JavaObject1)
      return visit((ConstantMod_JavaObject1)node);
    if(node instanceof ConstantMod_JavaObject0)
      return visit((ConstantMod_JavaObject0)node);
    if(node instanceof ConstantMod_JavaObject)
      return visit((ConstantMod_JavaObject)node);
    if(node instanceof AbstractMethodDec)
      return visit((AbstractMethodDec)node);
    if(node instanceof DeprAbstractMethodDec)
      return visit((DeprAbstractMethodDec)node);
    if(node instanceof AbstractMethodMod_JavaObject0)
      return visit((AbstractMethodMod_JavaObject0)node);
    if(node instanceof AbstractMethodMod_JavaObject)
      return visit((AbstractMethodMod_JavaObject)node);
    if(node instanceof AnnoDec)
      return visit((AnnoDec)node);
    if(node instanceof AnnoDecHead)
      return visit((AnnoDecHead)node);
    if(node instanceof AnnoMethodDec)
      return visit((AnnoMethodDec)node);
    if(node instanceof AnnoElemDec_JavaObject3)
      return visit((AnnoElemDec_JavaObject3)node);
    if(node instanceof AnnoElemDec_JavaObject2)
      return visit((AnnoElemDec_JavaObject2)node);
    if(node instanceof AnnoElemDec_JavaObject1)
      return visit((AnnoElemDec_JavaObject1)node);
    if(node instanceof AnnoElemDec_JavaObject0)
      return visit((AnnoElemDec_JavaObject0)node);
    if(node instanceof AnnoElemDec_JavaObject)
      return visit((AnnoElemDec_JavaObject)node);
    if(node instanceof Semicolon2)
      return visit((Semicolon2)node);
    if(node instanceof DefaultVal)
      return visit((DefaultVal)node);
    if(node instanceof InterfaceDec_JavaObject)
      return visit((InterfaceDec_JavaObject)node);
    if(node instanceof InterfaceDec)
      return visit((InterfaceDec)node);
    if(node instanceof InterfaceDecHead)
      return visit((InterfaceDecHead)node);
    if(node instanceof ExtendsInterfaces)
      return visit((ExtendsInterfaces)node);
    if(node instanceof InterfaceMemberDec_JavaObject2)
      return visit((InterfaceMemberDec_JavaObject2)node);
    if(node instanceof InterfaceMemberDec_JavaObject1)
      return visit((InterfaceMemberDec_JavaObject1)node);
    if(node instanceof InterfaceMemberDec_JavaObject0)
      return visit((InterfaceMemberDec_JavaObject0)node);
    if(node instanceof InterfaceMemberDec_JavaObject)
      return visit((InterfaceMemberDec_JavaObject)node);
    if(node instanceof Semicolon1)
      return visit((Semicolon1)node);
    if(node instanceof InterfaceMod_JavaObject4)
      return visit((InterfaceMod_JavaObject4)node);
    if(node instanceof InterfaceMod_JavaObject3)
      return visit((InterfaceMod_JavaObject3)node);
    if(node instanceof InterfaceMod_JavaObject2)
      return visit((InterfaceMod_JavaObject2)node);
    if(node instanceof InterfaceMod_JavaObject1)
      return visit((InterfaceMod_JavaObject1)node);
    if(node instanceof InterfaceMod_JavaObject0)
      return visit((InterfaceMod_JavaObject0)node);
    if(node instanceof InterfaceMod_JavaObject)
      return visit((InterfaceMod_JavaObject)node);
    if(node instanceof ClassDec_JavaObject)
      return visit((ClassDec_JavaObject)node);
    if(node instanceof ClassDec)
      return visit((ClassDec)node);
    if(node instanceof ClassBody)
      return visit((ClassBody)node);
    if(node instanceof ClassDecHead)
      return visit((ClassDecHead)node);
    if(node instanceof ClassMod_JavaObject5)
      return visit((ClassMod_JavaObject5)node);
    if(node instanceof ClassMod_JavaObject4)
      return visit((ClassMod_JavaObject4)node);
    if(node instanceof ClassMod_JavaObject3)
      return visit((ClassMod_JavaObject3)node);
    if(node instanceof ClassMod_JavaObject2)
      return visit((ClassMod_JavaObject2)node);
    if(node instanceof ClassMod_JavaObject1)
      return visit((ClassMod_JavaObject1)node);
    if(node instanceof ClassMod_JavaObject0)
      return visit((ClassMod_JavaObject0)node);
    if(node instanceof ClassMod_JavaObject)
      return visit((ClassMod_JavaObject)node);
    if(node instanceof SuperDec)
      return visit((SuperDec)node);
    if(node instanceof ImplementsDec)
      return visit((ImplementsDec)node);
    if(node instanceof ClassBodyDec_JavaObject2)
      return visit((ClassBodyDec_JavaObject2)node);
    if(node instanceof ClassBodyDec_JavaObject1)
      return visit((ClassBodyDec_JavaObject1)node);
    if(node instanceof ClassBodyDec_JavaObject0)
      return visit((ClassBodyDec_JavaObject0)node);
    if(node instanceof ClassBodyDec_JavaObject)
      return visit((ClassBodyDec_JavaObject)node);
    if(node instanceof ClassMemberDec_JavaObject2)
      return visit((ClassMemberDec_JavaObject2)node);
    if(node instanceof ClassMemberDec_JavaObject1)
      return visit((ClassMemberDec_JavaObject1)node);
    if(node instanceof ClassMemberDec_JavaObject0)
      return visit((ClassMemberDec_JavaObject0)node);
    if(node instanceof ClassMemberDec_JavaObject)
      return visit((ClassMemberDec_JavaObject)node);
    if(node instanceof Semicolon0)
      return visit((Semicolon0)node);
    if(node instanceof NewInstance)
      return visit((NewInstance)node);
    if(node instanceof QNewInstance)
      return visit((QNewInstance)node);
    if(node instanceof Expr_JavaObject3)
      return visit((Expr_JavaObject3)node);
    if(node instanceof NewArray0)
      return visit((NewArray0)node);
    if(node instanceof NewArray)
      return visit((NewArray)node);
    if(node instanceof ArrayBaseType_JavaObject0)
      return visit((ArrayBaseType_JavaObject0)node);
    if(node instanceof ArrayBaseType_JavaObject)
      return visit((ArrayBaseType_JavaObject)node);
    if(node instanceof UnboundWld)
      return visit((UnboundWld)node);
    if(node instanceof Dim)
      return visit((Dim)node);
    if(node instanceof Expr_JavaObject2)
      return visit((Expr_JavaObject2)node);
    if(node instanceof Field)
      return visit((Field)node);
    if(node instanceof SuperField)
      return visit((SuperField)node);
    if(node instanceof QSuperField)
      return visit((QSuperField)node);
    if(node instanceof Expr_JavaObject1)
      return visit((Expr_JavaObject1)node);
    if(node instanceof ArrayAccess)
      return visit((ArrayAccess)node);
    if(node instanceof ArraySubscript)
      return visit((ArraySubscript)node);
    if(node instanceof Invoke)
      return visit((Invoke)node);
    if(node instanceof Method0)
      return visit((Method0)node);
    if(node instanceof Method)
      return visit((Method)node);
    if(node instanceof SuperMethod)
      return visit((SuperMethod)node);
    if(node instanceof QSuperMethod)
      return visit((QSuperMethod)node);
    if(node instanceof GenericMethod)
      return visit((GenericMethod)node);
    if(node instanceof Expr_JavaObject0)
      return visit((Expr_JavaObject0)node);
    if(node instanceof PostIncr)
      return visit((PostIncr)node);
    if(node instanceof PostDecr)
      return visit((PostDecr)node);
    if(node instanceof Plus0)
      return visit((Plus0)node);
    if(node instanceof Minus0)
      return visit((Minus0)node);
    if(node instanceof PreIncr)
      return visit((PreIncr)node);
    if(node instanceof PreDecr)
      return visit((PreDecr)node);
    if(node instanceof Complement)
      return visit((Complement)node);
    if(node instanceof Not)
      return visit((Not)node);
    if(node instanceof CastPrim)
      return visit((CastPrim)node);
    if(node instanceof CastRef)
      return visit((CastRef)node);
    if(node instanceof Expr_JavaObject)
      return visit((Expr_JavaObject)node);
    if(node instanceof InstanceOf)
      return visit((InstanceOf)node);
    if(node instanceof Mul)
      return visit((Mul)node);
    if(node instanceof Div)
      return visit((Div)node);
    if(node instanceof Remain)
      return visit((Remain)node);
    if(node instanceof Plus)
      return visit((Plus)node);
    if(node instanceof Minus)
      return visit((Minus)node);
    if(node instanceof LeftShift)
      return visit((LeftShift)node);
    if(node instanceof RightShift)
      return visit((RightShift)node);
    if(node instanceof URightShift)
      return visit((URightShift)node);
    if(node instanceof Lt)
      return visit((Lt)node);
    if(node instanceof Gt)
      return visit((Gt)node);
    if(node instanceof LtEq)
      return visit((LtEq)node);
    if(node instanceof GtEq)
      return visit((GtEq)node);
    if(node instanceof Eq)
      return visit((Eq)node);
    if(node instanceof NotEq)
      return visit((NotEq)node);
    if(node instanceof LazyAnd)
      return visit((LazyAnd)node);
    if(node instanceof LazyOr)
      return visit((LazyOr)node);
    if(node instanceof And)
      return visit((And)node);
    if(node instanceof ExcOr)
      return visit((ExcOr)node);
    if(node instanceof Or)
      return visit((Or)node);
    if(node instanceof Cond)
      return visit((Cond)node);
    if(node instanceof CondMid)
      return visit((CondMid)node);
    if(node instanceof Assign)
      return visit((Assign)node);
    if(node instanceof AssignMul)
      return visit((AssignMul)node);
    if(node instanceof AssignDiv)
      return visit((AssignDiv)node);
    if(node instanceof AssignRemain)
      return visit((AssignRemain)node);
    if(node instanceof AssignPlus)
      return visit((AssignPlus)node);
    if(node instanceof AssignMinus)
      return visit((AssignMinus)node);
    if(node instanceof AssignLeftShift)
      return visit((AssignLeftShift)node);
    if(node instanceof AssignRightShift)
      return visit((AssignRightShift)node);
    if(node instanceof AssignURightShift)
      return visit((AssignURightShift)node);
    if(node instanceof AssignAnd)
      return visit((AssignAnd)node);
    if(node instanceof AssignExcOr)
      return visit((AssignExcOr)node);
    if(node instanceof AssignOr)
      return visit((AssignOr)node);
    if(node instanceof LHS_JavaObject1)
      return visit((LHS_JavaObject1)node);
    if(node instanceof LHS_JavaObject0)
      return visit((LHS_JavaObject0)node);
    if(node instanceof LHS_JavaObject)
      return visit((LHS_JavaObject)node);
    if(node instanceof Anno)
      return visit((Anno)node);
    if(node instanceof SingleElemAnno)
      return visit((SingleElemAnno)node);
    if(node instanceof MarkerAnno)
      return visit((MarkerAnno)node);
    if(node instanceof ElemValPair)
      return visit((ElemValPair)node);
    if(node instanceof ElemVal_JavaObject0)
      return visit((ElemVal_JavaObject0)node);
    if(node instanceof ElemVal_JavaObject)
      return visit((ElemVal_JavaObject)node);
    if(node instanceof ElemValArrayInit0)
      return visit((ElemValArrayInit0)node);
    if(node instanceof ElemValArrayInit)
      return visit((ElemValArrayInit)node);
    if(node instanceof PackageDec)
      return visit((PackageDec)node);
    if(node instanceof TypeImportDec)
      return visit((TypeImportDec)node);
    if(node instanceof TypeImportOnDemandDec)
      return visit((TypeImportOnDemandDec)node);
    if(node instanceof StaticImportDec)
      return visit((StaticImportDec)node);
    if(node instanceof StaticImportOnDemandDec)
      return visit((StaticImportOnDemandDec)node);
    if(node instanceof TypeDec_JavaObject0)
      return visit((TypeDec_JavaObject0)node);
    if(node instanceof TypeDec_JavaObject)
      return visit((TypeDec_JavaObject)node);
    if(node instanceof Semicolon)
      return visit((Semicolon)node);
    if(node instanceof CompilationUnit)
      return visit((CompilationUnit)node);
    if(node instanceof Metavar41)
      return visit((Metavar41)node);
    if(node instanceof Metavar40)
      return visit((Metavar40)node);
    if(node instanceof Metavar39)
      return visit((Metavar39)node);
    if(node instanceof Metavar38)
      return visit((Metavar38)node);
    if(node instanceof Metavar37)
      return visit((Metavar37)node);
    if(node instanceof Metavar36)
      return visit((Metavar36)node);
    if(node instanceof ToMetaExpr80)
      return visit((ToMetaExpr80)node);
    if(node instanceof ToMetaExpr79)
      return visit((ToMetaExpr79)node);
    if(node instanceof ToMetaExpr78)
      return visit((ToMetaExpr78)node);
    if(node instanceof ToMetaExpr77)
      return visit((ToMetaExpr77)node);
    if(node instanceof ToMetaExpr76)
      return visit((ToMetaExpr76)node);
    if(node instanceof ToMetaExpr75)
      return visit((ToMetaExpr75)node);
    if(node instanceof ToMetaExpr74)
      return visit((ToMetaExpr74)node);
    if(node instanceof ToMetaExpr73)
      return visit((ToMetaExpr73)node);
    if(node instanceof ToMetaExpr72)
      return visit((ToMetaExpr72)node);
    if(node instanceof ToMetaExpr71)
      return visit((ToMetaExpr71)node);
    if(node instanceof ToMetaListExpr7)
      return visit((ToMetaListExpr7)node);
    if(node instanceof ToMetaListExpr6)
      return visit((ToMetaListExpr6)node);
    if(node instanceof ToMetaExpr70)
      return visit((ToMetaExpr70)node);
    if(node instanceof ToMetaExpr69)
      return visit((ToMetaExpr69)node);
    if(node instanceof ToMetaListExpr5)
      return visit((ToMetaListExpr5)node);
    if(node instanceof ToMetaListExpr4)
      return visit((ToMetaListExpr4)node);
    if(node instanceof ToMetaExpr68)
      return visit((ToMetaExpr68)node);
    if(node instanceof ToMetaExpr67)
      return visit((ToMetaExpr67)node);
    if(node instanceof ToMetaListExpr3)
      return visit((ToMetaListExpr3)node);
    if(node instanceof ToMetaListExpr2)
      return visit((ToMetaListExpr2)node);
    if(node instanceof FromMetaExpr33)
      return visit((FromMetaExpr33)node);
    if(node instanceof FromMetaExpr32)
      return visit((FromMetaExpr32)node);
    if(node instanceof FromMetaExpr31)
      return visit((FromMetaExpr31)node);
    if(node instanceof FromMetaExpr30)
      return visit((FromMetaExpr30)node);
    if(node instanceof FromMetaExpr29)
      return visit((FromMetaExpr29)node);
    if(node instanceof FromMetaExpr28)
      return visit((FromMetaExpr28)node);
    if(node instanceof Metavar35)
      return visit((Metavar35)node);
    if(node instanceof Metavar34)
      return visit((Metavar34)node);
    if(node instanceof Metavar33)
      return visit((Metavar33)node);
    if(node instanceof Metavar32)
      return visit((Metavar32)node);
    if(node instanceof Metavar31)
      return visit((Metavar31)node);
    if(node instanceof Metavar30)
      return visit((Metavar30)node);
    if(node instanceof Metavar29)
      return visit((Metavar29)node);
    if(node instanceof Metavar28)
      return visit((Metavar28)node);
    if(node instanceof ToMetaExpr66)
      return visit((ToMetaExpr66)node);
    if(node instanceof ToMetaExpr65)
      return visit((ToMetaExpr65)node);
    if(node instanceof ToMetaExpr64)
      return visit((ToMetaExpr64)node);
    if(node instanceof ToMetaExpr63)
      return visit((ToMetaExpr63)node);
    if(node instanceof Metavar27)
      return visit((Metavar27)node);
    if(node instanceof Metavar26)
      return visit((Metavar26)node);
    if(node instanceof Metavar25)
      return visit((Metavar25)node);
    if(node instanceof Metavar24)
      return visit((Metavar24)node);
    if(node instanceof Metavar23)
      return visit((Metavar23)node);
    if(node instanceof MetaTypeVar_JavaObject)
      return visit((MetaTypeVar_JavaObject)node);
    if(node instanceof MetaPrimTypeVar_JavaObject)
      return visit((MetaPrimTypeVar_JavaObject)node);
    if(node instanceof MetaRefTypeVar_JavaObject)
      return visit((MetaRefTypeVar_JavaObject)node);
    if(node instanceof ToMetaExpr62)
      return visit((ToMetaExpr62)node);
    if(node instanceof ToMetaExpr61)
      return visit((ToMetaExpr61)node);
    if(node instanceof ToMetaExpr60)
      return visit((ToMetaExpr60)node);
    if(node instanceof ToMetaExpr59)
      return visit((ToMetaExpr59)node);
    if(node instanceof ToMetaExpr58)
      return visit((ToMetaExpr58)node);
    if(node instanceof ToMetaExpr57)
      return visit((ToMetaExpr57)node);
    if(node instanceof FromMetaExpr27)
      return visit((FromMetaExpr27)node);
    if(node instanceof FromMetaExpr26)
      return visit((FromMetaExpr26)node);
    if(node instanceof ToMetaExpr56)
      return visit((ToMetaExpr56)node);
    if(node instanceof ToMetaExpr55)
      return visit((ToMetaExpr55)node);
    if(node instanceof ToMetaExpr54)
      return visit((ToMetaExpr54)node);
    if(node instanceof ToMetaExpr53)
      return visit((ToMetaExpr53)node);
    if(node instanceof ToMetaExpr52)
      return visit((ToMetaExpr52)node);
    if(node instanceof ToMetaExpr51)
      return visit((ToMetaExpr51)node);
    if(node instanceof ToMetaExpr50)
      return visit((ToMetaExpr50)node);
    if(node instanceof ToMetaExpr49)
      return visit((ToMetaExpr49)node);
    if(node instanceof ToMetaExpr48)
      return visit((ToMetaExpr48)node);
    if(node instanceof ToMetaExpr47)
      return visit((ToMetaExpr47)node);
    if(node instanceof ToMetaExpr46)
      return visit((ToMetaExpr46)node);
    if(node instanceof ToMetaExpr45)
      return visit((ToMetaExpr45)node);
    if(node instanceof ToMetaExpr44)
      return visit((ToMetaExpr44)node);
    if(node instanceof ToMetaExpr43)
      return visit((ToMetaExpr43)node);
    if(node instanceof ToMetaExpr42)
      return visit((ToMetaExpr42)node);
    if(node instanceof ToMetaExpr41)
      return visit((ToMetaExpr41)node);
    if(node instanceof ToMetaExpr40)
      return visit((ToMetaExpr40)node);
    if(node instanceof ToMetaExpr39)
      return visit((ToMetaExpr39)node);
    if(node instanceof ToMetaExpr38)
      return visit((ToMetaExpr38)node);
    if(node instanceof ToMetaExpr37)
      return visit((ToMetaExpr37)node);
    if(node instanceof FromMetaExpr25)
      return visit((FromMetaExpr25)node);
    if(node instanceof ToMetaExpr36)
      return visit((ToMetaExpr36)node);
    if(node instanceof ToMetaExpr35)
      return visit((ToMetaExpr35)node);
    if(node instanceof ToMetaExpr34)
      return visit((ToMetaExpr34)node);
    if(node instanceof ToMetaExpr33)
      return visit((ToMetaExpr33)node);
    if(node instanceof ToMetaExpr32)
      return visit((ToMetaExpr32)node);
    if(node instanceof FromMetaExpr24)
      return visit((FromMetaExpr24)node);
    if(node instanceof FromMetaExpr23)
      return visit((FromMetaExpr23)node);
    if(node instanceof ToMetaExpr31)
      return visit((ToMetaExpr31)node);
    if(node instanceof ToMetaExpr30)
      return visit((ToMetaExpr30)node);
    if(node instanceof ToMetaExpr29)
      return visit((ToMetaExpr29)node);
    if(node instanceof ToMetaListExpr1)
      return visit((ToMetaListExpr1)node);
    if(node instanceof ToMetaListExpr0)
      return visit((ToMetaListExpr0)node);
    if(node instanceof FromMetaExpr22)
      return visit((FromMetaExpr22)node);
    if(node instanceof ToMetaExpr28)
      return visit((ToMetaExpr28)node);
    if(node instanceof ToMetaExpr27)
      return visit((ToMetaExpr27)node);
    if(node instanceof ToMetaExpr26)
      return visit((ToMetaExpr26)node);
    if(node instanceof ToMetaExpr25)
      return visit((ToMetaExpr25)node);
    if(node instanceof ToMetaExpr24)
      return visit((ToMetaExpr24)node);
    if(node instanceof ToMetaExpr23)
      return visit((ToMetaExpr23)node);
    if(node instanceof ToMetaExpr22)
      return visit((ToMetaExpr22)node);
    if(node instanceof ToMetaExpr21)
      return visit((ToMetaExpr21)node);
    if(node instanceof ToMetaExpr20)
      return visit((ToMetaExpr20)node);
    if(node instanceof ToMetaExpr19)
      return visit((ToMetaExpr19)node);
    if(node instanceof ToMetaExpr18)
      return visit((ToMetaExpr18)node);
    if(node instanceof ToMetaExpr17)
      return visit((ToMetaExpr17)node);
    if(node instanceof ToMetaExpr16)
      return visit((ToMetaExpr16)node);
    if(node instanceof ToMetaExpr15)
      return visit((ToMetaExpr15)node);
    if(node instanceof ToMetaExpr14)
      return visit((ToMetaExpr14)node);
    if(node instanceof ToMetaExpr13)
      return visit((ToMetaExpr13)node);
    if(node instanceof ToMetaExpr12)
      return visit((ToMetaExpr12)node);
    if(node instanceof ToMetaExpr11)
      return visit((ToMetaExpr11)node);
    if(node instanceof ToMetaExpr10)
      return visit((ToMetaExpr10)node);
    if(node instanceof ToMetaExpr9)
      return visit((ToMetaExpr9)node);
    if(node instanceof ToMetaExpr8)
      return visit((ToMetaExpr8)node);
    if(node instanceof ToMetaExpr7)
      return visit((ToMetaExpr7)node);
    if(node instanceof ToMetaListExpr)
      return visit((ToMetaListExpr)node);
    if(node instanceof ToMetaExpr6)
      return visit((ToMetaExpr6)node);
    if(node instanceof ToMetaExpr5)
      return visit((ToMetaExpr5)node);
    if(node instanceof ToMetaExpr4)
      return visit((ToMetaExpr4)node);
    if(node instanceof ToMetaExpr3)
      return visit((ToMetaExpr3)node);
    if(node instanceof ToMetaExpr2)
      return visit((ToMetaExpr2)node);
    if(node instanceof ToMetaExpr1)
      return visit((ToMetaExpr1)node);
    if(node instanceof ToMetaExpr0)
      return visit((ToMetaExpr0)node);
    if(node instanceof ToMetaExpr)
      return visit((ToMetaExpr)node);
    if(node instanceof FromMetaExpr21)
      return visit((FromMetaExpr21)node);
    if(node instanceof FromMetaExpr20)
      return visit((FromMetaExpr20)node);
    if(node instanceof FromMetaExpr19)
      return visit((FromMetaExpr19)node);
    if(node instanceof FromMetaExpr18)
      return visit((FromMetaExpr18)node);
    if(node instanceof FromMetaExpr17)
      return visit((FromMetaExpr17)node);
    if(node instanceof FromMetaExpr16)
      return visit((FromMetaExpr16)node);
    if(node instanceof FromMetaExpr15)
      return visit((FromMetaExpr15)node);
    if(node instanceof FromMetaExpr14)
      return visit((FromMetaExpr14)node);
    if(node instanceof FromMetaExpr13)
      return visit((FromMetaExpr13)node);
    if(node instanceof FromMetaExpr12)
      return visit((FromMetaExpr12)node);
    if(node instanceof FromMetaExpr11)
      return visit((FromMetaExpr11)node);
    if(node instanceof FromMetaExpr10)
      return visit((FromMetaExpr10)node);
    if(node instanceof FromMetaExpr9)
      return visit((FromMetaExpr9)node);
    if(node instanceof FromMetaExpr8)
      return visit((FromMetaExpr8)node);
    if(node instanceof FromMetaExpr7)
      return visit((FromMetaExpr7)node);
    if(node instanceof FromMetaExpr6)
      return visit((FromMetaExpr6)node);
    if(node instanceof FromMetaExpr5)
      return visit((FromMetaExpr5)node);
    if(node instanceof FromMetaExpr4)
      return visit((FromMetaExpr4)node);
    if(node instanceof FromMetaExpr3)
      return visit((FromMetaExpr3)node);
    if(node instanceof FromMetaExpr2)
      return visit((FromMetaExpr2)node);
    if(node instanceof FromMetaExpr1)
      return visit((FromMetaExpr1)node);
    if(node instanceof Metavar22)
      return visit((Metavar22)node);
    if(node instanceof Metavar21)
      return visit((Metavar21)node);
    if(node instanceof Metavar20)
      return visit((Metavar20)node);
    if(node instanceof Metavar19)
      return visit((Metavar19)node);
    if(node instanceof Metavar18)
      return visit((Metavar18)node);
    if(node instanceof Metavar17)
      return visit((Metavar17)node);
    if(node instanceof Metavar16)
      return visit((Metavar16)node);
    if(node instanceof Metavar15)
      return visit((Metavar15)node);
    if(node instanceof Metavar14)
      return visit((Metavar14)node);
    if(node instanceof Metavar13)
      return visit((Metavar13)node);
    if(node instanceof Metavar12)
      return visit((Metavar12)node);
    if(node instanceof Metavar11)
      return visit((Metavar11)node);
    if(node instanceof Metavar10)
      return visit((Metavar10)node);
    if(node instanceof Metavar9)
      return visit((Metavar9)node);
    if(node instanceof Metavar8)
      return visit((Metavar8)node);
    if(node instanceof Metavar7)
      return visit((Metavar7)node);
    if(node instanceof Metavar6)
      return visit((Metavar6)node);
    if(node instanceof Metavar5)
      return visit((Metavar5)node);
    if(node instanceof Metavar4)
      return visit((Metavar4)node);
    if(node instanceof Metavar3)
      return visit((Metavar3)node);
    if(node instanceof Metavar2)
      return visit((Metavar2)node);
    if(node instanceof Metavar1)
      return visit((Metavar1)node);
    if(node instanceof Metavar0)
      return visit((Metavar0)node);
    if(node instanceof Metavar)
      return visit((Metavar)node);
    if(node instanceof FromMetaExpr0)
      return visit((FromMetaExpr0)node);
    if(node instanceof FromMetaExpr)
      return visit((FromMetaExpr)node);
    if(node instanceof ListPlusOfCommChar0)
      return visit((ListPlusOfCommChar0)node);
    if(node instanceof ListStarOfCommChar0)
      return visit((ListStarOfCommChar0)node);
    if(node instanceof ListPlusOfModNamePart_StrategoHost0)
      return visit((ListPlusOfModNamePart_StrategoHost0)node);
    if(node instanceof ListStarOfModNamePart_StrategoHost0)
      return visit((ListStarOfModNamePart_StrategoHost0)node);
    if(node instanceof ListPlusOfStrChar_StrategoHost0)
      return visit((ListPlusOfStrChar_StrategoHost0)node);
    if(node instanceof ListStarOfStrChar_StrategoHost0)
      return visit((ListStarOfStrChar_StrategoHost0)node);
    if(node instanceof ListPlusOfCommentPart0)
      return visit((ListPlusOfCommentPart0)node);
    if(node instanceof ListStarOfCommentPart0)
      return visit((ListStarOfCommentPart0)node);
    if(node instanceof OptDeciFloatExponentPart_JavaObject0)
      return visit((OptDeciFloatExponentPart_JavaObject0)node);
    if(node instanceof OptWildcardBound_JavaObject0)
      return visit((OptWildcardBound_JavaObject0)node);
    if(node instanceof OptTypeBound_JavaObject0)
      return visit((OptTypeBound_JavaObject0)node);
    if(node instanceof OptId_JavaObject0)
      return visit((OptId_JavaObject0)node);
    if(node instanceof OptExpr_JavaObject0)
      return visit((OptExpr_JavaObject0)node);
    if(node instanceof OptConstrInv_JavaObject0)
      return visit((OptConstrInv_JavaObject0)node);
    if(node instanceof OptEnumBodyDecs_JavaObject0)
      return visit((OptEnumBodyDecs_JavaObject0)node);
    if(node instanceof OptEnumConstArgs_JavaObject0)
      return visit((OptEnumConstArgs_JavaObject0)node);
    if(node instanceof Anno_JavaObject_ConstantMod_JavaObject00)
      return visit((Anno_JavaObject_ConstantMod_JavaObject00)node);
    if(node instanceof Anno_JavaObject_ConstantMod_JavaObject0)
      return visit((Anno_JavaObject_ConstantMod_JavaObject0)node);
    if(node instanceof Anno_JavaObject_AbstractMethodMod_JavaObject00)
      return visit((Anno_JavaObject_AbstractMethodMod_JavaObject00)node);
    if(node instanceof Anno_JavaObject_AbstractMethodMod_JavaObject0)
      return visit((Anno_JavaObject_AbstractMethodMod_JavaObject0)node);
    if(node instanceof OptThrows_JavaObject0)
      return visit((OptThrows_JavaObject0)node);
    if(node instanceof OptDefaultVal_JavaObject0)
      return visit((OptDefaultVal_JavaObject0)node);
    if(node instanceof Anno_JavaObject_InterfaceMod_JavaObject00)
      return visit((Anno_JavaObject_InterfaceMod_JavaObject00)node);
    if(node instanceof Anno_JavaObject_InterfaceMod_JavaObject0)
      return visit((Anno_JavaObject_InterfaceMod_JavaObject0)node);
    if(node instanceof OptExtendsInterfaces_JavaObject0)
      return visit((OptExtendsInterfaces_JavaObject0)node);
    if(node instanceof OptTypeParams_JavaObject0)
      return visit((OptTypeParams_JavaObject0)node);
    if(node instanceof OptSuper_JavaObject0)
      return visit((OptSuper_JavaObject0)node);
    if(node instanceof OptInterfaces_JavaObject0)
      return visit((OptInterfaces_JavaObject0)node);
    if(node instanceof OptTypeArgs_JavaObject00)
      return visit((OptTypeArgs_JavaObject00)node);
    if(node instanceof OptPackageDec_JavaObject0)
      return visit((OptPackageDec_JavaObject0)node);
    if(node instanceof Anno_JavaObject_MethodMod_JavaObject00)
      return visit((Anno_JavaObject_MethodMod_JavaObject00)node);
    if(node instanceof Anno_JavaObject_MethodMod_JavaObject0)
      return visit((Anno_JavaObject_MethodMod_JavaObject0)node);
    if(node instanceof Anno_JavaObject_ClassMod_JavaObject00)
      return visit((Anno_JavaObject_ClassMod_JavaObject00)node);
    if(node instanceof Anno_JavaObject_ClassMod_JavaObject0)
      return visit((Anno_JavaObject_ClassMod_JavaObject0)node);
    if(node instanceof Anno_JavaObject_ConstrMod_JavaObject00)
      return visit((Anno_JavaObject_ConstrMod_JavaObject00)node);
    if(node instanceof Anno_JavaObject_ConstrMod_JavaObject0)
      return visit((Anno_JavaObject_ConstrMod_JavaObject0)node);
    if(node instanceof Anno_JavaObject_VarMod_JavaObject00)
      return visit((Anno_JavaObject_VarMod_JavaObject00)node);
    if(node instanceof Anno_JavaObject_VarMod_JavaObject0)
      return visit((Anno_JavaObject_VarMod_JavaObject0)node);
    if(node instanceof Anno_JavaObject_FieldMod_JavaObject00)
      return visit((Anno_JavaObject_FieldMod_JavaObject00)node);
    if(node instanceof Anno_JavaObject_FieldMod_JavaObject0)
      return visit((Anno_JavaObject_FieldMod_JavaObject0)node);
    if(node instanceof OptClassBody_JavaObject0)
      return visit((OptClassBody_JavaObject0)node);
    if(node instanceof OptTypeArgs_JavaObject0)
      return visit((OptTypeArgs_JavaObject0)node);
    if(node instanceof WsSort)
      return visit((WsSort)node);
    if(node instanceof ShortComSort)
      return visit((ShortComSort)node);
    if(node instanceof LongComSort)
      return visit((LongComSort)node);
    if(node instanceof EofSort)
      return visit((EofSort)node);
    if(node instanceof CommCharSort)
      return visit((CommCharSort)node);
    if(node instanceof AsteriskSort)
      return visit((AsteriskSort)node);
    if(node instanceof ModName_StrategoHostSort)
      return visit((ModName_StrategoHostSort)node);
    if(node instanceof ModNamePart_StrategoHostSort)
      return visit((ModNamePart_StrategoHostSort)node);
    if(node instanceof Id_StrategoHostSort)
      return visit((Id_StrategoHostSort)node);
    if(node instanceof LId_StrategoHostSort)
      return visit((LId_StrategoHostSort)node);
    if(node instanceof LCID_StrategoHostSort)
      return visit((LCID_StrategoHostSort)node);
    if(node instanceof UCID_StrategoHostSort)
      return visit((UCID_StrategoHostSort)node);
    if(node instanceof Keyword_StrategoHostSort)
      return visit((Keyword_StrategoHostSort)node);
    if(node instanceof Int_StrategoHostSort)
      return visit((Int_StrategoHostSort)node);
    if(node instanceof Real_StrategoHostSort)
      return visit((Real_StrategoHostSort)node);
    if(node instanceof String_StrategoHostSort)
      return visit((String_StrategoHostSort)node);
    if(node instanceof StrChar_StrategoHostSort)
      return visit((StrChar_StrategoHostSort)node);
    if(node instanceof Char_StrategoHostSort)
      return visit((Char_StrategoHostSort)node);
    if(node instanceof CharChar_StrategoHostSort)
      return visit((CharChar_StrategoHostSort)node);
    if(node instanceof Sdecl_StrategoHostSort)
      return visit((Sdecl_StrategoHostSort)node);
    if(node instanceof Opdecl_StrategoHostSort)
      return visit((Opdecl_StrategoHostSort)node);
    if(node instanceof ConstType_StrategoHostSort)
      return visit((ConstType_StrategoHostSort)node);
    if(node instanceof FunType_StrategoHostSort)
      return visit((FunType_StrategoHostSort)node);
    if(node instanceof ArgTypeSort)
      return visit((ArgTypeSort)node);
    if(node instanceof ArgType_StrategoHostSort)
      return visit((ArgType_StrategoHostSort)node);
    if(node instanceof RetType_StrategoHostSort)
      return visit((RetType_StrategoHostSort)node);
    if(node instanceof Type_StrategoHostSort)
      return visit((Type_StrategoHostSort)node);
    if(node instanceof SVar_StrategoHostSort)
      return visit((SVar_StrategoHostSort)node);
    if(node instanceof StrategyParen_StrategoHostSort)
      return visit((StrategyParen_StrategoHostSort)node);
    if(node instanceof StrategyMid_StrategoHostSort)
      return visit((StrategyMid_StrategoHostSort)node);
    if(node instanceof Module_StrategoHostSort)
      return visit((Module_StrategoHostSort)node);
    if(node instanceof ImportModName_StrategoHostSort)
      return visit((ImportModName_StrategoHostSort)node);
    if(node instanceof LID_StrategoHostSort)
      return visit((LID_StrategoHostSort)node);
    if(node instanceof Var_StrategoHostSort)
      return visit((Var_StrategoHostSort)node);
    if(node instanceof ID_StrategoHostSort)
      return visit((ID_StrategoHostSort)node);
    if(node instanceof PreTerm_StrategoHostSort)
      return visit((PreTerm_StrategoHostSort)node);
    if(node instanceof Sort_StrategoHostSort)
      return visit((Sort_StrategoHostSort)node);
    if(node instanceof Kind_StrategoHostSort)
      return visit((Kind_StrategoHostSort)node);
    if(node instanceof StrategyDef_StrategoHostSort)
      return visit((StrategyDef_StrategoHostSort)node);
    if(node instanceof Typedid_StrategoHostSort)
      return visit((Typedid_StrategoHostSort)node);
    if(node instanceof StrategyAngleSort)
      return visit((StrategyAngleSort)node);
    if(node instanceof StrategyCurly_StrategoHostSort)
      return visit((StrategyCurly_StrategoHostSort)node);
    if(node instanceof StrategySort)
      return visit((StrategySort)node);
    if(node instanceof SwitchCase_StrategoHostSort)
      return visit((SwitchCase_StrategoHostSort)node);
    if(node instanceof Overlay_StrategoHostSort)
      return visit((Overlay_StrategoHostSort)node);
    if(node instanceof RuleDef_StrategoHostSort)
      return visit((RuleDef_StrategoHostSort)node);
    if(node instanceof Rule_StrategoHostSort)
      return visit((Rule_StrategoHostSort)node);
    if(node instanceof Decl_StrategoHostSort)
      return visit((Decl_StrategoHostSort)node);
    if(node instanceof Def_StrategoHostSort)
      return visit((Def_StrategoHostSort)node);
    if(node instanceof ScopeLabels_StrategoHostSort)
      return visit((ScopeLabels_StrategoHostSort)node);
    if(node instanceof DynRuleDef_StrategoHostSort)
      return visit((DynRuleDef_StrategoHostSort)node);
    if(node instanceof DynRuleId_StrategoHostSort)
      return visit((DynRuleId_StrategoHostSort)node);
    if(node instanceof DynRuleScopeId_StrategoHostSort)
      return visit((DynRuleScopeId_StrategoHostSort)node);
    if(node instanceof RuleDec_StrategoHostSort)
      return visit((RuleDec_StrategoHostSort)node);
    if(node instanceof RuleNames_StrategoHostSort)
      return visit((RuleNames_StrategoHostSort)node);
    if(node instanceof Strategy_StrategoHostSort)
      return visit((Strategy_StrategoHostSort)node);
    if(node instanceof LineTerminatorSort)
      return visit((LineTerminatorSort)node);
    if(node instanceof CarriageReturnSort)
      return visit((CarriageReturnSort)node);
    if(node instanceof EndOfFileSort)
      return visit((EndOfFileSort)node);
    if(node instanceof CommentSort)
      return visit((CommentSort)node);
    if(node instanceof EOLCommentCharsSort)
      return visit((EOLCommentCharsSort)node);
    if(node instanceof CommentPartSort)
      return visit((CommentPartSort)node);
    if(node instanceof BlockCommentCharsSort)
      return visit((BlockCommentCharsSort)node);
    if(node instanceof EscEscCharSort)
      return visit((EscEscCharSort)node);
    if(node instanceof EscCharSort)
      return visit((EscCharSort)node);
    if(node instanceof UnicodeEscapeSort)
      return visit((UnicodeEscapeSort)node);
    if(node instanceof Keyword_JavaObjectSort)
      return visit((Keyword_JavaObjectSort)node);
    if(node instanceof Public_JavaObjectSort)
      return visit((Public_JavaObjectSort)node);
    if(node instanceof Private_JavaObjectSort)
      return visit((Private_JavaObjectSort)node);
    if(node instanceof Protected_JavaObjectSort)
      return visit((Protected_JavaObjectSort)node);
    if(node instanceof Abstract_JavaObjectSort)
      return visit((Abstract_JavaObjectSort)node);
    if(node instanceof Final_JavaObjectSort)
      return visit((Final_JavaObjectSort)node);
    if(node instanceof Static_JavaObjectSort)
      return visit((Static_JavaObjectSort)node);
    if(node instanceof Native_JavaObjectSort)
      return visit((Native_JavaObjectSort)node);
    if(node instanceof Transient_JavaObjectSort)
      return visit((Transient_JavaObjectSort)node);
    if(node instanceof Volatile_JavaObjectSort)
      return visit((Volatile_JavaObjectSort)node);
    if(node instanceof Synchronized_JavaObjectSort)
      return visit((Synchronized_JavaObjectSort)node);
    if(node instanceof StrictFP_JavaObjectSort)
      return visit((StrictFP_JavaObjectSort)node);
    if(node instanceof Modifier_JavaObjectSort)
      return visit((Modifier_JavaObjectSort)node);
    if(node instanceof DeciNumeral_JavaObjectSort)
      return visit((DeciNumeral_JavaObjectSort)node);
    if(node instanceof HexaNumeral_JavaObjectSort)
      return visit((HexaNumeral_JavaObjectSort)node);
    if(node instanceof OctaNumeral_JavaObjectSort)
      return visit((OctaNumeral_JavaObjectSort)node);
    if(node instanceof IntLiteral_JavaObjectSort)
      return visit((IntLiteral_JavaObjectSort)node);
    if(node instanceof DeciFloatLiteral_JavaObjectSort)
      return visit((DeciFloatLiteral_JavaObjectSort)node);
    if(node instanceof HexaFloatLiteral_JavaObjectSort)
      return visit((HexaFloatLiteral_JavaObjectSort)node);
    if(node instanceof DeciFloatNumeral_JavaObjectSort)
      return visit((DeciFloatNumeral_JavaObjectSort)node);
    if(node instanceof DeciFloatDigits_JavaObjectSort)
      return visit((DeciFloatDigits_JavaObjectSort)node);
    if(node instanceof DeciFloatExponentPart_JavaObjectSort)
      return visit((DeciFloatExponentPart_JavaObjectSort)node);
    if(node instanceof SignedInteger_JavaObjectSort)
      return visit((SignedInteger_JavaObjectSort)node);
    if(node instanceof HexaFloatNumeral_JavaObjectSort)
      return visit((HexaFloatNumeral_JavaObjectSort)node);
    if(node instanceof HexaSignificand_JavaObjectSort)
      return visit((HexaSignificand_JavaObjectSort)node);
    if(node instanceof BinaryExponent_JavaObjectSort)
      return visit((BinaryExponent_JavaObjectSort)node);
    if(node instanceof BoolLiteral_JavaObjectSort)
      return visit((BoolLiteral_JavaObjectSort)node);
    if(node instanceof Bool_JavaObjectSort)
      return visit((Bool_JavaObjectSort)node);
    if(node instanceof EscapeSeq_JavaObjectSort)
      return visit((EscapeSeq_JavaObjectSort)node);
    if(node instanceof NamedEscape_JavaObjectSort)
      return visit((NamedEscape_JavaObjectSort)node);
    if(node instanceof OctaEscape_JavaObjectSort)
      return visit((OctaEscape_JavaObjectSort)node);
    if(node instanceof LastOcta_JavaObjectSort)
      return visit((LastOcta_JavaObjectSort)node);
    if(node instanceof CharContent_JavaObjectSort)
      return visit((CharContent_JavaObjectSort)node);
    if(node instanceof StringPart_JavaObjectSort)
      return visit((StringPart_JavaObjectSort)node);
    if(node instanceof NullLiteral_JavaObjectSort)
      return visit((NullLiteral_JavaObjectSort)node);
    if(node instanceof NumType_JavaObjectSort)
      return visit((NumType_JavaObjectSort)node);
    if(node instanceof IntType_JavaObjectSort)
      return visit((IntType_JavaObjectSort)node);
    if(node instanceof FloatType_JavaObjectSort)
      return visit((FloatType_JavaObjectSort)node);
    if(node instanceof TypeArgs_JavaObjectSort)
      return visit((TypeArgs_JavaObjectSort)node);
    if(node instanceof ActualTypeArg_JavaObjectSort)
      return visit((ActualTypeArg_JavaObjectSort)node);
    if(node instanceof WildcardBound_JavaObjectSort)
      return visit((WildcardBound_JavaObjectSort)node);
    if(node instanceof TypeBound_JavaObjectSort)
      return visit((TypeBound_JavaObjectSort)node);
    if(node instanceof TypeParams_JavaObjectSort)
      return visit((TypeParams_JavaObjectSort)node);
    if(node instanceof TypeVarId_JavaObjectSort)
      return visit((TypeVarId_JavaObjectSort)node);
    if(node instanceof ClassOrInterfaceType_JavaObjectSort)
      return visit((ClassOrInterfaceType_JavaObjectSort)node);
    if(node instanceof ClassType_JavaObjectSort)
      return visit((ClassType_JavaObjectSort)node);
    if(node instanceof InterfaceType_JavaObjectSort)
      return visit((InterfaceType_JavaObjectSort)node);
    if(node instanceof TypeDecSpec_JavaObjectSort)
      return visit((TypeDecSpec_JavaObjectSort)node);
    if(node instanceof TypeVar_JavaObjectSort)
      return visit((TypeVar_JavaObjectSort)node);
    if(node instanceof ArrayType_JavaObjectSort)
      return visit((ArrayType_JavaObjectSort)node);
    if(node instanceof Literal_JavaObjectSort)
      return visit((Literal_JavaObjectSort)node);
    if(node instanceof ClassLiteral_JavaObjectSort)
      return visit((ClassLiteral_JavaObjectSort)node);
    if(node instanceof ExprSort)
      return visit((ExprSort)node);
    if(node instanceof ArrayInit_JavaObjectSort)
      return visit((ArrayInit_JavaObjectSort)node);
    if(node instanceof FieldDec_JavaObjectSort)
      return visit((FieldDec_JavaObjectSort)node);
    if(node instanceof VarDecId_JavaObjectSort)
      return visit((VarDecId_JavaObjectSort)node);
    if(node instanceof Dim_JavaObjectSort)
      return visit((Dim_JavaObjectSort)node);
    if(node instanceof VarInit_JavaObjectSort)
      return visit((VarInit_JavaObjectSort)node);
    if(node instanceof LocalVarDecStm_JavaObjectSort)
      return visit((LocalVarDecStm_JavaObjectSort)node);
    if(node instanceof SwitchBlock_JavaObjectSort)
      return visit((SwitchBlock_JavaObjectSort)node);
    if(node instanceof SwitchLabel_JavaObjectSort)
      return visit((SwitchLabel_JavaObjectSort)node);
    if(node instanceof CatchClause_JavaObjectSort)
      return visit((CatchClause_JavaObjectSort)node);
    if(node instanceof Block_JavaObjectSort)
      return visit((Block_JavaObjectSort)node);
    if(node instanceof MethodDec_JavaObjectSort)
      return visit((MethodDec_JavaObjectSort)node);
    if(node instanceof MethodDecHead_JavaObjectSort)
      return visit((MethodDecHead_JavaObjectSort)node);
    if(node instanceof ResultType_JavaObjectSort)
      return visit((ResultType_JavaObjectSort)node);
    if(node instanceof Throws_JavaObjectSort)
      return visit((Throws_JavaObjectSort)node);
    if(node instanceof ExceptionType_JavaObjectSort)
      return visit((ExceptionType_JavaObjectSort)node);
    if(node instanceof MethodBody_JavaObjectSort)
      return visit((MethodBody_JavaObjectSort)node);
    if(node instanceof InstanceInit_JavaObjectSort)
      return visit((InstanceInit_JavaObjectSort)node);
    if(node instanceof StaticInit_JavaObjectSort)
      return visit((StaticInit_JavaObjectSort)node);
    if(node instanceof ConstrDec_JavaObjectSort)
      return visit((ConstrDec_JavaObjectSort)node);
    if(node instanceof ConstrHead_JavaObjectSort)
      return visit((ConstrHead_JavaObjectSort)node);
    if(node instanceof ConstrBody_JavaObjectSort)
      return visit((ConstrBody_JavaObjectSort)node);
    if(node instanceof ConstrInv_JavaObjectSort)
      return visit((ConstrInv_JavaObjectSort)node);
    if(node instanceof EnumDec_JavaObjectSort)
      return visit((EnumDec_JavaObjectSort)node);
    if(node instanceof EnumDecHead_JavaObjectSort)
      return visit((EnumDecHead_JavaObjectSort)node);
    if(node instanceof EnumBody_JavaObjectSort)
      return visit((EnumBody_JavaObjectSort)node);
    if(node instanceof EnumConst_JavaObjectSort)
      return visit((EnumConst_JavaObjectSort)node);
    if(node instanceof EnumConstArgsSort)
      return visit((EnumConstArgsSort)node);
    if(node instanceof EnumBodyDecs_JavaObjectSort)
      return visit((EnumBodyDecs_JavaObjectSort)node);
    if(node instanceof ConstantDec_JavaObjectSort)
      return visit((ConstantDec_JavaObjectSort)node);
    if(node instanceof ConstantMod_JavaObjectSort)
      return visit((ConstantMod_JavaObjectSort)node);
    if(node instanceof AbstractMethodDec_JavaObjectSort)
      return visit((AbstractMethodDec_JavaObjectSort)node);
    if(node instanceof AbstractMethodMod_JavaObjectSort)
      return visit((AbstractMethodMod_JavaObjectSort)node);
    if(node instanceof AnnoDec_JavaObjectSort)
      return visit((AnnoDec_JavaObjectSort)node);
    if(node instanceof AnnoDecHead_JavaObjectSort)
      return visit((AnnoDecHead_JavaObjectSort)node);
    if(node instanceof AnnoElemDec_JavaObjectSort)
      return visit((AnnoElemDec_JavaObjectSort)node);
    if(node instanceof DefaultVal_JavaObjectSort)
      return visit((DefaultVal_JavaObjectSort)node);
    if(node instanceof InterfaceDec_JavaObjectSort)
      return visit((InterfaceDec_JavaObjectSort)node);
    if(node instanceof InterfaceDecHead_JavaObjectSort)
      return visit((InterfaceDecHead_JavaObjectSort)node);
    if(node instanceof ExtendsInterfaces_JavaObjectSort)
      return visit((ExtendsInterfaces_JavaObjectSort)node);
    if(node instanceof InterfaceMod_JavaObjectSort)
      return visit((InterfaceMod_JavaObjectSort)node);
    if(node instanceof ClassDec_JavaObjectSort)
      return visit((ClassDec_JavaObjectSort)node);
    if(node instanceof ClassBody_JavaObjectSort)
      return visit((ClassBody_JavaObjectSort)node);
    if(node instanceof ClassDecHead_JavaObjectSort)
      return visit((ClassDecHead_JavaObjectSort)node);
    if(node instanceof Super_JavaObjectSort)
      return visit((Super_JavaObjectSort)node);
    if(node instanceof Interfaces_JavaObjectSort)
      return visit((Interfaces_JavaObjectSort)node);
    if(node instanceof ClassMemberDec_JavaObjectSort)
      return visit((ClassMemberDec_JavaObjectSort)node);
    if(node instanceof ArrayCreationExpr_JavaObjectSort)
      return visit((ArrayCreationExpr_JavaObjectSort)node);
    if(node instanceof ArrayBaseType_JavaObjectSort)
      return visit((ArrayBaseType_JavaObjectSort)node);
    if(node instanceof DimExpr_JavaObjectSort)
      return visit((DimExpr_JavaObjectSort)node);
    if(node instanceof FieldAccess_JavaObjectSort)
      return visit((FieldAccess_JavaObjectSort)node);
    if(node instanceof ArrayAccess_JavaObjectSort)
      return visit((ArrayAccess_JavaObjectSort)node);
    if(node instanceof ArraySubscriptSort)
      return visit((ArraySubscriptSort)node);
    if(node instanceof MethodSpec_JavaObjectSort)
      return visit((MethodSpec_JavaObjectSort)node);
    if(node instanceof CondMidSort)
      return visit((CondMidSort)node);
    if(node instanceof Anno_JavaObjectSort)
      return visit((Anno_JavaObjectSort)node);
    if(node instanceof ElemValPair_JavaObjectSort)
      return visit((ElemValPair_JavaObjectSort)node);
    if(node instanceof ElemVal_JavaObjectSort)
      return visit((ElemVal_JavaObjectSort)node);
    if(node instanceof CompilationUnit_JavaObjectSort)
      return visit((CompilationUnit_JavaObjectSort)node);
    if(node instanceof SwitchGroup_JavaObjectSort)
      return visit((SwitchGroup_JavaObjectSort)node);
    if(node instanceof Stm_JavaObjectSort)
      return visit((Stm_JavaObjectSort)node);
    if(node instanceof BlockStm_JavaObjectSort)
      return visit((BlockStm_JavaObjectSort)node);
    if(node instanceof LocalVarDec_JavaObjectSort)
      return visit((LocalVarDec_JavaObjectSort)node);
    if(node instanceof VarDec_JavaObjectSort)
      return visit((VarDec_JavaObjectSort)node);
    if(node instanceof LHS_JavaObjectSort)
      return visit((LHS_JavaObjectSort)node);
    if(node instanceof PrimType_JavaObjectSort)
      return visit((PrimType_JavaObjectSort)node);
    if(node instanceof RefType_JavaObjectSort)
      return visit((RefType_JavaObjectSort)node);
    if(node instanceof MetaTypeVar_JavaObjectSort)
      return visit((MetaTypeVar_JavaObjectSort)node);
    if(node instanceof MetaPrimTypeVar_JavaObjectSort)
      return visit((MetaPrimTypeVar_JavaObjectSort)node);
    if(node instanceof MetaRefTypeVar_JavaObjectSort)
      return visit((MetaRefTypeVar_JavaObjectSort)node);
    if(node instanceof Type_JavaObjectSort)
      return visit((Type_JavaObjectSort)node);
    if(node instanceof PackageDec_JavaObjectSort)
      return visit((PackageDec_JavaObjectSort)node);
    if(node instanceof OptPackageDec_JavaObject0Sort)
      return visit((OptPackageDec_JavaObject0Sort)node);
    if(node instanceof ImportDec_JavaObjectSort)
      return visit((ImportDec_JavaObjectSort)node);
    if(node instanceof Term_StrategoHostSort)
      return visit((Term_StrategoHostSort)node);
    if(node instanceof Name_JavaObjectSort)
      return visit((Name_JavaObjectSort)node);
    if(node instanceof Id_JavaObjectSort)
      return visit((Id_JavaObjectSort)node);
    if(node instanceof ID_JavaObjectSort)
      return visit((ID_JavaObjectSort)node);
    if(node instanceof TypeParam_JavaObjectSort)
      return visit((TypeParam_JavaObjectSort)node);
    if(node instanceof Expr_JavaObjectSort)
      return visit((Expr_JavaObjectSort)node);
    if(node instanceof TypeDec_JavaObjectSort)
      return visit((TypeDec_JavaObjectSort)node);
    if(node instanceof ClassBodyDec_JavaObjectSort)
      return visit((ClassBodyDec_JavaObjectSort)node);
    if(node instanceof InterfaceMemberDec_JavaObjectSort)
      return visit((InterfaceMemberDec_JavaObjectSort)node);
    if(node instanceof DeciLiteral_JavaObjectSort)
      return visit((DeciLiteral_JavaObjectSort)node);
    if(node instanceof HexaLiteral_JavaObjectSort)
      return visit((HexaLiteral_JavaObjectSort)node);
    if(node instanceof OctaLiteral_JavaObjectSort)
      return visit((OctaLiteral_JavaObjectSort)node);
    if(node instanceof FloatLiteral_JavaObjectSort)
      return visit((FloatLiteral_JavaObjectSort)node);
    if(node instanceof StringLiteral_JavaObjectSort)
      return visit((StringLiteral_JavaObjectSort)node);
    if(node instanceof CharLiteral_JavaObjectSort)
      return visit((CharLiteral_JavaObjectSort)node);
    if(node instanceof AmbName_JavaObjectSort)
      return visit((AmbName_JavaObjectSort)node);
    if(node instanceof ExprName_JavaObjectSort)
      return visit((ExprName_JavaObjectSort)node);
    if(node instanceof MethodName_JavaObjectSort)
      return visit((MethodName_JavaObjectSort)node);
    if(node instanceof TypeName_JavaObjectSort)
      return visit((TypeName_JavaObjectSort)node);
    if(node instanceof PackageOrTypeName_JavaObjectSort)
      return visit((PackageOrTypeName_JavaObjectSort)node);
    if(node instanceof PackageName_JavaObjectSort)
      return visit((PackageName_JavaObjectSort)node);
    if(node instanceof MethodMod_JavaObjectSort)
      return visit((MethodMod_JavaObjectSort)node);
    if(node instanceof ClassMod_JavaObjectSort)
      return visit((ClassMod_JavaObjectSort)node);
    if(node instanceof ConstrMod_JavaObjectSort)
      return visit((ConstrMod_JavaObjectSort)node);
    if(node instanceof VarMod_JavaObjectSort)
      return visit((VarMod_JavaObjectSort)node);
    if(node instanceof FieldMod_JavaObjectSort)
      return visit((FieldMod_JavaObjectSort)node);
    if(node instanceof FormalParam_JavaObjectSort)
      return visit((FormalParam_JavaObjectSort)node);
    if(node instanceof StringChars_JavaObjectSort)
      return visit((StringChars_JavaObjectSort)node);
    if(node instanceof SingleChar_JavaObjectSort)
      return visit((SingleChar_JavaObjectSort)node);
    if(node instanceof ListPlusOfCommChar0Sort)
      return visit((ListPlusOfCommChar0Sort)node);
    if(node instanceof ListStarOfCommChar0Sort)
      return visit((ListStarOfCommChar0Sort)node);
    if(node instanceof ListPlusOfModNamePart_StrategoHost0Sort)
      return visit((ListPlusOfModNamePart_StrategoHost0Sort)node);
    if(node instanceof ListStarOfModNamePart_StrategoHost0Sort)
      return visit((ListStarOfModNamePart_StrategoHost0Sort)node);
    if(node instanceof ListPlusOfStrChar_StrategoHost0Sort)
      return visit((ListPlusOfStrChar_StrategoHost0Sort)node);
    if(node instanceof ListStarOfStrChar_StrategoHost0Sort)
      return visit((ListStarOfStrChar_StrategoHost0Sort)node);
    if(node instanceof ListStarOfSort_StrategoHost1Sort)
      return visit((ListStarOfSort_StrategoHost1Sort)node);
    if(node instanceof ListStarOfOpdecl_StrategoHost0Sort)
      return visit((ListStarOfOpdecl_StrategoHost0Sort)node);
    if(node instanceof ListStarOfArgType_StrategoHost0Sort)
      return visit((ListStarOfArgType_StrategoHost0Sort)node);
    if(node instanceof ListStarOfID_StrategoHost0Sort)
      return visit((ListStarOfID_StrategoHost0Sort)node);
    if(node instanceof ListStarOfDecl_StrategoHost0Sort)
      return visit((ListStarOfDecl_StrategoHost0Sort)node);
    if(node instanceof ListStarOfImportModName_StrategoHost0Sort)
      return visit((ListStarOfImportModName_StrategoHost0Sort)node);
    if(node instanceof ListStarOfSdecl_StrategoHost0Sort)
      return visit((ListStarOfSdecl_StrategoHost0Sort)node);
    if(node instanceof ListStarOfSort_StrategoHost0Sort)
      return visit((ListStarOfSort_StrategoHost0Sort)node);
    if(node instanceof ListStarOfStrategy_StrategoHost0Sort)
      return visit((ListStarOfStrategy_StrategoHost0Sort)node);
    if(node instanceof ListStarOfSwitchCase_StrategoHost0Sort)
      return visit((ListStarOfSwitchCase_StrategoHost0Sort)node);
    if(node instanceof ListStarOfDef_StrategoHost0Sort)
      return visit((ListStarOfDef_StrategoHost0Sort)node);
    if(node instanceof ListStarOfOverlay_StrategoHost0Sort)
      return visit((ListStarOfOverlay_StrategoHost0Sort)node);
    if(node instanceof ListStarOfDynRuleScopeId_StrategoHost0Sort)
      return visit((ListStarOfDynRuleScopeId_StrategoHost0Sort)node);
    if(node instanceof ListStarOfDynRuleDef_StrategoHost0Sort)
      return visit((ListStarOfDynRuleDef_StrategoHost0Sort)node);
    if(node instanceof ListStarOfTypedid_StrategoHost0Sort)
      return visit((ListStarOfTypedid_StrategoHost0Sort)node);
    if(node instanceof ListStarOfId_StrategoHost0Sort)
      return visit((ListStarOfId_StrategoHost0Sort)node);
    if(node instanceof ListStarOfCharClass1Sort)
      return visit((ListStarOfCharClass1Sort)node);
    if(node instanceof ListPlusOfCommentPart0Sort)
      return visit((ListPlusOfCommentPart0Sort)node);
    if(node instanceof ListStarOfCommentPart0Sort)
      return visit((ListStarOfCommentPart0Sort)node);
    if(node instanceof OptDeciFloatExponentPart_JavaObject0Sort)
      return visit((OptDeciFloatExponentPart_JavaObject0Sort)node);
    if(node instanceof ListStarOfStringPart_JavaObject0Sort)
      return visit((ListStarOfStringPart_JavaObject0Sort)node);
    if(node instanceof ListStarOfCharClass0Sort)
      return visit((ListStarOfCharClass0Sort)node);
    if(node instanceof ListStarOfId_JavaObject0Sort)
      return visit((ListStarOfId_JavaObject0Sort)node);
    if(node instanceof ListStarOfActualTypeArg_JavaObject0Sort)
      return visit((ListStarOfActualTypeArg_JavaObject0Sort)node);
    if(node instanceof ListStarOfClassOrInterfaceType_JavaObject0Sort)
      return visit((ListStarOfClassOrInterfaceType_JavaObject0Sort)node);
    if(node instanceof ListStarOfSwitchLabel_JavaObject0Sort)
      return visit((ListStarOfSwitchLabel_JavaObject0Sort)node);
    if(node instanceof ListStarOfBlockStm_JavaObject0Sort)
      return visit((ListStarOfBlockStm_JavaObject0Sort)node);
    if(node instanceof ListStarOfCatchClause_JavaObject0Sort)
      return visit((ListStarOfCatchClause_JavaObject0Sort)node);
    if(node instanceof ListStarOfExceptionType_JavaObject0Sort)
      return visit((ListStarOfExceptionType_JavaObject0Sort)node);
    if(node instanceof ListStarOfEnumConst_JavaObject0Sort)
      return visit((ListStarOfEnumConst_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject_ConstantMod_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject_ConstantMod_JavaObject0Sort)node);
    if(node instanceof Anno_JavaObject_ConstantMod_JavaObject0Sort)
      return visit((Anno_JavaObject_ConstantMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject_AbstractMethodMod_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject_AbstractMethodMod_JavaObject0Sort)node);
    if(node instanceof Anno_JavaObject_AbstractMethodMod_JavaObject0Sort)
      return visit((Anno_JavaObject_AbstractMethodMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnnoElemDec_JavaObject0Sort)
      return visit((ListStarOfAnnoElemDec_JavaObject0Sort)node);
    if(node instanceof ListStarOfAbstractMethodMod_JavaObject0Sort)
      return visit((ListStarOfAbstractMethodMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject_InterfaceMod_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject_InterfaceMod_JavaObject0Sort)node);
    if(node instanceof Anno_JavaObject_InterfaceMod_JavaObject0Sort)
      return visit((Anno_JavaObject_InterfaceMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfInterfaceType_JavaObject0Sort)
      return visit((ListStarOfInterfaceType_JavaObject0Sort)node);
    if(node instanceof ListStarOfDimExpr_JavaObject0Sort)
      return visit((ListStarOfDimExpr_JavaObject0Sort)node);
    if(node instanceof ListStarOfDim_JavaObject0Sort)
      return visit((ListStarOfDim_JavaObject0Sort)node);
    if(node instanceof OptTypeArgs_JavaObject0Sort)
      return visit((OptTypeArgs_JavaObject0Sort)node);
    if(node instanceof ListStarOfElemValPair_JavaObject0Sort)
      return visit((ListStarOfElemValPair_JavaObject0Sort)node);
    if(node instanceof ListStarOfElemVal_JavaObject0Sort)
      return visit((ListStarOfElemVal_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject0Sort)node);
    if(node instanceof ListStarOfTypeDec_JavaObject0Sort)
      return visit((ListStarOfTypeDec_JavaObject0Sort)node);
    if(node instanceof ListStarOfVarInit_JavaObject0Sort)
      return visit((ListStarOfVarInit_JavaObject0Sort)node);
    if(node instanceof ListStarOfExpr_JavaObject0Sort)
      return visit((ListStarOfExpr_JavaObject0Sort)node);
    if(node instanceof ListStarOfSwitchGroup_JavaObject0Sort)
      return visit((ListStarOfSwitchGroup_JavaObject0Sort)node);
    if(node instanceof ListStarOfVarDec_JavaObject0Sort)
      return visit((ListStarOfVarDec_JavaObject0Sort)node);
    if(node instanceof ListStarOfImportDec_JavaObject0Sort)
      return visit((ListStarOfImportDec_JavaObject0Sort)node);
    if(node instanceof ListStarOfAbstractMethodDec_JavaObject0Sort)
      return visit((ListStarOfAbstractMethodDec_JavaObject0Sort)node);
    if(node instanceof ListStarOfTypeParam_JavaObject0Sort)
      return visit((ListStarOfTypeParam_JavaObject0Sort)node);
    if(node instanceof ListStarOfClassBodyDec_JavaObject0Sort)
      return visit((ListStarOfClassBodyDec_JavaObject0Sort)node);
    if(node instanceof ListStarOfInterfaceMemberDec_JavaObject0Sort)
      return visit((ListStarOfInterfaceMemberDec_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject_MethodMod_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject_MethodMod_JavaObject0Sort)node);
    if(node instanceof Anno_JavaObject_MethodMod_JavaObject0Sort)
      return visit((Anno_JavaObject_MethodMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject_ClassMod_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject_ClassMod_JavaObject0Sort)node);
    if(node instanceof Anno_JavaObject_ClassMod_JavaObject0Sort)
      return visit((Anno_JavaObject_ClassMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject_ConstrMod_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject_ConstrMod_JavaObject0Sort)node);
    if(node instanceof Anno_JavaObject_ConstrMod_JavaObject0Sort)
      return visit((Anno_JavaObject_ConstrMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject_VarMod_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject_VarMod_JavaObject0Sort)node);
    if(node instanceof Anno_JavaObject_VarMod_JavaObject0Sort)
      return visit((Anno_JavaObject_VarMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfAnno_JavaObject_FieldMod_JavaObject0Sort)
      return visit((ListStarOfAnno_JavaObject_FieldMod_JavaObject0Sort)node);
    if(node instanceof Anno_JavaObject_FieldMod_JavaObject0Sort)
      return visit((Anno_JavaObject_FieldMod_JavaObject0Sort)node);
    if(node instanceof ListStarOfFormalParam_JavaObject0Sort)
      return visit((ListStarOfFormalParam_JavaObject0Sort)node);
    if(node instanceof ListStarOfTerm_StrategoHost0Sort)
      return visit((ListStarOfTerm_StrategoHost0Sort)node);
    if(node instanceof ASTString)
      return visit((ASTString)node);
    if(node instanceof List)
      return visit((List)node);
    throw new java.lang.IllegalArgumentException("Node of type " + node.getClass().getSimpleName() + "not expected.");
  }

  public void endVisit(ASTNode node)
  { 
    if(node instanceof Ws)
    { 
      endVisit((Ws)node);
      return;
    }
    if(node instanceof ShortCom)
    { 
      endVisit((ShortCom)node);
      return;
    }
    if(node instanceof LongCom)
    { 
      endVisit((LongCom)node);
      return;
    }
    if(node instanceof Eof)
    { 
      endVisit((Eof)node);
      return;
    }
    if(node instanceof CommChar)
    { 
      endVisit((CommChar)node);
      return;
    }
    if(node instanceof Asterisk)
    { 
      endVisit((Asterisk)node);
      return;
    }
    if(node instanceof ModName_StrategoHost)
    { 
      endVisit((ModName_StrategoHost)node);
      return;
    }
    if(node instanceof ModNamePart_StrategoHost)
    { 
      endVisit((ModNamePart_StrategoHost)node);
      return;
    }
    if(node instanceof Id_StrategoHost)
    { 
      endVisit((Id_StrategoHost)node);
      return;
    }
    if(node instanceof LId_StrategoHost)
    { 
      endVisit((LId_StrategoHost)node);
      return;
    }
    if(node instanceof LCID_StrategoHost)
    { 
      endVisit((LCID_StrategoHost)node);
      return;
    }
    if(node instanceof UCID_StrategoHost)
    { 
      endVisit((UCID_StrategoHost)node);
      return;
    }
    if(node instanceof Keyword_StrategoHost)
    { 
      endVisit((Keyword_StrategoHost)node);
      return;
    }
    if(node instanceof Int_StrategoHost)
    { 
      endVisit((Int_StrategoHost)node);
      return;
    }
    if(node instanceof Real_StrategoHost)
    { 
      endVisit((Real_StrategoHost)node);
      return;
    }
    if(node instanceof String_StrategoHost)
    { 
      endVisit((String_StrategoHost)node);
      return;
    }
    if(node instanceof StrChar_StrategoHost)
    { 
      endVisit((StrChar_StrategoHost)node);
      return;
    }
    if(node instanceof Char_StrategoHost)
    { 
      endVisit((Char_StrategoHost)node);
      return;
    }
    if(node instanceof CharChar_StrategoHost)
    { 
      endVisit((CharChar_StrategoHost)node);
      return;
    }
    if(node instanceof Var0)
    { 
      endVisit((Var0)node);
      return;
    }
    if(node instanceof ID_StrategoHost0)
    { 
      endVisit((ID_StrategoHost0)node);
      return;
    }
    if(node instanceof PreTerm_StrategoHost)
    { 
      endVisit((PreTerm_StrategoHost)node);
      return;
    }
    if(node instanceof Term_StrategoHost)
    { 
      endVisit((Term_StrategoHost)node);
      return;
    }
    if(node instanceof Wld0)
    { 
      endVisit((Wld0)node);
      return;
    }
    if(node instanceof Wld)
    { 
      endVisit((Wld)node);
      return;
    }
    if(node instanceof Int0)
    { 
      endVisit((Int0)node);
      return;
    }
    if(node instanceof Real)
    { 
      endVisit((Real)node);
      return;
    }
    if(node instanceof Str)
    { 
      endVisit((Str)node);
      return;
    }
    if(node instanceof Op)
    { 
      endVisit((Op)node);
      return;
    }
    if(node instanceof OpQ)
    { 
      endVisit((OpQ)node);
      return;
    }
    if(node instanceof Explode)
    { 
      endVisit((Explode)node);
      return;
    }
    if(node instanceof Anno0)
    { 
      endVisit((Anno0)node);
      return;
    }
    if(node instanceof As0)
    { 
      endVisit((As0)node);
      return;
    }
    if(node instanceof As)
    { 
      endVisit((As)node);
      return;
    }
    if(node instanceof Sorts)
    { 
      endVisit((Sorts)node);
      return;
    }
    if(node instanceof Constructors)
    { 
      endVisit((Constructors)node);
      return;
    }
    if(node instanceof SortVar)
    { 
      endVisit((SortVar)node);
      return;
    }
    if(node instanceof SortNoArgs)
    { 
      endVisit((SortNoArgs)node);
      return;
    }
    if(node instanceof Sort)
    { 
      endVisit((Sort)node);
      return;
    }
    if(node instanceof OpDecl)
    { 
      endVisit((OpDecl)node);
      return;
    }
    if(node instanceof OpDeclQ)
    { 
      endVisit((OpDeclQ)node);
      return;
    }
    if(node instanceof OpDeclInj)
    { 
      endVisit((OpDeclInj)node);
      return;
    }
    if(node instanceof ConstType)
    { 
      endVisit((ConstType)node);
      return;
    }
    if(node instanceof FunType)
    { 
      endVisit((FunType)node);
      return;
    }
    if(node instanceof ArgType)
    { 
      endVisit((ArgType)node);
      return;
    }
    if(node instanceof ArgType_StrategoHost)
    { 
      endVisit((ArgType_StrategoHost)node);
      return;
    }
    if(node instanceof RetType_StrategoHost)
    { 
      endVisit((RetType_StrategoHost)node);
      return;
    }
    if(node instanceof Type_StrategoHost0)
    { 
      endVisit((Type_StrategoHost0)node);
      return;
    }
    if(node instanceof Type_StrategoHost)
    { 
      endVisit((Type_StrategoHost)node);
      return;
    }
    if(node instanceof Def_StrategoHost0)
    { 
      endVisit((Def_StrategoHost0)node);
      return;
    }
    if(node instanceof SVar)
    { 
      endVisit((SVar)node);
      return;
    }
    if(node instanceof Let)
    { 
      endVisit((Let)node);
      return;
    }
    if(node instanceof CallT)
    { 
      endVisit((CallT)node);
      return;
    }
    if(node instanceof CallDynamic)
    { 
      endVisit((CallDynamic)node);
      return;
    }
    if(node instanceof SDefT)
    { 
      endVisit((SDefT)node);
      return;
    }
    if(node instanceof ExtSDefInl)
    { 
      endVisit((ExtSDefInl)node);
      return;
    }
    if(node instanceof ExtSDef)
    { 
      endVisit((ExtSDef)node);
      return;
    }
    if(node instanceof VarDec1)
    { 
      endVisit((VarDec1)node);
      return;
    }
    if(node instanceof ParenStrat)
    { 
      endVisit((ParenStrat)node);
      return;
    }
    if(node instanceof Fail)
    { 
      endVisit((Fail)node);
      return;
    }
    if(node instanceof Id0)
    { 
      endVisit((Id0)node);
      return;
    }
    if(node instanceof Match)
    { 
      endVisit((Match)node);
      return;
    }
    if(node instanceof Build)
    { 
      endVisit((Build)node);
      return;
    }
    if(node instanceof Scope)
    { 
      endVisit((Scope)node);
      return;
    }
    if(node instanceof Seq)
    { 
      endVisit((Seq)node);
      return;
    }
    if(node instanceof GuardedLChoice)
    { 
      endVisit((GuardedLChoice)node);
      return;
    }
    if(node instanceof StrategyMid_StrategoHost)
    { 
      endVisit((StrategyMid_StrategoHost)node);
      return;
    }
    if(node instanceof PrimT)
    { 
      endVisit((PrimT)node);
      return;
    }
    if(node instanceof Some)
    { 
      endVisit((Some)node);
      return;
    }
    if(node instanceof One)
    { 
      endVisit((One)node);
      return;
    }
    if(node instanceof All)
    { 
      endVisit((All)node);
      return;
    }
    if(node instanceof ImportTerm)
    { 
      endVisit((ImportTerm)node);
      return;
    }
    if(node instanceof Module)
    { 
      endVisit((Module)node);
      return;
    }
    if(node instanceof Specification)
    { 
      endVisit((Specification)node);
      return;
    }
    if(node instanceof Imports)
    { 
      endVisit((Imports)node);
      return;
    }
    if(node instanceof Strategies)
    { 
      endVisit((Strategies)node);
      return;
    }
    if(node instanceof Signature)
    { 
      endVisit((Signature)node);
      return;
    }
    if(node instanceof Import)
    { 
      endVisit((Import)node);
      return;
    }
    if(node instanceof ImportWildcard)
    { 
      endVisit((ImportWildcard)node);
      return;
    }
    if(node instanceof ListVar)
    { 
      endVisit((ListVar)node);
      return;
    }
    if(node instanceof Var)
    { 
      endVisit((Var)node);
      return;
    }
    if(node instanceof ID_StrategoHost)
    { 
      endVisit((ID_StrategoHost)node);
      return;
    }
    if(node instanceof BuildDefaultPT)
    { 
      endVisit((BuildDefaultPT)node);
      return;
    }
    if(node instanceof BuildDefault)
    { 
      endVisit((BuildDefault)node);
      return;
    }
    if(node instanceof Char1)
    { 
      endVisit((Char1)node);
      return;
    }
    if(node instanceof AnnoList)
    { 
      endVisit((AnnoList)node);
      return;
    }
    if(node instanceof NoAnnoList)
    { 
      endVisit((NoAnnoList)node);
      return;
    }
    if(node instanceof App0)
    { 
      endVisit((App0)node);
      return;
    }
    if(node instanceof App)
    { 
      endVisit((App)node);
      return;
    }
    if(node instanceof RootApp0)
    { 
      endVisit((RootApp0)node);
      return;
    }
    if(node instanceof RootApp)
    { 
      endVisit((RootApp)node);
      return;
    }
    if(node instanceof Tuple)
    { 
      endVisit((Tuple)node);
      return;
    }
    if(node instanceof List0)
    { 
      endVisit((List0)node);
      return;
    }
    if(node instanceof ListTail)
    { 
      endVisit((ListTail)node);
      return;
    }
    if(node instanceof SortList)
    { 
      endVisit((SortList)node);
      return;
    }
    if(node instanceof SortListTl)
    { 
      endVisit((SortListTl)node);
      return;
    }
    if(node instanceof SortTuple)
    { 
      endVisit((SortTuple)node);
      return;
    }
    if(node instanceof Star)
    { 
      endVisit((Star)node);
      return;
    }
    if(node instanceof StarStar)
    { 
      endVisit((StarStar)node);
      return;
    }
    if(node instanceof SDefNoArgs)
    { 
      endVisit((SDefNoArgs)node);
      return;
    }
    if(node instanceof SDef)
    { 
      endVisit((SDef)node);
      return;
    }
    if(node instanceof DefaultVarDec)
    { 
      endVisit((DefaultVarDec)node);
      return;
    }
    if(node instanceof Call)
    { 
      endVisit((Call)node);
      return;
    }
    if(node instanceof ScopeDefault)
    { 
      endVisit((ScopeDefault)node);
      return;
    }
    if(node instanceof BA)
    { 
      endVisit((BA)node);
      return;
    }
    if(node instanceof StrategyAngle)
    { 
      endVisit((StrategyAngle)node);
      return;
    }
    if(node instanceof LChoice)
    { 
      endVisit((LChoice)node);
      return;
    }
    if(node instanceof Rec)
    { 
      endVisit((Rec)node);
      return;
    }
    if(node instanceof Not0)
    { 
      endVisit((Not0)node);
      return;
    }
    if(node instanceof Where)
    { 
      endVisit((Where)node);
      return;
    }
    if(node instanceof Test)
    { 
      endVisit((Test)node);
      return;
    }
    if(node instanceof PrimNoArgs)
    { 
      endVisit((PrimNoArgs)node);
      return;
    }
    if(node instanceof Prim)
    { 
      endVisit((Prim)node);
      return;
    }
    if(node instanceof StrCong)
    { 
      endVisit((StrCong)node);
      return;
    }
    if(node instanceof IntCong)
    { 
      endVisit((IntCong)node);
      return;
    }
    if(node instanceof RealCong)
    { 
      endVisit((RealCong)node);
      return;
    }
    if(node instanceof CharCong)
    { 
      endVisit((CharCong)node);
      return;
    }
    if(node instanceof CongQ)
    { 
      endVisit((CongQ)node);
      return;
    }
    if(node instanceof AnnoCong)
    { 
      endVisit((AnnoCong)node);
      return;
    }
    if(node instanceof StrategyCurly)
    { 
      endVisit((StrategyCurly)node);
      return;
    }
    if(node instanceof EmptyTupleCong)
    { 
      endVisit((EmptyTupleCong)node);
      return;
    }
    if(node instanceof Strategy)
    { 
      endVisit((Strategy)node);
      return;
    }
    if(node instanceof TupleCong)
    { 
      endVisit((TupleCong)node);
      return;
    }
    if(node instanceof ListCongNoTail)
    { 
      endVisit((ListCongNoTail)node);
      return;
    }
    if(node instanceof ListCong)
    { 
      endVisit((ListCong)node);
      return;
    }
    if(node instanceof ExplodeCong)
    { 
      endVisit((ExplodeCong)node);
      return;
    }
    if(node instanceof CallNoArgs)
    { 
      endVisit((CallNoArgs)node);
      return;
    }
    if(node instanceof LRule)
    { 
      endVisit((LRule)node);
      return;
    }
    if(node instanceof SRule)
    { 
      endVisit((SRule)node);
      return;
    }
    if(node instanceof Choice)
    { 
      endVisit((Choice)node);
      return;
    }
    if(node instanceof RChoice)
    { 
      endVisit((RChoice)node);
      return;
    }
    if(node instanceof CondChoice)
    { 
      endVisit((CondChoice)node);
      return;
    }
    if(node instanceof IfThen)
    { 
      endVisit((IfThen)node);
      return;
    }
    if(node instanceof SwitchChoiceNoOtherwise)
    { 
      endVisit((SwitchChoiceNoOtherwise)node);
      return;
    }
    if(node instanceof SwitchChoice)
    { 
      endVisit((SwitchChoice)node);
      return;
    }
    if(node instanceof SwitchCase)
    { 
      endVisit((SwitchCase)node);
      return;
    }
    if(node instanceof AM)
    { 
      endVisit((AM)node);
      return;
    }
    if(node instanceof Assign0)
    { 
      endVisit((Assign0)node);
      return;
    }
    if(node instanceof OverlayNoArgs)
    { 
      endVisit((OverlayNoArgs)node);
      return;
    }
    if(node instanceof Overlay)
    { 
      endVisit((Overlay)node);
      return;
    }
    if(node instanceof RDefNoArgs)
    { 
      endVisit((RDefNoArgs)node);
      return;
    }
    if(node instanceof RDef)
    { 
      endVisit((RDef)node);
      return;
    }
    if(node instanceof RDefT)
    { 
      endVisit((RDefT)node);
      return;
    }
    if(node instanceof RuleNoCond)
    { 
      endVisit((RuleNoCond)node);
      return;
    }
    if(node instanceof Rule)
    { 
      endVisit((Rule)node);
      return;
    }
    if(node instanceof Rules)
    { 
      endVisit((Rules)node);
      return;
    }
    if(node instanceof Overlays)
    { 
      endVisit((Overlays)node);
      return;
    }
    if(node instanceof Def_StrategoHost)
    { 
      endVisit((Def_StrategoHost)node);
      return;
    }
    if(node instanceof DynRuleScope)
    { 
      endVisit((DynRuleScope)node);
      return;
    }
    if(node instanceof ScopeLabels_StrategoHost)
    { 
      endVisit((ScopeLabels_StrategoHost)node);
      return;
    }
    if(node instanceof ScopeLabels)
    { 
      endVisit((ScopeLabels)node);
      return;
    }
    if(node instanceof GenDynRules)
    { 
      endVisit((GenDynRules)node);
      return;
    }
    if(node instanceof AddScopeLabel)
    { 
      endVisit((AddScopeLabel)node);
      return;
    }
    if(node instanceof UndefineDynRule)
    { 
      endVisit((UndefineDynRule)node);
      return;
    }
    if(node instanceof SetDynRule)
    { 
      endVisit((SetDynRule)node);
      return;
    }
    if(node instanceof AddDynRule)
    { 
      endVisit((AddDynRule)node);
      return;
    }
    if(node instanceof SetDynRuleMatch)
    { 
      endVisit((SetDynRuleMatch)node);
      return;
    }
    if(node instanceof DynRuleAssign)
    { 
      endVisit((DynRuleAssign)node);
      return;
    }
    if(node instanceof DynRuleAssignAdd)
    { 
      endVisit((DynRuleAssignAdd)node);
      return;
    }
    if(node instanceof SetDynRuleDepends)
    { 
      endVisit((SetDynRuleDepends)node);
      return;
    }
    if(node instanceof LabeledDynRuleId)
    { 
      endVisit((LabeledDynRuleId)node);
      return;
    }
    if(node instanceof AddLabelDynRuleId)
    { 
      endVisit((AddLabelDynRuleId)node);
      return;
    }
    if(node instanceof DynRuleId)
    { 
      endVisit((DynRuleId)node);
      return;
    }
    if(node instanceof LabeledDynRuleScopeId)
    { 
      endVisit((LabeledDynRuleScopeId)node);
      return;
    }
    if(node instanceof DynRuleScopeId)
    { 
      endVisit((DynRuleScopeId)node);
      return;
    }
    if(node instanceof RDecNoArgs)
    { 
      endVisit((RDecNoArgs)node);
      return;
    }
    if(node instanceof RDec)
    { 
      endVisit((RDec)node);
      return;
    }
    if(node instanceof RDecT)
    { 
      endVisit((RDecT)node);
      return;
    }
    if(node instanceof RuleNames_StrategoHost)
    { 
      endVisit((RuleNames_StrategoHost)node);
      return;
    }
    if(node instanceof RuleNames)
    { 
      endVisit((RuleNames)node);
      return;
    }
    if(node instanceof DynRuleIntersectFix)
    { 
      endVisit((DynRuleIntersectFix)node);
      return;
    }
    if(node instanceof DynRuleUnionFix0)
    { 
      endVisit((DynRuleUnionFix0)node);
      return;
    }
    if(node instanceof DynRuleUnionFix)
    { 
      endVisit((DynRuleUnionFix)node);
      return;
    }
    if(node instanceof DynRuleIntersectUnionFix0)
    { 
      endVisit((DynRuleIntersectUnionFix0)node);
      return;
    }
    if(node instanceof DynRuleIntersectUnionFix)
    { 
      endVisit((DynRuleIntersectUnionFix)node);
      return;
    }
    if(node instanceof DynRuleIntersect)
    { 
      endVisit((DynRuleIntersect)node);
      return;
    }
    if(node instanceof DynRuleUnion)
    { 
      endVisit((DynRuleUnion)node);
      return;
    }
    if(node instanceof DynRuleIntersectUnion)
    { 
      endVisit((DynRuleIntersectUnion)node);
      return;
    }
    if(node instanceof UnicodeEscape0)
    { 
      endVisit((UnicodeEscape0)node);
      return;
    }
    if(node instanceof LineTerminator)
    { 
      endVisit((LineTerminator)node);
      return;
    }
    if(node instanceof CarriageReturn)
    { 
      endVisit((CarriageReturn)node);
      return;
    }
    if(node instanceof EndOfFile)
    { 
      endVisit((EndOfFile)node);
      return;
    }
    if(node instanceof Comment)
    { 
      endVisit((Comment)node);
      return;
    }
    if(node instanceof EOLCommentChars)
    { 
      endVisit((EOLCommentChars)node);
      return;
    }
    if(node instanceof CommentPart)
    { 
      endVisit((CommentPart)node);
      return;
    }
    if(node instanceof BlockCommentChars)
    { 
      endVisit((BlockCommentChars)node);
      return;
    }
    if(node instanceof EscEscChar)
    { 
      endVisit((EscEscChar)node);
      return;
    }
    if(node instanceof EscChar)
    { 
      endVisit((EscChar)node);
      return;
    }
    if(node instanceof UnicodeEscape)
    { 
      endVisit((UnicodeEscape)node);
      return;
    }
    if(node instanceof Keyword_JavaObject)
    { 
      endVisit((Keyword_JavaObject)node);
      return;
    }
    if(node instanceof ID_JavaObject)
    { 
      endVisit((ID_JavaObject)node);
      return;
    }
    if(node instanceof Id)
    { 
      endVisit((Id)node);
      return;
    }
    if(node instanceof Public)
    { 
      endVisit((Public)node);
      return;
    }
    if(node instanceof Private)
    { 
      endVisit((Private)node);
      return;
    }
    if(node instanceof Protected)
    { 
      endVisit((Protected)node);
      return;
    }
    if(node instanceof Abstract)
    { 
      endVisit((Abstract)node);
      return;
    }
    if(node instanceof Final)
    { 
      endVisit((Final)node);
      return;
    }
    if(node instanceof Static)
    { 
      endVisit((Static)node);
      return;
    }
    if(node instanceof Native)
    { 
      endVisit((Native)node);
      return;
    }
    if(node instanceof Transient)
    { 
      endVisit((Transient)node);
      return;
    }
    if(node instanceof Volatile)
    { 
      endVisit((Volatile)node);
      return;
    }
    if(node instanceof Synchronized0)
    { 
      endVisit((Synchronized0)node);
      return;
    }
    if(node instanceof StrictFP)
    { 
      endVisit((StrictFP)node);
      return;
    }
    if(node instanceof Modifier_JavaObject9)
    { 
      endVisit((Modifier_JavaObject9)node);
      return;
    }
    if(node instanceof Modifier_JavaObject8)
    { 
      endVisit((Modifier_JavaObject8)node);
      return;
    }
    if(node instanceof Modifier_JavaObject7)
    { 
      endVisit((Modifier_JavaObject7)node);
      return;
    }
    if(node instanceof Modifier_JavaObject6)
    { 
      endVisit((Modifier_JavaObject6)node);
      return;
    }
    if(node instanceof Modifier_JavaObject5)
    { 
      endVisit((Modifier_JavaObject5)node);
      return;
    }
    if(node instanceof Modifier_JavaObject4)
    { 
      endVisit((Modifier_JavaObject4)node);
      return;
    }
    if(node instanceof Modifier_JavaObject3)
    { 
      endVisit((Modifier_JavaObject3)node);
      return;
    }
    if(node instanceof Modifier_JavaObject2)
    { 
      endVisit((Modifier_JavaObject2)node);
      return;
    }
    if(node instanceof Modifier_JavaObject1)
    { 
      endVisit((Modifier_JavaObject1)node);
      return;
    }
    if(node instanceof Modifier_JavaObject0)
    { 
      endVisit((Modifier_JavaObject0)node);
      return;
    }
    if(node instanceof Modifier_JavaObject)
    { 
      endVisit((Modifier_JavaObject)node);
      return;
    }
    if(node instanceof DeciLiteral_JavaObject)
    { 
      endVisit((DeciLiteral_JavaObject)node);
      return;
    }
    if(node instanceof HexaLiteral_JavaObject)
    { 
      endVisit((HexaLiteral_JavaObject)node);
      return;
    }
    if(node instanceof OctaLiteral_JavaObject)
    { 
      endVisit((OctaLiteral_JavaObject)node);
      return;
    }
    if(node instanceof DeciNumeral_JavaObject)
    { 
      endVisit((DeciNumeral_JavaObject)node);
      return;
    }
    if(node instanceof HexaNumeral_JavaObject)
    { 
      endVisit((HexaNumeral_JavaObject)node);
      return;
    }
    if(node instanceof OctaNumeral_JavaObject)
    { 
      endVisit((OctaNumeral_JavaObject)node);
      return;
    }
    if(node instanceof Deci)
    { 
      endVisit((Deci)node);
      return;
    }
    if(node instanceof Hexa)
    { 
      endVisit((Hexa)node);
      return;
    }
    if(node instanceof Octa)
    { 
      endVisit((Octa)node);
      return;
    }
    if(node instanceof DeciFloatLiteral_JavaObject)
    { 
      endVisit((DeciFloatLiteral_JavaObject)node);
      return;
    }
    if(node instanceof HexaFloatLiteral_JavaObject)
    { 
      endVisit((HexaFloatLiteral_JavaObject)node);
      return;
    }
    if(node instanceof Float1)
    { 
      endVisit((Float1)node);
      return;
    }
    if(node instanceof Float0)
    { 
      endVisit((Float0)node);
      return;
    }
    if(node instanceof DeciFloatNumeral_JavaObject)
    { 
      endVisit((DeciFloatNumeral_JavaObject)node);
      return;
    }
    if(node instanceof DeciFloatDigits_JavaObject)
    { 
      endVisit((DeciFloatDigits_JavaObject)node);
      return;
    }
    if(node instanceof DeciFloatExponentPart_JavaObject)
    { 
      endVisit((DeciFloatExponentPart_JavaObject)node);
      return;
    }
    if(node instanceof SignedInteger_JavaObject)
    { 
      endVisit((SignedInteger_JavaObject)node);
      return;
    }
    if(node instanceof HexaFloatNumeral_JavaObject)
    { 
      endVisit((HexaFloatNumeral_JavaObject)node);
      return;
    }
    if(node instanceof HexaSignificand_JavaObject)
    { 
      endVisit((HexaSignificand_JavaObject)node);
      return;
    }
    if(node instanceof BinaryExponent_JavaObject)
    { 
      endVisit((BinaryExponent_JavaObject)node);
      return;
    }
    if(node instanceof Bool)
    { 
      endVisit((Bool)node);
      return;
    }
    if(node instanceof True)
    { 
      endVisit((True)node);
      return;
    }
    if(node instanceof False)
    { 
      endVisit((False)node);
      return;
    }
    if(node instanceof EscapeSeq_JavaObject0)
    { 
      endVisit((EscapeSeq_JavaObject0)node);
      return;
    }
    if(node instanceof EscapeSeq_JavaObject)
    { 
      endVisit((EscapeSeq_JavaObject)node);
      return;
    }
    if(node instanceof NamedEscape)
    { 
      endVisit((NamedEscape)node);
      return;
    }
    if(node instanceof OctaEscape1)
    { 
      endVisit((OctaEscape1)node);
      return;
    }
    if(node instanceof OctaEscape20)
    { 
      endVisit((OctaEscape20)node);
      return;
    }
    if(node instanceof OctaEscape2)
    { 
      endVisit((OctaEscape2)node);
      return;
    }
    if(node instanceof OctaEscape3)
    { 
      endVisit((OctaEscape3)node);
      return;
    }
    if(node instanceof LastOcta_JavaObject)
    { 
      endVisit((LastOcta_JavaObject)node);
      return;
    }
    if(node instanceof Char0)
    { 
      endVisit((Char0)node);
      return;
    }
    if(node instanceof Single)
    { 
      endVisit((Single)node);
      return;
    }
    if(node instanceof CharContent_JavaObject0)
    { 
      endVisit((CharContent_JavaObject0)node);
      return;
    }
    if(node instanceof CharContent_JavaObject)
    { 
      endVisit((CharContent_JavaObject)node);
      return;
    }
    if(node instanceof SingleChar_JavaObject)
    { 
      endVisit((SingleChar_JavaObject)node);
      return;
    }
    if(node instanceof String)
    { 
      endVisit((String)node);
      return;
    }
    if(node instanceof Chars)
    { 
      endVisit((Chars)node);
      return;
    }
    if(node instanceof StringPart_JavaObject0)
    { 
      endVisit((StringPart_JavaObject0)node);
      return;
    }
    if(node instanceof StringPart_JavaObject)
    { 
      endVisit((StringPart_JavaObject)node);
      return;
    }
    if(node instanceof StringChars_JavaObject)
    { 
      endVisit((StringChars_JavaObject)node);
      return;
    }
    if(node instanceof Null)
    { 
      endVisit((Null)node);
      return;
    }
    if(node instanceof PrimType_JavaObject)
    { 
      endVisit((PrimType_JavaObject)node);
      return;
    }
    if(node instanceof Boolean)
    { 
      endVisit((Boolean)node);
      return;
    }
    if(node instanceof NumType_JavaObject0)
    { 
      endVisit((NumType_JavaObject0)node);
      return;
    }
    if(node instanceof NumType_JavaObject)
    { 
      endVisit((NumType_JavaObject)node);
      return;
    }
    if(node instanceof Byte)
    { 
      endVisit((Byte)node);
      return;
    }
    if(node instanceof Short)
    { 
      endVisit((Short)node);
      return;
    }
    if(node instanceof Int)
    { 
      endVisit((Int)node);
      return;
    }
    if(node instanceof Long)
    { 
      endVisit((Long)node);
      return;
    }
    if(node instanceof Char)
    { 
      endVisit((Char)node);
      return;
    }
    if(node instanceof Float)
    { 
      endVisit((Float)node);
      return;
    }
    if(node instanceof Double)
    { 
      endVisit((Double)node);
      return;
    }
    if(node instanceof PackageName)
    { 
      endVisit((PackageName)node);
      return;
    }
    if(node instanceof AmbName0)
    { 
      endVisit((AmbName0)node);
      return;
    }
    if(node instanceof AmbName)
    { 
      endVisit((AmbName)node);
      return;
    }
    if(node instanceof TypeName0)
    { 
      endVisit((TypeName0)node);
      return;
    }
    if(node instanceof TypeName)
    { 
      endVisit((TypeName)node);
      return;
    }
    if(node instanceof ExprName0)
    { 
      endVisit((ExprName0)node);
      return;
    }
    if(node instanceof ExprName)
    { 
      endVisit((ExprName)node);
      return;
    }
    if(node instanceof MethodName0)
    { 
      endVisit((MethodName0)node);
      return;
    }
    if(node instanceof MethodName)
    { 
      endVisit((MethodName)node);
      return;
    }
    if(node instanceof PackageOrTypeName0)
    { 
      endVisit((PackageOrTypeName0)node);
      return;
    }
    if(node instanceof PackageOrTypeName)
    { 
      endVisit((PackageOrTypeName)node);
      return;
    }
    if(node instanceof TypeArgs)
    { 
      endVisit((TypeArgs)node);
      return;
    }
    if(node instanceof ActualTypeArg_JavaObject)
    { 
      endVisit((ActualTypeArg_JavaObject)node);
      return;
    }
    if(node instanceof Wildcard)
    { 
      endVisit((Wildcard)node);
      return;
    }
    if(node instanceof WildcardUpperBound)
    { 
      endVisit((WildcardUpperBound)node);
      return;
    }
    if(node instanceof WildcardLowerBound)
    { 
      endVisit((WildcardLowerBound)node);
      return;
    }
    if(node instanceof TypeParam)
    { 
      endVisit((TypeParam)node);
      return;
    }
    if(node instanceof TypeBound)
    { 
      endVisit((TypeBound)node);
      return;
    }
    if(node instanceof TypeParams)
    { 
      endVisit((TypeParams)node);
      return;
    }
    if(node instanceof TypeVarId_JavaObject)
    { 
      endVisit((TypeVarId_JavaObject)node);
      return;
    }
    if(node instanceof RefType_JavaObject0)
    { 
      endVisit((RefType_JavaObject0)node);
      return;
    }
    if(node instanceof RefType_JavaObject)
    { 
      endVisit((RefType_JavaObject)node);
      return;
    }
    if(node instanceof ClassOrInterfaceType)
    { 
      endVisit((ClassOrInterfaceType)node);
      return;
    }
    if(node instanceof ClassType)
    { 
      endVisit((ClassType)node);
      return;
    }
    if(node instanceof InterfaceType)
    { 
      endVisit((InterfaceType)node);
      return;
    }
    if(node instanceof TypeDecSpec_JavaObject)
    { 
      endVisit((TypeDecSpec_JavaObject)node);
      return;
    }
    if(node instanceof Member)
    { 
      endVisit((Member)node);
      return;
    }
    if(node instanceof TypeVar)
    { 
      endVisit((TypeVar)node);
      return;
    }
    if(node instanceof ArrayType)
    { 
      endVisit((ArrayType)node);
      return;
    }
    if(node instanceof Type_JavaObject0)
    { 
      endVisit((Type_JavaObject0)node);
      return;
    }
    if(node instanceof Type_JavaObject)
    { 
      endVisit((Type_JavaObject)node);
      return;
    }
    if(node instanceof Lit)
    { 
      endVisit((Lit)node);
      return;
    }
    if(node instanceof Literal_JavaObject5)
    { 
      endVisit((Literal_JavaObject5)node);
      return;
    }
    if(node instanceof Literal_JavaObject4)
    { 
      endVisit((Literal_JavaObject4)node);
      return;
    }
    if(node instanceof Literal_JavaObject3)
    { 
      endVisit((Literal_JavaObject3)node);
      return;
    }
    if(node instanceof Literal_JavaObject2)
    { 
      endVisit((Literal_JavaObject2)node);
      return;
    }
    if(node instanceof Literal_JavaObject1)
    { 
      endVisit((Literal_JavaObject1)node);
      return;
    }
    if(node instanceof Literal_JavaObject0)
    { 
      endVisit((Literal_JavaObject0)node);
      return;
    }
    if(node instanceof Literal_JavaObject)
    { 
      endVisit((Literal_JavaObject)node);
      return;
    }
    if(node instanceof Class)
    { 
      endVisit((Class)node);
      return;
    }
    if(node instanceof VoidClass)
    { 
      endVisit((VoidClass)node);
      return;
    }
    if(node instanceof This)
    { 
      endVisit((This)node);
      return;
    }
    if(node instanceof QThis)
    { 
      endVisit((QThis)node);
      return;
    }
    if(node instanceof Expr)
    { 
      endVisit((Expr)node);
      return;
    }
    if(node instanceof ArrayInit0)
    { 
      endVisit((ArrayInit0)node);
      return;
    }
    if(node instanceof ArrayInit)
    { 
      endVisit((ArrayInit)node);
      return;
    }
    if(node instanceof FieldDec)
    { 
      endVisit((FieldDec)node);
      return;
    }
    if(node instanceof VarDec0)
    { 
      endVisit((VarDec0)node);
      return;
    }
    if(node instanceof VarDec)
    { 
      endVisit((VarDec)node);
      return;
    }
    if(node instanceof VarDecId_JavaObject)
    { 
      endVisit((VarDecId_JavaObject)node);
      return;
    }
    if(node instanceof ArrayVarDecId)
    { 
      endVisit((ArrayVarDecId)node);
      return;
    }
    if(node instanceof Dim0)
    { 
      endVisit((Dim0)node);
      return;
    }
    if(node instanceof VarInit_JavaObject0)
    { 
      endVisit((VarInit_JavaObject0)node);
      return;
    }
    if(node instanceof VarInit_JavaObject)
    { 
      endVisit((VarInit_JavaObject)node);
      return;
    }
    if(node instanceof FieldMod_JavaObject5)
    { 
      endVisit((FieldMod_JavaObject5)node);
      return;
    }
    if(node instanceof FieldMod_JavaObject4)
    { 
      endVisit((FieldMod_JavaObject4)node);
      return;
    }
    if(node instanceof FieldMod_JavaObject3)
    { 
      endVisit((FieldMod_JavaObject3)node);
      return;
    }
    if(node instanceof FieldMod_JavaObject2)
    { 
      endVisit((FieldMod_JavaObject2)node);
      return;
    }
    if(node instanceof FieldMod_JavaObject1)
    { 
      endVisit((FieldMod_JavaObject1)node);
      return;
    }
    if(node instanceof FieldMod_JavaObject0)
    { 
      endVisit((FieldMod_JavaObject0)node);
      return;
    }
    if(node instanceof FieldMod_JavaObject)
    { 
      endVisit((FieldMod_JavaObject)node);
      return;
    }
    if(node instanceof LocalVarDecStm)
    { 
      endVisit((LocalVarDecStm)node);
      return;
    }
    if(node instanceof LocalVarDec)
    { 
      endVisit((LocalVarDec)node);
      return;
    }
    if(node instanceof Stm_JavaObject)
    { 
      endVisit((Stm_JavaObject)node);
      return;
    }
    if(node instanceof Empty)
    { 
      endVisit((Empty)node);
      return;
    }
    if(node instanceof Labeled)
    { 
      endVisit((Labeled)node);
      return;
    }
    if(node instanceof ExprStm)
    { 
      endVisit((ExprStm)node);
      return;
    }
    if(node instanceof If0)
    { 
      endVisit((If0)node);
      return;
    }
    if(node instanceof If)
    { 
      endVisit((If)node);
      return;
    }
    if(node instanceof AssertStm0)
    { 
      endVisit((AssertStm0)node);
      return;
    }
    if(node instanceof AssertStm)
    { 
      endVisit((AssertStm)node);
      return;
    }
    if(node instanceof Switch)
    { 
      endVisit((Switch)node);
      return;
    }
    if(node instanceof SwitchBlock)
    { 
      endVisit((SwitchBlock)node);
      return;
    }
    if(node instanceof SwitchGroup)
    { 
      endVisit((SwitchGroup)node);
      return;
    }
    if(node instanceof Case)
    { 
      endVisit((Case)node);
      return;
    }
    if(node instanceof Default)
    { 
      endVisit((Default)node);
      return;
    }
    if(node instanceof While)
    { 
      endVisit((While)node);
      return;
    }
    if(node instanceof DoWhile)
    { 
      endVisit((DoWhile)node);
      return;
    }
    if(node instanceof For0)
    { 
      endVisit((For0)node);
      return;
    }
    if(node instanceof For)
    { 
      endVisit((For)node);
      return;
    }
    if(node instanceof ForEach)
    { 
      endVisit((ForEach)node);
      return;
    }
    if(node instanceof Break)
    { 
      endVisit((Break)node);
      return;
    }
    if(node instanceof Continue)
    { 
      endVisit((Continue)node);
      return;
    }
    if(node instanceof Return)
    { 
      endVisit((Return)node);
      return;
    }
    if(node instanceof Throw)
    { 
      endVisit((Throw)node);
      return;
    }
    if(node instanceof Synchronized)
    { 
      endVisit((Synchronized)node);
      return;
    }
    if(node instanceof Try0)
    { 
      endVisit((Try0)node);
      return;
    }
    if(node instanceof Try)
    { 
      endVisit((Try)node);
      return;
    }
    if(node instanceof Catch)
    { 
      endVisit((Catch)node);
      return;
    }
    if(node instanceof Block)
    { 
      endVisit((Block)node);
      return;
    }
    if(node instanceof BlockStm_JavaObject0)
    { 
      endVisit((BlockStm_JavaObject0)node);
      return;
    }
    if(node instanceof ClassDecStm)
    { 
      endVisit((ClassDecStm)node);
      return;
    }
    if(node instanceof BlockStm_JavaObject)
    { 
      endVisit((BlockStm_JavaObject)node);
      return;
    }
    if(node instanceof MethodDec)
    { 
      endVisit((MethodDec)node);
      return;
    }
    if(node instanceof MethodDecHead)
    { 
      endVisit((MethodDecHead)node);
      return;
    }
    if(node instanceof DeprMethodDecHead)
    { 
      endVisit((DeprMethodDecHead)node);
      return;
    }
    if(node instanceof ResultType_JavaObject)
    { 
      endVisit((ResultType_JavaObject)node);
      return;
    }
    if(node instanceof Void)
    { 
      endVisit((Void)node);
      return;
    }
    if(node instanceof Param)
    { 
      endVisit((Param)node);
      return;
    }
    if(node instanceof VarArityParam)
    { 
      endVisit((VarArityParam)node);
      return;
    }
    if(node instanceof VarMod_JavaObject)
    { 
      endVisit((VarMod_JavaObject)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject7)
    { 
      endVisit((MethodMod_JavaObject7)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject6)
    { 
      endVisit((MethodMod_JavaObject6)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject5)
    { 
      endVisit((MethodMod_JavaObject5)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject4)
    { 
      endVisit((MethodMod_JavaObject4)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject3)
    { 
      endVisit((MethodMod_JavaObject3)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject2)
    { 
      endVisit((MethodMod_JavaObject2)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject1)
    { 
      endVisit((MethodMod_JavaObject1)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject0)
    { 
      endVisit((MethodMod_JavaObject0)node);
      return;
    }
    if(node instanceof MethodMod_JavaObject)
    { 
      endVisit((MethodMod_JavaObject)node);
      return;
    }
    if(node instanceof ThrowsDec)
    { 
      endVisit((ThrowsDec)node);
      return;
    }
    if(node instanceof ExceptionType_JavaObject)
    { 
      endVisit((ExceptionType_JavaObject)node);
      return;
    }
    if(node instanceof MethodBody_JavaObject)
    { 
      endVisit((MethodBody_JavaObject)node);
      return;
    }
    if(node instanceof NoMethodBody)
    { 
      endVisit((NoMethodBody)node);
      return;
    }
    if(node instanceof InstanceInit)
    { 
      endVisit((InstanceInit)node);
      return;
    }
    if(node instanceof StaticInit)
    { 
      endVisit((StaticInit)node);
      return;
    }
    if(node instanceof ConstrDec)
    { 
      endVisit((ConstrDec)node);
      return;
    }
    if(node instanceof ConstrDecHead)
    { 
      endVisit((ConstrDecHead)node);
      return;
    }
    if(node instanceof ConstrBody)
    { 
      endVisit((ConstrBody)node);
      return;
    }
    if(node instanceof AltConstrInv)
    { 
      endVisit((AltConstrInv)node);
      return;
    }
    if(node instanceof SuperConstrInv)
    { 
      endVisit((SuperConstrInv)node);
      return;
    }
    if(node instanceof QSuperConstrInv)
    { 
      endVisit((QSuperConstrInv)node);
      return;
    }
    if(node instanceof ConstrMod_JavaObject1)
    { 
      endVisit((ConstrMod_JavaObject1)node);
      return;
    }
    if(node instanceof ConstrMod_JavaObject0)
    { 
      endVisit((ConstrMod_JavaObject0)node);
      return;
    }
    if(node instanceof ConstrMod_JavaObject)
    { 
      endVisit((ConstrMod_JavaObject)node);
      return;
    }
    if(node instanceof EnumDec)
    { 
      endVisit((EnumDec)node);
      return;
    }
    if(node instanceof EnumDecHead)
    { 
      endVisit((EnumDecHead)node);
      return;
    }
    if(node instanceof EnumBody0)
    { 
      endVisit((EnumBody0)node);
      return;
    }
    if(node instanceof EnumBody)
    { 
      endVisit((EnumBody)node);
      return;
    }
    if(node instanceof EnumConst)
    { 
      endVisit((EnumConst)node);
      return;
    }
    if(node instanceof EnumConstArgs)
    { 
      endVisit((EnumConstArgs)node);
      return;
    }
    if(node instanceof EnumBodyDecs)
    { 
      endVisit((EnumBodyDecs)node);
      return;
    }
    if(node instanceof ConstantDec)
    { 
      endVisit((ConstantDec)node);
      return;
    }
    if(node instanceof ConstantMod_JavaObject1)
    { 
      endVisit((ConstantMod_JavaObject1)node);
      return;
    }
    if(node instanceof ConstantMod_JavaObject0)
    { 
      endVisit((ConstantMod_JavaObject0)node);
      return;
    }
    if(node instanceof ConstantMod_JavaObject)
    { 
      endVisit((ConstantMod_JavaObject)node);
      return;
    }
    if(node instanceof AbstractMethodDec)
    { 
      endVisit((AbstractMethodDec)node);
      return;
    }
    if(node instanceof DeprAbstractMethodDec)
    { 
      endVisit((DeprAbstractMethodDec)node);
      return;
    }
    if(node instanceof AbstractMethodMod_JavaObject0)
    { 
      endVisit((AbstractMethodMod_JavaObject0)node);
      return;
    }
    if(node instanceof AbstractMethodMod_JavaObject)
    { 
      endVisit((AbstractMethodMod_JavaObject)node);
      return;
    }
    if(node instanceof AnnoDec)
    { 
      endVisit((AnnoDec)node);
      return;
    }
    if(node instanceof AnnoDecHead)
    { 
      endVisit((AnnoDecHead)node);
      return;
    }
    if(node instanceof AnnoMethodDec)
    { 
      endVisit((AnnoMethodDec)node);
      return;
    }
    if(node instanceof AnnoElemDec_JavaObject3)
    { 
      endVisit((AnnoElemDec_JavaObject3)node);
      return;
    }
    if(node instanceof AnnoElemDec_JavaObject2)
    { 
      endVisit((AnnoElemDec_JavaObject2)node);
      return;
    }
    if(node instanceof AnnoElemDec_JavaObject1)
    { 
      endVisit((AnnoElemDec_JavaObject1)node);
      return;
    }
    if(node instanceof AnnoElemDec_JavaObject0)
    { 
      endVisit((AnnoElemDec_JavaObject0)node);
      return;
    }
    if(node instanceof AnnoElemDec_JavaObject)
    { 
      endVisit((AnnoElemDec_JavaObject)node);
      return;
    }
    if(node instanceof Semicolon2)
    { 
      endVisit((Semicolon2)node);
      return;
    }
    if(node instanceof DefaultVal)
    { 
      endVisit((DefaultVal)node);
      return;
    }
    if(node instanceof InterfaceDec_JavaObject)
    { 
      endVisit((InterfaceDec_JavaObject)node);
      return;
    }
    if(node instanceof InterfaceDec)
    { 
      endVisit((InterfaceDec)node);
      return;
    }
    if(node instanceof InterfaceDecHead)
    { 
      endVisit((InterfaceDecHead)node);
      return;
    }
    if(node instanceof ExtendsInterfaces)
    { 
      endVisit((ExtendsInterfaces)node);
      return;
    }
    if(node instanceof InterfaceMemberDec_JavaObject2)
    { 
      endVisit((InterfaceMemberDec_JavaObject2)node);
      return;
    }
    if(node instanceof InterfaceMemberDec_JavaObject1)
    { 
      endVisit((InterfaceMemberDec_JavaObject1)node);
      return;
    }
    if(node instanceof InterfaceMemberDec_JavaObject0)
    { 
      endVisit((InterfaceMemberDec_JavaObject0)node);
      return;
    }
    if(node instanceof InterfaceMemberDec_JavaObject)
    { 
      endVisit((InterfaceMemberDec_JavaObject)node);
      return;
    }
    if(node instanceof Semicolon1)
    { 
      endVisit((Semicolon1)node);
      return;
    }
    if(node instanceof InterfaceMod_JavaObject4)
    { 
      endVisit((InterfaceMod_JavaObject4)node);
      return;
    }
    if(node instanceof InterfaceMod_JavaObject3)
    { 
      endVisit((InterfaceMod_JavaObject3)node);
      return;
    }
    if(node instanceof InterfaceMod_JavaObject2)
    { 
      endVisit((InterfaceMod_JavaObject2)node);
      return;
    }
    if(node instanceof InterfaceMod_JavaObject1)
    { 
      endVisit((InterfaceMod_JavaObject1)node);
      return;
    }
    if(node instanceof InterfaceMod_JavaObject0)
    { 
      endVisit((InterfaceMod_JavaObject0)node);
      return;
    }
    if(node instanceof InterfaceMod_JavaObject)
    { 
      endVisit((InterfaceMod_JavaObject)node);
      return;
    }
    if(node instanceof ClassDec_JavaObject)
    { 
      endVisit((ClassDec_JavaObject)node);
      return;
    }
    if(node instanceof ClassDec)
    { 
      endVisit((ClassDec)node);
      return;
    }
    if(node instanceof ClassBody)
    { 
      endVisit((ClassBody)node);
      return;
    }
    if(node instanceof ClassDecHead)
    { 
      endVisit((ClassDecHead)node);
      return;
    }
    if(node instanceof ClassMod_JavaObject5)
    { 
      endVisit((ClassMod_JavaObject5)node);
      return;
    }
    if(node instanceof ClassMod_JavaObject4)
    { 
      endVisit((ClassMod_JavaObject4)node);
      return;
    }
    if(node instanceof ClassMod_JavaObject3)
    { 
      endVisit((ClassMod_JavaObject3)node);
      return;
    }
    if(node instanceof ClassMod_JavaObject2)
    { 
      endVisit((ClassMod_JavaObject2)node);
      return;
    }
    if(node instanceof ClassMod_JavaObject1)
    { 
      endVisit((ClassMod_JavaObject1)node);
      return;
    }
    if(node instanceof ClassMod_JavaObject0)
    { 
      endVisit((ClassMod_JavaObject0)node);
      return;
    }
    if(node instanceof ClassMod_JavaObject)
    { 
      endVisit((ClassMod_JavaObject)node);
      return;
    }
    if(node instanceof SuperDec)
    { 
      endVisit((SuperDec)node);
      return;
    }
    if(node instanceof ImplementsDec)
    { 
      endVisit((ImplementsDec)node);
      return;
    }
    if(node instanceof ClassBodyDec_JavaObject2)
    { 
      endVisit((ClassBodyDec_JavaObject2)node);
      return;
    }
    if(node instanceof ClassBodyDec_JavaObject1)
    { 
      endVisit((ClassBodyDec_JavaObject1)node);
      return;
    }
    if(node instanceof ClassBodyDec_JavaObject0)
    { 
      endVisit((ClassBodyDec_JavaObject0)node);
      return;
    }
    if(node instanceof ClassBodyDec_JavaObject)
    { 
      endVisit((ClassBodyDec_JavaObject)node);
      return;
    }
    if(node instanceof ClassMemberDec_JavaObject2)
    { 
      endVisit((ClassMemberDec_JavaObject2)node);
      return;
    }
    if(node instanceof ClassMemberDec_JavaObject1)
    { 
      endVisit((ClassMemberDec_JavaObject1)node);
      return;
    }
    if(node instanceof ClassMemberDec_JavaObject0)
    { 
      endVisit((ClassMemberDec_JavaObject0)node);
      return;
    }
    if(node instanceof ClassMemberDec_JavaObject)
    { 
      endVisit((ClassMemberDec_JavaObject)node);
      return;
    }
    if(node instanceof Semicolon0)
    { 
      endVisit((Semicolon0)node);
      return;
    }
    if(node instanceof NewInstance)
    { 
      endVisit((NewInstance)node);
      return;
    }
    if(node instanceof QNewInstance)
    { 
      endVisit((QNewInstance)node);
      return;
    }
    if(node instanceof Expr_JavaObject3)
    { 
      endVisit((Expr_JavaObject3)node);
      return;
    }
    if(node instanceof NewArray0)
    { 
      endVisit((NewArray0)node);
      return;
    }
    if(node instanceof NewArray)
    { 
      endVisit((NewArray)node);
      return;
    }
    if(node instanceof ArrayBaseType_JavaObject0)
    { 
      endVisit((ArrayBaseType_JavaObject0)node);
      return;
    }
    if(node instanceof ArrayBaseType_JavaObject)
    { 
      endVisit((ArrayBaseType_JavaObject)node);
      return;
    }
    if(node instanceof UnboundWld)
    { 
      endVisit((UnboundWld)node);
      return;
    }
    if(node instanceof Dim)
    { 
      endVisit((Dim)node);
      return;
    }
    if(node instanceof Expr_JavaObject2)
    { 
      endVisit((Expr_JavaObject2)node);
      return;
    }
    if(node instanceof Field)
    { 
      endVisit((Field)node);
      return;
    }
    if(node instanceof SuperField)
    { 
      endVisit((SuperField)node);
      return;
    }
    if(node instanceof QSuperField)
    { 
      endVisit((QSuperField)node);
      return;
    }
    if(node instanceof Expr_JavaObject1)
    { 
      endVisit((Expr_JavaObject1)node);
      return;
    }
    if(node instanceof ArrayAccess)
    { 
      endVisit((ArrayAccess)node);
      return;
    }
    if(node instanceof ArraySubscript)
    { 
      endVisit((ArraySubscript)node);
      return;
    }
    if(node instanceof Invoke)
    { 
      endVisit((Invoke)node);
      return;
    }
    if(node instanceof Method0)
    { 
      endVisit((Method0)node);
      return;
    }
    if(node instanceof Method)
    { 
      endVisit((Method)node);
      return;
    }
    if(node instanceof SuperMethod)
    { 
      endVisit((SuperMethod)node);
      return;
    }
    if(node instanceof QSuperMethod)
    { 
      endVisit((QSuperMethod)node);
      return;
    }
    if(node instanceof GenericMethod)
    { 
      endVisit((GenericMethod)node);
      return;
    }
    if(node instanceof Expr_JavaObject0)
    { 
      endVisit((Expr_JavaObject0)node);
      return;
    }
    if(node instanceof PostIncr)
    { 
      endVisit((PostIncr)node);
      return;
    }
    if(node instanceof PostDecr)
    { 
      endVisit((PostDecr)node);
      return;
    }
    if(node instanceof Plus0)
    { 
      endVisit((Plus0)node);
      return;
    }
    if(node instanceof Minus0)
    { 
      endVisit((Minus0)node);
      return;
    }
    if(node instanceof PreIncr)
    { 
      endVisit((PreIncr)node);
      return;
    }
    if(node instanceof PreDecr)
    { 
      endVisit((PreDecr)node);
      return;
    }
    if(node instanceof Complement)
    { 
      endVisit((Complement)node);
      return;
    }
    if(node instanceof Not)
    { 
      endVisit((Not)node);
      return;
    }
    if(node instanceof CastPrim)
    { 
      endVisit((CastPrim)node);
      return;
    }
    if(node instanceof CastRef)
    { 
      endVisit((CastRef)node);
      return;
    }
    if(node instanceof Expr_JavaObject)
    { 
      endVisit((Expr_JavaObject)node);
      return;
    }
    if(node instanceof InstanceOf)
    { 
      endVisit((InstanceOf)node);
      return;
    }
    if(node instanceof Mul)
    { 
      endVisit((Mul)node);
      return;
    }
    if(node instanceof Div)
    { 
      endVisit((Div)node);
      return;
    }
    if(node instanceof Remain)
    { 
      endVisit((Remain)node);
      return;
    }
    if(node instanceof Plus)
    { 
      endVisit((Plus)node);
      return;
    }
    if(node instanceof Minus)
    { 
      endVisit((Minus)node);
      return;
    }
    if(node instanceof LeftShift)
    { 
      endVisit((LeftShift)node);
      return;
    }
    if(node instanceof RightShift)
    { 
      endVisit((RightShift)node);
      return;
    }
    if(node instanceof URightShift)
    { 
      endVisit((URightShift)node);
      return;
    }
    if(node instanceof Lt)
    { 
      endVisit((Lt)node);
      return;
    }
    if(node instanceof Gt)
    { 
      endVisit((Gt)node);
      return;
    }
    if(node instanceof LtEq)
    { 
      endVisit((LtEq)node);
      return;
    }
    if(node instanceof GtEq)
    { 
      endVisit((GtEq)node);
      return;
    }
    if(node instanceof Eq)
    { 
      endVisit((Eq)node);
      return;
    }
    if(node instanceof NotEq)
    { 
      endVisit((NotEq)node);
      return;
    }
    if(node instanceof LazyAnd)
    { 
      endVisit((LazyAnd)node);
      return;
    }
    if(node instanceof LazyOr)
    { 
      endVisit((LazyOr)node);
      return;
    }
    if(node instanceof And)
    { 
      endVisit((And)node);
      return;
    }
    if(node instanceof ExcOr)
    { 
      endVisit((ExcOr)node);
      return;
    }
    if(node instanceof Or)
    { 
      endVisit((Or)node);
      return;
    }
    if(node instanceof Cond)
    { 
      endVisit((Cond)node);
      return;
    }
    if(node instanceof CondMid)
    { 
      endVisit((CondMid)node);
      return;
    }
    if(node instanceof Assign)
    { 
      endVisit((Assign)node);
      return;
    }
    if(node instanceof AssignMul)
    { 
      endVisit((AssignMul)node);
      return;
    }
    if(node instanceof AssignDiv)
    { 
      endVisit((AssignDiv)node);
      return;
    }
    if(node instanceof AssignRemain)
    { 
      endVisit((AssignRemain)node);
      return;
    }
    if(node instanceof AssignPlus)
    { 
      endVisit((AssignPlus)node);
      return;
    }
    if(node instanceof AssignMinus)
    { 
      endVisit((AssignMinus)node);
      return;
    }
    if(node instanceof AssignLeftShift)
    { 
      endVisit((AssignLeftShift)node);
      return;
    }
    if(node instanceof AssignRightShift)
    { 
      endVisit((AssignRightShift)node);
      return;
    }
    if(node instanceof AssignURightShift)
    { 
      endVisit((AssignURightShift)node);
      return;
    }
    if(node instanceof AssignAnd)
    { 
      endVisit((AssignAnd)node);
      return;
    }
    if(node instanceof AssignExcOr)
    { 
      endVisit((AssignExcOr)node);
      return;
    }
    if(node instanceof AssignOr)
    { 
      endVisit((AssignOr)node);
      return;
    }
    if(node instanceof LHS_JavaObject1)
    { 
      endVisit((LHS_JavaObject1)node);
      return;
    }
    if(node instanceof LHS_JavaObject0)
    { 
      endVisit((LHS_JavaObject0)node);
      return;
    }
    if(node instanceof LHS_JavaObject)
    { 
      endVisit((LHS_JavaObject)node);
      return;
    }
    if(node instanceof Anno)
    { 
      endVisit((Anno)node);
      return;
    }
    if(node instanceof SingleElemAnno)
    { 
      endVisit((SingleElemAnno)node);
      return;
    }
    if(node instanceof MarkerAnno)
    { 
      endVisit((MarkerAnno)node);
      return;
    }
    if(node instanceof ElemValPair)
    { 
      endVisit((ElemValPair)node);
      return;
    }
    if(node instanceof ElemVal_JavaObject0)
    { 
      endVisit((ElemVal_JavaObject0)node);
      return;
    }
    if(node instanceof ElemVal_JavaObject)
    { 
      endVisit((ElemVal_JavaObject)node);
      return;
    }
    if(node instanceof ElemValArrayInit0)
    { 
      endVisit((ElemValArrayInit0)node);
      return;
    }
    if(node instanceof ElemValArrayInit)
    { 
      endVisit((ElemValArrayInit)node);
      return;
    }
    if(node instanceof PackageDec)
    { 
      endVisit((PackageDec)node);
      return;
    }
    if(node instanceof TypeImportDec)
    { 
      endVisit((TypeImportDec)node);
      return;
    }
    if(node instanceof TypeImportOnDemandDec)
    { 
      endVisit((TypeImportOnDemandDec)node);
      return;
    }
    if(node instanceof StaticImportDec)
    { 
      endVisit((StaticImportDec)node);
      return;
    }
    if(node instanceof StaticImportOnDemandDec)
    { 
      endVisit((StaticImportOnDemandDec)node);
      return;
    }
    if(node instanceof TypeDec_JavaObject0)
    { 
      endVisit((TypeDec_JavaObject0)node);
      return;
    }
    if(node instanceof TypeDec_JavaObject)
    { 
      endVisit((TypeDec_JavaObject)node);
      return;
    }
    if(node instanceof Semicolon)
    { 
      endVisit((Semicolon)node);
      return;
    }
    if(node instanceof CompilationUnit)
    { 
      endVisit((CompilationUnit)node);
      return;
    }
    if(node instanceof Metavar41)
    { 
      endVisit((Metavar41)node);
      return;
    }
    if(node instanceof Metavar40)
    { 
      endVisit((Metavar40)node);
      return;
    }
    if(node instanceof Metavar39)
    { 
      endVisit((Metavar39)node);
      return;
    }
    if(node instanceof Metavar38)
    { 
      endVisit((Metavar38)node);
      return;
    }
    if(node instanceof Metavar37)
    { 
      endVisit((Metavar37)node);
      return;
    }
    if(node instanceof Metavar36)
    { 
      endVisit((Metavar36)node);
      return;
    }
    if(node instanceof ToMetaExpr80)
    { 
      endVisit((ToMetaExpr80)node);
      return;
    }
    if(node instanceof ToMetaExpr79)
    { 
      endVisit((ToMetaExpr79)node);
      return;
    }
    if(node instanceof ToMetaExpr78)
    { 
      endVisit((ToMetaExpr78)node);
      return;
    }
    if(node instanceof ToMetaExpr77)
    { 
      endVisit((ToMetaExpr77)node);
      return;
    }
    if(node instanceof ToMetaExpr76)
    { 
      endVisit((ToMetaExpr76)node);
      return;
    }
    if(node instanceof ToMetaExpr75)
    { 
      endVisit((ToMetaExpr75)node);
      return;
    }
    if(node instanceof ToMetaExpr74)
    { 
      endVisit((ToMetaExpr74)node);
      return;
    }
    if(node instanceof ToMetaExpr73)
    { 
      endVisit((ToMetaExpr73)node);
      return;
    }
    if(node instanceof ToMetaExpr72)
    { 
      endVisit((ToMetaExpr72)node);
      return;
    }
    if(node instanceof ToMetaExpr71)
    { 
      endVisit((ToMetaExpr71)node);
      return;
    }
    if(node instanceof ToMetaListExpr7)
    { 
      endVisit((ToMetaListExpr7)node);
      return;
    }
    if(node instanceof ToMetaListExpr6)
    { 
      endVisit((ToMetaListExpr6)node);
      return;
    }
    if(node instanceof ToMetaExpr70)
    { 
      endVisit((ToMetaExpr70)node);
      return;
    }
    if(node instanceof ToMetaExpr69)
    { 
      endVisit((ToMetaExpr69)node);
      return;
    }
    if(node instanceof ToMetaListExpr5)
    { 
      endVisit((ToMetaListExpr5)node);
      return;
    }
    if(node instanceof ToMetaListExpr4)
    { 
      endVisit((ToMetaListExpr4)node);
      return;
    }
    if(node instanceof ToMetaExpr68)
    { 
      endVisit((ToMetaExpr68)node);
      return;
    }
    if(node instanceof ToMetaExpr67)
    { 
      endVisit((ToMetaExpr67)node);
      return;
    }
    if(node instanceof ToMetaListExpr3)
    { 
      endVisit((ToMetaListExpr3)node);
      return;
    }
    if(node instanceof ToMetaListExpr2)
    { 
      endVisit((ToMetaListExpr2)node);
      return;
    }
    if(node instanceof FromMetaExpr33)
    { 
      endVisit((FromMetaExpr33)node);
      return;
    }
    if(node instanceof FromMetaExpr32)
    { 
      endVisit((FromMetaExpr32)node);
      return;
    }
    if(node instanceof FromMetaExpr31)
    { 
      endVisit((FromMetaExpr31)node);
      return;
    }
    if(node instanceof FromMetaExpr30)
    { 
      endVisit((FromMetaExpr30)node);
      return;
    }
    if(node instanceof FromMetaExpr29)
    { 
      endVisit((FromMetaExpr29)node);
      return;
    }
    if(node instanceof FromMetaExpr28)
    { 
      endVisit((FromMetaExpr28)node);
      return;
    }
    if(node instanceof Metavar35)
    { 
      endVisit((Metavar35)node);
      return;
    }
    if(node instanceof Metavar34)
    { 
      endVisit((Metavar34)node);
      return;
    }
    if(node instanceof Metavar33)
    { 
      endVisit((Metavar33)node);
      return;
    }
    if(node instanceof Metavar32)
    { 
      endVisit((Metavar32)node);
      return;
    }
    if(node instanceof Metavar31)
    { 
      endVisit((Metavar31)node);
      return;
    }
    if(node instanceof Metavar30)
    { 
      endVisit((Metavar30)node);
      return;
    }
    if(node instanceof Metavar29)
    { 
      endVisit((Metavar29)node);
      return;
    }
    if(node instanceof Metavar28)
    { 
      endVisit((Metavar28)node);
      return;
    }
    if(node instanceof ToMetaExpr66)
    { 
      endVisit((ToMetaExpr66)node);
      return;
    }
    if(node instanceof ToMetaExpr65)
    { 
      endVisit((ToMetaExpr65)node);
      return;
    }
    if(node instanceof ToMetaExpr64)
    { 
      endVisit((ToMetaExpr64)node);
      return;
    }
    if(node instanceof ToMetaExpr63)
    { 
      endVisit((ToMetaExpr63)node);
      return;
    }
    if(node instanceof Metavar27)
    { 
      endVisit((Metavar27)node);
      return;
    }
    if(node instanceof Metavar26)
    { 
      endVisit((Metavar26)node);
      return;
    }
    if(node instanceof Metavar25)
    { 
      endVisit((Metavar25)node);
      return;
    }
    if(node instanceof Metavar24)
    { 
      endVisit((Metavar24)node);
      return;
    }
    if(node instanceof Metavar23)
    { 
      endVisit((Metavar23)node);
      return;
    }
    if(node instanceof MetaTypeVar_JavaObject)
    { 
      endVisit((MetaTypeVar_JavaObject)node);
      return;
    }
    if(node instanceof MetaPrimTypeVar_JavaObject)
    { 
      endVisit((MetaPrimTypeVar_JavaObject)node);
      return;
    }
    if(node instanceof MetaRefTypeVar_JavaObject)
    { 
      endVisit((MetaRefTypeVar_JavaObject)node);
      return;
    }
    if(node instanceof ToMetaExpr62)
    { 
      endVisit((ToMetaExpr62)node);
      return;
    }
    if(node instanceof ToMetaExpr61)
    { 
      endVisit((ToMetaExpr61)node);
      return;
    }
    if(node instanceof ToMetaExpr60)
    { 
      endVisit((ToMetaExpr60)node);
      return;
    }
    if(node instanceof ToMetaExpr59)
    { 
      endVisit((ToMetaExpr59)node);
      return;
    }
    if(node instanceof ToMetaExpr58)
    { 
      endVisit((ToMetaExpr58)node);
      return;
    }
    if(node instanceof ToMetaExpr57)
    { 
      endVisit((ToMetaExpr57)node);
      return;
    }
    if(node instanceof FromMetaExpr27)
    { 
      endVisit((FromMetaExpr27)node);
      return;
    }
    if(node instanceof FromMetaExpr26)
    { 
      endVisit((FromMetaExpr26)node);
      return;
    }
    if(node instanceof ToMetaExpr56)
    { 
      endVisit((ToMetaExpr56)node);
      return;
    }
    if(node instanceof ToMetaExpr55)
    { 
      endVisit((ToMetaExpr55)node);
      return;
    }
    if(node instanceof ToMetaExpr54)
    { 
      endVisit((ToMetaExpr54)node);
      return;
    }
    if(node instanceof ToMetaExpr53)
    { 
      endVisit((ToMetaExpr53)node);
      return;
    }
    if(node instanceof ToMetaExpr52)
    { 
      endVisit((ToMetaExpr52)node);
      return;
    }
    if(node instanceof ToMetaExpr51)
    { 
      endVisit((ToMetaExpr51)node);
      return;
    }
    if(node instanceof ToMetaExpr50)
    { 
      endVisit((ToMetaExpr50)node);
      return;
    }
    if(node instanceof ToMetaExpr49)
    { 
      endVisit((ToMetaExpr49)node);
      return;
    }
    if(node instanceof ToMetaExpr48)
    { 
      endVisit((ToMetaExpr48)node);
      return;
    }
    if(node instanceof ToMetaExpr47)
    { 
      endVisit((ToMetaExpr47)node);
      return;
    }
    if(node instanceof ToMetaExpr46)
    { 
      endVisit((ToMetaExpr46)node);
      return;
    }
    if(node instanceof ToMetaExpr45)
    { 
      endVisit((ToMetaExpr45)node);
      return;
    }
    if(node instanceof ToMetaExpr44)
    { 
      endVisit((ToMetaExpr44)node);
      return;
    }
    if(node instanceof ToMetaExpr43)
    { 
      endVisit((ToMetaExpr43)node);
      return;
    }
    if(node instanceof ToMetaExpr42)
    { 
      endVisit((ToMetaExpr42)node);
      return;
    }
    if(node instanceof ToMetaExpr41)
    { 
      endVisit((ToMetaExpr41)node);
      return;
    }
    if(node instanceof ToMetaExpr40)
    { 
      endVisit((ToMetaExpr40)node);
      return;
    }
    if(node instanceof ToMetaExpr39)
    { 
      endVisit((ToMetaExpr39)node);
      return;
    }
    if(node instanceof ToMetaExpr38)
    { 
      endVisit((ToMetaExpr38)node);
      return;
    }
    if(node instanceof ToMetaExpr37)
    { 
      endVisit((ToMetaExpr37)node);
      return;
    }
    if(node instanceof FromMetaExpr25)
    { 
      endVisit((FromMetaExpr25)node);
      return;
    }
    if(node instanceof ToMetaExpr36)
    { 
      endVisit((ToMetaExpr36)node);
      return;
    }
    if(node instanceof ToMetaExpr35)
    { 
      endVisit((ToMetaExpr35)node);
      return;
    }
    if(node instanceof ToMetaExpr34)
    { 
      endVisit((ToMetaExpr34)node);
      return;
    }
    if(node instanceof ToMetaExpr33)
    { 
      endVisit((ToMetaExpr33)node);
      return;
    }
    if(node instanceof ToMetaExpr32)
    { 
      endVisit((ToMetaExpr32)node);
      return;
    }
    if(node instanceof FromMetaExpr24)
    { 
      endVisit((FromMetaExpr24)node);
      return;
    }
    if(node instanceof FromMetaExpr23)
    { 
      endVisit((FromMetaExpr23)node);
      return;
    }
    if(node instanceof ToMetaExpr31)
    { 
      endVisit((ToMetaExpr31)node);
      return;
    }
    if(node instanceof ToMetaExpr30)
    { 
      endVisit((ToMetaExpr30)node);
      return;
    }
    if(node instanceof ToMetaExpr29)
    { 
      endVisit((ToMetaExpr29)node);
      return;
    }
    if(node instanceof ToMetaListExpr1)
    { 
      endVisit((ToMetaListExpr1)node);
      return;
    }
    if(node instanceof ToMetaListExpr0)
    { 
      endVisit((ToMetaListExpr0)node);
      return;
    }
    if(node instanceof FromMetaExpr22)
    { 
      endVisit((FromMetaExpr22)node);
      return;
    }
    if(node instanceof ToMetaExpr28)
    { 
      endVisit((ToMetaExpr28)node);
      return;
    }
    if(node instanceof ToMetaExpr27)
    { 
      endVisit((ToMetaExpr27)node);
      return;
    }
    if(node instanceof ToMetaExpr26)
    { 
      endVisit((ToMetaExpr26)node);
      return;
    }
    if(node instanceof ToMetaExpr25)
    { 
      endVisit((ToMetaExpr25)node);
      return;
    }
    if(node instanceof ToMetaExpr24)
    { 
      endVisit((ToMetaExpr24)node);
      return;
    }
    if(node instanceof ToMetaExpr23)
    { 
      endVisit((ToMetaExpr23)node);
      return;
    }
    if(node instanceof ToMetaExpr22)
    { 
      endVisit((ToMetaExpr22)node);
      return;
    }
    if(node instanceof ToMetaExpr21)
    { 
      endVisit((ToMetaExpr21)node);
      return;
    }
    if(node instanceof ToMetaExpr20)
    { 
      endVisit((ToMetaExpr20)node);
      return;
    }
    if(node instanceof ToMetaExpr19)
    { 
      endVisit((ToMetaExpr19)node);
      return;
    }
    if(node instanceof ToMetaExpr18)
    { 
      endVisit((ToMetaExpr18)node);
      return;
    }
    if(node instanceof ToMetaExpr17)
    { 
      endVisit((ToMetaExpr17)node);
      return;
    }
    if(node instanceof ToMetaExpr16)
    { 
      endVisit((ToMetaExpr16)node);
      return;
    }
    if(node instanceof ToMetaExpr15)
    { 
      endVisit((ToMetaExpr15)node);
      return;
    }
    if(node instanceof ToMetaExpr14)
    { 
      endVisit((ToMetaExpr14)node);
      return;
    }
    if(node instanceof ToMetaExpr13)
    { 
      endVisit((ToMetaExpr13)node);
      return;
    }
    if(node instanceof ToMetaExpr12)
    { 
      endVisit((ToMetaExpr12)node);
      return;
    }
    if(node instanceof ToMetaExpr11)
    { 
      endVisit((ToMetaExpr11)node);
      return;
    }
    if(node instanceof ToMetaExpr10)
    { 
      endVisit((ToMetaExpr10)node);
      return;
    }
    if(node instanceof ToMetaExpr9)
    { 
      endVisit((ToMetaExpr9)node);
      return;
    }
    if(node instanceof ToMetaExpr8)
    { 
      endVisit((ToMetaExpr8)node);
      return;
    }
    if(node instanceof ToMetaExpr7)
    { 
      endVisit((ToMetaExpr7)node);
      return;
    }
    if(node instanceof ToMetaListExpr)
    { 
      endVisit((ToMetaListExpr)node);
      return;
    }
    if(node instanceof ToMetaExpr6)
    { 
      endVisit((ToMetaExpr6)node);
      return;
    }
    if(node instanceof ToMetaExpr5)
    { 
      endVisit((ToMetaExpr5)node);
      return;
    }
    if(node instanceof ToMetaExpr4)
    { 
      endVisit((ToMetaExpr4)node);
      return;
    }
    if(node instanceof ToMetaExpr3)
    { 
      endVisit((ToMetaExpr3)node);
      return;
    }
    if(node instanceof ToMetaExpr2)
    { 
      endVisit((ToMetaExpr2)node);
      return;
    }
    if(node instanceof ToMetaExpr1)
    { 
      endVisit((ToMetaExpr1)node);
      return;
    }
    if(node instanceof ToMetaExpr0)
    { 
      endVisit((ToMetaExpr0)node);
      return;
    }
    if(node instanceof ToMetaExpr)
    { 
      endVisit((ToMetaExpr)node);
      return;
    }
    if(node instanceof FromMetaExpr21)
    { 
      endVisit((FromMetaExpr21)node);
      return;
    }
    if(node instanceof FromMetaExpr20)
    { 
      endVisit((FromMetaExpr20)node);
      return;
    }
    if(node instanceof FromMetaExpr19)
    { 
      endVisit((FromMetaExpr19)node);
      return;
    }
    if(node instanceof FromMetaExpr18)
    { 
      endVisit((FromMetaExpr18)node);
      return;
    }
    if(node instanceof FromMetaExpr17)
    { 
      endVisit((FromMetaExpr17)node);
      return;
    }
    if(node instanceof FromMetaExpr16)
    { 
      endVisit((FromMetaExpr16)node);
      return;
    }
    if(node instanceof FromMetaExpr15)
    { 
      endVisit((FromMetaExpr15)node);
      return;
    }
    if(node instanceof FromMetaExpr14)
    { 
      endVisit((FromMetaExpr14)node);
      return;
    }
    if(node instanceof FromMetaExpr13)
    { 
      endVisit((FromMetaExpr13)node);
      return;
    }
    if(node instanceof FromMetaExpr12)
    { 
      endVisit((FromMetaExpr12)node);
      return;
    }
    if(node instanceof FromMetaExpr11)
    { 
      endVisit((FromMetaExpr11)node);
      return;
    }
    if(node instanceof FromMetaExpr10)
    { 
      endVisit((FromMetaExpr10)node);
      return;
    }
    if(node instanceof FromMetaExpr9)
    { 
      endVisit((FromMetaExpr9)node);
      return;
    }
    if(node instanceof FromMetaExpr8)
    { 
      endVisit((FromMetaExpr8)node);
      return;
    }
    if(node instanceof FromMetaExpr7)
    { 
      endVisit((FromMetaExpr7)node);
      return;
    }
    if(node instanceof FromMetaExpr6)
    { 
      endVisit((FromMetaExpr6)node);
      return;
    }
    if(node instanceof FromMetaExpr5)
    { 
      endVisit((FromMetaExpr5)node);
      return;
    }
    if(node instanceof FromMetaExpr4)
    { 
      endVisit((FromMetaExpr4)node);
      return;
    }
    if(node instanceof FromMetaExpr3)
    { 
      endVisit((FromMetaExpr3)node);
      return;
    }
    if(node instanceof FromMetaExpr2)
    { 
      endVisit((FromMetaExpr2)node);
      return;
    }
    if(node instanceof FromMetaExpr1)
    { 
      endVisit((FromMetaExpr1)node);
      return;
    }
    if(node instanceof Metavar22)
    { 
      endVisit((Metavar22)node);
      return;
    }
    if(node instanceof Metavar21)
    { 
      endVisit((Metavar21)node);
      return;
    }
    if(node instanceof Metavar20)
    { 
      endVisit((Metavar20)node);
      return;
    }
    if(node instanceof Metavar19)
    { 
      endVisit((Metavar19)node);
      return;
    }
    if(node instanceof Metavar18)
    { 
      endVisit((Metavar18)node);
      return;
    }
    if(node instanceof Metavar17)
    { 
      endVisit((Metavar17)node);
      return;
    }
    if(node instanceof Metavar16)
    { 
      endVisit((Metavar16)node);
      return;
    }
    if(node instanceof Metavar15)
    { 
      endVisit((Metavar15)node);
      return;
    }
    if(node instanceof Metavar14)
    { 
      endVisit((Metavar14)node);
      return;
    }
    if(node instanceof Metavar13)
    { 
      endVisit((Metavar13)node);
      return;
    }
    if(node instanceof Metavar12)
    { 
      endVisit((Metavar12)node);
      return;
    }
    if(node instanceof Metavar11)
    { 
      endVisit((Metavar11)node);
      return;
    }
    if(node instanceof Metavar10)
    { 
      endVisit((Metavar10)node);
      return;
    }
    if(node instanceof Metavar9)
    { 
      endVisit((Metavar9)node);
      return;
    }
    if(node instanceof Metavar8)
    { 
      endVisit((Metavar8)node);
      return;
    }
    if(node instanceof Metavar7)
    { 
      endVisit((Metavar7)node);
      return;
    }
    if(node instanceof Metavar6)
    { 
      endVisit((Metavar6)node);
      return;
    }
    if(node instanceof Metavar5)
    { 
      endVisit((Metavar5)node);
      return;
    }
    if(node instanceof Metavar4)
    { 
      endVisit((Metavar4)node);
      return;
    }
    if(node instanceof Metavar3)
    { 
      endVisit((Metavar3)node);
      return;
    }
    if(node instanceof Metavar2)
    { 
      endVisit((Metavar2)node);
      return;
    }
    if(node instanceof Metavar1)
    { 
      endVisit((Metavar1)node);
      return;
    }
    if(node instanceof Metavar0)
    { 
      endVisit((Metavar0)node);
      return;
    }
    if(node instanceof Metavar)
    { 
      endVisit((Metavar)node);
      return;
    }
    if(node instanceof FromMetaExpr0)
    { 
      endVisit((FromMetaExpr0)node);
      return;
    }
    if(node instanceof FromMetaExpr)
    { 
      endVisit((FromMetaExpr)node);
      return;
    }
    if(node instanceof ListPlusOfCommChar0)
    { 
      endVisit((ListPlusOfCommChar0)node);
      return;
    }
    if(node instanceof ListStarOfCommChar0)
    { 
      endVisit((ListStarOfCommChar0)node);
      return;
    }
    if(node instanceof ListPlusOfModNamePart_StrategoHost0)
    { 
      endVisit((ListPlusOfModNamePart_StrategoHost0)node);
      return;
    }
    if(node instanceof ListStarOfModNamePart_StrategoHost0)
    { 
      endVisit((ListStarOfModNamePart_StrategoHost0)node);
      return;
    }
    if(node instanceof ListPlusOfStrChar_StrategoHost0)
    { 
      endVisit((ListPlusOfStrChar_StrategoHost0)node);
      return;
    }
    if(node instanceof ListStarOfStrChar_StrategoHost0)
    { 
      endVisit((ListStarOfStrChar_StrategoHost0)node);
      return;
    }
    if(node instanceof ListPlusOfCommentPart0)
    { 
      endVisit((ListPlusOfCommentPart0)node);
      return;
    }
    if(node instanceof ListStarOfCommentPart0)
    { 
      endVisit((ListStarOfCommentPart0)node);
      return;
    }
    if(node instanceof OptDeciFloatExponentPart_JavaObject0)
    { 
      endVisit((OptDeciFloatExponentPart_JavaObject0)node);
      return;
    }
    if(node instanceof OptWildcardBound_JavaObject0)
    { 
      endVisit((OptWildcardBound_JavaObject0)node);
      return;
    }
    if(node instanceof OptTypeBound_JavaObject0)
    { 
      endVisit((OptTypeBound_JavaObject0)node);
      return;
    }
    if(node instanceof OptId_JavaObject0)
    { 
      endVisit((OptId_JavaObject0)node);
      return;
    }
    if(node instanceof OptExpr_JavaObject0)
    { 
      endVisit((OptExpr_JavaObject0)node);
      return;
    }
    if(node instanceof OptConstrInv_JavaObject0)
    { 
      endVisit((OptConstrInv_JavaObject0)node);
      return;
    }
    if(node instanceof OptEnumBodyDecs_JavaObject0)
    { 
      endVisit((OptEnumBodyDecs_JavaObject0)node);
      return;
    }
    if(node instanceof OptEnumConstArgs_JavaObject0)
    { 
      endVisit((OptEnumConstArgs_JavaObject0)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ConstantMod_JavaObject00)
    { 
      endVisit((Anno_JavaObject_ConstantMod_JavaObject00)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ConstantMod_JavaObject0)
    { 
      endVisit((Anno_JavaObject_ConstantMod_JavaObject0)node);
      return;
    }
    if(node instanceof Anno_JavaObject_AbstractMethodMod_JavaObject00)
    { 
      endVisit((Anno_JavaObject_AbstractMethodMod_JavaObject00)node);
      return;
    }
    if(node instanceof Anno_JavaObject_AbstractMethodMod_JavaObject0)
    { 
      endVisit((Anno_JavaObject_AbstractMethodMod_JavaObject0)node);
      return;
    }
    if(node instanceof OptThrows_JavaObject0)
    { 
      endVisit((OptThrows_JavaObject0)node);
      return;
    }
    if(node instanceof OptDefaultVal_JavaObject0)
    { 
      endVisit((OptDefaultVal_JavaObject0)node);
      return;
    }
    if(node instanceof Anno_JavaObject_InterfaceMod_JavaObject00)
    { 
      endVisit((Anno_JavaObject_InterfaceMod_JavaObject00)node);
      return;
    }
    if(node instanceof Anno_JavaObject_InterfaceMod_JavaObject0)
    { 
      endVisit((Anno_JavaObject_InterfaceMod_JavaObject0)node);
      return;
    }
    if(node instanceof OptExtendsInterfaces_JavaObject0)
    { 
      endVisit((OptExtendsInterfaces_JavaObject0)node);
      return;
    }
    if(node instanceof OptTypeParams_JavaObject0)
    { 
      endVisit((OptTypeParams_JavaObject0)node);
      return;
    }
    if(node instanceof OptSuper_JavaObject0)
    { 
      endVisit((OptSuper_JavaObject0)node);
      return;
    }
    if(node instanceof OptInterfaces_JavaObject0)
    { 
      endVisit((OptInterfaces_JavaObject0)node);
      return;
    }
    if(node instanceof OptTypeArgs_JavaObject00)
    { 
      endVisit((OptTypeArgs_JavaObject00)node);
      return;
    }
    if(node instanceof OptPackageDec_JavaObject0)
    { 
      endVisit((OptPackageDec_JavaObject0)node);
      return;
    }
    if(node instanceof Anno_JavaObject_MethodMod_JavaObject00)
    { 
      endVisit((Anno_JavaObject_MethodMod_JavaObject00)node);
      return;
    }
    if(node instanceof Anno_JavaObject_MethodMod_JavaObject0)
    { 
      endVisit((Anno_JavaObject_MethodMod_JavaObject0)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ClassMod_JavaObject00)
    { 
      endVisit((Anno_JavaObject_ClassMod_JavaObject00)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ClassMod_JavaObject0)
    { 
      endVisit((Anno_JavaObject_ClassMod_JavaObject0)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ConstrMod_JavaObject00)
    { 
      endVisit((Anno_JavaObject_ConstrMod_JavaObject00)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ConstrMod_JavaObject0)
    { 
      endVisit((Anno_JavaObject_ConstrMod_JavaObject0)node);
      return;
    }
    if(node instanceof Anno_JavaObject_VarMod_JavaObject00)
    { 
      endVisit((Anno_JavaObject_VarMod_JavaObject00)node);
      return;
    }
    if(node instanceof Anno_JavaObject_VarMod_JavaObject0)
    { 
      endVisit((Anno_JavaObject_VarMod_JavaObject0)node);
      return;
    }
    if(node instanceof Anno_JavaObject_FieldMod_JavaObject00)
    { 
      endVisit((Anno_JavaObject_FieldMod_JavaObject00)node);
      return;
    }
    if(node instanceof Anno_JavaObject_FieldMod_JavaObject0)
    { 
      endVisit((Anno_JavaObject_FieldMod_JavaObject0)node);
      return;
    }
    if(node instanceof OptClassBody_JavaObject0)
    { 
      endVisit((OptClassBody_JavaObject0)node);
      return;
    }
    if(node instanceof OptTypeArgs_JavaObject0)
    { 
      endVisit((OptTypeArgs_JavaObject0)node);
      return;
    }
    if(node instanceof WsSort)
    { 
      endVisit((WsSort)node);
      return;
    }
    if(node instanceof ShortComSort)
    { 
      endVisit((ShortComSort)node);
      return;
    }
    if(node instanceof LongComSort)
    { 
      endVisit((LongComSort)node);
      return;
    }
    if(node instanceof EofSort)
    { 
      endVisit((EofSort)node);
      return;
    }
    if(node instanceof CommCharSort)
    { 
      endVisit((CommCharSort)node);
      return;
    }
    if(node instanceof AsteriskSort)
    { 
      endVisit((AsteriskSort)node);
      return;
    }
    if(node instanceof ModName_StrategoHostSort)
    { 
      endVisit((ModName_StrategoHostSort)node);
      return;
    }
    if(node instanceof ModNamePart_StrategoHostSort)
    { 
      endVisit((ModNamePart_StrategoHostSort)node);
      return;
    }
    if(node instanceof Id_StrategoHostSort)
    { 
      endVisit((Id_StrategoHostSort)node);
      return;
    }
    if(node instanceof LId_StrategoHostSort)
    { 
      endVisit((LId_StrategoHostSort)node);
      return;
    }
    if(node instanceof LCID_StrategoHostSort)
    { 
      endVisit((LCID_StrategoHostSort)node);
      return;
    }
    if(node instanceof UCID_StrategoHostSort)
    { 
      endVisit((UCID_StrategoHostSort)node);
      return;
    }
    if(node instanceof Keyword_StrategoHostSort)
    { 
      endVisit((Keyword_StrategoHostSort)node);
      return;
    }
    if(node instanceof Int_StrategoHostSort)
    { 
      endVisit((Int_StrategoHostSort)node);
      return;
    }
    if(node instanceof Real_StrategoHostSort)
    { 
      endVisit((Real_StrategoHostSort)node);
      return;
    }
    if(node instanceof String_StrategoHostSort)
    { 
      endVisit((String_StrategoHostSort)node);
      return;
    }
    if(node instanceof StrChar_StrategoHostSort)
    { 
      endVisit((StrChar_StrategoHostSort)node);
      return;
    }
    if(node instanceof Char_StrategoHostSort)
    { 
      endVisit((Char_StrategoHostSort)node);
      return;
    }
    if(node instanceof CharChar_StrategoHostSort)
    { 
      endVisit((CharChar_StrategoHostSort)node);
      return;
    }
    if(node instanceof Sdecl_StrategoHostSort)
    { 
      endVisit((Sdecl_StrategoHostSort)node);
      return;
    }
    if(node instanceof Opdecl_StrategoHostSort)
    { 
      endVisit((Opdecl_StrategoHostSort)node);
      return;
    }
    if(node instanceof ConstType_StrategoHostSort)
    { 
      endVisit((ConstType_StrategoHostSort)node);
      return;
    }
    if(node instanceof FunType_StrategoHostSort)
    { 
      endVisit((FunType_StrategoHostSort)node);
      return;
    }
    if(node instanceof ArgTypeSort)
    { 
      endVisit((ArgTypeSort)node);
      return;
    }
    if(node instanceof ArgType_StrategoHostSort)
    { 
      endVisit((ArgType_StrategoHostSort)node);
      return;
    }
    if(node instanceof RetType_StrategoHostSort)
    { 
      endVisit((RetType_StrategoHostSort)node);
      return;
    }
    if(node instanceof Type_StrategoHostSort)
    { 
      endVisit((Type_StrategoHostSort)node);
      return;
    }
    if(node instanceof SVar_StrategoHostSort)
    { 
      endVisit((SVar_StrategoHostSort)node);
      return;
    }
    if(node instanceof StrategyParen_StrategoHostSort)
    { 
      endVisit((StrategyParen_StrategoHostSort)node);
      return;
    }
    if(node instanceof StrategyMid_StrategoHostSort)
    { 
      endVisit((StrategyMid_StrategoHostSort)node);
      return;
    }
    if(node instanceof Module_StrategoHostSort)
    { 
      endVisit((Module_StrategoHostSort)node);
      return;
    }
    if(node instanceof ImportModName_StrategoHostSort)
    { 
      endVisit((ImportModName_StrategoHostSort)node);
      return;
    }
    if(node instanceof LID_StrategoHostSort)
    { 
      endVisit((LID_StrategoHostSort)node);
      return;
    }
    if(node instanceof Var_StrategoHostSort)
    { 
      endVisit((Var_StrategoHostSort)node);
      return;
    }
    if(node instanceof ID_StrategoHostSort)
    { 
      endVisit((ID_StrategoHostSort)node);
      return;
    }
    if(node instanceof PreTerm_StrategoHostSort)
    { 
      endVisit((PreTerm_StrategoHostSort)node);
      return;
    }
    if(node instanceof Sort_StrategoHostSort)
    { 
      endVisit((Sort_StrategoHostSort)node);
      return;
    }
    if(node instanceof Kind_StrategoHostSort)
    { 
      endVisit((Kind_StrategoHostSort)node);
      return;
    }
    if(node instanceof StrategyDef_StrategoHostSort)
    { 
      endVisit((StrategyDef_StrategoHostSort)node);
      return;
    }
    if(node instanceof Typedid_StrategoHostSort)
    { 
      endVisit((Typedid_StrategoHostSort)node);
      return;
    }
    if(node instanceof StrategyAngleSort)
    { 
      endVisit((StrategyAngleSort)node);
      return;
    }
    if(node instanceof StrategyCurly_StrategoHostSort)
    { 
      endVisit((StrategyCurly_StrategoHostSort)node);
      return;
    }
    if(node instanceof StrategySort)
    { 
      endVisit((StrategySort)node);
      return;
    }
    if(node instanceof SwitchCase_StrategoHostSort)
    { 
      endVisit((SwitchCase_StrategoHostSort)node);
      return;
    }
    if(node instanceof Overlay_StrategoHostSort)
    { 
      endVisit((Overlay_StrategoHostSort)node);
      return;
    }
    if(node instanceof RuleDef_StrategoHostSort)
    { 
      endVisit((RuleDef_StrategoHostSort)node);
      return;
    }
    if(node instanceof Rule_StrategoHostSort)
    { 
      endVisit((Rule_StrategoHostSort)node);
      return;
    }
    if(node instanceof Decl_StrategoHostSort)
    { 
      endVisit((Decl_StrategoHostSort)node);
      return;
    }
    if(node instanceof Def_StrategoHostSort)
    { 
      endVisit((Def_StrategoHostSort)node);
      return;
    }
    if(node instanceof ScopeLabels_StrategoHostSort)
    { 
      endVisit((ScopeLabels_StrategoHostSort)node);
      return;
    }
    if(node instanceof DynRuleDef_StrategoHostSort)
    { 
      endVisit((DynRuleDef_StrategoHostSort)node);
      return;
    }
    if(node instanceof DynRuleId_StrategoHostSort)
    { 
      endVisit((DynRuleId_StrategoHostSort)node);
      return;
    }
    if(node instanceof DynRuleScopeId_StrategoHostSort)
    { 
      endVisit((DynRuleScopeId_StrategoHostSort)node);
      return;
    }
    if(node instanceof RuleDec_StrategoHostSort)
    { 
      endVisit((RuleDec_StrategoHostSort)node);
      return;
    }
    if(node instanceof RuleNames_StrategoHostSort)
    { 
      endVisit((RuleNames_StrategoHostSort)node);
      return;
    }
    if(node instanceof Strategy_StrategoHostSort)
    { 
      endVisit((Strategy_StrategoHostSort)node);
      return;
    }
    if(node instanceof LineTerminatorSort)
    { 
      endVisit((LineTerminatorSort)node);
      return;
    }
    if(node instanceof CarriageReturnSort)
    { 
      endVisit((CarriageReturnSort)node);
      return;
    }
    if(node instanceof EndOfFileSort)
    { 
      endVisit((EndOfFileSort)node);
      return;
    }
    if(node instanceof CommentSort)
    { 
      endVisit((CommentSort)node);
      return;
    }
    if(node instanceof EOLCommentCharsSort)
    { 
      endVisit((EOLCommentCharsSort)node);
      return;
    }
    if(node instanceof CommentPartSort)
    { 
      endVisit((CommentPartSort)node);
      return;
    }
    if(node instanceof BlockCommentCharsSort)
    { 
      endVisit((BlockCommentCharsSort)node);
      return;
    }
    if(node instanceof EscEscCharSort)
    { 
      endVisit((EscEscCharSort)node);
      return;
    }
    if(node instanceof EscCharSort)
    { 
      endVisit((EscCharSort)node);
      return;
    }
    if(node instanceof UnicodeEscapeSort)
    { 
      endVisit((UnicodeEscapeSort)node);
      return;
    }
    if(node instanceof Keyword_JavaObjectSort)
    { 
      endVisit((Keyword_JavaObjectSort)node);
      return;
    }
    if(node instanceof Public_JavaObjectSort)
    { 
      endVisit((Public_JavaObjectSort)node);
      return;
    }
    if(node instanceof Private_JavaObjectSort)
    { 
      endVisit((Private_JavaObjectSort)node);
      return;
    }
    if(node instanceof Protected_JavaObjectSort)
    { 
      endVisit((Protected_JavaObjectSort)node);
      return;
    }
    if(node instanceof Abstract_JavaObjectSort)
    { 
      endVisit((Abstract_JavaObjectSort)node);
      return;
    }
    if(node instanceof Final_JavaObjectSort)
    { 
      endVisit((Final_JavaObjectSort)node);
      return;
    }
    if(node instanceof Static_JavaObjectSort)
    { 
      endVisit((Static_JavaObjectSort)node);
      return;
    }
    if(node instanceof Native_JavaObjectSort)
    { 
      endVisit((Native_JavaObjectSort)node);
      return;
    }
    if(node instanceof Transient_JavaObjectSort)
    { 
      endVisit((Transient_JavaObjectSort)node);
      return;
    }
    if(node instanceof Volatile_JavaObjectSort)
    { 
      endVisit((Volatile_JavaObjectSort)node);
      return;
    }
    if(node instanceof Synchronized_JavaObjectSort)
    { 
      endVisit((Synchronized_JavaObjectSort)node);
      return;
    }
    if(node instanceof StrictFP_JavaObjectSort)
    { 
      endVisit((StrictFP_JavaObjectSort)node);
      return;
    }
    if(node instanceof Modifier_JavaObjectSort)
    { 
      endVisit((Modifier_JavaObjectSort)node);
      return;
    }
    if(node instanceof DeciNumeral_JavaObjectSort)
    { 
      endVisit((DeciNumeral_JavaObjectSort)node);
      return;
    }
    if(node instanceof HexaNumeral_JavaObjectSort)
    { 
      endVisit((HexaNumeral_JavaObjectSort)node);
      return;
    }
    if(node instanceof OctaNumeral_JavaObjectSort)
    { 
      endVisit((OctaNumeral_JavaObjectSort)node);
      return;
    }
    if(node instanceof IntLiteral_JavaObjectSort)
    { 
      endVisit((IntLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof DeciFloatLiteral_JavaObjectSort)
    { 
      endVisit((DeciFloatLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof HexaFloatLiteral_JavaObjectSort)
    { 
      endVisit((HexaFloatLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof DeciFloatNumeral_JavaObjectSort)
    { 
      endVisit((DeciFloatNumeral_JavaObjectSort)node);
      return;
    }
    if(node instanceof DeciFloatDigits_JavaObjectSort)
    { 
      endVisit((DeciFloatDigits_JavaObjectSort)node);
      return;
    }
    if(node instanceof DeciFloatExponentPart_JavaObjectSort)
    { 
      endVisit((DeciFloatExponentPart_JavaObjectSort)node);
      return;
    }
    if(node instanceof SignedInteger_JavaObjectSort)
    { 
      endVisit((SignedInteger_JavaObjectSort)node);
      return;
    }
    if(node instanceof HexaFloatNumeral_JavaObjectSort)
    { 
      endVisit((HexaFloatNumeral_JavaObjectSort)node);
      return;
    }
    if(node instanceof HexaSignificand_JavaObjectSort)
    { 
      endVisit((HexaSignificand_JavaObjectSort)node);
      return;
    }
    if(node instanceof BinaryExponent_JavaObjectSort)
    { 
      endVisit((BinaryExponent_JavaObjectSort)node);
      return;
    }
    if(node instanceof BoolLiteral_JavaObjectSort)
    { 
      endVisit((BoolLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof Bool_JavaObjectSort)
    { 
      endVisit((Bool_JavaObjectSort)node);
      return;
    }
    if(node instanceof EscapeSeq_JavaObjectSort)
    { 
      endVisit((EscapeSeq_JavaObjectSort)node);
      return;
    }
    if(node instanceof NamedEscape_JavaObjectSort)
    { 
      endVisit((NamedEscape_JavaObjectSort)node);
      return;
    }
    if(node instanceof OctaEscape_JavaObjectSort)
    { 
      endVisit((OctaEscape_JavaObjectSort)node);
      return;
    }
    if(node instanceof LastOcta_JavaObjectSort)
    { 
      endVisit((LastOcta_JavaObjectSort)node);
      return;
    }
    if(node instanceof CharContent_JavaObjectSort)
    { 
      endVisit((CharContent_JavaObjectSort)node);
      return;
    }
    if(node instanceof StringPart_JavaObjectSort)
    { 
      endVisit((StringPart_JavaObjectSort)node);
      return;
    }
    if(node instanceof NullLiteral_JavaObjectSort)
    { 
      endVisit((NullLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof NumType_JavaObjectSort)
    { 
      endVisit((NumType_JavaObjectSort)node);
      return;
    }
    if(node instanceof IntType_JavaObjectSort)
    { 
      endVisit((IntType_JavaObjectSort)node);
      return;
    }
    if(node instanceof FloatType_JavaObjectSort)
    { 
      endVisit((FloatType_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeArgs_JavaObjectSort)
    { 
      endVisit((TypeArgs_JavaObjectSort)node);
      return;
    }
    if(node instanceof ActualTypeArg_JavaObjectSort)
    { 
      endVisit((ActualTypeArg_JavaObjectSort)node);
      return;
    }
    if(node instanceof WildcardBound_JavaObjectSort)
    { 
      endVisit((WildcardBound_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeBound_JavaObjectSort)
    { 
      endVisit((TypeBound_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeParams_JavaObjectSort)
    { 
      endVisit((TypeParams_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeVarId_JavaObjectSort)
    { 
      endVisit((TypeVarId_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassOrInterfaceType_JavaObjectSort)
    { 
      endVisit((ClassOrInterfaceType_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassType_JavaObjectSort)
    { 
      endVisit((ClassType_JavaObjectSort)node);
      return;
    }
    if(node instanceof InterfaceType_JavaObjectSort)
    { 
      endVisit((InterfaceType_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeDecSpec_JavaObjectSort)
    { 
      endVisit((TypeDecSpec_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeVar_JavaObjectSort)
    { 
      endVisit((TypeVar_JavaObjectSort)node);
      return;
    }
    if(node instanceof ArrayType_JavaObjectSort)
    { 
      endVisit((ArrayType_JavaObjectSort)node);
      return;
    }
    if(node instanceof Literal_JavaObjectSort)
    { 
      endVisit((Literal_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassLiteral_JavaObjectSort)
    { 
      endVisit((ClassLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof ExprSort)
    { 
      endVisit((ExprSort)node);
      return;
    }
    if(node instanceof ArrayInit_JavaObjectSort)
    { 
      endVisit((ArrayInit_JavaObjectSort)node);
      return;
    }
    if(node instanceof FieldDec_JavaObjectSort)
    { 
      endVisit((FieldDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof VarDecId_JavaObjectSort)
    { 
      endVisit((VarDecId_JavaObjectSort)node);
      return;
    }
    if(node instanceof Dim_JavaObjectSort)
    { 
      endVisit((Dim_JavaObjectSort)node);
      return;
    }
    if(node instanceof VarInit_JavaObjectSort)
    { 
      endVisit((VarInit_JavaObjectSort)node);
      return;
    }
    if(node instanceof LocalVarDecStm_JavaObjectSort)
    { 
      endVisit((LocalVarDecStm_JavaObjectSort)node);
      return;
    }
    if(node instanceof SwitchBlock_JavaObjectSort)
    { 
      endVisit((SwitchBlock_JavaObjectSort)node);
      return;
    }
    if(node instanceof SwitchLabel_JavaObjectSort)
    { 
      endVisit((SwitchLabel_JavaObjectSort)node);
      return;
    }
    if(node instanceof CatchClause_JavaObjectSort)
    { 
      endVisit((CatchClause_JavaObjectSort)node);
      return;
    }
    if(node instanceof Block_JavaObjectSort)
    { 
      endVisit((Block_JavaObjectSort)node);
      return;
    }
    if(node instanceof MethodDec_JavaObjectSort)
    { 
      endVisit((MethodDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof MethodDecHead_JavaObjectSort)
    { 
      endVisit((MethodDecHead_JavaObjectSort)node);
      return;
    }
    if(node instanceof ResultType_JavaObjectSort)
    { 
      endVisit((ResultType_JavaObjectSort)node);
      return;
    }
    if(node instanceof Throws_JavaObjectSort)
    { 
      endVisit((Throws_JavaObjectSort)node);
      return;
    }
    if(node instanceof ExceptionType_JavaObjectSort)
    { 
      endVisit((ExceptionType_JavaObjectSort)node);
      return;
    }
    if(node instanceof MethodBody_JavaObjectSort)
    { 
      endVisit((MethodBody_JavaObjectSort)node);
      return;
    }
    if(node instanceof InstanceInit_JavaObjectSort)
    { 
      endVisit((InstanceInit_JavaObjectSort)node);
      return;
    }
    if(node instanceof StaticInit_JavaObjectSort)
    { 
      endVisit((StaticInit_JavaObjectSort)node);
      return;
    }
    if(node instanceof ConstrDec_JavaObjectSort)
    { 
      endVisit((ConstrDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof ConstrHead_JavaObjectSort)
    { 
      endVisit((ConstrHead_JavaObjectSort)node);
      return;
    }
    if(node instanceof ConstrBody_JavaObjectSort)
    { 
      endVisit((ConstrBody_JavaObjectSort)node);
      return;
    }
    if(node instanceof ConstrInv_JavaObjectSort)
    { 
      endVisit((ConstrInv_JavaObjectSort)node);
      return;
    }
    if(node instanceof EnumDec_JavaObjectSort)
    { 
      endVisit((EnumDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof EnumDecHead_JavaObjectSort)
    { 
      endVisit((EnumDecHead_JavaObjectSort)node);
      return;
    }
    if(node instanceof EnumBody_JavaObjectSort)
    { 
      endVisit((EnumBody_JavaObjectSort)node);
      return;
    }
    if(node instanceof EnumConst_JavaObjectSort)
    { 
      endVisit((EnumConst_JavaObjectSort)node);
      return;
    }
    if(node instanceof EnumConstArgsSort)
    { 
      endVisit((EnumConstArgsSort)node);
      return;
    }
    if(node instanceof EnumBodyDecs_JavaObjectSort)
    { 
      endVisit((EnumBodyDecs_JavaObjectSort)node);
      return;
    }
    if(node instanceof ConstantDec_JavaObjectSort)
    { 
      endVisit((ConstantDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof ConstantMod_JavaObjectSort)
    { 
      endVisit((ConstantMod_JavaObjectSort)node);
      return;
    }
    if(node instanceof AbstractMethodDec_JavaObjectSort)
    { 
      endVisit((AbstractMethodDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof AbstractMethodMod_JavaObjectSort)
    { 
      endVisit((AbstractMethodMod_JavaObjectSort)node);
      return;
    }
    if(node instanceof AnnoDec_JavaObjectSort)
    { 
      endVisit((AnnoDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof AnnoDecHead_JavaObjectSort)
    { 
      endVisit((AnnoDecHead_JavaObjectSort)node);
      return;
    }
    if(node instanceof AnnoElemDec_JavaObjectSort)
    { 
      endVisit((AnnoElemDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof DefaultVal_JavaObjectSort)
    { 
      endVisit((DefaultVal_JavaObjectSort)node);
      return;
    }
    if(node instanceof InterfaceDec_JavaObjectSort)
    { 
      endVisit((InterfaceDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof InterfaceDecHead_JavaObjectSort)
    { 
      endVisit((InterfaceDecHead_JavaObjectSort)node);
      return;
    }
    if(node instanceof ExtendsInterfaces_JavaObjectSort)
    { 
      endVisit((ExtendsInterfaces_JavaObjectSort)node);
      return;
    }
    if(node instanceof InterfaceMod_JavaObjectSort)
    { 
      endVisit((InterfaceMod_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassDec_JavaObjectSort)
    { 
      endVisit((ClassDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassBody_JavaObjectSort)
    { 
      endVisit((ClassBody_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassDecHead_JavaObjectSort)
    { 
      endVisit((ClassDecHead_JavaObjectSort)node);
      return;
    }
    if(node instanceof Super_JavaObjectSort)
    { 
      endVisit((Super_JavaObjectSort)node);
      return;
    }
    if(node instanceof Interfaces_JavaObjectSort)
    { 
      endVisit((Interfaces_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassMemberDec_JavaObjectSort)
    { 
      endVisit((ClassMemberDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof ArrayCreationExpr_JavaObjectSort)
    { 
      endVisit((ArrayCreationExpr_JavaObjectSort)node);
      return;
    }
    if(node instanceof ArrayBaseType_JavaObjectSort)
    { 
      endVisit((ArrayBaseType_JavaObjectSort)node);
      return;
    }
    if(node instanceof DimExpr_JavaObjectSort)
    { 
      endVisit((DimExpr_JavaObjectSort)node);
      return;
    }
    if(node instanceof FieldAccess_JavaObjectSort)
    { 
      endVisit((FieldAccess_JavaObjectSort)node);
      return;
    }
    if(node instanceof ArrayAccess_JavaObjectSort)
    { 
      endVisit((ArrayAccess_JavaObjectSort)node);
      return;
    }
    if(node instanceof ArraySubscriptSort)
    { 
      endVisit((ArraySubscriptSort)node);
      return;
    }
    if(node instanceof MethodSpec_JavaObjectSort)
    { 
      endVisit((MethodSpec_JavaObjectSort)node);
      return;
    }
    if(node instanceof CondMidSort)
    { 
      endVisit((CondMidSort)node);
      return;
    }
    if(node instanceof Anno_JavaObjectSort)
    { 
      endVisit((Anno_JavaObjectSort)node);
      return;
    }
    if(node instanceof ElemValPair_JavaObjectSort)
    { 
      endVisit((ElemValPair_JavaObjectSort)node);
      return;
    }
    if(node instanceof ElemVal_JavaObjectSort)
    { 
      endVisit((ElemVal_JavaObjectSort)node);
      return;
    }
    if(node instanceof CompilationUnit_JavaObjectSort)
    { 
      endVisit((CompilationUnit_JavaObjectSort)node);
      return;
    }
    if(node instanceof SwitchGroup_JavaObjectSort)
    { 
      endVisit((SwitchGroup_JavaObjectSort)node);
      return;
    }
    if(node instanceof Stm_JavaObjectSort)
    { 
      endVisit((Stm_JavaObjectSort)node);
      return;
    }
    if(node instanceof BlockStm_JavaObjectSort)
    { 
      endVisit((BlockStm_JavaObjectSort)node);
      return;
    }
    if(node instanceof LocalVarDec_JavaObjectSort)
    { 
      endVisit((LocalVarDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof VarDec_JavaObjectSort)
    { 
      endVisit((VarDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof LHS_JavaObjectSort)
    { 
      endVisit((LHS_JavaObjectSort)node);
      return;
    }
    if(node instanceof PrimType_JavaObjectSort)
    { 
      endVisit((PrimType_JavaObjectSort)node);
      return;
    }
    if(node instanceof RefType_JavaObjectSort)
    { 
      endVisit((RefType_JavaObjectSort)node);
      return;
    }
    if(node instanceof MetaTypeVar_JavaObjectSort)
    { 
      endVisit((MetaTypeVar_JavaObjectSort)node);
      return;
    }
    if(node instanceof MetaPrimTypeVar_JavaObjectSort)
    { 
      endVisit((MetaPrimTypeVar_JavaObjectSort)node);
      return;
    }
    if(node instanceof MetaRefTypeVar_JavaObjectSort)
    { 
      endVisit((MetaRefTypeVar_JavaObjectSort)node);
      return;
    }
    if(node instanceof Type_JavaObjectSort)
    { 
      endVisit((Type_JavaObjectSort)node);
      return;
    }
    if(node instanceof PackageDec_JavaObjectSort)
    { 
      endVisit((PackageDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof OptPackageDec_JavaObject0Sort)
    { 
      endVisit((OptPackageDec_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ImportDec_JavaObjectSort)
    { 
      endVisit((ImportDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof Term_StrategoHostSort)
    { 
      endVisit((Term_StrategoHostSort)node);
      return;
    }
    if(node instanceof Name_JavaObjectSort)
    { 
      endVisit((Name_JavaObjectSort)node);
      return;
    }
    if(node instanceof Id_JavaObjectSort)
    { 
      endVisit((Id_JavaObjectSort)node);
      return;
    }
    if(node instanceof ID_JavaObjectSort)
    { 
      endVisit((ID_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeParam_JavaObjectSort)
    { 
      endVisit((TypeParam_JavaObjectSort)node);
      return;
    }
    if(node instanceof Expr_JavaObjectSort)
    { 
      endVisit((Expr_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeDec_JavaObjectSort)
    { 
      endVisit((TypeDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassBodyDec_JavaObjectSort)
    { 
      endVisit((ClassBodyDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof InterfaceMemberDec_JavaObjectSort)
    { 
      endVisit((InterfaceMemberDec_JavaObjectSort)node);
      return;
    }
    if(node instanceof DeciLiteral_JavaObjectSort)
    { 
      endVisit((DeciLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof HexaLiteral_JavaObjectSort)
    { 
      endVisit((HexaLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof OctaLiteral_JavaObjectSort)
    { 
      endVisit((OctaLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof FloatLiteral_JavaObjectSort)
    { 
      endVisit((FloatLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof StringLiteral_JavaObjectSort)
    { 
      endVisit((StringLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof CharLiteral_JavaObjectSort)
    { 
      endVisit((CharLiteral_JavaObjectSort)node);
      return;
    }
    if(node instanceof AmbName_JavaObjectSort)
    { 
      endVisit((AmbName_JavaObjectSort)node);
      return;
    }
    if(node instanceof ExprName_JavaObjectSort)
    { 
      endVisit((ExprName_JavaObjectSort)node);
      return;
    }
    if(node instanceof MethodName_JavaObjectSort)
    { 
      endVisit((MethodName_JavaObjectSort)node);
      return;
    }
    if(node instanceof TypeName_JavaObjectSort)
    { 
      endVisit((TypeName_JavaObjectSort)node);
      return;
    }
    if(node instanceof PackageOrTypeName_JavaObjectSort)
    { 
      endVisit((PackageOrTypeName_JavaObjectSort)node);
      return;
    }
    if(node instanceof PackageName_JavaObjectSort)
    { 
      endVisit((PackageName_JavaObjectSort)node);
      return;
    }
    if(node instanceof MethodMod_JavaObjectSort)
    { 
      endVisit((MethodMod_JavaObjectSort)node);
      return;
    }
    if(node instanceof ClassMod_JavaObjectSort)
    { 
      endVisit((ClassMod_JavaObjectSort)node);
      return;
    }
    if(node instanceof ConstrMod_JavaObjectSort)
    { 
      endVisit((ConstrMod_JavaObjectSort)node);
      return;
    }
    if(node instanceof VarMod_JavaObjectSort)
    { 
      endVisit((VarMod_JavaObjectSort)node);
      return;
    }
    if(node instanceof FieldMod_JavaObjectSort)
    { 
      endVisit((FieldMod_JavaObjectSort)node);
      return;
    }
    if(node instanceof FormalParam_JavaObjectSort)
    { 
      endVisit((FormalParam_JavaObjectSort)node);
      return;
    }
    if(node instanceof StringChars_JavaObjectSort)
    { 
      endVisit((StringChars_JavaObjectSort)node);
      return;
    }
    if(node instanceof SingleChar_JavaObjectSort)
    { 
      endVisit((SingleChar_JavaObjectSort)node);
      return;
    }
    if(node instanceof ListPlusOfCommChar0Sort)
    { 
      endVisit((ListPlusOfCommChar0Sort)node);
      return;
    }
    if(node instanceof ListStarOfCommChar0Sort)
    { 
      endVisit((ListStarOfCommChar0Sort)node);
      return;
    }
    if(node instanceof ListPlusOfModNamePart_StrategoHost0Sort)
    { 
      endVisit((ListPlusOfModNamePart_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfModNamePart_StrategoHost0Sort)
    { 
      endVisit((ListStarOfModNamePart_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListPlusOfStrChar_StrategoHost0Sort)
    { 
      endVisit((ListPlusOfStrChar_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfStrChar_StrategoHost0Sort)
    { 
      endVisit((ListStarOfStrChar_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfSort_StrategoHost1Sort)
    { 
      endVisit((ListStarOfSort_StrategoHost1Sort)node);
      return;
    }
    if(node instanceof ListStarOfOpdecl_StrategoHost0Sort)
    { 
      endVisit((ListStarOfOpdecl_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfArgType_StrategoHost0Sort)
    { 
      endVisit((ListStarOfArgType_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfID_StrategoHost0Sort)
    { 
      endVisit((ListStarOfID_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfDecl_StrategoHost0Sort)
    { 
      endVisit((ListStarOfDecl_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfImportModName_StrategoHost0Sort)
    { 
      endVisit((ListStarOfImportModName_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfSdecl_StrategoHost0Sort)
    { 
      endVisit((ListStarOfSdecl_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfSort_StrategoHost0Sort)
    { 
      endVisit((ListStarOfSort_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfStrategy_StrategoHost0Sort)
    { 
      endVisit((ListStarOfStrategy_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfSwitchCase_StrategoHost0Sort)
    { 
      endVisit((ListStarOfSwitchCase_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfDef_StrategoHost0Sort)
    { 
      endVisit((ListStarOfDef_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfOverlay_StrategoHost0Sort)
    { 
      endVisit((ListStarOfOverlay_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfDynRuleScopeId_StrategoHost0Sort)
    { 
      endVisit((ListStarOfDynRuleScopeId_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfDynRuleDef_StrategoHost0Sort)
    { 
      endVisit((ListStarOfDynRuleDef_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfTypedid_StrategoHost0Sort)
    { 
      endVisit((ListStarOfTypedid_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfId_StrategoHost0Sort)
    { 
      endVisit((ListStarOfId_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ListStarOfCharClass1Sort)
    { 
      endVisit((ListStarOfCharClass1Sort)node);
      return;
    }
    if(node instanceof ListPlusOfCommentPart0Sort)
    { 
      endVisit((ListPlusOfCommentPart0Sort)node);
      return;
    }
    if(node instanceof ListStarOfCommentPart0Sort)
    { 
      endVisit((ListStarOfCommentPart0Sort)node);
      return;
    }
    if(node instanceof OptDeciFloatExponentPart_JavaObject0Sort)
    { 
      endVisit((OptDeciFloatExponentPart_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfStringPart_JavaObject0Sort)
    { 
      endVisit((ListStarOfStringPart_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfCharClass0Sort)
    { 
      endVisit((ListStarOfCharClass0Sort)node);
      return;
    }
    if(node instanceof ListStarOfId_JavaObject0Sort)
    { 
      endVisit((ListStarOfId_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfActualTypeArg_JavaObject0Sort)
    { 
      endVisit((ListStarOfActualTypeArg_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfClassOrInterfaceType_JavaObject0Sort)
    { 
      endVisit((ListStarOfClassOrInterfaceType_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfSwitchLabel_JavaObject0Sort)
    { 
      endVisit((ListStarOfSwitchLabel_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfBlockStm_JavaObject0Sort)
    { 
      endVisit((ListStarOfBlockStm_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfCatchClause_JavaObject0Sort)
    { 
      endVisit((ListStarOfCatchClause_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfExceptionType_JavaObject0Sort)
    { 
      endVisit((ListStarOfExceptionType_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfEnumConst_JavaObject0Sort)
    { 
      endVisit((ListStarOfEnumConst_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject_ConstantMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject_ConstantMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ConstantMod_JavaObject0Sort)
    { 
      endVisit((Anno_JavaObject_ConstantMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject_AbstractMethodMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject_AbstractMethodMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof Anno_JavaObject_AbstractMethodMod_JavaObject0Sort)
    { 
      endVisit((Anno_JavaObject_AbstractMethodMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnnoElemDec_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnnoElemDec_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAbstractMethodMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAbstractMethodMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject_InterfaceMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject_InterfaceMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof Anno_JavaObject_InterfaceMod_JavaObject0Sort)
    { 
      endVisit((Anno_JavaObject_InterfaceMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfInterfaceType_JavaObject0Sort)
    { 
      endVisit((ListStarOfInterfaceType_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfDimExpr_JavaObject0Sort)
    { 
      endVisit((ListStarOfDimExpr_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfDim_JavaObject0Sort)
    { 
      endVisit((ListStarOfDim_JavaObject0Sort)node);
      return;
    }
    if(node instanceof OptTypeArgs_JavaObject0Sort)
    { 
      endVisit((OptTypeArgs_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfElemValPair_JavaObject0Sort)
    { 
      endVisit((ListStarOfElemValPair_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfElemVal_JavaObject0Sort)
    { 
      endVisit((ListStarOfElemVal_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfTypeDec_JavaObject0Sort)
    { 
      endVisit((ListStarOfTypeDec_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfVarInit_JavaObject0Sort)
    { 
      endVisit((ListStarOfVarInit_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfExpr_JavaObject0Sort)
    { 
      endVisit((ListStarOfExpr_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfSwitchGroup_JavaObject0Sort)
    { 
      endVisit((ListStarOfSwitchGroup_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfVarDec_JavaObject0Sort)
    { 
      endVisit((ListStarOfVarDec_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfImportDec_JavaObject0Sort)
    { 
      endVisit((ListStarOfImportDec_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAbstractMethodDec_JavaObject0Sort)
    { 
      endVisit((ListStarOfAbstractMethodDec_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfTypeParam_JavaObject0Sort)
    { 
      endVisit((ListStarOfTypeParam_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfClassBodyDec_JavaObject0Sort)
    { 
      endVisit((ListStarOfClassBodyDec_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfInterfaceMemberDec_JavaObject0Sort)
    { 
      endVisit((ListStarOfInterfaceMemberDec_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject_MethodMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject_MethodMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof Anno_JavaObject_MethodMod_JavaObject0Sort)
    { 
      endVisit((Anno_JavaObject_MethodMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject_ClassMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject_ClassMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ClassMod_JavaObject0Sort)
    { 
      endVisit((Anno_JavaObject_ClassMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject_ConstrMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject_ConstrMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof Anno_JavaObject_ConstrMod_JavaObject0Sort)
    { 
      endVisit((Anno_JavaObject_ConstrMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject_VarMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject_VarMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof Anno_JavaObject_VarMod_JavaObject0Sort)
    { 
      endVisit((Anno_JavaObject_VarMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfAnno_JavaObject_FieldMod_JavaObject0Sort)
    { 
      endVisit((ListStarOfAnno_JavaObject_FieldMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof Anno_JavaObject_FieldMod_JavaObject0Sort)
    { 
      endVisit((Anno_JavaObject_FieldMod_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfFormalParam_JavaObject0Sort)
    { 
      endVisit((ListStarOfFormalParam_JavaObject0Sort)node);
      return;
    }
    if(node instanceof ListStarOfTerm_StrategoHost0Sort)
    { 
      endVisit((ListStarOfTerm_StrategoHost0Sort)node);
      return;
    }
    if(node instanceof ASTString)
    { 
      endVisit((ASTString)node);
      return;
    }
    if(node instanceof List)
    { 
      endVisit((List)node);
      return;
    }
    throw new java.lang.IllegalArgumentException("Node of type " + node.getClass().getSimpleName() + "not expected.");
  }
}