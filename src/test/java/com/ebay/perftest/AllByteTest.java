package com.ebay.perftest;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(ContiPerfSuiteRunner.class)
@SuiteClasses(TestByte.class)
@PerfTest(invocations = 1000000, threads = 40)
public class AllByteTest {


}