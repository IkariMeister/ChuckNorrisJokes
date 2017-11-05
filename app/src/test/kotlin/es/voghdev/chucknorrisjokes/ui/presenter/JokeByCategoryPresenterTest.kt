package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import es.voghdev.chucknorrisjokes.anyCategory
import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.model.JokeCategory
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JokeByCategoryPresenterTest() {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: JokeByCategoryPresenter.Navigator

    @Mock lateinit var mockView: JokeByCategoryPresenter.MVPView

    lateinit var presenter: JokeByCategoryPresenter

    @Mock lateinit var mockChuckNorrisRepository: ChuckNorrisRepository

    val someCategories = listOf(
            JokeCategory("politics"),
            JokeCategory("sports")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun `should request the list of categories on start`() {
        givenThereAreSomeCategories(someCategories)

        runBlocking {
            presenter.initialize()
        }

        verify(mockChuckNorrisRepository).getJokeCategories()
    }

    @Test
    fun `should fill the categories spinner when categories are received`() {
        givenThereAreSomeCategories(someCategories)

        runBlocking {
            presenter.initialize()
        }

        verify(mockView).fillCategoriesSpinner(anyList())
    }

    @Test
    fun `should request a random joke by category to the API when "search" button is clicked`() {
        givenThereAreSomeCategories(someCategories)

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked(1)
        }

        verify(mockChuckNorrisRepository).getRandomJokeByCategory(anyCategory())
    }

    @Test
    fun `should show the joke's text when a random joke is received`() {
        givenThereAreSomeCategories(someCategories)

        runBlocking {
            presenter.initialize()

            presenter.onSearchButtonClicked(1)
        }

        verify(mockView).showJokeText("")
    }

    private fun givenThereAreSomeCategories(someCategories: List<JokeCategory>) {
        whenever(mockChuckNorrisRepository.getJokeCategories()).thenReturn(Pair(someCategories, null))
    }

    private fun createMockedPresenter(): JokeByCategoryPresenter {
        val presenter = JokeByCategoryPresenter(mockResLocator, mockChuckNorrisRepository)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
