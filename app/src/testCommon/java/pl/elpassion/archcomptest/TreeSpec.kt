package pl.elpassion.archcomptest.common

import io.kotlintest.KTestJUnitRunner
import io.kotlintest.Spec
import io.kotlintest.TestSuite
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
abstract class TreeSpec : Spec() {

    fun nest(name: String, build: TreeTestSuiteBuilder.() -> Unit) {
        val suite = TreeTestSuiteBuilder(TestSuite(sanitizeSpecName(name)), testCaseConfig = defaultTestCaseConfig)
        suite.build()
        rootTestSuite.addNestedSuite(suite.testSuite)
    }
}

fun sanitizeSpecName(name: String) = name.replace("(", " ").replace(")", " ")