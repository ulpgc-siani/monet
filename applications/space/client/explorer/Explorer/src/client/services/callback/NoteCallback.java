package client.services.callback;

public interface NoteCallback extends Callback<NoteCallback.Result> {

	interface Result {
		String getName();
		String getValue();
	}

}
