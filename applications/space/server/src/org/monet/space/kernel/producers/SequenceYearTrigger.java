package org.monet.space.kernel.producers;

import org.quartz.JobExecutionContext;

public class SequenceYearTrigger implements org.quartz.Job {

	@Override
	public void execute(JobExecutionContext context) {
		ProducerSequence producer = (ProducerSequence) context.getMergedJobDataMap().get("producer");
		producer.resetYearSequences();
	}

}
