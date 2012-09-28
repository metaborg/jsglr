package org.spoofax.jsglr.client;

public class IntegratedRecoverySettings {
	private boolean useFineGrained;
	private boolean useRegionSelection;
	private boolean useRegionRecovery;
	private boolean useCursorLocation;
	private int maxNumberOfRecoverApplicationsGlobal; //branches with more then x recoveries are cut off (IS USED FOR ANALYSIS)
	
	public boolean useFineGrained() {
		return useFineGrained;
	}
	public void setUseFineGrained(boolean useFineGrained) {
		this.useFineGrained = useFineGrained;
	}
	public boolean useRegionSelection() {
		return useRegionSelection;
	}
	public void setUseRegionSelection(boolean useRegionSelection) {
		this.useRegionSelection = useRegionSelection;
	}
	public boolean useCursorLocation() {
		return useCursorLocation;
	}
	public void setUseCursorLocation(boolean useCursorLocation) {
		this.useCursorLocation = useCursorLocation;
	}
	public boolean useRegionRecovery() {
		return useRegionRecovery;
	}
	public void setUseRegionRecovery(boolean useRegionRecovery) {
		this.useRegionRecovery = useRegionRecovery;
	}
	
	public int getMaxNumberOfRecoverApplicationsGlobal() {
		return maxNumberOfRecoverApplicationsGlobal;
	}

	public void setMaxNumberOfRecoverApplicationsGlobal(
			int maxNumberOfRecoverApplicationsGlobal) {
		this.maxNumberOfRecoverApplicationsGlobal = maxNumberOfRecoverApplicationsGlobal;
	}

	
	private IntegratedRecoverySettings(){
		useFineGrained = true;
		useRegionSelection = true;
		useRegionRecovery = true;
		useCursorLocation = true;
		this.setMaxNumberOfRecoverApplicationsGlobal(Integer.MAX_VALUE);		
	}
	
	public static IntegratedRecoverySettings createDefaultSettings(){
		return new IntegratedRecoverySettings();
	}
}
