package grailscrowd.core.message
/**
 * @author ap
 */
class MailerJobConfig implements MailerJobConfigMBean{
    
    int startDelay = 6000
    
    int timeout = 60000     // execute job every minute

    private boolean enabled = true

    /** internal name of job. must be unique within group!*/
    def jobName
    
    /** internal group of jobs name. */
    def jobGroup = "mailerGroup"

    def quartzScheduler

    public int getStartDelayInMillis(){
        return startDelay
    }

    public void setStartDelayInMillis(int value){
        this.startDelay=value
    }

    public int getTimeoutInMillis(){
        return timeout
    }

    public void setTimeoutInMillis(int value){
         this.timeout = value
    }
    public void setEnabled(boolean value){
        this.enabled = value
    }
    
    public boolean isJobSchedulingEnabled(){
        return enabled
    }

    public void disableJobScheduling(){
        enabled = false
        quartzScheduler.pauseJob(jobName, jobGroup)
    }

    public void enableJobScheduling(){
        enabled = true
        quartzScheduler.resumeJob(jobName, jobGroup)
    }
}