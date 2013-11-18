package org.spoofax.jsglr.tests.unicode;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.spoofax.jsglr.unicode.charranges.UnicodeInterval;
import org.spoofax.jsglr.unicode.charranges.UnicodeRange;
import org.spoofax.jsglr.unicode.terms.DefaultSequenceCreator;

public class UnicodeRangeTest {

	private static UnicodeRange createRange(int start, int end) {
		return new UnicodeRange(new UnicodeInterval(start, end));
	}

	@Test
	public void testUnite() {
		UnicodeRange u11 = createRange(0, 5);
		UnicodeRange u12 = createRange(15, 20);
		UnicodeRange u13 = createRange(23, 28);
		UnicodeRange u14 = createRange(29, 33);
		UnicodeRange u1 = new UnicodeRange();
		u1.unite(u11);
		u1.unite(u12);
		u1.unite(u13);
		u1.unite(u14);

		UnicodeRange exp1 = createRange(23, 33);
		exp1.unite(u12);
		exp1.unite(u11);
		assertEquals(exp1, u1);

		UnicodeRange u21 = createRange(1, 4);
		UnicodeRange u22 = createRange(13, 16);
		UnicodeRange u23 = createRange(24, 27);
		UnicodeRange u2 = new UnicodeRange();
		u2.unite(u21);
		u2.unite(u22);
		u2.unite(u23);
		u2.unite(u14);

		UnicodeRange u31 = createRange(25, 28);
		UnicodeRange u32 = createRange(28, 29);
		UnicodeRange u3 = new UnicodeRange();
		u3.unite(u31);
		u3.unite(u32);

		assertEquals(createRange(25, 29), u3);

		UnicodeRange u4 = createRange(3, 8);

		UnicodeRange u5 = createRange(12, 17);

		UnicodeRange u6 = createRange(34, 34);

		UnicodeRange u = new UnicodeRange();
		u.unite(u1);
		assertEquals(u1, u);
		u.unite(u2);

		u.unite(u3);
		u.unite(u4);
		u.unite(u5);
		u.unite(u6);

		UnicodeRange expected = new UnicodeRange();
		expected.unite(createRange(0, 8));
		expected.unite(createRange(12, 20));
		expected.unite(createRange(23, 34));

		assertEquals(expected, u);
		
		UnicodeRange u7 = createRange(14,16);
		u = new UnicodeRange();
		u.unite(u7);
		u.unite(u5);
		assertEquals(u5,u);
	}

	@Test
	public void testIntersect() {
		UnicodeRange u1 = createRange(0, 20);
		u1.unite(createRange(22, 30));
		UnicodeRange u2 = createRange(6, 25);
		UnicodeRange u3 = createRange(4, 20);
		UnicodeRange u4 = createRange(8, 15);
		u1.unite(createRange(16, 19));
		UnicodeRange u5 = createRange(10, 13);
		UnicodeRange u6 = createRange(11, 13);
		UnicodeRange u7 = createRange(11, 12);
		UnicodeRange u8 = createRange(12, 14);
		UnicodeRange u9 = createRange(12, 12);
		UnicodeRange u10 = createRange(11, 11);

		u1.intersect(u2);
		UnicodeRange exp1 = createRange(6, 20);
		exp1.unite(createRange(22, 25));
		assertEquals(exp1, u1);
		u1.intersect(u3);
		assertEquals(createRange(6, 20), u1);
		u1.intersect(u4);
		assertEquals(u4, u1);
		u1.intersect(u5);
		assertEquals(createRange(10, 13), u1);
		u1.intersect(u6);
		assertEquals(createRange(11, 13), u1);
		u1.intersect(u7);
		assertEquals(createRange(11, 12), u1);
		u1.intersect(u8);
		assertEquals(createRange(12, 12), u1);
		u1.intersect(u9);
		assertEquals(createRange(12, 12), u1);
		u1.intersect(u10);
		assertEquals(new UnicodeRange(), u1);
	}

