@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package net.ferraSolution.food.utils

import android.animation.*
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.AddonModel
import net.ferraSolution.food.data.models.BestDealModel
import net.ferraSolution.food.data.models.SizeModel
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun roundOffDecimal(number: Double): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}

fun Double.formatPrice(): String {
    return String.format(Locale.ENGLISH, "%.2f", this)
}

fun Context.postToLooper(delay: Long = 0, r: Runnable) {
    val handlerUI = Handler(Looper.getMainLooper())
    handlerUI.postDelayed(r, delay)
}

interface TextWatcherMinified : TextWatcher {
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
}

fun Activity.navigationBarAndStatusBarColor(
    @ColorRes statusColor: Int,
    @ColorRes navigationColor: Int
) {
    val window: Window = this.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, statusColor)
        window.navigationBarColor = ContextCompat.getColor(this, navigationColor)
    }
}

private fun getSimpleDateFormat(): SimpleDateFormat {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    return sdf
}

/**
 * Formats timestamp to 'date month' format (e.g. 'February 3').
 */

fun String.formatDate(timeInMillis: String?): String? {
    val dateFormat = getSimpleDateFormat()
    var date = timeInMillis
    try {
        date = formatDate(dateFormat.parse(timeInMillis ?: "").time, true)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date
}

/**
 * Formats timestamp to 'date month' format (e.g. 'February 3').
 */

fun formatDate(timeInMillis: Long): String? {
    val dateFormat = SimpleDateFormat("MMMM dd", Locale.ENGLISH)
    return dateFormat.format(timeInMillis)
}


/**
 * Formats timestamp to 'date month' format (e.g. 'February 3').
 */
fun formatDate(timeInMillis: Long, fullDate: Boolean): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    return dateFormat.format(timeInMillis)
}

fun View.fadeIn(duration: Long = 600) {
    if (visibility != View.VISIBLE)
        this.apply {
            alpha = 0f
            visibility = View.VISIBLE
            post {
                animate().alpha(1f).setDuration(duration).start()
            }
        }

}

fun View.fadeOut(duration: Long = 800) {
    if (visibility != View.GONE)
        this.apply {
            alpha = 1f
            post {
                animate().alpha(0f).setDuration(duration)
                    .withEndAction {
                        visibility = View.GONE
                    }.start()
            }
        }
}

fun View.fadeIn(duration: Long = 600, visible: Int) {
    if (visibility != visible)
        this.apply {
            alpha = 0f
            visibility = visible
            post {
                animate().alpha(1f).setDuration(duration).start()
            }
        }

}

fun View.fadeOut(duration: Long = 800, visible: Int) {
    if (visibility != visible)
        this.apply {
            alpha = 1f
            post {
                animate().alpha(0f).setDuration(duration)
                    .withEndAction {
                        visibility = visible
                    }.start()
            }
        }
}

fun Context.loadWithGlide(
    into: ImageView?,
    url: Any?,
    fitImage: Boolean = false,
    roundImage: Boolean = false,
    listener: RequestListener<Drawable>? = null
) {
    if (url == null)
        return
    val circularProgressDrawable = CircularProgressDrawable(this)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 100f
    circularProgressDrawable.setStyle(CircularProgressDrawable.DEFAULT)
    circularProgressDrawable.start()

    val options = RequestOptions()
//            .optionalFitCenter()
        .placeholder(circularProgressDrawable.apply {
            this.backgroundColor = android.R.color.black

        })
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .error(circularProgressDrawable.apply {
            this.backgroundColor = android.R.color.holo_red_dark
        })
        .priority(Priority.HIGH)


    if (fitImage)
        options.fitCenter()
    if (roundImage)
        options.transform(RoundedCorners(16))



    try {
        var imgURI = "" //RemoteConfigs.BASE_URL

        val glide = Glide.with(this).asDrawable()

        if (url is String) {
            when {
                url.startsWith("/data") -> imgURI = url
                url.startsWith("/") -> imgURI = url.replaceFirst("/", "https://")
                url.startsWith("http:") -> imgURI = url.replace("http", "https")
                url.startsWith("https") -> imgURI = url
                else -> imgURI += url
            }

            if (into != null) {
                glide.load(imgURI).apply(options)
                    .listener(listener)
                    .into(into)
            }
        } else {
            if (into != null) {
                glide.load(url).apply(options)
                    .listener(listener)
                    .into(into)
            }

        }

    } catch (ex: Exception) {
        ex.printStackTrace()
    }

}

fun Context.loadWithGlide(
    into: CircleImageView?,
    url: Any?,
    fitImage: Boolean = true,
    roundImage: Boolean = false,
    listener: RequestListener<Drawable>? = null
) {
    if (url == null)
        return
    val circularProgressDrawable = CircularProgressDrawable(this)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 100f
    circularProgressDrawable.setStyle(CircularProgressDrawable.DEFAULT)
    circularProgressDrawable.start()

    val options = RequestOptions()
//            .optionalFitCenter()
        .placeholder(circularProgressDrawable.apply {
            this.backgroundColor = android.R.color.black

        })
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .error(circularProgressDrawable.apply {
            this.backgroundColor = android.R.color.holo_red_dark
        })
        .priority(Priority.HIGH)


    if (fitImage)
        options.fitCenter()
    if (roundImage)
        options.transform(RoundedCorners(16))



    try {
        var imgURI = "" //RemoteConfigs.BASE_URL

        val glide = Glide.with(this).asDrawable()

        if (url is String) {
            when {
                url.startsWith("/data") -> imgURI = url
                url.startsWith("/") -> imgURI = url.replaceFirst("/", "https://")
                url.startsWith("http:") -> imgURI = url.replace("http", "https")
                url.startsWith("https") -> imgURI = url
                else -> imgURI += url
            }

            if (into != null) {
                glide.load(imgURI).apply(options)
                    .listener(listener)
                    .into(into)
            }
        } else {
            if (into != null) {
                glide.load(url).apply(options)
                    .listener(listener)
                    .into(into)
            }

        }

    } catch (ex: Exception) {
        ex.printStackTrace()
    }

}

