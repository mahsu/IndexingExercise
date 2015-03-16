import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;



public class IndexTester {

	@Test
	public void basicInsertionSearch() {
		Index i = new Index();
		i.add("a", 123);
		i.add("aa", 3);
		i.add("ab",-1);
		i.add("aaa", 5);
		i.add("ba", 500);
		assertEquals("[a:123,aaa:5,aa:3,ab:-1]",i.search("a"));
		assertEquals("[aaa:5,aa:3]",i.search("aa"));
		assertEquals("[aaa:5]",i.search("aaa"));
		assertEquals("[]", i.search("z"));
		assertEquals("[]", i.search(""));
		assertEquals("[]", i.search(null));
	}
	
	@Test
	public void overflowRankingTest() {
		Index i = new Index();
		i.add("a", 123);
		i.add("aa", 3);
		i.add("ab",-1);
		i.add("aa", 5);
		i.add("a",  1000);
		i.add("aa", 1001);
		i.add("ab", 1004);
		i.add("aa", 1003);
		i.add("ab", 1002);
		i.add("aa", 1005);
		assertEquals("[aa:1005,ab:1004,aa:1003,ab:1002,aa:1001,a:1000,a:123,aa:5,aa:3,ab:-1]", i.search("a"));
		i.add("ab", 500);
		i.add("aa", 100);
		i.add("xy",550);
		assertEquals("[aa:1005,ab:1004,aa:1003,ab:1002,aa:1001,a:1000,ab:500,a:123,aa:100,aa:5]",i.search("a"));
	}
	
	@Test
	public void basicUnderscoreTest() {
		Index i = new Index();
		i.add("a",0);
		i.add("_a", 1);
		i.add("_aa", 2);
		i.add("__ab",3);
		i.add("__aaa", 4);
		i.add("___aab",5);
		i.add("___ba", 6);
		i.add("___bb", 7);
		assertEquals("[___aab:5,__aaa:4,__ab:3,_aa:2,_a:1,a:0]",i.search("a"));
		assertEquals("[___aab:5,__aaa:4,__ab:3,_aa:2,_a:1]",i.search("_a"));
		assertEquals("[___aab:5,__aaa:4,__ab:3]",i.search("__a"));
		assertEquals("[___aab:5]",i.search("___a"));
		assertEquals("[___aab:5,__aaa:4]",i.search("__aa"));
		System.out.println(i.search("a"));
	}
	
	@Test
	public void stringCreationTest() {
		assertEquals("[abc]", Arrays.toString(Index.generateStrings('_',"abc")));
		assertEquals("[a__, _]", Arrays.toString(Index.generateStrings('_',"a__")));
		assertEquals("[__abc___bcd_cde, __bcd_cde, cde]", Arrays.toString(Index.generateStrings('_',"__abc___bcd_cde")));
		System.out.println(Arrays.toString(Index.generateStrings('_',"rev_rev__rev_year")));
	}
	
	@Test
	public void advUnderscoreTest() {
		Index i = new Index();
		i.add("revenue", 1);
		i.add("yearly_revenue", 10);
		assertEquals("[yearly_revenue:10,revenue:1]",i.search("rev"));
		assertEquals("[]", i.search("_rev"));
		
		i.add("_revv", 20);
		i.add("rev_rev__rev_year",30);
		assertEquals("[rev_rev__rev_year:30,_revv:20,yearly_revenue:10,revenue:1]",i.search("rev"));
		assertEquals("[rev_rev__rev_year:30,yearly_revenue:10]",i.search("year"));
		
	}

}
