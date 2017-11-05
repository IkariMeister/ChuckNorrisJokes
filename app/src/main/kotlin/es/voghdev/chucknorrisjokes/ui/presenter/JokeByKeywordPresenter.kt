package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.app.success
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
        if(text.isNotEmpty()) {
            coroutine {
                repository.getRandomJokeByKeyword(text.toLowerCase())
            }.await().let { result ->
                if(result.success() && result.first?.isNotEmpty() ?: false) {
                    view?.showJokeText(result.first?.elementAt(0)?.value ?: "")
                    view?.showJokeImage(result.first?.elementAt(0)?.iconUrl ?: "")
                } else if(result.success()) {
                    view?.showEmptyCase()
                } else {
                    view?.showApiError(result.second?.message() ?: "")
                }
            }
        } else {
            view?.showKeywordError("Please enter a keyword")
        }
    }
}
