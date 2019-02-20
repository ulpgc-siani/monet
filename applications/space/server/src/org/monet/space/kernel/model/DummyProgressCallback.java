package org.monet.space.kernel.model;

public class DummyProgressCallback implements ProgressCallback {

	@Override
	public void onProgressUpdate(Progress progress) {
	}

	@Override
	public void onFailure(ImportError error) {
	}

	@Override
	public void onComplete(Progress progress) {
	}

	@Override
	public boolean isCanceled() {
		return false;
	}

}
