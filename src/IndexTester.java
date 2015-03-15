import static org.junit.Assert.*;

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
		System.out.println(i.search("z"));
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

}
