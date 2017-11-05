package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = createMockedPresenter()
    }

    @Test
    fun `should request the list of categories on start`() {
        runBlocking {
            presenter.initialize()
        }

        verify(mockChuckNorrisRepository).getJokeCategories()
    }

    @Test
    fun `should fill the list of categories in the spinner`() {
        val someCategories = listOf(
                JokeCategory("politics"),
                JokeCategory("sports")
        )
        givenThereAreSomeCategories(someCategories)

        runBlocking {
            presenter.initialize()
        }
    }

    @Test
    fun `should fill the categories spinner when categories are received`() {
        val someCategories = listOf(
                JokeCategory("politics"),
                JokeCategory("sports")
        )
        givenThereAreSomeCategories(someCategories)

        runBlocking {
            presenter.initialize()
        }

        verify(mockView).fillCategoriesSpinner(anyList())
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
