package pl.elpassion.archcomptest.common

import io.kotlintest.TestCase
import io.kotlintest.TestCaseConfig
import io.kotlintest.TestSuite

data class TreeTestSuiteBuilder(val testSuite: TestSuite,
                                val parent: TreeTestSuiteBuilder? = null,
                                val testCaseConfig: TestCaseConfig) {

    private var before: (() -> Unit) = {}

    fun before(block: () -> Unit) {
        before = block
    }

    fun test(name: String, apply: TreeTestSuiteBuilder.() -> Unit) {
        val nested = TreeTestSuiteBuilder(TestSuite(sanitizeSpecName(name)), this, testCaseConfig)
        testSuite.addNestedSuite(nested.testSuite)
        nested.apply()
    }

    fun assert(name: String, test: () -> Unit) {
        val tc = TestCase(
                suite = testSuite,
                name = sanitizeSpecName(name),
                test = {
                    executeUpstreamBefore()
                    test()
                },
                config = testCaseConfig)
        testSuite.addTestCase(tc)
    }

    private fun executeUpstreamBefore() {
        parent?.executeUpstreamBefore()
        before.invoke()
    }
}