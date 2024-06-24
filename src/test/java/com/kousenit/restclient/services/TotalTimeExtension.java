package com.kousenit.restclient.services;

import org.junit.jupiter.api.extension.*;

public class TotalTimeExtension implements BeforeAllCallback, AfterAllCallback {
        // BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static long totalStartTime;

    @Override
    public void beforeAll(ExtensionContext context) {
        totalStartTime = System.nanoTime();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        long totalDuration = System.nanoTime() - totalStartTime;
        System.out.println("Total time for all tests: " + totalDuration / 1_000_000 + " ms");
    }

//    @Override
//    public void beforeTestExecution(ExtensionContext context) {
//        startTime = System.nanoTime();
//    }
//
//    @Override
//    public void afterTestExecution(ExtensionContext context) {
//        long duration = System.nanoTime() - startTime;
//        System.out.println(context.getDisplayName() + " took " + duration / 1_000_000 + " ms");
//    }
}
