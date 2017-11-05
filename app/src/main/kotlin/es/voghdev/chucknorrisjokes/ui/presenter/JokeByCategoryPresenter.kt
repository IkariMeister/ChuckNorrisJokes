package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.success
import es.voghdev.chucknorrisjokes.model.JokeCategory
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByCategoryPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByCategoryPresenter.MVPView, JokeByCategoryPresenter.Navigator>() {

    override suspend fun initialize() {
        coroutine {
            repository.getJokeCategories()
        }.await().let { result ->
            if (result.success())
                view?.fillCategoriesSpinner(result.first ?: emptyList())
        }
    }

    interface MVPView {
        fun fillCategoriesSpinner(categories: List<JokeCategory>)

    }

    interface Navigator {

    }

    suspend fun onCategorySelected(category: JokeCategory) {
        coroutine {
            repository.getRandomJokeByCategory(category)
        }.await().let { result ->

        }
    }
}
