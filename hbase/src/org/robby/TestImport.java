package org.robby;

import junit.framework.TestCase;

public class TestImport extends TestCase {
	ImportCdr im;

	public void setUp() throws Exception {
		im = new ImportCdr();
		im.createTable();
		
	}

	public void test1() throws Exception{
		im.multi();
	}
	
	public void test2() throws Exception{
		
		//im.single();
	}
}
