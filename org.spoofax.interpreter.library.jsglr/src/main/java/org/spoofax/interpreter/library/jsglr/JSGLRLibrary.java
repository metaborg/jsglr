/*
 * Copyright (c) 2005-2011, Karl Trygve Kalleberg <karltk near strategoxt dot org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.interpreter.library.jsglr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.spoofax.interpreter.core.Interpreter;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.terms.IStrategoInt;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.Disambiguator;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.io.ParseTableManager;

public class JSGLRLibrary extends AbstractStrategoOperatorRegistry {

	public static final String REGISTRY_NAME = "JSGLR";

	private ParseTableManager parseTableManager;

	private final Map<Integer, ParseTable> parseTables = new HashMap<Integer, ParseTable>();

    private final Map<IStrategoTerm, IStrategoInt> parseTableCache =
            new WeakHashMap<IStrategoTerm, IStrategoInt>();

	private final Disambiguator filterSettings = new Disambiguator();

	private final AtomicBoolean recoveryEnabled = new AtomicBoolean(false); // silly placeholder

	public Disambiguator getFilterSettings() {
		return filterSettings;
	}

	public AtomicBoolean getRecoveryEnabledSetting() {
		return recoveryEnabled;
	}

	public JSGLRLibrary() {
        add(new STRSGLR_open_parse_table());
        add(new STRSGLR_parse_string_pt(filterSettings, recoveryEnabled));
        add(new STRSGLR_parse_stream_pt());
        add(new STRSGLR_recover_parse_string());
		add(new STRSGLR_get_parse_error());
		add(new STRSGLR_clear_parse_error());
		add(new STRSGLR_anno_location());
		add(new STRSGLR_close_parse_table());
		initFilterSettings();
	}

	private void initFilterSettings() {
		filterSettings.setFilterAny(true);

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_direct_eagernes_on") { // (sic)
			@Override
			public void set() {
				filterSettings.setFilterDirectPreference(true);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_direct_eagernes_off") { // (sic)
			@Override
			public void set() {
				filterSettings.setFilterDirectPreference(false);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_get_filter_direct_eagernes") { // (sic)
			@Override
			public boolean get() {
				return filterSettings.getFilterDirectPreference();
			}
		});

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_eagernes_on") { // (sic)
			@Override
			public void set() {
				filterSettings.setFilterPreferenceCount(true);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_eagernes_off") { // (sic)
			@Override
			public void set() {
				filterSettings.setFilterPreferenceCount(false);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_get_filter_eagernes") { // (sic)
			@Override
			public boolean get() {
				return filterSettings.getFilterPreferenceCount();
			}
		});

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_injection_count_on") {
			@Override
			public void set() {
				filterSettings.setFilterInjectionCount(true);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_injection_count_off") {
			@Override
			public void set() {
				filterSettings.setFilterInjectionCount(false);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_get_filter_injection_count") {
			@Override
			public boolean get() {
				return filterSettings.getFilterInjectionCount();
			}
		});

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_priority_on") {
			@Override
			public void set() {
				filterSettings.setFilterPriorities(true);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_priority_off") {
			@Override
			public void set() {
				filterSettings.setFilterPriorities(false);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_get_filter_priority") {
			@Override
			public boolean get() {
				return filterSettings.getFilterPriorities();
			}
		});

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_reject_on") {
			@Override
			public void set() {
				filterSettings.setFilterReject(true);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filter_reject_off") {
			@Override
			public void set() {
				filterSettings.setFilterReject(false);
			}
		});
		add(new AbstractFilterSetting(filterSettings, "STRSGLR_get_filter_reject") {
			@Override
			public boolean get() {
				return filterSettings.getFilterReject();
			}
		});

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_default_filters") {
			@Override
			public void set() {
				filterSettings.setDefaultFilters();
			}
		});

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_recovery_on") {
			@Override
			public void set() {
				recoveryEnabled.set(true);
			}
		});

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_recovery_off") {
			@Override
			public void set() {
				recoveryEnabled.set(false);
			}
		});

		add(new AbstractFilterSetting(filterSettings, "STRSGLR_get_recovery") {
			@Override
			public boolean get() {
				return recoveryEnabled.get();
			}
		});

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_default_config") {
            @Override
            public void set() {
                filterSettings.setDefaultFilters();
            }
        });

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_ambiguity_error_on") {
            @Override
            public void set() {
                filterSettings.setAmbiguityIsError(true);
            }
        });

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_ambiguity_error_off") {
            @Override
            public void set() {
                filterSettings.setAmbiguityIsError(false);
            }
        });

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_get_ambiguity_error") {
            @Override
            public boolean get() {
                return filterSettings.getAmbiguityIsError();
            }
        });

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filtering_on") {
            @Override
            public void set() {
                filterSettings.setFilterAny(true);
            }
        });

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_filtering_off") {
            @Override
            public void set() {
                filterSettings.setFilterAny(false);
            }
        });

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_log_statistics_on") {
            @Override
            public void set() {
            	filterSettings.setLogStatistics(true);
            }
        });

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_set_log_statistics_off") {
            @Override
            public void set() {
            	filterSettings.setLogStatistics(false);
            }
        });

        add(new AbstractFilterSetting(filterSettings, "STRSGLR_get_log_statistics") {
            @Override
            public boolean get() {
                return filterSettings.getLogStatistics();
            }
        });

	}


	public String getOperatorRegistryName() {
		return REGISTRY_NAME;
	}

	ParseTableManager getParseTableManager(ITermFactory factory) {
		if(parseTableManager == null)
			parseTableManager = new ParseTableManager(factory);
		return parseTableManager;
	}

	int addParseTable(ParseTable pt) {
		int idx = parseTables.size();
		parseTables.put(idx, pt);
		return idx;
	}

	ParseTable getParseTable(int idx) {
		return parseTables.get(idx);
	}

	Map<IStrategoTerm, IStrategoInt> getParseTableCache() {
		return parseTableCache;
	}

	public static void attach(Interpreter intp) throws IOException, InterpreterException {
		attach(intp, new JSGLRLibrary(), "share/libstratego-jsglr.ctree");
	}

	public void removeParseTable(int index) {
		parseTables.remove(index);
	}
}
