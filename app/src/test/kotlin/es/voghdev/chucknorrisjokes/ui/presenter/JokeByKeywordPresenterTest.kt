package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
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
            value = "Chuck Norris knows how to say souffle in the French language."
    )

    val someJokes = listOf(
            aJoke,
            anotherJoke
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun `should show an error if keyword is empty`() {
        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("")
        }

        verify(mockView).showKeywordError("Please enter a keyword")
    }

    @Test
    fun `should request jokes by keyword to the API when a valid keyword is entered`() {
        givenThereAreNoJokesInTheAPI()

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("Bruce")
        }

        verify(mockChuckNorrisRepository).getRandomJokeByKeyword("bruce")
    }

    @Test
    fun `should show an empty case when API returns no results`() {
        givenThereAreNoJokesInTheAPI()

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("Jack")
        }

        verify(mockView).showEmptyCase()
    }

    @Test
    fun `should show the text for the first joke if API returns results`() {
        givenThereAreSomeJokes()

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("Bruce")
        }

        verify(mockView).showJokeText("We have our fears, fear has its Chuck Norris'es")
    }

    @Test
    fun `should show the image of the first joke if API returns results`() {
        givenThereAreSomeJokes()

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked("Bruce")
        }

        verify(mockView).showJokeImage("http://chuck.image.url")
    }

    private fun givenThereAreSomeJokes() {
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(Pair(someJokes, null))
    }

    private fun givenThereAreNoJokesInTheAPI() {
        whenever(mockChuckNorrisRepository.getRandomJokeByKeyword(anyString())).thenReturn(Pair(emptyList<Joke>(), null))
    }

    private fun createMockedPresenter(): JokeByKeywordPresenter {
        val presenter = JokeByKeywordPresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
