package st.teamcataly.lokalocalpartner.root.loggedin.checkout

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Top level view for {@link CheckoutBuilder.CheckoutScope}.
 */
class CheckoutView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle), CheckoutInteractor.CheckoutPresenter
