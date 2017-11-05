package es.voghdev.chucknorrisjokes.ui.presenter

import es.voghdev.chucknorrisjokes.app.ResLocator
import es.voghdev.chucknorrisjokes.app.coroutine
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository

class JokeByKeywordPresenter(val context: ResLocator, val repository: ChuckNorrisRepository) :
        Presenter<JokeByKeywordPresenter.MVPView, JokeByKeywordPresenter.Navigator>() {

    override suspend fun initialize() {

    }

    interface MVPView {
        fun showKeywordError(text: String)

    }

    interface Navigator {

    }

    suspend fun onSearchButtonClicked(text: String) {
        if(text.isNotEmpty()) {
            coroutine {
                repository.getRandomJokeByKeyword(text.toLowerCase())
            }.await()
        } else {
            view?.showKeywordError("Please enter a keyword")
        }
    }
}
