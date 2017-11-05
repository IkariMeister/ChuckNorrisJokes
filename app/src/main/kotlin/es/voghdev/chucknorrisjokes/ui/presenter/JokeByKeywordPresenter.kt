package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.*
import es.voghdev.chucknorrisjokes.model.Joke
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByKeywordPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByKeywordPresenter.MVPView, JokeByKeywordPresenter.Navigator>() {

    override suspend fun initialize() {

    }

    suspend fun onSearchButtonClicked(text: String) {
        if (text.isEmpty()) {
            view?.showKeywordError("Please enter a keyword")
            return
        }

        val apiResponse = coroutine {
            repository.getRandomJokeByKeyword(text.toLowerCase())
        }.await()

        if (apiResponse.hasResults()) {
            renderJoke(apiResponse.getFirstJoke())
        } else if (apiResponse.hasNoResults()) {
            view?.showEmptyCase()
        } else {
            view?.showApiError(apiResponse.second?.message() ?: "")
        }
    }

    private fun renderJoke(joke: Joke?) {
        view?.showJokeText(joke?.value ?: "")
        view?.showJokeImage(joke?.iconUrl ?: "")
    }

    interface MVPView {
        fun showKeywordError(text: String)
        fun showEmptyCase()
        fun showJokeText(text: String)
        fun showJokeImage(url: String)
        fun showApiError(text: String)
    }

    interface Navigator {

    }
}
