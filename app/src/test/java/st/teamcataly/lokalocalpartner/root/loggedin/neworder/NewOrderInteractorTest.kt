package st.teamcataly.lokalocalpartner.root.loggedin.neworder

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NewOrderInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: NewOrderInteractor.NewOrderPresenter
  @Mock internal lateinit var router: NewOrderRouter

  private var interactor: NewOrderInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestNewOrderInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<NewOrderInteractor.NewOrderPresenter, NewOrderRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}