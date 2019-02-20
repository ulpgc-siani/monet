package client.widgets.popups.dialogs;

import client.widgets.toolbox.NavigableScrollPanel;

public class SectionNavigationController<Panel extends NavigableScrollPanel> {
	private final Panel[] sections;
    private int current = 0;

	public SectionNavigationController(Panel... sections) {
		this.sections = sections;
	}

    public void setOptionSelectedHandler(NavigableScrollPanel.OptionSelectedHandler handler) {
	    for (Panel section : sections)
		    section.setOptionSelectedHandler(handler);
    }

    public boolean hasSelection() {
        return getCurrentSection().hasSelection();
    }

	public <Option extends NavigableScrollPanel.Option> Option getSelectedOption() {
        return (Option) getCurrentSection().getSelectedOption();
    }

    public void noOptionSelected() {
	    getCurrentSection().noOptionSelected();
    }

    public boolean isFirstSelected() {
        return getCurrentSection().isFirstSelected();
    }

    public boolean isLastSelected() {
        return getCurrentSection().isLastSelected();
    }

    public void selectUpper() {
	    getCurrentSection().selectUpper();
    }

    public void selectBottom() {
	    getCurrentSection().selectBottom();
    }

    public void clear() {
	    for (Panel section : sections)
		    section.clear();
    }

    public void selectNextPanel() {
	    current++;

	    if (current >= sections.length)
		    current = 0;

	    getCurrentSection().selectBottom();
    }

	private Panel getCurrentSection() {
		return sections[current];
	}

}
