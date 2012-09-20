package org.robby;

import junit.framework.TestCase;

public class TestHbaseIf extends TestCase {
	HbaseIf hbase;

	public void setUp() throws Exception {
		hbase = new HbaseIf();
		
		hbase.createTables();
	}

	public void test1() throws Exception{
		//create user
		assertEquals(true, hbase.createNewUser("user1", "pwd1"));
		assertEquals(true, hbase.createNewUser("user2", "pwd2"));
		assertEquals(true, hbase.createNewUser("user3", "pwd3"));
		assertEquals(false, hbase.createNewUser("user1", "pwd1"));
		
		//check password
		assertEquals(1, hbase.checkPassword("user1", "pwd1"));
		assertEquals(2, hbase.checkPassword("user2", "pwd2"));
		assertEquals(0, hbase.checkPassword("user2", "pwd1221"));
		
		//get id by name
		assertEquals(0, hbase.getIdByUsername("test"));
		assertEquals(1, hbase.getIdByUsername("user1"));
		assertEquals(2, hbase.getIdByUsername("user2"));
		
		//get name by id
		assertEquals("user1", hbase.getNameById(1));
		assertEquals("", hbase.getNameById(100));
		
		//delete user
		assertEquals(false, hbase.deleteUser(100));
		assertEquals(true, hbase.deleteUser(3));
	}
	
}
