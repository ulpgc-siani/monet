package client.services.http;

import client.core.model.Task;
import client.core.model.User;
import client.core.model.workmap.DelegationAction;
import client.core.model.workmap.LineAction;
import client.services.Services;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.DayOfWeek;
import cosmos.utils.date.DateFormatter;

public class TranslatorService extends HttpService implements client.services.TranslatorService {

	public TranslatorService(Stub stub, Services services) {
		super(stub, services);
	}

	@Override
	public String getTaskAssignedToMeMessage() {
		return "Ha sido asignada a mí";
	}

	@Override
	public String getTaskAssignedToMeBySenderMessage(User sender) {
		return "Me la ha asignado " + sender.getFullName();
	}

	@Override
	public String getTaskAssignedToUserMessage(User owner) {
		return "Ha sido asignada a " + owner.getFullName();
	}

	@Override
	public String getTaskAssignedToUserBySenderMessage(User owner, User sender) {
		return "Ha sido asignada a " + owner.getFullName() + " por " + sender.getFullName();
	}

	@Override
	public String getTaskDateMessage(Date createDate, Date updateDate) {
		return "Creada ayer a las 10:00. Actualizada hoy a las 9:00";
	}

	@Override
	public String getTaskNotificationsMessage(int count) {
		if (count == 0)
			return null;

		if (count == 1)
			return "Hay 1 mensaje nuevo";

		if (count <= 10)
			return "Hay " + count + " mensajes nuevos";

		return "Hay muchos mensajes nuevos";
	}

	@Override
	public String getTaskDelegationMessage(DelegationAction.Message message, Date failureDate, String roleTypeLabel) {

		if (message == DelegationAction.Message.SETUP_ROLE)
			return "Selecciona el cliente al cual enviar el encargo:";

		if (message == DelegationAction.Message.NO_ROLES)
			return "No se ha encontrado ningún role que permita realizar esta acción. Vaya a la vista de roles y asigne el role " + roleTypeLabel + " a algún usuario.";

		if (message == DelegationAction.Message.FAILURE_INTERNAL)
			return "Esta tarea no soporta delegar el encargo a un usuario de la unidad de negocio";

		if (message == DelegationAction.Message.FAILURE_EXTERNAL)
			return "Esta tarea no soporta delegar el encargo a un usuario externo";

		if (message == DelegationAction.Message.SETUP_ORDER)
			return "Configura el encargo de " + roleTypeLabel;

		if (message == DelegationAction.Message.SENDING)
			return "Enviando al proveedor seleccionado...";

		if (message == DelegationAction.Message.SENDING_FAILURE)
			return "Intentando enviar el encargo. El último intento de envío fue realizado " + translateFullDate(failureDate) + ".";

		return null;
	}

	@Override
	public String getTaskLineTimeoutMessage(Date timeout, LineAction.Stop timeoutStop) {
		if (timeout == null || timeoutStop == null)
			return "";

		return "Se seleccionará " + timeoutStop.getLabel() + " si no se elije una opción antes de " + translateFullDate(timeout);
	}

	@Override
	public String getTaskWaitMessage(Date dueDate) {
		if (dueDate == null)
			return "";

		return "Esperando hasta " + translateFullDate(dueDate) + "...";
	}

	@Override
	public String getTaskStateLabel(Task.State state) {
		if (state == Task.State.NEW)
			return "nueva";
		if (state == Task.State.PENDING)
			return "pendiente";
		if (state == Task.State.WAITING)
			return "en espera";
		if (state == Task.State.FINISHED)
			return "finalizada";
		if (state == Task.State.FAILURE)
			return "con fallos";
		if (state == Task.State.ABORTED)
			return "abortada";
		if (state == Task.State.EXPIRED)
			return "caducada";
		return null;
	}

	@Override
	public String getTaskTypeIcon(Task.Type type) {
		return getConfiguration().getImagesPath() + "/task-type-" + type + ".png";
	}

	@Override
	public String getCurrentLanguage() {
		return "es";
	}