fun ViewPager2.setCurrentItemX(
    item: Int,
    duration: Long,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    pagePxWidth: Int = width // Default value taken from getWidth() from ViewPager2 view
) {
    val pxToDrag: Int = pagePxWidth * (item - currentItem)
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()
        fakeDragBy(currentPxToDrag)
        previousValue = currentValue
    }
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            try {
                beginFakeDrag()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

        override fun onAnimationEnd(animation: Animator?) {
            try {
                endFakeDrag()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }

        override fun onAnimationCancel(animation: Animator?) { /* Ignored */
        }

        override fun onAnimationRepeat(animation: Animator?) { /* Ignored */
        }
    })
    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}

/*fun vatString(value: Double): String {
    val totalAmountWithoutVAT = value / 1.05
    val vatValue = value - totalAmountWithoutVAT
    return String.format("%.2f", vatValue)
}*/

fun TextView.toColor(@ColorRes colorResId: Int) {
    ObjectAnimator.ofObject(
        this,  // Object to animating
        "textColor",  // Property to animate
        ArgbEvaluator(),  // Interpolation function
        this.currentTextColor,  // Start color
        colorResId // End color
    ).setDuration(600) // Duration in milliseconds
        .start() //

}

fun TextView.colorize(subStringToColorize: String, @ColorRes colorResId: Int) {

    val spannable: Spannable = SpannableString(text)

    val startIndex = text.indexOf(subStringToColorize, startIndex = 0, ignoreCase = false)
    val endIndex = startIndex + subStringToColorize.length

    val color: Int = ContextCompat.getColor(context, colorResId)

    if (startIndex != -1) {
        spannable.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setText(spannable, TextView.BufferType.SPANNABLE)
    }
}

fun TextInputLayout.setRedBoarder(field: Int) {
    this.boxStrokeColor =
        ResourcesCompat.getColor(resources, R.color.text_input_stroke_red_color, null)
    this.hint = "${resources.getString(R.string.please_enter)} ${resources.getString(field)}"
    setTextInputLayoutHintColor(this, context, R.color.red_400)
}

private fun setTextInputLayoutHintColor(
    textInputLayout: TextInputLayout,
    context: Context,
    @ColorRes colorIdRes: Int
) {
    textInputLayout.defaultHintTextColor = ColorStateList.valueOf(
        ContextCompat.getColor(
            context,
            colorIdRes
        )
    )
}

fun EditText.setGoldColorBoarder(field: Int, textInputLayout: TextInputLayout) {
    this.addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.isEmpty() == true) {
                textInputLayout.setRedBoarder(field)
            } else {
                textInputLayout.boxStrokeColor =
                    ResourcesCompat.getColor(resources, R.color.gold_500, null)
                textInputLayout.hint = resources.getString(field)
                setTextInputLayoutHintColor(textInputLayout, context, R.color.gold_300)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    })
}

fun Int.dp(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
}

fun Context.getColorCompat(colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun transformListAndAddTwo(itemList: List<BestDealModel>): ArrayList<BestDealModel> {
    val size: Int = itemList.size
    val listTemp: ArrayList<BestDealModel> = ArrayList(size + 2)
    for (iPL in 0..size + 2) {
        listTemp.add(itemList[(iPL + size - 2) % size])
    }
    return listTemp
}

fun returnExtras(
    sizes: List<SizeModel>,
    addons: List<AddonModel>?,
    isSize: Boolean = true
): String {
    return if (isSize) {
        if (sizes.isNullOrEmpty()) {
            "Default"
        } else {
            sizes.toString()
        }
    } else {
        if (addons.isNullOrEmpty()) {
            "Default"
        } else {
            addons.toString()
        }
    }
}

fun returnExtrasPrice(
    sizes: List<SizeModel>,
    addons: List<AddonModel>?
): Double {
    var price = 0.0

    if (!sizes.isNullOrEmpty()) {
        sizes.forEach {
            price += it.price.toDouble()
        }
    }

    if (!addons.isNullOrEmpty()) {
        addons.forEach {
            price += it.price?.toDouble()?: 0.0
        }
    }
    return price
}

// Set gender checked
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun setChecked(btn1: RelativeLayout, btn2: RelativeLayout, tv1: TextView, tv2: TextView, img1: ImageView, img2: ImageView, isChecked: Boolean, context: Context) {
    if (isChecked) {
        btn1.background = ContextCompat.getDrawable(context, R.drawable.boarder_gold500_color)
        tv1.setHintTextColor(ContextCompat.getColor(context, R.color.gold_300))
        img1.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_checked_gender
            )
        )

        btn2.background =
            ContextCompat.getDrawable(context, R.drawable.boarder_gray_color)
        tv2.setHintTextColor(ContextCompat.getColor(context, R.color.grayColor500))
        img2.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_unchecked_gender
            )
        )
    } else {
        btn2.background =
            ContextCompat.getDrawable(context, R.drawable.boarder_gold500_color)
        tv2.setHintTextColor(ContextCompat.getColor(context, R.color.gold_300))
        img2.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_checked_gender
            )
        )

        btn1.background =
            ContextCompat.getDrawable(context, R.drawable.boarder_gray_color)
        tv1.setHintTextColor(ContextCompat.getColor(context, R.color.grayColor500))
        img1.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_unchecked_gender
            )
        )
    }
}