	@Test
	public void testDiff() {
		UnicodeRange u1 = createRange(3, 14);
		u1.unite(createRange(17, 25));
		u1.unite(createRange(28, 34));

		UnicodeRange u2 = createRange(11, 18);
		u2.unite(createRange(22, 23));
		u2.unite(createRange(30, 32));

		UnicodeRange u3 = createRange(5, 5);
		u3.unite(createRange(9, 19));
		u3.unite(createRange(27, 30));

		UnicodeRange u4 = createRange(3, 6);
		u4.unite(createRange(22, 35));

		UnicodeRange u5 = createRange(7, 23);

		UnicodeRange exp1 = createRange(3, 10);
		exp1.unite(createRange(19, 21));
		exp1.unite(createRange(24, 25));
		exp1.unite(createRange(28, 29));
		exp1.unite(createRange(33, 34));

		UnicodeRange exp2 = createRange(3, 4);
		exp2.unite(createRange(6, 8));
		exp2.unite(createRange(20, 21));
		exp2.unite(createRange(24, 25));
		exp2.unite(createRange(33, 34));

		UnicodeRange exp3 = createRange(7, 8);
		exp3.unite(createRange(20, 21));

		UnicodeRange exp4 = new UnicodeRange();

		u1.diff(u2);
		assertEquals(u1, exp1);
		u1.diff(u3);
		assertEquals(u1, exp2);
		u1.diff(u4);
		assertEquals(u1, exp3);
		u1.diff(u5);
		assertEquals(u1, exp4);

	}

	@Test
	public void testInvert() {
		UnicodeRange r = createRange(25, 27);
		r.unite(createRange(40, 54));
		UnicodeRange r2 = r.clone();

		r.invert(new UnicodeInterval(0, 60));

		UnicodeRange result = createRange(0, 24);
		result.unite(createRange(28, 39));
		result.unite(createRange(55, 60));

		assertEquals(result, r);

		r.invert(new UnicodeInterval(0, 60));

		assertEquals(r2, r);
	}

	@Test
	public void test1() {
		UnicodeRange r = createRange(0, 255);
		r.diff(createRange(10, 10));

		UnicodeRange r2 = createRange(10, 10);
		r2.invert(new UnicodeInterval(0, 255));

		UnicodeRange result = createRange(0, 9);
		result.unite(createRange(11, 255));

		assertEquals(result, r);
		assertEquals(result, r2);
	}
	
	private String makeCharRangeString(int from, int to) {
		if (from == to) {
			return "char-class(simple-charclass(present(numeric(\"\\\\"+from+"\"))))";
		}
		return "char-class(simple-charclass(present(range(numeric(\"\\\\"+from+"\"),numeric(\"\\\\"+to+"\")))))";
	}

	@Test
	public void testAsciiToAST() {
		UnicodeRange r = createRange(0,127);
		r.diff(createRange(20,30));
		r.diff(createRange(45,45));
		String astString = r.toAST(new DefaultSequenceCreator()).toString();
		String expect = "alt(" +
							makeCharRangeString(0, 19) + "," +
							"alt(" +
								makeCharRangeString(31, 44) + "," +
								makeCharRangeString(46, 127) + "))";
		assertEquals(expect, astString);
	}
	
	@Test
	public void testUTF8_2ByteAST() {
		UnicodeRange r = createRange(128, 1400);
	
		String astString = r.toAST(new DefaultSequenceCreator()).toString();
		String expect = 
						"alt(" +
							"seq(" +
								makeCharRangeString(0, 0) + "," +
								"[" + makeCharRangeString(128, 255) +"])," +
							"alt(" +
								"seq(" +
									makeCharRangeString(1, 4) + "," +
									"[" + makeCharRangeString(0, 255) +"])," +
								"seq(" +
									makeCharRangeString(5, 5) + "," +
									"[" + makeCharRangeString(0, 120) + "])))";
		assertEquals(expect, astString);
	}
	
	private static List<UnicodeInterval> createIntervalList(long... values) {
		List<UnicodeInterval> l = new ArrayList<UnicodeInterval>(values.length/2);
		for (int i = 0; i < values.length; i=i+2) {
			l.add(new UnicodeInterval(values[i], values[i+1]));
		}
		return l;
	}
	
	@Test
	public void testNormalizeUTF8Charclasses() {
		UnicodeInterval interval;
		List<UnicodeInterval> expected;
		
		interval = new UnicodeInterval(0x23000000, 0x23FFFFFF);
		expected = createIntervalList(0x23000000, 0x23FFFFFF);
		assertEquals(expected, interval.normalize());
		
		interval = new UnicodeInterval(0x1234, 0x12DE);
		expected = createIntervalList(0x1234,0x12DE);
		assertEquals(expected, interval.normalize());
		
		interval = new UnicodeInterval(0x123221, 0x452132);
		expected = createIntervalList(0x123221,0x1232ff,0x123300,0x12ffff,0x130000,0x44ffff,0x450000,0x4520ff,0x452100,0x452132);
		assertEquals(expected, interval.normalize());
		
		interval = new UnicodeInterval(0x3476, 0x35D0);
		expected = createIntervalList(0x3476, 0x34ff,0x3500, 0x35D0);
		assertEquals(expected, interval.normalize());
		
		interval = new UnicodeInterval(0x1111, 0x1111);
		expected = createIntervalList(0x1111,0x1111);
		assertEquals(expected, interval.normalize());
		
	}
}
