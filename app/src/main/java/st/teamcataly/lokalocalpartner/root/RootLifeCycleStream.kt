package st.teamcataly.lokalocalpartner.root

import android.os.Bundle

/**
 * @author Melby Baldove
 * melbourne.baldove@owtoph.com
 */

sealed class RootLifecycleEvent {
    data class OnCreate(val bundle: Bundle?) : RootLifecycleEvent()
    data class OnSaveInstanceState(val bundle: Bundle) : RootLifecycleEvent()
    object OnResume : RootLifecycleEvent()
    object OnStart : RootLifecycleEvent()
    object OnStop : RootLifecycleEvent()
    object OnPause : RootLifecycleEvent()
    object OnDestroy : RootLifecycleEvent()
    object OnLowMemory : RootLifecycleEvent()
}