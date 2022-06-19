/*
 * Copyright (c) 2018. this code belongs to Z3R0 any modifications is prohibited
 */

package net.ferraSolution.food.ui.bottomTabs.home.models

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.BestDealModel
import net.ferraSolution.food.ui.progress.CirclePagerIndicatorDecoration
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.carousel_slider)
abstract class SliderCarouselModel(val sliderPosition: MutableLiveData<Int>) : EpoxyModelWithHolder<SliderCarouselModel.Holder>(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val slidingFlow by lazy {
        flow {
            var x = 0
            do {
                emit(x)
                x++
                delay(delay)
            } while (x < 1000)
        }
    }

    @EpoxyAttribute
    var imageList = listOf<BestDealModel>()

    @EpoxyAttribute(hash = false)
    var onViewAllPromotionListener: OnModelClickListener<SliderImageModel, View>? = null

    private val indicator = CirclePagerIndicatorDecoration()

    override fun bind(holder: Holder) {

        holder.sliderCarousel.apply {
            (layoutManager as LinearLayoutManager).stackFromEnd = true
            withModels {
                imageList.forEachIndexed { index, image ->
                    Timber.d("The image is ${image.name}")
                    SliderImageModel_()
                        .id("SliderImageModel_$index")
                        .onImageClickedItemListener { model, parentView, clickedView, position ->
                            onViewAllPromotionListener?.onClick(model, parentView.carItem, clickedView, position)
                        }
                        .image(image)
                        .addTo(this)
                }

                //LoaderModelRow_().id("loading").addIf(imageList.isEmpty() && showLoading,this)
            }
            this.numViewsToShowOnScreen = 1f
            addItemDecoration(indicator)
            if (imageList.size > 1)
                autoScroll(imageList.size, this)
            addListener(this)
        }

    }

    private fun addListener(carousel: Carousel) {
        carousel.apply {
            val onScrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    // When dragging, we assume user swiped. So we will stop auto rotation
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        stopAutoScroll()
                    }

                }
            }
            carousel.addOnScrollListener(onScrollListener)
        }
    }

    private val delay = 3000L

    private fun autoScroll(listSize: Int, carousel: Carousel) {
        launch {
            withContext(Dispatchers.Main) {
                slidingFlow.collect {
                    sliderPosition.postValue(it % listSize)
                    carousel.smoothScrollToPosition(it % listSize)
                }
            }
        }
    }

    fun stopAutoScroll() {
        slidingFlow.debounce(delay)
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        stopAutoScroll()
        holder.sliderCarousel.removeItemDecoration(indicator)
        job.cancel()
    }

    class Holder : EpoxyHolder() {
        lateinit var sliderCarousel: Carousel
        override fun bindView(itemView: View) {
            sliderCarousel = itemView.findViewById(R.id.cars)
        }
    }
}