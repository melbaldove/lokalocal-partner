package st.teamcataly.lokalocalpartner

import android.view.ViewGroup
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter
import st.teamcataly.lokalocalpartner.root.RootBuilder

class RootActivity : RibActivity() {
    override fun createRouter(parentViewGroup: ViewGroup): ViewRouter<*, *, *> {
        return RootBuilder(object : RootBuilder.ParentComponent {}).build(parentViewGroup)
    }
}
