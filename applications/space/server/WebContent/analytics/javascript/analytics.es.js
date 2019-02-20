var AppTemplate=new Array;
var Lang=new Array;
var ToolbarDefinition=new Array;
var MenuDefinition=new Array;
var Path=new Array;

Lang = {
	ConnectionFailure: "Fallo de conexión",
	SessionExpired: "La sesión ha caducado",
	BusinessUnitStopped: "El espacio ha sido parado",
	InitApplication: "Error al iniciar la aplicación",
	Error: "Error",
	Warning: "Advertencia",
	Information: "Información",
	Loading: "cargando...",

	Buttons: {
		Accept: "Aceptar",
		Replace: "Reemplazar",
		Cancel: "Cancelar"
	},

	Desktop: {
		Title: "Ir al inicio",
		Loading: "cargando..."
	},

	DateFormats: {
		0: "Y", // year
		1: "M Y", // month
		2: "d M Y", // day
		3: "d M Y \\a \\l\\a\\s H:00:00", // hour
		4: "d M Y \\a \\l\\a\\s H:i:00", // minute
		5: "d M Y \\a \\l\\a\\s H:i:s" // second
	},

	LayoutLeft: {
	},

	LayoutMain: {
	},

	ViewDashboard: {
		ShowHome: "ir al inicio",
		Starting: "Cargando. Por favor, espere...",
		NotLoaded: "No se ha terminado de cargar el dashboard",
		LineChart: "Lineal",
		BarChart: "Barras",
		Table: "Tabla",
		BubbleChart: "Disperso",
		MapChart: "Mapa",
		Helper: "Asistente",
		Print: "Vista de impresión",
		Download: "Descargar",
		Reload: "volver a intentar"
	},

	ViewDashboardInfo: {
		Delete: "quitar"
	},

	ViewIndicators: {
		Title: "- indicadores",
		Loading: "cargando indicadores",
		ClearIndicators: "quitar selección"
	},

	ViewMeasureUnits: {
		Title: "+ métricas",
		Mode: "modo",
		ModeAbsolute: "absoluto",
		ModeRelative: "relativo",
		Ranges: "valores mínimo y máximo",
		RangeMinValue: "cota inferior",
		RangeMaxValue: "cota superior"
	},

	ViewTaxonomies: {
		CompareBy: "+ analizar por",
		Filters: "+ filtros",
		NoFilters: "no hay filtros disponibles",
		SelectCompare: "seleccione uno",
		ClearCompare: "quitar selección",
		ClearFilters: "quitar selección",
		SelectAllFilters: "seleccionar todos"
	},

	ViewChart: {
		Loading: "cargando la gráfica...",
		SelectIndicator: "Seleccione al menos un indicador para mostrar la gráfica",
		NotEnoughSelectedIndicators: "Seleccione otro indicador",
		TooMuchSelectedIndicators: "En esta vista solo se permiten %d indicadores.<br/>Quite indicadores del panel de indicadores.",
		TooMuchSelectedMetrics: "En esta vista solo se permiten %d métricas.<br/>Quite indicadores del panel de indicadores.",
		IndicatorNotVisiblePart1: "no se puede ver con este nivel de resolución",
		IndicatorNotVisiblePart1Multiple: "no se pueden ver con este nivel de resolución",
		IndicatorNotVisiblePart2: "Cambiar",
		IndicatorNotVisiblePart2Multiple: "Cambiar",
		IndicatorNotVisiblePart3: "de escala para verlo",
		IndicatorNotVisiblePart3Multiple: "de escala para verlos",
		Details: "detalles",
		ComingSoon: "en breve...",
		EmptyChart: "no hay datos en la gráfica"
	},

	ViewAccount: {
		Home: "inicio",
		GotoHome: "ir al inicio",
		CloseSessionFor: "Cerrar sesión de",
		User: "Nombre de usuario",
		Environments: "Escritorios",
		Dashboards: "Paneles de control",
		Units: "Unidades de negocio",
		Current: "(actual)",
		StartSessionWith: "iniciar sesión en",
		GotoUnit: "ir a",
		Logout: "cerrar sesión"
	},

	DialogTimeLapse: {
		CurrentPeriod: "Periodo actual",
		Play: "reproducir",
		Pause: "detener",
		Next: "siguiente",
		Last: "último",
		First: "primero",
		Previous: "anterior"
	},

	DialogScale: {
		ScaleSecond: "segundo",
		ScaleMinute: "minuto",
		ScaleHour: "hora",
		ScaleDay: "día",
		ScaleMonth: "mes",
		ScaleYear: "año"
	},

	DialogMapLayer: {
		LayerPoint: "Puntos",
		LayerHeat: "Calor"
	},

	DialogRange: {
		Play: "play",
		Pause: "pause"
	},

	DialogColors: {
	},

	DialogZoom: {
		ZoomIn: "acercar",
		RestoreZoom: "restaurar"
	}

};

