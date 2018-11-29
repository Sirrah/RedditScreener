package nl.sirrah.redditscreener

import nl.sirrah.redditscreener.api.Listing
import nl.sirrah.redditscreener.api.RedditApi
import nl.sirrah.redditscreener.common.mock
import nl.sirrah.redditscreener.common.mockWhen
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import rx.Observable.just
import rx.observers.TestSubscriber

class RedditApiTest {

    private var redditApi = mock<RedditApi>()

    val emptyListing = Listing(children = emptyList(), after = "mock", before = null)

    val subscriber = TestSubscriber<Listing>()

    @Before
    fun setup() {
        mockWhen(redditApi.listing(anyString(), anyString(), anyInt())).thenReturn(
            just(emptyListing)
        )
    }

    @Test
    fun listing() {
        redditApi.listing("awww")
            .subscribe(subscriber)

        with(subscriber) {
            assertNoErrors()
            assertCompleted()
            assertValueCount(1)
        }
    }
}