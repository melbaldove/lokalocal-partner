package st.teamcataly.lokalocalpartner.root.loggedin.neworder

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NewOrderRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: NewOrderBuilder.Component
  @Mock internal lateinit var interactor: NewOrderInteractor
  @Mock internal lateinit var view: NewOrderView

  private var router: NewOrderRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = NewOrderRouter(view, interactor, component)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use RouterHelper to drive your router's lifecycle.
    RouterHelper.attach(router!!)
    RouterHelper.detach(router!!)

    throw RuntimeException("Remove this test and add real tests.")
  }

}

