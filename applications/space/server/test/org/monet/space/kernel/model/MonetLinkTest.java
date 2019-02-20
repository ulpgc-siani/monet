package org.monet.space.kernel.model;

import org.monet.space.kernel.model.MonetLink.Type;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;

public class MonetLinkTest {

	@org.junit.Test
	public void testParseSimpleLink() throws Exception {
		MonetLink link = MonetLink.from("ml://node.1");
		assertNotNull("Link is not null", link);
		assertThat("Link entity identifier", link.getId(), is("1"));
		assertThat("Link entity type is", link.getType(), is(Type.Node));
		assertNull("Link view", link.getView());
		assertFalse("Link mode", link.isEditMode());
	}

	@org.junit.Test
	public void testParseLinkInEditMode() throws Exception {
		MonetLink link = MonetLink.from("ml://node.1/edit");
		assertNotNull("Link is not null", link);
		assertThat("Link entity identifier", link.getId(), is("1"));
		assertThat("Link entity type is", link.getType(), is(Type.Node));
		assertNull("Link view", link.getView());
		assertTrue("Link mode", link.isEditMode());
	}

	@org.junit.Test
	public void testParseLinkWithView() throws Exception {
		MonetLink link = MonetLink.from("ml://node.1.view.2");
		assertNotNull("Link is not null", link);
		assertThat("Link entity identifier", link.getId(), is("1"));
		assertThat("Link entity type is", link.getType(), is(Type.Node));
		assertThat("Link view", link.getView(), is("2"));
		assertFalse("Link mode", link.isEditMode());
	}

	@org.junit.Test
	public void testParseLinkWithViewInEditMode() throws Exception {
		MonetLink link = MonetLink.from("ml://node.1.view.2/edit");
		assertNotNull("Link is not null", link);
		assertThat("Link entity identifier", link.getId(), is("1"));
		assertThat("Link entity type is", link.getType(), is(Type.Node));
		assertThat("Link view", link.getView(), is("2"));
		assertTrue("Link mode", link.isEditMode());
	}

}