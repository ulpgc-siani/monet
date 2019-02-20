package cosmos.presenters;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PresenterTypeTest {
	@Test
	public void SimpleTest() {
		assertTrue(Menu.TYPE.is(Presenter.TYPE));
		assertTrue(Menu.TYPE.is(new Presenter.Type("Menu", Presenter.TYPE)));
		assertTrue(new Presenter.Type("MyMenu", Menu.TYPE).is(Menu.TYPE));
	}
}
