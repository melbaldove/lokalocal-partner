package st.teamcataly.lokalocalpartner.root.loggedin.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import st.teamcataly.lokalocalpartner.R
import st.teamcataly.lokalocalpartner.root.loggedin.Profile
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link OrdersScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class OrdersBuilder(dependency: ParentComponent) : ViewBuilder<OrdersView, OrdersRouter, OrdersBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [OrdersRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [OrdersRouter].
     */
    fun build(parentViewGroup: ViewGroup, profileIdOrderMap: Map<String, Pair<Profile, Order>>): OrdersRouter {
        val view = createView(parentViewGroup)
        val interactor = OrdersInteractor()
        interactor.setProfileIdOrderMap(profileIdOrderMap)
        val component = DaggerOrdersBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.ordersRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): OrdersView? {
        return inflater.inflate(R.layout.orders_rib, parentViewGroup, false) as OrdersView

    }

    interface ParentComponent {
        fun ordersListener(): OrdersInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @OrdersScope
        @Binds
        internal abstract fun presenter(view: OrdersView): OrdersInteractor.OrdersPresenter

        @dagger.Module
        companion object {

            @OrdersScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: OrdersView,
                    interactor: OrdersInteractor): OrdersRouter {
                return OrdersRouter(view, interactor, component)
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @OrdersScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<OrdersInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: OrdersInteractor): Builder

            @BindsInstance
            fun view(view: OrdersView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun ordersRouter(): OrdersRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class OrdersScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class OrdersInternal
}
