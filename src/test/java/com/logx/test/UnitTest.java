package com.logx.test;

import org.junit.Test;

import com.logx.utils.Unit;

import junit.framework.Assert;

public class UnitTest {
	@Test
	public void test_parser() {
		Assert.assertEquals("10->BYTE", Unit.parser(" 10 B ").toString());
		Assert.assertEquals("100->BYTE", Unit.parser(" 100 ").toString());
		Assert.assertEquals("10->KB", Unit.parser(" 10	K ").toString());
		Assert.assertEquals("10->MB", Unit.parser(" 10 M ").toString());

		Assert.assertEquals("10->GB", Unit.parser(" 10 G ").toString());
		Assert.assertEquals("10->GB", Unit.parser("10 G ").toString());

		Assert.assertEquals("10->TB", Unit.parser(" 10 T ").toString());
		Assert.assertEquals("10->TB", Unit.parser("10 T ").toString());
	}

	@Test
	public void test_getBytes() {
		Assert.assertEquals(1073741824L, Unit.parser("1g").getBytes());
		Assert.assertEquals(1048576L, Unit.parser("1 m").getBytes());
		Assert.assertEquals(1024L, Unit.parser("1 k").getBytes());
		Assert.assertEquals(10L, Unit.parser("10 ").getBytes());
		Assert.assertEquals(3298534883328L, Unit.parser("3 t").getBytes());

		Assert.assertEquals(2L, Unit.BIT.getBytes(16));
		Assert.assertEquals(0L, Unit.BIT.getBytes(7));
	}

}
