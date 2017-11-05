package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.hasNoResults
import es.voghdev.chucknorrisjokes.app.hasResults
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByKeywordPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByKeywordPresenter.MVPView, JokeByKeywordPresenter.Navigator>() {

    override suspend fun initialize() {

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

    suspend fun onSearchButtonClicked(text: String) {
        if (text.isEmpty()) {
            view?.showKeywordError("Please enter a keyword")
            return
        }

        val apiResponse = coroutine {
            repository.getRandomJokeByKeyword(text.toLowerCase())
        }.await()

        if (apiResponse.hasResults()) {
            view?.showJokeText(apiResponse.first?.elementAt(0)?.value ?: "")
            view?.showJokeImage(apiResponse.first?.elementAt(0)?.iconUrl ?: "")
        } else if (apiResponse.hasNoResults()) {
            view?.showEmptyCase()
        } else {
            view?.showApiError(apiResponse.second?.message() ?: "")
        }
    }
}
