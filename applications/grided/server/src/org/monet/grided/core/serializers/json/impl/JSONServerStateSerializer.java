package org.monet.grided.core.serializers.json.impl;

import net.sf.json.JSONObject;

import org.monet.api.deploy.setupservice.impl.model.ServerState;
import org.monet.api.deploy.setupservice.impl.model.ServerState.Memory;
import org.monet.api.deploy.setupservice.impl.model.ServerState.Performance;
import org.monet.api.deploy.setupservice.impl.model.ServerState.Swap;
import org.monet.api.deploy.setupservice.impl.model.ServerState.Task;
import org.monet.grided.constants.PropertyNames;
import org.monet.grided.core.serializers.json.JSONSerializer;

public class JSONServerStateSerializer implements JSONSerializer<JSONObject, ServerState> {

    @Override
    public JSONObject serialize(ServerState serverState) {
        JSONObject json = new JSONObject();

        json.put(PropertyNames.LOG, serverState.getLog());

        Performance performance = serverState.getPerformance();
        JSONObject jsonPerformance = new JSONObject();

        jsonPerformance.put(PropertyNames.LOAD, performance.getLoad());
        jsonPerformance.put(PropertyNames.CPU, performance.getCPU());
        jsonPerformance.put(PropertyNames.USERS, performance.getUsers());

        json.put(PropertyNames.PERFORMANCE, jsonPerformance);

        Task task = serverState.getTask();
        JSONObject jsonTask = new JSONObject();

        jsonTask.put(PropertyNames.TOTAL, task.getTotal());
        jsonTask.put(PropertyNames.EXECUTION, task.getExecution());
        jsonTask.put(PropertyNames.SLEEPED, task.getSleeped());
        jsonTask.put(PropertyNames.ZOOMBIES, task.getZoombies());

        json.put(PropertyNames.TASK, jsonTask);

        Memory memory = serverState.getMemory();
        JSONObject jsonMemory = new JSONObject();

        jsonMemory.put(PropertyNames.TOTAL, memory.getTotal());
        jsonMemory.put(PropertyNames.USED, memory.getUsed());
        jsonMemory.put(PropertyNames.AVAILABLE, memory.getAvailable());

        json.put(PropertyNames.MEMORY, jsonMemory);

        Swap swap = serverState.getSwap();
        JSONObject jsonSwap = new JSONObject();

        jsonSwap.put(PropertyNames.TOTAL, swap.getTotal());
        jsonSwap.put(PropertyNames.USED, swap.getUsed());
        jsonSwap.put(PropertyNames.AVAILABLE, swap.getAvailable());

        json.put(PropertyNames.SWAP, jsonSwap);

        return json;        
    }

    @Override
    public ServerState unserialize(String json) {
        return null;
    }
}