	@Override
	public String translate(Object name) {

		if (name.equals(Label.SEARCH) || name.equals(Label.SEARCH.toString())) return "Búsqueda";
		if (name.equals(Label.SEARCH_RESULTS_FOR) || name.equals(Label.SEARCH_RESULTS_FOR.toString())) return "Resultados de búsqueda";
		if (name.equals(Label.DELETE) || name.equals(Label.DELETE.toString())) return "Borrar";
		if (name.equals(Label.PUBLISH) || name.equals(Label.PUBLISH.toString())) return "Publicar";
		if (name.equals(Label.LIST_EMPTY) || name.equals(Label.LIST_EMPTY.toString())) return "No existen elementos";
		if (name.equals(Label.LOADING) || name.equals(Label.LOADING.toString())) return "Cargando...";
		if (name.equals(Label.TITLE) || name.equals(Label.TITLE.toString())) return "Título";
		if (name.equals(Label.TASK_LIST) || name.equals(Label.TASK_LIST.toString())) return "Tareas";
		if (name.equals(Label.NEWS) || name.equals(Label.NEWS.toString())) return "Noticias";
		if (name.equals(Label.TRASH) || name.equals(Label.TRASH.toString())) return "Papelera";
		if (name.equals(Label.HISTORY) || name.equals(Label.HISTORY.toString())) return "Historial";
		if (name.equals(Label.BACK) || name.equals(Label.BACK.toString())) return "Volver";
		if (name.equals(Label.UP) || name.equals(Label.UP.toString())) return "Subir";
		if (name.equals(Label.PREVIOUS) || name.equals(Label.PREVIOUS.toString())) return "Anterior";
		if (name.equals(Label.NEXT) || name.equals(Label.NEXT.toString())) return "Siguiente";
		if (name.equals(Label.LINK) || name.equals(Label.LINK.toString())) return "Anclar";
		if (name.equals(Label.MEMBERS) || name.equals(Label.MEMBERS.toString())) return "unidades de negocio";
		if (name.equals(Label.EMPTY_MEMBERS) || name.equals(Label.EMPTY_MEMBERS.toString())) return "no hay unidades de negocio disponibles";
		if (name.equals(Label.NOTIFICATIONS) || name.equals(Label.NOTIFICATIONS.toString())) return "avisos";
		if (name.equals(Label.EMPTY_NOTIFICATIONS) || name.equals(Label.EMPTY_NOTIFICATIONS.toString())) return "No hay avisos nuevos. Puede ver toda la actividad de su unidad accediendo a las noticias.";
		if (name.equals(Label.SHOW_LIST_ITEMS) || name.equals(Label.SHOW_LIST_ITEMS.toString())) return "Mostrar...";
		if (name.equals(Label.SHOW_SECTION) || name.equals(Label.SHOW_SECTION.toString())) return "mostrar elementos";
		if (name.equals(Label.HIDE_SECTION) || name.equals(Label.HIDE_SECTION.toString())) return "ocultar elementos";
		if (name.equals(Label.YES) || name.equals(Label.YES.toString())) return "Si";
		if (name.equals(Label.NO) || name.equals(Label.NO.toString())) return "No";
		if (name.equals(Label.OTHER) || name.equals(Label.OTHER.toString())) return "Otro";
		if (name.equals(Label.YEAR) || name.equals(Label.YEAR.toString())) return "Año";
		if (name.equals(Label.MONTH) || name.equals(Label.MONTH.toString())) return "Mes";
		if (name.equals(Label.DAY) || name.equals(Label.DAY.toString())) return "Día";
		if (name.equals(Label.HOUR) || name.equals(Label.HOUR.toString())) return "Hora";
		if (name.equals(Label.MINUTE) || name.equals(Label.MINUTE.toString())) return "Minuto";
		if (name.equals(Label.SECOND) || name.equals(Label.SECOND.toString())) return "Segundo";
		if (name.equals(Label.MONDAY) || name.equals(Label.MONDAY.toString())) return "L";
		if (name.equals(Label.TUESDAY) || name.equals(Label.TUESDAY.toString())) return "M";
		if (name.equals(Label.WEDNESDAY) || name.equals(Label.WEDNESDAY.toString())) return "X";
		if (name.equals(Label.THURSDAY) || name.equals(Label.THURSDAY.toString())) return "J";
		if (name.equals(Label.FRIDAY) || name.equals(Label.FRIDAY.toString())) return "V";
		if (name.equals(Label.SATURDAY) || name.equals(Label.SATURDAY.toString())) return "S";
		if (name.equals(Label.SUNDAY) || name.equals(Label.SUNDAY.toString())) return "D";
		if (name.equals(Label.DATE_PARSER) || name.equals(Label.DATE_PARSER.toString())) return "Escriba aquí la fecha";
		if (name.equals(Label.ONE_HOUR) || name.equals(Label.ONE_HOUR.toString())) return "una hora";
		if (name.equals(Label.ONE_DAY) || name.equals(Label.ONE_DAY.toString())) return "un día";
		if (name.equals(Label.ONE_MONTH) || name.equals(Label.ONE_MONTH.toString())) return "un mes";
		if (name.equals(Label.ONE_YEAR) || name.equals(Label.ONE_YEAR.toString())) return "un año";
		if (name.equals(Label.INCREMENT_ONE_HOUR) || name.equals(Label.INCREMENT_ONE_HOUR.toString())) return "incrementar una hora";
		if (name.equals(Label.INCREMENT_ONE_DAY) || name.equals(Label.INCREMENT_ONE_DAY.toString())) return "incrementar un día";
		if (name.equals(Label.INCREMENT_ONE_MONTH) || name.equals(Label.INCREMENT_ONE_MONTH.toString())) return "incrementar un mes";
		if (name.equals(Label.INCREMENT_ONE_YEAR) || name.equals(Label.INCREMENT_ONE_YEAR.toString())) return "incrementar un año";
		if (name.equals(Label.DECREMENT_ONE_HOUR) || name.equals(Label.DECREMENT_ONE_HOUR.toString())) return "decrementar una hora";
		if (name.equals(Label.DECREMENT_ONE_DAY) || name.equals(Label.DECREMENT_ONE_DAY.toString())) return "decrementar un día";
		if (name.equals(Label.DECREMENT_ONE_MONTH) || name.equals(Label.DECREMENT_ONE_MONTH.toString())) return "decrementar un mes";
		if (name.equals(Label.DECREMENT_ONE_YEAR) || name.equals(Label.DECREMENT_ONE_YEAR.toString())) return "decrementar un año";
		if (name.equals(Label.NO_VALUE) || name.equals(Label.NO_VALUE.toString())) return "Sin valor";
		if (name.equals(Label.WELCOME) || name.equals(Label.WELCOME.toString())) return "Bienvenido";
		if (name.equals(Label.CREATE_DATE) || name.equals(Label.CREATE_DATE.toString())) return "Fecha de creación";
		if (name.equals(Label.UPDATE_DATE) || name.equals(Label.UPDATE_DATE.toString())) return "Fecha de actualización";
		if (name.equals(Label.URGENT) || name.equals(Label.URGENT.toString())) return "Urgencia";
		if (name.equals(Label.ALL) || name.equals(Label.ALL.toString())) return "todas";
		if (name.equals(Label.ALIVE) || name.equals(Label.ALIVE.toString())) return "en ejecución";
		if (name.equals(Label.ACTIVE) || name.equals(Label.ACTIVE.toString())) return "activas";
		if (name.equals(Label.PENDING) || name.equals(Label.PENDING.toString())) return "pendientes";
		if (name.equals(Label.FINISHED) || name.equals(Label.FINISHED.toString())) return "finalizadas";
		if (name.equals(Label.STATE) || name.equals(Label.STATE.toString())) return "Estado";
		if (name.equals(Label.PREVIEW) || name.equals(Label.PREVIEW.toString())) return "Vista previa";
		if (name.equals(Label.DOWNLOAD) || name.equals(Label.DOWNLOAD.toString())) return "descargar";
		if (name.equals(Label.NO_LABEL) || name.equals(Label.NO_LABEL.toString())) return "sin etiqueta";
		if (name.equals(Label.IMAGE_TOO_BIG) || name.equals(Label.IMAGE_TOO_BIG.toString())) return "La imagen es demasiado grande y necesita recortarse.";
		if (name.equals(Label.ADD_NODE) || name.equals(Label.ADD_NODE.toString())) return "añadir elemento";
		if (name.equals(Label.ADD_COMPOSITE_NODE) || name.equals(Label.ADD_COMPOSITE_NODE.toString())) return "añadir elemento...";
		if (name.equals(Label.OPTIONS) || name.equals(Label.OPTIONS.toString())) return "opciones";
		if (name.equals(Label.RECENT_OPTIONS) || name.equals(Label.RECENT_OPTIONS.toString())) return "historial";
		if (name.equals(Label.ADD_ORDER) || name.equals(Label.ADD_ORDER.toString())) return "Indique una condición para ordenar...";
		if (name.equals(Label.HELPER) || name.equals(Label.HELPER.toString())) return "ayuda";
		if (name.equals(Label.SHOW_ELEMENT) || name.equals(Label.SHOW_ELEMENT.toString())) return "Ver elemento";
		if (name.equals(Label.CLICK_TO_CONFIRM) || name.equals(Label.CLICK_TO_CONFIRM.toString())) return "Haga click en caso afirmativo";
		if (name.equals(Label.DOCUMENT_LOAD_SECONDS_REMAINING) || name.equals(Label.DOCUMENT_LOAD_SECONDS_REMAINING.toString())) return "El documento se está procesando, tiempo estimado para terminar ::TIME:: segundos.";
		if (name.equals(Label.SEARCHING) || name.equals(Label.SEARCHING.toString())) return "buscando...";
		if (name.equals(Label.DESCRIPTION) || name.equals(Label.DESCRIPTION.toString())) return "Descripción";

		if (name.equals(OperationLabel.ADD) || name.equals(OperationLabel.ADD.toString())) return "Añadir";
		if (name.equals(OperationLabel.DELETE_SELECTION) || name.equals(OperationLabel.DELETE_SELECTION.toString())) return "borrar selección";
		if (name.equals(OperationLabel.SHOW_NOTIFICATION_LIST) || name.equals(OperationLabel.SHOW_NOTIFICATION_LIST.toString())) return "mostrar notificaciones";
		if (name.equals(OperationLabel.SHOW_BUSINESS_UNIT_LIST) || name.equals(OperationLabel.SHOW_BUSINESS_UNIT_LIST.toString())) return "mostrar unidades de negocio";
		if (name.equals(OperationLabel.SHOW_NEWS) || name.equals(OperationLabel.SHOW_NEWS.toString())) return "mostrar noticias";
		if (name.equals(OperationLabel.LOGOUT) || name.equals(OperationLabel.LOGOUT.toString())) return "cerrar sesión";
		if (name.equals(OperationLabel.TOGGLE_LAYOUT_MODE) || name.equals(OperationLabel.TOGGLE_LAYOUT_MODE.toString())) return "cambiar vista";
		if (name.equals(OperationLabel.TOGGLE_TASK_URGENT) || name.equals(OperationLabel.TOGGLE_TASK_URGENT.toString())) return "Marcar la tarea como urgente/no urgente";
		if (name.equals(OperationLabel.UPLOAD) || name.equals(OperationLabel.UPLOAD.toString())) return "Subir este archivo";
		if (name.equals(OperationLabel.UPLOADING) || name.equals(OperationLabel.UPLOADING.toString())) return "Subiendo...";
		if (name.equals(OperationLabel.SET_TASKS_OWNER_SELECTION) || name.equals(OperationLabel.SET_TASKS_OWNER_SELECTION.toString())) return "Asignar tareas...";
		if (name.equals(OperationLabel.UN_SET_TASKS_OWNER_SELECTION) || name.equals(OperationLabel.UN_SET_TASKS_OWNER_SELECTION.toString())) return "Desasignar tareas...";
		if (name.equals(OperationLabel.UN_SET_TASK_OWNER) || name.equals(OperationLabel.UN_SET_TASK_OWNER.toString())) return "desasignar tarea";
		if (name.equals(OperationLabel.ABORT_TASK) || name.equals(OperationLabel.ABORT_TASK.toString())) return "abortar tarea";
		if (name.equals(OperationLabel.REFRESH_TASK) || name.equals(OperationLabel.REFRESH_TASK.toString())) return "actualizar tarea";
		if (name.equals(OperationLabel.REFRESH_ACCOUNT) || name.equals(OperationLabel.REFRESH_ACCOUNT.toString())) return "actualizar perfil";
		if (name.equals(OperationLabel.SETUP_TASK_DELEGATION) || name.equals(OperationLabel.SETUP_TASK_DELEGATION.toString())) return "continuar";
		if (name.equals(OperationLabel.SHOW_COMPOSITE_FIELDS) || name.equals(OperationLabel.SHOW_COMPOSITE_FIELDS.toString())) return "Marcar/Desmarcar";
		if (name.equals(OperationLabel.SHOW_MORE_FIELDS) || name.equals(OperationLabel.SHOW_MORE_FIELDS.toString())) return "Ver más";
		if (name.equals(OperationLabel.SHOW_LESS_FIELDS) || name.equals(OperationLabel.SHOW_LESS_FIELDS.toString())) return "Ver menos";
		if (name.equals(OperationLabel.SELECT_TASK_DELEGATION_ROLE) || name.equals(OperationLabel.SELECT_TASK_DELEGATION_ROLE.toString())) return "seleccionar role";
		if (name.equals(OperationLabel.SOLVE_TASK_LINE) || name.equals(OperationLabel.SOLVE_TASK_LINE.toString())) return "elegir opción";
		if (name.equals(OperationLabel.SOLVE_TASK_EDITION) || name.equals(OperationLabel.SOLVE_TASK_EDITION.toString())) return "continuar";
		if (name.equals(OperationLabel.SHOW_HOME) || name.equals(OperationLabel.SHOW_HOME.toString())) return "ir al inicio";
		if (name.equals(OperationLabel.SHOW_TASK_VIEW) || name.equals(OperationLabel.SHOW_TASK_VIEW.toString())) return "actualizar";
		if (name.equals(OperationLabel.CROP_IMAGE) || name.equals(OperationLabel.CROP_IMAGE.toString())) return "Recortar";
		if (name.equals(OperationLabel.RESTORE) || name.equals(OperationLabel.RESTORE.toString())) return "Restaurar";
		if (name.equals(OperationLabel.CLOSE_DIALOG) || name.equals(OperationLabel.CLOSE_DIALOG.toString())) return "cerrar";
		if (name.equals(OperationLabel.CONFIRM) || name.equals(OperationLabel.CONFIRM.toString())) return "Confirmar";
		if (name.equals(OperationLabel.CONFIRM_DELETE_NODE) || name.equals(OperationLabel.CONFIRM_DELETE_NODE.toString())) return "¿Está seguro que desea borrar el elemento?";
		if (name.equals(OperationLabel.CONFIRM_DELETE_NODES) || name.equals(OperationLabel.CONFIRM_DELETE_NODES.toString())) return "¿Está seguro que desea borrar los elementos seleccionados?";
		if (name.equals(OperationLabel.ACCEPT) || name.equals(OperationLabel.ACCEPT.toString())) return "aceptar";
		if (name.equals(OperationLabel.CANCEL) || name.equals(OperationLabel.CANCEL.toString())) return "cancelar";
		if (name.equals(OperationLabel.TOGGLE_HELPER) || name.equals(OperationLabel.TOGGLE_HELPER.toString())) return "mostrar/ocultar la ayuda del objeto";
		if (name.equals(OperationLabel.HIDE_HELPER) || name.equals(OperationLabel.HIDE_HELPER.toString())) return "cerrar";

		if (name.equals(ListLabel.MODE_LIST) || name.equals(ListLabel.MODE_LIST.toString())) return "Mostrar los elementos como lista";
		if (name.equals(ListLabel.MODE_ICON) || name.equals(ListLabel.MODE_ICON.toString())) return "Mostrar los elementos como iconos";
		if (name.equals(ListLabel.ALL) || name.equals(ListLabel.ALL.toString())) return "todos";
		if (name.equals(ListLabel.MORE_THAN_ONE) || name.equals(ListLabel.MORE_THAN_ONE.toString())) return "muchos";
		if (name.equals(ListLabel.FILTER_MESSAGE_WHEN_EMPTY) || name.equals(ListLabel.FILTER_MESSAGE_WHEN_EMPTY.toString())) return "indique aquí una condición para filtrar...";
		if (name.equals(ListLabel.MESSAGE_WHEN_EMPTY) || name.equals(ListLabel.MESSAGE_WHEN_EMPTY.toString())) return "no existen elementos";
		if (name.equals(ListLabel.SELECT_ELEMENT) || name.equals(ListLabel.SELECT_ELEMENT.toString())) return "seleccionar elemento";
		if (name.equals(ListLabel.SELECT_ALL) || name.equals(ListLabel.SELECT_ALL.toString())) return "seleccionar todos";
		if (name.equals(ListLabel.SELECT_NONE) || name.equals(ListLabel.SELECT_NONE.toString())) return "quitar selección";
		if (name.equals(ListLabel.SELECT_INVERT) || name.equals(ListLabel.SELECT_INVERT.toString())) return "invertir selección";
		if (name.equals(ListLabel.DELETE) || name.equals(ListLabel.DELETE.toString())) return "borrar elemento";

		if (name.equals(ErrorLabel.TITLE) || name.equals(ErrorLabel.TITLE.toString())) return "Error";
		if (name.equals(ErrorLabel.USER_NOT_LOGGED) || name.equals(ErrorLabel.USER_NOT_LOGGED.toString())) return "Se ha perdido la sesión. A continuación, se cerrará la aplicación...";
		if (name.equals(ErrorLabel.TEXT_FIELD_LENGTH) || name.equals(ErrorLabel.TEXT_FIELD_LENGTH.toString())) return "Tamaño del dato incorrecto. No se guardará el valor";
		if (name.equals(ErrorLabel.TEXT_FIELD_PATTERN) || name.equals(ErrorLabel.TEXT_FIELD_PATTERN.toString())) return "El valor no se corresponde con el formato esperado. No se guardará el valor";
		if (name.equals(ErrorLabel.DATE_FIELD_RANGE) || name.equals(ErrorLabel.DATE_FIELD_RANGE.toString())) return "Fecha fuera de rango. No se guardará el valor";
		if (name.equals(ErrorLabel.SOURCE_OPTIONS) || name.equals(ErrorLabel.SOURCE_OPTIONS.toString())) return "No se ha podido cargar las opciones";
		if (name.equals(ErrorLabel.INDEX_OPTIONS) || name.equals(ErrorLabel.INDEX_OPTIONS.toString())) return "No se ha podido cargar las opciones";
		if (name.equals(ErrorLabel.UPLOADING) || name.equals(ErrorLabel.UPLOADING.toString())) return "No se ha podido subir el fichero";
		if (name.equals(ErrorLabel.SAVE_VALUE) || name.equals(ErrorLabel.SAVE_VALUE.toString())) return "No se ha podido guardar el valor del campo";
		if (name.equals(ErrorLabel.ADD_FIELD) || name.equals(ErrorLabel.ADD_FIELD.toString())) return "No se ha podido añadir el campo";
		if (name.equals(ErrorLabel.CHANGE_FIELD_ORDER) || name.equals(ErrorLabel.CHANGE_FIELD_ORDER.toString())) return "No se ha podido cambiar el orden de los campos";
		if (name.equals(ErrorLabel.DELETE_FIELD) || name.equals(ErrorLabel.DELETE_FIELD.toString())) return "No se ha podido borrar el campo";
		if (name.equals(ErrorLabel.CLEAR_FIELD) || name.equals(ErrorLabel.CLEAR_FIELD.toString())) return "No se ha podido vacíar el campo";
		if (name.equals(ErrorLabel.FILE_SIZE) || name.equals(ErrorLabel.FILE_SIZE.toString())) return "El fichero es más grande de lo permitido.";
		if (name.equals(ErrorLabel.MAX_NUMBER_OF_FIELDS) || name.equals(ErrorLabel.MAX_NUMBER_OF_FIELDS.toString())) return "El campo no admite más valores.";
		if (name.equals(ErrorLabel.WARNING) || name.equals(ErrorLabel.WARNING.toString())) return "Aviso";

		return name.toString();
	}

