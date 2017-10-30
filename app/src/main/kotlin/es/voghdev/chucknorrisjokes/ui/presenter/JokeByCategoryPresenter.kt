package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.success
import es.voghdev.chucknorrisjokes.model.JokeCategory
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByCategoryPresenter(val context: ResLocator, val chuckNorrisRepository: ChuckNorrisRepository) :
        Presenter<JokeByCategoryPresenter.MVPView, JokeByCategoryPresenter.Navigator>() {

    var categories: List<JokeCategory> = emptyList()

    override suspend fun initialize() {
        coroutine {
            chuckNorrisRepository.getJokeCategories()
        }.await().let { result ->
            if (result.success()) {
                categories = result.first ?: emptyList()
                view?.fillCategories(categories)
            }
        }
    }

    suspend fun onSearchButtonClicked(position: Int) {
        coroutine {
            chuckNorrisRepository.getRandomJokeByCategory(categories[position])
        }.await().let { result ->
            if (result.success()) {
                view?.showJokeText(result.first?.value ?: "")
            }
        }
    }

    interface MVPView {
        fun fillCategories(list: List<JokeCategory>)
        fun showJokeText(text: String)
    }

    interface Navigator {

    }
}
