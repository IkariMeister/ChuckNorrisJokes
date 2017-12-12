package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.hasResults
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByKeywordPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByKeywordPresenter.MVPView, JokeByKeywordPresenter.Navigator>() {

    override suspend fun initialize() {

    }

    interface MVPView {
        fun showError(msg: String)
        fun addJoke(joke: Joke)
        fun showEmptyCase()
        fun hideEmptyCase()
    }

    interface Navigator {

    }

    suspend fun onSearchButtonClicked(query: String) {
        if (query.isEmpty()) {
            view?.showError("Please enter a keyword")
            return
        }

        val response = coroutine {
            repository.getRandomJokeByKeyword(query.toLowerCase())
        }.await()

        if (response.hasResults()) {
            view?.hideEmptyCase()

            response.first?.forEach { joke ->
                view?.addJoke(joke)
            }
        } else {
            view?.showEmptyCase()
        }
    }
}
