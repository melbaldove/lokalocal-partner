package st.teamcataly.lokalocalpartner.root.loggedin.neworder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import st.teamcataly.lokalocalpartner.R
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersInteractor
import st.teamcataly.lokalocalpartner.root.loggedin.orders.OrdersView
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link NewOrderScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class NewOrderBuilder(dependency: ParentComponent) : ViewBuilder<NewOrderView, NewOrderRouter, NewOrderBuilder.ParentComponent>(dependency) {

  /**
   * Builds a new [NewOrderRouter].
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new [NewOrderRouter].
   */
  fun build(parentViewGroup: ViewGroup): NewOrderRouter {
    val view = createView(parentViewGroup)
    val interactor = NewOrderInteractor()
    val component = DaggerNewOrderBuilder_Component.builder()
        .parentComponent(dependency)
        .view(view)
        .interactor(interactor)
        .build()
    return component.neworderRouter()
  }

  override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): NewOrderView? {
    return inflater.inflate(R.layout.new_order_rib, parentViewGroup, false) as NewOrderView
  }

  interface ParentComponent {
    fun newOrderListener(): NewOrderInteractor.Listener
  }

  @dagger.Module
  abstract class Module {

    @NewOrderScope
    @Binds
    internal abstract fun presenter(view: NewOrderView): NewOrderInteractor.NewOrderPresenter

    @dagger.Module
    companion object {

      @NewOrderScope
      @Provides
      @JvmStatic
      internal fun router(
          component: Component,
          view: NewOrderView,
          interactor: NewOrderInteractor): NewOrderRouter {
        return NewOrderRouter(view, interactor, component)
      }
    }

    // TODO: Create provider methods for dependencies created by this Rib. These should be static.
  }

  @NewOrderScope
  @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
  interface Component : InteractorBaseComponent<NewOrderInteractor>, BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      fun interactor(interactor: NewOrderInteractor): Builder

      @BindsInstance
      fun view(view: NewOrderView): Builder

      fun parentComponent(component: ParentComponent): Builder
      fun build(): Component
    }
  }

  interface BuilderComponent {
    fun neworderRouter(): NewOrderRouter
  }

  @Scope
  @Retention(CLASS)
  internal annotation class NewOrderScope

  @Qualifier
  @Retention(CLASS)
  internal annotation class NewOrderInternal
}
