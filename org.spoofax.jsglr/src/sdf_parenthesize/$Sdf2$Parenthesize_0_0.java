package sdf_parenthesize;

import org.strategoxt.stratego_lib.*;
import org.strategoxt.lang.*;
import org.spoofax.interpreter.terms.*;
import static org.strategoxt.lang.Term.*;
import org.spoofax.interpreter.library.AbstractPrimitive;
import java.util.ArrayList;
import java.lang.ref.WeakReference;

@SuppressWarnings("all") public class $Sdf2$Parenthesize_0_0 extends Strategy 
{ 
  public static $Sdf2$Parenthesize_0_0 instance = new $Sdf2$Parenthesize_0_0();

  @Override public IStrategoTerm invoke(Context context, IStrategoTerm term)
  { 
    ITermFactory termFactory = context.getFactory();
    Fail2:
    { 
      IStrategoTerm term28 = term;
      IStrategoConstructor cons0 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
      Success0:
      { 
        if(cons0 == sdf_parenthesize._conscomp_1)
        { 
          Fail3:
          { 
            IStrategoTerm z_1 = null;
            z_1 = term.getSubterm(0);
            term = z_1;
            IStrategoTerm term29 = term;
            IStrategoConstructor cons1 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
            Success1:
            { 
              if(cons1 == sdf_parenthesize._consunion_2)
              { 
                Fail4:
                { 
                  if(true)
                    break Success1;
                }
                term = term29;
              }
              Success2:
              { 
                if(cons1 == sdf_parenthesize._consisect_2)
                { 
                  Fail5:
                  { 
                    if(true)
                      break Success2;
                  }
                  term = term29;
                }
                if(cons1 == sdf_parenthesize._consdiff_2)
                { }
                else
                { 
                  break Fail3;
                }
              }
            }
            term = termFactory.makeAppl(sdf_parenthesize._conscomp_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{z_1})});
            if(true)
              break Success0;
          }
          term = term28;
        }
        Success3:
        { 
          if(cons0 == sdf_parenthesize._consdiff_2)
          { 
            Fail6:
            { 
              IStrategoTerm w_1 = null;
              IStrategoTerm x_1 = null;
              x_1 = term.getSubterm(0);
              w_1 = term.getSubterm(1);
              term = x_1;
              IStrategoTerm term32 = term;
              IStrategoConstructor cons2 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
              Success4:
              { 
                if(cons2 == sdf_parenthesize._consunion_2)
                { 
                  Fail7:
                  { 
                    if(true)
                      break Success4;
                  }
                  term = term32;
                }
                if(cons2 == sdf_parenthesize._consisect_2)
                { }
                else
                { 
                  break Fail6;
                }
              }
              term = termFactory.makeAppl(sdf_parenthesize._consdiff_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{x_1}), w_1});
              if(true)
                break Success3;
            }
            term = term28;
          }
          Success5:
          { 
            if(cons0 == sdf_parenthesize._consdiff_2)
            { 
              Fail8:
              { 
                IStrategoTerm s_1 = null;
                IStrategoTerm u_1 = null;
                s_1 = term.getSubterm(0);
                u_1 = term.getSubterm(1);
                term = u_1;
                IStrategoTerm term34 = term;
                IStrategoConstructor cons3 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                Success6:
                { 
                  if(cons3 == sdf_parenthesize._consunion_2)
                  { 
                    Fail9:
                    { 
                      if(true)
                        break Success6;
                    }
                    term = term34;
                  }
                  Success7:
                  { 
                    if(cons3 == sdf_parenthesize._consisect_2)
                    { 
                      Fail10:
                      { 
                        if(true)
                          break Success7;
                      }
                      term = term34;
                    }
                    if(cons3 == sdf_parenthesize._consdiff_2)
                    { }
                    else
                    { 
                      break Fail8;
                    }
                  }
                }
                term = termFactory.makeAppl(sdf_parenthesize._consdiff_2, new IStrategoTerm[]{s_1, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{u_1})});
                if(true)
                  break Success5;
              }
              term = term28;
            }
            Success8:
            { 
              if(cons0 == sdf_parenthesize._consiter_1)
              { 
                Fail11:
                { 
                  IStrategoTerm q_1 = null;
                  q_1 = term.getSubterm(0);
                  term = q_1;
                  IStrategoTerm term37 = term;
                  IStrategoConstructor cons4 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                  Success9:
                  { 
                    if(cons4 == sdf_parenthesize._conslabel_2)
                    { 
                      Fail12:
                      { 
                        if(true)
                          break Success9;
                      }
                      term = term37;
                    }
                    if(cons4 == sdf_parenthesize._consalt_2)
                    { }
                    else
                    { 
                      break Fail11;
                    }
                  }
                  term = termFactory.makeAppl(sdf_parenthesize._consiter_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{q_1})});
                  if(true)
                    break Success8;
                }
                term = term28;
              }
              Success10:
              { 
                if(cons0 == sdf_parenthesize._consiter_star_1)
                { 
                  Fail13:
                  { 
                    IStrategoTerm o_1 = null;
                    o_1 = term.getSubterm(0);
                    term = o_1;
                    IStrategoTerm term39 = term;
                    IStrategoConstructor cons5 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                    Success11:
                    { 
                      if(cons5 == sdf_parenthesize._conslabel_2)
                      { 
                        Fail14:
                        { 
                          if(true)
                            break Success11;
                        }
                        term = term39;
                      }
                      if(cons5 == sdf_parenthesize._consalt_2)
                      { }
                      else
                      { 
                        break Fail13;
                      }
                    }
                    term = termFactory.makeAppl(sdf_parenthesize._consiter_star_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{o_1})});
                    if(true)
                      break Success10;
                  }
                  term = term28;
                }
                Success12:
                { 
                  if(cons0 == sdf_parenthesize._consopt_1)
                  { 
                    Fail15:
                    { 
                      IStrategoTerm m_1 = null;
                      m_1 = term.getSubterm(0);
                      term = m_1;
                      IStrategoTerm term41 = term;
                      IStrategoConstructor cons6 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                      Success13:
                      { 
                        if(cons6 == sdf_parenthesize._conslabel_2)
                        { 
                          Fail16:
                          { 
                            if(true)
                              break Success13;
                          }
                          term = term41;
                        }
                        if(cons6 == sdf_parenthesize._consalt_2)
                        { }
                        else
                        { 
                          break Fail15;
                        }
                      }
                      term = termFactory.makeAppl(sdf_parenthesize._consopt_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{m_1})});
                      if(true)
                        break Success12;
                    }
                    term = term28;
                  }
                  Success14:
                  { 
                    if(cons0 == sdf_parenthesize._consalt_2)
                    { 
                      Fail17:
                      { 
                        IStrategoTerm j_1 = null;
                        IStrategoTerm k_1 = null;
                        k_1 = term.getSubterm(0);
                        j_1 = term.getSubterm(1);
                        term = k_1;
                        IStrategoTerm term43 = term;
                        IStrategoConstructor cons7 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                        Success15:
                        { 
                          if(cons7 == sdf_parenthesize._conslabel_2)
                          { 
                            Fail18:
                            { 
                              if(true)
                                break Success15;
                            }
                            term = term43;
                          }
                          if(cons7 == sdf_parenthesize._consalt_2)
                          { }
                          else
                          { 
                            break Fail17;
                          }
                        }
                        term = termFactory.makeAppl(sdf_parenthesize._consalt_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{k_1}), j_1});
                        if(true)
                          break Success14;
                      }
                      term = term28;
                    }
                    Success16:
                    { 
                      if(cons0 == sdf_parenthesize._consalt_2)
                      { 
                        Fail19:
                        { 
                          IStrategoTerm g_1 = null;
                          IStrategoTerm h_1 = null;
                          g_1 = term.getSubterm(0);
                          h_1 = term.getSubterm(1);
                          term = h_1;
                          if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conslabel_2 != ((IStrategoAppl)term).getConstructor())
                            break Fail19;
                          term = termFactory.makeAppl(sdf_parenthesize._consalt_2, new IStrategoTerm[]{g_1, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{h_1})});
                          if(true)
                            break Success16;
                        }
                        term = term28;
                      }
                      Success17:
                      { 
                        if(cons0 == sdf_parenthesize._consseq_2)
                        { 
                          Fail20:
                          { 
                            IStrategoTerm d_1 = null;
                            IStrategoTerm e_1 = null;
                            e_1 = term.getSubterm(0);
                            d_1 = term.getSubterm(1);
                            term = e_1;
                            if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consalt_2 != ((IStrategoAppl)term).getConstructor())
                              break Fail20;
                            term = termFactory.makeAppl(sdf_parenthesize._consseq_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{e_1}), d_1});
                            if(true)
                              break Success17;
                          }
                          term = term28;
                        }
                        Success18:
                        { 
                          if(cons0 == sdf_parenthesize._consseq_2)
                          { 
                            Fail21:
                            { 
                              IStrategoTerm a_1 = null;
                              IStrategoTerm b_1 = null;
                              a_1 = term.getSubterm(0);
                              b_1 = term.getSubterm(1);
                              term = b_1;
                              if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consalt_2 != ((IStrategoAppl)term).getConstructor())
                                break Fail21;
                              term = termFactory.makeAppl(sdf_parenthesize._consseq_2, new IStrategoTerm[]{a_1, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{b_1})});
                              if(true)
                                break Success18;
                            }
                            term = term28;
                          }
                          Success19:
                          { 
                            if(cons0 == sdf_parenthesize._consisect_2)
                            { 
                              Fail22:
                              { 
                                IStrategoTerm x_0 = null;
                                IStrategoTerm y_0 = null;
                                y_0 = term.getSubterm(0);
                                x_0 = term.getSubterm(1);
                                term = y_0;
                                if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consunion_2 != ((IStrategoAppl)term).getConstructor())
                                  break Fail22;
                                term = termFactory.makeAppl(sdf_parenthesize._consisect_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{y_0}), x_0});
                                if(true)
                                  break Success19;
                              }
                              term = term28;
                            }
                            Success20:
                            { 
                              if(cons0 == sdf_parenthesize._consisect_2)
                              { 
                                Fail23:
                                { 
                                  IStrategoTerm u_0 = null;
                                  IStrategoTerm v_0 = null;
                                  u_0 = term.getSubterm(0);
                                  v_0 = term.getSubterm(1);
                                  term = v_0;
                                  IStrategoTerm term49 = term;
                                  IStrategoConstructor cons8 = term.getTermType() == IStrategoTerm.APPL ? ((IStrategoAppl)term).getConstructor() : null;
                                  Success21:
                                  { 
                                    if(cons8 == sdf_parenthesize._consunion_2)
                                    { 
                                      Fail24:
                                      { 
                                        if(true)
                                          break Success21;
                                      }
                                      term = term49;
                                    }
                                    if(cons8 == sdf_parenthesize._consisect_2)
                                    { }
                                    else
                                    { 
                                      break Fail23;
                                    }
                                  }
                                  term = termFactory.makeAppl(sdf_parenthesize._consisect_2, new IStrategoTerm[]{u_0, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{v_0})});
                                  if(true)
                                    break Success20;
                                }
                                term = term28;
                              }
                              Success22:
                              { 
                                if(cons0 == sdf_parenthesize._consnon_transitive_1)
                                { 
                                  Fail25:
                                  { 
                                    IStrategoTerm r_0 = null;
                                    r_0 = term.getSubterm(0);
                                    term = r_0;
                                    if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consnon_transitive_1 != ((IStrategoAppl)term).getConstructor())
                                      break Fail25;
                                    term = termFactory.makeAppl(sdf_parenthesize._consnon_transitive_1, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{r_0})});
                                    if(true)
                                      break Success22;
                                  }
                                  term = term28;
                                }
                                Success23:
                                { 
                                  if(cons0 == sdf_parenthesize._consunion_2)
                                  { 
                                    Fail26:
                                    { 
                                      IStrategoTerm o_0 = null;
                                      IStrategoTerm p_0 = null;
                                      o_0 = term.getSubterm(0);
                                      p_0 = term.getSubterm(1);
                                      term = p_0;
                                      if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consunion_2 != ((IStrategoAppl)term).getConstructor())
                                        break Fail26;
                                      term = termFactory.makeAppl(sdf_parenthesize._consunion_2, new IStrategoTerm[]{o_0, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{p_0})});
                                      if(true)
                                        break Success23;
                                    }
                                    term = term28;
                                  }
                                  Success24:
                                  { 
                                    if(cons0 == sdf_parenthesize._consconc_2)
                                    { 
                                      Fail27:
                                      { 
                                        IStrategoTerm l_0 = null;
                                        IStrategoTerm m_0 = null;
                                        m_0 = term.getSubterm(0);
                                        l_0 = term.getSubterm(1);
                                        term = m_0;
                                        if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consconc_2 != ((IStrategoAppl)term).getConstructor())
                                          break Fail27;
                                        term = termFactory.makeAppl(sdf_parenthesize._consconc_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{m_0}), l_0});
                                        if(true)
                                          break Success24;
                                      }
                                      term = term28;
                                    }
                                    Success25:
                                    { 
                                      if(cons0 == sdf_parenthesize._conswith_arguments_2)
                                      { 
                                        Fail28:
                                        { 
                                          IStrategoTerm i_0 = null;
                                          IStrategoTerm j_0 = null;
                                          j_0 = term.getSubterm(0);
                                          i_0 = term.getSubterm(1);
                                          term = j_0;
                                          if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conswith_arguments_2 != ((IStrategoAppl)term).getConstructor())
                                            break Fail28;
                                          term = termFactory.makeAppl(sdf_parenthesize._conswith_arguments_2, new IStrategoTerm[]{termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{j_0}), i_0});
                                          if(true)
                                            break Success25;
                                        }
                                        term = term28;
                                      }
                                      Success26:
                                      { 
                                        if(cons0 == sdf_parenthesize._conswith_arguments_2)
                                        { 
                                          Fail29:
                                          { 
                                            IStrategoTerm f_0 = null;
                                            IStrategoTerm g_0 = null;
                                            f_0 = term.getSubterm(0);
                                            g_0 = term.getSubterm(1);
                                            term = g_0;
                                            if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._conswith_arguments_2 != ((IStrategoAppl)term).getConstructor())
                                              break Fail29;
                                            term = termFactory.makeAppl(sdf_parenthesize._conswith_arguments_2, new IStrategoTerm[]{f_0, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{g_0})});
                                            if(true)
                                              break Success26;
                                          }
                                          term = term28;
                                        }
                                        if(cons0 == sdf_parenthesize._consconc_grammars_2)
                                        { 
                                          IStrategoTerm c_0 = null;
                                          IStrategoTerm d_0 = null;
                                          c_0 = term.getSubterm(0);
                                          d_0 = term.getSubterm(1);
                                          term = d_0;
                                          if(term.getTermType() != IStrategoTerm.APPL || sdf_parenthesize._consconc_grammars_2 != ((IStrategoAppl)term).getConstructor())
                                            break Fail2;
                                          term = termFactory.makeAppl(sdf_parenthesize._consconc_grammars_2, new IStrategoTerm[]{c_0, termFactory.makeAppl(sdf_parenthesize._consParenthetical_1, new IStrategoTerm[]{d_0})});
                                        }
                                        else
                                        { 
                                          break Fail2;
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
        return term;
    }
    context.push("Sdf2Parenthesize_0_0");
    context.popOnFailure();
    return null;
  }
}