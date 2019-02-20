package client.presenters.displays;

import client.core.NodeBuilder;
import client.presenters.operations.Operation;
import client.presenters.operations.ShowNodeOperation;
import cosmos.presenters.Presenter;
import org.junit.Before;
import org.junit.Test;

import static cosmos.presenters.Operation.Context;
import static junit.framework.TestCase.*;

public class ExplorationDisplayTest {
	private Operation operation1;
	private Operation operation2;
	private Operation operation3;
	private Operation operationChild;

	@Before
	public void createOperations() {
		final Context context = new Context() {
			@Override
			public Presenter getCanvas() {
				return null;
			}

			@Override
			public cosmos.presenters.Operation getReferral() {
				return null;
			}
		};
		operation1 = new Operation(context) {
			@Override
			public String getDefaultLabel() {
				return "operation 1";
			}

			@Override
			public void doExecute() {
			}

			@Override
			public boolean disableButtonWhenExecute() {
				return false;
			}

			@Override
			public Type getType() {
				return null;
			}
		};
		operation2 = new Operation(context) {
			@Override
			public String getDefaultLabel() {
				return "operation 2";
			}

			@Override
			public void doExecute() {
			}

			@Override
			public boolean disableButtonWhenExecute() {
				return false;
			}

			@Override
			public Type getType() {
				return null;
			}
		};
		operation3 = new Operation(context) {
			@Override
			public String getDefaultLabel() {
				return "operation 3";
			}

			@Override
			public void doExecute() {
			}

			@Override
			public boolean disableButtonWhenExecute() {
				return false;
			}

			@Override
			public Type getType() {
				return null;
			}
		};
		operationChild = new Operation(context) {
			@Override
			public String getDefaultLabel() {
				return "operation child";
			}

			@Override
			public void doExecute() {
			}

			@Override
			public boolean disableButtonWhenExecute() {
				return false;
			}

			@Override
			public Type getType() {
				return null;
			}
		};
	}

	@Test
	public void testLoadOperations() {
		ExplorationDisplay display = createExplorationDisplay();

		assertEquals(((ExplorationDisplay.ExplorationItemDisplay)display.getChild(1)).getLabel(), "operation 2");
		assertEquals(display.childrenCount(), 3);
	}

	@Test
	public void testAddChildOperation() {
		ExplorationDisplay display = createExplorationDisplayWithChildren();

		assertEquals(display.get(operation1).get(0).getLabel(), "operation child");
	}

	@Test
	public void testAddDeeplyOperations() {
		ExplorationDisplay display = createExplorationDisplay();

		display.addDeeply(new Operation[]{
			new ShowNodeOperation(new Context() {
				@Override
				public Presenter getCanvas() {
					return null;
				}

				@Override
				public cosmos.presenters.Operation getReferral() {
					return null;
				}
			}, NodeBuilder.buildCollection(null, "Colección en profundidad", false), null),
			new ShowNodeOperation(new Context() {
				@Override
				public Presenter getCanvas() {
					return null;
				}

				@Override
				public cosmos.presenters.Operation getReferral() {
					return null;
				}
			}, NodeBuilder.buildContainer(null, "Contenedor en profundidad"), null),
			new ShowNodeOperation(new Context() {
				@Override
				public Presenter getCanvas() {
					return null;
				}

				@Override
				public cosmos.presenters.Operation getReferral() {
					return null;
				}
			}, NodeBuilder.buildForm(null, "Formulario en profundidad", false), null)
		});

		assertEquals(display.get(3).getLabel(), "Colección en profundidad");
		assertEquals(display.get(3).get(0).getLabel(), "Contenedor en profundidad");
		assertEquals(display.get(3).get(0).get(0).getLabel(), "Formulario en profundidad");
	}

	@Test
	public void testClearChildrenOperations() {
		ExplorationDisplay display = createExplorationDisplayWithChildren();

		assertEquals(display.get(operation1).childrenCount(), 1);
		display.get(operation1).clear();
		assertEquals(display.get(operation1).childrenCount(), 0);
	}

	@Test
	public void testActivate() {
		ExplorationDisplay display = createExplorationDisplayWithChildren();

		display.activate(operation2);

		assertEquals(display.getActive(), display.get(operation2));
	}

	@Test
	public void testGetChildOperation() {
		ExplorationDisplay display = createExplorationDisplayWithChildren();

		assertNotNull(display.get(operationChild));
	}

	@Test
	public void testLinkOperation() {
		ExplorationDisplay display = createExplorationDisplay();

		display.link(operation1);

		assertTrue(display.get(operation1).isLinked());
	}

	@Test
	public void testLinkChildOperation() {
		ExplorationDisplay display = createExplorationDisplayWithChildren();

		display.link(operationChild);

		assertTrue(display.get(operationChild).isLinked());
	}

	@Test
	public void testUnLinkOperation() {
		ExplorationDisplay display = createExplorationDisplay();

		display.link(operation1);
		display.unLink(operation1);

		assertFalse(display.get(operation1).isLinked());
	}

	@Test
	public void testUnLinkChildOperation() {
		ExplorationDisplay display = createExplorationDisplayWithChildren();

		display.link(operationChild);
		display.unLink(operationChild);

		assertFalse(display.get(operationChild).isLinked());
	}

	@Test
	public void testHook() {
		ExplorationDisplay display = createExplorationDisplay();
		final boolean[] updated = new boolean[1];
		final boolean[] activated = new boolean[1];
		final boolean[] linked = new boolean[1];

		display.addHook(new ExplorationDisplay.Hook() {
			@Override
			public void update() {
				updated[0] = true;
			}

			@Override
			public void activate(ExplorationDisplay.ExplorationItemDisplay item) {
				activated[0] = true;
			}

			@Override
			public void link(ExplorationDisplay.ExplorationItemDisplay item) {
				linked[0] = true;
			}

			@Override
			public void unLink(ExplorationDisplay.ExplorationItemDisplay item) {
				linked[0] = false;
			}
		});

		display.add(operation3);
		assertTrue(updated[0]);

		display.activate(operation2);
		assertTrue(activated[0]);

		display.link(operation1);
		assertTrue(linked[0]);

		display.unLink(operation1);
		assertFalse(linked[0]);
	}

	private ExplorationDisplay createExplorationDisplay() {
		ExplorationDisplay display = new ExplorationDisplay();

		display.add(operation1);
		display.add(operation2);
		display.add(operation3);

		return display;
	}

	private ExplorationDisplay createExplorationDisplayWithChildren() {
		ExplorationDisplay display = createExplorationDisplay();

		display.get(operation1).add(operationChild);

		return display;
	}

}
