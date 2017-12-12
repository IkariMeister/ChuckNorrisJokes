package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.success
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
    }

    interface Navigator {

    }

    suspend fun onSearchButtonClicked(query: String) {
        if (query.isEmpty())
            view?.showError("Please enter a keyword")
        else {
            val result = coroutine {
                repository.getRandomJokeByKeyword(query.toLowerCase())
            }.await()

            if(result.success()) {
                result.first?.forEach { joke ->
                    view?.addJoke(joke)
                }
            }
        }
    }
}
