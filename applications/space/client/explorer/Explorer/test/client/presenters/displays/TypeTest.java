package client.presenters.displays;

import client.core.NodeBuilder;
import client.core.ViewBuilder;
import client.presenters.displays.view.CollectionViewDisplay;
import client.presenters.displays.view.NodeViewDisplay;
import client.presenters.displays.view.SetViewDisplay;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TypeTest {

	@Test
	public void testSimpleType() {
		CollectionViewDisplay display = createCollectionViewDisplay();

		assertEquals(true, display.is(CollectionViewDisplay.TYPE));
	}

	@Test
	public void testTypeHierarchy() {
		CollectionViewDisplay display = createCollectionViewDisplay();

		assertEquals(true, display.is(CollectionViewDisplay.TYPE));
		assertEquals(true, display.is(SetViewDisplay.TYPE));
		assertEquals(true, display.is(NodeViewDisplay.TYPE));
	}

	private CollectionViewDisplay createCollectionViewDisplay() {
		return new CollectionViewDisplay(NodeBuilder.buildCollection(null, null, false), ViewBuilder.buildCollectionView("001", "Test", null, false));
	}

}
