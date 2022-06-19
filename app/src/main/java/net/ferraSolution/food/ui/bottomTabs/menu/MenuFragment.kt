package net.ferraSolution.food.ui.bottomTabs.menu

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_menu.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.ui.HomeActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MenuFragment : BaseSupportFragment(R.layout.fragment_menu),
    MenuFragmentController.AdapterCallbacks {

    override val viewModel by viewModel<MenuFragmentViewModel>()

    private val menuFragmentController: MenuFragmentController by lazy { MenuFragmentController(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBarVisibilityAndTitle(View.VISIBLE, R.string.menu)

        navigationVisibility = View.VISIBLE

        initController()

        observer()

        addCallBackToExit()

    }

    private fun initController() {

        menu_recycler_view.setControllerAndBuildModels(controller = menuFragmentController)

    }

    private fun observer() {

        viewModel.category.observe(viewLifecycleOwner, {
            menuFragmentController.categoryList = it.toList()
            menuFragmentController.requestModelBuild()
        })

        viewModel.getCategoryFromDB().observe(viewLifecycleOwner, {
            if (it.isEmpty()){
                viewModel.getCategories(true)
            } else {
                menuFragmentController.categoryList = it.toList()
                menuFragmentController.requestModelBuild()
                viewModel.getCategories(false)
            }
        })

    }

    private fun addCallBackToExit() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBack)
    }


    private val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (activity is HomeActivity) {
                val homeActivity = activity as HomeActivity
                navController.navigate(R.id.nav_home_fragment)
                homeActivity.bottomNavigationView.selectedItemId = R.id.navigation_home
            }
        }

    }

    override fun onCategoryClickedLister(categoryModel: CategoryModel) {
        navController.navigate(MenuFragmentDirections.actionMenuFragmentToFoodsFragment(categoryModel))
    }

}