package es.voghdev.chucknorrisjokes.ui.presenter

import com.nhaarman.mockito_kotlin.verify
import es.voghdev.chucknorrisjokes.app.ResLocator
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JokeByCategoryPresenterTest() {
    @Mock lateinit var mockResLocator: ResLocator

    @Mock lateinit var mockNavigator: JokeByCategoryPresenter.Navigator

    @Mock lateinit var mockView: JokeByCategoryPresenter.MVPView

    lateinit var presenter: JokeByCategoryPresenter

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

        //verify(mockChuckNorrisRepository)
    }

    private fun createMockedPresenter(): JokeByCategoryPresenter {
        val presenter = JokeByCategoryPresenter(mockResLocator)
        presenter.view = mockView
        presenter.navigator = mockNavigator
        return presenter
    }
}
