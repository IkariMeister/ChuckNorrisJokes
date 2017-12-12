package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

class JokeByKeywordPresenterTest() {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: JokeByKeywordPresenter.Navigator

    @Mock lateinit var mockView: JokeByKeywordPresenter.MVPView

    @Mock lateinit var mockChuckNorrisRepository: ChuckNorrisRepository

    lateinit var presenter: JokeByKeywordPresenter

    val aJoke = Joke(id = "abc",
            iconUrl = "http://chuck.image.url",
            url = "http://example.url",
            value = "We have our fears, fear has its Chuck Norris'es")

    val anotherJoke = Joke(
            id = "GdEH64AkS9qEQCmqMwM2Rg",
            iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
            url = "http://api.chucknorris.io/jokes/GdEH64AkS9qEQCmqMwM2Rg",
            value = "Chuck Norris tells Olmo how to cook croquettes"
    )

    val someJokes = listOf(
            aJoke,
            anotherJoke
    )

    val captor = argumentCaptor<Joke>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun `should not accept an empty text as search keyword`() {
        runBlocking {
            presenter.onSearchButtonClicked("")
        }

        verify(mockView).showError("Please enter a keyword")
    }

    @Test
    fun `should request jokes by keyword to the API when "search" button is clicked with a valid keyword`() {
        givenTheApiReturns(someJokes)

        runBlocking {
            presenter.onSearchButtonClicked("Bruce Lee")
        }

        verify(mockChuckNorrisRepository).getRandomJokeByKeyword("bruce lee")
    }

    @Test
    fun `should add two jokes with expected texts and pictures when the API returns two jokes`() {
        givenTheApiReturns(someJokes)

        runBlocking {
            presenter.onSearchButtonClicked("Bruce Lee")
        }

        verify(mockView, times(2)).addJoke(captor.capture())

        assertEquals("We have our fears, fear has its Chuck Norris'es", captor.firstValue.value)
        assertEquals("http://chuck.image.url", captor.firstValue.iconUrl)

        assertEquals("Chuck Norris tells Olmo how to cook croquettes", captor.secondValue.value)
        assertEquals("https://assets.chucknorris.host/img/avatar/chuck-norris.png", captor.secondValue.iconUrl)
    }

    @Test
    fun `should show an empty case if the API returns zero results`() {
        givenTheApiReturns(emptyList())

        runBlocking {
            presenter.onSearchButtonClicked("Bruce Lee")
        }

        verify(mockView).showEmptyCase()
    }

    private fun givenTheApiReturns(someJokes: List<Joke>) {
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(Pair(someJokes, null))
    }

    private fun createMockedPresenter(): JokeByKeywordPresenter {
        val presenter = JokeByKeywordPresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
