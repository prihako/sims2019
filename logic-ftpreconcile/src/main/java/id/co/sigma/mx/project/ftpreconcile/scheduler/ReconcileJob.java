package id.co.sigma.mx.project.ftpreconcile.scheduler;

import id.co.sigma.mx.project.ftpreconcile.process.ProcessorPayment;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ReconcileJob extends QuartzJobBean{

	private ProcessorPayment processorPayment;
	protected static transient Logger logger = Logger.getLogger(ReceiverFtpJob.class);
	private static boolean isRunning = false;
//	final int[] forbiddenHours = {4,5};

	public void setProcessorPayment(ProcessorPayment processorPayment) {
		this.processorPayment = processorPayment;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Calendar startDate = Calendar.getInstance();

		if (!isRunning) {
			isRunning = true;
		} else {
			logger.info("Previous Payment Processing still running at "
					+ startDate.getTime().toString());
			return;
		}
		while (isRunning) {
			try {
//				if(isAllowedHours(startDate)){
					logger.info("Payment Processing starts at: ".concat(startDate
							.getTime().toString()));

					processorPayment.execute();

					Calendar endDate = Calendar.getInstance();
					logger.info("Payment Processing completes at: " + endDate.getTime()
							+ ", takes "
							+ (endDate.getTimeInMillis() - startDate.getTimeInMillis())
							+ " ms");
//				}
				isRunning = false;
			} catch (Exception e) {
				e.printStackTrace();
				logger.fatal(e);
				isRunning = false;
				break;
			}
		}
	}

//	private boolean isAllowedHours(Calendar startDate) {
//		boolean result = true;
//		int currentHour = startDate.get(Calendar.HOUR_OF_DAY);
//		for (int i = 0; i < forbiddenHours.length; i++) {
//			if(currentHour == forbiddenHours[i]){
//				result = false;
//				break;
//			}
//		}
//		return result;
//	}
}
