import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class IndexTester {

	@Test
	public void basicInsertionSearch() {
		Index i = new Index();
		i.add("a", 123);
		i.add("aa", 3);
		i.add("ab", -1);
		i.add("aaa", 5);
		i.add("ba", 500);
		assertEquals("[a:123,aaa:5,aa:3,ab:-1]", i.searchToString("a"));
		assertEquals("[aaa:5,aa:3]", i.searchToString("aa"));
		assertEquals("[aaa:5]", i.searchToString("aaa"));
		assertEquals("[]", i.searchToString("z"));
		assertEquals("[]", i.searchToString(""));
		assertEquals("[]", i.searchToString(null));

		Index j = new Index();
		j.add("123456789", 123);
		j.add("$abc123", 100);
		assertEquals("[123456789:123]", j.searchToString("1"));
		assertEquals("[$abc123:100]", j.searchToString("$"));
	}

	@Test
	public void overflowRankingTest() {
		Index i = new Index();
		i.add("a", 123);
		i.add("aa", 3);
		i.add("ab", -1);
		i.add("aa", 5);
		i.add("a", 1000);
		i.add("aa", 1001);
		i.add("ab", 1004);
		i.add("aa", 1003);
		i.add("ab", 1002);
		i.add("aa", 1005);
		assertEquals("[aa:1005,ab:1004,aa:1003,ab:1002,aa:1001,a:1000,a:123,aa:5,aa:3,ab:-1]", i.searchToString("a"));
		i.add("ab", 500);
		i.add("aa", 100);
		i.add("xy", 550);
		assertEquals("[aa:1005,ab:1004,aa:1003,ab:1002,aa:1001,a:1000,ab:500,a:123,aa:100,aa:5]", i.searchToString("a"));
	}

	@Test
	public void basicUnderscoreTest() {
		Index i = new Index();
		i.add("a", 0);
		i.add("_a", 1);
		i.add("_aa", 2);
		i.add("__ab", 3);
		i.add("__aaa", 4);
		i.add("___aab", 5);
		i.add("___ba", 6);
		i.add("___bb", 7);
		assertEquals("[___aab:5,__aaa:4,__ab:3,_aa:2,_a:1,a:0]", i.searchToString("a"));
		assertEquals("[___aab:5,__aaa:4,__ab:3,_aa:2,_a:1]", i.searchToString("_a"));
		assertEquals("[___aab:5,__aaa:4,__ab:3]", i.searchToString("__a"));
		assertEquals("[___aab:5]", i.searchToString("___a"));
		assertEquals("[___aab:5,__aaa:4]", i.searchToString("__aa"));
		System.out.println(i.searchToString("a"));
	}

	@Test
	public void stringCreationTest() {
		assertEquals("[abc]", Arrays.toString(Index.generateStrings('_', "abc")));
		assertEquals("[a__, _]", Arrays.toString(Index.generateStrings('_', "a__")));
		assertEquals("[__abc___bcd_cde, __bcd_cde, cde]",
				Arrays.toString(Index.generateStrings('_', "__abc___bcd_cde")));
		System.out.println(Arrays.toString(Index.generateStrings('_', "rev_rev__rev_year")));
	}

	@Test
	public void advUnderscoreTest() {
		Index i = new Index();
		i.add("revenue", 1);
		i.add("yearly_revenue", 10);
		assertEquals("[yearly_revenue:10,revenue:1]", i.searchToString("rev"));
		assertEquals("[]", i.searchToString("_rev"));

		// same number of underscores does not get duplicated
		i.add("r_revv", 20);
		i.add("rev_rev__rev_year", 30);
		assertEquals("[rev_rev__rev_year:30,r_revv:20,yearly_revenue:10,revenue:1]", i.searchToString("rev"));
		assertEquals("[rev_rev__rev_year:30,yearly_revenue:10]", i.searchToString("year"));

		// different number of underscores get added
		i.add("rev__rev____rev", -1);
		assertEquals("[rev__rev____rev:-1]", i.searchToString("___rev"));
		assertEquals("[rev_rev__rev_year:30,rev__rev____rev:-1]", i.searchToString("_rev"));

		// underscores by themselves are added.
		i.add("abc_def__abc__________", 1000);
		i.add("index____intensity____famous____", -140749);
		assertEquals("[abc_def__abc__________:1000]", i.searchToString("_________"));
	}

}
