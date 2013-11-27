package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") final class p_111 extends Strategy 
{ 
  public static final p_111 instance = new p_111();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    Fail6:
    { 
      IStrategoTerm term0 = term;
      Success3:
      { 
        Fail7:
        { 
          IStrategoTerm term1 = term;
          IStrategoConstructor cons1 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
          Success4:
          { 
            if(cons1 == sdf_parenthesize._consprod_3)
            { 
              Fail8:
              { 
                IStrategoTerm z_108 = null;
                IStrategoTerm a_109 = null;
                IStrategoTerm b_109 = null;
                IStrategoTerm c_109 = null;
                z_108 = term.getSubterm(0);
                a_109 = term.getSubterm(1);
                b_109 = term.getSubterm(2);
                term = $Parenthesize_$List_1_0.instance.invoke(context, z_108, _Fail.instance);
                if(term == null)
                  break Fail8;
                c_109 = term;
                term = m_111.instance.invoke(context, c_109, this);
                if(term == null)
                  break Fail8;
                term = termFactory.makeAppl(sdf_parenthesize._consprod_3, new IStrategoTerm[]{term, a_109, b_109});
                term = this.invoke(context, term);
                if(term == null)
                  break Fail8;
                if(true)
                  break Success4;
              }
              term = term1;
            }
            Success5:
            { 
              if(cons1 == sdf_parenthesize._conscomp_1)
              { 
                Fail9:
                { 
                  IStrategoTerm g_109 = null;
                  g_109 = term.getSubterm(0);
                  term = g_109;
                  IStrategoTerm term3 = term;
                  IStrategoConstructor cons2 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                  Success6:
                  { 
                    if(cons2 == sdf_parenthesize._consunion_2)
                    { 
                      Fail10:
                      { 
                        if(true)
                          break Success6;
                      }
                      term = term3;
                    }
                    Success7:
                    { 
                      if(cons2 == sdf_parenthesize._consisect_2)
                      { 
                        Fail11:
                        { 
                          if(true)
                            break Success7;
                        }
                        term = term3;
                      }
                      if(cons2 == sdf_parenthesize._consdiff_2)
                      { }
                      else
                      { 
                        break Fail9;
                      }
                    }
                  }
                  term = termFactory.makeAppl(sdf_parenthesize._conscomp_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{g_109})});
                  term = this.invoke(context, term);
                  if(term == null)
                    break Fail9;
                  if(true)
                    break Success5;
                }
                term = term1;
              }
              Success8:
              { 
                if(cons1 == sdf_parenthesize._consdiff_2)
                { 
                  Fail12:
                  { 
                    IStrategoTerm i_109 = null;
                    IStrategoTerm j_109 = null;
                    j_109 = term.getSubterm(0);
                    i_109 = term.getSubterm(1);
                    term = j_109;
                    IStrategoTerm term6 = term;
                    IStrategoConstructor cons3 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                    Success9:
                    { 
                      if(cons3 == sdf_parenthesize._consunion_2)
                      { 
                        Fail13:
                        { 
                          if(true)
                            break Success9;
                        }
                        term = term6;
                      }
                      if(cons3 == sdf_parenthesize._consisect_2)
                      { }
                      else
                      { 
                        break Fail12;
                      }
                    }
                    term = termFactory.makeAppl(sdf_parenthesize._consdiff_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{j_109}), i_109});
                    term = this.invoke(context, term);
                    if(term == null)
                      break Fail12;
                    if(true)
                      break Success8;
                  }
                  term = term1;
                }
                Success10:
                { 
                  if(cons1 == sdf_parenthesize._consdiff_2)
                  { 
                    Fail14:
                    { 
                      IStrategoTerm m_109 = null;
                      IStrategoTerm n_109 = null;
                      m_109 = term.getSubterm(0);
                      n_109 = term.getSubterm(1);
                      term = n_109;
                      IStrategoTerm term8 = term;
                      IStrategoConstructor cons4 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                      Success11:
                      { 
                        if(cons4 == sdf_parenthesize._consunion_2)
                        { 
                          Fail15:
                          { 
                            if(true)
                              break Success11;
                          }
                          term = term8;
                        }
                        Success12:
                        { 
                          if(cons4 == sdf_parenthesize._consisect_2)
                          { 
                            Fail16:
                            { 
                              if(true)
                                break Success12;
                            }
                            term = term8;
                          }
                          if(cons4 == sdf_parenthesize._consdiff_2)
                          { }
                          else
                          { 
                            break Fail14;
                          }
                        }
                      }
                      term = termFactory.makeAppl(sdf_parenthesize._consdiff_2, new IStrategoTerm[]{m_109, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{n_109})});
                      term = this.invoke(context, term);
                      if(term == null)
                        break Fail14;
                      if(true)
                        break Success10;
                    }
                    term = term1;
                  }
                  Success13:
                  { 
                    if(cons1 == sdf_parenthesize._consiter_1)
                    { 
                      Fail17:
                      { 
                        IStrategoTerm r_109 = null;
                        r_109 = term.getSubterm(0);
                        term = r_109;
                        IStrategoTerm term11 = term;
                        IStrategoConstructor cons5 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                        Success14:
                        { 
                          if(cons5 == sdf_parenthesize._conslabel_2)
                          { 
                            Fail18:
                            { 
                              if(true)
                                break Success14;
                            }
                            term = term11;
                          }
                          if(cons5 == sdf_parenthesize._consalt_2)
                          { }
                          else
                          { 
                            break Fail17;
                          }
                        }
                        term = termFactory.makeAppl(sdf_parenthesize._consiter_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{r_109})});
                        term = this.invoke(context, term);
                        if(term == null)
                          break Fail17;
                        if(true)
                          break Success13;
                      }
                      term = term1;
                    }
                    Success15:
                    { 
                      if(cons1 == sdf_parenthesize._consiter_star_1)
                      { 
                        Fail19:
                        { 
                          IStrategoTerm t_109 = null;
                          t_109 = term.getSubterm(0);
                          term = t_109;
                          IStrategoTerm term13 = term;
                          IStrategoConstructor cons6 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                          Success16:
                          { 
                            if(cons6 == sdf_parenthesize._conslabel_2)
                            { 
                              Fail20:
                              { 
                                if(true)
                                  break Success16;
                              }
                              term = term13;
                            }
                            if(cons6 == sdf_parenthesize._consalt_2)
                            { }
                            else
                            { 
                              break Fail19;
                            }
                          }
                          term = termFactory.makeAppl(sdf_parenthesize._consiter_star_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{t_109})});
                          term = this.invoke(context, term);
                          if(term == null)
                            break Fail19;
                          if(true)
                            break Success15;
                        }
                        term = term1;
                      }
                      Success17:
                      { 
                        if(cons1 == sdf_parenthesize._consopt_1)
                        { 
                          Fail21:
                          { 
                            IStrategoTerm v_109 = null;
                            v_109 = term.getSubterm(0);
                            term = v_109;
                            IStrategoTerm term15 = term;
                            IStrategoConstructor cons7 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                            Success18:
                            { 
                              if(cons7 == sdf_parenthesize._conslabel_2)
                              { 
                                Fail22:
                                { 
                                  if(true)
                                    break Success18;
                                }
                                term = term15;
                              }
                              if(cons7 == sdf_parenthesize._consalt_2)
                              { }
                              else
                              { 
                                break Fail21;
                              }
                            }
                            term = termFactory.makeAppl(sdf_parenthesize._consopt_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{v_109})});
                            term = this.invoke(context, term);
                            if(term == null)
                              break Fail21;
                            if(true)
                              break Success17;
                          }
                          term = term1;
                        }
                        Success19:
                        { 
                          if(cons1 == sdf_parenthesize._consalt_2)
                          { 
                            Fail23:
                            { 
                              IStrategoTerm x_109 = null;
                              IStrategoTerm y_109 = null;
                              y_109 = term.getSubterm(0);
                              x_109 = term.getSubterm(1);
                              term = y_109;
                              IStrategoTerm term17 = term;
                              IStrategoConstructor cons8 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                              Success20:
                              { 
                                if(cons8 == sdf_parenthesize._conslabel_2)
                                { 
                                  Fail24:
                                  { 
                                    if(true)
                                      break Success20;
                                  }
                                  term = term17;
                                }
                                if(cons8 == sdf_parenthesize._consalt_2)
                                { }
                                else
                                { 
                                  break Fail23;
                                }
                              }
                              term = termFactory.makeAppl(sdf_parenthesize._consalt_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{y_109}), x_109});
                              term = this.invoke(context, term);
                              if(term == null)
                                break Fail23;
                              if(true)
                                break Success19;
                            }
                            term = term1;
                          }
                          Success21:
                          { 
                            if(cons1 == sdf_parenthesize._consalt_2)
                            { 
                              Fail25:
                              { 
                                IStrategoTerm a_110 = null;
                                IStrategoTerm b_110 = null;
                                a_110 = term.getSubterm(0);
                                b_110 = term.getSubterm(1);
                                term = b_110;
                                if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conslabel_2 != ((IStrategoAppl)term).getConstructor())
                                  break Fail25;
                                term = termFactory.makeAppl(sdf_parenthesize._consalt_2, new IStrategoTerm[]{a_110, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{b_110})});
                                term = this.invoke(context, term);
                                if(term == null)
                                  break Fail25;
                                if(true)
                                  break Success21;
                              }
                              term = term1;
                            }
                            Success22:
                            { 
                              if(cons1 == sdf_parenthesize._consseq_2)
                              { 
                                Fail26:
                                { 
                                  IStrategoTerm d_110 = null;
                                  IStrategoTerm e_110 = null;
                                  e_110 = term.getSubterm(0);
                                  d_110 = term.getSubterm(1);
                                  term = e_110;
                                  if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consalt_2 != ((IStrategoAppl)term).getConstructor())
                                    break Fail26;
                                  term = termFactory.makeAppl(sdf_parenthesize._consseq_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{e_110}), d_110});
                                  term = this.invoke(context, term);
                                  if(term == null)
                                    break Fail26;
                                  if(true)
                                    break Success22;
                                }
                                term = term1;
                              }
                              Success23:
                              { 
                                if(cons1 == sdf_parenthesize._consseq_2)
                                { 
                                  Fail27:
                                  { 
                                    IStrategoTerm g_110 = null;
                                    IStrategoTerm h_110 = null;
                                    g_110 = term.getSubterm(0);
                                    h_110 = term.getSubterm(1);
                                    term = h_110;
                                    if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consalt_2 != ((IStrategoAppl)term).getConstructor())
                                      break Fail27;
                                    term = termFactory.makeAppl(sdf_parenthesize._consseq_2, new IStrategoTerm[]{g_110, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{h_110})});
                                    term = this.invoke(context, term);
                                    if(term == null)
                                      break Fail27;
                                    if(true)
                                      break Success23;
                                  }
                                  term = term1;
                                }
                                Success24:
                                { 
                                  if(cons1 == sdf_parenthesize._consisect_2)
                                  { 
                                    Fail28:
                                    { 
                                      IStrategoTerm j_110 = null;
                                      IStrategoTerm k_110 = null;
                                      k_110 = term.getSubterm(0);
                                      j_110 = term.getSubterm(1);
                                      term = k_110;
                                      if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consunion_2 != ((IStrategoAppl)term).getConstructor())
                                        break Fail28;
                                      term = termFactory.makeAppl(sdf_parenthesize._consisect_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{k_110}), j_110});
                                      term = this.invoke(context, term);
                                      if(term == null)
                                        break Fail28;
                                      if(true)
                                        break Success24;
                                    }
                                    term = term1;
                                  }
                                  Success25:
                                  { 
                                    if(cons1 == sdf_parenthesize._consisect_2)
                                    { 
                                      Fail29:
                                      { 
                                        IStrategoTerm m_110 = null;
                                        IStrategoTerm n_110 = null;
                                        m_110 = term.getSubterm(0);
                                        n_110 = term.getSubterm(1);
                                        term = n_110;
                                        IStrategoTerm term23 = term;
                                        IStrategoConstructor cons9 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                                        Success26:
                                        { 
                                          if(cons9 == sdf_parenthesize._consunion_2)
                                          { 
                                            Fail30:
                                            { 
                                              if(true)
                                                break Success26;
                                            }
                                            term = term23;
                                          }
                                          if(cons9 == sdf_parenthesize._consisect_2)
                                          { }
                                          else
                                          { 
                                            break Fail29;
                                          }
                                        }
                                        term = termFactory.makeAppl(sdf_parenthesize._consisect_2, new IStrategoTerm[]{m_110, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{n_110})});
                                        term = this.invoke(context, term);
                                        if(term == null)
                                          break Fail29;
                                        if(true)
                                          break Success25;
                                      }
                                      term = term1;
                                    }
                                    Success27:
                                    { 
                                      if(cons1 == sdf_parenthesize._consnon_transitive_1)
                                      { 
                                        Fail31:
                                        { 
                                          IStrategoTerm p_110 = null;
                                          p_110 = term.getSubterm(0);
                                          term = p_110;
                                          if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consnon_transitive_1 != ((IStrategoAppl)term).getConstructor())
                                            break Fail31;
                                          term = termFactory.makeAppl(sdf_parenthesize._consnon_transitive_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{p_110})});
                                          term = this.invoke(context, term);
                                          if(term == null)
                                            break Fail31;
                                          if(true)
                                            break Success27;
                                        }
                                        term = term1;
                                      }
                                      Success28:
                                      { 
                                        if(cons1 == sdf_parenthesize._consunion_2)
                                        { 
                                          Fail32:
                                          { 
                                            IStrategoTerm r_110 = null;
                                            IStrategoTerm s_110 = null;
                                            r_110 = term.getSubterm(0);
                                            s_110 = term.getSubterm(1);
                                            term = s_110;
                                            if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consunion_2 != ((IStrategoAppl)term).getConstructor())
                                              break Fail32;
                                            term = termFactory.makeAppl(sdf_parenthesize._consunion_2, new IStrategoTerm[]{r_110, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{s_110})});
                                            term = this.invoke(context, term);
                                            if(term == null)
                                              break Fail32;
                                            if(true)
                                              break Success28;
                                          }
                                          term = term1;
                                        }
                                        Success29:
                                        { 
                                          if(cons1 == sdf_parenthesize._consconc_2)
                                          { 
                                            Fail33:
                                            { 
                                              IStrategoTerm u_110 = null;
                                              IStrategoTerm v_110 = null;
                                              v_110 = term.getSubterm(0);
                                              u_110 = term.getSubterm(1);
                                              term = v_110;
                                              if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consconc_2 != ((IStrategoAppl)term).getConstructor())
                                                break Fail33;
                                              term = termFactory.makeAppl(sdf_parenthesize._consconc_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{v_110}), u_110});
                                              term = this.invoke(context, term);
                                              if(term == null)
                                                break Fail33;
                                              if(true)
                                                break Success29;
                                            }
                                            term = term1;
                                          }
                                          Success30:
                                          { 
                                            if(cons1 == sdf_parenthesize._conswith_arguments_2)
                                            { 
                                              Fail34:
                                              { 
                                                IStrategoTerm d_111 = null;
                                                IStrategoTerm e_111 = null;
                                                e_111 = term.getSubterm(0);
                                                d_111 = term.getSubterm(1);
                                                term = e_111;
                                                if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conswith_arguments_2 != ((IStrategoAppl)term).getConstructor())
                                                  break Fail34;
                                                term = termFactory.makeAppl(sdf_parenthesize._conswith_arguments_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{e_111}), d_111});
                                                term = this.invoke(context, term);
                                                if(term == null)
                                                  break Fail34;
                                                if(true)
                                                  break Success30;
                                              }
                                              term = term1;
                                            }
                                            Success31:
                                            { 
                                              if(cons1 == sdf_parenthesize._conswith_arguments_2)
                                              { 
                                                Fail35:
                                                { 
                                                  IStrategoTerm g_111 = null;
                                                  IStrategoTerm h_111 = null;
                                                  g_111 = term.getSubterm(0);
                                                  h_111 = term.getSubterm(1);
                                                  term = h_111;
                                                  if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conswith_arguments_2 != ((IStrategoAppl)term).getConstructor())
                                                    break Fail35;
                                                  term = termFactory.makeAppl(sdf_parenthesize._conswith_arguments_2, new IStrategoTerm[]{g_111, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{h_111})});
                                                  term = this.invoke(context, term);
                                                  if(term == null)
                                                    break Fail35;
                                                  if(true)
                                                    break Success31;
                                                }
                                                term = term1;
                                              }
                                              if(cons1 == sdf_parenthesize._consconc_grammars_2)
                                              { 
                                                IStrategoTerm j_111 = null;
                                                IStrategoTerm k_111 = null;
                                                j_111 = term.getSubterm(0);
                                                k_111 = term.getSubterm(1);
                                                term = k_111;
                                                if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consconc_grammars_2 != ((IStrategoAppl)term).getConstructor())
                                                  break Fail7;
                                                term = termFactory.makeAppl(sdf_parenthesize._consconc_grammars_2, new IStrategoTerm[]{j_111, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{k_111})});
                                                term = this.invoke(context, term);
                                                if(term == null)
                                                  break Fail7;
                                              }
                                              else
                                              { 
                                                break Fail7;
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
          }
          if(true)
            break Success3;
        }
        term = term0;
      }
      if(true)
        return term;
    }
    return null;
  }
}