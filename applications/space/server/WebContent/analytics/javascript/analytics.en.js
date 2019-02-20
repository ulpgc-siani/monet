var AppTemplate=new Array;
var Lang=new Array;
var ToolbarDefinition=new Array;
var MenuDefinition=new Array;
var Path=new Array;

Lang = {
	ConnectionFailure: "Connection failure",
	SessionExpired: "Session expired",
	BusinessUnitStopped: "Space stopped",
	InitApplication: "Error starting application",
	Error: "Error",
	Warning: "Warning",
	Information: "Information",
	Loading: "loading...",

	Buttons: {
		Accept: "Accept",
		Replace: "Replace",
		Cancel: "Cancel"
	},

	Desktop: {
		Title: "Goto home",
		Loading: "loading..."
	},

	DateFormats: {
		0: "Y", // year
		1: "M Y", // month
		2: "d M Y", // day
		3: "d M Y \\a\\t H:00:00", // hour
		4: "d M Y \\a\\t H:i:00", // minute
		5: "d M Y \\a\\t H:i:s" // second
	},

	LayoutLeft: {
	},

	LayoutMain: {
	},

	ViewDashboard: {
		ShowHome: "goto home",
		Starting: "Loading. Please, waiting...",
		NotLoaded: "Dashboard not loaded yet",
		LineChart: "Linear",
		BarChart: "Bars",
		Table: "Table",
		BubbleChart: "Bubbles",
		MapChart: "Map",
		Helper: "Assistant",
		Print: "Print view",
		Download: "Download",
		Reload : "retry again"
	},

	ViewDashboardInfo: {
		Delete: "delete"
	},

	ViewIndicators: {
		Title: "- indicators",
		Loading: "loading indicators",
		ClearIndicators: "clear selection"
	},

	ViewMeasureUnits: {
		Title: "+ metrics",
		Mode: "mode",
		ModeAbsolute: "absolute",
		ModeRelative: "relative",
		Ranges: "min/max values",
		RangeMinValue: "minimum value",
		RangeMaxValue: "maximum value"
	},

	ViewTaxonomies: {
		CompareBy: "+ analyze by",
		Filters: "+ filters",
		NoFilters: "no available filters",
		SelectCompare: "select one",
		ClearCompare: "clear selection",
		ClearFilters: "clear selection",
		SelectAllFilters: "select all"
	},

	ViewChart: {
		Loading: "loading chart...",
		SelectIndicator: "Select an indicator to render the graph",
		NotEnoughSelectedIndicators: "Select other indicator",
		TooMuchSelectedIndicators: "In this view you can only select %d indicators. Unselect indicators from indicators panel.",
		TooMuchSelectedMetrics: "In this view you can only select %d metrics. Unselect indicators from indicators panel.",
		IndicatorNotVisiblePart1: "is not visible with this resolution level",
		IndicatorNotVisiblePart1Multiple: "are not visible with this resolution level",
		IndicatorNotVisiblePart2: "Change",
		IndicatorNotVisiblePart2Multiple: "Change",
		IndicatorNotVisiblePart3: "resolution to see indicator",
		IndicatorNotVisiblePart3Multiple: "resolution to see indicators",
		Details: "details",
		ComingSoon: "coming soon...",
		EmptyChart: "no data in this chart"
	},

	ViewAccount: {
		Home: "home",
		GotoHome: "go home",
		CloseSessionFor: "Close session of",
		User: "User name",
		Environments: "Desktops",
		Dashboards: "Dashboards",
		Units: "Business units",
		Current: "(current)",
		StartSessionWith: "start session in",
		GotoUnit: "go",
		Logout: "logout",
		SendSuggestion: "suggestions"
	},

	DialogTimeLapse: {
		CurrentPeriod: "Current period",
		Play: "play",
		Pause: "pause",
		Next: "next",
		Last: "last",
		First: "first",
		Previous: "previous"
	},

	DialogScale: {
		ScaleSecond: "second",
		ScaleMinute: "minute",
		ScaleHour: "hour",
		ScaleDay: "day",
		ScaleMonth: "month",
		ScaleYear: "year"
	},

	DialogMapLayer: {
		LayerPoint: "Points",
		LayerHeat: "Heat"
	},

	DialogRange: {
		Play: "play",
		Pause: "pause"
	},

	DialogColors: {
	},

	DialogZoom: {
		ZoomIn: "zoom in",
		RestoreZoom: "restore"
	}

};

