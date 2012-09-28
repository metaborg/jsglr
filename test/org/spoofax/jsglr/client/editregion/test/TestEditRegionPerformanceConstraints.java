package org.spoofax.jsglr.client.editregion.test;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class TestEditRegionPerformanceConstraints {

	@Test @Ignore
	public void testPerformanceLargeEditRegion() {
		fail("Not yet implemented");
		//TODO: what if editing is done at the start AND at the end of a (large) file?
		//Best would be to match lines before (in stead of) matching characters
		//Alternatively, the application of the algorithm should be cancelled.
	}

	@Test @Ignore
	public void testPerformanceMultipleEdits() {
		fail("Not yet implemented");
		//TODO: what if the correct file and the erroneous file are completely different?
		//The application of the algorithm should be cancelled.		
	}
}
