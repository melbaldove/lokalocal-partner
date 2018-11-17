package st.teamcataly.lokalocalpartner.root.loggedin

import com.uber.rib.core.Builder
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.InteractorBaseComponent
import java.lang.annotation.Retention

import javax.inject.Qualifier
import javax.inject.Scope

import dagger.Provides
import dagger.BindsInstance
import st.teamcataly.lokalocalpartner.root.LokaLocalApi

import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersBuilder

import java.lang.annotation.RetentionPolicy.CLASS
import st.teamcataly.lokalocalpartner.root.RootView
import st.teamcataly.lokalocalpartner.root.loggedin.neworder.NewOrderBuilder
import st.teamcataly.lokalocalpartner.root.loggedin.neworder.NewOrderInteractor
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersInteractor


class LoggedInBuilder(dependency: ParentComponent) : Builder<LoggedInRouter, LoggedInBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [LoggedInRouter].
     *
     * @return a new [LoggedInRouter].
     */
    fun build(partner: Partner): LoggedInRouter {
        val interactor = LoggedInInteractor()
        interactor.setPartner(partner)
        val component = DaggerLoggedInBuilder_Component.builder()
                .parentComponent(dependency)
                .interactor(interactor)
                .build()

        return component.loggedinRouter()
    }

    interface ParentComponent {
        fun lokaLocalApi(): LokaLocalApi
        fun rootView(): RootView
    }


    @dagger.Module
    abstract class Module {

        @dagger.Module
        companion object {

            @LoggedInScope
            @Provides
            @JvmStatic
            internal fun presenter(): EmptyPresenter {
                return EmptyPresenter()
            }

            @LoggedInScope
            @Provides
            @JvmStatic
            internal fun router(component: Component, interactor: LoggedInInteractor, rootView: RootView): LoggedInRouter {
                return LoggedInRouter(interactor, component, rootView, OrdersBuilder(component), NewOrderBuilder(component))
            }

            @LoggedInScope
            @Provides
            @JvmStatic
            internal fun ordersListener(interactor: LoggedInInteractor): OrdersInteractor.Listener {
                return interactor.OrdersListener()
            }

            @LoggedInScope
            @Provides
            @JvmStatic
            internal fun newOrderListener(interactor: LoggedInInteractor): NewOrderInteractor.Listener {
                return interactor.NewOrderListener()
            }
        }
    }


    @LoggedInScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<LoggedInInteractor>, BuilderComponent, OrdersBuilder.ParentComponent, NewOrderBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: LoggedInInteractor): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }

    }

    interface BuilderComponent {
        fun loggedinRouter(): LoggedInRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class LoggedInScope


    @Qualifier
    @Retention(CLASS)
    internal annotation class LoggedInInternal
}
