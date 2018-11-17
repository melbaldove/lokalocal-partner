package st.teamcataly.lokalocalpartner.root.loggedout

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.logged_out_rib.view.*
import st.teamcataly.lokalocalpartner.root.loggedin.Credentials

/**
 * Top level view for {@link LoggedOutBuilder.LoggedOutScope}.
 */
class LoggedOutView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle), LoggedOutInteractor.LoggedOutPresenter {
    override fun loginFailed() {
        usernameTIL.error = "Invalid Credentials"
        passwordTIL.error = "Invalid Credentials"
    }

    override fun login(): Observable<Credentials> = RxView.clicks(logged_out_sign_in).map {
        Credentials(username.text.toString(), password.text.toString())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}
