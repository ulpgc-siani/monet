import cosmos.presenters.Holder;
import cosmos.presenters.Menu;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PresenterHolderTest {

	@Test
	public void simpleTest() {
        assertTrue(holder("Menu").canHold(new Menu()));
	}

    @Test
	public void inheritanceTest() {
        Presenter saveOperation = mock(Operation.class);
        when(saveOperation.getType()).thenReturn(new Presenter.Type("Save", Operation.TYPE));
		assertTrue(holder("Operation").canHold(saveOperation));
		assertFalse(holder("Menu").canHold(saveOperation));
	}

    private Holder holder(final String name) {
        Holder holder = mock(Holder.class);
        doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return ((Presenter) invocation.getArguments()[0]).is(new Presenter.Type(name, null));
            }
        }).when(holder).canHold(any(Presenter.class));
        return holder;
    }
}
