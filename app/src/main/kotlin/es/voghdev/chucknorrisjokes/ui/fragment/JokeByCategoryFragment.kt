package es.voghdev.chucknorrisjokes.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import es.voghdev.chucknorrisjokes.R
import es.voghdev.chucknorrisjokes.app.AndroidResLocator
import es.voghdev.chucknorrisjokes.app.configureDefaultAdapter
import es.voghdev.chucknorrisjokes.datasource.api.GetJokeCategoriesApiImpl
import es.voghdev.chucknorrisjokes.datasource.api.GetRandomJokeApiImpl
import es.voghdev.chucknorrisjokes.datasource.api.GetRandomJokeByCategoryApiImpl
import es.voghdev.chucknorrisjokes.datasource.api.GetRandomJokeByKeywordApiImpl
import es.voghdev.chucknorrisjokes.model.JokeCategory
import es.voghdev.chucknorrisjokes.repository.ChuckNorrisRepository
import es.voghdev.chucknorrisjokes.ui.presenter.JokeByCategoryPresenter
import kotlinx.android.synthetic.main.fragment_joke_by_category.*
import kotlinx.coroutines.experimental.runBlocking

class JokeByCategoryFragment : BaseFragment(), JokeByCategoryPresenter.MVPView, JokeByCategoryPresenter.Navigator {
    var presenter: JokeByCategoryPresenter? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chuckNorrisRepository = ChuckNorrisRepository(
                GetRandomJokeApiImpl(),
                GetJokeCategoriesApiImpl(),
                GetRandomJokeByKeywordApiImpl(),
                GetRandomJokeByCategoryApiImpl())

        presenter = JokeByCategoryPresenter(AndroidResLocator(context), chuckNorrisRepository)
        presenter?.view = this
        presenter?.navigator = this

        runBlocking {
            presenter?.initialize()
        }

        spn_categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                runBlocking {
                    presenter?.onCategorySelected(spn_categories.adapter.getItem(position) as JokeCategory)
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_joke_by_category
    }

    override fun fillCategoriesSpinner(categories: List<JokeCategory>) {
        val names = categories.map { c -> c.name }
        spn_categories.configureDefaultAdapter(names)
    }
}
