package pl.elpassion.archcomptest.common

import io.reactivex.observers.TestObserver
import org.junit.Assert

fun <T> TestObserver<T>.assertLastValue(value: T) {
    Assert.assertEquals(value, values().last())
}