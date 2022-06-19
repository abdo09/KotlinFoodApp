package net.ferraSolution.food.ui.bottomTabs.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.models.BestDealModel
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.models.PopularCategoriesResponse
import net.ferraSolution.food.data.models.UserModel
import net.ferraSolution.food.utils.Constants
import okhttp3.internal.toImmutableList
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : BaseSupportFragment(R.layout.fragment_home), HomePageController.AdapterCallbacks {

    override val viewModel by viewModel<HomeFragmentViewModel>()
    var allCategories = ArrayList<CategoryModel?>()

    private val homePageController: HomePageController by lazy { HomePageController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFirstTime = Constants().firstTime(requireContext())

        if (isFirstTime){
            viewModel.getBestDeals(true)
            Constants().firstTime(requireContext(),  false)
        } else {
            viewModel.getBestDeals(false)
        }

        viewModel.getAllCategories()
        val uid = Constants().getUid(requireContext())
        viewModel.getUserInfo(uid)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set bottomBar visibility//
        navigationVisibility = View.VISIBLE
        setAppBarVisibilityAndTitle(View.VISIBLE, R.string.home)

        initController()
        observer()
        addCallBackToExit()

    }

    private fun initController() {

        main_home_recyclerView.setControllerAndBuildModels(controller = homePageController)

    }

    private fun observer() {

        homePageController.sliderPosition.observe(viewLifecycleOwner){
            Timber.d("SLIDERPOSITION = $it")
        }

        viewModel.popularCategories.observe(viewLifecycleOwner) { popularCategoriesList ->
            homePageController.popularCategoriesList = popularCategoriesList.toImmutableList()
            homePageController.sliderPosition = viewModel.sliderPosition
            homePageController.requestModelBuild()

        }

        viewModel.bestDeals.observe(viewLifecycleOwner) { bestDeal ->
            homePageController.bestDealList = bestDeal.toImmutableList()
            homePageController.requestModelBuild()
            viewModel.getPopularCategories()
        }

        viewModel.allCategories.observe(viewLifecycleOwner) {
            allCategories = it
        }

        viewModel.userModel.observe(viewLifecycleOwner) {
            Constants().setUser(requireContext(), it ?: UserModel())
        }

    }

    private fun addCallBackToExit() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callBack)
    }

    var lastCallback: Long = 0
    private var callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - lastCallback < 3000) {
                requireActivity().finish()
            } else {
                viewModel.showInfo.value = R.string.press_back_again_to_exit
                lastCallback = System.currentTimeMillis()
            }
        }
    }

    override fun onPopularCategoryClickLister(popularCategory: PopularCategoriesResponse) {
        allCategories.forEach { category ->
            if (category?.menuId == popularCategory.menu_id){
                category?.foods?.forEach { food ->
                    if (food.id == popularCategory.food_id){
                        navController.navigate(HomeFragmentDirections.actionNavHomeFragmentToNavFoodDetailsFragment(category = category, itemFood = food))
                    }
                }
            }
        }
    }

    override fun onBestDealClickLister(bestDealModel: BestDealModel) {
        allCategories.forEach { category ->
            if (category?.menuId == bestDealModel.menu_id){
                category?.foods?.forEach { food ->
                    if (food.id == bestDealModel.food_id){
                        navController.navigate(HomeFragmentDirections.actionNavHomeFragmentToNavFoodDetailsFragment(category = category, itemFood = food))
                    }
                }
            }
        }
    }

    override fun onViewAllCategoriesClickLister() {
        //Toast.makeText(requireContext(), "Moving to categories list", Toast.LENGTH_SHORT).show()
    }


}