package com.logx.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.logx.trace.IPValidateUtils;

public class IPValidateTest {

  private IPValidateUtils ipValidate;

  @Before
  public void before() {
    List<String> list = new ArrayList<String>();
    list.add("*.1.0.1");
    list.add("12.*.*.1");
    list.add("190.1.2.*");
    list.add("100.*.*.*");
    list.add("110.120.119.114");
    ipValidate = IPValidateUtils.getInstance(list);
  }

  @Test
  public void test_matches() {
    Assert.assertTrue(ipValidate.validateIPV4("112123.1.0.1"));
    Assert.assertTrue(ipValidate.validateIPV4("12.111.1222.1"));
    Assert.assertTrue(ipValidate.validateIPV4("190.1.2.112312"));
    Assert.assertTrue(ipValidate.validateIPV4("100.13.0123.11231"));
    Assert.assertTrue(ipValidate.validateIPV4("13.1.0.1"));
    Assert.assertTrue(ipValidate.validateIPV4("110.120.119.114"));
  }

  @Test
  public void test_matchesFalse() {
    Assert.assertFalse(ipValidate.validateIPV4("112123.2.0.1"));
    Assert.assertFalse(ipValidate.validateIPV4("12.111.1222.2"));
    Assert.assertFalse(ipValidate.validateIPV4("190.1.3.1"));
    Assert.assertFalse(ipValidate.validateIPV4("110.13.123.131"));
    Assert.assertFalse(ipValidate.validateIPV4("13.1.2.1"));
    Assert.assertFalse(ipValidate.validateIPV4("188.1.1.1"));
    Assert.assertFalse(ipValidate.validateIPV4("110.120.119.123"));
  }

  @Test
  public void test_ipv4() {
    Assert.assertTrue(ipValidate.isIPV4("1.12.123.123"));;
    Assert.assertTrue(ipValidate.isIPV4("xxx.xx.xxx.xxx"));;
    Assert.assertFalse(ipValidate.isIPV4("xxx.xx.xxx.xxx.xxx"));;
  }
}