	@Override
	public String translateHTML(String html) {
		RegExp regExp = RegExp.compile("::([^:]*)::", "g");

		for (MatchResult matcher = regExp.exec(html); matcher != null; matcher = regExp.exec(html)) {
			String name = matcher.getGroup(1);
			html = html.replace("::" + name + "::", translate(name));
		}

		return html;
	}

	@Override
	public int dayOfWeekToNumber(DayOfWeek dayOfWeek) {
		if (dayOfWeek == DayOfWeek.MONDAY) return 0;
		if (dayOfWeek == DayOfWeek.TUESDAY) return 1;
		if (dayOfWeek == DayOfWeek.WEDNESDAY) return 2;
		if (dayOfWeek == DayOfWeek.THURSDAY) return 3;
		if (dayOfWeek == DayOfWeek.FRIDAY) return 4;
		if (dayOfWeek == DayOfWeek.SATURDAY) return 5;
		if (dayOfWeek == DayOfWeek.SUNDAY) return 6;
		return -1;
	}

	@Override
	public int monthToNumber(String month) {
		if (month.equalsIgnoreCase("Enero") || month.equalsIgnoreCase("Ene")) return 1;
		if (month.equalsIgnoreCase("Febrero") || month.equalsIgnoreCase("Feb")) return 2;
		if (month.equalsIgnoreCase("Marzo") || month.equalsIgnoreCase("Mar")) return 3;
		if (month.equalsIgnoreCase("Abril") || month.equalsIgnoreCase("Abr")) return 4;
		if (month.equalsIgnoreCase("Mayo") || month.equalsIgnoreCase("May")) return 5;
		if (month.equalsIgnoreCase("Junio") || month.equalsIgnoreCase("Jun")) return 6;
		if (month.equalsIgnoreCase("Julio") || month.equalsIgnoreCase("Jul")) return 7;
		if (month.equalsIgnoreCase("Agosto") || month.equalsIgnoreCase("Ago")) return 8;
		if (month.equalsIgnoreCase("Septiembre") || month.equalsIgnoreCase("Sep")) return 9;
		if (month.equalsIgnoreCase("Octubre") || month.equalsIgnoreCase("Oct")) return 10;
		if (month.equalsIgnoreCase("Noviembre") || month.equalsIgnoreCase("Nov")) return 11;
		if (month.equalsIgnoreCase("Diciembre") || month.equalsIgnoreCase("Dic")) return 12;
		return -1;
	}

