
var Console = new Object;
Console.STOP = 0;
Console.RUNNING = 1;

Console.init = function() {	
	Console.stadistics = {};
	Console.state = Console.STOP; 
	Console.currentUsers = [];
};
Console.init();
