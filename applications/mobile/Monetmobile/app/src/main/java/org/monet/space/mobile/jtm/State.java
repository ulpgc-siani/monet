package org.monet.space.mobile.jtm;

import org.monet.mobile.model.TaskDefinition;
import org.monet.mobile.model.TaskDefinition.Step;
import org.monet.space.mobile.model.schema.Schema;

public class State {

    public int CurrentStepIndex = 0;
    public int MultipleStepIteration = 0;
    public long SourceId;
    public long DefinitionId;
    public String TaskContext;
    public TaskDefinition Definition;
    public String Context;
    public Schema Schema;
    public Schema CurrentStepSchema;
    public String taskLabel;


    private int getSchemaListSize(String stepName) {
        return this.Schema.getSchemaList(stepName).size();
    }

    public Step getCurrentStep() {
        return this.Definition.stepList.get(this.CurrentStepIndex);
    }

    public boolean canGoNext() {
        Step currentStep = getCurrentStep();
        return (this.CurrentStepIndex < (this.Definition.stepList.size() - 1)) ||
                (currentStep.isMultiple && (this.MultipleStepIteration < (this.getSchemaListSize(currentStep.name) - 1)));
    }

    public void goNext() {
        Step currentStep = getCurrentStep();
        if (currentStep.isMultiple && (this.MultipleStepIteration < (this.getSchemaListSize(currentStep.name) - 1))) {
            this.MultipleStepIteration++;
        } else {
            this.MultipleStepIteration = 0;
            if (this.CurrentStepIndex < (this.Definition.stepList.size() - 1))
                this.CurrentStepIndex++;
        }
    }

    public boolean canGoBack() {
        return (this.CurrentStepIndex > 0) ||
                (getCurrentStep().isMultiple && (this.MultipleStepIteration > 0));
    }

    public void goBack() {
        Step currentStep = getCurrentStep();
        if (currentStep.isMultiple && (this.MultipleStepIteration > 0)) {
            MultipleStepIteration--;
        } else {
            if (this.CurrentStepIndex > 0)
                this.CurrentStepIndex--;

            Step newCurrentStep = getCurrentStep();
            if (newCurrentStep.isMultiple) {
                for (int i = 0; i< this.Schema.getSchemaList(newCurrentStep.name).size();i++) {
                    Schema schema = this.Schema.getSchemaList(newCurrentStep.name).get(i);
                    if (schema.getBoolean("IsLastStep") || schema.getBoolean("IsLastStep" + newCurrentStep.name) == true) {
                        MultipleStepIteration = i;
                        return;
                    }
                }
                MultipleStepIteration = getSchemaListSize(newCurrentStep.name) - 1;
                if (MultipleStepIteration < 0)
                    MultipleStepIteration = 0;
            }
        }
    }
}
