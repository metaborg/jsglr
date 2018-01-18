package org.spoofax.jsglr2.datadependent;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class DataDependentSymbolNode extends BasicParseForest {

	public final IProduction production;
	private final List<DataDependentRuleNode> derivations;
	
	public DataDependentSymbolNode(int nodeNumber, Parse parse, Position startPosition, Position endPosition, IProduction production) {
		super(nodeNumber, parse, startPosition, endPosition);
		this.production = production;
		this.derivations = new ArrayList<DataDependentRuleNode>();
	}
	
	public void addDerivation(DataDependentRuleNode derivation) {
		this.derivations.add(derivation);
	}
	
	public List<DataDependentRuleNode> getDerivations() {
		return derivations;
	}
    
    public List<DataDependentRuleNode> getPreferredAvoidedDerivations() {
        if (derivations.size() <= 1)
            return derivations;
        else {
            List<DataDependentRuleNode> preferred = null, avoided = null, other = null;
            
            for (DataDependentRuleNode derivation : derivations) {
                switch (derivation.productionType) {
                    case PREFER:
                        if (preferred == null)
                            preferred = new ArrayList<DataDependentRuleNode>();
                        
                        preferred.add(derivation);
                        break;
                    case AVOID:
                        if (avoided == null)
                            avoided = new ArrayList<DataDependentRuleNode>();
                        
                        avoided.add(derivation);
                        break;
                    default:
                        if (other == null)
                            other = new ArrayList<DataDependentRuleNode>();
                        
                        other.add(derivation);
                }
            }
            
            if (preferred != null && !preferred.isEmpty())
                return preferred;
            else if (other != null && !other.isEmpty())
                return other;
            else
                return avoided;
        }
    }
	
	public DataDependentRuleNode getOnlyDerivation() {
		return derivations.get(0);
	}
	
	public boolean isAmbiguous() {
		return derivations.size() > 1;
	}
	
	public String descriptor() {
		return production.descriptor();
	}
	
}
