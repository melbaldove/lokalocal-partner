package st.teamcataly.lokalocalpartner.root.loggedout

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

/**
 * Top level view for {@link LoggedOutBuilder.LoggedOutScope}.
 */
class LoggedOutView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle), LoggedOutInteractor.LoggedOutPresenter
