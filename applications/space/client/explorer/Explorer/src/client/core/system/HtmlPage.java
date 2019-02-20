package client.core.system;

public class HtmlPage implements client.core.model.HtmlPage {
	private String content;

	public HtmlPage() {
	}

	public HtmlPage(String content) {
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
