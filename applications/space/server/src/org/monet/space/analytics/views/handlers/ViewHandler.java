package org.monet.space.analytics.views.handlers;


public class ViewHandler {
/*
	private UriFS uri;
	private ViewSerializer view = null;
	private ShowList showList = new ShowList();

	public ViewHandler(UriFS uri) {
		this.uri = uri;
	}

	public ShowList getShowList() {
		return showList;
	}

	public void load() throws ViewHandlerException {
		try {
			FileInputStream fileStream = new FileInputStream(uri);
			ViewInputStream stream = new ViewInputStream(fileStream);
			view = stream.read();
			deserializeShows();
			fileStream.close();
		}
		catch(RuntimeException e){throw e;}
		catch(Exception e){throw new ViewHandlerException("error when loading the definition");}
	}

	public void save(ShowList showList) throws ViewHandlerException {
		try {
			buildView(showList);
			FileOutputStream fileStream = new FileOutputStream(uri);
			ViewOutputStream stream = new ViewOutputStream(fileStream);
			stream.write(view);
			fileStream.close();
		}
		catch(RuntimeException e){throw e;}
		catch(Exception e){throw new ViewHandlerException("error when saving the definition");
		}
	}

	public static void create(UriFS uri) throws Exception {
		ViewOutputStream stream = new ViewOutputStream(new FileOutputStream(uri));
		stream.write(new ViewSerializer());
	}

	private void buildView(ShowList showList) {
		view = new ViewSerializer();
		view.setShowList(serializeShows(showList));
	}
	
	private void deserializeShows(){
		for (ShowXml showXml : view.getShowList())
			showList.add(new XmlShowTranslator().deserialize(showXml));
	}
	
	private ArrayList<ShowXml> serializeShows(ShowList showList) {
		ArrayList<ShowXml> showXmlList = new ArrayList<ShowXml>();
		for (Show show : showList)
			showXmlList.add((ShowXml) new XmlShowTranslator().serialize(show));
		return showXmlList;
	}
	
	public class ViewHandlerException extends Exception {

		private static final long serialVersionUID = 6648122624446761308L;

		public ViewHandlerException(String message) {
			super(ViewHandlerException.class.getName() + ":" + message);
		}
	}
	*/
}
