package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.verify
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RandomJokePresenterTest {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: RandomJokePresenter.Navigator

    @Mock lateinit var mockView: RandomJokePresenter.MVPView

    @Mock lateinit var mockChuckNorrisRepository: ChuckNorrisRepository

    lateinit var presenter: RandomJokePresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun `should request a random joke on start`() {
        presenter.initialize()

        verify(mockChuckNorrisRepository).getRandomJoke()
    }

    @Test
    fun `should show the joke's text on screen when a random joke is received`() {
        presenter.initialize()

        verify(mockView).showJokeText(anyString())
    }

    private fun createMockedPresenter(): RandomJokePresenter {
        val presenter = RandomJokePresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}