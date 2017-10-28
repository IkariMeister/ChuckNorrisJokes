package es.voghdev.chucknorrisjokes

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import es.voghdev.chucknorrisjokes.app.AndroidResLocator
import es.voghdev.chucknorrisjokes.ui.activity.BaseActivity
import es.voghdev.chucknorrisjokes.ui.adapter.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainPresenter.MVPView, MainPresenter.Navigator {
    var presenter: MainPresenter? = null
    lateinit var onTabSelectedListener: TabLayout.OnTabSelectedListener
    lateinit var adapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MainPresenter(AndroidResLocator(this))
        presenter?.view = this
        presenter?.navigator = this

        onTabSelectedListener = TabLayout.ViewPagerOnTabSelectedListener(viewPager)

        presenter?.initialize()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.destroy()

        tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun configureTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Random Joke"))
        tabLayout.addTab(tabLayout.newTab().setText("Joke by Keyword"))
        tabLayout.addTab(tabLayout.newTab().setText("Joke by category"))

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }
        })

        adapter = MainPagerAdapter(this)
        viewPager.adapter = adapter
    }
}