	@Override
	public String monthNumberToString(Integer month) {
		if (month == 1) return "Enero";
		if (month == 2) return "Febrero";
		if (month == 3) return "Marzo";
		if (month == 4) return "Abril";
		if (month == 5) return "Mayo";
		if (month == 6) return "Junio";
		if (month == 7) return "Julio";
		if (month == 8) return "Agosto";
		if (month == 9) return "Septiembre";
		if (month == 10) return "Octubre";
		if (month == 11) return "Noviembre";
		if (month == 12) return "Diciembre";
		return "";
	}

	@Override
	public String[] getDateSeparators() {
		return new String[]{"de", "del", "/", " ", ":", "a", "las"};
	}

	@Override
	public String translateDateWithPrecision(Date date, DatePrecision precision) {
		if (precision == DatePrecision.CENTURY)
			return date.getCentury().toString();
		if (precision == DatePrecision.DECADE)
			return date.getDecade().toString();
		if (precision == DatePrecision.YEAR)
			return String.valueOf(date.getYear());
		if (precision == DatePrecision.MONTH)
			return monthNumberToString(date.getMonth()) + " de " + date.getYear();
		return date.getDay() + " de " + monthNumberToString(date.getMonth()) + " de " + date.getYear();
	}

	@Override
	public String translateFullDate(Date date) {
		if (date == null)
			return "";

		return DateFormatter.format(date, DateFormatter.Format.SECONDS);
	}

