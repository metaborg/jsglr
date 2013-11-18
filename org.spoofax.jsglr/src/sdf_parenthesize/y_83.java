package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class y_83 extends Strategy 
{ 
  public static final y_83 instance = new y_83();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    Fail31:
    { 
      IStrategoTerm term0 = term;
      Success27:
      { 
        Fail32:
        { 
          IStrategoTerm term1 = term;
          IStrategoConstructor cons9 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
          Success28:
          { 
            if(cons9 == sdf_parenthesize._conscomp_1)
            { 
              Fail33:
              { 
                IStrategoTerm a_82 = null;
                a_82 = term.getSubterm(0);
                term = a_82;
                IStrategoTerm term2 = term;
                IStrategoConstructor cons10 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                Success29:
                { 
                  if(cons10 == sdf_parenthesize._consunion_2)
                  { 
                    Fail34:
                    { 
                      if(true)
                        break Success29;
                    }
                    term = term2;
                  }
                  Success30:
                  { 
                    if(cons10 == sdf_parenthesize._consisect_2)
                    { 
                      Fail35:
                      { 
                        if(true)
                          break Success30;
                      }
                      term = term2;
                    }
                    if(cons10 == sdf_parenthesize._consdiff_2)
                    { }
                    else
                    { 
                      break Fail33;
                    }
                  }
                }
                term = termFactory.makeAppl(sdf_parenthesize._conscomp_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{a_82})});
                term = this.invoke(context, term);
                if(term == null)
                  break Fail33;
                if(true)
                  break Success28;
              }
              term = term1;
            }
            Success31:
            { 
              if(cons9 == sdf_parenthesize._consdiff_2)
              { 
                Fail36:
                { 
                  IStrategoTerm c_82 = null;
                  IStrategoTerm d_82 = null;
                  d_82 = term.getSubterm(0);
                  c_82 = term.getSubterm(1);
                  term = d_82;
                  IStrategoTerm term5 = term;
                  IStrategoConstructor cons11 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                  Success32:
                  { 
                    if(cons11 == sdf_parenthesize._consunion_2)
                    { 
                      Fail37:
                      { 
                        if(true)
                          break Success32;
                      }
                      term = term5;
                    }
                    if(cons11 == sdf_parenthesize._consisect_2)
                    { }
                    else
                    { 
                      break Fail36;
                    }
                  }
                  term = termFactory.makeAppl(sdf_parenthesize._consdiff_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{d_82}), c_82});
                  term = this.invoke(context, term);
                  if(term == null)
                    break Fail36;
                  if(true)
                    break Success31;
                }
                term = term1;
              }
              Success33:
              { 
                if(cons9 == sdf_parenthesize._consdiff_2)
                { 
                  Fail38:
                  { 
                    IStrategoTerm f_82 = null;
                    IStrategoTerm g_82 = null;
                    f_82 = term.getSubterm(0);
                    g_82 = term.getSubterm(1);
                    term = g_82;
                    IStrategoTerm term7 = term;
                    IStrategoConstructor cons12 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                    Success34:
                    { 
                      if(cons12 == sdf_parenthesize._consunion_2)
                      { 
                        Fail39:
                        { 
                          if(true)
                            break Success34;
                        }
                        term = term7;
                      }
                      Success35:
                      { 
                        if(cons12 == sdf_parenthesize._consisect_2)
                        { 
                          Fail40:
                          { 
                            if(true)
                              break Success35;
                          }
                          term = term7;
                        }
                        if(cons12 == sdf_parenthesize._consdiff_2)
                        { }
                        else
                        { 
                          break Fail38;
                        }
                      }
                    }
                    term = termFactory.makeAppl(sdf_parenthesize._consdiff_2, new IStrategoTerm[]{f_82, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{g_82})});
                    term = this.invoke(context, term);
                    if(term == null)
                      break Fail38;
                    if(true)
                      break Success33;
                  }
                  term = term1;
                }
                Success36:
                { 
                  if(cons9 == sdf_parenthesize._consiter_1)
                  { 
                    Fail41:
                    { 
                      IStrategoTerm i_82 = null;
                      i_82 = term.getSubterm(0);
                      term = i_82;
                      IStrategoTerm term10 = term;
                      IStrategoConstructor cons13 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                      Success37:
                      { 
                        if(cons13 == sdf_parenthesize._conslabel_2)
                        { 
                          Fail42:
                          { 
                            if(true)
                              break Success37;
                          }
                          term = term10;
                        }
                        if(cons13 == sdf_parenthesize._consalt_2)
                        { }
                        else
                        { 
                          break Fail41;
                        }
                      }
                      term = termFactory.makeAppl(sdf_parenthesize._consiter_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{i_82})});
                      term = this.invoke(context, term);
                      if(term == null)
                        break Fail41;
                      if(true)
                        break Success36;
                    }
                    term = term1;
                  }
                  Success38:
                  { 
                    if(cons9 == sdf_parenthesize._consiter_star_1)
                    { 
                      Fail43:
                      { 
                        IStrategoTerm k_82 = null;
                        k_82 = term.getSubterm(0);
                        term = k_82;
                        IStrategoTerm term12 = term;
                        IStrategoConstructor cons14 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                        Success39:
                        { 
                          if(cons14 == sdf_parenthesize._conslabel_2)
                          { 
                            Fail44:
                            { 
                              if(true)
                                break Success39;
                            }
                            term = term12;
                          }
                          if(cons14 == sdf_parenthesize._consalt_2)
                          { }
                          else
                          { 
                            break Fail43;
                          }
                        }
                        term = termFactory.makeAppl(sdf_parenthesize._consiter_star_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{k_82})});
                        term = this.invoke(context, term);
                        if(term == null)
                          break Fail43;
                        if(true)
                          break Success38;
                      }
                      term = term1;
                    }
                    Success40:
                    { 
                      if(cons9 == sdf_parenthesize._consopt_1)
                      { 
                        Fail45:
                        { 
                          IStrategoTerm m_82 = null;
                          m_82 = term.getSubterm(0);
                          term = m_82;
                          IStrategoTerm term14 = term;
                          IStrategoConstructor cons15 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                          Success41:
                          { 
                            if(cons15 == sdf_parenthesize._conslabel_2)
                            { 
                              Fail46:
                              { 
                                if(true)
                                  break Success41;
                              }
                              term = term14;
                            }
                            if(cons15 == sdf_parenthesize._consalt_2)
                            { }
                            else
                            { 
                              break Fail45;
                            }
                          }
                          term = termFactory.makeAppl(sdf_parenthesize._consopt_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{m_82})});
                          term = this.invoke(context, term);
                          if(term == null)
                            break Fail45;
                          if(true)
                            break Success40;
                        }
                        term = term1;
                      }
                      Success42:
                      { 
                        if(cons9 == sdf_parenthesize._consalt_2)
                        { 
                          Fail47:
                          { 
                            IStrategoTerm o_82 = null;
                            IStrategoTerm p_82 = null;
                            p_82 = term.getSubterm(0);
                            o_82 = term.getSubterm(1);
                            term = p_82;
                            IStrategoTerm term16 = term;
                            IStrategoConstructor cons16 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                            Success43:
                            { 
                              if(cons16 == sdf_parenthesize._conslabel_2)
                              { 
                                Fail48:
                                { 
                                  if(true)
                                    break Success43;
                                }
                                term = term16;
                              }
                              if(cons16 == sdf_parenthesize._consalt_2)
                              { }
                              else
                              { 
                                break Fail47;
                              }
                            }
                            term = termFactory.makeAppl(sdf_parenthesize._consalt_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{p_82}), o_82});
                            term = this.invoke(context, term);
                            if(term == null)
                              break Fail47;
                            if(true)
                              break Success42;
                          }
                          term = term1;
                        }
                        Success44:
                        { 
                          if(cons9 == sdf_parenthesize._consalt_2)
                          { 
                            Fail49:
                            { 
                              IStrategoTerm r_82 = null;
                              IStrategoTerm s_82 = null;
                              r_82 = term.getSubterm(0);
                              s_82 = term.getSubterm(1);
                              term = s_82;
                              if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conslabel_2 != ((IStrategoAppl)term).getConstructor())
                                break Fail49;
                              term = termFactory.makeAppl(sdf_parenthesize._consalt_2, new IStrategoTerm[]{r_82, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{s_82})});
                              term = this.invoke(context, term);
                              if(term == null)
                                break Fail49;
                              if(true)
                                break Success44;
                            }
                            term = term1;
                          }
                          Success45:
                          { 
                            if(cons9 == sdf_parenthesize._consseq_2)
                            { 
                              Fail50:
                              { 
                                IStrategoTerm u_82 = null;
                                IStrategoTerm v_82 = null;
                                v_82 = term.getSubterm(0);
                                u_82 = term.getSubterm(1);
                                term = v_82;
                                if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consalt_2 != ((IStrategoAppl)term).getConstructor())
                                  break Fail50;
                                term = termFactory.makeAppl(sdf_parenthesize._consseq_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{v_82}), u_82});
                                term = this.invoke(context, term);
                                if(term == null)
                                  break Fail50;
                                if(true)
                                  break Success45;
                              }
                              term = term1;
                            }
                            Success46:
                            { 
                              if(cons9 == sdf_parenthesize._consseq_2)
                              { 
                                Fail51:
                                { 
                                  IStrategoTerm x_82 = null;
                                  IStrategoTerm y_82 = null;
                                  x_82 = term.getSubterm(0);
                                  y_82 = term.getSubterm(1);
                                  term = y_82;
                                  if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consalt_2 != ((IStrategoAppl)term).getConstructor())
                                    break Fail51;
                                  term = termFactory.makeAppl(sdf_parenthesize._consseq_2, new IStrategoTerm[]{x_82, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{y_82})});
                                  term = this.invoke(context, term);
                                  if(term == null)
                                    break Fail51;
                                  if(true)
                                    break Success46;
                                }
                                term = term1;
                              }
                              Success47:
                              { 
                                if(cons9 == sdf_parenthesize._consisect_2)
                                { 
                                  Fail52:
                                  { 
                                    IStrategoTerm a_83 = null;
                                    IStrategoTerm b_83 = null;
                                    b_83 = term.getSubterm(0);
                                    a_83 = term.getSubterm(1);
                                    term = b_83;
                                    if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consunion_2 != ((IStrategoAppl)term).getConstructor())
                                      break Fail52;
                                    term = termFactory.makeAppl(sdf_parenthesize._consisect_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{b_83}), a_83});
                                    term = this.invoke(context, term);
                                    if(term == null)
                                      break Fail52;
                                    if(true)
                                      break Success47;
                                  }
                                  term = term1;
                                }
                                Success48:
                                { 
                                  if(cons9 == sdf_parenthesize._consisect_2)
                                  { 
                                    Fail53:
                                    { 
                                      IStrategoTerm d_83 = null;
                                      IStrategoTerm e_83 = null;
                                      d_83 = term.getSubterm(0);
                                      e_83 = term.getSubterm(1);
                                      term = e_83;
                                      IStrategoTerm term22 = term;
                                      IStrategoConstructor cons17 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                                      Success49:
                                      { 
                                        if(cons17 == sdf_parenthesize._consunion_2)
                                        { 
                                          Fail54:
                                          { 
                                            if(true)
                                              break Success49;
                                          }
                                          term = term22;
                                        }
                                        if(cons17 == sdf_parenthesize._consisect_2)
                                        { }
                                        else
                                        { 
                                          break Fail53;
                                        }
                                      }
                                      term = termFactory.makeAppl(sdf_parenthesize._consisect_2, new IStrategoTerm[]{d_83, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{e_83})});
                                      term = this.invoke(context, term);
                                      if(term == null)
                                        break Fail53;
                                      if(true)
                                        break Success48;
                                    }
                                    term = term1;
                                  }
                                  Success50:
                                  { 
                                    if(cons9 == sdf_parenthesize._consnon_transitive_1)
                                    { 
                                      Fail55:
                                      { 
                                        IStrategoTerm g_83 = null;
                                        g_83 = term.getSubterm(0);
                                        term = g_83;
                                        if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consnon_transitive_1 != ((IStrategoAppl)term).getConstructor())
                                          break Fail55;
                                        term = termFactory.makeAppl(sdf_parenthesize._consnon_transitive_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{g_83})});
                                        term = this.invoke(context, term);
                                        if(term == null)
                                          break Fail55;
                                        if(true)
                                          break Success50;
                                      }
                                      term = term1;
                                    }
                                    Success51:
                                    { 
                                      if(cons9 == sdf_parenthesize._consunion_2)
                                      { 
                                        Fail56:
                                        { 
                                          IStrategoTerm i_83 = null;
                                          IStrategoTerm j_83 = null;
                                          i_83 = term.getSubterm(0);
                                          j_83 = term.getSubterm(1);
                                          term = j_83;
                                          if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consunion_2 != ((IStrategoAppl)term).getConstructor())
                                            break Fail56;
                                          term = termFactory.makeAppl(sdf_parenthesize._consunion_2, new IStrategoTerm[]{i_83, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{j_83})});
                                          term = this.invoke(context, term);
                                          if(term == null)
                                            break Fail56;
                                          if(true)
                                            break Success51;
                                        }
                                        term = term1;
                                      }
                                      Success52:
                                      { 
                                        if(cons9 == sdf_parenthesize._consconc_2)
                                        { 
                                          Fail57:
                                          { 
                                            IStrategoTerm l_83 = null;
                                            IStrategoTerm m_83 = null;
                                            m_83 = term.getSubterm(0);
                                            l_83 = term.getSubterm(1);
                                            term = m_83;
                                            if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consconc_2 != ((IStrategoAppl)term).getConstructor())
                                              break Fail57;
                                            term = termFactory.makeAppl(sdf_parenthesize._consconc_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{m_83}), l_83});
                                            term = this.invoke(context, term);
                                            if(term == null)
                                              break Fail57;
                                            if(true)
                                              break Success52;
                                          }
                                          term = term1;
                                        }
                                        Success53:
                                        { 
                                          if(cons9 == sdf_parenthesize._conswith_arguments_2)
                                          { 
                                            Fail58:
                                            { 
                                              IStrategoTerm o_83 = null;
                                              IStrategoTerm p_83 = null;
                                              p_83 = term.getSubterm(0);
                                              o_83 = term.getSubterm(1);
                                              term = p_83;
                                              if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conswith_arguments_2 != ((IStrategoAppl)term).getConstructor())
                                                break Fail58;
                                              term = termFactory.makeAppl(sdf_parenthesize._conswith_arguments_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{p_83}), o_83});
                                              term = this.invoke(context, term);
                                              if(term == null)
                                                break Fail58;
                                              if(true)
                                                break Success53;
                                            }
                                            term = term1;
                                          }
                                          Success54:
                                          { 
                                            if(cons9 == sdf_parenthesize._conswith_arguments_2)
                                            { 
                                              Fail59:
                                              { 
                                                IStrategoTerm r_83 = null;
                                                IStrategoTerm s_83 = null;
                                                r_83 = term.getSubterm(0);
                                                s_83 = term.getSubterm(1);
                                                term = s_83;
                                                if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conswith_arguments_2 != ((IStrategoAppl)term).getConstructor())
                                                  break Fail59;
                                                term = termFactory.makeAppl(sdf_parenthesize._conswith_arguments_2, new IStrategoTerm[]{r_83, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{s_83})});
                                                term = this.invoke(context, term);
                                                if(term == null)
                                                  break Fail59;
                                                if(true)
                                                  break Success54;
                                              }
                                              term = term1;
                                            }
                                            if(cons9 == sdf_parenthesize._consconc_grammars_2)
                                            { 
                                              IStrategoTerm u_83 = null;
                                              IStrategoTerm v_83 = null;
                                              u_83 = term.getSubterm(0);
                                              v_83 = term.getSubterm(1);
                                              term = v_83;
                                              if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consconc_grammars_2 != ((IStrategoAppl)term).getConstructor())
                                                break Fail32;
                                              term = termFactory.makeAppl(sdf_parenthesize._consconc_grammars_2, new IStrategoTerm[]{u_83, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{v_83})});
                                              term = this.invoke(context, term);
                                              if(term == null)
                                                break Fail32;
                                            }
                                            else
                                            { 
                                              break Fail32;
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
          if(true)
            break Success27;
        }
        term = term0;
      }
      if(true)
        return term;
    }
    return null;
  }
}