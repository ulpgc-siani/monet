ServerStateSerializer = {
    
  unserialize : function(jsonServerState) {    
    var serverState = new ServerState();
    
    var jsonPerformance = jsonServerState.performance;
    var jsonTask   = jsonServerState.task;
    var jsonMemory = jsonServerState.memory;
    var jsonSwap   = jsonServerState.swap;
    
    serverState.set(ServerState.LOG, jsonServerState.log);
    serverState.set(ServerState.PERFORMANCE, {load : jsonPerformance.load, cpu : jsonPerformance.cpu, users : jsonPerformance.users});
    serverState.set(ServerState.TASK, {total : jsonTask.total, execution : jsonTask.execution, sleeped : jsonTask.sleeped, stopped : jsonTask.stopped, zoombies : jsonTask.zoombies});
    serverState.set(ServerState.MEMORY, {total : jsonMemory.total, used : jsonMemory.used, available : jsonMemory.available});
    serverState.set(ServerState.SWAP, {total : jsonSwap.total, used: jsonSwap.used, available : jsonSwap.available});
        
    return serverState;
  }      
};