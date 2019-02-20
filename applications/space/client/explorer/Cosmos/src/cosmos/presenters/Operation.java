package cosmos.presenters;

import cosmos.model.ServerError;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Operation extends Presenter {
	protected final Context context;
	protected boolean enabled = true;
	protected boolean visible = true;
	public static final Type TYPE = new Type("Operation", Presenter.TYPE);

	protected Operation(Context context) {
		this.context = context;
	}

	public final void execute() {
		beforeExecute();
		doExecute();
	}

	protected abstract void beforeExecute();

	protected abstract void doExecute();

	public boolean disableButtonWhenExecute() {
		return false;
	}

	public boolean enabled() {
		return enabled;
	}

	public void setEnabled(boolean value) {
		if (value)
			enable();
		else
			disable();
	}

	public boolean visible() {
		return visible;
	}

	public void enable() {
		enabled = true;
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.enable();
			}
		});
	}

	public void disable() {
		enabled = false;
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.disable();
			}
		});
	}

	public void setVisible(boolean value) {
		if (value)
			show();
		else
			hide();
	}

	public void show() {
		visible = true;
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.show();
			}
		});
	}

	public void hide() {
		visible = false;
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.hide();
			}
		});
	}

	protected void executePerformed() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.executePerformed();
			}
		});
	}

	protected void executeFailed(final ServerError details) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not execute operation " + this.getType().toString() + ". Reason: " + details.getContent());
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.executeFailed(details);
			}
		});
	}

	public interface Hook extends Presenter.Hook {
		void show();
		void hide();
		void enable();
		void disable();
		void executePerformed();
		void executeFailed(ServerError details);
	}

	public interface Context {
		<T extends Presenter> T getCanvas();
		Operation getReferral();
	}

	public static class NullContext implements Context {
		@Override
		public Presenter getCanvas() {
			return null;
		}

		@Override
		public Operation getReferral() {
			return null;
		}
	}

}
