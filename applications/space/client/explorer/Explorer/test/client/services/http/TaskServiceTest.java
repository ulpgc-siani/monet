package client.services.http;

import client.core.model.Task;
import client.core.model.TaskList;
import client.services.callback.TaskCallback;
import client.services.callback.TaskListCallback;
import org.junit.Test;

public class TaskServiceTest extends ServiceTest {

	@Test
	public void testOpenTask() {
		final TaskService service = createService();

		service.open("1", new TaskCallback() {
			@Override
			public void success(Task object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	@Test
	public void testOpenTaskList() {
		final TaskService service = createService();

		service.open(new TaskListCallback() {
			@Override
			public void success(TaskList object) {
				System.out.println(object);
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	@Override
	protected <T extends client.services.Service> T createService() {
		return (T)new client.services.http.TaskService(createStub(), createServices());
	}
}