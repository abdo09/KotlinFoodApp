package net.ferraSolution.food.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import net.ferraSolution.food.R
import net.ferraSolution.food.base.BaseSupportFragment
import org.koin.android.viewmodel.ext.android.viewModel

/*class OnBoardingViewPager : BaseSupportFragment(R.layout.onboarding_viewpager) {

    override val viewModel by viewModel<LoginFragmentViewModel>()

    private lateinit var onBoardingViewPager: ViewPager2
    private val itemCount by lazy { onBoardingViewPager.adapter!!.itemCount }
    private lateinit var onBoardingBtnSkip: TextView
    private lateinit var onBoardingBtnNext: TextView
    private lateinit var onBoardingBtnStart: TextView
    private lateinit var onBoardingBtnPrevious: TextView
    private lateinit var wormDotsIndicator: WormDotsIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().navigationBarAndStatusBarColor(R.color.purple_700, R.color.purple_700)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateViews(view)

        onClick()

    }

    private fun inflateViews(view: View) {
        onBoardingViewPager = view.findViewById(R.id.onboarding_viewpager)
        onBoardingViewPager.apply {
            adapter = OnBoardingViewPagerFragmentAdapter(requireActivity())
            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handleNextState(position)
                }
            })
        }
        onBoardingBtnStart = view.findViewById(R.id.onboarding_btn_start)
        onBoardingBtnNext = view.findViewById(R.id.onboarding_btn_next)
        onBoardingBtnPrevious = view.findViewById(R.id.onboarding_btn_previous)
        onBoardingBtnSkip = view.findViewById(R.id.onboarding_btn_skip)

        wormDotsIndicator = view.findViewById(R.id.worm_dots_indicator)
        wormDotsIndicator.setViewPager2(onBoardingViewPager)

    }

    private fun setParallaxTransformation(page: View, position: Float) {
        page.apply {
            val parallaxView = this.onboarding_image_view
            when {
                position < -1 -> // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 1f
                position <= 1 -> { // [-1,1]
                    parallaxView.translationX = (-position * (width / 8)).toFloat() //Half the normal speed
                    onboarding_header_text.translationX = (-position * (width / 4)).toFloat() //Half the normal speed

                    parallaxView.scaleX = (1 - Math.abs(position / 1.92)).toFloat()
                    parallaxView.scaleY = (1 - Math.abs(position / 1.92)).toFloat()
                }
                else -> // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 1f
            }
        }
    }

    private fun handleNextState(index: Int) {
        if (index == itemCount - 1) {
            onBoardingBtnNext.fadeOut(200)
            onBoardingBtnStart.fadeIn(500)
        } else {
            onBoardingBtnStart.fadeOut(200)
            onBoardingBtnNext.fadeIn(300)
            if (index == 1)
                context?.getCustomColor(R.color.white)?.let { onBoardingBtnNext.toColor(it) }
        }

        if (index == 0) {
            onBoardingBtnNext.fadeIn(200)
            onBoardingBtnPrevious.fadeOut(200)
            context?.getCustomColor(R.color.cyan_200)?.let { onBoardingBtnNext.toColor(it) }
        } else{
            onBoardingBtnPrevious.fadeIn(300)
        }

    }

    private fun onClick(){
        onBoardingBtnSkip.setOnClickListener {
            navController.navigate(OnBoardingViewPagerDirections.actionOnBoardingViewPagerToLoginFragment())
        }

        onBoardingBtnStart.setOnClickListener {
            navController.navigate(OnBoardingViewPagerDirections.actionOnBoardingViewPagerToLoginFragment())
        }

        onBoardingBtnNext.setOnClickListener {
            if (onBoardingViewPager.currentItem < itemCount - 1)
                onBoardingViewPager.setCurrentItemX((onBoardingViewPager.currentItem - 1), 300)
        }

        onBoardingBtnPrevious.setOnClickListener {
            if (onBoardingViewPager.currentItem > 0)
                onBoardingViewPager.setCurrentItemX((onBoardingViewPager.currentItem + 1), 300)
        }
    }

    class OnBoardingViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): OnBoardingStep {
            return OnBoardingStep.newInstance(position)
        }

        override fun getItemCount(): Int {
            return 3
        }
    }

    class OnBoardingStep : Fragment() {


        private val onBoardingText by lazy { resources.getStringArray(R.array.on_boarding_titles) }
        private val onBoardingIllustrations = listOf(R.drawable.ic_search_cyan, R.drawable.ic_calendar, R.drawable.ic_love)

        companion object {
            fun newInstance(position: Int): OnBoardingStep {
                return OnBoardingStep().apply {
                    arguments = Bundle().apply {
                        putInt("i", position)
                    }
                }
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            onBoardingIllustrations[0]
            val view = inflater.inflate(R.layout.onboarding_viewpager_item, container, false)

            val index = requireArguments().getInt("i")

            requireContext().loadWithGlide( into = view.findViewById(R.id.onboarding_image_view), url = ResourcesCompat.getDrawable(resources, onBoardingIllustrations[index], activity?.theme))

            view.findViewById<TextView>(R.id.onboarding_header_text).apply { text = onBoardingText[index] }

            return view
        }
    }
}*/
