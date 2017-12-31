package nl.sirrah.redditscreener.common

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

/**
 * Make it easier to mock Kotlin classes
 */
inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

/**
 * Alternative to Mockito.'when'.
 *
 * Because 'when' is a keyword in Kotlin it has to be put between quotes.
 */
fun <T> mockWhen(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)