	@Override
	public String translateFullDateByUser(Date date, String user) {
		return translateFullDate(date) + ((user!=null && !user.isEmpty())?" por " + user:"");
	}

	@Override
	public String getLoadingLabel() {
		return "cargando...";
	}

	@Override
	public String getNoPhoto() {
		return getConfiguration().getImagesPath() + "/nopicture.jpg";
	}

	@Override
	public String getAddPhoto() {
		return getConfiguration().getImagesPath() + "/addphoto.jpeg";
	}

	@Override
	public String translateSavedPeriodAgo(String message, Date date) {
		return message + " guardado hace " + translateCountDate(date);
	}

	@Override
	public String translateSearchForCondition(String condition) {
		return "resultados de búsqueda para <span style=\"font-style:italic\">" + SafeHtmlUtils.htmlEscape(condition) + "</span>";
	}

	@Override
	public String getCountLabel(int count) {
		if (count == 0) return "";
		if (count == 1) return "1 elemento";
		return count + " elementos";
	}

	@Override
	public String getSelectionCountLabel(int selectionCount) {
		if (selectionCount == 0) return "ningún elemento seleccionado";
		if (selectionCount == 1) return "1 elemento seleccionado";
		if (selectionCount > 1 && selectionCount < 10) return selectionCount + " elementos seleccionados";
		return "muchos elementos seleccionados";
	}

	@Override
	public String translateCountDate(Date date) {
		Date current = new Date();
		long difference = current.getMilliseconds()-date.getMilliseconds();

		if (difference < 60000)
			return "menos de un minuto";
		else if (difference >= 60000 && difference < 60000*2)
			return "un minuto";
		else if (difference < 60000*60)
			return difference/60000 + " minutos";

		return difference/(60000*60) + " horas";
	}
}
